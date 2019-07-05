package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.adapter.GroupEditAdapter;
import com.yellowseed.databinding.ActivityNewGroupDoneBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.imageUtils.CropImage;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.UserListModel;
import com.yellowseed.model.resModel.GroupMemberResonse;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.ConverterUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.LogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yellowseed.imageUtils.TakePictureUtils.CROP_FROM_CAMERA;

public class GroupEditActivity extends BaseActivity {
    private static final int TAKE_PICTURE = 1;
    private ActivityNewGroupDoneBinding binding;
    private Context context;
    private GroupEditAdapter adapter;
    private ArrayList<GroupMemberResonse.MembersBean> models = new ArrayList<>();
    private String image = "", base64_value = "", path = "", lookingFor = "";
    private MultipartBody.Part groupImage = null;
    private File imageFile;
    private int myPosition;
    private String assocId;
    private ArrayList<UserListModel> memberList = new ArrayList<>();
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_group_done);
        context = GroupEditActivity.this;
        getIntentValue();
        initializedControl();
        setToolBar();
        setOnClickListener();
        if(CommonUtils.isOnline(context)) {
            callGroupMemberListAPI(true);
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

    private void getIntentValue() {
        if (getIntent() != null && getIntent().getExtras() != null){
            if (getIntent().getStringExtra(AppConstants.FROM) != null){
                if (getIntent().getStringExtra(AppConstants.FROM).equalsIgnoreCase(NewGroupNextActivity.class.getSimpleName())){

                    if (getIntent().getStringExtra(AppConstants.NAME) != null){
                        name = getIntent().getStringExtra(AppConstants.NAME);
                        binding.etGroupName.setText(getIntent().getStringExtra(AppConstants.NAME));
                    }

                    if (getIntent().getStringExtra(AppConstants.ASSOC_ID) != null){
                        assocId = getIntent().getStringExtra(AppConstants.ASSOC_ID);
                    }

                    if (getIntent().getStringExtra(AppConstants.USER_IMAGE) != null){
//                        image = getIntent().getStringExtra(AppConstants.USER_IMAGE);
                        Picasso.with(context).load(getIntent().getStringExtra(AppConstants.USER_IMAGE)).transform(new CircleTransformation()).into(binding.ivGroupPic);
                    }

                    if (getIntent().getSerializableExtra(AppConstants.PARTICIPANTS) != null){
                        memberList = (ArrayList<UserListModel>)getIntent().getSerializableExtra(AppConstants.PARTICIPANTS);
                        if (memberList != null && memberList.size() > 0) {
                            if (CommonUtils.isOnline(context)) {
                                callAddGroupMemberAPI();
                            } else {
                                ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
                            }
                        }
                    }
                }
            }
        }

        if (getIntent() != null && getIntent().getExtras() != null ){

            if (getIntent().getStringExtra(AppConstants.NAME) != null){
                name = getIntent().getStringExtra(AppConstants.NAME);
                binding.etGroupName.setText(getIntent().getStringExtra(AppConstants.NAME));
            }

            if (getIntent().getStringExtra(AppConstants.ASSOC_ID) != null){
                assocId = getIntent().getStringExtra(AppConstants.ASSOC_ID);
            }

            if (getIntent().getStringExtra(AppConstants.USER_IMAGE) != null){
                image = getIntent().getStringExtra(AppConstants.USER_IMAGE);
                Picasso.with(context).load(getIntent().getStringExtra(AppConstants.USER_IMAGE)).into(binding.ivGroupPic);
            }
        }
    }

    @Override
    public void initializedControl() {
        groupDoneRecyclerView();

        //Setting the image from gallery or camra
        setImagePost();
    }

    private void groupDoneRecyclerView() {
        binding.rvNewGroupDone.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvNewGroupDone.setLayoutManager(layoutManager);
        adapter = new GroupEditAdapter(models, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                myPosition = position;
                switch (view.getId()) {
                    case R.id.btnGroupUserRemove:
                        if (CommonUtils.isOnline(context)) {
                            //callRemoveGroupMemberAPI(models.get(position).getId());
                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
                        }
                        break;
                    default:
                        break;

                }
            }
        });
        binding.rvNewGroupDone.setAdapter(adapter);
    }

    @Override
    public void setToolBar() {
        binding.toolbarNewGroupDone.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarNewGroupDone.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarNewGroupDone.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarNewGroupDone.tvHeader.setText(R.string.editgroup);
        binding.toolbarNewGroupDone.ivRight.setVisibility(View.GONE);
        binding.toolbarNewGroupDone.tvRighttext.setVisibility(View.VISIBLE);
        binding.toolbarNewGroupDone.tvRighttext.setText(R.string.done);
        binding.toolbarNewGroupDone.tvRighttext.setTextColor(getResources().getColor(R.color.blueissh));
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarNewGroupDone.ivLeft.setOnClickListener(this);
        binding.toolbarNewGroupDone.tvRighttext.setOnClickListener(this);
        binding.tvAddParticipants.setOnClickListener(this);
        binding.ivGroupPic.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.tvRighttext:
                if (!binding.etGroupName.getText().toString().trim().isEmpty()) {
                    if (CommonUtils.isOnline(context)) {
                        callEditGroupAPI(binding.etGroupName.getText().toString().trim());
                    } else {
                        ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
                    }
                }
                break;
            case R.id.tvAddParticipants:
                startActivity(new Intent(context, NewGroupNextActivity.class).
                        putExtra(AppConstants.FROM, GroupEditActivity.class.getSimpleName()).
                        putExtra(AppConstants.NAME, name).
                        putExtra(AppConstants.ASSOC_ID, assocId).
                        putExtra(AppConstants.USER_IMAGE, image));
                finish();
                break;
            case R.id.ivGroupPic:
                if (!CheckPermission.checkCameraPermission(context)) {
                    CheckPermission.requestCameraPermission((Activity) context, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    addPhotoDialog();
                }
                break;
            default:
                break;
        }
    }
    private void addPhotoDialog() {
        final CharSequence[] items = {getString(R.string.take_a_photo), getString(R.string.from_gallery)};
        new MaterialDialog.Builder(context)
                .title(R.string.choose_photo).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeStory()))
                .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                        if (items[position].equals(getString(R.string.take_a_photo))) {
                            image = System.currentTimeMillis() + "_photo.png";
                            takePicture((Activity) context, image);

                        } else if (items[position].equals(getString(R.string.from_gallery))) {
                            image = System.currentTimeMillis() + "_photo.png";

                            openGallery();
                        } else {
                            dialog.dismiss();
                        }


                    }
                }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }
    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, com.yellowseed.imageUtils.TakePictureUtils.PICK_GALLERY);
    }
    /*
   This method is being used for taking picture from camera
   */
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

    private void setImagePost() {

        if (getIntent() != null && getIntent().getStringExtra(AppConstants.IMAGE_PATH) != null){
            path = getIntent().getStringExtra(AppConstants.IMAGE_PATH);
            Picasso.with(context).load(new File(path)).into(binding.ivGroupPic);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == com.yellowseed.imageUtils.TakePictureUtils.PICK_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(intent.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(this.context.getExternalFilesDir("temp"), image + ".jpg"));
                    com.yellowseed.imageUtils.TakePictureUtils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage(this, image + ".jpg");
                } catch (Exception e) {
                    //CommonUtils.showToast(mContext, getString(R.string.error_in_picture));

                }
            }
        } else if (requestCode == com.yellowseed.imageUtils.TakePictureUtils.TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                startCropImage(this, image + ".jpg");
            }
        } else if (requestCode == CROP_FROM_CAMERA) {
            //  imageName="picture";
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    path = intent.getStringExtra(CropImage.IMAGE_PATH);
                    imageFile = new File(path);
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

                Picasso.with(context).load(new File(path)).placeholder(R.mipmap.profile_icon).
                        transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivGroupPic);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //addPhotoDialog();
                } else {
                    CommonUtils.showToast(context, "Permission denial");
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

    /**
     * method for create post api
     */
    private void callEditGroupAPI(String groupName) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            RequestBody groupId = null;
            if (groupName != null) {
                groupId = ConverterUtils.parseString(assocId);
            }

            RequestBody name = null;
            if (groupName != null) {
                name = ConverterUtils.parseString(groupName);
            }

            if (imageFile != null) {
                groupImage = ConverterUtils.getMultipartFromFile("group_image", imageFile);
            }

            Call<ApiResponse> call = ApiExecutor.getClient(context).apiEditGroup(groupId, name, groupImage);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null){
//                                CommonUtils.showToast(context, response.body().getResponseMessage());
                                startActivity(new Intent(context, HomeActivity.class).putExtra(AppConstants.FROM, GroupEditActivity.class.getSimpleName()));
                                finish();
                            }
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog("Create room failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

    /**
     * method for create post api
     */
    private void callAddGroupMemberAPI() {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            RequestBody groupId = ConverterUtils.parseString(assocId);

            /*Tagged friend list*/
            Map<String,RequestBody> member_ids = new HashMap<>();
            for (int i = 0; i < memberList.size(); i++) {
                RequestBody requestBody = ConverterUtils.parseString(memberList.get(i).getId());
                member_ids.put("member_ids["+i+"]",requestBody);
            }

            Call<ApiResponse> call = ApiExecutor.getClient(context).apiAddGroupMember(groupId, member_ids);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null){
//                                CommonUtils.showToast(context, response.body().getResponseMessage());
                                callGroupMemberListAPI(false);
                            }
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog("Create room failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }




    /**
     * method to call group member api
     */
    private void callGroupMemberListAPI(final boolean isShowProgress) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            if (isShowProgress) {
                progressDialog.show();
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("group_id", assocId);

            Call<GroupMemberResonse> call = ApiExecutor.getClient(context).apiGroupMemberList(jsonObject);
            call.enqueue(new Callback<GroupMemberResonse>() {
                @Override
                public void onResponse(@NonNull Call<GroupMemberResonse> call, @NonNull final Response<GroupMemberResonse> response) {
                    if (isShowProgress) {
                        progressDialog.dismiss();
                    }
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
//                                CommonUtils.showToast(context, response.body().getResponseMessage());
                                models.clear();
                                if (response.body().getMembers() != null && response.body().getMembers().size() > 0) {
                                    models.addAll(response.body().getMembers());
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GroupMemberResonse> call, @NonNull Throwable t) {
                    if (isShowProgress) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }
}
