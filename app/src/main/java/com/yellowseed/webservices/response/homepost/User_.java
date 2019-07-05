
package com.yellowseed.webservices.response.homepost;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User_ implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("image")
    @Expose
    private String image;

    public final static Creator<User_> CREATOR = new Creator<User_>() {


        @SuppressWarnings({
            "unchecked"
        })
        public User_ createFromParcel(Parcel in) {
            return new User_(in);
        }

        public User_[] newArray(int size) {
            return (new User_[size]);
        }

    }
    ;

    protected User_(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public User_() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(email);
        dest.writeValue(name);
        dest.writeValue(image);
    }

    public int describeContents() {
        return  0;
    }

}
