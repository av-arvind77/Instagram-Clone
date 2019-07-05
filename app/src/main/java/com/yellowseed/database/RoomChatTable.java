package com.yellowseed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;


public class RoomChatTable {

    private SQLiteDatabase myDataBase;
    private PrefManager prefManager;

    // TABLE CREATE STATEMENTS
    private static final String CREATE_ROOM_TABLE = "CREATE  TABLE  IF NOT EXISTS "+DBConstant.TABLE_ROOM_CHAT
            +" ( "
            +DBConstant.KEY_ROOM_ID+" VARCHAR, "
            +DBConstant.KEY_SENDER_ID+" VARCHAR, "
            +DBConstant.KEY_MESSAGE_ID+" VARCHAR, "
            +DBConstant.KEY_BODY+" VARCHAR, "
            +DBConstant.KEY_READ_STATUS+" BOOLEAN, "
            +DBConstant.KEY_UPLOAD_TYPE+" VARCHAR, "
            +DBConstant.KEY_THUMBNAIL+" VARCHAR, "
            +DBConstant.KEY_CREATED_TIME_STAMP+" VARCHAR, "
            +DBConstant.KEY_CREATED_AT+" VARCHAR, "
            +DBConstant.KEY_IS_USER_SENDER+" BOOLEAN, "
            +DBConstant.KEY_SENDER_IMAGE+" VARCHAR, "
            +DBConstant.KEY_RECEIVER_IMAGE+" BOOLEAN, "
            +DBConstant.KEY_LOCAL_MESSAGE_ID+" VARCHAR, "
            +DBConstant.KEY_IS_STARED+" VARCHAR, "
            +"PRIMARY KEY ("+DBConstant.KEY_MESSAGE_ID+", "+DBConstant.KEY_ROOM_ID+")"
            +")";

    public RoomChatTable(Context context) {
        prefManager = PrefManager.getInstance(context);
        try {
            myDataBase = context.openOrCreateDatabase(DBConstant.DB_NAME, Context.MODE_PRIVATE, null);
            myDataBase.execSQL(CREATE_ROOM_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStarredStatus(boolean status, String room_id){
        try {
            ContentValues values = new ContentValues();
            values.put(DBConstant.KEY_IS_STARED, status);
            myDataBase.update(DBConstant.TABLE_ROOM_CHAT, values,  DBConstant.KEY_ROOM_ID+" = ?  ",
                    new String[]{room_id});
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateReadStatus(String room_id){
        try {
            ContentValues values = new ContentValues();
            values.put(DBConstant.KEY_READ_STATUS, true);
            myDataBase.update(DBConstant.TABLE_ROOM_CHAT, values,  DBConstant.KEY_ROOM_ID+" = ?  AND "+DBConstant.KEY_READ_STATUS+" = ? ",
                    new String[]{room_id,"0"});
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteRoomChat(String room_id){
        try {
            myDataBase.delete(DBConstant.TABLE_ROOM_CHAT, DBConstant.KEY_ROOM_ID+" = ? ",
                    new String[]{room_id});
        }catch (Exception e){
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
    public void insertRoomInformation(List<GetChatResonse.UserInfoBean> beanList,String room_id){
        try {
            myDataBase.beginTransaction();

            for (GetChatResonse.UserInfoBean model : beanList) {
                ContentValues values = new ContentValues();
                values.put(DBConstant.KEY_ROOM_ID,room_id);
                values.put(DBConstant.KEY_SENDER_ID, model.getSender_id());
                values.put(DBConstant.KEY_MESSAGE_ID, model.getId());
                values.put(DBConstant.KEY_SENDER_IMAGE, model.getSender_image());
                values.put(DBConstant.KEY_RECEIVER_IMAGE, model.getReceiver_image());
                values.put(DBConstant.KEY_READ_STATUS, model.isRead_status());
                values.put(DBConstant.KEY_CREATED_TIME_STAMP, model.getCreated_timestamp());
                values.put(DBConstant.KEY_CREATED_AT, model.getCreated_at());
                values.put(DBConstant.KEY_BODY, model.getBody());
                values.put(DBConstant.KEY_UPLOAD_TYPE, model.getUpload_type());
                values.put(DBConstant.KEY_THUMBNAIL, model.getThumbnail());
                values.put(DBConstant.KEY_IS_USER_SENDER, model.isIs_user_sender());
                values.put(DBConstant.KEY_IS_STARED, model.getIs_stared());

                myDataBase.insertWithOnConflict(DBConstant.TABLE_ROOM_CHAT, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
            myDataBase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            myDataBase.endTransaction();
        }
    }

    public void insertSingleRoomInformation(GetChatResonse.UserInfoBean model,String room_id){
        try {
            myDataBase.beginTransaction();
                ContentValues values = new ContentValues();
                values.put(DBConstant.KEY_ROOM_ID,room_id);
                values.put(DBConstant.KEY_SENDER_ID, model.getSender_id());
                values.put(DBConstant.KEY_MESSAGE_ID, model.getId());
                values.put(DBConstant.KEY_SENDER_IMAGE, model.getSender_image());
                values.put(DBConstant.KEY_RECEIVER_IMAGE, model.getReceiver_image());
                values.put(DBConstant.KEY_READ_STATUS, model.isRead_status());
                values.put(DBConstant.KEY_CREATED_TIME_STAMP, model.getCreated_timestamp());
                values.put(DBConstant.KEY_CREATED_AT, model.getCreated_at());
                values.put(DBConstant.KEY_BODY, model.getBody());
                values.put(DBConstant.KEY_UPLOAD_TYPE, model.getUpload_type());
                values.put(DBConstant.KEY_THUMBNAIL, model.getThumbnail());
                values.put(DBConstant.KEY_IS_USER_SENDER, model.isIs_user_sender());
                values.put(DBConstant.KEY_IS_STARED, model.getIs_stared());

                myDataBase.insertWithOnConflict(DBConstant.TABLE_ROOM_CHAT, null, values, SQLiteDatabase.CONFLICT_IGNORE);

            myDataBase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            myDataBase.endTransaction();
        }
    }


    public List<GetChatResonse.UserInfoBean> getChatPerRoom (String room_id){
        List<GetChatResonse.UserInfoBean> chatList = new ArrayList<>();
        try{
            Cursor cursor = myDataBase.query(DBConstant.TABLE_ROOM_CHAT, null,  DBConstant.KEY_ROOM_ID+" = ? ",
                    new String[]{room_id}, null, null,DBConstant.KEY_CREATED_TIME_STAMP+ " ASC");

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                GetChatResonse.UserInfoBean model= new GetChatResonse.UserInfoBean();
                model.setSender_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_SENDER_ID)));
                model.setRoom_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_ROOM_ID)));
                model.setIs_stared(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_STARED))==1);
                model.setId(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_MESSAGE_ID)));
                model.setBody(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_BODY)));
                model.setRead_status(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_READ_STATUS))==1);
                model.setUpload_type(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_UPLOAD_TYPE)));
                model.setThumbnail(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_THUMBNAIL)));
                model.setCreated_timestamp(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_CREATED_TIME_STAMP)));
                model.setCreated_at(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_CREATED_AT)));
                model.setIs_user_sender(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_USER_SENDER))==1);
                model.setSender_image(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_SENDER_IMAGE)));
                model.setReceiver_image(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_RECEIVER_IMAGE)));
                model.setLocal_message_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LOCAL_MESSAGE_ID)));
                chatList.add(model);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return chatList;
    }

    public List<GetChatResonse.UserInfoBean> getMediaChat(String room_id){
        List<GetChatResonse.UserInfoBean> chatList = new ArrayList<>();
        try{
            Cursor cursor = myDataBase.query(DBConstant.TABLE_ROOM_CHAT, null,  DBConstant.KEY_ROOM_ID+" = ? AND ("+DBConstant.KEY_UPLOAD_TYPE+" = ? OR "+DBConstant.KEY_UPLOAD_TYPE+" = ? )",
                    new String[]{room_id,"image","video"}, null, null,DBConstant.KEY_CREATED_TIME_STAMP+ " ASC");

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                GetChatResonse.UserInfoBean model= new GetChatResonse.UserInfoBean();
                model.setSender_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_SENDER_ID)));
                model.setRoom_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_ROOM_ID)));
                model.setIs_stared(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_STARED))==1);
                model.setId(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_MESSAGE_ID)));
                model.setBody(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_BODY)));
                model.setRead_status(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_READ_STATUS))==1);
                model.setUpload_type(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_UPLOAD_TYPE)));
                model.setThumbnail(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_THUMBNAIL)));
                model.setCreated_timestamp(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_CREATED_TIME_STAMP)));
                model.setCreated_at(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_CREATED_AT)));
                model.setIs_user_sender(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_USER_SENDER))==1);
                model.setSender_image(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_SENDER_IMAGE)));
                model.setReceiver_image(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_RECEIVER_IMAGE)));
                model.setLocal_message_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LOCAL_MESSAGE_ID)));
                chatList.add(model);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return chatList;
    }

    public List<GetChatResonse.UserInfoBean> getStarredMessages (String room_id){
        List<GetChatResonse.UserInfoBean> chatList = new ArrayList<>();
        try{
            Cursor cursor = myDataBase.query(DBConstant.TABLE_ROOM_CHAT, null,  DBConstant.KEY_ROOM_ID+" = ? AND "+DBConstant.KEY_IS_STARED+" = ",
                    new String[]{room_id,"1"}, null, null,DBConstant.KEY_CREATED_TIME_STAMP+ " ASC");

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                GetChatResonse.UserInfoBean model= new GetChatResonse.UserInfoBean();
                model.setSender_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_SENDER_ID)));
                model.setRoom_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_ROOM_ID)));
                model.setIs_stared(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_STARED))==1);
                model.setId(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_MESSAGE_ID)));
                model.setBody(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_BODY)));
                model.setRead_status(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_READ_STATUS))==1);
                model.setUpload_type(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_UPLOAD_TYPE)));
                model.setThumbnail(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_THUMBNAIL)));
                model.setCreated_timestamp(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_CREATED_TIME_STAMP)));
                model.setCreated_at(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_CREATED_AT)));
                model.setIs_user_sender(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_USER_SENDER))==1);
                model.setSender_image(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_SENDER_IMAGE)));
                model.setReceiver_image(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_RECEIVER_IMAGE)));
                model.setLocal_message_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LOCAL_MESSAGE_ID)));
                chatList.add(model);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return chatList;
    }

    public void closeDB(){
        if (myDataBase != null && myDataBase.isOpen())
            myDataBase.close();
    }

    public GetChatResonse.UserInfoBean getLastMessageDetail(String room_id) {
        GetChatResonse.UserInfoBean lastMessageModel=new GetChatResonse.UserInfoBean();
        try{
            Cursor cursor = myDataBase.query(DBConstant.TABLE_ROOM_CHAT, null,  DBConstant.KEY_ROOM_ID+" = ? ",
                    new String[]{room_id}, null, null,DBConstant.KEY_CREATED_TIME_STAMP+ " DESC", "1");

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                lastMessageModel.setSender_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_SENDER_ID)));
                lastMessageModel.setRoom_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_ROOM_ID)));
                lastMessageModel.setIs_stared(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_STARED))==1);
                lastMessageModel.setId(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_MESSAGE_ID)));
                lastMessageModel.setBody(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_BODY)));
                lastMessageModel.setRead_status(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_READ_STATUS))==1);
                lastMessageModel.setUpload_type(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_UPLOAD_TYPE)));
                lastMessageModel.setThumbnail(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_THUMBNAIL)));
                lastMessageModel.setCreated_timestamp(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_CREATED_TIME_STAMP)));
                lastMessageModel.setCreated_at(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_CREATED_AT)));
                lastMessageModel.setIs_user_sender(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_USER_SENDER))==1);
                lastMessageModel.setSender_image(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_SENDER_IMAGE)));
                lastMessageModel.setReceiver_image(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_RECEIVER_IMAGE)));
                lastMessageModel.setLocal_message_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LOCAL_MESSAGE_ID)));
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return lastMessageModel;
    }
}
