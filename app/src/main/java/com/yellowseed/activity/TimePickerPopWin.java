package com.yellowseed.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bruce.pickerview.LoopScrollListener;
import com.bruce.pickerview.LoopView;
import com.yellowseed.R;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimePickerPopWin extends PopupWindow implements View.OnClickListener {

    List<String> hourList = new ArrayList();
    List<String> minList = new ArrayList();
    List<String> dayList = new ArrayList();
    List<String> meridianList = new ArrayList();
    private Button cancelBtn;
    private Button confirmBtn;
    private LoopView hourLoopView;
    private LoopView daysLoopView;
    private LoopView minuteLoopView;
    private LoopView meridianLoopView;
    private View pickerContainerV;
    private View contentView;
    private int hourPos = 0;
    private int minutePos = 0;
    private int meridianPos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private TextView tvHours,tvDays,tvMinutes;
    private int colorConfirm;
    private int btnTextsize;
    private int viewTextSize;
    private int day = 0;
    private View dateView;
    private View relativeView;
    private OnTimePickListener mListener;

    public TimePickerPopWin(Builder builder) {
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        initView();
    }

    /**
     * Transform int to String with prefix "0" if less than 10
     *
     * @param num
     * @return
     */
    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    private void initView() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_time_picker, null);
        dateView = contentView.findViewById(R.id.llDate);
        relativeView = contentView.findViewById(R.id.container_picker);
       dateView.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkChatBox()));
        relativeView.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkStoryBackground()));

        cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
        cancelBtn.setTextColor(colorCancel);
        cancelBtn.setTextSize(btnTextsize);
        confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);
        confirmBtn.setTextColor(colorConfirm);
        confirmBtn.setTextSize(btnTextsize);
        hourLoopView = (LoopView) contentView.findViewById(R.id.picker_hour);
        daysLoopView = (LoopView) contentView.findViewById(R.id.picker_days);
        minuteLoopView = (LoopView) contentView.findViewById(R.id.picker_minute);
        tvDays = (TextView) contentView.findViewById(R.id.tvDays);
        tvHours = (TextView) contentView.findViewById(R.id.tvHour);
        tvMinutes = (TextView) contentView.findViewById(R.id.tvMinutes);
        tvDays.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvHours.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvMinutes.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));


        relativeView.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkStoryBackground()));



        //       meridianLoopView = (LoopView) contentView.findViewById(R.id.picker_meridian);
        pickerContainerV = contentView.findViewById(R.id.container_picker);


        hourLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {

                hourPos  = item;
            }
        });
        daysLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                day =item;
            }
        });

        minuteLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                minutePos = item;
            }
        });

           /* meridianLoopView.setLoopListener(new LoopScrollListener() {
                @Override
                public void onItemSelect(int item) {
                    meridianPos=item;
                }
            });
*/
        initPickerViews();  // init hour and minute loop view


        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);

        if (!TextUtils.isEmpty(textConfirm)) {
            confirmBtn.setText(textConfirm);
        }

        if (!TextUtils.isEmpty(textCancel)) {
            cancelBtn.setText(textCancel);
        }

        setTouchable(true);
        setFocusable(true);

        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void initPickerViews() {

        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        hourPos = Calendar.getInstance().get(Calendar.HOUR) - 1;
        minutePos = Calendar.getInstance().get(Calendar.MINUTE);
        //  meridianPos=Calendar.getInstance().get(Calendar.AM_PM);

        for (int i = 0; i <= 30; i++) {
            dayList.add(format2LenStr(i));
        }

        for (int i = 1; i <= 23; i++) {
            hourList.add(format2LenStr(i));
        }

        for (int j = 0; j < 60; j++) {
            minList.add(format2LenStr(j));
        }

         /*   meridianList.add("AM");
            meridianList.add("PM");*/
        daysLoopView.setDataList(dayList);
        daysLoopView.setInitPosition(day);
        hourLoopView.setDataList(hourList);
        hourLoopView.setInitPosition(hourPos);

        minuteLoopView.setDataList(minList);
        minuteLoopView.setInitPosition(minutePos);

        //  meridianLoopView.setDataList(meridianList);
        // meridianLoopView.setInitPosition(meridianPos);
    }


    @Override
    public void onClick(View v) {

        if (v == contentView || v == cancelBtn) {
            dismissPopWin();
        } else if (v == confirmBtn) {

            if (null != mListener) {
                //    String amPm=meridianList.get(meridianPos);

                StringBuffer sb = new StringBuffer();
                StringBuffer sbHour = new StringBuffer();


                sb.append(String.valueOf(dayList.get(day)) +" Days");
                sbHour.append(String.valueOf(dayList.get(hourPos)) +" Hours");
                //sb.append(String.valueOf(hourList.get(hourPos)) + "hr");

             //   sb.append(String.valueOf(minList.get(minutePos)) + "Min");
                // sb.append(amPm);
                mListener.onTimePickCompleted(day, hourPos , minutePos, sb.toString(),sbHour.toString());
            }
            dismissPopWin();
        }
    }

    /**
     * Show time picker popWindow
     *
     * @param activity
     */
    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
                    0, 0);
            trans.setDuration(400);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());

            pickerContainerV.startAnimation(trans);
        }
    }

    /**
     * Dismiss time picker popWindow
     */
    public void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(400);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                dismiss();
            }
        });

        pickerContainerV.startAnimation(trans);
    }

    public interface OnTimePickListener {

        /**
         * Listener when date been selected
         *
         * @param time
         */
        void onTimePickCompleted(int day, int hour, int min, String time,String sbHour);
    }

    public static class Builder {
        private Context context;
        private OnTimePickListener listener;
        //Optional Parameters
        private String textCancel = "Cancel";
        private String textConfirm = "Confirm";
        private int colorCancel = Color.parseColor("#999999");
        private int colorConfirm = Color.parseColor("#303F9F");
        private int btnTextSize = 16;//text btnTextsize of cancel and confirm button
        private int viewTextSize = 25;
        public Builder(Context context, OnTimePickListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public Builder textCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }

        public Builder textConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }

        public Builder colorCancel(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }

        public Builder colorConfirm(int colorConfirm) {
            this.colorConfirm = colorConfirm;
            return this;
        }

        public Builder btnTextSize(int textSize) {
            this.btnTextSize = textSize;
            return this;
        }

        public Builder viewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public TimePickerPopWin build() {
            return new TimePickerPopWin(this);
        }
    }
}





