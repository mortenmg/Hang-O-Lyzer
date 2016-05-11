package com.morten.hang_o_lyzer;

/**
 * Created by Morten on 10-05-2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";

    private static final String TAG = "TestDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "droidTest1d";
    private static final String table1 = "table1";

    private static final int DATABASE_VERSION = 5;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE1 =
            " create table " + table1 +
                    " (_id integer primary key autoincrement," +
                    " title text);";

    private final Context mCtx;



    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE1);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + table1 + ";");

            onCreate(db);
        }
    }


    public DatabaseAdapter(Context ctx)
    {
        this.mCtx = ctx;
    }



    public DatabaseAdapter open() throws SQLException
    {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }


    public void close()
    {
        mDbHelper.close();
    }

// CRUD below
    //Insert

    public long insertEntryTable(String colourName)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, colourName);

        return mDb.insert(table1, null, initialValues);
    }



    public boolean deleteEntryTable1(long rowId)
    {

        return mDb.delete(table1, KEY_ROWID + "=" + rowId, null) > 0;
    }



    public Cursor fetchAllEntriesForTable()
    {
        if (mDb == null)
        {
            this.open();
        }


        return mDb.query("table1", new String[] { KEY_ROWID, KEY_TITLE}, null, null, null, null, null);



    }
}

