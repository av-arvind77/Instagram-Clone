package com.yellowseed.listener;

public interface DrawableClickListener {
    public static enum DrawablePosition {TOP, BOTTOM, LEFT, RIGHT};



    public void onClick(DrawablePosition target);
}
