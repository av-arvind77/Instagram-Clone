package com.yellowseed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;


public class GroupIDsTable {

    private SQLiteDatabase myDataBase;
    private PrefManager prefManager;

    // TABLE CREATE STATEMENTS
    private static final String CREATE_ROOM_TABLE = "CREATE  TABLE  IF NOT EXISTS "+DBConstant.TABLE_GROUP_IDS
            +" ( "
            +DBConstant.KEY_ROOM_ID+" VARCHAR, "
            +DBConstant.KEY_GROUP_MEMBERS_IDS+" VARCHAR, "
            +"PRIMARY KEY ("+DBConstant.KEY_GROUP_MEMBERS_IDS+", "+DBConstant.KEY_ROOM_ID+")"
            +")";

    public GroupIDsTable(Context context) {
        prefManager = PrefManager.getInstance(context);
        try {
            myDataBase = context.openOrCreateDatabase(DBConstant.DB_NAME, Context.MODE_PRIVATE, null);
            myDataBase.execSQL(CREATE_ROOM_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_ROOM_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(String.format("DROP TABLE IF EXISTS %s", CREATE_ROOM_TABLE));
        onCreate(database);
    }
    public void insertRoomGroupIds(List<String> beanList,String room_id){
        try {
            myDataBase.beginTransaction();
            for (String model : beanList) {
                ContentValues values = new ContentValues();
                values.put(DBConstant.KEY_ROOM_ID, room_id);
                values.put(DBConstant.KEY_GROUP_MEMBERS_IDS, model.toString());
                myDataBase.insertWithOnConflict(DBConstant.TABLE_GROUP_IDS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
            myDataBase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            myDataBase.endTransaction();
        }
    }

    public List<String> getAllGroupIds (String room_id){
            List<String> groupIds = new ArrayList<>();
            try{
                Cursor cursor = myDataBase.query(DBConstant.TABLE_GROUP_IDS, null,  DBConstant.KEY_ROOM_ID +" = ? ", new String[]{room_id}, null, null,null);

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    groupIds.add(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_GROUP_MEMBERS_IDS)));
                    cursor.moveToNext();
                }
                // make sure to close the cursor
                cursor.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return groupIds;
    }

    public void closeDB(){
        if (myDataBase != null && myDataBase.isOpen())
            myDataBase.close();
    }
}
