package com.secuso.privacyFriendlyCodeScanner.qrscanner.QRGenerating;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.MainActivity;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class GeoLocatioGnrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_locatio_gnr);

        Button btnstore = (Button) findViewById(R.id.btnstore);

        Bundle QRData = getIntent().getExtras();//from QRGenerator
        final String qrInputText = QRData.getString("gn");


        Bitmap bitmap=createBitmap(qrInputText);

        ImageView myImage = (ImageView) findViewById(R.id.imageView1);
        myImage.setImageBitmap(bitmap );


        btnstore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle QRData = getIntent().getExtras();//from QRGenerator
                final String qrInputText = QRData.getString("gn");


                Bitmap bitmap=createBitmap(qrInputText);
                saveImageToExternalStorage(bitmap);

                Intent i=new Intent(GeoLocatioGnrActivity.this, MainActivity.class);
                startActivity(i);
                Toast.makeText(GeoLocatioGnrActivity.this, "QR code stored in gallery", Toast.LENGTH_LONG).show();




            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share,menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()){
            case R.id.share:
                Bundle QRData = getIntent().getExtras();//from QRGenerator
                final String qrInputText = QRData.getString("gn");
                Bitmap bitmap=createBitmap(qrInputText);
                shareIt(bitmap);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void shareIt(Bitmap result)
    {
        try {
            File file = new File(this.getExternalCacheDir(),"logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            result.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    private void saveImageToExternalStorage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

    }
    public Bitmap createBitmap (String qrInputText) {

        //Find screen size
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText,
                null,
                Contents.Type.LOCATION,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        Bitmap bitmap_ = null;
        try {
            bitmap_ = qrCodeEncoder.encodeAsBitmap();
            // return bitmap_;

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap_;
    }
}

