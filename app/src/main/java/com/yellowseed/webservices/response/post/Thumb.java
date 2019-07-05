package com.yellowseed.webservices.response.post;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumb implements Parcelable
{

@SerializedName("url")
@Expose
private String url;
public final static Parcelable.Creator<Thumb> CREATOR = new Creator<Thumb>() {


@SuppressWarnings({
"unchecked"
})
public Thumb createFromParcel(Parcel in) {
return new Thumb(in);
}

public Thumb[] newArray(int size) {
return (new Thumb[size]);
}

}
;

protected Thumb(Parcel in) {
this.url = ((String) in.readValue((String.class.getClassLoader())));
}

public Thumb() {
}

public String getUrl() {
return url;
}

public void setUrl(String url) {
this.url = url;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(url);
}

public int describeContents() {
return 0;
}

}