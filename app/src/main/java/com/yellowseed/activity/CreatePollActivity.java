package com.yellowseed.activity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.CreatePollAdapter;
import com.yellowseed.databinding.ActivityCreatePollBinding;
import com.yellowseed.model.CreatePollModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.TimeStampFormatter;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.requests.CreatePollRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CreatePollActivity extends BaseActivity {

    public static String firstAddress;
    private Context context;
    private ActivityCreatePollBinding binding;
    private Themes themes;
    private CreatePollAdapter adapter;
    private List<CreatePollModel> alCreatePoll = new ArrayList<>();
    private boolean isEmpty=false;
    private long selectedMilli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_poll);
        context = CreatePollActivity.this;
        themes = new Themes(context);

        setOptionsData();
        initializedControl();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        setToolBar();
        setOnClickListener();
        setDay();
        //openCustomDatePicker();

        setRecycler();
    }

    @Override
    public void setToolBar() {
        binding.createPollTollBar.ivLeft.setVisibility(View.VISIBLE);
        binding.createPollTollBar.tvHeader.setVisibility(View.VISIBLE);
        binding.createPollTollBar.tvHeader.setText("Poll");
    }

    @Override
    public void setOnClickListener() {
        binding.createPollTollBar.ivLeft.setOnClickListener(this);
        binding.etTime.setKeyListener(null);
        binding.etTime.setClickable(true);
        binding.etTime.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        CommonUtils.hideSoftKeyboard(this);
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;

            case R.id.etTime:
                openCustomDatePicker();
                break;
            case R.id.btnPoll:
                if (validate()) {
                   ToastUtils.showToastShort(context,"Poll successfully created.");
                    onBackPressed();

                    // createPollApi();

                }

                break;

            default:
                break;
        }
    }

    private void openCustomDatePicker() {
        TimePickerPopWin pickerPopWin = new TimePickerPopWin.Builder(context, new TimePickerPopWin.OnTimePickListener() {
            @Override
            public void onTimePickCompleted(int days, int hour,int minute, String time,String sbhour) {


                if (days < 1) {
                    binding.etTime.setText("" + hour);
                } else {
                    binding.etTime.setText("" + hour);
                }
                if (hour < 10) {
                    binding.etTime.setText("" + hour);
                } else {
                    binding.etTime.setText("" + hour);
                }

                if (days<1)
                {
                    binding.etTime.setText( "" +sbhour );

                }
                else {
                    binding.etTime.setText( "" +time );

                }


            }

        }).textConfirm("CONFIRM")
                .textCancel("CANCEL")
                .btnTextSize(16)
                .viewTextSize(25)
                .colorCancel(Color.parseColor("#999999"))
                .colorConfirm(Color.parseColor("#009900"))
                .build();
        pickerPopWin.showPopWin(CreatePollActivity.this);


    }
        private boolean validate() {
        clearError();
        for (int i = 0; i < alCreatePoll.size(); i++) {


            if (!alCreatePoll.get(i).getOptions().toString().trim().equalsIgnoreCase("")) {
                isEmpty = false;

            } else {
                isEmpty = true;
                break;
            }
        }

        if (TextUtils.isEmpty(binding.etAskQuestion.getText().toString().trim())) {
            binding.tvQuestionError.setVisibility(View.VISIBLE);
            binding.tvQuestionError.setText("Please enter a question.");
            return false;
        } else if (CommonUtils.lengthFieldValidation(binding.etAskQuestion.getText().toString(), 2)) {
            binding.tvQuestionError.setVisibility(View.VISIBLE);
            binding.tvQuestionError.setText(R.string.validnameerror);
            return false;
        } else if (isEmpty) {
            CommonUtils.showToast(context, context.getString(R.string.enter_option));

            return false;
        } else {
            return true;
        }
    }

    private void clearError() {
        binding.tvQuestionError.setVisibility(View.GONE);
    }

    public void setCurrentTheme() {
        binding.createPollTollBar.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.createPollTollBar.tvHeader.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvPollLength.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvPollLength.setHintTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        themes.changeIconColor(context, binding.createPollTollBar.ivLeft);
        themes.changeIconColor(context, binding.ivDown);
        binding.etAskQuestion.setBackground(ContextCompat.getDrawable(context, themes.setDarkStoryBackground()));
        binding.llTime.setBackground(ContextCompat.getDrawable(context, themes.setDarkStoryBackground()));
        binding.etAskQuestion.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.etTime.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.etTime.setHintTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.etAskQuestion.setHintTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkEditProfileHint()));

        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.tvPollLength.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        // binding.pollLengthSpinner.setBackground(ContextCompat.getDrawable(context, themes.setSpinnerDrawable()));
       /* binding.ivSpinner.setBackground(ContextCompat.getDrawable(context, themes.setSpinnerDrawable()));
        themes.changeIconColor(context, binding.ivSpinner);*/

        binding.pollLengthSpinner.setBackground(ContextCompat.getDrawable(context, themes.setSpinnerDrawable()));

        binding.pollLengthSpinner.setPopupBackgroundDrawable(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkThemeDialog()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));


    }



    private void setDay() {

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,R.layout.custom_spinner,R.array.poll_length);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.poll_length, R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.pollLengthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.pollLengthSpinner.setAdapter(adapter);

    }

    private void setOptionsData() {
        for (int i = 1; i < 3; i++) {

            CreatePollModel pollOptions = new CreatePollModel();
            pollOptions.setOptions("");
            pollOptions.setHintText("Choice " + i);
            alCreatePoll.add(pollOptions);

        }

    }

    public void setRecycler() {
        binding.rvOptions.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.rvOptions.setLayoutManager(layoutManager);
        adapter = new CreatePollAdapter(context, alCreatePoll, (view, position) -> {
            TextView tvAdd = (TextView) view.findViewById(R.id.tvAddOptions);
            switch (view.getId()) {
                case R.id.tvAddOptions:
                    // if (tvAdd.getText().toString().equalsIgnoreCase(AppConstants.PLUS)) {
                    if (alCreatePoll.get(position).isAdd()) {
                        if (alCreatePoll.size() < 4) {
                            CreatePollModel model = new CreatePollModel();
                            alCreatePoll.add(model);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        alCreatePoll.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                    break;
                default:
                    break;
            }
        });
        binding.rvOptions.setAdapter(adapter);

    }


    private void createPollApi() {


        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            CreatePollRequest createPollRequest = new CreatePollRequest();
            for (int i = 0; i < alCreatePoll.size(); i++) {

                if (i == 0) {
                    createPollRequest.setType1(alCreatePoll.get(i).getOptions());
                } else if (i == 1) {
                    createPollRequest.setType2(alCreatePoll.get(i).getOptions());
                } else if (i == 2) {
                    createPollRequest.setType3(alCreatePoll.get(i).getOptions());
                } else if (i == 3) {
                    createPollRequest.setType4(alCreatePoll.get(i).getOptions());
                }

            }

            createPollRequest.setPollTitle(binding.etAskQuestion.getText().toString().trim());

            switch (binding.pollLengthSpinner.getSelectedItem().toString().trim()) {

                case "1 Day":
                    createPollRequest.setValidTill("1");
                    break;
                case "2 Day":
                    createPollRequest.setValidTill("2");
                    break;
                case "1 Week":
                    createPollRequest.setValidTill("7");
                    break;
                case "2 Week":
                    createPollRequest.setValidTill("17");
                    break;
                case "1 Month":
                    createPollRequest.setValidTill("30");
                    break;


            }


            jsonObject.add("poll_attributes", new Gson().toJsonTree(createPollRequest));


            Call<ApiResponse> call = ApiExecutor.getClient(context).createPollApi(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {


                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.body() != null && response.body().getResponseCode() == 200) {


                        if (response.body().getResponseMessage() != null && !response.body().getResponseMessage().equalsIgnoreCase("")) {
                            CommonUtils.showToast(context, response.body().getResponseMessage());
                        }
                        Intent intent = new Intent();
                        intent.putExtra(AppConstants.IS_POLL_ADD, true);
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                    ToastUtils.showToastLong(context, response.message());
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                }
            });
        } else {
            CommonUtils.showToast(context, getString(R.string.internet_alert_msg));
        }
    }


    private class DatePickerPopUpWindow {
    }
}
