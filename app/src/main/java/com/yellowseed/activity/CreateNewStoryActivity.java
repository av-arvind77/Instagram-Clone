package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.yellowseed.R;
import com.yellowseed.adapter.SocialPostPhotoAdapter;
import com.yellowseed.databinding.ActivityPostBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.imageUtils.CropImage;
import com.yellowseed.imageUtils.TakePictureUtils;
import com.yellowseed.imageeditor.EditImageActivity;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SocialPostPhotoModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.model.reqModel.TagFriendsAttributes;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.ConverterUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.POST;

import static com.yellowseed.fragments.HomeBottomFragment.TAKE_PICTURE;
import static com.yellowseed.imageUtils.TakePictureUtils.CROP_FROM_CAMERA;
import static com.yellowseed.imageUtils.TakePictureUtils.startCropImage;
public class CreateNewStoryActivity extends BaseActivity implements PlaceSelectionListener {

    private static final int TAG_FRIEND = 1001;
    private Context mContext;
    private String path = "";
    private ActivityPostBinding binding;
    private static final int PLACE_PICKER_REQUEST = 110;
    public static final int POLL_INTENT = 9;
    private List<UserListModel> tagedFriendList = new ArrayList<>();
    private File imageFile;
    private LatLng latLong;
    private double latitude=0.0;
    private double longitude=0.0;
    private CharSequence checkedInPlaceName;
    private MultipartBody.Part imagePart;
    private SocialPostPhotoAdapter addImageAdapter;
    private String image;
    private ArrayList<SocialPostPhotoModel> imageList;
    private String base64_value;
    private File file;
    private SocialPostPhotoModel model = new SocialPostPhotoModel();
    private File firstImageFile;
    private String question="";
    private String option1="";
    private String option2="";
    private int initial_x;
    private int initial_y;
    private int initial_x_location;
    private int initial_y_location;
    private String placeAddress;
    private boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_post);
        mContext = CreateNewStoryActivity.this;
        tagedFriendList.clear();
        initializedControl();
        setToolBar();
        setRecyclerView();
        setImagePost();
        setOnClickListener();
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getIntentData(Intent intent) {

        if(intent.getStringExtra(AppConstants.QUESTION)!=null &&
                intent.getStringExtra(AppConstants.OPTION1)!=null &&
                intent.getStringExtra(AppConstants.OPTION2)!=null &&
                !intent.getStringExtra(AppConstants.QUESTION).equalsIgnoreCase("") &&
                !intent.getStringExtra(AppConstants.OPTION1).equalsIgnoreCase("") &&
                !intent.getStringExtra(AppConstants.OPTION2).equalsIgnoreCase("")
                ){
             question= intent.getStringExtra(AppConstants.QUESTION);
             option1= intent.getStringExtra(AppConstants.OPTION1);
             option2= intent.getStringExtra(AppConstants.OPTION2);
        }

            initial_x=intent.getIntExtra(AppConstants.INITIAL_X,0);
            initial_y=intent.getIntExtra(AppConstants.INITIAL_Y,0);
            initial_x_location=intent.getIntExtra(AppConstants.INITIAL_X_LOCATION,0);
            initial_y_location=intent.getIntExtra(AppConstants.INITIAL_Y_LOCATION,0);
    }



    public void setRecyclerView() {

        binding.rvImageView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.rvImageView.setLayoutManager(layoutManager);
        binding.rvImageView.setNestedScrollingEnabled(false);

        addImageAdapter = new SocialPostPhotoAdapter(mContext, imageList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.ivCross:
                        imageList.remove(position);
                        addImageAdapter.notifyItemRemoved(position);
                        break;
                    case R.id.ivAddImage:
                        openDialog();
                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvImageView.setAdapter(addImageAdapter);
    }

    private void setNumberOfTaggedFriend(){
        if (tagedFriendList != null) {
            binding.tvNumerOfTagFriends.setText(tagedFriendList.size()+" People");
        }
        else
        {
            binding.tvNumerOfTagFriends.setText("0"+" People");
        }
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        imageList = new ArrayList<>();
        setNumberOfTaggedFriend();

    }


    //Setting the image
    private void setImagePost() {
        if (getIntent() != null && getIntent().getStringExtra(AppConstants.IMAGE_PATH) != null){
            path = getIntent().getStringExtra(AppConstants.IMAGE_PATH);
            firstImageFile = new File(path);
            model.setFile(firstImageFile);
            model.setType("local");
            imageList.add(model);
            addImageAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void setToolBar() {
        binding.toolbarPost.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarPost.tvHeader.setText(R.string.story);
        binding.toolbarPost.ivLeft.setVisibility(View.VISIBLE);
    }

    @Override
    public void setOnClickListener() {

        binding.toolbarPost.ivLeft.setOnClickListener(this);
        binding.btnPost.setOnClickListener(this);
        binding.llPost.setOnClickListener(this);
        binding.tvCheckIn.setOnClickListener(this);
        binding.tvTagPeople.setOnClickListener(this);
        binding.tvPoll.setOnClickListener(this);
        binding.toolbarPost.tvRighttext.setOnClickListener(this);
        binding.tvPhotoVideo.setOnClickListener(this);
    }

    private void setCurrentTheme() {
        binding.viewLine2.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        binding.viewLine3.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));

        binding.viewLine4.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));

    }


        @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){

            case R.id.ivLeft:
              // startActivity(new Intent(this,HomeActivity.class));
                finish();
                break;

            case R.id.btnPost:
               /* if(CommonUtils.isOnline(mContext)) {
                    callCreateStoryAPI();
                }
                else
                {
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
                }*/
                CommonUtils.showToast(mContext, "Posted successfully.");
               finish();
                break;

            case R.id.llPost:
                CommonUtils.hideSoftKeyboard(this);
                break;

            case R.id.tvCheckIn:
                CommonUtils.getThePlace(this, PLACE_PICKER_REQUEST);
                break;

            case R.id.tvTagPeople:

                intent = new Intent(new Intent(this, TagFollowingActivity.class));
                if (tagedFriendList != null && tagedFriendList.size() > 0) {
                    intent.putExtra(AppConstants.TAG_FRIEND_LIST, (Serializable) tagedFriendList);
                }
                startActivityForResult(intent, TAG_FRIEND);

                break;

            case R.id.tvPoll:
                intent =new Intent(this,CreateNewPollActivity.class);
                intent.putExtra("image",imageList.get(0).getFile().getAbsolutePath());
                intent.putExtra("location",checkedInPlaceName);
                startActivityForResult(intent,POLL_INTENT);
                break;

            case R.id.tvRighttext:
                finish();
                break;

            case R.id.tvPhotoVideo:
                if (!CheckPermission.checkCameraPermission(mContext)) {
                    CheckPermission.requestCameraPermission((Activity) mContext, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    openDialog();
                }
                break;

            default:
                break;
        }
    }

    /**
     * this method is used to create dialog for choosing photo / video.
     */

    private void openDialog() {

        final CharSequence[] items = {getString(R.string.take_a_photo), getString(R.string.from_gallery)};
        new MaterialDialog.Builder(mContext)
                .title(R.string.choose_photo).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeStory()))
                .titleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                        if (items[position].equals(getString(R.string.take_a_photo))) {
                            image = System.currentTimeMillis() + "_photo.png";
                            takePicture((Activity) mContext, image);

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

    /*
   This method is being used for taking picture from gallery
   */
    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, TakePictureUtils.PICK_GALLERY);
    }
    public  void startCropImage(Activity context, String fileName) {
        Intent intent = new Intent(context, EditImageActivity.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp"), fileName).getPath());
        startActivityForResult(intent,CROP_FROM_CAMERA);
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
            mImageCaptureUri = Uri.fromFile(new File(this.getExternalFilesDir("temp"), fileName + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, TAKE_PICTURE);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == TakePictureUtils.PICK_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(intent.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(this.getExternalFilesDir("temp"), image + ".jpg"));
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
                 //  File imageFile = file;
                    file = new File(path);
                }
                if (path == null) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                base64_value = "data:image/jpeg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT).trim();
                base64_value = base64_value.replace("\n", "");
                model = new SocialPostPhotoModel();
                model.setFile(file);
                model.setType("local");
                imageList.add(model);
                addImageAdapter.notifyDataSetChanged();
            }

        }
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(mContext, intent);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(mContext, intent);
                this.onError(status);
            }
        }

        if (requestCode == TAG_FRIEND) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO Extract the data returned from the child Activity.
                if (intent != null && intent.getExtras() != null) {
                    tagedFriendList = (List<UserListModel>) intent.getSerializableExtra("TagFriendList");
                    binding.tvNumerOfTagFriends.setText(tagedFriendList.size() + " People");
                }
            }
        }

        if(requestCode==POLL_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                getIntentData(intent);
            }
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        if(place.getAddress()!=null && !place.getAddress().toString().equalsIgnoreCase("")){
            checkedInPlaceName = place.getAddress();
        }else{
            checkedInPlaceName="";
        }
        if(place.getLatLng()!=null && !place.getLatLng().toString().equalsIgnoreCase("")){
            latLong = place.getLatLng();
        }
        latitude = latLong.latitude;
        longitude = latLong.longitude;
        Log.e("Place", latLong.latitude+" "+latLong.longitude);
        binding.tvCheckInPlace.setText(place.getName());

    }

    @Override
    public void onError(Status status) {

    }

    /**
     * method for create story api
     */
    @POST
    private void callCreateStoryAPI() {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            imageFile = new File(path);

            RequestBody title = ConverterUtils.parseString("Story");
            RequestBody discription = ConverterUtils.parseString("discription");

            /*Tagged friend list*/
            Map<String,RequestBody> tagFriendsAttributes=new HashMap<>();
            for (int i = 0; i < tagedFriendList.size(); i++) {
                RequestBody requestBody = ConverterUtils.parseString(tagedFriendList.get(i).getId());
                tagFriendsAttributes.put("tag_friends_attributes["+i+"][user_id]",requestBody);
            }

            /*Poll attributes*/
            Map<String,RequestBody> poolAttributes=new HashMap<>();
            poolAttributes.put("poll_attributes[poll_title]", ConverterUtils.parseString(question));
            poolAttributes.put("poll_attributes[type1]", ConverterUtils.parseString(option1));
            poolAttributes.put("poll_attributes[type2]", ConverterUtils.parseString(option2));
            poolAttributes.put("poll_attributes[x_axis]", ConverterUtils.parseString(String.valueOf(initial_x)));
            poolAttributes.put("poll_attributes[y_axis]", ConverterUtils.parseString(String.valueOf(initial_y)));

            Map<String,RequestBody> checkInAttributes=new HashMap<>();
            if(checkedInPlaceName!=null&&checkedInPlaceName.length()>0){
                if(checkedInPlaceName!=null){
                    checkInAttributes.put("check_in_attributes[location]", ConverterUtils.parseString(checkedInPlaceName.toString()));
                }
                checkInAttributes.put("check_in_attributes[latitude]", ConverterUtils.parseString(String.valueOf(latitude)));
                checkInAttributes.put("check_in_attributes[longitude]", ConverterUtils.parseString(String.valueOf(longitude)));
                checkInAttributes.put("check_in_attributes[x_axis]", ConverterUtils.parseString(String.valueOf(initial_x_location)));
                checkInAttributes.put("check_in_attributes[y_axis]", ConverterUtils.parseString(String.valueOf(initial_y_location)));
            }


            /*images_attributes*/
            Map<String,RequestBody> imagesAttributes=new HashMap<>();
            List<MultipartBody.Part> parts = new ArrayList<>();

            for (int i = 0; i < imageList.size(); i++) {
                parts.add(ConverterUtils.getMultipartFromFile("images_attributes["+i+"][file]",imageList.get(i).getFile()));
                //imagesAttributes.put("images_attributes[0][file]", imagePart);
                imagesAttributes.put("images_attributes["+i+"][width]", ConverterUtils.parseString("555"));
                imagesAttributes.put("images_attributes["+i+"][height]",ConverterUtils.parseString("558"));
                imagesAttributes.put("images_attributes["+i+"][file_type]",ConverterUtils.parseString(".jpg"));
            }

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiCreateStory(title, discription, tagFriendsAttributes, poolAttributes, checkInAttributes,
                    imagesAttributes ,parts);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            tagedFriendList.clear();
                            startActivity(new Intent(CreateNewStoryActivity.this,HomeActivity.class));
                        }
                        else
                        {
                            ToastUtils.showToastShort(mContext,getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
}
