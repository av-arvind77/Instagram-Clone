package com.yellowseed.webservices.response.avatar;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvatarList {

@SerializedName("responseCode")
@Expose
private Integer responseCode;
@SerializedName("responseMessage")
@Expose
private String responseMessage;
@SerializedName("avatar_categories")
@Expose
private List<AvatarCategory> avatarCategories = null;

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

public List<AvatarCategory> getAvatarCategories() {
return avatarCategories;
}

public void setAvatarCategories(List<AvatarCategory> avatarCategories) {
this.avatarCategories = avatarCategories;
}

}