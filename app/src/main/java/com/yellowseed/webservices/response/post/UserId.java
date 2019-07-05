
        package com.yellowseed.webservices.response.post;

        import android.os.Parcel;
        import android.os.Parcelable;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class UserId implements Parcelable
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
    public final static Parcelable.Creator<UserId> CREATOR = new Creator<UserId>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UserId createFromParcel(Parcel in) {
            return new UserId(in);
        }

        public UserId[] newArray(int size) {
            return (new UserId[size]);
        }

    }
            ;

    protected UserId(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UserId() {
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
        return 0;
    }

}