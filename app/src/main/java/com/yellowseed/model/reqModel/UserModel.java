package com.yellowseed.model.reqModel;

import android.text.TextUtils;

import com.yellowseed.model.AvtarImageModel;
import com.yellowseed.model.resModel.GetRoomResonse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ankit_mobiloitte on 5/6/18.
 */

public class UserModel implements Serializable {

    private String id;
    private String email;
    private String password;
    private String name;
    private String mobile;
    private String user_name;
    private String uid;
    private String access_token;
    private String code;
    private String old_password;
    private String new_password;
    private String confirm_new_password;
    private String bio;
    private String website;
    private String hometown;
    private String phone_no;
    private String mute_noti;
    private String is_follow;
    private String address;
    private String country;
    private String gender;
    private String status;
    private String otp;
    private String otp_gen_time;
    private String created_at;
    private String updated_at;
    private String existing;
    private String avatar_image;

    public String getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(String avatar_image) {
        this.avatar_image = avatar_image;
    }

    private AvtarImageModelNew avatar_img;
    private String qb_id;

    private GetRoomResonse.RoomsBean room_info;

    public GetRoomResonse.RoomsBean getRoom_info() {
        return room_info;
    }

    public void setRoom_info(GetRoomResonse.RoomsBean room_info) {
        this.room_info = room_info;
    }
    public String getQb_id() {
        return qb_id;
    }

    public void setQb_id(String qb_id) {
        this.qb_id = qb_id;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    private String dob;

    public String getLives_in() {
        return lives_in;
    }

    public void setLives_in(String lives_in) {
        this.lives_in = lives_in;
    }

    private String lives_in;

    public AvtarImageModelNew getAvatar_img() {
        return avatar_img;
    }

    public void setAvatar_img(AvtarImageModelNew avatar_img) {
        this.avatar_img = avatar_img;
    }

    private int follow;

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    private int follower;
    //    private ImageModel image;
    private String image;
    private DeviceModel device;
    private String city;
    private List<SchoolModel> schools_attributes;
    private List<SchoolModel> colleges_attributes;
    private List<SchoolModel> works_attributes;
    private SettingDetailModel setting_detail;

    public SettingDetailModel getSetting_detail() {
        return setting_detail;
    }

    public void setSetting_detail(SettingDetailModel setting_detail) {
        this.setting_detail = setting_detail;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getMute_noti() {
        return mute_noti;
    }

    public void setMute_noti(String mute_noti) {
        this.mute_noti = mute_noti;
    }

    public List<SchoolModel> getSchool() {
        return schools_attributes;
    }

    public void setSchool(List<SchoolModel> school) {
        this.schools_attributes = school;
    }

    public List<SchoolModel> getCollege() {
        return colleges_attributes;
    }

    public void setCollege(List<SchoolModel> college) {
        this.colleges_attributes = college;
    }

    public List<SchoolModel> getWork() {
        return works_attributes;
    }
    public void setWork(List<SchoolModel> work) {
        this.works_attributes = work;
    }


    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getConfirm_new_password() {
        return confirm_new_password;
    }

    public void setConfirm_new_password(String confirm_new_password) {
        this.confirm_new_password = confirm_new_password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtp_gen_time() {
        return otp_gen_time;
    }

    public void setOtp_gen_time(String otp_gen_time) {
        this.otp_gen_time = otp_gen_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getExisting() {
        return existing;
    }

    public void setExisting(String existing) {
        this.existing = existing;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    private String provider;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return TextUtils.isEmpty(name)?"":name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUser_name() {
        return TextUtils.isEmpty(user_name)?"":user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

   /* public ImageModel getImage() {
        return image;
    }*/

/*
    public void setImage(ImageModel image) {
        this.image = image;
    }
*/

    public DeviceModel getDevice() {
        return device;
    }

    public void setDevice(DeviceModel device) {
        this.device = device;
    }


}
