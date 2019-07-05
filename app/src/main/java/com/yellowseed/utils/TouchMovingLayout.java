package com.yellowseed.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TouchMovingLayout extends RelativeLayout implements View.OnTouchListener{

    public static interface OnChildViewClickListener{
        public void onChildViewClick(View view);
        public void onChildViewLongClick(View view);
    }


    private OnChildViewClickListener onChildViewClickListener;
    private GestureDetector gestureDetector;
    private ImageView recyclerIcon;
    private View touchedView;
    float xOffset = 0;
    float yOffset = 0;

    public TouchMovingLayout(Context context) {
        this(context, null);
    }

    public TouchMovingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchMovingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        recyclerIcon = new ImageView(getContext());
        LayoutParams params = new LayoutParams
                (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.bottomMargin = (int)(getContext().getResources().getDisplayMetrics().density * 24.0f);
        recyclerIcon.setLayoutParams(params);
        recyclerIcon.setVisibility(INVISIBLE);
        addView(recyclerIcon);

        gestureDetector = new GestureDetector(getContext(), gestureListener);
        gestureDetector.setIsLongpressEnabled(true);

        setClickable(true);
        setOnTouchListener(this);
    }

    public void setOnChildViewClickListener(OnChildViewClickListener onChildViewClickListener){
        this.onChildViewClickListener = onChildViewClickListener;
    }

    private boolean shouldDeleteElement(View elementView){

        float bottom = elementView.getY() + elementView.getHeight();
        float left = elementView.getX();
        float startSpot = recyclerIcon.getX() -  elementView.getWidth();
        float endSpot = recyclerIcon.getX() + recyclerIcon.getWidth();

        if (bottom > recyclerIcon.getY() && (left > startSpot && left < endSpot)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){

            touchedView = ViewUtil.findViewByPosition
                    (this, motionEvent.getX(), motionEvent.getY());

            if (touchedView != null){

                if (touchedView == recyclerIcon){
                    touchedView = null;
                    return false;
                }

                xOffset = motionEvent.getX() - touchedView.getX();
                yOffset = motionEvent.getY() - touchedView.getY();
                //touchedView.setBackgroundResource(R.drawable.rectangle_background_white_black_das);
                recyclerIcon.setVisibility(View.VISIBLE);
            }
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP){
            if (touchedView != null){
                touchedView.setBackgroundColor(Color.TRANSPARENT);

                if (shouldDeleteElement(touchedView)){
                    removeView(touchedView);
                }

                touchedView = null;
            }

            recyclerIcon.setVisibility(View.INVISIBLE);
        }

        if (touchedView != null){
            touchedView.setX(motionEvent.getX() - xOffset);
            touchedView.setY(motionEvent.getY() - yOffset);

            if (shouldDeleteElement(touchedView)){
                //recyclerIcon.setBackgroundResource(R.drawable.rectangle_background_white_black_das);
                recyclerIcon.getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            }else{
                recyclerIcon.setBackgroundColor(Color.TRANSPARENT);
                recyclerIcon.getDrawable().clearColorFilter();
            }
        }

        gestureDetector.onTouchEvent(motionEvent);
        return false;
    }


    private GestureDetector.OnGestureListener gestureListener =
            new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (onChildViewClickListener != null){

                onChildViewClickListener.onChildViewClick(
                        ViewUtil.findViewByPosition(TouchMovingLayout.this,
                                motionEvent.getX(), motionEvent.getY())
                );
            }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            if (onChildViewClickListener != null){

                onChildViewClickListener.onChildViewLongClick(
                        ViewUtil.findViewByPosition(TouchMovingLayout.this,
                                motionEvent.getX(), motionEvent.getY())
                );
            }
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}