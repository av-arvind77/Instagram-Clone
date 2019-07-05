package com.yellowseed.webservices;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.activity.LoginActivity;
import com.yellowseed.activity.OtpActivity;
import com.yellowseed.listener.ApiCallback;
import com.yellowseed.listener.OkCancelInterface;
import com.yellowseed.model.reqModel.BlockUserModel;
import com.yellowseed.model.reqModel.DeviceModel;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ankit_mobiloitte on 7/6/18.
 */

public class CommonApi {

    public static void callStaticPageAPI(final Context mContext, String type, final ApiCallback apiCallback) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setTyepe(type);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiStaticPages(apiRequest);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            apiCallback.onSuccess(response.body());
                        }
                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            ((Activity)mContext).finishAffinity();
                            CommonUtils.clearPrefData(mContext);
                        }else {
                            ToastUtils.showToastShort(mContext, ""+response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }


    public static void callGetProfileAPI(final Context mContext,final boolean isShowLoading, final ApiCallback apiCallback) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            if(isShowLoading){
            progressDialog.show();
            }

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiUserProfile();
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    if(isShowLoading) {
                        progressDialog.dismiss();
                    }
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            if(response.body().getUser() != null){
                                apiCallback.onSuccess(response.body());

                            }
                        }
                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            CommonUtils.clearPrefData(mContext);
                            ((Activity)mContext).finishAffinity();

                        }else {
                            ToastUtils.showToastShort(mContext, ""+response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    if(isShowLoading) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }

    public static void callUploadImageApi(final Context mContext, File imageFile) {
        if (CommonUtils.isOnline(mContext)){
            Map<String, RequestBody> user = new HashMap<>();
            if (imageFile!=null && imageFile.exists()){
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), imageFile);
                user.put("image\"; filename=\"" + imageFile.getName(), fileBody);
            }

            Call<ApiResponse> call = ApiExecutor.getMultipartClientWithHeader(mContext).apiUpdateProfilePic(user);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                    try {
                        if (response != null && response.body() != null) {

                            if(response.body().getResponseCode()==AppConstants.SUCCESS) {
                                ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                            }
                            else {
                                ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                            }

                        }else {
                            CommonUtils.showToast(mContext, mContext.getResources().getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                }
            });
        }else {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));

        }
    }


    public static void callGetOtherUserProfileAPI(final Context mContext, String userID, final ApiCallback apiCallback) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setUser(new UserModel());
            apiRequest.getUser().setId(userID);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiOtherUserProfile(apiRequest);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            if(response.body().getUser() != null){
                                apiCallback.onSuccess(response.body());
                            }
                        }
                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            CommonUtils.clearPrefData(mContext);
                            ((Activity)mContext).finishAffinity();

                        }else {
                            ToastUtils.showToastShort(mContext, ""+response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }

    /**
     * Call Block User Api
     */

    public static void callBlockUserAPI(final Context mContext, String userID, final ApiCallback apiCallback) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setBlock(new BlockUserModel());
            apiRequest.getBlock().setUser_id(userID);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiBlockUser(apiRequest);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    if (response != null && response.body() != null) {

                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            apiCallback.onSuccess(response.body());
                        }
                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            CommonUtils.clearPrefData(mContext);
                            ((Activity)mContext).finishAffinity();

                        }else {
                            ToastUtils.showToastShort(mContext, ""+response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }


    public static void turnOnPushStory(final Context mContext, String userID, boolean status,final ApiCallback apiCallback) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("user_id",userID);
            jsonObject.addProperty("story",status);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiPushOnOff(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    if (response != null && response.body() != null) {

                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            apiCallback.onSuccess(response.body());
                        }
                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            CommonUtils.clearPrefData(mContext);
                            ((Activity)mContext).finishAffinity();

                        }else {
                            ToastUtils.showToastShort(mContext, ""+response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }

    public static void turnOnPushPost(final Context mContext, String userID, boolean status, final ApiCallback apiCallback) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("user_id",userID);
            jsonObject.addProperty("post",status);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiPushOnOff(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    if (response != null && response.body() != null) {

                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            apiCallback.onSuccess(response.body());
                        }
                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            CommonUtils.clearPrefData(mContext);
                            ((Activity)mContext).finishAffinity();

                        }else {
                            ToastUtils.showToastShort(mContext, ""+response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }


    /**
     * Call Unblock User Api
     */


    public static void callUnblockUserAPI(final Context mContext, String userID, final ApiCallback apiCallback) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setBlock(new BlockUserModel());
            apiRequest.getBlock().setUser_id(userID);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiUnblockUser(apiRequest);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            apiCallback.onSuccess(response.body());
                        }
                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            CommonUtils.clearPrefData(mContext);
                            ((Activity)mContext).finishAffinity();

                        }else {
                            ToastUtils.showToastShort(mContext, ""+response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }

}
