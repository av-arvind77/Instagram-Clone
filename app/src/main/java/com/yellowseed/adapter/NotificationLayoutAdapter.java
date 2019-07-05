package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.activity.ShowPostItemsActivity;
import com.yellowseed.databinding.LayoutNotificationsBinding;
import com.yellowseed.fragments.FragmentHomeBottomView.notificationFragment.AllNotificationFragment;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.model.NotificationLayoutModel;
import com.yellowseed.model.resModel.ApiNotificationResonse;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.RecyclerItemClickListener;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

public class NotificationLayoutAdapter extends RecyclerView.Adapter<NotificationLayoutAdapter.MyViewHolder> {
  //  private List<ApiNotificationResonse.NotificationsBean> notificationsBeanList;
    private Context context;
 //   private ApiNotificationResonse.NotificationsBean model;
    private List<NotificationLayoutModel> models;

    private String from;
    private RecyclerItemClickListener.OnItemClickListener listener;
    private Themes themes;
    private Boolean darkThemeEnabled;


    public NotificationLayoutAdapter(ArrayList<NotificationLayoutModel> models, Context context) {
        this.models = models;
        this.context = context;
        this.from = from;
        themes =new Themes(context);
        this.listener = listener;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notifications, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.binding.llNotificationItem.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvNotificationData.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvTime.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkEditProfileHint()));
     //   holder.binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));

        /*if (model.getAction_user().getImage() != null && !model.getAction_user().getImage().equalsIgnoreCase("")) {
            Picasso.with(context).load(model.getAction_user().getImage()).transform(new CircleTransformation()).into(holder.binding.ivNotificationUserImage);
        } else {*/
          // Picasso.with(context).load(R.drawable.user_placeholder).transform(new CircleTransformation()).into(holder.binding.ivNotificationUserImage);
           /* CommonUtils.otherUserProfile(context,holder.binding.ivNotificationUserImage,models.getAction_user().getImage(),holder.binding.tvUserImage,
                    model.getAction_user().getName());*/
        holder.binding.ivNotificationUserImage.setImageResource(models.get(position).getNotificationUserImage());
        holder.binding.ivNotificationPostImage.setImageResource(models.get(position).getNotificationUserPostImage());
        holder.binding.tvNotificationData.setText(Html.fromHtml(models.get(position).getNotificationData()));
        holder.binding.tvTime.setText(models.get(position).getNotificationTime());

        holder.binding.llNotificationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.startActivity(context, ShowPostItemsActivity.class);
            }
        });

       /* if (from.equalsIgnoreCase(AllNotificationFragment.class.getSimpleName())) {

//        holder.binding.ivNotificationPostImage.setImageResource(model.getNotificationUserPostImage());
            Spanned noti = (Html.fromHtml(model.getContent()));
            holder.binding.tvNotificationData.setText(noti);
            holder.binding.tvNotificationData.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));


        } else {

            Spanned noti = (Html.fromHtml(model.getTitle()));
            holder.binding.tvNotificationData.setText(noti);
            holder.binding.tvNotificationData.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        }

        holder.binding.llNotificationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, holder.getAdapterPosition());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LayoutNotificationsBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
