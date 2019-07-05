/*
package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.AvatarImageAdapter;
import com.yellowseed.adapter.AvtarImageTextAdapter;
import com.yellowseed.avatar.AvatarGenerator;
import com.yellowseed.avatar.AvatarPart;
import com.yellowseed.avatar.AvatarPartDb;
import com.yellowseed.avatar.AvatarSelectionListener;
import com.yellowseed.databinding.ActivityEditAvtarBinding;
import com.yellowseed.fragments.HomeBottomFragment;
import com.yellowseed.model.AvatarModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.ConverterUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.LogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.response.avatar.AvatarCategory;
import com.yellowseed.webservices.response.avatar.AvatarList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

public class EditAvtarActivity extends BaseActivity implements AvatarSelectionListener{

    private ActivityEditAvtarBinding binding;
    private Context context;
    private AvatarImageAdapter adapter;
    private AvtarImageTextAdapter avtarImageTextAdapter;
    private List<AvatarPart> avtarImageModelList;
    private int[] image = {R.mipmap.hair_img, R.mipmap.hair_img_two, R.mipmap.hair_img_three, R.mipmap.hair_img_four, R.mipmap.hair_img_five, R.mipmap.hair_img, R.mipmap.hair_img_two, R.mipmap.hair_img_three, R.mipmap.hair_img_four, R.mipmap.hair_img_five, R.mipmap.hair_img, R.mipmap.hair_img_two, R.mipmap.hair_img_three, R.mipmap.hair_img_four, R.mipmap.hair_img_five, R.mipmap.hair_img, R.mipmap.hair_img_two, R.mipmap.hair_img_three, R.mipmap.hair_img_four, R.mipmap.hair_img_five};
    private AvatarGenerator mAvatarGenerator;
    private AvatarPartDb mAvatarPartDb = new AvatarPartDb();
    private long mSeed = 0;
    private final String PARTS_JSON_MALE = "male/male.json";
    private final String PARTS_JSON_FEMALE = "female/female.json";
    private ArrayList<AvatarPart> avatarParts = new ArrayList<>();
    List<AvatarCategory> avatarCategories = new ArrayList<>();
    private String isFrom;

    private MultipartBody.Part groupImage = null;
    boolean isMale;
    private String from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_avtar);
        context = EditAvtarActivity.this;
        getIntentData();
        avtarImageModelList = new ArrayList<>();
     //   isMale = getIntent().getBooleanExtra("isMale",false);
       // isMale = false;
        initializedControl();
        setToolBar();
        setOnClickListener();
       // getAvtarList();
        from=getIntent().getStringExtra("from");
    }

    private void getIntentData()
    {

        isFrom = getIntent().getStringExtra(AppConstants.IS_FROM);
        if (isFrom != null) {
            if (isFrom.equalsIgnoreCase(EditProfileActivity.class.getSimpleName())) {
                setCurrentTheme();
            } else {

            }
        }
    }


    @Override
    public void initializedControl() {

        */
/*try {
            InputStream stream = getAssets().open(isMale ?PARTS_JSON_MALE:PARTS_JSON_FEMALE);
            mAvatarPartDb.init(stream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //Setting avtar images
          setAvtarImage(isMale ?AvatarPartDb.PART_NAMES_MALE[0]:AvatarPartDb.PART_NAMES_FEMALE[0]);
        //Setting avtar images and text
            setAvtarImageText();
            createDefaultAvatar();
            generateAvatar();*//*

    }

    private void setCurrentTheme() {
        binding.toolbarEditAvtar.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.toolbarEditAvtar.tvRighttext.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setTolbarText()));
        binding.toolbarEditAvtar.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setToolbarWhiteText()));
        binding.llAvtarImage.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setAvtarImageTheme()));
        binding.llAvtartext.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.llImageView.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.viewAvtarImage.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setAvtarViewLine()));
        binding.viewEdit.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setViewLine()));
        binding.imageView.setBackgroundColor(0);

        Themes.getInstance(context).changeIconColor(context,binding.toolbarEditAvtar.ivLeft);




    }

        public void createDefaultAvatar(){
        for (int i = 0; i < avatarCategories.size() ; i++) {
            avatarParts.add(mAvatarPartDb.getPart(avatarCategories.get(i).getName(), 0));
        }
    }

    @Override
    public void setToolBar() {
        binding.toolbarEditAvtar.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarEditAvtar.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarEditAvtar.tvHeader.setText(R.string.avtarstyle);
        binding.toolbarEditAvtar.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarEditAvtar.tvRighttext.setVisibility(View.VISIBLE);
        binding.toolbarEditAvtar.tvRighttext.setText(R.string.save);

    }

    @Override
    public void setOnClickListener() {

        binding.toolbarEditAvtar.ivLeft.setOnClickListener(this);
        binding.toolbarEditAvtar.tvRighttext.setOnClickListener(this);


    }

    public void generateAvatar() {
        int size = 1200;
        if (mAvatarGenerator != null) {
            mAvatarGenerator.cancel(true);
        }
        mAvatarGenerator = new AvatarGenerator(this, mAvatarPartDb, binding.imageView, size, avatarParts, avatarCategories);
        mAvatarGenerator.execute(mSeed);
    }


    private void setAvtarImageText() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvAvtarImageText.setLayoutManager(linearLayoutManager);
        avtarImageTextAdapter = new AvtarImageTextAdapter(context, avatarCategories,this,isFrom);
        binding.rvAvtarImageText.setAdapter(avtarImageTextAdapter);
    }

    private void setAvtarImage(String partName) {
        AvatarPart avtarImageModel;
        avtarImageModelList.clear();
        int count = mAvatarPartDb.getPartCount(partName);
        for (int i = 0 ; i < count ; i++){
            avtarImageModel = mAvatarPartDb.getPart(partName,i);
            avtarImageModelList.add(avtarImageModel);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.rvAvtarImage.setLayoutManager(gridLayoutManager);
        adapter = new AvatarImageAdapter(context, avtarImageModelList,this, isMale);
        binding.rvAvtarImage.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.tvRighttext:
                uploadAvtarImage(from);
                //
                break;

            default:
                break;
        }

    }

    @Override
    public void onAccessoriesSelected(int position) {
        setAvtarImage(avatarCategories.get(position).getName());
    }

    @Override
    public void onSubAccessoriesSelected(int position, AvatarPart avatarPart) {
        for (int i = 0; i <avatarParts.size() ; i++) {
            if(avatarParts.get(i)!=null && avatarPart.type == avatarParts.get(i).type){
                avatarParts.remove(i);
                avatarParts.add(i, avatarPart);
                break;
            }
        }
        generateAvatar();
    }


    private void getAvtarList() {
      */
/*  if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            Call<AvatarList> call = ApiExecutor.getClient(context).apiGetAvtarList();
            call.enqueue(new Callback<AvatarList>() {
                @Override
                public void onResponse(@NonNull Call<AvatarList> call, @NonNull final Response<AvatarList> response) {
                    progressDialog.dismiss();
                    try {*//*


                        avatarCategories.clear();
                        if(isMale) {
                            avatarCategories.remove(3);
                            avatarCategories.remove(4);
                        }else {
                  //          avatarCategories.remove(3);
                            avatarCategories.remove(4);
                            avatarCategories.remove(5);
                        }
                        //Setting avtar images
                        setAvtarImage(avatarCategories.get(0).getName());
                        //Setting avtar images and text
                        setAvtarImageText();
                        avtarImageTextAdapter.notifyDataSetChanged();
                        createDefaultAvatar();
                        generateAvatar();


    }


    private void uploadAvtarImage(final String from) {
       */
/* if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            RequestBody action=null;*//*

 */
/* if(from!=null&&from.length()>0&&from.equalsIgnoreCase("update")) {
                 action = ConverterUtils.parseString("update");
            }else {
                 action = ConverterUtils.parseString("create");
            }*//*

            RequestBody skin = ConverterUtils.parseString("");
            RequestBody hair_color = ConverterUtils.parseString("");
            RequestBody hair_style = ConverterUtils.parseString("");
            RequestBody beard = ConverterUtils.parseString("");
            RequestBody beard_color = ConverterUtils.parseString("");
            RequestBody spacts = ConverterUtils.parseString("");
            RequestBody hat = ConverterUtils.parseString("");
            RequestBody cloths = ConverterUtils.parseString("");
            RequestBody status = ConverterUtils.parseString("");
            RequestBody lips_color = ConverterUtils.parseString("");

            for (int i = 0; i < avatarParts.size(); i++) {
                switch (avatarParts.get(i).type) {
                    case "Skin Color":
                        skin = ConverterUtils.parseString(avatarParts.get(i).id);
                        break;
                    case "Cloths":
                        cloths = ConverterUtils.parseString(avatarParts.get(i).id);
                        break;
                    case "Hair Style":
                        hair_style = ConverterUtils.parseString(avatarParts.get(i).id);
                        break;
                    case "Hair Color":
                        hair_style = ConverterUtils.parseString(avatarParts.get(i).id);
                        break;
                    case "Beared Shape":
                        beard_color = ConverterUtils.parseString(avatarParts.get(i).id);
                        break;
                    case "Beared Color":
                        beard_color = ConverterUtils.parseString(avatarParts.get(i).id);
                        break;
                    case "Spects":
                        spacts = ConverterUtils.parseString(avatarParts.get(i).id);
                        break;
                    case "Hat":
                        hat = ConverterUtils.parseString(avatarParts.get(i).id);
                        break;
                    case "Lips color":
                        lips_color = ConverterUtils.parseString(avatarParts.get(i).id);
                        break;
                    case "Lips shape":
                        lips_color = ConverterUtils.parseString(avatarParts.get(i).id);
                        break;
                }
            }
            Map<String, RequestBody> image = new HashMap<>();

            if(CommonUtils.getLocalBitmapUri(binding.imageView,context)!=null){
                    groupImage = ConverterUtils.getMultipartFromFile("image", CommonUtils.getLocalBitmapUri(binding.imageView,context));
            }

          */
/*  Call<ApiResponse> call = ApiExecutor.getClient(context).apiUpdateAvatar(action,skin,hair_color,hair_style,beard,beard_color,spacts,hat,cloths,status,lips_color,groupImage);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(context,response.body().getResponseMessage());
                            if(from!=null&&from.length()>0&&from.equalsIgnoreCase("update")) {
                                finish();*//*

 */
/* }else {*//*

                                Intent intent=new Intent(context,AvtarStyleSocialShareActivity.class);
                                */
/*if(response.body().getUser().getAvatar_image()!=null) {
                                    intent.putExtra("url", response.body().getUser().getAvatar_image());
                                }*//*

                                startActivity(intent);
                            }
                        }
*/
package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.AvatarImageAdapter;
import com.yellowseed.adapter.AvtarImageTextAdapter;
import com.yellowseed.databinding.ActivityEditAvtarBinding;
import com.yellowseed.model.AvtarImageModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

public class EditAvtarActivity extends BaseActivity {

    private ActivityEditAvtarBinding binding;
    private Context context;
    private AvatarImageAdapter adapter;
    private AvtarImageTextAdapter avtarImageTextAdapter;
    private List<AvtarImageModel> avtarImageModelList;
    private int[] image = {R.mipmap.hair_img, R.mipmap.hair_img_two, R.mipmap.hair_img_three, R.mipmap.hair_img_four, R.mipmap.hair_img_five, R.mipmap.hair_img, R.mipmap.hair_img_two, R.mipmap.hair_img_three, R.mipmap.hair_img_four, R.mipmap.hair_img_five, R.mipmap.hair_img, R.mipmap.hair_img_two, R.mipmap.hair_img_three, R.mipmap.hair_img_four, R.mipmap.hair_img_five, R.mipmap.hair_img, R.mipmap.hair_img_two, R.mipmap.hair_img_three, R.mipmap.hair_img_four, R.mipmap.hair_img_five};
    private String isFrom = "";
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_avtar);
        context = EditAvtarActivity.this;
        getIntentData();
        avtarImageModelList = new ArrayList<>();
        initializedControl();
        setToolBar();
        setOnClickListener();
        from=getIntent().getStringExtra("from");


    }


    @Override
    public void initializedControl() {
        //Setting avtar images
        setAvtarImage();
        //Setting avtar images and text
        setAvtarImageText();
    }

    @Override
    public void setToolBar() {
       /* binding.toolbarEditAvtar.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarEditAvtar.ivLeft.setImageResource(R.mipmap.back_icon);*/
        binding.toolbarEditAvtar.tvHeader.setText(R.string.avtarstyle);
        binding.toolbarEditAvtar.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarEditAvtar.tvRighttext.setVisibility(View.VISIBLE);
        binding.toolbarEditAvtar.tvRighttext.setText(R.string.save);
        binding.toolbarEditAvtar.tvRighttext.setTextColor(this.getResources().getColor(R.color.skyblue));

    }

    @Override
    public void setOnClickListener() {

        binding.toolbarEditAvtar.ivLeft.setOnClickListener(this);
        binding.toolbarEditAvtar.tvRighttext.setOnClickListener(this);


    }



    private void getIntentData()
    {
        isFrom = getIntent().getStringExtra(AppConstants.IS_FROM);
        if (isFrom != null) {
            if (isFrom.equalsIgnoreCase(EditProfileActivity.class.getSimpleName())) {
                setCurrentTheme();
            } else {

            }
        }
    }


  /*  private void getIntentData() {
if (getIntent()!=null){

    if (getIntent().getStringExtra(AppConstants.IS_FROM)!=null){

        isFrom = getIntent().getStringExtra(AppConstants.IS_FROM);
    }

}

        if (isFrom != null) {
            if (isFrom.equalsIgnoreCase(EditProfileActivity.class.getSimpleName())) {

                setCurrentTheme();
            } else {

            }
        }
    }*/

    private void setAvtarImageText() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvAvtarImageText.setLayoutManager(linearLayoutManager);
        avtarImageTextAdapter = new AvtarImageTextAdapter(context, isFrom);
        binding.rvAvtarImageText.setAdapter(avtarImageTextAdapter);
    }

    private void setAvtarImage() {
        AvtarImageModel avtarImageModel;
        for (int i = 0; i < image.length; i++) {
            avtarImageModel = new AvtarImageModel();
            avtarImageModel.setImage(image[i]);
            avtarImageModelList.add(avtarImageModel);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.rvAvtarImage.setLayoutManager(gridLayoutManager);
        adapter = new AvatarImageAdapter(context, avtarImageModelList);
        binding.rvAvtarImage.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.tvRighttext:

                    ActivityController.startActivity(this, AvtarStyleSocialShareActivity.class);

                break;

            default:
                break;
        }

    }


    private void setCurrentTheme() {
        binding.toolbarEditAvtar.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.toolbarEditAvtar.tvRighttext.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setTolbarText()));
        binding.toolbarEditAvtar.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setToolbarWhiteText()));
        binding.llAvtarImage.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setAvtarImageTheme()));
        binding.llAvtartext.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.llImageView.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.viewAvtarImage.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setAvtarViewLine()));
        binding.viewEdit.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLine()));
        binding.imageView.setBackgroundColor(0);

        Themes.getInstance(context).changeIconColor(context, binding.toolbarEditAvtar.ivLeft);


    }
}

