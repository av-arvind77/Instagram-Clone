
package com.yellowseed.webservices.response.homepost;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User__ implements Serializable
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
    public final static Creator<User__> CREATOR = new Creator<User__>() {


        @SuppressWarnings({
            "unchecked"
        })
        public User__ createFromParcel(Parcel in) {
            return new User__(in);
        }

        public User__[] newArray(int size) {
            return (new User__[size]);
        }

    }
    ;

    protected User__(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public User__() {
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(email);
        dest.writeValue(name);
    }

    public int describeContents() {
        return  0;
    }

}
