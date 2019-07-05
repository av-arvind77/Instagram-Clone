
package com.yellowseed.webservices.response.homepost;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class File implements Serializable
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


    public File() {
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


    public int describeContents() {
        return  0;
    }

}
