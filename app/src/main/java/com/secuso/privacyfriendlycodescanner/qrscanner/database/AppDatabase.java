package com.secuso.privacyfriendlycodescanner.qrscanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.zxing.BarcodeFormat;
import com.secuso.privacyfriendlycodescanner.qrscanner.helpers.Utils;

import java.util.GregorianCalendar;

/**
 * This is the room database for this app.
 *
 * @author Christopher Beckmann
 */
@Database(entities = {HistoryItem.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "DB.db";

    public abstract HistoryDao historyDao();

    private static AppDatabase INSTANCE;

    public static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `Histories` (" +
                    "`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`text` TEXT NOT NULL," +
                    "`image` TEXT," +
                    "`rawBytes` BLOB," +
                    "`numBits` INTEGER NOT NULL DEFAULT 0," +
                    "`timestamp` INTEGER NOT NULL DEFAULT 0," +
                    "`format` TEXT," +
                    "`resultPoints` TEXT)");
            database.execSQL("INSERT INTO Histories(text) SELECT content FROM contents");
            database.execSQL("DROP TABLE contents;");

            HistoryItem[] items = null;
            Cursor c = database.query("SELECT * FROM Histories");
            if(c != null) {
                if(c.moveToFirst()) {
                    items = new HistoryItem[c.getCount()];
                    int i = 0;

                    while (!c.isAfterLast()) {
                        items[i] = new HistoryItem();
                        items[i].set_id(c.getInt(c.getColumnIndex("_id")));
                        items[i].setText(c.getString(c.getColumnIndex("text")));
                        items[i].setImage(Utils.generateCode(items[i].getText(), BarcodeFormat.QR_CODE, null));

                        i++;
                        c.moveToNext();
                    }
                }
                c.close();
            }

            if(items != null) {
                for(HistoryItem item : items) {
                    ContentValues cv = new ContentValues();
                    cv.put("_id", item.get_id());
                    cv.put("text", item.getText());
                    cv.put("image", Converters.encodeImage(item.getImage()));
                    cv.put("timestamp", GregorianCalendar.getInstance().getTimeInMillis());
                    cv.put("format", BarcodeFormat.QR_CODE.name());
                    database.update("Histories", 0, cv, "_id = ?", new String[]{Integer.toString(item.get_id())});
                }
            }
        }
    };
    public static final Migration MIGRATION_2_1 = new Migration(2,1) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE contents (_id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT);");
            database.execSQL("INSERT INTO contents (content) SELECT text FROM Histories");
            database.execSQL("DROP TABLE Histories;");
        }
    };


    public static final Migration[] MIGRATIONS = {
            MIGRATION_1_2,
            MIGRATION_2_1
    };

    public synchronized static AppDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .addMigrations(MIGRATIONS)
                .fallbackToDestructiveMigration()
                .build();
    }


}
