
package com.yellowseed.webservices.response.homepost;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Image implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("file")
    @Expose
    private File file;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("imageable_id")
    @Expose
    private String imageableId;
    @SerializedName("imageable_type")
    @Expose
    private String imageableType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    public final static Creator<Image> CREATOR = new Creator<Image>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        public Image[] newArray(int size) {
            return (new Image[size]);
        }

    }
    ;

    protected Image(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.file = ((File) in.readValue((File.class.getClassLoader())));
        this.width = ((String) in.readValue((String.class.getClassLoader())));
        this.height = ((String) in.readValue((String.class.getClassLoader())));
        this.fileType = ((String) in.readValue((String.class.getClassLoader())));
        this.imageableId = ((String) in.readValue((String.class.getClassLoader())));
        this.imageableType = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Image() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getImageableId() {
        return imageableId;
    }

    public void setImageableId(String imageableId) {
        this.imageableId = imageableId;
    }

    public String getImageableType() {
        return imageableType;
    }

    public void setImageableType(String imageableType) {
        this.imageableType = imageableType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(file);
        dest.writeValue(width);
        dest.writeValue(height);
        dest.writeValue(fileType);
        dest.writeValue(imageableId);
        dest.writeValue(imageableType);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
    }

    public int describeContents() {
        return  0;
    }

}
