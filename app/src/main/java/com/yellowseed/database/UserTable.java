package com.yellowseed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;


public class UserTable {

    private SQLiteDatabase myDataBase;
    private PrefManager prefManager;

    // TABLE CREATE STATEMENTS
    private static final String CREATE_USER_TABLE = "CREATE  TABLE  IF NOT EXISTS "+DBConstant.TABLE_USER
            +" ( "
            +DBConstant.KEY_USER_ID+" VARCHAR, "
            +DBConstant.KEY_USER_NAME+" VARCHAR, "
            +DBConstant.KEY_USER_ACCESS_TOKEN+" VARCHAR, "
            +DBConstant.KEY_USER_IMAGE+" VARCHAR, "
            +DBConstant.KEY_FOLLOW+" VARCHAR, "
            +DBConstant.KEY_FOLLOWER+" VARCHAR, "
            +"PRIMARY KEY ("+DBConstant.KEY_USER_ACCESS_TOKEN+", "+DBConstant.KEY_USER_ID+")"
            +")";

    public UserTable(Context context) {
        prefManager = PrefManager.getInstance(context);
        try {
            myDataBase = context.openOrCreateDatabase(DBConstant.DB_NAME, Context.MODE_PRIVATE, null);
            myDataBase.execSQL(CREATE_USER_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_USER_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(String.format("DROP TABLE IF EXISTS %s", CREATE_USER_TABLE));
        onCreate(database);
    }
    public void insertUserInformation(UserModel model){
        try {
            myDataBase.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(DBConstant.KEY_USER_ID, model.getId());
            values.put(DBConstant.KEY_USER_NAME, model.getName());
            values.put(DBConstant.KEY_USER_ACCESS_TOKEN, model.getAccess_token());
            values.put(DBConstant.KEY_USER_IMAGE, model.getImage());
            values.put(DBConstant.KEY_FOLLOW, model.getFollow());
            values.put(DBConstant.KEY_FOLLOWER, model.getFollower());
            myDataBase.insertWithOnConflict(DBConstant.TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            myDataBase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            myDataBase.endTransaction();
        }
    }

    public List<UserModel> getAllSavedUsers (){
            List<UserModel> userModels = new ArrayList<>();
            try{
                Cursor cursor = myDataBase.query(DBConstant.TABLE_USER, null,  null, null, null, null,DBConstant.KEY_USER_NAME+" ASC");

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    UserModel model= new UserModel();
                    model.setId(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_USER_ID)));
                    model.setName(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_USER_NAME)));
                    model.setImage(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_USER_IMAGE)));
                    model.setFollow(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_FOLLOW)));
                    model.setFollower(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_FOLLOWER)));
                    model.setAccess_token(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_USER_ACCESS_TOKEN)));
                    userModels.add(model);
                    cursor.moveToNext();
                }
                // make sure to close the cursor
                cursor.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return userModels;
    }

    public void closeDB(){
        if (myDataBase != null && myDataBase.isOpen())
            myDataBase.close();
    }
}
