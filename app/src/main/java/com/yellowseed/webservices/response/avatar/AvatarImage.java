package com.yellowseed.webservices.response.avatar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvatarImage {

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

}