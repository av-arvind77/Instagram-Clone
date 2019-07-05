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
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.apradanas.simplelinkabletext.Link;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.yellowseed.R;
import com.yellowseed.adapter.SocialPostPhotoAdapter;
import com.yellowseed.databinding.ActivitySocialPostBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.imageUtils.CropImage;
import com.yellowseed.imageUtils.TakePictureUtils;
import com.yellowseed.imageeditor.EditImageActivity;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SocialPostPhotoModel;
import com.yellowseed.model.UserListModel;
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
import com.yellowseed.webservices.response.homepost.Post;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.yellowseed.fragments.HomeBottomFragment.TAKE_PICTURE;
import static com.yellowseed.imageUtils.TakePictureUtils.CROP_FROM_CAMERA;

public class SocialPostActivity extends BaseActivity implements PlaceSelectionListener {

    private static final int PLACE_PICKER_REQUEST = 110;
    // Remember some things for zooming
    PointF start = new PointF();
    private ActivitySocialPostBinding binding;
    private Context mContext;
    private boolean flag = true;
    private String image = "", base64_value = "", path = "";
    private File file;
    private ArrayList<SocialPostPhotoModel> imageList;
    private ArrayList<String> tagsNames = new ArrayList<>();
    private List<UserListModel> tagedFriendList = new ArrayList<>();
    private SocialPostPhotoAdapter addImageAdapter;
    private double placeLatitude, placeLongitude;
    private int TAG_FRIEND_IN_POST = 1005;
    private String postId = "", postDiscription, postCheckIn;
    private boolean postIsAllowComment;
    private Post postList;
    private Intent intent;
    private Link linkHashtag;
    private Themes themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_social_post);
        mContext = SocialPostActivity.this;
        tagedFriendList.clear();
        themes = new Themes(mContext);
        initializedControl();

      //  getIntentValue();
        setToolBar();
        setOnClickListener();
        setRecyclerView();
        setNumberOfTaggedFriend();
    }

    private void getIntentValue() {
        if (getIntent() != null && getIntent().getExtras() != null) {

            if (getIntent().getStringExtra(AppConstants.FROM) != null &&
                    getIntent().getStringExtra(AppConstants.FROM).equalsIgnoreCase(AppConstants.FROM_EDIT_POST)) {

                binding.btnPostSocial.setText(getString(R.string.update_post));

                if (getIntent().getSerializableExtra(AppConstants.POST_MODEL) != null) {
                    postList = (Post) getIntent().getSerializableExtra(AppConstants.POST_MODEL);
                }
                //Post Id
                postId = postList.getPost().getId();

                //Descrition
                postDiscription = postList.getPost().getDescription();
                //setTags(binding.etWriteSomething,postDiscription);
                binding.etWriteSomething.setText(postDiscription);
                //Check In
                postCheckIn = postList.getPost().getCheckIn();
                binding.tvAddress.setText(postCheckIn);

                //Allow comment
                postIsAllowComment = postList.getPost().getStatus();
                if (postIsAllowComment) {
                    binding.ivSwitch.setImageResource(R.mipmap.yellow_switch_img);
                    flag = true;
                } else {
                    binding.ivSwitch.setImageResource(R.mipmap.white_switch_img);
                    flag = false;
                }

                //Tag friend list
                if (postList != null && postList.getTagFriends() != null && postList.getTagFriends().size() > 0) {

                    for (int i = 0; i < postList.getTagFriends().size(); i++) {
                        UserListModel tagFriendsAttributes = new UserListModel();
                        tagFriendsAttributes.setId(postList.getTagFriends().get(i).getUserId());
                        tagedFriendList.add(tagFriendsAttributes);
                    }
                }
                //Image list
                for (int i = 0; i < postList.getImages().size(); i++) {
                    SocialPostPhotoModel socialPostPhotoModel = new SocialPostPhotoModel();
                    socialPostPhotoModel.setUrl(postList.getImages().get(i).getFile().getUrl());
                    socialPostPhotoModel.setId(postList.getImages().get(i).getId());
                    socialPostPhotoModel.setType("server");
                    socialPostPhotoModel.setFile_type(postList.getImages().get(i).getFileType());
                    socialPostPhotoModel.setThumbServer(postList.getImages().get(i).getFile().getThumb().getUrl());
                    imageList.add(socialPostPhotoModel);
                }
            }
        }
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        BitmapDrawable drawable = (BitmapDrawable) CommonUtils.getDrawable(mContext, R.mipmap.photo_video_ico);

        Bitmap bInput, bOutput;
        bInput = drawable.getBitmap();
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);

        linkHashtag = new Link(Pattern.compile("(#\\w+)"))
                .setUnderlined(true)
                .setTextStyle(Link.TextStyle.ITALIC)
                .setTextColor(Color.BLUE)
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        // do something
                    }
                });

        binding.etWriteSomething.addLink(linkHashtag);
        binding.ivPhoto.setImageBitmap(bOutput);
        imageList = new ArrayList<>();

    }


    private void setCurrentTheme() {

        binding.llPost.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
        binding.toolbarPostSocial.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
        binding.toolbarPostSocial.tvRighttext.setTextColor(ContextCompat.getColor(mContext, themes.setTolbarText()));
        binding.toolbarPostSocial.tvHeader.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.etWriteSomething.setBackground(ContextCompat.getDrawable(mContext, themes.setDarkSearchDrawable()));
        binding.etWriteSomething.setHintTextColor(ContextCompat.getColor(mContext, themes.setGreyHint()));
        binding.etWriteSomething.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.tvPhotoVideo.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.tvCheckIn.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.tvPeople.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.tvTagPeoplePost.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.tvCheckIn.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.tvAllowComments.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.tvAddress.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.viewLine1.setBackgroundColor(ContextCompat.getColor(mContext, themes.setViewLineGrey()));
        binding.viewLine2.setBackgroundColor(ContextCompat.getColor(mContext, themes.setViewLineGrey()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, themes.setViewLineGrey()));


        themes.changePostIconColor(mContext, binding.ivPhoto);
        themes.changePostIconColor(mContext, binding.ivCheckIn);
        themes.changePostIconColor(mContext, binding.ivTag);
        themes.changePostIconColor(mContext, binding.ivComment);
        themes.changeIconColor(mContext, binding.toolbarPostSocial.ivLeft);


    }

    private void setNumberOfTaggedFriend() {
        if (tagedFriendList != null) {
            binding.tvPeople.setText(tagedFriendList.size() + " People");
        } else {
            binding.tvPeople.setText("0" + " People");
        }
    }

    @Override
    public void setToolBar() {
        binding.toolbarPostSocial.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarPostSocial.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarPostSocial.tvHeader.setText(R.string.post);
    }

    @Override
    public void setOnClickListener() {

        binding.ivSwitch.setOnClickListener(this);
        binding.toolbarPostSocial.ivLeft.setOnClickListener(this);
        binding.tvPhotoVideo.setOnClickListener(this);
        binding.tvCheckIn.setOnClickListener(this);
        binding.tvTagPeoplePost.setOnClickListener(this);
        binding.btnPostSocial.setOnClickListener(this);
        binding.lllayout.setOnClickListener(this);
        binding.etWriteSomething.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ivSwitch:
                if (flag) {
                    binding.ivSwitch.setImageResource(R.mipmap.white_switch_img);
                    flag = false;
                } else {
                    binding.ivSwitch.setImageResource(R.mipmap.yellow_switch_img);
                    flag = true;
                }
                break;

            case R.id.ivLeft:
                onBackPressed();
                break;

            case R.id.tvPhotoVideo:
                if (!CheckPermission.checkCameraPermission(mContext)) {
                    CheckPermission.requestCameraPermission((Activity) mContext, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    addPhotoDialog();


                }
                break;


            case R.id.tvCheckIn:
                //Getting the place
                CommonUtils.getThePlace(this, PLACE_PICKER_REQUEST);
                break;

            case R.id.tvTagPeoplePost:
//                startActivityForResult(new Intent(this, TagFollowingActivity.class).putExtra(), TAG_FRIEND_IN_POST);

                intent = new Intent(new Intent(this, TagFollowingActivity.class));
                if (tagedFriendList != null && tagedFriendList.size() > 0) {
                    intent.putExtra(AppConstants.TAG_FRIEND_LIST, (Serializable) tagedFriendList);
                }
                startActivityForResult(intent, TAG_FRIEND_IN_POST);

                break;

            case R.id.etWriteSomething:
                binding.etWriteSomething.setCursorVisible(true);
                break;

            case R.id.btnPostSocial:
             /*   if (CommonUtils.isOnline(mContext)) {
                    if (binding.etWriteSomething.getText().toString().trim().length() > 0) {
                        tagsNames = new ArrayList<>();
                        Pattern pattern = Pattern.compile("#\\w+");
                        Matcher matcher = pattern.matcher(binding.etWriteSomething.getText().toString().trim());
                        while (matcher.find()) {
                            tagsNames.add(matcher.group());
                        }
                    }
                    if (binding.btnPostSocial.getText().toString().equalsIgnoreCase(getString(R.string.post))) {
                        callCreatePostAPI();
                    } else //Update Post
                    {
                        callUpdatePostAPI(postId);
                    }
                } else {
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                }*/

                if (binding.btnPostSocial.getText().toString().equalsIgnoreCase(getString(R.string.post))) {
                    ToastUtils.showToastShort(mContext, "Post successfully created.");

                } else //Update Post
                {
                    ToastUtils.showToastShort(mContext, "Post successfully updated.");

                }

                onBackPressed();
                break;

            case R.id.lllayout:
                binding.etWriteSomething.setCursorVisible(false);
                CommonUtils.hideSoftKeyboard(this);
                break;

            default:
                break;
        }
    }

    public void setRecyclerView() {

        binding.rvImageView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.rvImageView.setLayoutManager(layoutManager);
        binding.rvImageView.setNestedScrollingEnabled(false);

        if (imageList.size() == 0) {
            binding.rvImageView.setVisibility(View.GONE);
        } else {
            binding.rvImageView.setVisibility(View.VISIBLE);
        }

        addImageAdapter = new SocialPostPhotoAdapter(mContext, imageList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.ivCross:
                        imageList.remove(position);
                        addImageAdapter.notifyItemRemoved(position);
                        if (imageList.size() == 0) {
                            binding.rvImageView.setVisibility(View.GONE);
                        } else {
                            binding.rvImageView.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.ivAddImage:
                        addPhotoDialog();
                        break;

                    default:
                        break;
                }
            }
        });
        binding.rvImageView.setAdapter(addImageAdapter);
    }


    /**
     * this method is used to create dialog for choosing photo / video.
     */

   /* private void openDialog() {

        final CharSequence[] items = {getString(R.string.take_a_photo), getString(R.string.from_gallery)};
        new MaterialDialog.Builder(mContext)
                .title(R.string.choose_photo).titleGravity(GravityEnum.CENTER)
                .items(items)
                .titleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                .itemsColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeStory()))
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
*/
    private void addPhotoDialog() {
        final CharSequence[] items = {getString(R.string.take_a_photo), getString(R.string.from_gallery)};
        AlertDialog.Builder builder;
        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {

            builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
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

    /*
   This method is being used for taking picture from gallery
   */
    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/* video/*");
        startActivityForResult(photoPickerIntent, TakePictureUtils.PICK_GALLERY);
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == TakePictureUtils.PICK_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent.getData() != null && intent.getData().toString().contains("video")) {
                    String video = String.valueOf(System.currentTimeMillis() / 1000);
                    try {
                        InputStream inputStream = mContext.getContentResolver().openInputStream(intent.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(this.mContext.getExternalFilesDir("temp"), video + ".mp4"));
                        com.yellowseed.imageUtils.TakePictureUtils.copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File imageFile = new File(mContext.getExternalFilesDir("temp"), video + ".mp4");
                    SocialPostPhotoModel model = new SocialPostPhotoModel();
                    model.setFile(imageFile);
                    model.setUrl("");
                    model.setFile_type("video");
                    model.setType("local");
                    model.setThumbLocal(ThumbnailUtils.createVideoThumbnail(imageFile.getPath(), MediaStore.Video.Thumbnails.MICRO_KIND));
                    imageList.add(model);
                    addImageAdapter.notifyDataSetChanged();
                    //appVideo(imageFile);
                } else {
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
                    File imageFile = file;
                    /*if (imageFile.exists()) {
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), imageFile);
                    }*/
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

//                Picasso.with(mContext).load(file).placeholder(R.mipmap.img).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivSocialPost);


                SocialPostPhotoModel model = new SocialPostPhotoModel();
                model.setFile(file);
                model.setUrl("");
                model.setType("local");
                imageList.add(model);
                addImageAdapter.notifyDataSetChanged();
            }

            //Getting place address

        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(mContext, intent);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(mContext, intent);
                this.onError(status);
            }
        } else if (requestCode == TAG_FRIEND_IN_POST) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO Extract the data returned from the child Activity.
                if (intent != null && intent.getExtras() != null) {
                    tagedFriendList.clear();
                    tagedFriendList = (List<UserListModel>) intent.getSerializableExtra("TagFriendList");
                    binding.tvPeople.setText(tagedFriendList.size() + " People");
                }
            }
        }
        if (imageList.size() == 0) {
            binding.rvImageView.setVisibility(View.GONE);
        } else {
            binding.rvImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //addPhotoDialog();
                    addPhotoDialog();
                    /*SandriosCamera
                            .with(SocialPostActivity.this)
                            .setShowPicker(true)
                            .setShowPickerType(CameraConfiguration.PHOTO)
                            .setVideoFileSize(20)
                            .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                            .enableImageCropping(true)
                            .launchCamera(new SandriosCamera.CameraCallback() {
                                @Override
                                public void onComplete(CameraOutputModel model) {
                                    Log.e("File", "" + model.getPath());
                                    Log.e("Type", "" + model.getType());
                                    Toast.makeText(getApplicationContext(), "Media captured.", Toast.LENGTH_SHORT).show();
                                }
                            });*/
                } else {
                    CommonUtils.showToast(this, "Permission denial");
                }
                break;
        }
    }

    /**
     * this method is used for open crop image
     */
    public void startCropImage(Activity context, String fileName) {
        Intent intent = new Intent(context, EditImageActivity.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp"), fileName).getPath());
        startActivityForResult(intent, CROP_FROM_CAMERA);

    }

   /* @Override
    public void onPlaceSelected(Place place) {
        binding.tvAddress.setText(place.getName());
        placeLatitude = place.getLatLng().latitude;
        placeLongitude = place.getLatLng().longitude;
    }
*/

    @Override
    public void onPlaceSelected(Place place) {
        binding.tvAddress.setText(place.getName());
    }



    @Override
    public void onError(Status status) {

    }

    /**
     * method for create post api
     */
    /**
     * method for create post api
     */
    private void callCreatePostAPI() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            RequestBody title = ConverterUtils.parseString("");

            RequestBody discriptionRequest;
            if (!binding.etWriteSomething.getText().toString().isEmpty()) {
                String description = binding.etWriteSomething.getText().toString();
                discriptionRequest = ConverterUtils.parseString(description);
            } else {
                discriptionRequest = ConverterUtils.parseString("");
            }

            RequestBody placeAddressRequest;
            if (binding.tvAddress.getText().toString().trim() != null) {
                placeAddressRequest = ConverterUtils.parseString(binding.tvAddress.getText().toString().trim());
            } else {
                placeAddressRequest = ConverterUtils.parseString("");
            }

            RequestBody placeLatitudeRequest = ConverterUtils.parseString(String.valueOf(placeLatitude));
            RequestBody placeLongitudeRequest = ConverterUtils.parseString(String.valueOf(placeLongitude));
            RequestBody shareTypeRequest = ConverterUtils.parseString("public");
            RequestBody statusRequest = ConverterUtils.parseString(String.valueOf(flag));

            /*Tagged friend list*/
            Map<String, RequestBody> tagFriendsAttributes = new HashMap<>();
            for (int i = 0; i < tagedFriendList.size(); i++) {
                RequestBody requestBody = ConverterUtils.parseString(tagedFriendList.get(i).getId());
                tagFriendsAttributes.put("tag_friends_attributes[" + i + "][user_id]", requestBody);
            }

            Map<String, RequestBody> tagHashAttributes = new HashMap<>();
            if (tagsNames != null && tagsNames.size() > 0) {
                for (int i = 0; i < tagsNames.size(); i++) {
                    RequestBody requestBody = ConverterUtils.parseString(tagsNames.get(i));
                    tagHashAttributes.put("post_tags_attributes[" + i + "][tag_attributes][name]", requestBody);
                }
            }


            /*images_attributes*/
            Map<String, RequestBody> imagesAttributes = new HashMap<>();
            List<MultipartBody.Part> parts = new ArrayList<>();

            for (int i = 0; i < imageList.size(); i++) {
                parts.add(ConverterUtils.getMultipartFromFile("post_images_attributes[" + i + "][file]", imageList.get(i).getFile()));
                //imagesAttributes.put("images_attributes[0][file]", imagePart);
                imagesAttributes.put("post_images_attributes[" + i + "][width]", ConverterUtils.parseString("555"));
                imagesAttributes.put("post_images_attributes[" + i + "][height]", ConverterUtils.parseString("558"));
                if (imageList.get(i).getFile_type() != null && imageList.get(i).getFile_type().length() > 0 && imageList.get(i).getFile_type().equalsIgnoreCase("video")) {
                    imagesAttributes.put("post_images_attributes[" + i + "][file_type]", ConverterUtils.parseString("video"));
                } else {
                    imagesAttributes.put("post_images_attributes[" + i + "][file_type]", ConverterUtils.parseString("image"));
                }
            }


            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiCreatePost(title, discriptionRequest, placeAddressRequest,
                    placeLatitudeRequest, placeLongitudeRequest, shareTypeRequest, statusRequest, imagesAttributes, tagFriendsAttributes, tagHashAttributes, parts);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            tagedFriendList.clear();
                            startActivity(new Intent(SocialPostActivity.this, HomeActivity.class));
                        } else {
                            ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog("Social Post Failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


    /**
     * method for create post api
     */
    private void callUpdatePostAPI(String oldPostId) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            RequestBody postId = ConverterUtils.parseString(oldPostId);
            RequestBody title = ConverterUtils.parseString("");

            RequestBody discriptionRequest;
            if (!binding.etWriteSomething.getText().toString().isEmpty()) {
                String description = binding.etWriteSomething.getText().toString();
                discriptionRequest = ConverterUtils.parseString(description);
            } else {
                discriptionRequest = ConverterUtils.parseString("");
            }

            RequestBody placeAddressRequest;
            if (binding.tvAddress.getText().toString().trim() != null) {
                placeAddressRequest = ConverterUtils.parseString(binding.tvAddress.getText().toString().trim());
            } else {
                placeAddressRequest = ConverterUtils.parseString("");
            }

            RequestBody placeLatitudeRequest = ConverterUtils.parseString(String.valueOf(placeLatitude));
            RequestBody placeLongitudeRequest = ConverterUtils.parseString(String.valueOf(placeLongitude));
            RequestBody shareTypeRequest = ConverterUtils.parseString("public");
            RequestBody statusRequest = ConverterUtils.parseString(String.valueOf(flag));

            /*Tagged friend list*/
            Map<String, RequestBody> tagFriendsAttributes = new HashMap<>();
            List<UserListModel> tempTaggedUser = new ArrayList<>();
            tempTaggedUser.addAll(tagedFriendList);

            if (postList.getTagFriends() != null && postList.getTagFriends().size() > 0) {
                for (int i = 0; i < postList.getTagFriends().size(); i++) {
                    UserListModel model = new UserListModel();
                    model.setServer_id(postList.getTagFriends().get(i).getId());
                    model.setId(postList.getTagFriends().get(i).getUserId());
                    tempTaggedUser.add(model);
                }
            }

            if (tempTaggedUser.size() > 0) {
                for (int i = 0; i < tempTaggedUser.size(); i++) {
                    if (tempTaggedUser.get(i).getServer_id() != null && tempTaggedUser.get(i).getServer_id().length() > 0) {
                        if (tagedFriendList.size() > 0) {
                            for (int j = 0; j < tagedFriendList.size(); j++) {
                                if (!tempTaggedUser.get(i).getId().equalsIgnoreCase(tagedFriendList.get(j).getId())) {
                                    tempTaggedUser.get(i).setIs_destory("1");
                                }
                            }
                        } else {
                            tempTaggedUser.get(i).setIs_destory("1");
                        }
                    }
                }
            }

            for (int i = 0; i < tempTaggedUser.size(); i++) {
                if (tempTaggedUser.get(i).getServer_id() != null && tempTaggedUser.get(i).getServer_id().length() > 0) {
                    RequestBody userId = ConverterUtils.parseString(tempTaggedUser.get(i).getId());
                    RequestBody isDestrory = ConverterUtils.parseString(tempTaggedUser.get(i).getIs_destory());
                    RequestBody id = ConverterUtils.parseString(tempTaggedUser.get(i).getServer_id());
                    tagFriendsAttributes.put("tag_friends_attributes[" + i + "][user_id]", userId);
                    tagFriendsAttributes.put("tag_friends_attributes[" + i + "][id]", id);
                    tagFriendsAttributes.put("tag_friends_attributes[" + i + "][_destroy]", isDestrory);
                } else {
                    RequestBody userId = ConverterUtils.parseString(tempTaggedUser.get(i).getId());
                    tagFriendsAttributes.put("tag_friends_attributes[" + i + "][user_id]", userId);
                }
            }

            /*images_attributes*/
            Map<String, RequestBody> imagesAttributes = new HashMap<>();
            List<MultipartBody.Part> parts = new ArrayList<>();

            List<SocialPostPhotoModel> tempPhoto = new ArrayList<>();
            tempPhoto.addAll(imageList);

            if (postList.getImages().size() > 0) {
                for (int i = 0; i < postList.getImages().size(); i++) {
                    SocialPostPhotoModel socialPostPhotoModel = new SocialPostPhotoModel();
                    socialPostPhotoModel.setUrl(postList.getImages().get(i).getFile().getUrl());
                    socialPostPhotoModel.setId(postList.getImages().get(i).getId());
                    socialPostPhotoModel.setType("server");
                    tempPhoto.add(socialPostPhotoModel);
                }
            }
            for (int i = 0; i < tempPhoto.size(); i++) {
                if (tempPhoto.get(i).getId() != null && tempPhoto.get(i).getId().length() > 0) {
                    if (imageList.size() > 0) {
                        for (int j = 0; j < imageList.size(); j++) {
                            if (imageList.get(j).getId() != null && imageList.get(j).getId().length() > 0) {
                                if (!imageList.get(j).getId().equalsIgnoreCase(tempPhoto.get(i).getId())) {
                                    tempPhoto.get(i).set_destroy("1");
                                } else {
                                    tempPhoto.get(i).set_destroy("0");
                                }
                            }
                        }
                    } else {
                        tempPhoto.get(i).set_destroy("1");
                    }
                }
            }
            for (int i = 0; i < tempPhoto.size(); i++) {
                if (tempPhoto.get(i).getType() != null && tempPhoto.get(i).getType().equalsIgnoreCase("server")) {
                    RequestBody id = ConverterUtils.parseString(tempPhoto.get(i).getId());
                    RequestBody isDestrory = ConverterUtils.parseString(tempPhoto.get(i).get_destroy());
                    imagesAttributes.put("post_images_attributes[" + i + "][id]", id);
                    imagesAttributes.put("post_images_attributes[" + i + "][_destroy]", isDestrory);
                } else {
                    parts.add(ConverterUtils.getMultipartFromFile("post_images_attributes[" + i + "][file]", tempPhoto.get(i).getFile()));
                    imagesAttributes.put("post_images_attributes[" + i + "][width]", ConverterUtils.parseString("555"));
                    imagesAttributes.put("post_images_attributes[" + i + "][height]", ConverterUtils.parseString("558"));
                    if (imageList.get(i).getFile_type() != null && imageList.get(i).getFile_type().length() > 0 && imageList.get(i).getFile_type().equalsIgnoreCase("video")) {
                        imagesAttributes.put("post_images_attributes[" + i + "][file_type]", ConverterUtils.parseString("video"));
                    } else {
                        imagesAttributes.put("post_images_attributes[" + i + "][file_type]", ConverterUtils.parseString("image"));

                    }
                }
            }

            Map<String, RequestBody> tagHashAttributes = new HashMap<>();
            if (tagsNames != null && tagsNames.size() > 0) {
                for (int i = 0; i < tagsNames.size(); i++) {
                    RequestBody requestBody = ConverterUtils.parseString(tagsNames.get(i));
                    tagHashAttributes.put("post_tags_attributes[" + i + "][tag_attributes][name]", requestBody);
                }
            }

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiUpdatePost(postId, title, discriptionRequest, placeAddressRequest,
                    placeLatitudeRequest, placeLongitudeRequest, shareTypeRequest, statusRequest, imagesAttributes, tagFriendsAttributes, tagHashAttributes, parts);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            tagedFriendList.clear();
                            startActivity(new Intent(SocialPostActivity.this, HomeActivity.class));
                        } else {
                            ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog("Social Post Failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

}
