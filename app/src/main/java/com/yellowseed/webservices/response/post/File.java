package com.yellowseed.webservices.response.post;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class File extends Drawable implements Parcelable
{

@SerializedName("url")
@Expose
private String url;
@SerializedName("thumb")
@Expose
private Thumb thumb;
@SerializedName("thumb1")
@Expose
private Thumb1 thumb1;
public final static Parcelable.Creator<File> CREATOR = new Creator<File>() {


@SuppressWarnings({
"unchecked"
})
public File createFromParcel(Parcel in) {
return new File(in);
}

public File[] newArray(int size) {
return (new File[size]);
}

}
;

public File(Parcel in) {
this.url = ((String) in.readValue((String.class.getClassLoader())));
this.thumb = ((Thumb) in.readValue((Thumb.class.getClassLoader())));
this.thumb1 = ((Thumb1) in.readValue((Thumb1.class.getClassLoader())));
}

public File() {
}

    @Override
    public void draw(@NonNull Canvas canvas) {

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    public String getUrl() {
return url;
}

public void setUrl(String url) {
this.url = url;
}

public Thumb getThumb() {
return thumb;
}

public void setThumb(Thumb thumb) {
this.thumb = thumb;
}

public Thumb1 getThumb1() {
return thumb1;
}

public void setThumb1(Thumb1 thumb1) {
this.thumb1 = thumb1;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(url);
dest.writeValue(thumb);
dest.writeValue(thumb1);
}

public int describeContents() {
return 0;
}

}