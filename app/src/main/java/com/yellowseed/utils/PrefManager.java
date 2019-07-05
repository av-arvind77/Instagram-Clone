package com.yellowseed.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yellowseed.R;
import com.yellowseed.model.reqModel.UserModel;


public class PrefManager {
    private static PrefManager instance;
    public Context context;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private UserModel cachedUser;


    public PrefManager(Context context) {
        super();
        this.context = context;
        this.preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        this.editor = this.preferences.edit();
    }

    public static PrefManager getInstance(Context context){
        if (instance == null)
        {
            synchronized (PrefManager.class)
            {
                if(instance==null)
                {
                    instance = new PrefManager(context);
                }
            }
        }
        return instance;
    }
    //preferences variables
    public static final String KEY_USER_DETAIL = "key_user_detail";
    public static final String KEY_IS_REMEMBER_ME = "key_is_remember_me";
    public static final String KEY_IS_LOGIN = "key_is_login";
    public static final String KEY_DEVICE_TOKEN = "key_device_token";
    public static final String KEY_ACCESS_TOKEN = "key_access_token";
    public static final String KEY_USER_PIC = "key_user_pic";
    public static final String KEY_QB_ID = "key_qb_id";
    public static final String KEY_IS_NOTIFICATION_ENABLE = "key_is_notification_enable";
    public static final String KEY_NOTIFICATION_ID = "key_notification_id";
    public static final String KEY_USER_NAME_NUM = "key_user_name_pwd";
    public static final String KEY_USER_PWD = "key_user_pwd";
    public static final String KEY_USER_ID = "key_user_id";
    public static final String KEY_LATITUDE = "key_lattitude";
    public static final String KEY_LONGITUDE = "key_longitude";


    public void setKeyIsRememberMe(boolean keyIsRememberMe){
        setBooleanValue(KEY_IS_REMEMBER_ME,keyIsRememberMe);
    }
    public boolean isRememberMe(){
        return getBooleanValue(KEY_IS_REMEMBER_ME);
    }

    public void setKeyIsLogin(boolean keyIsLogin){
        setBooleanValue(KEY_IS_LOGIN,keyIsLogin);
    }
    public boolean isLogin(){
        return getBooleanValue(KEY_IS_LOGIN);
    }


    public  boolean isNotificationEnable() {
        return getBooleanValue(KEY_IS_NOTIFICATION_ENABLE);
    }

    public  void setKeyIsNotificationEnable(boolean keyIsNotificationEnable) {
        setBooleanValue(KEY_IS_NOTIFICATION_ENABLE,keyIsNotificationEnable);
    }

    public int getKeyNotificationId(){
        return getIntValue(KEY_NOTIFICATION_ID);
    }

    public void setKeyNotifiactionId(int keyNotifiaction){
        setIntValue(KEY_NOTIFICATION_ID,keyNotifiaction);
    }



    public void setKeyDeviceToken(String token){
        setStringValue(KEY_DEVICE_TOKEN,token);
    }
    public String getKeyDeviceToken(){
       return getStringValue(KEY_DEVICE_TOKEN);
    }

    public void setUserId(String token){
        setStringValue(KEY_USER_ID,token);
    }
    public String getUserId(){
       return getStringValue(KEY_USER_ID);
    }

    public void setKeyAccessToken(String token){
        setStringValue(KEY_ACCESS_TOKEN,token);
    }
    public String getKeyAccessToken(){
       return getStringValue(KEY_ACCESS_TOKEN);
    }

    public void setUserProfilePic(String url){
        setStringValue(KEY_USER_PIC,url);
    }
    public String getQBId(){
       return getStringValue(KEY_QB_ID);
    }


    public void setQBid(String id){
        setStringValue(KEY_QB_ID,id);
    }
    public String getUserPic(){
        return getStringValue(KEY_USER_PIC);
    }


    public void setUserNameNum(String token){
        setStringValue(KEY_USER_NAME_NUM,token);
    }
    public String getUserNameNum(){
        return getStringValue(KEY_USER_NAME_NUM);
    }


    public void setUserPassword(String token){
        setStringValue(KEY_USER_PWD,token);
    }
    public String getUserPassword(){
        return getStringValue(KEY_USER_PWD);
    }

    public void saveUser(UserModel user) {
        if (user == null) return;
        cachedUser = user;
        setStringValue(KEY_USER_DETAIL, GsonUtils.toJson(user));
    }

    public UserModel getCurrentUser() {
        if (cachedUser == null) {
            String userJson = getStringValue(KEY_USER_DETAIL);
            cachedUser = GsonUtils.fromJson(userJson, UserModel.class);
        }
        return cachedUser;
    }


    /*

    /**
     * Retrieving the value from the preference for the respective key.
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as boolean.
     */
    private boolean getBooleanValue(String key) {
        return this.preferences.getBoolean(key, false);
    }

    /**
     * Saving the preference
     * @param key : Key of the preference.
     * @param value : Value of the preference.
     */
    private void setBooleanValue(String key, boolean value) {
        this.editor.putBoolean(key, value);
        this.editor.commit();
    }

    /**
     * Retrieving the value from the preference for the respective key.
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as string.
     */
    private String getStringValue(String key) {
        return this.preferences.getString(key,"");
    }

    /**
     * Saving the preference
     * @param key : Key of the preference.
     * @param value : Value of the preference.
     */
    private void setStringValue(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

    /**
     * Retrieving the value from the preference for the respective key.
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as string.
     */
    private int getIntValue(String key) {
        return this.preferences.getInt(key, 0);
    }

    /**
     * Saving the preference
     * @param key : Key of the preference.
     * @param value : Value of the preference.
     */
    private void setIntValue(String key, int value) {
        this.editor.putInt(key, value);
        this.editor.commit();
    }

    /**
     * Retrieving the value from the preference for the respective key.
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as string.
     */
    public long getLongValue(String key) {
        return this.preferences.getLong(key, 0L);
    }

    /**
     * Saving the preference
     * @param key : Key of the preference.
     * @param value : Value of the preference.
     */
    public void setLongValue(String key, long value) {
        this.editor.putLong(key, value);
        this.editor.commit();
    }

    /**
     * Removes all the fields from SharedPrefs
     */
    public void clearPrefs() {
        this.editor.clear();
        this.editor.commit();

    }

    /**
     *Remove the preference for the particular key
     * @param key : Key for which the preference to be cleared.
     */
    public void removeFromPreference(String key){
        this.editor.remove(key);
        this.editor.commit();
    }
}
