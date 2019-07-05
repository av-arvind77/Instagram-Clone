package com.yellowseed.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by ankit.saini on 24/4/18.
 */

public class ConverterUtils {
    private static String TAG=ConverterUtils.class.getSimpleName();


   /* public static List<VehiclesModel> getVehicleList(Object o){
        Gson gson = new Gson();
        Type type = new TypeToken<List<VehiclesModel>>() {}.getType();
        List<VehiclesModel> mList = gson.fromJson(gson.toJson(o), type);
        LogUtils.errorLog(TAG, "prefManager mList @@"+new Gson().toJsonTree(mList));
        return mList;
    }*/

    public static RequestBody parseString(String str){
        return RequestBody.create(MediaType.parse("text/plain"),str);
    }
    public static MultipartBody.Part getMultipartFromFile(String partName, File imageFile) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, imageFile.getName(), requestFile);
    }

    /*public static VehiclesModel getVehicles(Object data) {
        Gson gson=new Gson();
        return gson.fromJson(gson.toJson(data),VehiclesModel.class);
    }*/

    public static boolean getStatus(String valueFromXml) {
        if(valueFromXml.equalsIgnoreCase("N/A")){
            return false;
        }
        return true;
    }



    public static String getLink(Object data) {
        String link="";
        Gson gson = new Gson();
        String gsonString=gson.toJson(data);
        try {
            JSONObject jsonObject   =   new JSONObject(gsonString);
            link= jsonObject.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return link;
    }
}
