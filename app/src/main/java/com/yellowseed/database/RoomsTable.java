package com.yellowseed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;


public class RoomsTable {

    private SQLiteDatabase myDataBase;
    private PrefManager prefManager;

    // TABLE CREATE STATEMENTS
    private static final String CREATE_ROOM_TABLE = "CREATE  TABLE  IF NOT EXISTS " + DBConstant.TABLE_ROOMS
            + " ( "
            + DBConstant.KEY_ROOM_ID + " VARCHAR, "
            + DBConstant.KEY_LAST_UPDATE + " VARCHAR, "
            + DBConstant.KEY_SENDER_ID + " VARCHAR, "
            + DBConstant.KEY_ASSOC_ID + " VARCHAR, "
            + DBConstant.KEY_NAME + " VARCHAR, "
            + DBConstant.KEY_IMAGE + " VARCHAR, "
            + DBConstant.KEY_IS_GROUP + " BOOLEAN, "
            + DBConstant.KEY_LAST_MSG_TYPE + " VARCHAR, "
            + DBConstant.KEY_LAST_MESSAGE + " VARCHAR, "
            + DBConstant.KEY_QB_ID + " VARCHAR, "
            + DBConstant.KEY_IS_AVTAR_ENABLE+" BOOLEAN, "
            + DBConstant.KEY_LAST_MESSAGE_SENDER + " VARCHAR, "
            + DBConstant.KEY_MESSAGE_TIME + " VARCHAR, "
            + DBConstant.KEY_MESSAGE_CREATED_TIME + " VARCHAR, "
            + DBConstant.KEY_IS_MUTE + " BOOLEAN, "
            + DBConstant.KEY_IS_BROAD_CAST + " BOOLEAN, "

            + DBConstant.KEY_LAST_SEEN + " VARCHAR, "
            + DBConstant.KEY_LAST_STATUS + " VARCHAR, "

            + DBConstant.KEY_IS_BLOCK + " BOOLEAN, "
            + DBConstant.KEY_IS_PINNED + " VARCHAR, "
            + DBConstant.KEY_GROUP_MEMBERS_IDS + " VARCHAR, "
            + "PRIMARY KEY (" + DBConstant.KEY_SENDER_ID + ", " + DBConstant.KEY_ASSOC_ID + ", " + DBConstant.KEY_ROOM_ID + ")"
            + ")";

    public RoomsTable(Context context) {
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

    public void insertRoomInformation(List<GetRoomResonse.RoomsBean> beanList) {
        try {
            myDataBase.beginTransaction();
            for (GetRoomResonse.RoomsBean model : beanList) {
                ContentValues values = new ContentValues();
                values.put(DBConstant.KEY_ROOM_ID, model.getRoom_id());
                values.put(DBConstant.KEY_LAST_UPDATE, model.getLast_update());
                values.put(DBConstant.KEY_SENDER_ID, model.getSender_id());
                values.put(DBConstant.KEY_IS_BROAD_CAST, model.isIs_broadcast());
                values.put(DBConstant.KEY_ASSOC_ID, model.getAssoc_id());
                values.put(DBConstant.KEY_QB_ID, model.getQb_id());
                values.put(DBConstant.KEY_NAME, model.getName());
                if(model.getChat_status()!=null){
                    values.put(DBConstant.KEY_LAST_SEEN, model.getChat_status().getLast_seen_timestamp());
                    values.put(DBConstant.KEY_LAST_STATUS, model.getChat_status().getStatus());
                }


                values.put(DBConstant.KEY_IMAGE, model.getImage());

                values.put(DBConstant.KEY_IS_PINNED, model.getIs_pinned());
                values.put(DBConstant.KEY_IS_GROUP, model.isIs_group());
                values.put(DBConstant.KEY_LAST_MSG_TYPE, model.getLast_msg_type());
                values.put(DBConstant.KEY_LAST_MESSAGE, model.getLast_message());
                values.put(DBConstant.KEY_MESSAGE_TIME, model.getLast_message_time());
                values.put(DBConstant.KEY_MESSAGE_CREATED_TIME, model.getLast_message_time());
                values.put(DBConstant.KEY_IS_MUTE, model.isIs_mute());
                values.put(DBConstant.KEY_IS_BLOCK, model.isIs_blocked());
                values.put(DBConstant.KEY_IS_AVTAR_ENABLE, model.isIs_enable_avatar());
                values.put(DBConstant.KEY_LAST_MESSAGE_SENDER, model.getLast_message_sender());
                myDataBase.insertWithOnConflict(DBConstant.TABLE_ROOMS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
            myDataBase.setTransactionSuccessful();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myDataBase.endTransaction();
        }
        /*for (GetRoomResonse.RoomsBean model : beanList) {
            if (model.getGroup_member_ids() != null && model.getGroup_member_ids().size() > 0) {
                GroupIDsTable groupIDsTable = new GroupIDsTable(context);
                groupIDsTable.insertRoomGroupIds(model.getGroup_member_ids(), model.getRoom_id());
                groupIDsTable.closeDB();
            }
        }*/
    }


    public void insertSingleRoom(GetRoomResonse.RoomsBean model,String name) {
        try {
            myDataBase.beginTransaction();
                ContentValues values = new ContentValues();
                values.put(DBConstant.KEY_ROOM_ID, model.getId());
                values.put(DBConstant.KEY_LAST_UPDATE, model.getLast_update());
                values.put(DBConstant.KEY_SENDER_ID, model.getSender_id());
                values.put(DBConstant.KEY_IS_BROAD_CAST, model.isIs_broadcast());
                values.put(DBConstant.KEY_ASSOC_ID, model.getAssoc_id());
                values.put(DBConstant.KEY_QB_ID, model.getQb_id());
                values.put(DBConstant.KEY_NAME, name);
            if(model.getChat_status()!=null){
                values.put(DBConstant.KEY_LAST_SEEN, model.getChat_status().getLast_seen_timestamp());
                values.put(DBConstant.KEY_LAST_STATUS, model.getChat_status().getStatus());
            }

            values.put(DBConstant.KEY_IMAGE, model.getImage());
                values.put(DBConstant.KEY_IS_PINNED, model.getIs_pinned());
                values.put(DBConstant.KEY_IS_GROUP, model.isIs_group());
                values.put(DBConstant.KEY_LAST_MSG_TYPE, model.getLast_msg_type());
                values.put(DBConstant.KEY_LAST_MESSAGE, model.getLast_message());
                values.put(DBConstant.KEY_MESSAGE_TIME, model.getLast_message_time());
                values.put(DBConstant.KEY_MESSAGE_CREATED_TIME, model.getLast_message_time());
                values.put(DBConstant.KEY_IS_AVTAR_ENABLE, model.isIs_enable_avatar());
                values.put(DBConstant.KEY_IS_MUTE, model.isIs_mute());
                values.put(DBConstant.KEY_IS_BLOCK, model.isIs_blocked());
                values.put(DBConstant.KEY_LAST_MESSAGE_SENDER, model.getLast_message_sender());
                myDataBase.insertWithOnConflict(DBConstant.TABLE_ROOMS, null, values, SQLiteDatabase.CONFLICT_IGNORE);

            myDataBase.setTransactionSuccessful();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myDataBase.endTransaction();
        }
        /*for (GetRoomResonse.RoomsBean model : beanList) {
            if (model.getGroup_member_ids() != null && model.getGroup_member_ids().size() > 0) {
                GroupIDsTable groupIDsTable = new GroupIDsTable(context);
                groupIDsTable.insertRoomGroupIds(model.getGroup_member_ids(), model.getRoom_id());
                groupIDsTable.closeDB();
            }
        }*/
    }

    public void deleteRoomChat(String room_id) {
        try {
            myDataBase.delete(DBConstant.TABLE_ROOMS, DBConstant.KEY_ROOM_ID + " = ? ",
                    new String[]{room_id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<GetRoomResonse.RoomsBean> getAllRooms(Context context) {
        List<GetRoomResonse.RoomsBean> roomsBeanList = new ArrayList<>();
        try {
            Cursor cursor = myDataBase.query(DBConstant.TABLE_ROOMS, null, null, null, null, null, DBConstant.KEY_IS_PINNED + " DESC");

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                GetRoomResonse.RoomsBean model = new GetRoomResonse.RoomsBean();
                model.setAssoc_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_ASSOC_ID)));
                model.setSender_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_SENDER_ID)));
                model.setRoom_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_ROOM_ID)));
                model.setQb_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_QB_ID)));
                GetRoomResonse.ChatStatusModel statusModel=new GetRoomResonse.ChatStatusModel();
                statusModel.setLast_seen_timestamp(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_SEEN)));
                statusModel.setStatus(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_STATUS)));
                model.setChat_status(statusModel);
                model.setLast_message(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_MESSAGE)));
                model.setLast_message_sender(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_MESSAGE_SENDER)));
                model.setName(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_NAME)));
                model.setImage(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_IMAGE)));
                model.setIs_mute(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_MUTE)) == 1);
                model.setIs_broadcast(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_BROAD_CAST)) == 1);
                model.setIs_group(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_GROUP)) == 1);
                model.setIs_enable_avatar(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_AVTAR_ENABLE)) == 1);
                model.setIs_pinned(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_PINNED)));
                model.setLast_msg_type(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_MSG_TYPE)));
                GroupIDsTable groupIDsTable = new GroupIDsTable(context);
                model.setGroup_member_ids(groupIDsTable.getAllGroupIds(model.getRoom_id()));
                model.setLast_update(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_UPDATE)));
                model.setLast_message_time(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_MESSAGE_TIME)));
                model.setIs_blocked(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_BLOCK))==1);
                roomsBeanList.add(model);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomsBeanList;
    }


    public GetRoomResonse.RoomsBean getSingleRoom(Context context, String room_id) {
        GetRoomResonse.RoomsBean model = new GetRoomResonse.RoomsBean();
        try {
            Cursor cursor = myDataBase.query(DBConstant.TABLE_ROOMS, null, DBConstant.KEY_ROOM_ID + " = ?  ", new String[]{String.valueOf(room_id)}, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                model.setAssoc_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_ASSOC_ID)));
                model.setSender_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_SENDER_ID)));
                model.setQb_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_QB_ID)));
                model.setRoom_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_ROOM_ID)));
                GetRoomResonse.ChatStatusModel statusModel=new GetRoomResonse.ChatStatusModel();
                statusModel.setLast_seen_timestamp(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_SEEN)));
                statusModel.setStatus(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_STATUS)));
                model.setChat_status(statusModel);
                model.setLast_message(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_MESSAGE)));
                model.setLast_message_sender(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_MESSAGE_SENDER)));
                model.setName(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_NAME)));
                model.setImage(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_IMAGE)));
                model.setIs_broadcast(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_BROAD_CAST)) == 1);
                model.setIs_enable_avatar(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_AVTAR_ENABLE)) == 1);
                model.setIs_mute(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_MUTE)) == 1);
                model.setIs_group(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_GROUP)) == 1);
                model.setIs_pinned(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_PINNED)));
                model.setLast_msg_type(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_MSG_TYPE)));
                GroupIDsTable groupIDsTable = new GroupIDsTable(context);
                model.setGroup_member_ids(groupIDsTable.getAllGroupIds(model.getRoom_id()));
                model.setIs_blocked(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_BLOCK))==1);
                model.setLast_update(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_UPDATE)));
                model.setLast_message_time(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_MESSAGE_TIME)));

                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public void deleteAllRooms() {
        myDataBase.delete(DBConstant.TABLE_ROOMS, null,
                null);
    }


    public void closeDB() {
        if (myDataBase != null && myDataBase.isOpen())
            myDataBase.close();
    }

    public void updatePinnedStatus(int status, String room_id) {
        try {
            ContentValues values = new ContentValues();
            values.put(DBConstant.KEY_IS_PINNED, status);
            myDataBase.update(DBConstant.TABLE_ROOMS, values, DBConstant.KEY_ROOM_ID + " = ?  ",
                    new String[]{room_id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAvtarEnableStatus(boolean status, String room_id) {
        try {
            ContentValues values = new ContentValues();
            values.put(DBConstant.KEY_IS_AVTAR_ENABLE, status);
            myDataBase.update(DBConstant.TABLE_ROOMS, values, DBConstant.KEY_ROOM_ID + " = ?  ",
                    new String[]{room_id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateIsMuteStatus(boolean status, String room_id) {
        try {
            ContentValues values = new ContentValues();
            values.put(DBConstant.KEY_IS_MUTE, status);
            myDataBase.update(DBConstant.TABLE_ROOMS, values, DBConstant.KEY_ROOM_ID + " = ?  ",
                    new String[]{room_id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBlockStatus(boolean status, String room_id) {
        try {
            ContentValues values = new ContentValues();
            values.put(DBConstant.KEY_IS_BLOCK, status);
            myDataBase.update(DBConstant.TABLE_ROOMS, values, DBConstant.KEY_ROOM_ID + " = ?  ",
                    new String[]{room_id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateImage(String image, String room_id) {
        try {
            ContentValues values = new ContentValues();
            values.put(DBConstant.KEY_IMAGE, image);
            myDataBase.update(DBConstant.TABLE_ROOMS, values, DBConstant.KEY_ROOM_ID + " = ?  ",
                    new String[]{room_id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTotalPinCount() {
        List<GetRoomResonse.RoomsBean> roomsBeanList = new ArrayList<>();
        try {
            Cursor cursor = myDataBase.query(DBConstant.TABLE_ROOMS, null, DBConstant.KEY_IS_PINNED + " = ?  ", new String[]{String.valueOf(1)}, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                GetRoomResonse.RoomsBean model = new GetRoomResonse.RoomsBean();
                model.setAssoc_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_ASSOC_ID)));
                model.setSender_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_SENDER_ID)));
                model.setRoom_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_ROOM_ID)));
                model.setQb_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_QB_ID)));
                model.setLast_message(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_MESSAGE)));
                model.setLast_message_sender(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_MESSAGE_SENDER)));
                model.setName(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_NAME)));
                GetRoomResonse.ChatStatusModel statusModel=new GetRoomResonse.ChatStatusModel();
                statusModel.setLast_seen_timestamp(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_SEEN)));
                statusModel.setStatus(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_STATUS)));
                model.setChat_status(statusModel);
                model.setImage(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_IMAGE)));
                model.setIs_broadcast(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_BROAD_CAST)) == 1);
                model.setIs_mute(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_MUTE)) == 1);
                model.setIs_group(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_GROUP)) == 1);
                model.setIs_enable_avatar(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_AVTAR_ENABLE)) == 1);
                model.setIs_pinned(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_PINNED)));
                model.setLast_msg_type(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_MSG_TYPE)));
                model.setLast_update(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_UPDATE)));
                model.setLast_message_time(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_MESSAGE_TIME)));
                model.setIs_blocked(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_BLOCK))==1);
                roomsBeanList.add(model);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomsBeanList.size();
    }

    public GetRoomResonse.RoomsBean getRoomDetail(String chatUserId, String senderId) {
        GetRoomResonse.RoomsBean model = new GetRoomResonse.RoomsBean();
        try {
            Cursor cursor = myDataBase.query(DBConstant.TABLE_ROOMS, null, DBConstant.KEY_SENDER_ID + " = ?  AND "+DBConstant.KEY_ASSOC_ID + " = ? ", new String[]{senderId,chatUserId}, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                model.setAssoc_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_ASSOC_ID)));
                model.setSender_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_SENDER_ID)));
                model.setRoom_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_ROOM_ID)));
                model.setLast_message(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_MESSAGE)));
                model.setLast_message_sender(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_MESSAGE_SENDER)));
                model.setName(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_NAME)));
                GetRoomResonse.ChatStatusModel statusModel=new GetRoomResonse.ChatStatusModel();
                statusModel.setLast_seen_timestamp(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_SEEN)));
                statusModel.setStatus(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_STATUS)));
                model.setChat_status(statusModel);
                model.setImage(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_IMAGE)));
                model.setQb_id(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_QB_ID)));
                model.setIs_mute(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_MUTE)) == 1);
                model.setIs_broadcast(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_BROAD_CAST)) == 1);
                model.setIs_enable_avatar(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_AVTAR_ENABLE)) == 1);
                model.setIs_group(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_GROUP)) == 1);
                model.setIs_pinned(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_PINNED)));
                model.setLast_msg_type(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_MSG_TYPE)));
                model.setLast_update(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_LAST_UPDATE)));
                model.setLast_message_time(cursor.getString(cursor.getColumnIndex(DBConstant.KEY_MESSAGE_TIME)));
                model.setIs_blocked(cursor.getInt(cursor.getColumnIndex(DBConstant.KEY_IS_BLOCK))==1);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
            return model;

        } catch (Exception e) {
            e.printStackTrace();
        }
         return model;
    }

}
