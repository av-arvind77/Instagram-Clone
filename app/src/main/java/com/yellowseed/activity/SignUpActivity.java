package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.video.VideoApp;
import com.quickblox.sample.video.services.CallService;
import com.quickblox.sample.video.util.QBResRequestExecutor;
import com.quickblox.sample.video.utils.Consts;
import com.quickblox.sample.video.utils.QBEntityCallbackImpl;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.countrypicker.CountryPicker;
import com.yellowseed.countrypicker.CountryPickerListener;
import com.yellowseed.databinding.ActivitySignUpBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.imageUtils.CropImage;
import com.yellowseed.imageUtils.TakePictureUtils;
import com.yellowseed.model.reqModel.DeviceModel;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.ConverterUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Iso2Phone;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.yellowseed.fragments.HomeBottomFragment.TAKE_PICTURE;
import static com.yellowseed.imageUtils.TakePictureUtils.CROP_FROM_CAMERA;

public class SignUpActivity extends BaseActivity implements TextWatcher {

    private ActivitySignUpBinding binding;
    private Context mContext;
    private String selectedGender;

    private String image = "", base64_value = "", path = "", lookingFor = "", countryCodeStr = "+91";
    ;
    private Intent intent;
    private File file;
    private MultipartBody.Part imageFile = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        mContext = SignUpActivity.this;
        setOnClickListener();
        initializedControl();
        setToolBar();
        setOnClickListener();

    }

    @Override
    public void initializedControl() {

        //Setting country code in spinner
        ArrayAdapter<String> arrayAdapterContact = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, Iso2Phone.getaCountryCodeNew());
//        ArrayAdapter<String> arrayAdapterContact = ArrayAdapter.createFromResource(mContext,/* R.array.country_code*/Iso2Phone.getCountryCode(), android.R.layout.simple_spinner_item);
        binding.spCountryCode.setAdapter(arrayAdapterContact);


        //Setting gender in spinner
        String s = getString(R.string.bytapping);
        SpannableString ss = new SpannableString(s);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View CustomRo) {

                startActivity(new Intent(SignUpActivity.this, TermsAndConditionActivity.class).putExtra("from", SignUpActivity.class.getSimpleName()));

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 52, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 52, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new UnderlineSpan(), 52, ss.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 11, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView textView = (TextView) findViewById(R.id.tvTermsAndCondition);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());


    }


    @Override
    public void setToolBar() {

        binding.toolbarSignup.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarSignup.tvHeader.setText(R.string.sigup);
        binding.toolbarSignup.tvHeader.setVisibility(View.VISIBLE);
    }

    @Override
    public void setOnClickListener() {
        binding.btnSignupCreateAccount.setOnClickListener(this);
        binding.toolbarSignup.ivLeft.setOnClickListener(this);
        binding.etNameSignup.addTextChangedListener(this);
        binding.etMobileNumberSignup.addTextChangedListener(this);
        binding.etEmailSignup.addTextChangedListener(this);
        binding.etPasswordSignup.addTextChangedListener(this);
        binding.etConfirmPassSignup.addTextChangedListener(this);
        binding.llSignup.setOnClickListener(this);
        binding.tvLogIn.setOnClickListener(this);
        binding.ivCamera.setOnClickListener(this);
        binding.tvCountryCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignupCreateAccount:
                if (checkValidation()) {
                    CommonUtils.savePreferencesString(mContext,AppConstants.USER_NAME,"John Thomas");
                    intent = new Intent(this, OtpActivity.class);
                    intent.putExtra(AppConstants.FROM, AppConstants.SIGNUP);
                    startActivity(intent);


                 //   callSignUpAPI();
                    // callSignUpQB();
                }
                //ActivityController.startActivity((Activity) mContext, OtpActivity.class, true);
                break;

            case R.id.ivLeft:
                finish();
                break;
            case R.id.tvCountryCode:
                final CountryPicker picker = CountryPicker.newInstance("Select Country Code");
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code) {
                        countryCodeStr = code;
                        Log.d(TAG, "countryCodeStr : " + countryCodeStr);
                        binding.tvCountryCode.setText("+" + countryCodeStr);
                        picker.dismiss();
                    }
                });
                picker.show(getSupportFragmentManager(), "COUNTRY_CODE_PICKER");
                break;

            case R.id.tvLogIn:
                finish();
                break;
            case R.id.ivCamera:
                if (!CheckPermission.checkCameraPermission(mContext)) {
                    CheckPermission.requestCameraPermission((Activity) mContext, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    addPhotoDialog();
                }
                break;

            case R.id.llSignup:
                CommonUtils.hideSoftKeyboard(this);
                break;

            default:
                break;
        }
    }


    private void addPhotoDialog() {

        final CharSequence[] items = {getString(R.string.take_a_photo), getString(R.string.from_gallery)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.choose_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.take_a_photo))) {
                    image = System.currentTimeMillis() + "_photo.png";
                    takePicture((Activity) mContext, image);

                } else if (items[item].equals(getString(R.string.from_gallery))) {
                    image = System.currentTimeMillis() + "_photo.png";

                    openGallery();
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        builder.setCancelable(true);
    }

    public void takePicture(Activity context, String fileName) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri;
            mImageCaptureUri = Uri.fromFile(new File(context.getExternalFilesDir("temp"), fileName + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, TAKE_PICTURE);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, TakePictureUtils.PICK_GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == TakePictureUtils.PICK_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream inputStream = mContext.getContentResolver().openInputStream(intent.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(this.mContext.getExternalFilesDir("temp"), image + ".jpg"));
                    TakePictureUtils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage(this, image + ".jpg");
                } catch (Exception e) {
                    //CommonUtils.showToast(mContext, getString(R.string.error_in_picture));

                }
            }
        } else if (requestCode == TakePictureUtils.TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                startCropImage(this, image + ".jpg");
            }
        } else if (requestCode == CROP_FROM_CAMERA) {
            //  imageName="picture";
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    path = intent.getStringExtra(CropImage.IMAGE_PATH);
                    File imageFile = new File(path);
                    /*if (imageFile.exists()) {
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), imageFile);
                    }*/
                }
                if (path == null) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                base64_value = "data:image/jpeg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT).trim();
                base64_value = base64_value.replace("\n", "");
                file = new File(path);

                if (path != null && !path.isEmpty()) {
                    Picasso.with(mContext).load(new File(path)).placeholder(R.drawable.user_placeholder).
                            transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivProfilePic);
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    addPhotoDialog();
                } else {
                    CommonUtils.showToast(mContext, "Permission denial");
                }
                break;
        }
    }

    /**
     * this method is used for open crop image
     */
    public void startCropImage(Activity context, String fileName) {
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp"), fileName).getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra(CropImage.OUTPUT_X, 600);
        intent.putExtra(CropImage.OUTPUT_Y, 600);
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    //Method to check validation on login button click.

    private boolean checkValidation() {
        clearError();
        if (TextUtils.isEmpty(binding.etNameSignup.getText().toString().trim())) {

            clearError();
            binding.tvNameErrorSignup.setVisibility(View.VISIBLE);
            binding.tvNameErrorSignup.setText(R.string.enternameerror);

            return false;
        } else if (CommonUtils.lengthFieldValidation(binding.etNameSignup.getText().toString(), 2)) {
            clearError();
            binding.tvNameErrorSignup.setVisibility(View.VISIBLE);
            binding.tvNameErrorSignup.setText(R.string.validnameerror);
            return false;

        } else if (TextUtils.isEmpty(binding.etMobileNumberSignup.getText().toString().trim())) {
            clearError();
            binding.tvSignupMobileError.setVisibility(View.VISIBLE);
            binding.tvSignupMobileError.setText(R.string.entermobilenoerror);
            return false;
        } else if (CommonUtils.lengthFieldValidation(binding.etMobileNumberSignup.getText().toString(), 8)) {
            clearError();
            binding.tvSignupMobileError.setVisibility(View.VISIBLE);
            binding.tvSignupMobileError.setText(R.string.validmobilenoerror);
            return false;
        } else if (!TextUtils.isEmpty(binding.etEmailSignup.getText().toString().trim()) && !CommonUtils.isValidEmail(binding.etEmailSignup.getText().toString().trim())) {
            clearError();
            binding.tvEmailErrorSignup.setVisibility(View.VISIBLE);
            binding.tvEmailErrorSignup.setText(R.string.entervalidemailerror);
            return false;
        } else if (TextUtils.isEmpty(binding.etPasswordSignup.getText().toString().trim())) {

            clearError();
            binding.tvPassError.setVisibility(View.VISIBLE);
            binding.tvPassError.setText(R.string.enterpassworderror);
            return false;

        } else if (CommonUtils.lengthFieldValidation(binding.etPasswordSignup.getText().toString(), 8)) {
            clearError();
            binding.tvPassError.setVisibility(View.VISIBLE);
            binding.tvPassError.setText(R.string.validpassworderror);
            return false;

        } else if (TextUtils.isEmpty(binding.etConfirmPassSignup.getText().toString().trim())) {
            clearError();
            binding.tvConfirmPassErrorSignup.setVisibility(View.VISIBLE);
            binding.tvConfirmPassErrorSignup.setText(R.string.matchingpassworderror);
            return false;
        } else if (!binding.etPasswordSignup.getText().toString().equalsIgnoreCase(binding.etConfirmPassSignup.getText().toString())) {
            clearError();
            binding.tvConfirmPassErrorSignup.setVisibility(View.VISIBLE);
            binding.tvConfirmPassErrorSignup.setText(R.string.matchingpassworderror);
            return false;
        } else {
            return true;
        }

    }

    private void clearError() {
        binding.tvNameErrorSignup.setVisibility(View.GONE);
        binding.tvSignupMobileError.setVisibility(View.GONE);
        binding.tvEmailErrorSignup.setVisibility(View.GONE);
        binding.tvConfirmPassErrorSignup.setVisibility(View.GONE);
        binding.tvPassError.setVisibility(View.GONE);
        binding.tvGenderError.setVisibility(View.GONE);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (!TextUtils.isEmpty(binding.etNameSignup.getText().toString())) {
            binding.tvNameErrorSignup.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(binding.etMobileNumberSignup.getText().toString())) {
            binding.tvSignupMobileError.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(binding.etEmailSignup.getText().toString())) {
            binding.tvEmailErrorSignup.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(binding.etPasswordSignup.getText().toString())) {
            binding.tvPassError.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(binding.etConfirmPassSignup.getText().toString())) {
            binding.tvConfirmPassErrorSignup.setVisibility(View.GONE);
        }
    }


    private void callSignUpAPI() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            Map<String, RequestBody> requestBodyMap = new HashMap<>();

            if (binding.etEmailSignup.getText().toString().trim().length() > 0) {
                requestBodyMap.put("email", ConverterUtils.parseString(binding.etEmailSignup.getText().toString().trim()));
            }
            requestBodyMap.put("password", ConverterUtils.parseString(binding.etPasswordSignup.getText().toString().trim()));
            requestBodyMap.put("password_confirmation", ConverterUtils.parseString(binding.etPasswordSignup.getText().toString().trim()));
            requestBodyMap.put("name", ConverterUtils.parseString(binding.etNameSignup.getText().toString().trim()));
            String code = binding.tvCountryCode.getText().toString().trim();
            requestBodyMap.put("code", ConverterUtils.parseString(countryCodeStr));
            requestBodyMap.put("mobile", ConverterUtils.parseString(binding.etMobileNumberSignup.getText().toString().trim()));

            if (file != null && file.exists()) {
                imageFile = ConverterUtils.getMultipartFromFile("image", file);
            }

            Log.e(TAG, new Gson().toJsonTree(requestBodyMap).toString());
            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiUserSignUp(requestBodyMap, imageFile);
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
                            PrefManager.getInstance(SignUpActivity.this).saveUser(response.body().getUser());
                            CommonUtils.savePreferencesString(mContext,AppConstants.USER_NAME,response.body().getUser().getName());
                            CommonUtils.savePreferencesString(mContext,AppConstants.USER_PROFILE_IMAGE,response.body().getUser().getImage());

                            callOtpActivity(response.body());
                        } else {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        }
                    } else {
                        ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    private void callOtpActivity(ApiResponse data) {
        Intent intent = new Intent(mContext, OtpActivity.class);
        intent.putExtra(AppConstants.USER_DATA, data);
        intent.putExtra(AppConstants.FROM, AppConstants.SIGNUP);
        startActivity(intent);
        finishAffinity();
    }

}
