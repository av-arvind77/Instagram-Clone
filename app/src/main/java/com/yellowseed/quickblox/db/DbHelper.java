package com.yellowseed.quickblox.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yellowseed.database.DBConstant;

/**
 * Created by tereha on 17.05.16.
 */
public class DbHelper extends SQLiteOpenHelper {

    private String TAG = DbHelper.class.getSimpleName();

    private static final String DB_NAME = DBConstant.DB_NAME;

    public static final String DB_TABLE_NAME = "qbusers";
    public static final String DB_COLUMN_ID = "qbID";
    public static final String DB_COLUMN_USER_FULL_NAME = "qbuserFullName";
    public static final String DB_COLUMN_USER_LOGIN = "qbuserLogin";
    public static final String DB_COLUMN_USER_ID = "qbuserID";
    public static final String DB_COLUMN_USER_PASSWORD = "qbuserPass";
    public static final String DB_COLUMN_USER_TAG = "qbuserTag";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "--- onCreate database ---");
        db.execSQL("create table " + DB_TABLE_NAME + " ("
                + DB_COLUMN_ID + " integer primary key autoincrement,"
                + DB_COLUMN_USER_ID + " integer,"
                + DB_COLUMN_USER_LOGIN + " text,"
                + DB_COLUMN_USER_PASSWORD + " text,"
                + DB_COLUMN_USER_FULL_NAME + " text,"
                + DB_COLUMN_USER_TAG + " text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
