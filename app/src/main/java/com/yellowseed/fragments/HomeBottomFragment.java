package com.yellowseed.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.activity.CreateNewStoryActivity;
import com.yellowseed.activity.CreatePollActivity;
import com.yellowseed.activity.DirectScreenActivity;
import com.yellowseed.activity.HomeActivity;
import com.yellowseed.activity.LiveUserActivity;
import com.yellowseed.activity.LoginActivity;
import com.yellowseed.activity.MyProfileActivity;
import com.yellowseed.activity.RePostActivity;
import com.yellowseed.activity.SocialPostActivity;
import com.yellowseed.activity.StoriesViewActivity;
import com.yellowseed.activity.StoryViewActivity;
import com.yellowseed.activity.SuggestionsActivity;
import com.yellowseed.adapter.DirectShareAdapter;
import com.yellowseed.adapter.HomeBottomViewAdapter;
import com.yellowseed.adapter.TagFollowingAdapter;
import com.yellowseed.databinding.DialogShareBinding;
import com.yellowseed.databinding.FragmentHomeBottomBinding;
import com.yellowseed.databinding.LayoutRepostBinding;
import com.yellowseed.databinding.SpamdialogBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.imageUtils.CropImage;
import com.yellowseed.imageUtils.TakePictureUtils;
import com.yellowseed.imageeditor.EditImageActivity;
import com.yellowseed.imagezoom.ImageZoomHelper;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.OnItemTouchHelper;
import com.yellowseed.model.MessageEvent;
import com.yellowseed.model.UserListModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.model.resModel.ReportPostResponse;
import com.yellowseed.model.resModel.StoryListModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.ServiceConstant;
import com.yellowseed.webservices.requests.posts.PostRequest;
import com.yellowseed.webservices.response.homepost.Post;
import com.yellowseed.webservices.response.homepost.PostResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;

import static com.yellowseed.imageUtils.TakePictureUtils.CROP_FROM_CAMERA;


public class HomeBottomFragment extends BaseFragment implements View.OnClickListener, OnItemTouchHelper {

    public static final int TAKE_PICTURE = 1;
    public static final int POLL_REQUEST_CODE = 522;
    // We can be in one of these 3 states
    static final int REQUEST_VIDEO_CAPTURE = 1;
    // Remember some things for zooming
    ImageZoomHelper imageZoomHelper;
    int page = 1;

    private HomeBottomViewAdapter adapter;
    private FragmentHomeBottomBinding binding;
    private Context mContext;
    private String image = "", base64_value = "", path = "", lookingFor = "";
    private ImageView ivRight;
    private TagFollowingAdapter tagFollowingAdapter;
    private DirectShareAdapter directShareAdapter;
    private ArrayList<Post> models = new ArrayList<>();
    private ArrayList<GetRoomResonse.RoomsBean> roomList = new ArrayList<>();
    private boolean isLastPage;
    private boolean isLoading;
    private Integer totalRecord;
    private String report;
    private ArrayList<StoryListModel.StoriesBean> storyList = new ArrayList<>();
    private WebSocketClient webSocketClient;
    private GetChatResonse.UserInfoBean chatScreenModel;
    private Themes themes;
    private Boolean darkThemeEnabled;
    private List<String> arlNumber = new ArrayList<>();
    private ArrayList<UserListModel> arlModel = new ArrayList<>();
    private ApiResponse userData;
    private String phoneNo;
    private String id;
    private String name;
    private String email;
    private Dialog progressContactsDialog;
    private LocalBroadcastManager lbm;
    private PopupWindow quickAction;
    private BottomSheetBehavior sheetBehavior;

    private Dialog dialog;
    private int removePosition = -1;
    private String[] username = {"John Thomas", "Jenny Koul", "Mile Keel", "Julie Kite", "Tiny Tim", "Mike Keel", "Kellly Kim"};
    private String[] mutaualFriend = {"2 Mutual Friend", "3 Mutaul Friend", "2 Mutual Friend", "3 Mutaul Friend", "1 Mutual Friend", "4 Mutaul Friend", "2 Mutual Friend"};
    private int img[] = {R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon2, R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon4};

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            String data = intent.getStringExtra("key");
            adapter.notifyDataSetChanged();
            try {
                setHeaderRecylerView();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    };


    public HomeBottomFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        ((HomeActivity) context).mListener = this;
        themes = new Themes(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        models = new ArrayList<>();
        imageZoomHelper = new ImageZoomHelper((Activity) mContext);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);
        models.addAll(getPostData());
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(receiver, new IntentFilter("theme_change"));

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_bottom, container, false);
        View view = binding.getRoot();
        initializedControl();
        setToolBar();
        setOnClickListener();

        return view;

    }

    private void setHeaderRecylerView() throws IOException {


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.rvPost.setLayoutManager(linearLayoutManager);
        /*
         * setting dummy data
         * */
        arlModel.clear();
        for (int i = 0; i < username.length; i++) {
            UserListModel model = new UserListModel();

            model.setName(username[i]);
            model.setChecked(false);
            model.setEmail("xyz@gmail.com");
            model.setId("dmbvdbfvdfbdf");
            model.setIs_follow("0");

            model.setMutual_follower(5);

            arlModel.add(model);

        }

        adapter = new HomeBottomViewAdapter(models, storyList, arlModel, mContext, new OnItemClickListener() {


            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {

                    case R.id.ivPostImage:


                        break;
                    case R.id.flImage:
                        ActivityController.startActivity((Activity) mContext, StoriesViewActivity.class);
                        break;

                    case R.id.ivPostMenuItem:
                        if (!models.get(position).isOpenPopup()) {
                            openMenuPopup(models.get(position), view, position);
                        } else {
                            quickAction.dismiss();
                        }
                        break;

                    case R.id.tvDeletePost:
                        //     deletePostPopup(position, models.get(position).getPost().getId(), models.get(position).getSharedId());
                        break;

                    case R.id.llComment:
//                        ActivityController.startActivity(mContext, CommentsActivity.class);
                        break;

                    case R.id.llShare:
                        if (models.get(position).getPost().getId() != null)
                            setPostShareDialoge(models.get(position));


                        break;

                    case R.id.ivPostDownloads:
                        ToastUtils.showToastShort(mContext, "Post is saved successfully");
                        break;

                    case R.id.tvReportUser:
                        setSpamDialog(position);
                        // addReportDialog(position);
                        break;
                    case R.id.seeAll:
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConstants.IS_FROM, HomeBottomFragment.class.getSimpleName());
                        ActivityController.startActivity((Activity) mContext, SuggestionsActivity.class, bundle);
                        break;

                    case R.id.ivPostProfilePic:
                    case R.id.tvPostUserName:
                    case R.id.tv_PostTime:
                        Intent intent = new Intent(mContext, MyProfileActivity.class);
                        intent.putExtra(AppConstants.USER_ID, models.get(position).getUser().getId());
                        Log.e(AppConstants.USER_ID, models.get(position).getUser().getId());
                        startActivity(intent);
                        break;

                    case R.id.ivHeart:
                        break;

                    case R.id.llGoLive:

                        startActivity(new Intent(mContext, LiveUserActivity.class));

                        break;
                    case R.id.llStory3:

                        addPhotoDialog();

                        break;
                    case R.id.llPost:
                        ActivityController.startActivity(mContext, SocialPostActivity.class);

                        break;
                    case R.id.llPoll:
                        ActivityController.startActivityForResult(getActivity(), CreatePollActivity.class, POLL_REQUEST_CODE);
                        break;


                    case R.id.tvWatchAll:
                      /*  if (models.size() > 0) {
                            Intent intent1 = new Intent(mContext, StoriesViewActivity.class);
                            intent1.putExtra(AppConstants.USER_ID, models.get(0).getUser().getId());
                            intent1.putExtra("all", "all");
                            startActivity(intent1);
                        }*/

                        break;
                    case R.id.etWriteSomething:

                        ActivityController.startActivity(mContext, SocialPostActivity.class);

                        break;

                    default:
                        break;
                }


            }


        });
        binding.rvPost.setAdapter(adapter);

        binding.rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {


          /*  @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();
                if (models != null && models.size() > 0) {
                    if (adapter != null && adapter.getItemCount() - 1 == totalRecord) {
                        isLastPage = true;
                    }

                    if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                        if (!isLoading && !isLastPage) {
                            isLoading = true;
                            callPostListAPI(++page);
                            // Increment the pagecount everytime we scroll to fetch data from the next page
                            // make isLoading = false once the data is loaded
                            // call mAdapter.notifyDataSetChanged() to refresh the Adapter and Layout

                        }

                    }
                }
            }
        });


      *//*  endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int pageNo, int totalItemsCount) {
                callPostListAPI(page);
            }
        };
 *//*
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isLoading = true;
                callStoryListAPI();
                callPostListAPI(page);
            }
        });*/


        });


        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeContainer.setRefreshing(false);
            }
        });


    }


    /**
     * Save Post List Api
     */
    private void callSavePostListApi(final Post post, final String sharedId, final boolean isSaved) {
        final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();

        if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
            jsonObject.addProperty("shared_id", sharedId);
        } else {
            jsonObject.addProperty("post_id", post.getPost().getId());
        }

        if (isSaved) {
            jsonObject.addProperty("status", true);
        } else {
            jsonObject.addProperty("status", false);
        }
        Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiSavePost(jsonObject);
        call.enqueue(new retrofit2.Callback<ApiResponse>() {


            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getResponseCode() == 200) {

                    if (response.body().getResponseMessage() != null && !response.body().getResponseMessage().equalsIgnoreCase("")) {
                        CommonUtils.showToast(mContext, response.body().getResponseMessage());
                        if (isSaved) {
                            post.getPost().setSaved(false);
                        } else {
                            post.getPost().setSaved(true);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                t.printStackTrace();
            }
        });
    }


    private void deletePostPopup(final int position, final String postId, final String sharedId) {
        AlertDialog.Builder builder;
        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {

            builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setTitle(R.string.delete_cnfm);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                removePosition = position;

                models.remove(removePosition);
                adapter.notifyDataSetChanged();
                ToastUtils.showToastShort(mContext, "Post is deleted successfully");
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deletePostApi(final int position, String postId, String sharedId) {
        final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();

        if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
            jsonObject.addProperty("shared_id", sharedId);
        } else {
            jsonObject.addProperty("post_id", postId);
        }

        Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiDeletePost(jsonObject);
        call.enqueue(new retrofit2.Callback<ApiResponse>() {


            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getResponseCode() == 200) {

                    models.remove(position);
                    if (response.body().getResponseMessage() != null && !response.body().getResponseMessage().equalsIgnoreCase("")) {
                        CommonUtils.showToast(mContext, response.body().getResponseMessage());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                t.printStackTrace();
            }
        });
    }

    private void deleteTagPopup(final int position, final String postId) {

        AlertDialog.Builder builder;
        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {

            builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setTitle(R.string.remove_tag);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CommonUtils.isOnline(mContext)) {

                    callDeleteTag(position, postId);
                } else {
                    CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                }
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void callDeleteTag(final int position, String postId) {
        final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tag_id", postId);


        Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiRemoveTagFriend(jsonObject);
        call.enqueue(new retrofit2.Callback<ApiResponse>() {


            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getResponseCode() == 200) {

                    // models.remove(position);
                    if (response.body().getResponseMessage() != null && !response.body().getResponseMessage().equalsIgnoreCase("")) {
                        CommonUtils.showToast(mContext, response.body().getResponseMessage());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                t.printStackTrace();
            }
        });
    }


    private void animateHeart(View view) {
        ImageView ivHeart = (ImageView) view.findViewById(R.id.ivHeart);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        prepareAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        prepareAnimation(alphaAnimation);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(alphaAnimation);
        animation.addAnimation(scaleAnimation);
        animation.setDuration(700);
        animation.setFillAfter(true);

        view.startAnimation(animation);
    }

    private Animation prepareAnimation(Animation animation) {
        animation.setRepeatCount(4);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

    @Override
    public void initializedControl() {

        try {
            setHeaderRecylerView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivRight = (ImageView) getActivity().findViewById(R.id.ivRight);
    }

    @Override
    public void setToolBar() {
        ivRight.setImageResource(R.mipmap.share_post_icon);
    }


    @Override
    public void setOnClickListener() {
        ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivRight:
                ActivityController.startActivity(mContext, DirectScreenActivity.class);
                break;
            case R.id.llHomeFragment:
                CommonUtils.hideSoftKeyboard((Activity) mContext);
                break;
        }
    }

    private void setSpamDialog(final int postPos) {


        final SpamdialogBinding spamdialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.spamdialog, null, false);

        final Dialog dialog = DialogUtils.createDialog(mContext, spamdialogBinding.getRoot());
        spamdialogBinding.llMain.setBackground(ContextCompat.getDrawable(mContext, themes.setDarkSearchDrawable()));
        spamdialogBinding.viewHate.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        spamdialogBinding.viewLineFalse.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        spamdialogBinding.viwLine2.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        spamdialogBinding.viewLineSpam.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        spamdialogBinding.tvCancel.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setCancel()));
        spamdialogBinding.tvhelpUs.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        spamdialogBinding.tvFalseStory.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setSpamText()));
        spamdialogBinding.tvSpam.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setSpamText()));
        spamdialogBinding.tvHate.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setSpamText()));
        spamdialogBinding.tvInappropriate.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setSpamText()));

        spamdialogBinding.tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        spamdialogBinding.tvInappropriate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        spamdialogBinding.tvHate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        spamdialogBinding.tvSpam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        spamdialogBinding.tvFalseStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Setting the recycler view inside the dialog

    }


    private void addReportDialog(final int postPos) {

        final CharSequence[] items = {Html.fromHtml("<font color = '#FC2B2B'>Its Spam</font>"), Html.fromHtml("<font color = '#FC2B2B'>Inappropriate</font>"), Html.fromHtml("<font color = '#FC2B2B'>Its False Story</font>"), Html.fromHtml("<font color = '#FC2B2B'>Hate Speech</font>")};
        new MaterialDialog.Builder(mContext)
                .title(R.string.helpusunderstand).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeStory()))

                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        CharSequence data = items[position];
                        if (String.valueOf(data).equalsIgnoreCase(getString(R.string.itsspam))) {
                            report = getString(R.string.itsspam);
                            dialog.dismiss();
                        } else if (String.valueOf(data).equalsIgnoreCase(getString(R.string.inappropriate))) {
                            report = getString(R.string.inappropriate);
                            dialog.dismiss();
                        } else if (String.valueOf(data).equalsIgnoreCase(getString(R.string.itsfalsestory))) {
                            report = getString(R.string.itsfalsestory);
                            dialog.dismiss();
                        } else if (String.valueOf(data).equalsIgnoreCase(getString(R.string.hatespeech))) {
                            dialog.dismiss();
                            report = getString(R.string.hatespeech);
                        } else {
                            report = "";
                        }
                        /*if (CommonUtils.isOnline(mContext)) {
                            callReportPostApi(report, models.get(postPos).getPost().getId(), models.get(postPos).getSharedId());
                        } else {
                            CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                        }
*/
                    }
                }).show();

    }


    private void callReportPostApi(String report, String postId, String sharedId) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
                jsonObject.addProperty(AppConstants.SHARED_ID, sharedId);
            } else {
                jsonObject.addProperty(AppConstants.POST_ID, postId);
            }
            jsonObject.addProperty(AppConstants.REASON, report);
            Call<ReportPostResponse> call = ApiExecutor.getClient(mContext).apiReportUser(jsonObject);
            call.enqueue(new retrofit2.Callback<ReportPostResponse>() {
                @Override
                public void onResponse(@NonNull Call<ReportPostResponse> call, @NonNull final Response<ReportPostResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ReportPostResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


    /**
     * PostShareDialoge
     *
     * @param post
     */
    private void setPostShareDialoge(final Post post) {


      /*  final CharSequence[] items = {getString(R.string.Repost), getString(R.string.directshare)};
        AlertDialog.Builder builder;
        if (darkThemeEnabled) {

            builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));


            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals(getString(R.string.Repost))) {
                        Intent intent = new Intent(mContext, RePostActivity.class);
                        intent.putExtra(AppConstants.POST_MODEL, post);
                        startActivity(intent);

                    } else if (items[item].equals(getString(R.string.directshare))) {
                        if (CommonUtils.isOnline(mContext)) {
                            callGetRoomAPI(post);
                        } else {
                            CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                        }
                    }

                }

            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();

            builder.create().getWindow().setBackgroundDrawableResource(android.R.color.background_dark);


        } else {
            builder = new AlertDialog.Builder(mContext);

            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals(getString(R.string.Repost))) {
                        Intent intent = new Intent(mContext, RePostActivity.class);
                        intent.putExtra(AppConstants.POST_MODEL, post);
                        startActivity(intent);

                    } else if (items[item].equals(getString(R.string.directshare))) {
                        if (CommonUtils.isOnline(mContext)) {
                            callGetRoomAPI(post);
                        } else {
                            CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                        }
                    }

                }


            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();

            builder.create().getWindow().setBackgroundDrawableResource(android.R.color.background_dark);


        }*/


        DialogShareBinding dialogShareBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_share, null, false);
        dialog = DialogUtils.createDialog(mContext, dialogShareBinding.getRoot());
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogShareBinding.llMain.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkStoryBackground()));
        dialogShareBinding.tvRepost.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        dialogShareBinding.tvDirectShare.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        dialogShareBinding.tvRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RePostActivity.class);
                intent.putExtra(AppConstants.POST_MODEL, post);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialogShareBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialogShareBinding.tvDirectShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  CommonUtils.setFragment(new ListFragment(), true, R.id.llContainer, "other profile list");

                DirectShareFragment directShareFragment =
                        new DirectShareFragment();
                directShareFragment.show(getChildFragmentManager(),
                        "directShare");
                //   ActivityController.startActivity(mContext,BottomSheetActivity.class);
                //setPostShareDialogeRecyclerView(post);

                /*if (CommonUtils.isOnline(mContext)) {
                     callGetRoomAPI(post);
                } else {
                    CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                }*/
                dialog.dismiss();

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);


    }


    private void setPostShareDialogeRecyclerView(final Post post) {


        LayoutRepostBinding repostBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_repost, null, false);

         Dialog dialog = DialogUtils.createDialog(mContext, repostBinding.getRoot());
      //  sheetBehavior = BottomSheetBehavior.from(LayoutRepostBinding.botomSheet);


       // sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        repostBinding.llRepost.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkThemeDialog()));
        repostBinding.etRepostSearch.setBackground(ContextCompat.getDrawable(mContext, themes.setDarkSearchDrawable()));
        repostBinding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        repostBinding.etRepostSearch.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkGreyStoryTextColor()));
        repostBinding.etRepostSearch.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        repostBinding.tvPrivateAccount.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        repostBinding.tvFollower.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setLightThemeText()));

        repostBinding.etCaption.setBackground(ContextCompat.getDrawable(mContext, themes.setDarkSearchDrawable()));
        repostBinding.etCaption.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
       // Themes.getInstance(mContext).changeCrossIconColor(mContext, repostBinding.ivCancelRepost);

        if (darkThemeEnabled) {
            repostBinding.ivBlockUser.setImageResource(R.mipmap.private_lock);

        } else {
            repostBinding.ivBlockUser.setImageResource(R.mipmap.private_lock_black);


        }
        /*repostBinding.ivCancelRepost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
        repostBinding.ivSendRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Setting the recycler view inside the dialog

        repostBinding.rvRepostHome.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        repostBinding.rvRepostHome.setLayoutManager(linearLayoutManager);
        tagFollowingAdapter = new TagFollowingAdapter(mContext, roomList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
              /*  if (roomList.get(position).isIs_selected()) {
                    roomList.get(position).setIs_selected(false);
                } else {
                    roomList.get(position).setIs_selected(true);
                }*/
                tagFollowingAdapter.notifyItemChanged(position);
            }
        });
        repostBinding.rvRepostHome.setAdapter(tagFollowingAdapter);

        repostBinding.ivSendRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(mContext, "Post has been shared.");

                dialog.dismiss();
            /*    if (tagFollowingAdapter.getSelected().size() > 0) {
                    chatScreenModel = new GetChatResonse.UserInfoBean();
                    chatScreenModel.setCaption(repostBinding.etCaption.getText().toString().trim());
                    chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setCreated_timestamp(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setId("");
                    chatScreenModel.setIs_user_sender(true);
                    chatScreenModel.setIs_stared(false);
                    chatScreenModel.setLocal_message_id(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setRead_status(false);
                    chatScreenModel.setReceiver_image("");
                    PostChatBody postChatBody = new PostChatBody();
                    postChatBody.setPost_id(post.getPost().getId());
                    postChatBody.setUser_name(post.getUser().getName());
                    postChatBody.setUser_image(post.getUser().getImage());
                    postChatBody.setDescription(post.getPost().getDescription());
                    if (post.getImages() != null && post.getImages().size() > 0) {
                        postChatBody.setImage(post.getImages().get(0).getFile().getUrl());
                    }
                    chatScreenModel.setBody(new Gson().toJson(postChatBody));
                    chatScreenModel.setBody(new Gson().toJson(postChatBody));
                    chatScreenModel.setSender_id(new PrefManager(mContext).getUserId());
                    chatScreenModel.setUpload_type("post");
                    sendMessage(chatScreenModel, tagFollowingAdapter.getSelected());
                    repostBinding.etCaption.setText("");
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }*/

            }
        });
    }


    /*
     * dialog for taking picture
     */
    private void addPhotoDialog() {

        AlertDialog.Builder builder;
        if (darkThemeEnabled) {

            builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        final CharSequence[] items = {getString(R.string.take_a_photo), getString(R.string.from_gallery)};
        if (darkThemeEnabled)
            new MaterialDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogCustom))
                    .title(R.string.choose_photo).titleGravity(GravityEnum.CENTER)
                    .items(items)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                            if (items[position].equals(getString(R.string.take_a_photo))) {
                                image = System.currentTimeMillis() + "_photo.png";
                                takePicture((Activity) mContext, image);

                            } else if (items[position].equals(getString(R.string.from_gallery))) {
                                image = System.currentTimeMillis() + "_photo.png";

                                openGallery();
                            } else {
                                dialog.dismiss();
                            }


                        }
                    }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            }).show();
        else {
            new MaterialDialog.Builder(mContext)
                    .title(R.string.choose_photo).titleGravity(GravityEnum.CENTER)
                    .items(items)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                            if (items[position].equals(getString(R.string.take_a_photo))) {
                                image = System.currentTimeMillis() + "_photo.png";
                                takePicture((Activity) mContext, image);

                            } else if (items[position].equals(getString(R.string.from_gallery))) {
                                image = System.currentTimeMillis() + "_photo.png";

                                openGallery();
                            } else {
                                dialog.dismiss();
                            }


                        }
                    }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    /*
    This method is being used for taking picture from gallery
    */
    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, TakePictureUtils.PICK_GALLERY);
    }

    /*
     This method is being used for taking picture from camera
     */


    public void takePicture(Activity context, String fileName) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri;
            mImageCaptureUri = Uri.fromFile(new File(context.getExternalFilesDir("temp"), fileName + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, TAKE_PICTURE);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == TakePictureUtils.PICK_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream inputStream = mContext.getContentResolver().openInputStream(intent.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(this.mContext.getExternalFilesDir("temp"), image + ".jpg"));
                    TakePictureUtils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage(getActivity(), image + ".jpg");
                } catch (Exception e) {
                    //CommonUtils.showToast(mContext, getString(R.string.error_in_picture));

                }
            }
        } else if (requestCode == TakePictureUtils.TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                startCropImage(getActivity(), image + ".jpg");
            }
        } else if (requestCode == CROP_FROM_CAMERA) {
            //  imageName="picture";
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    path = intent.getStringExtra(CropImage.IMAGE_PATH);
                    File imageFile = new File(path);
                    /*if (imageFile.exists()) {
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), imageFile);
                    }*/
                }
                if (path == null) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeFile(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                base64_value = "data:image/jpeg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT).trim();
                base64_value = base64_value.replace("\n", "");

                /*Picasso.with(mContext).load(new File(path)).placeholder(R.mipmap.profile).
                        transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivLoginProfile);*/
                if (path != null && !path.isEmpty())
                    startActivity(new Intent(mContext, CreateNewStoryActivity.class)
                            .putExtra(AppConstants.IMAGE_PATH, path));

            }
        } else if (requestCode == POLL_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {


            if (intent != null) {

                if (intent.getBooleanExtra(AppConstants.IS_POLL_ADD, false)) {
                    //   callPostListAPI(1);

                }

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //addPhotoDialog();
                } else {
                    CommonUtils.showToast(mContext, "Permission denial");
                }
                break;


            case CheckPermission.PERMISSIONS_REQUEST_READ_CONTACTS:
                try {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        new GetContactAsync().execute();

                    } else {
                        ToastUtils.showToastShort(mContext, getString(R.string.grant_contact_permission));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }


    /**
     * this method is used for open crop image
     */
    public void startCropImage(Activity context, String fileName) {
        Intent intent = new Intent(context, EditImageActivity.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp"), fileName).getPath());
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    @Override
    public boolean onItemTouch(MotionEvent ev) {
        return imageZoomHelper.onDispatchTouchEvent(ev);
    }


    private void callPostListAPI(final int pageNo) {
        if (CommonUtils.isOnline(mContext)) {
            if (pageNo > 1)
                binding.loadMore.setVisibility(View.VISIBLE);
//            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
//            progressDialog.show();
            PostRequest postRequest = new PostRequest();
            postRequest.setPage(pageNo);
            postRequest.setId(PrefManager.getInstance(mContext).getUserId());

            Call<PostResponse> call = ApiExecutor.getClient(mContext).apiGetHomePost(postRequest);
            call.enqueue(new retrofit2.Callback<PostResponse>() {
                @Override
                public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                  /*if(progressDialog!=null && progressDialog.isShowing()){
                      progressDialog.dismiss();
                  }*/
                    binding.loadMore.setVisibility(View.GONE);
                    isLoading = false;
                    try {

                        if (response != null && response.body() != null) {
                            if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                                if (pageNo == 1) {
                                    models.clear();
                                    binding.swipeContainer.setRefreshing(false);
                                }
                                if (response.body().getPost().size() > 0) {
                                    models.addAll(response.body().getPost());
                                    /*List<Post> temp = new ArrayList<>();
                                    if (response.body().getPost() != null && response.body().getPost().size() > 0) {

                                        temp.add(response.body().getPost().get(0));
                                        models.addAll(temp);
                                    }*/
                                    adapter.notifyDataSetChanged();
                                }
                                totalRecord = response.body().getPagination().getTotalRecords();
                            } else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED) {

                            } else {
                                ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                            }
                        } else {
                            ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                    binding.loadMore.setVisibility(View.GONE);
                    if (pageNo == 1) {
                        binding.swipeContainer.setRefreshing(false);
                    }
                    isLoading = false;
//                    progressDialog.dismiss();
                    t.printStackTrace();
                    //   ToastUtils.showToastShort(mContext,getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


    /**
     * method for call story list api
     */
    private void callStoryListAPI() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("page", 1);

            Call<StoryListModel> call = ApiExecutor.getClient(mContext).apiStoryList(jsonObject);
            call.enqueue(new retrofit2.Callback<StoryListModel>() {
                @Override
                public void onResponse(@NonNull Call<StoryListModel> call, @NonNull final Response<StoryListModel> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            storyList.clear();
                            storyList.addAll(response.body().getStories());
                            adapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StoryListModel> call, @NonNull Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    private void facebookSharing(Post post) {
        if (CommonUtils.isOnline(mContext)) {
            if (post.getImages() != null && post.getImages().size() > 0 && post.getImages().get(0).getFile().getUrl() != null) {
                CallbackManager callbackManager = CallbackManager.Factory.create();
                ShareDialog shareDialog = new ShareDialog((Activity) mContext);
                String imagePath = null;
                if (post.getImages() != null && post.getImages().size() > 0) {

                    imagePath = post.getImages().get(0).getFile().getUrl();
                }

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setQuote("New Post")
                            .setContentUrl(Uri.parse(imagePath))
                            .build();
                    shareDialog.show(linkContent, ShareDialog.Mode.FEED);
                }
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        CommonUtils.showToast(mContext, "Posted successfully.");
                      /*  if (CommonUtils.isOnline(mContext)) {
                        } else {
                            CommonUtils.showToast(mContext, mContext.getString(R.string.internet_alert_msg));
                        }*/
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

            } else {
                CommonUtils.showToast(mContext, "You can only share image via facebook.");
            }

        } else {
            CommonUtils.showToast(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


    private void openMenuPopup(final Post post, View targetView, final int position) {


        BubbleLayout customLayout = (BubbleLayout) LayoutInflater.from(mContext).inflate(R.layout.popup_menu, null);
        customLayout.setBubbleColor(ContextCompat.getColor(mContext, themes.setPopupBackground()));
        quickAction = BubblePopupHelper.create(mContext, customLayout);
        LinearLayout otherProfile = (LinearLayout) customLayout.findViewById(R.id.llOtherProfile);
        LinearLayout llMyProfile = (LinearLayout) customLayout.findViewById(R.id.llMyProfile);
        llMyProfile.setBackgroundColor(ContextCompat.getColor(mContext, themes.setPopupBackground()));
        otherProfile.setBackgroundColor(ContextCompat.getColor(mContext, themes.setPopupBackground()));


        if (new PrefManager(mContext).getUserId().equalsIgnoreCase(post.getUser().getId())) {
            llMyProfile.setVisibility(View.VISIBLE);
            otherProfile.setVisibility(View.GONE);
        } else {
            llMyProfile.setVisibility(View.GONE);
            otherProfile.setVisibility(View.VISIBLE);

        }
        TextView tvSendToChat = (TextView) customLayout.findViewById(R.id.tvSendToChat);
        TextView tvSendToChat1 = (TextView) customLayout.findViewById(R.id.tvSendToChat1);
        TextView tvShareFb1 = (TextView) customLayout.findViewById(R.id.tvShareFb1);
        TextView tvShareFb = (TextView) customLayout.findViewById(R.id.tvShareFb);
        TextView tvDeletePost = (TextView) customLayout.findViewById(R.id.tvDeletePost);
        TextView tvEditPost = (TextView) customLayout.findViewById(R.id.tvEditPost);
        TextView tvReportUser = (TextView) customLayout.findViewById(R.id.tvReportUser);
        TextView tvDeleteTag = (TextView) customLayout.findViewById(R.id.tvDeleteTag);
        View viewReportUser = (View) customLayout.findViewById(R.id.viewReportUser);
        ImageView ivEdit = customLayout.findViewById(R.id.ivEdit);
        ImageView ivShare = customLayout.findViewById(R.id.ivShare);
        ImageView ivSend2 = customLayout.findViewById(R.id.ivSend2);
        ImageView ivShare1 = customLayout.findViewById(R.id.ivShare1);
        ImageView ivSend = customLayout.findViewById(R.id.ivSend);

        ImageView ivHide = (ImageView) customLayout.findViewById(R.id.ivHide);
        ImageView ivHide1 = (ImageView) customLayout.findViewById(R.id.ivHide1);

        ImageView ivPin = (ImageView) customLayout.findViewById(R.id.ivPin);
        ImageView ivEdit1 = (ImageView) customLayout.findViewById(R.id.ivEdit1);
        ImageView ivRemove = (ImageView) customLayout.findViewById(R.id.ivRemove);
        TextView tvHide = (TextView) customLayout.findViewById(R.id.tvHide);
        TextView tvHide1 = (TextView) customLayout.findViewById(R.id.tvHide1);
        TextView tvEdit2 = (TextView) customLayout.findViewById(R.id.tvEditPost1);
        TextView tvPin = (TextView) customLayout.findViewById(R.id.tvPin);
        TextView tvRemove = (TextView) customLayout.findViewById(R.id.tvRemove);

        tvShareFb.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvSendToChat.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvEditPost.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvShareFb1.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvSendToChat1.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvHide.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvHide1.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvPin.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvEditPost.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvEdit2.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvRemove.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        Themes.getInstance(mContext).changeRightIcon(mContext, ivEdit);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivShare);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivSend2);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivShare1);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivSend);
        Themes.getInstance(mContext).changeShareIcon(mContext, ivHide);
        Themes.getInstance(mContext).changeShareIcon(mContext, ivHide1);
        Themes.getInstance(mContext).changeShareIcon(mContext, ivRemove);


        tvShareFb.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvSendToChat.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvEditPost.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvShareFb1.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvSendToChat1.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        themes.changeRightIcon(mContext, ivEdit);
        themes.changeIconColorToWhite(mContext, ivShare);
        themes.changeIconColorToWhite(mContext, ivSend2);
        themes.changeIconColorToWhite(mContext, ivShare1);
        themes.changeIconColorToWhite(mContext, ivSend);
        themes.changeIconColorToWhite(mContext, ivEdit1);
        themes.changePinColor(mContext, ivPin);


        if (models.get(position).getTagFriends() != null && models.get(position).getTagFriends().size() > 0) {
            tvDeleteTag.setVisibility(View.VISIBLE);
            viewReportUser.setVisibility(View.VISIBLE);
        } else {
            tvDeleteTag.setVisibility(View.GONE);
            viewReportUser.setVisibility(View.GONE);

        }
        tvDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePostPopup(position, models.get(position).getPost().getId(), models.get(position).getSharedId());
                quickAction.dismiss();

            }

        });
        tvShareFb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookSharing(post);
                quickAction.dismiss();
            }
        }); tvPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(mContext,"Post is pinned successfully");
                quickAction.dismiss();
            }
        });
        tvShareFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookSharing(post);
                quickAction.dismiss();
            }
        });
        tvSendToChat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(mContext, "Work in progress.");
               /* if (CommonUtils.isOnline(mContext)) {
                    callGetRoomAPI(post);
                } else {
                    CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                }*/
                quickAction.dismiss();
            }
        });

        tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(mContext, "Work in progress.");
               /* if (CommonUtils.isOnline(mContext)) {
                    callGetRoomAPI(post);
                } else {
                    CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                }*/
                quickAction.dismiss();
            }
        });
        tvDeleteTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removePosition = position;

                models.remove(removePosition);
                adapter.notifyDataSetChanged();
                //   deleteTagPopup(position, models.get(position).getTagFriends().get(position).getId());
                quickAction.dismiss();
            }
        });
        tvSendToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(mContext, "Work in progress.");

               /* if (CommonUtils.isOnline(mContext)) {
                    callGetRoomAPI(post);
                } else {
                    CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                }*/
                quickAction.dismiss();
            }
        });
        tvHide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(mContext, "Work in progress");
              /*  removePosition = position;

                models.remove(removePosition);
                adapter.notifyDataSetChanged();*/
               /* if (CommonUtils.isOnline(mContext)) {


                    removePosition = position;

                    models.remove(removePosition);
                    adapter.notifyItemRemoved(removePosition);
                  //  callHidePost(models.get(position).getPost().getId());


                } else {
                    CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                }*/
                quickAction.dismiss();
            }
        });
        tvHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(mContext, "Work in progress");
              /*  removePosition = position;

                models.remove(removePosition);
                adapter.notifyDataSetChanged();*/
               /* if (CommonUtils.isOnline(mContext)) {


                    removePosition = position;

                    models.remove(removePosition);
                    adapter.notifyItemRemoved(removePosition);
                  //  callHidePost(models.get(position).getPost().getId());


                } else {
                    CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                }*/
                quickAction.dismiss();
            }
        });
        tvReportUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSpamDialog(position);
                // addReportDialog(position);
                quickAction.dismiss();
            }
        });
        tvEdit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickAction.dismiss();;
                ToastUtils.showToastShort(mContext,"Work in progress");
            }
        });
        tvEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.dismiss();

                startActivity(new Intent(mContext, SocialPostActivity.class)
                        .putExtra(AppConstants.FROM, AppConstants.FROM_EDIT_POST)
                        .putExtra(AppConstants.POSITION, position)
                        .putExtra(AppConstants.POST_MODEL, models.get(position)));

            }
        });
        quickAction.showAsDropDown(targetView);


    }

    private void callHidePost(String postId) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("post_id", postId);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiHidePost(jsonObject);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {

                        if (response.body().getResponseCode() == 200) {

                            models.remove(removePosition);
                            adapter.notifyItemRemoved(removePosition);


                        }
                        ToastUtils.showToastShort(mContext, response.message());


                    } else {

                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));

                }
            });

        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    /**
     * method for create story api
     */
    private void callGetRoomAPI(final Post post) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();

            Call<GetRoomResonse> call = ApiExecutor.getClient(mContext).apiGetRoom(jsonObject);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            roomList.clear();
                            if (response.body().getRooms() != null && response.body().getRooms().size() > 0) {
                                roomList.addAll(response.body().getRooms());
                            }
                            adapter.notifyDataSetChanged();

                            //totalPage = response.body().getTotal_pages();
                            setPostShareDialogeRecyclerView(post);
                 /*           PostShareBottomSheetFragment postShareBottomSheetFragment =
                                    new PostShareBottomSheetFragment();
                            postShareBottomSheetFragment.show(getChildFragmentManager(),
                                    "add_photo_dialog_fragment");*/
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetRoomResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


    private void createWebSocketClient() {
        URI uri;

        try {
            uri = new URI(ServiceConstant.WEB_SOCKET_URL + "?id=" + new PrefManager(mContext).getUserId());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                System.out.println("onOpen");
            }

            @Override
            public void onTextReceived(final String message) {

            }

            @Override
            public void onBinaryReceived(byte[] data) {
                // isSocketConneted = true;
                System.out.println("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                //isSocketConneted = true;
                System.out.println("onPingReceived");
            }

            @Override
            public void onPongReceived(byte[] data) {
                // isSocketConneted = true;
                System.out.println("onPongReceived");
            }

            @Override
            public void onException(Exception e) {
                //  isSocketConneted = false;
                System.out.println("onException");
                e.printStackTrace();
            }

            @Override
            public void onCloseReceived() {
                // isSocketConneted = false;
                System.out.println("onCloseReceived");
            }
        };

        webSocketClient.setConnectTimeout(10000);
        // webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void sendMessage(GetChatResonse.UserInfoBean chatScreenModel, List<String> roomIds) {
        JsonObject object = new JsonObject();
        object.addProperty("local_message_id", "");
        object.addProperty("sender_id", new PrefManager(mContext).getUserId());
        object.addProperty("room_id", chatScreenModel.getRoom_id());
        object.addProperty("is_group", "");
        object.addProperty("body", chatScreenModel.getBody());
        object.addProperty("is_group", false);
        object.addProperty("created_at", chatScreenModel.getCreated_at());
        object.addProperty("upload_type", chatScreenModel.getUpload_type());
        object.addProperty("type", "Joined");
        object.addProperty("message_type", "broadcast");
        object.addProperty("is_broadcast", true);
        object.add("room_ids", new Gson().toJsonTree(roomIds).getAsJsonArray());
        if (webSocketClient != null)
            webSocketClient.send(new Gson().toJson(object).toString());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        if (event != null && event.getMessage().equalsIgnoreCase("Delete Strory")) {
            //
            //     callStoryListAPI();
        } else if (event != null && event.getMessage().equalsIgnoreCase("vote")) {
            //   callPostListAPI(1);
        }

    }

    public void callpostContctApi() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setMobile(arlNumber);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiPostContacts(apiRequest);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            if (response.body().getContacts() != null) {

                                arlModel.clear();
                                arlModel.addAll(response.body().getContacts());
                                adapter.notifyDataSetChanged();
                            }
                        } else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED) {
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            CommonUtils.clearPrefData(mContext);
                            ((Activity) mContext).finishAffinity();

                        } else {
                            ToastUtils.showToastShort(mContext, "" + response.body().getResponseMessage());
                        }
                    } else {
                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    @Override
    public void onDestroy() {
        lbm.unregisterReceiver(receiver);
        super.onDestroy();
    }

    private List<Post> getPostData() {

        // builds country data from json
        InputStream is = getResources().openRawResource(R.raw.post_data);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        PostResponse data = new Gson().fromJson(jsonString, PostResponse.class);

        return data.getPost();
    }

    public class GetContactAsync extends AsyncTask<List<String>, Void, List<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressContactsDialog = DialogUtils.customProgressDialog(mContext);
            progressContactsDialog.show();

        }

        @Override
        protected List<String> doInBackground(List<String>[] lists) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mContext.checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, CheckPermission.PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                ContentResolver cr = mContext.getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

                if ((cur != null ? cur.getCount() : 0) > 0) {
                    while (cur != null && cur.moveToNext()) {
                        id = cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts._ID));
                        name = cur.getString(cur.getColumnIndex(

                                ContactsContract.Contacts.DISPLAY_NAME));

                        if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                phoneNo = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));

                                Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                        new String[]{id}, null);
                                email = "abc@gmail.com";
                                if (emailCur != null) {
                                    while (emailCur.moveToNext()) {
                                        email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

                                    }
                                    emailCur.close();
                                }

                                phoneNo = phoneNo.replace(" ", "");
                                phoneNo = phoneNo.replace("(", "");
                                phoneNo = phoneNo.replace(")", "");

                                if (CommonUtils.isValidPhone(phoneNo)) {

                                    arlNumber.add(phoneNo);


                                }

                                Log.i(TAG, "Name: " + name);
                                Log.i(TAG, "Phone Number: " + phoneNo);
                                Log.i(TAG, "email: " + email);
                            }
                            pCur.close();
                        }
                    }
                }
                if (cur != null) {
                    cur.close();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<String> s) {
            super.onPostExecute(s);
            progressContactsDialog.dismiss();
            // callpostContctApi();
        }
    }


}
