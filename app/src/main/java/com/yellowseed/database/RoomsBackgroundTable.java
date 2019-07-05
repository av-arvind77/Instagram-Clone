package com.yellowseed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;


public class RoomsBackgroundTable {

    private SQLiteDatabase myDataBase;
    private PrefManager prefManager;
    // TABLE CREATE STATEMENTS
    private static final String CREATE_BACKGROUND = "CREATE  TABLE  IF NOT EXISTS " + DBConstant.TABLE_BACKGROUND
            + " ( "
            + DBConstant.KEY_ROOM_ID + " VARCHAR, "
            + DBConstant.KEY_IMAGE + " VARCHAR, "
            + DBConstant.KEY_COLOR_CODE + " VARCHAR, "
            + DBConstant.KEY_TYPE + " VARCHAR, "
            + "PRIMARY KEY (" + DBConstant.KEY_ROOM_ID + ")"
            + ")";

    public RoomsBackgroundTable(Context context) {
        prefManager = PrefManager.getInstance(context);
        try {
            myDataBase = context.openOrCreateDatabase(DBConstant.DB_NAME, Context.MODE_PRIVATE, null);
            myDataBase.execSQL(CREATE_BACKGROUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_BACKGROUND);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(String.format("DROP TABLE IF EXISTS %s", CREATE_BACKGROUND));
        onCreate(database);
    }

    public void saveBackground(WallpaperModel model,String room_id) {
        try {
            myDataBase.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(DBConstant.KEY_COLOR_CODE, model.getColor_code());
            values.put(DBConstant.KEY_IMAGE, model.getImage());
            values.put(DBConstant.KEY_TYPE, model.getType());
            values.put(DBConstant.KEY_ROOM_ID, room_id);
            myDataBase.insertWithOnConflict(DBConstant.TABLE_BACKGROUND, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            myDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myDataBase.endTransaction();
        }
    }

    public void updateBackGround(WallpaperModel model,String room_id) {
        try {
            ContentValues values = new ContentValues();
            values.put(DBConstant.KEY_COLOR_CODE, model.getColor_code());
            values.put(DBConstant.KEY_IMAGE, model.getImage());
            values.put(DBConstant.KEY_TYPE, model.getType());
            myDataBase.update(DBConstant.TABLE_BACKGROUND, values, DBConstant.KEY_ROOM_ID + " = ?  ",
                    new String[]{room_id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public WallpaperModel getBackground( String room_id) {
        WallpaperModel model = new WallpaperModel();
        try {
            Cursor cursor = myDataBase.query(DBConstant.TABLE_BACKGROUND, null, DBConstant.KEY_ROOM_ID + " = ?  ", new String[]{String.valueOf(room_id)}, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                model.setColor_code(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_COLOR_CODE)));
                model.setImage(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_IMAGE)));
                model.setType(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_TYPE)));
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }


    public void closeDB() {
        if (myDataBase != null && myDataBase.isOpen())
            myDataBase.close();
    }

    public boolean isExist(String roomId) {
        boolean isExist = false;
        try {
            Cursor cursor = myDataBase.query(DBConstant.TABLE_BACKGROUND, null, DBConstant.KEY_ROOM_ID + " = ?  ", new String[]{String.valueOf(roomId)}, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cursor.moveToNext();
                isExist=true;
            }
            // make sure to close the cursor
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isExist;
    }
}
