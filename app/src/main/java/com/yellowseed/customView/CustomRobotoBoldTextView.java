package com.yellowseed.customView;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CustomRobotoBoldTextView extends AppCompatTextView {

    public CustomRobotoBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //    init();
        this.setTypeface(setFont(context));
    }

    public CustomRobotoBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(setFont(context));
    }

    public CustomRobotoBoldTextView(Context context) {
        super(context);
        this.setTypeface(setFont(context));

    }


    public Typeface setFont(Context context) {
/*
        return Typeface.createFromAsset(context.getAssets(), "fonts/Arial-Bold.ttf");
*/

        return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");

    }

}
