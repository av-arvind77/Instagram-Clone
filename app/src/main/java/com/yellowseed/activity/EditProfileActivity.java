package com.yellowseed.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.adapter.EditCollegeItemAdapter;
import com.yellowseed.adapter.EditSchoolItemAdapter;
import com.yellowseed.adapter.EditWorkedAtAdapter;
import com.yellowseed.countrypicker.CountryPicker;
import com.yellowseed.countrypicker.CountryPickerListener;
import com.yellowseed.databinding.ActivityEditProfileBinding;
import com.yellowseed.databinding.LayoutavatarimageclickableBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.imageUtils.CropImage;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.reqModel.SchoolModel;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DateUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Iso2Phone;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.CommonApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

import static com.yellowseed.imageUtils.TakePictureUtils.CROP_FROM_CAMERA;

public class EditProfileActivity extends BaseActivity implements TextWatcher {

    private static final int TAKE_PICTURE = 1;
    final Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    private String image = "", base64_value = "", path = "", lookingFor = "",countryCodeStr = "+91";
    private ActivityEditProfileBinding binding;
    private Context mContext;
    private Dialog avatarDialog;
    private EditCollegeItemAdapter adapterCollege;
    private EditSchoolItemAdapter adapterSchool;
    private EditWorkedAtAdapter adapterWorkedAt;
    private List<SchoolModel> arlSchoolModel;
    private List<SchoolModel> arlApiSchoolModel = new ArrayList<>();
    private List<SchoolModel> arlCollegeModels;
    private List<SchoolModel> arlApiCollegeModels = new ArrayList<>();
    private List<SchoolModel> arlWorkAtModels;
    private List<SchoolModel> arlApiWorkAtModels = new ArrayList<>();
    private long dobTimeStamp = 0;
    private UserModel userData=new UserModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        mContext = EditProfileActivity.this;

        arlSchoolModel = new ArrayList<>();
        arlCollegeModels = new ArrayList<>();
        arlWorkAtModels = new ArrayList<>();
        initializedControl();
        setToolBar();
        setOnClickListener();
        setTextChangedListeners();
       // userData = PrefManager.getInstance(this).getCurrentUser();
        userData.setName("John Thomas");
        userData.setUser_name("john");
        userData.setBio("john");
        userData.setEmail("john@123");
        userData.setAddress("Mobiloitte");
        userData.setGender("male");

        setUserData(userData);
    }


    private void setUserData(UserModel user) {
        CommonUtils.userProfile(mContext, binding.ivUserImageEditProfile, binding.tvUserImage);

        binding.etName.setText(TextUtils.isEmpty(user.getName()) ? "" : user.getName());
        binding.etUserName.setText(TextUtils.isEmpty(user.getUser_name()) ? "" : user.getUser_name());
        binding.etBio.setText(TextUtils.isEmpty(user.getBio()) ? "" : user.getBio());
       /* if (!TextUtils.isEmpty(user.getImage())) {
            Picasso.with(mContext).load(user.getImage()).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivUserImageEditProfile);
        }*/

        if (userData.getAvatar_img() != null && !TextUtils.isEmpty(user.getAvatar_img().getAvatar_image())) {
            Picasso.with(mContext).load(user.getAvatar_img().getAvatar_image()).placeholder(R.mipmap.avaterimg).
                    transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivAvatarEditProfile);
        }
        binding.etHomeTown.setText(TextUtils.isEmpty(user.getHometown()) ? "" : user.getHometown());
        binding.etEmail.setText(TextUtils.isEmpty(user.getEmail()) ? "" : user.getEmail());
        binding.etWebsite.setText(TextUtils.isEmpty(user.getWebsite()) ? "" : user.getWebsite());
        binding.etPhoneNumber.setText(TextUtils.isEmpty(user.getMobile()) ? "" : user.getMobile());
    //    binding.etPhoneNumber.setEnabled(false);
        String homeTown = "";
        if (!TextUtils.isEmpty(user.getCity()) && !TextUtils.isEmpty(user.getCountry())) {
            homeTown = user.getCity() + ", " + user.getCountry();
        } else if (!TextUtils.isEmpty(user.getCity())) {
            homeTown = user.getCity();
        }
        binding.etLivesIn.setText(homeTown);

        if (user.getSchool() != null && user.getSchool().size() > 0) {
            arlSchoolModel.clear();
            arlSchoolModel.addAll(user.getSchool());
            arlApiSchoolModel.addAll(user.getSchool());
            adapterSchool.notifyDataSetChanged();
        } else {
            arlSchoolModel.clear();
            arlSchoolModel.add(new SchoolModel());
            adapterSchool.notifyDataSetChanged();
        }


        if (user.getCollege() != null && user.getCollege().size() > 0) {
            arlCollegeModels.clear();
            arlCollegeModels.addAll(user.getCollege());
            arlApiCollegeModels.addAll(user.getCollege());
            adapterCollege.notifyDataSetChanged();
        } else {
            arlCollegeModels.clear();
            arlCollegeModels.add(new SchoolModel());
            adapterCollege.notifyDataSetChanged();
        }


        if (user.getWork() != null && user.getWork().size() > 0) {
            arlWorkAtModels.clear();
            arlWorkAtModels.addAll(user.getWork());
            arlApiWorkAtModels.addAll(user.getWork());
            adapterWorkedAt.notifyDataSetChanged();
        } else {
            arlWorkAtModels.clear();
            arlWorkAtModels.add(new SchoolModel());
            adapterWorkedAt.notifyDataSetChanged();
        }


        if (!TextUtils.isEmpty(user.getGender())) {
            if (user.getGender().equalsIgnoreCase("male")) {
                binding.spGender.setSelection(1);
            } else if (user.getGender().equalsIgnoreCase("female")) {
                binding.spGender.setSelection(2);
            }
        }

        if (!TextUtils.isEmpty(user.getDob()) && !(user.getDob().equalsIgnoreCase("0"))) {
            binding.tvDatePicker.setText(DateUtils.getDateOfTimestamp(Long.parseLong(user.getDob())));
        }


    }

    public void initializedControl() {
        setCurrentTheme();

        datePicker();
        setGender();
        setImagePost();
        setSchool();
        setCollege();
        setWorked();
        binding.etName.requestFocus();


        ArrayAdapter<String> arrayAdapterContact = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, Iso2Phone.getaCountryCodeNew());
//        ArrayAdapter<String> arrayAdapterContact = ArrayAdapter.createFromResource(mContext,/* R.array.country_code*/Iso2Phone.getCountryCode(), android.R.layout.simple_spinner_item);
        binding.spCountryCode.setAdapter(arrayAdapterContact);


        binding.spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {

                    binding.tvGenderError.setVisibility(View.GONE);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setCurrentTheme() {
        binding.llEditProfile.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarEditProfile.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarEditProfile.tvRighttext.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setTolbarText()));
        binding.toolbarEditProfile.tvHeader.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etName.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.viewEdit.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLine()));
        binding.etUserName.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.spGender.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.spGender.setPopupBackgroundDrawable(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkThemeDialog()));
        binding.etBio.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.tvDatePicker.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkSearchDrawable()));
        binding.etEmail.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.etLivesIn.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.etHomeTown.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.etWebsite.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.etPhoneNumber.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.tvCountryCode.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.etName.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etPhoneNumber.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvCountryCode.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etUserName.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etBio.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setDarkThemeText()));
        binding.etHomeTown.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etLivesIn.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etEmail.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etWebsite.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etEmail.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvName.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etName.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etPhoneNumber.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.tvCountryCode.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etUserName.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etBio.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etHomeTown.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etLivesIn.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etEmail.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etWebsite.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etEmail.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etLivesIn.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.tvName.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvUserName.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvGender.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvBio.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvDOB.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvSchool.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvCollege.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvHomeTown.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvLives.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvEmail.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvCollege.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvSchool.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvWebsite.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setDarkThemeText()));
        binding.tvPhoneNumber.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvUserImage.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));

        Themes.getInstance(mContext).changeIconColor(mContext, binding.toolbarEditProfile.ivLeft);


    }


    @Override
    protected void onResume() {
        super.onResume();
        binding.etName.requestFocus();
    }

    private void setWorked() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvWorkedat.setLayoutManager(linearLayoutManager);
        binding.rvWorkedat.setNestedScrollingEnabled(false);
        adapterWorkedAt = new EditWorkedAtAdapter(mContext, arlWorkAtModels, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                TextView tvWorkedAt = (TextView) view.findViewById(R.id.tv_add_school);

                switch (view.getId()) {
                    case R.id.tv_add_school:
                        if (tvWorkedAt.getText().toString().equalsIgnoreCase(AppConstants.PLUS)) {
                            SchoolModel model = new SchoolModel();
                            arlWorkAtModels.add(model);
                            adapterWorkedAt.notifyDataSetChanged();
                        } else {
                            arlWorkAtModels.remove(position);
                            adapterWorkedAt.notifyItemRemoved(position);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        binding.rvWorkedat.setAdapter(adapterWorkedAt);
    }

    //Setting the college item adapter

    private void setCollege() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvCollege.setLayoutManager(linearLayoutManager);
        binding.rvCollege.setNestedScrollingEnabled(false);
        adapterCollege = new EditCollegeItemAdapter(mContext, arlCollegeModels, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tvAddSchool = (TextView) view.findViewById(R.id.tv_add_school);
                switch (view.getId()) {
                    case R.id.tv_add_school:
                        if (tvAddSchool.getText().toString().equalsIgnoreCase(AppConstants.PLUS)) {
                            SchoolModel model = new SchoolModel();
                            arlCollegeModels.add(model);
                            adapterCollege.notifyDataSetChanged();
                        } else {
                            arlCollegeModels.remove(position);
                            adapterCollege.notifyItemRemoved(position);
                        }

                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvCollege.setAdapter(adapterCollege);
    }

    //Setting the school item adapter

    private void setSchool() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvSchool.setLayoutManager(linearLayoutManager);
        binding.rvSchool.setNestedScrollingEnabled(false);
        adapterSchool = new EditSchoolItemAdapter(mContext, arlSchoolModel, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tvAddSchool = (TextView) view.findViewById(R.id.tv_add_school);
                switch (view.getId()) {
                    case R.id.tv_add_school:
                        if (tvAddSchool.getText().toString().equalsIgnoreCase(AppConstants.PLUS)) {
                            SchoolModel model = new SchoolModel();
                            arlSchoolModel.add(model);
                            adapterSchool.notifyDataSetChanged();
                        } else {
                            arlSchoolModel.remove(position);
                            adapterSchool.notifyItemRemoved(position);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvSchool.setAdapter(adapterSchool);

    }


    private void setGender() {

        ArrayAdapter<CharSequence> arrayAdapterContact = ArrayAdapter.createFromResource(mContext, R.array.gender_option, android.R.layout.simple_spinner_item);
        binding.spGender.setAdapter(arrayAdapterContact);
        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {


            binding.spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(position)).setTextColor(Color.parseColor("#646b99"));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }

    private void datePicker() {
        binding.tvDatePicker.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvDatePicker.setInputType(InputType.TYPE_NULL);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateCalender();
            }
        };
    }

    private void updateCalender() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
        binding.tvDatePicker.setText(simpleDateFormat.format(calendar.getTime()));
        dobTimeStamp = calendar.getTimeInMillis() / 1000;

    }

    @Override
    public void setToolBar() {
        binding.toolbarEditProfile.tvHeader.setText(R.string.edit_profile);
        binding.toolbarEditProfile.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarEditProfile.tvRighttext.setText(R.string.save);
        binding.toolbarEditProfile.tvRighttext.setVisibility(View.VISIBLE);
        binding.toolbarEditProfile.ivLeft.setVisibility(View.VISIBLE);

    }

    @Override
    public void setOnClickListener() {
        binding.tvDatePicker.setOnClickListener(this);
        binding.toolbarEditProfile.ivLeft.setOnClickListener(this);
        binding.toolbarEditProfile.tvRighttext.setOnClickListener(this);
        binding.tvCountryCode.setOnClickListener(this);
        // binding.addSchool.setOnClickListener(this);
        // binding.addCollege.setOnClickListener(this);
        //   binding.addWorkedAt.setOnClickListener(this);
        binding.ivAvatarEditProfile.setOnClickListener(this);
        binding.ivUserImageEditProfile.setOnClickListener(this);
        binding.tvUserImage.setOnClickListener(this);
        binding.lllayout.setOnClickListener(this);
        binding.toolbarEditProfile.tvRighttext.setOnClickListener(this);
    }

    private void setTextChangedListeners() {
        binding.etName.addTextChangedListener(this);
        binding.etUserName.addTextChangedListener(this);
        binding.etBio.addTextChangedListener(this);
        binding.tvDatePicker.addTextChangedListener(this);
        // binding.etSchool.addTextChangedListener(this);
//        binding.etCollege.addTextChangedListener(this);

        binding.etHomeTown.addTextChangedListener(this);
        binding.etLivesIn.addTextChangedListener(this);
        binding.etEmail.addTextChangedListener(this);
        binding.etWebsite.addTextChangedListener(this);
        binding.etPhoneNumber.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvDatePicker:
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(mContext, date, calendar.get(Calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
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


            case R.id.tvRighttext:
                if (validate()) {

                    if (isFilledAllField()) {

                        ToastUtils.showToastShort(mContext, "Profile Upadted successfully");
                        finish();
                    } else {
                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.enter_designation));
                    }

                }
                break;
            case R.id.lllayout:
                CommonUtils.hideSoftKeyboard(this);
                break;

            case R.id.ivAvatarEditProfile:
                if (userData.getAvatar_img() != null && userData.getAvatar_img().getAvatar_image() != null && userData.getAvatar_img().getAvatar_image().length() > 0) {
                    avatarPopUp();
                } else {
                    Intent intent = new Intent(mContext, EditAvtarActivity.class);
                    intent.putExtra("from", "update");
                    intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    if (userData.getGender().equalsIgnoreCase("male")) {
                        intent.putExtra("isMale", true);

                    } else {
                        intent.putExtra("isMale", false);

                    }
                    intent.putExtra(AppConstants.IS_FROM, EditProfileActivity.class.getSimpleName());
                    mContext.startActivity(intent);
                    EditProfileActivity.this.finish();
                }
                break;
            case R.id.ivAvatarEditReadyImage:
                break;
            case R.id.btnavatarContinue:
                AppConstants.clickAvatarSkip = true;
                Intent intent = new Intent(mContext, EditAvtarActivity.class);
                intent.putExtra("from", "update");
                intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                if (userData.getGender().equalsIgnoreCase("male")) {
                    intent.putExtra("isMale", true);
                } else {
                    intent.putExtra("isMale", false);
                }
                intent.putExtra(AppConstants.IS_FROM, EditProfileActivity.class.getSimpleName());
                mContext.startActivity(intent);
                EditProfileActivity.this.finish();
                avatarDialog.dismiss();
                break;
            case R.id.btnavatarCancel:
                avatarDialog.dismiss();
                break;
            case R.id.ivUserImageEditProfile:
            case R.id.tvUserImage:
                if (!CheckPermission.checkCameraPermission(mContext)) {
                    CheckPermission.requestCameraPermission((Activity) mContext, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    addPhotoDialog();
                }
                break;
            default:
                break;
        }

    }

    private boolean isFilledAllField() {
        boolean isFilled = true;
        for (int i = 0; i < arlWorkAtModels.size(); i++) {
            if (!TextUtils.isEmpty(arlWorkAtModels.get(i).getWork_at()) &&
                    TextUtils.isEmpty(arlWorkAtModels.get(i).getDesignation())) {
                isFilled = false;
                break;
            }

        }
        return isFilled;
    }

    private void avatarPopUp() {


        LayoutavatarimageclickableBinding layoutavatarimageclickableBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layoutavatarimageclickable, null, false);
        avatarDialog = DialogUtils.createDialog(mContext, layoutavatarimageclickableBinding.getRoot());
        layoutavatarimageclickableBinding.ivAvatarEditReadyImage.setOnClickListener(this);
        layoutavatarimageclickableBinding.btnavatarContinue.setOnClickListener(this);
        layoutavatarimageclickableBinding.btnavatarCancel.setOnClickListener(this);
        layoutavatarimageclickableBinding.llAvatar.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setRoundedBlackBackground()));
        layoutavatarimageclickableBinding.btnavatarContinue.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        layoutavatarimageclickableBinding.btnavatarCancel.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setRoundedBlackBackground()));
        layoutavatarimageclickableBinding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        // layoutavatarimageclickableBinding.llAvatar.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkAvtar( )));
        layoutavatarimageclickableBinding.btnavatarContinue.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        // layoutavatarimageclickableBinding.btnavatarCancel.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkAvtar( )));
        // layoutavatarimageclickableBinding.viewLine.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkThemeViewColor( )));

        Glide.with(mContext) // Bind it with the context of the actual view used
                .load(userData.getAvatar_img().getAvatar_image()) // Load the image
                .asBitmap() // All our images are static, we want to display them as bitmaps
                .format(DecodeFormat.PREFER_RGB_565) // the decode format - this will not use alpha at all
                .thumbnail(0.2f)// make use of the thumbnail which can display a down-sized version of the image
                .into(layoutavatarimageclickableBinding.ivAvatarEditReadyImage); // Voilla - the target view
    }

    private boolean validate() {
        clearError();

        if (TextUtils.isEmpty(binding.etName.getText().toString().trim())) {
            binding.etName.requestFocus();
            binding.tvNameError.setVisibility(View.VISIBLE);
            binding.tvNameError.setText(R.string.enternameerror);

            return false;
        } else if (CommonUtils.lengthFieldValidation(binding.etName.getText().toString(), 2)) {
            binding.etName.requestFocus();
            binding.tvNameError.setVisibility(View.VISIBLE);
            binding.tvNameError.setText(R.string.validnameerror);
            return false;

        } else if (TextUtils.isEmpty(binding.etUserName.getText().toString().trim())) {
            binding.etUserName.requestFocus();
            binding.tvUserNameError.setVisibility(View.VISIBLE);
            binding.tvUserNameError.setText(R.string.usernameerror);
            return false;
        } /*else if (!isValidUserName(binding.etUserName.getText().toString())) {
            binding.tvUserNameError.setVisibility(View.VISIBLE);
            binding.tvUserNameError.setText(R.string.validusernameerror);
            return false;

        } */ else if (binding.etUserName.getText().toString().length() < 4) {
            binding.etUserName.requestFocus();

            binding.tvUserNameError.setVisibility(View.VISIBLE);
            binding.tvUserNameError.setText(R.string.validusernamelangth);
            return false;

        } else if (binding.spGender.getSelectedItem().toString().equalsIgnoreCase("Select Gender")) {
            binding.tvGenderError.setVisibility(View.VISIBLE);
            binding.tvGenderError.setText(R.string.selectgendererror);
            return false;
        } else {
            return true;
        }


    }

    private void clearError() {
        binding.tvNameError.setVisibility(View.GONE);
        binding.tvGenderError.setVisibility(View.GONE);
        binding.tvBioError.setVisibility(View.GONE);
        binding.tvBioError.setVisibility(View.GONE);
        binding.tvDobError.setVisibility(View.GONE);

        binding.tvSchoolError.setVisibility(View.GONE);
        binding.tvCollegeError.setVisibility(View.GONE);
        binding.tvWorkedAtError.setVisibility(View.GONE);
        binding.tvHomeError.setVisibility(View.GONE);
        binding.tvLivesInError.setVisibility(View.GONE);
        binding.tvEmailError.setVisibility(View.GONE);
        binding.tvWebsiteError.setVisibility(View.GONE);
        binding.tvPhoneNumberError.setVisibility(View.GONE);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (!TextUtils.isEmpty(binding.etName.getText().toString())) {
            binding.tvNameError.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(binding.etUserName.getText().toString())) {
            binding.tvUserNameError.setVisibility(View.GONE);
        }
        /*if (!TextUtils.isEmpty(binding.etEmail.getText().toString())) {
            binding.tvEmailError.setVisibility(View.GONE);
        } if (!TextUtils.isEmpty(binding.etWebsite.getText().toString())) {
            binding.tvWebsiteError.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(binding.etPhoneNumber.getText().toString())) {
            binding.tvPhoneNumberError.setVisibility(View.GONE);
        }*/


    }


    //Image Set in Profile
    protected Boolean isValidUserName(String userName) {
        return (Pattern.matches("[a-zA-Z-]{2,15}", userName));
    }

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

        if (getIntent() != null && getIntent().getStringExtra(AppConstants.IMAGE_PATH) != null) {
            path = getIntent().getStringExtra(AppConstants.IMAGE_PATH);
            Picasso.with(mContext).load(new File(path)).into(binding.ivUserImageEditProfile);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == com.yellowseed.imageUtils.TakePictureUtils.PICK_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream inputStream = mContext.getContentResolver().openInputStream(intent.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(this.mContext.getExternalFilesDir("temp"), image + ".jpg"));
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
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    path = intent.getStringExtra(CropImage.IMAGE_PATH);

                    CommonApi.callUploadImageApi(mContext, new File(path));

                }
                if (path == null) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                base64_value = "data:image/jpeg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT).trim();
                base64_value = base64_value.replace("\n", "");
                binding.ivUserImageEditProfile.setVisibility(View.VISIBLE);
                binding.tvUserImage.setVisibility(View.GONE);
                Picasso.with(mContext).load(new File(path)).placeholder(R.drawable.user_placeholder).
                        transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivUserImageEditProfile);
                // CommonUtils.userProfile(mContext, binding.ivUserImageEditProfile, binding.tvUserImage);


               /* if (path != null && !path.isEmpty())
                    startActivity(new Intent(mContext, NewGroupDoneActivity.class)
                            .putExtra(AppConstants.IMAGE_PATH, path));*/

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


    public void callUpdateProfileAPI() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();


            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiUpdateProfile(reqUserUpdatedData());
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
                            CommonUtils.savePreferencesString(mContext, AppConstants.USER_NAME, response.body().getUser().getName());
                            CommonUtils.savePreferencesString(mContext, AppConstants.USER_PROFILE_IMAGE, response.body().getUser().getImage());
                            PrefManager.getInstance(EditProfileActivity.this).saveUser(userData);
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                            onBackPressed();
                        } else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED) {
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            CommonUtils.clearPrefData(mContext);
                            ((Activity) mContext).finishAffinity();

                        } else {
                            ToastUtils.showToastShort(mContext, "" + response.body().getResponseMessage());
                        }
                    } else {
                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    private ApiRequest reqUserUpdatedData() {

        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setUser(new UserModel());

        apiRequest.getUser().setName(binding.etName.getText().toString().trim());
        apiRequest.getUser().setUser_name(binding.etUserName.getText().toString().trim());
        if (!(binding.spGender.getSelectedItem().toString().trim().equalsIgnoreCase("Select Gender"))) {
            apiRequest.getUser().setGender(binding.spGender.getSelectedItem().toString().trim());
        } else {
            apiRequest.getUser().setGender("");
        }
        apiRequest.getUser().setBio(binding.etBio.getText().toString().trim());
        apiRequest.getUser().setBio(binding.etBio.getText().toString().trim());
        List<SchoolModel> mySchoolModels = new ArrayList<>();
        List<SchoolModel> myCollegeModels = new ArrayList<>();
        List<SchoolModel> myWorkAtModels = new ArrayList<>();

//=============================== remove school blank data =====================
        for (int i = 0; i < arlSchoolModel.size(); i++) {
            if (!TextUtils.isEmpty(arlSchoolModel.get(i).getSchool_name())) {
                mySchoolModels.add(arlSchoolModel.get(i));
            } else {
                arlSchoolModel.get(i).set_destroy("1");
                mySchoolModels.add(arlSchoolModel.get(i));
            }

        }
//======================== filter school deleted item ===================================
        for (int i = 0; i < arlApiSchoolModel.size(); i++) {
            boolean isMatched = false;
            for (int j = 0; j < arlSchoolModel.size(); j++) {
                if (arlApiSchoolModel.get(i).getId().equalsIgnoreCase(arlSchoolModel.get(j).getId())) {
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) {
                arlApiSchoolModel.get(i).set_destroy("1");
                mySchoolModels.add(arlApiSchoolModel.get(i));
            }
        }
//======================== filter college deleted item ===================================


        //=============================== remove school blank data =====================
        for (int i = 0; i < arlCollegeModels.size(); i++) {

            if (!TextUtils.isEmpty(arlCollegeModels.get(i).getCollege_name())) {
                myCollegeModels.add(arlCollegeModels.get(i));
            } else {
                arlCollegeModels.get(i).set_destroy("1");
                myCollegeModels.add(arlCollegeModels.get(i));
            }
        }
//======================== filter college deleted item ===================================
        for (int i = 0; i < arlApiCollegeModels.size(); i++) {
            boolean isMatched = false;
            for (int j = 0; j < arlCollegeModels.size(); j++) {
                if (arlApiCollegeModels.get(i).getId().equalsIgnoreCase(arlCollegeModels.get(j).getId())) {
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) {
                arlApiCollegeModels.get(i).set_destroy("1");
                myCollegeModels.add(arlApiCollegeModels.get(i));
            }
        }
//======================== filter work deleted item ===================================


        //======================== filter work deleted item ===================================


        //========================= ====== remove school blank data =====================
        for (int i = 0; i < arlWorkAtModels.size(); i++) {
            if (!TextUtils.isEmpty(arlWorkAtModels.get(i).getWork_at())) {
                myWorkAtModels.add(arlWorkAtModels.get(i));
            } else {
                arlWorkAtModels.get(i).set_destroy("1");
                myWorkAtModels.add(arlWorkAtModels.get(i));
            }
        }
//======================== filter work deleted item ===================================
        for (int i = 0; i < arlApiWorkAtModels.size(); i++) {
            boolean isMatched = false;
            for (int j = 0; j < arlWorkAtModels.size(); j++) {
                if (arlApiWorkAtModels.get(i).getId().equalsIgnoreCase(arlWorkAtModels.get(j).getId())) {
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) {
                arlApiWorkAtModels.get(i).set_destroy("1");
                myWorkAtModels.add(arlApiWorkAtModels.get(i));
            }
        }
//======================== filter work deleted item ===================================

        apiRequest.getUser().setSchool(mySchoolModels);
        apiRequest.getUser().setCollege(myCollegeModels);
        apiRequest.getUser().setWork(myWorkAtModels);
        apiRequest.getUser().setHometown(binding.etHomeTown.getText().toString().trim());
//        apiRequest.getUser().setLives_in(binding.etLivesIn.getText().toString().trim());
        apiRequest.getUser().setCity(binding.etLivesIn.getText().toString().trim());
        apiRequest.getUser().setEmail(binding.etEmail.getText().toString().trim());
        apiRequest.getUser().setWebsite(binding.etWebsite.getText().toString().trim());
        apiRequest.getUser().setPhone_no(binding.etPhoneNumber.getText().toString().trim());
        if (dobTimeStamp != 0) {
            apiRequest.getUser().setDob("" + dobTimeStamp);
        }

        userData = apiRequest.getUser();

        return apiRequest;
    }


}
