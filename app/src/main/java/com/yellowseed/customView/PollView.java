package com.yellowseed.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class PollView extends FrameLayout {
    private final Context context;
    private Bitmap bitmap;
    private View location;

    public PollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public void setLocationView(View view){
        this.location = view;
        addView(location);
        location.setX(0);
        location.setY(0);
        invalidate();
    }



    public class PollingView extends View{

        public PollingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
           // super.onDraw(canvas);
            if(bitmap!=null) {
                canvas.drawBitmap(bitmap, 0, 0, null);
            }
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
