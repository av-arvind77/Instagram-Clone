package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yellowseed.R;
import com.yellowseed.database.UserTable;
import com.yellowseed.databinding.ActivityLoginBinding;
import com.yellowseed.listener.OkCancelInterface;
import com.yellowseed.model.reqModel.DeviceModel;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;


public class LoginActivity extends BaseActivity implements TextWatcher, GoogleApiClient.OnConnectionFailedListener {

    private ActivityLoginBinding binding;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String PasswordKey = "passwordKey";
    private SharedPreferences sharedpreferences;
    private Context mContext;
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 7;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mContext = LoginActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();


    }

    @Override
    public void initializedControl() {
     /*   callbackManager = CallbackManager.Factory.create();
        *//********  Google plus integration code ***********//*

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (PrefManager.getInstance(mContext).isRememberMe()) {
            binding.cbRemember.setChecked(PrefManager.getInstance(mContext).isRememberMe());
            binding.etLoginemail.setText(PrefManager.getInstance(mContext).getUserNameNum());
            binding.etLoginemail.setSelection(binding.etLoginemail.getText().toString().length());
            binding.etLoginpassword.setText(PrefManager.getInstance(mContext).getUserPassword());
        }*/
    }


    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

        binding.btnLogin.setOnClickListener(this);
        binding.tvForgotPass.setOnClickListener(this);
        binding.llSignup.setOnClickListener(this);
        binding.flRow.setOnClickListener(this);
        binding.llFacebook.setOnClickListener(this);
        binding.llGoogle.setOnClickListener(this);
        binding.etLoginemail.addTextChangedListener(this);
        binding.etLoginpassword.addTextChangedListener(this);
        binding.cbRemember.setOnClickListener(this);
        binding.tvLoginFacebook.setOnClickListener(this);
        binding.tvLoginGoogle.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (checkValidation()) {
                   /* setRememberMeValueInPref();
                    callLoginAPI();*/
                    CommonUtils.savePreferencesString(mContext,AppConstants.USER_NAME,"John Thomas");

                    ActivityController.startActivity(LoginActivity.this, HomeActivity.class);


                }
                CommonUtils.hideSoftKeyboard(this);
                break;


            case R.id.tvForgotPass:
                ActivityController.startActivity(LoginActivity.this, ForgotPasswordActivity.class);
                CommonUtils.hideSoftKeyboard(this);
                break;

            case R.id.llSignup:
                ActivityController.startActivity(this, SignUpActivity.class);
                CommonUtils.hideSoftKeyboard(this);
                break;

            case R.id.fl_row:
                CommonUtils.hideSoftKeyboard(this);
                break;

            case R.id.tvLoginFacebook:

              /*  if (CommonUtils.isOnline(mContext)) {
                    fbLogin();
                } else {
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                }
*/
                break;

            case R.id.tvLoginGoogle:
           /*     if(CommonUtils.isOnline(mContext)) {
                    loginWithGooglePlus();
                } else {
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                }
*/
                break;

            default:
                break;
        }

    }

    private void setRememberMeValueInPref() {
        if (binding.cbRemember.isChecked()) {
            PrefManager.getInstance(mContext).setUserNameNum(binding.etLoginemail.getText().toString().trim());
            PrefManager.getInstance(mContext).setUserPassword(binding.etLoginpassword.getText().toString().trim());
            PrefManager.getInstance(mContext).setKeyIsRememberMe(true);
        }else
            {
            PrefManager.getInstance(mContext).setKeyIsRememberMe(false);
        }
    }

    private boolean checkValidation() {

        if (TextUtils.isDigitsOnly(binding.etLoginemail.getText().toString().trim())) {
            if (TextUtils.isEmpty(binding.etLoginemail.getText().toString())) {
                binding.tvLoginemailerror.setVisibility(View.VISIBLE);
                binding.tvLoginemailerror.setText(R.string.usernamemobileblanckerror);
                return false;
            }/*else if (!binding.etLoginemail.toString().matches("^[^\\s]+$")) {
                binding.tvLoginemailerror.setVisibility(View.VISIBLE);
                binding.tvLoginemailerror.setText(R.string.validmobilelength);
                return false;
            }*/ else if (CommonUtils.lengthFieldValidation(binding.etLoginemail.getText().toString(), 8)) {
                binding.tvLoginemailerror.setVisibility(View.VISIBLE);
                binding.tvLoginemailerror.setText(R.string.validmobilenoerror);
                return false;
            } else if (TextUtils.isEmpty(binding.etLoginpassword.getText().toString())) {
                binding.tvLoginemailerror.setVisibility(View.GONE);
                binding.tvLoginpassworderror.setVisibility(View.VISIBLE);
                binding.tvLoginpassworderror.setText(R.string.enterpassworderror);
                return false;
            } else if (CommonUtils.lengthFieldValidation(binding.etLoginpassword.getText().toString(), 8)) {
                binding.tvLoginpassworderror.setVisibility(View.VISIBLE);
                binding.tvLoginpassworderror.setText(R.string.validpassworderror);
                return false;
            } else {
                return true;
            }
        } else {
            if (TextUtils.isEmpty(binding.etLoginemail.getText().toString())) {
                binding.tvLoginemailerror.setVisibility(View.VISIBLE);
                binding.tvLoginemailerror.setText(R.string.usernamemobileblanckerror);
                return false;
            } else if (CommonUtils.lengthFieldValidation(binding.etLoginemail.getText().toString(), 2)) {
                binding.tvLoginemailerror.setVisibility(View.VISIBLE);
                binding.tvLoginemailerror.setText(R.string.validusermimimumlength);
                return false;

            } else if (TextUtils.isEmpty(binding.etLoginpassword.getText().toString())) {
                binding.tvLoginemailerror.setVisibility(View.GONE);
                binding.tvLoginpassworderror.setVisibility(View.VISIBLE);
                binding.tvLoginpassworderror.setText(R.string.enterpassworderror);
                return false;
            } else if (CommonUtils.lengthFieldValidation(binding.etLoginpassword.getText().toString(), 8)) {
                binding.tvLoginpassworderror.setVisibility(View.VISIBLE);
                binding.tvLoginpassworderror.setText(R.string.validpassworderror);
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (TextUtils.isDigitsOnly(binding.etLoginemail.getText().toString().trim())) {
            binding.etLoginemail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
        } else {
            binding.etLoginemail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});

        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(binding.etLoginemail.getText().toString())) {
            binding.tvLoginemailerror.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(binding.etLoginpassword.getText().toString())) {
            binding.tvLoginpassworderror.setVisibility(View.GONE);
        }
    }

    private void fbLogin() {
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // LogUtils.LOGE("Gson", "response Gson::"+new Gson().toJsonTree(response));
                                String fbID = "";
                                String fbName = "";
                                String fbFName = "";
                                String fbLName = "";
                                String fbGender = "";
                                String fbEmail = "";
                                String fbDOB = "";
                                String fbImage = "";
                                String fbPhone = "";
                                String fbCity = "";
                                String imageFacebook = "";

                                if (object != null) {
                                    fbID = object.optString("id");
                                    fbName = object.optString("name");
                                    fbEmail = object.optString("email");
                                    fbFName = object.optString("first_name");
                                    fbLName = object.optString("last_name");
                                    fbGender = object.optString("gender");
                                    fbDOB = object.optString("birthday");
                                    fbPhone = object.optString("phone_number");
                                    fbCity = object.optString("city");
                                    //prefManager.setKeySocialId(object.optString("id"));
                                    fbImage = "https://graph.facebook.com/" + fbID + "/picture?type=large";

                                    UserModel userModel = new UserModel();
                                    userModel.setProvider(AppConstants.FACEBOOK);
                                    userModel.setEmail(TextUtils.isEmpty(fbEmail)?fbID+"@facebook.com": fbEmail);
                                    userModel.setUid(fbID);
                                    userModel.setName(fbFName +" "+fbLName);
                                    userModel.setImage(fbImage);
                                    callSocialLoginAPI(userModel);
                                }
                            }
                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,name,gender,email,picture.width(200)");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
//                        DisplayUtils.showToast(mContext, getString(R.string.login_cancelled));
                    }

                    @Override
                    public void onError(FacebookException exception) {
//                        DisplayUtils.showToast(mContext, getString(R.string.login_error));
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     /*   if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }*/

    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount signInResult=result.getSignInAccount();
            UserModel userModel=new UserModel();
            userModel.setUid(signInResult.getIdToken());
            userModel.setEmail(TextUtils.isEmpty(signInResult.getEmail()) ? signInResult.getId()+"@gmail.com": signInResult.getEmail());
            userModel.setName(signInResult.getDisplayName());
            userModel.setImage(signInResult.getPhotoUrl()+"");
            userModel.setProvider(AppConstants.GMAIL);
            callSocialLoginAPI(userModel);
        }

    }


    /**
     * Google PLus Login
     */
    private void loginWithGooglePlus() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    /**
     * method for login api
     */
    private void callLoginAPI() {
        if(CommonUtils.isOnline(mContext)) {

            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setUser(new UserModel());
            apiRequest.getUser().setEmail(binding.etLoginemail.getText().toString().trim());
            apiRequest.getUser().setPassword(binding.etLoginpassword.getText().toString().trim());
            apiRequest.setDevice(new DeviceModel());
            apiRequest.getDevice().setDevice_token(PrefManager.getInstance(mContext).getKeyDeviceToken());
            apiRequest.getDevice().setDevice_type(AppConstants.DEVICE_TYPE);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiLogin(apiRequest);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();

                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            PrefManager.getInstance(LoginActivity.this).saveUser(response.body().getUser());
                            CommonUtils.savePreferencesString(mContext,AppConstants.USER_ID,response.body().getUser().getId());
                            CommonUtils.savePreferencesString(mContext,AppConstants.USER_NAME,response.body().getUser().getName());
                            CommonUtils.savePreferencesString(mContext,AppConstants.USER_PROFILE_IMAGE,response.body().getUser().getImage());

                            UserTable table=new UserTable(mContext);
                            table.insertUserInformation(response.body().getUser());
                            table.closeDB();
                            callHomeActivity(response.body());
                        } else if (response.body().getResponseCode() == AppConstants.USER_NOT_VERIFIED){

                            DialogUtils.dilaogOkWithInterFace(mContext, response.body().getResponseMessage(), new OkCancelInterface() {
                                @Override
                                public void onSuccess() {
                                    PrefManager.getInstance(LoginActivity.this).saveUser(response.body().getUser());
                                    startActivity(new Intent(mContext, OtpActivity.class)
                                            .putExtra(AppConstants.FROM, AppConstants.FROM_LOGIN)
                                            .putExtra(AppConstants.USER_DATA, response.body())
                                    );
                                }

                                @Override
                                public void onCancel() {

                                }
                            });


                        }
                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                            startActivity(new Intent(mContext, LoginActivity.class));
                            finishAffinity();
                            CommonUtils.clearPrefData(mContext);
                        }else {
                            ToastUtils.showToastShort(mContext, ""+response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(mContext,getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }


    /**
     * method for social Login api
     */
    private void callSocialLoginAPI(UserModel userModel) {
        if(CommonUtils.isOnline(mContext)) {

            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setUser(new UserModel());
            apiRequest.getUser().setUid(userModel.getUid());
            apiRequest.getUser().setEmail(userModel.getEmail());
            apiRequest.getUser().setProvider(userModel.getProvider());
            apiRequest.getUser().setName(userModel.getName());
            apiRequest.getUser().setImage(userModel.getImage());
            apiRequest.setDevice(new DeviceModel());
            apiRequest.getDevice().setDevice_token(PrefManager.getInstance(mContext).getKeyDeviceToken());
            apiRequest.getDevice().setDevice_type(AppConstants.DEVICE_TYPE);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiSocialLogin(apiRequest);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            if(response.body().getUser()!=null){
                                PrefManager.getInstance(LoginActivity.this).saveUser(response.body().getUser());
                                UserTable table=new UserTable(mContext);
                                table.insertUserInformation(response.body().getUser());
                                table.closeDB();
                            }
                            if(response.body().getUser() !=null && !TextUtils.isEmpty(response.body().getUser().getExisting())
                                    && response.body().getUser().getExisting().equalsIgnoreCase("no")){
                                callSuggestionActivity(response.body());
                            }
                            else {
                                CommonUtils.savePreferencesString(mContext,AppConstants.USER_NAME,response.body().getUser().getName());
                                CommonUtils.savePreferencesString(mContext,AppConstants.USER_PROFILE_IMAGE,response.body().getUser().getImage());
                                CommonUtils.savePreferencesString(mContext,AppConstants.USER_ID,response.body().getUser().getId());
                                callHomeActivity(response.body());
                            }
                        }
                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                            startActivity(new Intent(mContext, LoginActivity.class));
                            finishAffinity();
                            CommonUtils.clearPrefData(mContext);
                        }
                        else {
                            ToastUtils.showToastShort(mContext,response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(mContext,getString(R.string.server_error_msg));
                    }

                }
                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }



    private void callHomeActivity(ApiResponse response) {
        CommonUtils.saveUserData(mContext, response.getUser()); // save user data
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
       finishAffinity();
    }

    private void callSuggestionActivity(ApiResponse response) {

      CommonUtils.saveUserData(mContext, response.getUser()); // save user data

        Intent intent = new Intent(mContext, SuggestionsActivity.class);
        intent.putExtra(AppConstants.USER_DATA, response);
        startActivity(intent);
        finishAffinity();
    }

}
