package com.yellowseed.webservices;

import com.google.gson.JsonObject;
import com.mindorks.placeholderview.annotations.Position;
import com.yellowseed.model.DeleteRequestModel;
import com.yellowseed.model.EditBroadCastModel;
import com.yellowseed.model.reqModel.ApproveRequestList;
import com.yellowseed.model.reqModel.ClearRoomRequest;
import com.yellowseed.model.reqModel.DestroyRequestList;
import com.yellowseed.model.reqModel.ViewModel;
import com.yellowseed.model.resModel.ApiNotificationResonse;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.model.resModel.GroupMemberResonse;
import com.yellowseed.model.resModel.PostDetailResponse;
import com.yellowseed.model.resModel.ReportPostResponse;
import com.yellowseed.model.resModel.RequestListResonse;
import com.yellowseed.model.resModel.ShareRequest;
import com.yellowseed.model.resModel.SharedResponse;
import com.yellowseed.model.resModel.StartedMessageListResponse;
import com.yellowseed.model.resModel.StoryDetailResponseModel;
import com.yellowseed.model.resModel.StoryListModel;
import com.yellowseed.model.resModel.TagUserListResponse;
import com.yellowseed.webservices.requests.UnFollowRequest;
import com.yellowseed.webservices.requests.ViewerRequest;
import com.yellowseed.webservices.requests.posts.PostRequest;
import com.yellowseed.webservices.response.BlockUserListResponse;
import com.yellowseed.webservices.response.CommentLikeDislike.CommentLikeDislike;
import com.yellowseed.webservices.response.avatar.AvatarList;
import com.yellowseed.webservices.response.post.PostResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;


public interface ApiService {


    @POST("api/users/login.json")
    Call<ApiResponse> apiLogin(@Body ApiRequest jsonObject); // login api

    @POST("api/socials/social_login.json")
    Call<ApiResponse> apiSocialLogin(@Body ApiRequest jsonObject); // login api

    @POST("api/homes/save_post.json")
    Call<ApiResponse> apiSavePost(@Body JsonObject jsonObject); // save post api

    @POST("api/posts/post_destroy.json")
    Call<ApiResponse> apiDeletePost(@Body JsonObject jsonObject); // delete post api

    @Multipart
    @POST("api/users/sign_up.json")
    Call<ApiResponse> apiUserSignUp(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part... part);

    @POST("api/users/verify_otp.json")
    Call<ApiResponse> apiOTPVerify(@Body ApiRequest jsonObject); // otpVerify api

    @POST("api/users/resend_otp.json")
    Call<ApiResponse> apiResendOTP(@Body ApiRequest jsonObject); // otpVerify api

    @POST("api/users/forget_password.json")
    Call<ApiResponse> apiForgotPwd(@Body ApiRequest jsonObject); // forgot pwd


    @POST("api/static_contents/static_content.json")
    Call<ApiResponse> apiStaticPages(@Body ApiRequest jsonObject); //static content

    @POST("api/users/logout.json")
    Call<ApiResponse> apiLogout(@Body ApiRequest jsonObject); // logout


    @POST("api/stories/story_destroy.json")
    Call<ApiResponse> apiStroryDelete(@Body ApiRequest jsonObject); // logout

    @POST("api/users/change_password.json")
    Call<ApiResponse> apiChangePwd(@Body ApiRequest jsonObject);

    // change pwd
    @POST("api/users/update_profile.json")
    Call<ApiResponse> apiUpdateProfile(@Body ApiRequest jsonObject); // update profile


    @GET("api/users/view_profile.json")
    Call<ApiResponse> apiUserProfile(); // user profile

    @GET("api/commons/suggestion_list.json")
    Call<ApiResponse> apiSuggestionList(); // getSuggestion list

    @Multipart
    @POST("api/users/update_profile_pic.json")
    Call<ApiResponse> apiUpdateProfilePic(@PartMap Map<String, RequestBody> jsonObject); // update user profile pic

    @POST("api/commons/suggestion_list.json")
    Call<ApiResponse> apiFollowerList(@Body ApiRequest jsonObject); // update profile

    @POST("api/posts/report_post.json")
    Call<ReportPostResponse> apiReportUser(@Body JsonObject jsonObject); // report post/user

    @POST("api/relationships.json")
    Call<ApiResponse> apiFollowUser(@Body UnFollowRequest jsonObject); // follow user

    @POST("api/relationships/destroy_request.json")
    Call<ApiResponse> apiUnFollowUser(@Body UnFollowRequest jsonObject); // unfollow user

    @POST("api/relationships/search_relation.json")
    Call<ApiResponse> apiGetFollowerList(@Body JsonObject jsonObject); // unfollow user


    @POST("api/users/user_details.json")
    Call<ApiResponse> apiOtherUserProfile(@Body ApiRequest jsonObject); // other user profile details

    @POST("api/commons/contact_similar")
    Call<ApiResponse> apiPostContacts(@Body ApiRequest jsonObject); // other user profile details

    @POST("api/settings/block_user.json")
    Call<ApiResponse> apiBlockUser(@Body ApiRequest apiRequest); // block user


    @POST("api/settings/unblock_user.json")
    Call<ApiResponse> apiUnblockUser(@Body ApiRequest apiRequest); // unBlock user

    @POST("api/notifications.json")
    Call<ApiResponse> apiPushOnOff(@Body JsonObject apiRequest); // unBlock user


    @POST("api/users/send_otp_mobile.json")
    Call<ApiResponse> apiChangeMobileNum(@Body ApiRequest apiRequest); // change mobile num

    @POST("api/streamings/live_streaming.json")
    Call<ApiResponse> apiGoLive(@Body JsonObject apiRequest); // change mobile num


    @POST("api/users/otp_change_mobile.json")
    Call<ApiResponse> apiOtpVerifyChangeMobileNum(@Body ApiRequest apiRequest); // verify OTP

    @POST("api/posts/users_post.json")
    Call<PostResponse> apiUserPostList(@Body PostRequest apiRequest); // get User Post List

    @POST("api/homes/tagged_post_list.json")
    Call<PostResponse> apiTaggedPostList(@Body PostRequest apiRequest); // get User Post List

    @POST("api/homes/save_post_list.json")
    Call<PostResponse> apiSavedPostList(@Body PostRequest apiRequest); // get User Post List

    @POST("api/homes/pinned_post_list.json")
    Call<PostResponse> apiPinnedList(@Body PostRequest apiRequest); // get User Post List


    @POST("api/posts/post_detail.json")
    Call<PostDetailResponse> postDetailApi(@Body JsonObject jsonObject); // get Post Detail

    /*@Multipart
    @POST("api/posts.json")
    Call<PostResponse> apiCreatePost(@PartMap() Map<String, RequestBody> partMap,@Part MultipartBody.Part... part); // create Post*/


    @POST("api/homes/post_list_api.json")
    Call<com.yellowseed.webservices.response.homepost.PostResponse> apiGetHomePost(@Body PostRequest apiRequest); // get Home Post

//    @POST("api/stories.json")
//    Call<ApiResponse> apiCreateStory(@Body PostModel postModel); // update profile

    @Multipart
    @POST("api/stories.json")
    Call<ApiResponse> apiCreateStory(
            @Part("title") RequestBody title,
            @Part("description") RequestBody discription,
            @PartMap() Map<String, RequestBody> tag_friends_attributes,
            @PartMap() Map<String, RequestBody> poll_attributes,
            @PartMap() Map<String, RequestBody> check_in_attributes,
            @PartMap() Map<String, RequestBody> images_attributes,
            @Part() List<MultipartBody.Part> parts);

    @Multipart
    @POST("api/posts.json")
    Call<ApiResponse> apiCreatePost(
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("check_in") RequestBody check_in,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("share_type") RequestBody share_type,
            @Part("status") RequestBody status,
            @PartMap() Map<String, RequestBody> tag_friends_attributes,
            @PartMap() Map<String, RequestBody> post_images_attributes,
            @PartMap() Map<String, RequestBody> post_tags_attributes,
            @Part() List<MultipartBody.Part> parts);

    @Multipart
    @POST("api/posts/share_post.json")
    Call<ApiResponse> apiSharePost(
            @Part("post_id") RequestBody postId,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("check_in") RequestBody check_in,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("share_type") RequestBody share_type,
            @Part("status") RequestBody status,
            @PartMap() Map<String, RequestBody> tag_friends_attributes,
            @PartMap() Map<String, RequestBody> post_tags_attributes);

    @Multipart
    @POST("api/messages/attach_audio.json")
    Call<ApiResponse> apiAddAudio(@PartMap Map<String, RequestBody> jsonObject);


    @Multipart
    @POST("api/messages/attach_video.json")
    Call<ApiResponse> apiAddVideo(@PartMap Map<String, RequestBody> jsonObject);

    @Multipart
    @POST("api/messages/attach_image.json")
    Call<ApiResponse> apiAddImage(@PartMap Map<String, RequestBody> jsonObject);

    @Multipart
    @POST("api/messages/attach_doc.json")
    Call<ApiResponse> apiAddDocument(@PartMap Map<String, RequestBody> jsonObject);
    /*@PartMap() Map<String, RequestBody> post_tags_attributes,*/

    @Multipart
    @POST("api/posts/post_update.json")
    Call<ApiResponse> apiUpdatePost(
            @Part("post_id") RequestBody postId,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("check_in") RequestBody check_in,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("share_type") RequestBody share_type,
            @Part("status") RequestBody status,
            @PartMap() Map<String, RequestBody> tag_friends_attributes,
            @PartMap() Map<String, RequestBody> post_images_attributes,
            @PartMap() Map<String, RequestBody> post_tags_attributes,
            @Part() List<MultipartBody.Part> parts);


    @Multipart
    @POST("api/commons/add_avtar.json")
    Call<ApiResponse> apiUpdateAvatar(
            @Part("do") RequestBody action,
            @Part("skin") RequestBody skin,
            @Part("hair_color") RequestBody hair_color,
            @Part("hair_style") RequestBody hair_style,
            @Part("beard") RequestBody beard,
            @Part("beard_color") RequestBody beard_color,
            @Part("spacts") RequestBody spacts,
            @Part("hat") RequestBody hat,
            @Part("cloths") RequestBody cloths,
            @Part("status") RequestBody status,
            @Part("lips_color") RequestBody lips_color,
            @Part MultipartBody.Part... part);


    @Multipart
    @POST("api/groups.json")
    Call<ApiResponse> apiCreateRoom(
            @Part("group_name") RequestBody postId,
            @PartMap Map<String, RequestBody> member_ids,
            @Part MultipartBody.Part... part);

    @POST("api/posts/all_comment_user.json")
    Call<ApiResponse> apiAllCommentUser(@Body JsonObject jsonObject);


    @POST("api/posts/liked_create.json")
    Call<ApiResponse> apiLikePost(@Body JsonObject jsonObject);

    @POST("api/homes/pinned_post.json")
    Call<ApiResponse> apiPinnedPost(@Body JsonObject jsonObject);


    @POST("api/posts/liked_create_comment.json")
    Call<CommentLikeDislike> commentLikeDislike(@Body JsonObject jsonObject);

    @POST("api/posts/comment_create.json")
    Call<ApiResponse> apiCommentCreate(@Body JsonObject jsonObject);

    @POST("api/posts/share_post.json")
    Call<SharedResponse> apiSharePost(@Body ShareRequest apiRequest);

    @POST("api/homes/story_list_api.json")
    Call<StoryListModel> apiStoryList(@Body JsonObject jsonObject);

    @POST("api/stories/story_detail.json")
    Call<StoryDetailResponseModel> apiGetStoryDetail(@Body JsonObject jsonObject);// get Story Detail

    @POST("api/stories/viewer_and_poll_list.json")
    Call<StoryDetailResponseModel> apiStoryViewerDetails(@Body ViewerRequest jsonObject);// get Story Detail

    @GET("api/streamings/live_streams.json")
    Call<ApiResponse> apiLiveUsers();// get Story Detail


    @POST("api/stories/poll_by_viewer.json")
    Call<StoryDetailResponseModel> apiPollStory(@Body ViewModel jsonObject);// get Story Detail

    @POST("api/explores/search_users.json")
    Call<ApiResponse> apiSearchUser(@Body JsonObject jsonObject);// get User List


    @POST("api/posts/tag_user_list.json")
    Call<TagUserListResponse> tagUserListApi(@Body JsonObject jsonObject);// get User List

    @POST("api/settings/setting_notification.json")
    Call<ApiResponse> saveSettingApi(@Body JsonObject jsonObject);// get User List


    @POST("api/settings/block_users_list.json")
    Call<BlockUserListResponse> blockUserListApi(@Body JsonObject jsonObject);// get block_users_list

    @POST("api/settings/write_feedback.json")
    Call<ApiResponse> feedbackApi(@Body JsonObject jsonObject);// get block_users_list

    @POST("api/explores/trending_story.json")
    Call<StoryListModel> trendingStoriesApi(@Body JsonObject jsonObject);// get

    @POST("api/explores/trending_post.json")
    Call<com.yellowseed.webservices.response.homepost.PostResponse> trendingPostApi(@Body JsonObject jsonObject);// get

    @POST("api/explores/trending_profile.json")
    Call<StoryListModel> trendingProfileApi(@Body JsonObject jsonObject);// trending profile api

    @POST("api/explores/search_with_tags.json")
    Call<com.yellowseed.webservices.response.homepost.PostResponse> searchWithHashTags(@Body JsonObject jsonObject);// search with tags

    @POST("api/explores/search_users.json")
    Call<ApiResponse> apiGetSearchUserList(@Body JsonObject jsonObject);// search user api

    @POST("api/notifications/call_notification.json")
    Call<ApiResponse> apiCallNotification(@Body PushModel jsonObject);// search user api

    @POST("api/messages/get_chats.json")
    Call<GetChatResonse> apiGetChat(@Body JsonObject jsonObject);// get chat list

    @POST("api/groups/get_broadcast_chat.json")
    Call<GetChatResonse> apiGetBroadCast(@Body JsonObject jsonObject);// get chat list

    @POST("api/messages/get_group_chat.json")
    Call<GetChatResonse> apiGetGroupChat(@Body JsonObject jsonObject);// get chat list

    @POST("api/messages/stared_message.json")
    Call<GetChatResonse> apiStarMessage(@Body JsonObject jsonObject);// get chat list

    @POST("api/messages/message_delete.json")
    Call<GetChatResonse> apiDeleteMessage(@Body DeleteRequestModel jsonObject);// get chat list

    @POST("api/anonymous/ready_for_anonymous.json")
    Call<GetChatResonse> apiAnonymousChat(@Body JsonObject jsonObject);// get chat list

    @POST("api/anonymous/disconnect_anonymous.json")
    Call<GetChatResonse> apiDisconnectAnonymousChat(@Body JsonObject jsonObject);// get chat list

    @GET("api/commons/create_avtar.json")
    Call<AvatarList> apiGetAvtarList();// get chat list


    @POST("api/messages/get_rooms.json")
    Call<GetRoomResonse> apiGetRoom(@Body JsonObject jsonObject);// get room list

    @POST("api/messages/clear_rooms.json")
    Call<GetRoomResonse> apiClearRoom(@Body ClearRoomRequest jsonObject);// clear room api

    @POST("api/groups/broadcast_edit.json")
    Call<ApiResponse> apiEditBroadCast(@Body EditBroadCastModel jsonObject);// clear room api

    @POST("api/messages/mute_room.json")
    Call<GetRoomResonse> apiMuteRoom(@Body JsonObject jsonObject);// mute room api

    @POST("api/groups/group_destroy.json")
    Call<GetRoomResonse> apiLeaveGroup(@Body JsonObject jsonObject);// mute room api

    @POST("api/messages/block_room.json")
    Call<GetRoomResonse> apiBlockChatUser(@Body JsonObject jsonObject);// Block Chat User api

    @POST("api/messages/unblock_room.json")
    Call<GetRoomResonse> apiUnblockChatUser(@Body JsonObject jsonObject);// Block Chat User api

    @POST("api/messages/stared_message_list.json")
    Call<StartedMessageListResponse> apiStaredMessageList(@Body JsonObject jsonObject);// Stared message list

    @POST("api/messages/clear_chat.json")
    Call<GetRoomResonse> apiClearChat(@Body JsonObject jsonObject);// Block Chat User api

    @POST("api/groups/group_member_list.json")
    Call<GroupMemberResonse> apiGroupMemberList(@Body JsonObject jsonObject);// Block Chat User api

    @POST("api/groups/chat_broadcast.json")
    Call<GroupMemberResonse> apiCreateBroadCast(@Body CreateBroadcastRequest jsonObject);// Block Chat User api

    @POST("api/groups/add_member.json")
    Call<ApiResponse> apiRemoveGroupMember(@Body JsonObject jsonObject);// Remove group member api

    @Multipart
    @POST("api/groups/edit_group.json")
    Call<ApiResponse> apiEditGroup(
            @Part("group_id") RequestBody groupId,
            @Part("group_name") RequestBody groupName,
            @Part MultipartBody.Part... part);

    @Multipart
    @POST("api/groups/add_member.json")
    Call<ApiResponse> apiAddGroupMember(
            @Part("group_id") RequestBody groupId,
            @PartMap() Map<String, RequestBody> member_ids);

    @POST("api/notifications/other_notifications_list.json")
    Call<ApiNotificationResonse> apiCallAllNotification(@Body JsonObject jsonObject);// All notification list

    @POST("api/notifications/my_notification_list.json")
    Call<ApiNotificationResonse> apiCallMyNotification(@Body JsonObject jsonObject);// My notification list

    @POST("api/relationships/request_list.json")
    Call<RequestListResonse> apiRequestList(@Body JsonObject jsonObject);// Follow request list

    @POST("api/relationships/approve_request.json")
    Call<RequestListResonse> apiApproveRequest(@Body ApproveRequestList jsonObject);// Approve follow request api

    @POST("api/relationships/destroy_request.json")
    Call<ApiResponse> apiDestroyRequest(@Body DestroyRequestList jsonObject);// Approve follow request api

    @POST("api/groups/chat_broadcast.json")
    Call<ApiResponse> apiCreateBraodcast(@Body CreateBroadcastRequest jsonObject);// Create broadcast

    @POST("api/posts/remove_tag_friend.json")
    Call<ApiResponse> apiRemoveTagFriend(@Body JsonObject jsonObject);// Create broadcast

    @POST("api/posts.json")
    Call<ApiResponse> createPollApi(@Body JsonObject jsonObject);// Create new poll

    @POST("api/posts/polling_post.json")
    Call<ApiResponse> giveVoteToPoll(@Body JsonObject jsonObject);// Create new poll


    @POST("api/posts/hide_post.json")
    Call<ApiResponse> apiHidePost(@Body JsonObject jsonObject);

}
