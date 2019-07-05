package com.yellowseed.record_view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.yellowseed.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by Devlomi on 13/12/2017.
 */

public class RecordButton extends AppCompatImageView implements View.OnTouchListener, View.OnClickListener, RecordViewResultListner {

    private ScaleAnim scaleAnim;
    private RecordView recordView;
    private LinearLayout llText;
    private boolean listenForRecord = true;
    private OnRecordClickListener onRecordClickListener;
    private Context context;
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    private String fileName= "";
    private OnRecordClickListener resultListner;
    private OnRecordAudioResult onRecordAudioResult;

    public void setRecordView(RecordView recordView, LinearLayout llText) {
        this.recordView = recordView;
        this.recordView.setRecordViewResultListner(this);
        this.llText = llText;
    }

    public RecordButton(Context context) {
        super(context);
        init(context, null);
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);


    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordButton);

            int imageResource = typedArray.getResourceId(R.styleable.RecordButton_mic_icon, -1);


            if (imageResource != -1) {
                setTheImageResource(imageResource);
            }

            typedArray.recycle();
        }


        scaleAnim = new ScaleAnim(this);


        this.setOnTouchListener(this);
        this.setOnClickListener(this);


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setClip(this);
    }

    public void setClip(View v) {
        if (v.getParent() == null) {
            return;
        }

        if (v instanceof ViewGroup) {
            ((ViewGroup) v).setClipChildren(false);
            ((ViewGroup) v).setClipToPadding(false);
        }

        if (v.getParent() instanceof View) {
            setClip((View) v.getParent());
        }
    }


    private void setTheImageResource(int imageResource) {
        Drawable image = AppCompatResources.getDrawable(getContext(), imageResource);
        setImageDrawable(image);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isListenForRecord()) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                   recordView.onActionDown((RecordButton) v, event);
                    llText.setVisibility(GONE);
                    recordView.setVisibility(VISIBLE);
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(((Activity) context), new String[]{Manifest.permission.RECORD_AUDIO}, 10011);
                    }else {
                        startRecording();
                    }
                    break;


                case MotionEvent.ACTION_MOVE:
                    recordView.onActionMove((RecordButton) v, event);
                    break;

                case MotionEvent.ACTION_UP:
                    recordView.onActionUp((RecordButton) v);
                    llText.setVisibility(VISIBLE);
                    recordView.setVisibility(GONE);
                    stopRecording();
                    break;

            }

        }
        return isListenForRecord();


    }

    public void setResultListner(OnRecordAudioResult onRecordAudioResult){
        this.onRecordAudioResult = onRecordAudioResult;
    }
    protected void startScale() {
        scaleAnim.start();
    }

    protected void stopScale() {
        scaleAnim.stop();
    }

    public void setListenForRecord(boolean listenForRecord) {
        this.listenForRecord = listenForRecord;
    }

    public boolean isListenForRecord() {
        return listenForRecord;
    }

    public void setOnRecordClickListener(OnRecordClickListener onRecordClickListener) {
        this.onRecordClickListener = onRecordClickListener;
    }


    @Override
    public void onClick(View v) {
        if (onRecordClickListener != null)
            onRecordClickListener.onClick(v);
    }


    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        fileName = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".amr");
        return fileName;
    }

    private void startRecording(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getFilename());
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);


        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
        }
    };

    private void stopRecording(){
        try{
            if(null != recorder){
                recorder.stop();
                recorder.release();

                recorder = null;

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(boolean isSuccess) {
        if(isSuccess){
//            Log.e("file", ""+fileName);
//            if(context instanceof ChatActivity)
//                ((ChatActivity)context).getAudioUrl(fileName);
//
//            if(context instanceof GenericChatActivity)
//                ((GenericChatActivity)context).getAudioUrl(fileName);
            onRecordAudioResult.onSuccess(fileName);
        }
    }
}
