package com.yellowseed.webservices.response.post;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image implements Parcelable
{

@SerializedName("id")
@Expose
private String id;
@SerializedName("file")
@Expose
private File file;
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
public final static Parcelable.Creator<Image> CREATOR = new Creator<Image>() {


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
dest.writeValue(imageableId);
dest.writeValue(imageableType);
dest.writeValue(createdAt);
dest.writeValue(updatedAt);
}

public int describeContents() {
return 0;
}

}