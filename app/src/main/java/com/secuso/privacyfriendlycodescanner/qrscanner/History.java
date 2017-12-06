package com.secuso.privacyFriendlyCodeScanner.qrscanner;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.secuso.privacyFriendlyCodeScanner.qrscanner.DataBase.DBHandler;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    ListView list_view;
    ArrayAdapter<String> adapter;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list_items = new ArrayList<String>();
    int count = 0;
    DBHandler dbHandler;/////////////DB
    ResultActivity resultActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHandler = new DBHandler(this, null, null, 1);////////////////DB

        list_view = findViewById(R.id.list_view);


        Cursor data = dbHandler.getListContents();///////////DB///////////////////////////////////
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in history!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(1));
                ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                list_view.setAdapter(adapter);

            }
        }/////////////////////////////////////////////////////////////////


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        list_view.setAdapter(adapter);
        list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String val =(String) parent.getItemAtPosition(position);
               //resultActivity.checkResult(val);
                Intent st= new Intent(History.this,ResultActivity.class);
               st.putExtra("QrHistory",val);
                startActivity(st);

            }
        });


        list_view.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {


                count = count + 1;
                if (count == 1)
                    mode.setTitle(count + " item selected");
                else
                    mode.setTitle(count + " items selected");
                list_items.add(list.get(position));


            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);


                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.delete_id:
                        for (String msg : list_items) {
                            adapter.remove(msg);
                            dbHandler.deleteContent(msg);///////////DB

                        }

                        if (count == 1) {
                            Toast.makeText(getBaseContext(), count + " item removed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(), count + " items removed", Toast.LENGTH_SHORT).show();
                        }
                        count = 0;
                        mode.finish();
                        return true;
                    //break;
                    default:
                        return false;
                }
                // return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

                mode = null;
                count = 0;

            }
        });

    }
}
