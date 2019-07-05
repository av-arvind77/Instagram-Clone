package com.yellowseed.webservices.response.avatar;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvatarCategory {

@SerializedName("id")
@Expose
private String id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("avatar_image")
@Expose
private List<AvatarImage> avatarImage = null;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public List<AvatarImage> getAvatarImage() {
return avatarImage;
}

public void setAvatarImage(List<AvatarImage> avatarImage) {
this.avatarImage = avatarImage;
}

}