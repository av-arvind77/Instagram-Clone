package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.databinding.ActivityReportProblemBinding;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;

import retrofit2.Call;
import retrofit2.Response;

public class ReportProblemActivity extends BaseActivity {
    private ActivityReportProblemBinding binding;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_problem);
        context = ReportProblemActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();
        setCurrentTheme();

    }

    private void setCurrentTheme() {
        binding.llReport.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.etReportProblem.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setEditTextBackground()));
        binding.toolbarReportProblem.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.toolbarReportProblem.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.etReportProblem.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkGreyTextColor()));
        binding.etReportProblem.setHintTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkHintColor()));
        Themes.getInstance(context).changeIconColor(context,binding.toolbarReportProblem.ivLeft);
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));

    }

    @Override
    public void initializedControl() {

    }

    @Override
    public void setToolBar() {
        binding.toolbarReportProblem.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarReportProblem.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarReportProblem.tvHeader.setText(R.string.report_problem);
    }

    @Override
    public void setOnClickListener() {
        binding.submitProblem.setOnClickListener(this);
        binding.toolbarReportProblem.ivLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_problem:
                CommonUtils.hideSoftKeyboard(this);
                if (!binding.etReportProblem.getText().toString().isEmpty()) {

                    finish();



                    // reportAProblemApi(binding.etReportProblem.getText().toString().trim());
                } else
                    ToastUtils.showToastShort(context, "Field can't be left blank!");
                break;
            case R.id.ivLeft:
                finish();
        }
    }


  /*  private void reportAProblemApi(String description){
        final Dialog progressDialog = DialogUtils.customProgressDialog(context);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("description",description);

        retrofit2.Call<ApiResponse> call= ApiExecutor.getClient(context).feedbackApi(jsonObject);
        call.enqueue(new retrofit2.Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if( progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if(response.body()!=null && response.body().getResponseCode()==200) {
                    if (response.body().getResponseMessage() != null){
                        CommonUtils.showToast(context, response.body().getResponseMessage());
                    }
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if( progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                t.printStackTrace();
            }
        });
    }*/

}
