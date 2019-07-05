package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.SuggestionsAdapter;
import com.yellowseed.databinding.ActivitySuggestionsBinding;
import com.yellowseed.fragments.HomeBottomFragment;
import com.yellowseed.model.MediaClickModel;
import com.yellowseed.model.SuggestionModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.model.resModel.SuggestionListModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SuggestionsActivity extends BaseActivity {

    private List<String> arlNumber = new ArrayList<>();
    private ActivitySuggestionsBinding binding;
    private Context mContext;
    private ArrayList<SuggestionListModel> arlModel;

    private ApiResponse userData;
    private SuggestionsAdapter adapter;
    private String phoneNo;
    private String id;
    private String name;
    private String email;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Dialog progressContactsDialog;
    private String isFrom;
    private String[] username = {"John Thomas", "Jenny Koul", "Mile Keel", "Julie Kite", "Tiny Tim", "Mike Keel", "Kellly Kim"};
    private int img[] = {R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon2, R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_suggestions);
        mContext = SuggestionsActivity.this;
        getIntentData();
        initializedControl();
        setToolBar();
        setOnClickListener();
      /*  if (checkPermission()) {
            new GetContactAsync().execute();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
*/

    }

    private void getIntentData() {
        if (getIntent() != null) {
          /*  if (getIntent().hasExtra(AppConstants.USER_DATA)) {

                userData = (ApiResponse) getIntent().getSerializableExtra(AppConstants.USER_DATA);
            }*/
            isFrom = getIntent().getStringExtra(AppConstants.IS_FROM);
            if (isFrom != null) {
                if (isFrom.equalsIgnoreCase(HomeBottomFragment.class.getSimpleName())) {
                    setCurrentTheme();
                } else {

                }

            }

        }
    }


    public void setCurrentTheme() {


        binding.llMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarSuggestions.tvHeader.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.toolbarSuggestions.tvRighttext.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setTolbarText()));
        binding.toolbarSuggestions.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        Themes.getInstance(mContext).changeIconColor(mContext, binding.toolbarSuggestions.ivLeft);

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;

        }
    }

    @Override
    public void initializedControl() {
        suggestionRecyclerView();
    }

    @Override
    public void setToolBar() {

        binding.toolbarSuggestions.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarSuggestions.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarSuggestions.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarSuggestions.tvHeader.setText(R.string.suggestions);
        binding.toolbarSuggestions.tvRighttext.setVisibility(View.VISIBLE);
        binding.toolbarSuggestions.tvRighttext.setText(R.string.done);

    }

    private void suggestionRecyclerView() {
        binding.rvSuggestions.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvSuggestions.setLayoutManager(layoutManager);
        arlModel = new ArrayList<>();
        arlModel.addAll(userListModels());

        adapter = new SuggestionsAdapter(arlModel, this, isFrom);
        binding.rvSuggestions.setAdapter(adapter);

    }

    @Override
    public void setOnClickListener() {
        binding.toolbarSuggestions.tvRighttext.setOnClickListener(this);
        binding.toolbarSuggestions.ivLeft.setOnClickListener(this);
        binding.btnGroupInfoAdmin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.tvRighttext:

                if (isFrom != null) {

                    if (isFrom.equalsIgnoreCase(HomeBottomFragment.class.getSimpleName())) {
                        finish();
                    }

                } else {
                    startActivity(new Intent(mContext, WelcomeActivity.class));

                }


                break;
            case R.id.btnGroupInfoAdmin:
                if (arlModel != null) {
                    for (int i = 0; i < arlModel.size(); i++) {
                        arlModel.get(i).setMutual("2");
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }


    private List<SuggestionListModel> userListModels() {
        List<SuggestionListModel> userListModelList = new ArrayList<>();
        for (int i = 0; i < img.length; i++) {
            SuggestionListModel model = new SuggestionListModel();
            model.setImg(img[i]);
            model.setMutual("0");
            model.setName(username[i]);

            userListModelList.add(model);
        }
        return userListModelList;
    }



   /* public void callpostContctApi() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setMobile(arlNumber);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiPostContacts(apiRequest);
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
                            if (response.body().getContacts() != null) {

                                arlModel.clear();
                                arlModel.addAll(response.body().getContacts());
                                adapter.notifyDataSetChanged();
                            }
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
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            try {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new GetContactAsync().execute();

                } else {
                    ToastUtils.showToastShort(mContext, getString(R.string.grant_contact_permission));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class GetContactAsync extends AsyncTask<List<String>, Void, List<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressContactsDialog = DialogUtils.customProgressDialog(mContext);
            progressContactsDialog.show();

        }

        @Override
        protected List<String> doInBackground(List<String>[] lists) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mContext.checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                ContentResolver cr = mContext.getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

                if ((cur != null ? cur.getCount() : 0) > 0) {
                    while (cur != null && cur.moveToNext()) {
                        id = cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts._ID));
                        name = cur.getString(cur.getColumnIndex(

                                ContactsContract.Contacts.DISPLAY_NAME));

                        if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                phoneNo = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));

                                Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                        new String[]{id}, null);
                                email = "abc@gmail.com";
                                if (emailCur != null) {
                                    while (emailCur.moveToNext()) {
                                        email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

                                    }
                                    emailCur.close();
                                }

                                phoneNo = phoneNo.replace(" ", "");
                                phoneNo = phoneNo.replace("(", "");
                                phoneNo = phoneNo.replace(")", "");

                                if (CommonUtils.isValidPhone(phoneNo)) {

                                    arlNumber.add(phoneNo);


                                }

                                Log.i(TAG, "Name: " + name);
                                Log.i(TAG, "Phone Number: " + phoneNo);
                                Log.i(TAG, "email: " + email);
                            }
                            pCur.close();
                        }
                    }
                }
                if (cur != null) {
                    cur.close();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<String> s) {
            super.onPostExecute(s);
          /*  progressContactsDialog.dismiss();
            callpostContctApi();*/
        }
    }
}
