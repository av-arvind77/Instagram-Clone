package com.yellowseed.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.yellowseed.R;
import com.yellowseed.listener.OkCancelInterface;

public class DialogUtils {

//    ((ViewGroup)scrollChildLayout.getParent()).removeView(scrollChildLayout);


    public static void createDialog(Context context, ViewDataBinding dialogLayout, boolean cancelable) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(cancelable);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        layoutParams.dimAmount = 0.65f;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.setContentView(dialogLayout.getRoot());
        dialog.show();
    }

    public static void createDialog(Context context, int dialogLayout, boolean cancelable) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(cancelable);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        layoutParams.dimAmount = 0.65f;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.setContentView(dialogLayout);
        dialog.show();
    }

    public static Dialog createDialog(Context context, int dialogLayout) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(dialogLayout);

        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
        return dialog;
    }


    public static Dialog createDialog(Context context, View dialogLayout) {
        Dialog dialog = new Dialog(context, R.style.Theme_AppCompat);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(dialogLayout);

        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        dialog.show();
        return dialog;
    }

    public static Dialog customProgressDialog(final Context mcontext/*, String message*/) {

        final TextView tvRegistering;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final Dialog mDialog = new Dialog(mcontext, android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.75f;
        mDialog.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow();
        mDialog.getWindow().setAttributes(lp);
        ViewGroup nullSet = null;
        View dialoglayout = inflater.inflate(R.layout.custom_dialog, nullSet);
        tvRegistering = (TextView) dialoglayout.findViewById(R.id.tv_msg);
        AVLoadingIndicatorView cpDialog = (AVLoadingIndicatorView) dialoglayout.findViewById(R.id.cpDialog);
        cpDialog.show();
//        tvRegistering.setText(message);
        tvRegistering.setText("");
        mDialog.setContentView(dialoglayout);

        return mDialog;
    }

    public static void dilaogOkWithInterFace(final Context mContext, String msg, final OkCancelInterface okCancelInterface) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(msg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                okCancelInterface.onSuccess();
                dialog.dismiss();

            }
        });
        builder.show();
    }


}