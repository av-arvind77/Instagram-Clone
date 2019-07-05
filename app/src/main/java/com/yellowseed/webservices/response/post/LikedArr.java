       package com.yellowseed.webservices.response.post;

        import android.os.Parcel;
        import android.os.Parcelable;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class LikedArr implements Parcelable
{

    @SerializedName("like_id")
    @Expose
    private String likeId;
    @SerializedName("user_id")
    @Expose
    private UserId userId;
    public final static Parcelable.Creator<LikedArr> CREATOR = new Creator<LikedArr>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LikedArr createFromParcel(Parcel in) {
            return new LikedArr(in);
        }

        public LikedArr[] newArray(int size) {
            return (new LikedArr[size]);
        }

    }
            ;

    protected LikedArr(Parcel in) {
        this.likeId = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((UserId) in.readValue((UserId.class.getClassLoader())));
    }

    public LikedArr() {
    }

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(likeId);
        dest.writeValue(userId);
    }

    public int describeContents() {
        return 0;
    }

}