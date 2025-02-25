package com.secuso.privacyfriendlycodescanner.qrscanner;

import android.content.Context;

import androidx.room.Room;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.secuso.privacyfriendlycodescanner.qrscanner.database.AppDatabase;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.DBHandler;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.ScannedData;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * Tests the database migration.
 *
 * @author Christopher Beckmann
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseMigrationInstrumentedTest {
    public static final String TEST_DB_NAME = "test_database.db";

    public static final ScannedData data1 = new ScannedData("This is an example Text");
    public static final ScannedData data2 = new ScannedData("smsto:+491725555555:This is a test message.");
    public static final ScannedData data3 = new ScannedData("WIFI:S:NetworkSSID;T:WPA;P:NetworkPassword;H:true;;");
    public static final ScannedData data4 = new ScannedData("mailto:email@example.com");

    @Rule
    public MigrationTestHelper testHelper = new MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AppDatabase.class.getCanonicalName(),
            new FrameworkSQLiteOpenHelperFactory());

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.secuso.privacyFriendlyCodeScanner", appContext.getPackageName());
    }

    @Before
    public void prepareDatabase() {
        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase(TEST_DB_NAME);
    }

    @After
    public void deleteDatabase() {
        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase(TEST_DB_NAME);
    }

    @Test
    public void testMigrationToRoomDatabase_1_2() throws Exception {

        // Create the old database with version 1
        DBHandler dbHandler = new DBHandler(InstrumentationRegistry.getInstrumentation().getTargetContext(), TEST_DB_NAME);

        dbHandler.addContent(data1);
        dbHandler.addContent(data2);
        dbHandler.addContent(data3);
        dbHandler.addContent(data4);

        dbHandler.close();

        //SupportSQLiteDatabase db = testHelper.createDatabase(TEST_DB_NAME, 1);
        SupportSQLiteDatabase db = testHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, AppDatabase.MIGRATION_1_2);

        AppDatabase appDatabase = getMigratedRoomDatabase();

        List<HistoryItem> data = appDatabase.historyDao().getAll();

        // Assert that data was copied
        assertNotNull(data);
        assertEquals(4, data.size());
        assertEquals(data1.get_name(), data.get(3).getText());
        assertEquals(data2.get_name(), data.get(2).getText());
        assertEquals(data3.get_name(), data.get(1).getText());
        assertEquals(data4.get_name(), data.get(0).getText());

        // Assert that image was added
        assertNotNull(data.get(0).getFormat());
        assertNotNull(data.get(0).getImage());

        assertNotNull(data.get(1).getFormat());
        assertNotNull(data.get(1).getImage());

        assertNotNull(data.get(2).getFormat());
        assertNotNull(data.get(2).getImage());

        assertNotNull(data.get(3).getFormat());
        assertNotNull(data.get(3).getImage());
    }

    private AppDatabase getMigratedRoomDatabase() {
        AppDatabase database = Room.databaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                AppDatabase.class, TEST_DB_NAME)
                .addMigrations(AppDatabase.MIGRATIONS)
                .build();
        // close the database and release any stream resources when the test finishes
        testHelper.closeWhenFinished(database);
        return database;
    }
}
