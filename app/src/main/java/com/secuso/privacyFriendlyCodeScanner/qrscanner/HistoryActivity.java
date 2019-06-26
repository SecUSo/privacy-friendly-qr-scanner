package com.secuso.privacyFriendlyCodeScanner.qrscanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.secuso.privacyFriendlyCodeScanner.qrscanner.DataBase.DBHandler;
import com.secuso.privacyFriendlyCodeScanner.qrscanner.helpers.MyListViewAdapter;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ListView list_view;
    // ArrayAdapter<String> adapter;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list_items = new ArrayList<String>();
    int count = 0;
    String  selecteditem;
    MyListViewAdapter adapter;
    DBHandler dbHandler;/////////////DB
    ResultActivity resultActivity;


    private SparseBooleanArray mSelectedItemsIds;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHandler = new DBHandler(this, null, null, 1);////////////////DB

        list_view = findViewById(R.id.list_view);


        Cursor data = dbHandler.getListContents();///////////DB///////////////////////////////////
        if (data.getCount() == 0) {
            Toast.makeText(this, R.string.no_content_in_history, Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                list.add(0,data.getString(1));// Add item to top of arraylist
                // ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
                adapter = new  MyListViewAdapter(this, R.layout.list_item, list);
                list_view.setAdapter(adapter);

            }
        }/////////////////////////////////////////////////////////////////

        // adapter = new  MyListViewAdapter(this, R.layout.list_item, list); // new


        // adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);


        list_view.setAdapter(adapter);
        list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String val =(String) parent.getItemAtPosition(position);
                //resultActivity.checkResult(val);
                Intent st= new Intent(HistoryActivity.this,ResultActivity.class);
                st.putExtra("QrHistory",val);
                startActivity(st);

            }
        });


        list_view.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {



            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {




                // TODO  Auto-generated method stub

                final int checkedCount  = list_view.getCheckedItemCount();

                // Set the  CAB title according to total checked items

                mode.setTitle(checkedCount  + getString(R.string.selected));

                // Calls  toggleSelection method from ListViewAdapter Class

                adapter.toggleSelection(position);
                list_items.add(list.get(position));




            }
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                // MenuInflater inflater = mode.getMenuInflater();
                // inflater.inflate(R.menu.context_menu, menu);

                mode.getMenuInflater().inflate(R.menu.context_menu, menu);

                return true;



                // return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }


            @Override

            public boolean  onActionItemClicked(final ActionMode mode, MenuItem item) {
                // TODO  Auto-generated method stub
                switch  (item.getItemId()) {
                    case R.id.selectAll:
                        final int checkedCount  = list.size();

                        // If item  is already selected or checked then remove or
                        // unchecked  and again select all
                        adapter.removeSelection();

                        for (int i = 0; i <  checkedCount; i++) {
                            list_view.setItemChecked(i,true);
                            //  listviewadapter.toggleSelection(i);
                        }

                        // Set the  CAB title according to total checked items
                        // Calls  toggleSelection method from ListViewAdapter Class
                        // Count no.  of selected item and print it

                        mode.setTitle(checkedCount  + getString(R.string.selected_1));

                        return true;



                    case R.id.delete_id:
                        // Add  dialog for confirmation to delete selected item
                        // record.
                        AlertDialog.Builder  builder = new AlertDialog.Builder(HistoryActivity.this);
                        builder.setMessage(R.string.Del);
                        builder.setNegativeButton(R.string.no,  null);
                        builder.setPositiveButton(R.string.yes, new  DialogInterface.OnClickListener() {



                            @Override

                            public void  onClick(DialogInterface dialog, int which) {
                                SparseBooleanArray  selected = adapter.getSelectedIds();

                                for (int i =  (selected.size() - 1); i >= 0; i--) {
                                    if  (selected.valueAt(i)) {
                                        selecteditem = adapter.getItem(selected.keyAt(i));

                                        // Remove  selected items following the ids
                                        adapter.remove(selecteditem);
                                        dbHandler.deleteContent(selecteditem);

                                        //Toast.makeText(getBaseContext(),selecteditem, Toast.LENGTH_SHORT).show();
                                        dbHandler.close();
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                                // Close CAB
                                mode.finish();
                                selected.clear();
                            }


                        });

                        AlertDialog alert =  builder.create();

                        // alert.setIcon(R.drawable.questionicon);// dialog  Icon

                        alert.setTitle(getString(R.string.confirmation)); // dialog  Title

                        alert.show();

                        return true;

                    default:

                        return false;

                }



            }




            @Override
            public void onDestroyActionMode(ActionMode mode) {

                mode = null;
                //  count = 0;

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {




        switch (item.getItemId()){

            case R.id.action_clear:


                new AlertDialog.Builder(this)
                        .setTitle(R.string.D_all)
                        .setMessage(R.string.all_records)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dbHandler.deleteAllContents();///////////DB

                                dbHandler.close();
                                list.clear();
                                adapter.notifyDataSetChanged();


                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;


            default:
                return false;
        }
    }


}

