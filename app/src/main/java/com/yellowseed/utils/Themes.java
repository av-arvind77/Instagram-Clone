package com.yellowseed.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.yellowseed.R;

public class Themes {


    private static Themes instance;
    private Context context;

    public Themes(Context context) {
        super();
        this.context=context;
    }

    public static Themes getInstance(Context context) {
        if (instance == null) {
            synchronized (Themes.class) {
                if (instance == null) {
                    instance = new Themes(context);
                }
            }
        }
        return instance;
    }


    public int setDarkTheme() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.theme_black;
        } else {
            return R.color.creamWhite;
        }

    }  public int setLightTheme() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.creamWhite;
        } else {
            return R.color.theme_black;
        }

    }

    public int setDarkBottom() {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.bottom_bg;
        } else {
            return R.color.lightwhite;
        }

    }

    public int setDarkThemeDialog() {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.grey_bg;
        } else {
            return R.color.creamWhite;
        }

    }


    public int setBackgroundDialog() {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.hint_editProfile_color;
        } else {
            return R.color.creamWhite;
        }

    }

    public int setMuteBackground() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.lightblack;
        } else {
            return R.color.orange;
        }

    }
    public int setPinBackround() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.grey ;
        } else {
            return R.color.blue_sky;
        }

    }




    public int setChatCardColor() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.dialog_background;
        } else {
            return R.color.gray_dark;
        }

    }

    public int setChatCardPressedColor() {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.grey_avtar_color;
        } else {
            return R.color.darkgrey;
        }

    }

    public int setDarkChatBox() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.search_black;
        } else {
            return R.color.white;
        }

    }


    public int setDarkThemeStory() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.black;
        } else {
            return R.color.storycolor;
        }

    }

    public int setDarkThemeStory2() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.theme_black;
        } else {
            return R.color.storycolor;
        }

    }

    public int setTolbarText() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.yellow_toolbar_text;
        } else {
            return R.color.skyblue;
        }

    }
    public int setSeeAll() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.yellow_toolbar_text;
        } else {
            return R.color.vote_blue;
        }

    }
    public int setBlueContact() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.white;
        } else {
            return R.color.vote_blue;
        }

    }


    public int setPinnedText() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.yellow_toolbar_text;
        } else {
            return R.color.grey;
        }

    }

    public int setToolbarWhiteText() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.white;
        } else {
            return R.color.theme_black;
        }

    }

    public int setViewBottomColor( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.black;
        } else {
            return R.color.storycolor;
        }

    }
    public int setDarkThemeText( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.white;
        } else {
            return R.color.theme_black;
        }

    }
    public int setSpamText( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.white;
        } else {
            return R.color.red;
        }

    }
    public int setCancel( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.red;
        } else {
            return R.color.vote_blue;
        }

    }


    public int setDarkThemeGreyText( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.greytext;
        } else {
            return R.color.grey;
        }

    }


    public int setViewLine( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.grey;
        } else {
            return R.color.theme_black;
        }

    }

    public int setViewLineGrey( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.line_grey;
        } else {
            return R.color.lightgrey;
        }

    }
    public int setAvtarViewLine( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.avatargrey;
        } else {
            return R.color.lightgrey;
        }

    }
    public int setAvtarImageTheme( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.avatargrey;
        } else {
            return R.color.avatarbackcolor;
        }

    }
    public int setDarkSearchDrawable( ){
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)){
            return R.drawable.bg_dark_search;
        } else {
            return R.drawable.bg_rounded_transparent;
        }

    }
    public int setChatOptionDrawble( ){
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)){
            return R.drawable.bg_curved_black;
        } else {
            return R.drawable.bg_curved_chattextet;
        }

    }
    public int setLiveUserDrawble( ){
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)){
            return R.drawable.bg_live_user_dark;
        } else {
            return R.drawable.bg_live_users;
        }

    }

    public int setDarkThemeViewColor() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.view_light_grey;
        } else {
            return R.color.viewLineColor;
        }

    }

    public int setRejectDrawable( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.drawable.bg_dark_curved_reject;
        } else {
            return R.drawable.bg_curved_reject;
        }

    }
    public int setFollowButtonDrawble( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.drawable.bg_follow_button;
        } else {
            return R.drawable.bg_home_edittext;
        }

    }
    public int setDarkStoryBackground( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.drawable.bg_home_dark_story;
        } else {
            return R.drawable.bg_home_edittext;
        }

    }

    public int setSpinnerDarwable() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.drawable.bg_spinner_white;
        } else {
            return R.drawable.bg_spinner;
        }

    }

    public int setLiveUserDarwable() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.drawable.bg_live_users_dark;
        } else {
            return R.drawable.bg_live_users;
        }

    }
    public int setDarkEditProfileBackground() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.drawable.bg_edittext;
        } else {
            return R.drawable.bg_rounded_transparent;
        }

    }
    public int setSpinnerBackgound( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.color.darkgrey;
        } else {
            return R.color.creamWhite;
        }

    }
    public int setNoLayoutColor() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.no_dark;
        } else {
            return R.color.no_day;
        }

    }
    public int setYesLayoutColor() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.yes_dark;
        } else {
            return R.color.no_day;
        }

    }

    public int setDarkGreyTextColor() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.white;
        } else {
            return R.color.grey;
        }

    }


    public int setDarkGreyStoryTextColor() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.grey;
        } else {
            return R.color.grey;
        }

    }


    public int setGreyHint() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.grey_hin_color;
        } else {
            return R.color.text_color_dark_grey;
        }
    }
    public int setVotesGrey() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.grey_hin_color;
        } else {
            return R.color.grey_hin_color;
        }
    }
    public int setLightThemeText() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.grey_hin_color;
        } else {
            return R.color.grey;
        }
    }

    public int setSuggestionText() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.grey;
        } else {
            return R.color.theme_black;
        }

    }
    public int setYellow() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.darkyellow;
        } else {
            return R.color.black;
        }

    }

    public int setDarkHintColor() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.hint_grey_color;
        } else {
            return R.color.theme_black;
        }

    }

    public int setDarkEditProfileHint() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.hint_editProfile_color;
        } else {
            return R.color.grey;
        }

    }

    public int setDarkRoundDrawable() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.drawable.bg_dark_comment;
        } else {
            return R.drawable.bg_home_edittext;
        }
    }
    public int setDarkComment() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.drawable.bg_black_theme_comment;
        } else {
            return R.drawable.bg_write_comment;
        }
    }


    public int setSuggestionViewLine() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.black;
        } else {
            return R.color.lightest_grey;
        }
    }


    public int setDarkGreyBackground() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            return R.color.blackdim;
        else
            return R.color.white;
    }

    public void changeCrossIconColor(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME))
            imageView.setColorFilter(ContextCompat.getColor(context,R.color.grey),PorterDuff.Mode.SRC_ATOP);
        else
            imageView.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
    }
    public void changeCallIcon(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            imageView.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);

        else
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.skyblue), PorterDuff.Mode.SRC_ATOP);

    } public void changeRightIcon(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.darkyellow), PorterDuff.Mode.SRC_ATOP);

        else
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.skyblue), PorterDuff.Mode.SRC_ATOP);

    }

    public void changeIconColor(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.darkyellow), PorterDuff.Mode.SRC_ATOP);
        else
            imageView.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
    }
    public void changeHideIcon(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            imageView.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);

        else
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.skyblue), PorterDuff.Mode.SRC_ATOP);
    }


    public void changePostIconColor(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_ATOP);
        else
            imageView.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
    }

    public void changeAnonymousColor(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.creamWhite), PorterDuff.Mode.SRC_ATOP);
        else
            imageView.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
    }


    public void changeIconColorToWhite(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_ATOP);
        else
            imageView.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
    }
    public void changeDownloadIcon(Context context, ImageView imageView) {
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_ATOP);

    }

    public void changeShareIcon(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            imageView.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);

        else
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.theme_black), PorterDuff.Mode.SRC_ATOP);
    }



    public void changeCallType(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            imageView.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);

        else
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.grey), PorterDuff.Mode.SRC_ATOP);
    }

    public void changePinColor(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_ATOP);
        else
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
    }
    public void changeInfoColor(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_ATOP);
        else
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.skyblue), PorterDuff.Mode.SRC_ATOP);
    }
    public void changeIconWhite(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_ATOP);
        else
            imageView.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
    }

    public int setPopupBackground() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            return R.color.dialog_background;
        else
            return R.color.transparent_white;
    }

    public int setRoundedBlackBackground() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME))
            return R.drawable.bg_rounded_black_theme;
        else
            return R.drawable.bg_rounded_transparent;
    }

    public int setEditTextBackground( ) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {
            return R.drawable.bg_home_edittext_dark_theme;
        } else {
            return R.drawable.bg_home_edittext;
        }
    }


    public int setSpinnerDrawable() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.drawable.bg_spinner_white;
        } else {
            return R.drawable.bg_spinner;
        }
    }

    public int setPollSelectedDrawable() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.drawable.blue_poll_drawable;
        } else {
            return R.drawable.blue_poll_drawable;
        }
    }

    public void changeArrowColor(Context context, ImageView imageView) {
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME))
            imageView.setColorFilter(ContextCompat.getColor(context,R.color.grey),PorterDuff.Mode.SRC_ATOP);
        else
            imageView.setColorFilter(ContextCompat.getColor(context,R.color.grey),PorterDuff.Mode.SRC_ATOP);
    }
    public int setPollUnSelectedDrawable() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.drawable.white_poll_drawable;
        } else {
            return R.drawable.grey_poll_drawable;
        }
    }

    public int setPollSelectedColor() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.poll_color;
        } else {
            return R.color.poll_color;
        }
    }

    public int setPollUnSelectedColor() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.white;
        } else {
            return R.color.grey;
        }
    }

    public int setFollowBg() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.drawable.bg_yellow_soft_corner;
        } else {
            return R.drawable.bg_rounded_transparent;
        }
    }

/*
public int setDarkThemeText() {
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.white;
        } else {
            return R.color.theme_black;
        }
    }
* */

    public int setSelectedIndicatorColor() {
        if (!CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            return R.color.yellow;
        } else {
            return R.color.black;
        }

    }



}
