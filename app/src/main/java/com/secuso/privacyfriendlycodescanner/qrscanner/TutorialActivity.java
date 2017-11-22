package com.secuso.privacyfriendlycodescanner.qrscanner;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TutorialActivity extends AppCompatActivity implements  View.OnClickListener {

    private ViewPager tPager; //new
    private int [] layouts ={R.layout.tutorial_slide1,R.layout.tutorial_slide2,R.layout.tutorial_slide3};//new
    private TpagerAdapter tpagerAdapter;// new

    private LinearLayout Dots_Layout; //
    private ImageView[] dots; //
    private Button bNext, bSkip;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_tutorial);

        if(new PreferenceManager(this).checkPreference())
        {
            loadMain();
        }


        if(Build.VERSION.SDK_INT >=19){

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// to make slides transparent
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }


        tPager=(ViewPager)  findViewById(R.id.viewPager); //
        tpagerAdapter = new TpagerAdapter(layouts,this);//
        tPager.setAdapter(tpagerAdapter); //
        Dots_Layout=(LinearLayout) findViewById(R.id.dotsLayout);//
        bNext=(Button) findViewById(R.id.btNext);
        bSkip=(Button) findViewById(R.id.btSkip);

        bNext.setOnClickListener(this);
        bSkip.setOnClickListener(this);
        creatDots(0); //

        tPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { //

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {

                creatDots(position);
                if (position==layouts.length-1)
                {
                    bNext.setText("Start");
                    bSkip.setVisibility(View.INVISIBLE);
                }
                else
                {
                    bNext.setText("Next");
                    bSkip.setVisibility(View.VISIBLE);

                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });
    }
    public void creatDots(int current_position)  //

    {
        if(Dots_Layout!=null)
            Dots_Layout.removeAllViews();

        dots= new  ImageView[layouts.length];
        for (int i=0;i<layouts.length; i++)
        {
            dots[i]= new ImageView(this);
            if (i==current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
            }
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            Dots_Layout.addView(dots[i], params);
        }



    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btNext:
                loadNextSlide();
                break;

            case R.id.btSkip:
                loadMain();
                new PreferenceManager(this).writePreference();
                break;


        }

    }
    private  void loadMain()
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
    private void loadNextSlide()
    {
        int next_slide= tPager.getCurrentItem()+1;
        if (next_slide<layouts.length)
        {
            tPager.setCurrentItem(next_slide);
        }
        else
        {
            loadMain();
            new PreferenceManager(this).writePreference();
        }

    }
}
