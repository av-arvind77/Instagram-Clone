package com.yellowseed.webservices.response.post;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumb1 implements Parcelable
{

@SerializedName("url")
@Expose
private String url;
public final static Parcelable.Creator<Thumb1> CREATOR = new Creator<Thumb1>() {


@SuppressWarnings({
"unchecked"
})
public Thumb1 createFromParcel(Parcel in) {
return new Thumb1(in);
}

public Thumb1 [] newArray(int size) {
return (new Thumb1[size]);
}

}
;

protected Thumb1(Parcel in) {
this.url = ((String) in.readValue((String.class.getClassLoader())));
}

public Thumb1() {
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