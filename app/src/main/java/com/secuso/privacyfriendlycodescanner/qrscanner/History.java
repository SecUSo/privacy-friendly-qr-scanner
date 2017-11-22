package com.secuso.privacyfriendlycodescanner.qrscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    ListView list_view;
    ArrayAdapter<String> adapter;
    ArrayList<String> list=new ArrayList<String>();
    ArrayList<String> list_items=new ArrayList<String>();
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        list_view= findViewById(R.id.list_view);
        list.add("one");
        list.add("two");

        adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        list_view.setAdapter(adapter);
        list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list_view.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {


                    count = count + 1;
                    mode.setTitle(count + "items selected");
                    list_items.add(list.get(position));





            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                MenuInflater inflater=mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu,menu);



                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){

                    case R.id.delete_id:
                        for(String msg:list_items)
                        {
                            adapter.remove(msg);

                        }
                        Toast.makeText(getBaseContext(),count +"items removed",Toast.LENGTH_SHORT).show();
                        count=0;
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

            }
        });
    }
}
