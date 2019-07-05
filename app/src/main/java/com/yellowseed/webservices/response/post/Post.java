package com.yellowseed.webservices.response.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post implements Parcelable
{

    @SerializedName("post")
    @Expose
    private Post_ post;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("liked_arr")
    @Expose
    private List<LikedArr> likedArr = null;
    @SerializedName("comments")
    @Expose
    private Integer comments;
    @SerializedName("comment_arr")
    @Expose
    private List<CommentArr> commentArr = null;
    public final static Parcelable.Creator<Post> CREATOR = new Creator<Post>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return (new Post[size]);
        }

    }
            ;

    protected Post(Parcel in) {
        this.post = ((Post_) in.readValue((Post_.class.getClassLoader())));
        in.readList(this.images, (Image.class.getClassLoader()));
        this.likesCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.likedArr, (LikedArr.class.getClassLoader()));
        this.comments = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.commentArr, (CommentArr.class.getClassLoader()));
    }

    public Post() {
    }

    public Post_ getPost() {
        return post;
    }

    public void setPost(Post_ post) {
        this.post = post;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public List<LikedArr> getLikedArr() {
        return likedArr;
    }

    public void setLikedArr(List<LikedArr> likedArr) {
        this.likedArr = likedArr;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public List<CommentArr> getCommentArr() {
        return commentArr;
    }

    public void setCommentArr(List<CommentArr> commentArr) {
        this.commentArr = commentArr;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(post);
        dest.writeList(images);
        dest.writeValue(likesCount);
        dest.writeList(likedArr);
        dest.writeValue(comments);
        dest.writeList(commentArr);
    }

    public int describeContents() {
        return 0;
    }

}