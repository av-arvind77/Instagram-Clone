package com.yellowseed.webservices.response.post;

        import java.util.List;
        import android.os.Parcel;
        import android.os.Parcelable;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class PostResponse implements Parcelable
{

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("posts")
    @Expose
    private List<com.yellowseed.webservices.response.homepost.Post> post = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    public final static Parcelable.Creator<PostResponse> CREATOR = new Creator<PostResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PostResponse createFromParcel(Parcel in) {
            return new PostResponse(in);
        }

        public PostResponse[] newArray(int size) {
            return (new PostResponse[size]);
        }

    }
            ;

    protected PostResponse(Parcel in) {
        this.responseCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.responseMessage = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.post, (Post.class.getClassLoader()));
        this.pagination = ((Pagination) in.readValue((Pagination.class.getClassLoader())));
    }

    public PostResponse() {
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<com.yellowseed.webservices.response.homepost.Post> getPost() {
        return post;
    }

    public void setPost(List<com.yellowseed.webservices.response.homepost.Post> post) {
        this.post = post;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(responseCode);
        dest.writeValue(responseMessage);
        dest.writeList(post);
        dest.writeValue(pagination);
    }

    public int describeContents() {
        return 0;
    }

}
