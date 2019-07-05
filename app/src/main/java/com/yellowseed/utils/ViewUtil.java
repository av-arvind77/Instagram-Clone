package com.yellowseed.utils;

import android.view.View;
import android.view.ViewGroup;

public class ViewUtil {

    public static View findViewByPosition(ViewGroup parentView, float x, float y){

        if (parentView == null) return null;
        if (parentView.getWidth() == 0 || parentView.getHeight() == 0) return null;

        for (int i = parentView.getChildCount() - 1; i > -1 ; i--){
            View view = parentView.getChildAt(i);
            if (view.getWidth() == 0 || view.getHeight() ==0) continue;

            float top = view.getY();
            float bottom = top + view.getHeight();
            float left = view.getX();
            float right = left + view.getWidth();

            if ((y >= top && y <= bottom) && (x >= left && x <= right)){
                return view;
            }

        }

        return null;
    }

}