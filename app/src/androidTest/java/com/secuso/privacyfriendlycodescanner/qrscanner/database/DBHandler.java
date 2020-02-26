package com.secuso.privacyfriendlycodescanner.qrscanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@Deprecated
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DB.db";
    public static final String TABLE_NAMES = "contents";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "content";

    //We need to pass database information along to superclass

    public DBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public DBHandler(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAMES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES);
        onCreate(db);
    }

    //Add a new row to the database
    public void addContent(ScannedData content){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, content.get_name());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAMES, null, values);
        db.close();
    }

    //Delete a content from the database
    public void deleteContent(String Content){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAMES + " WHERE " + COLUMN_NAME + "=\"" + Content + "\";");
        db.close();
    }

    public void deleteAllContents(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAMES );
        db.close();
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAMES, null);
        return data;
    }

   /* public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAMES + " WHERE 1";

        //Cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        //Position after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("content")) != null) {
                dbString += c.getString(c.getColumnIndex("content"));
                dbString += "\n";
            }
            c.moveToNext();

            db.close();
            return c;
        }
    } */
}