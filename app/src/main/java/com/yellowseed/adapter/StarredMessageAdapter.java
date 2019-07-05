package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.avatar.AvatarGenerator;
import com.yellowseed.avatar.AvatarPart;
import com.yellowseed.avatar.AvatarSelectionListener;
import com.yellowseed.databinding.AvtarImageLayoutBinding;
import com.yellowseed.databinding.ChatReceiverBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.model.AudioModel;
import com.yellowseed.model.ContactModel;
import com.yellowseed.model.LocationModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.TimeStampFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class StarredMessageAdapter extends RecyclerView.Adapter<StarredMessageAdapter.ViewHolder> {
    private Context context;
    private List<GetChatResonse.UserInfoBean> arlMessages;

    public StarredMessageAdapter(Context context, List<GetChatResonse.UserInfoBean> arlMessages) {
        this.context = context;
        this.arlMessages = arlMessages;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_receiver,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(arlMessages.get(position));
 
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ChatReceiverBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding  = DataBindingUtil.bind(itemView);
        }

        public void bind(GetChatResonse.UserInfoBean chatModel) {
            if ( chatModel.isSelected()) {
                binding.rlRow.setBackgroundColor(context.getResources().getColor(R.color.transparentBlack40));
            } else {
                binding.rlRow.setBackgroundColor(context.getResources().getColor(R.color.creamWhite));

            }
          /*  if ( chatModel.getIs_stared()) {
                binding.ivStared.setVisibility(View.VISIBLE);
            } else {
                binding.ivStared.setVisibility(View.GONE);
            }*/

           /* if ( chatModel.getUpload_type() != null &&  chatModel.getUpload_type().length() > 0) {
                switch ( chatModel.getUpload_type()) {
                    case "audio":
                        binding.text.getRoot().setVisibility(View.GONE);
                        binding.audio.getRoot().setVisibility(View.VISIBLE);
                        binding.video.getRoot().setVisibility(View.GONE);
                        binding.image.getRoot().setVisibility(View.GONE);
                        binding.contact.getRoot().setVisibility(View.GONE);
                        try {
                            Gson gson = new Gson();
                            AudioModel audioModel = gson.fromJson( chatModel.getBody(), AudioModel.class);
                            binding.audio.audioPlayer.setAudioTarget(audioModel.getUrl());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case "video":
                        binding.text.getRoot().setVisibility(View.GONE);
                        binding.audio.getRoot().setVisibility(View.GONE);
                        binding.video.getRoot().setVisibility(View.VISIBLE);
                        binding.image.getRoot().setVisibility(View.GONE);
                        binding.contact.getRoot().setVisibility(View.GONE);
                        binding.image.tvText.setVisibility(View.GONE);
                        Picasso.with(context).load( chatModel.getThumbnail()).into( binding.video.imageView);
                        break;
                    case "location":
                        binding.text.getRoot().setVisibility(View.GONE);
                        binding.audio.getRoot().setVisibility(View.GONE);
                        binding.video.getRoot().setVisibility(View.GONE);
                        binding.image.getRoot().setVisibility(View.VISIBLE);
                        binding.contact.getRoot().setVisibility(View.GONE);
                        binding.image.tvText.setVisibility(View.VISIBLE);
                        Gson gson1 = new Gson();
                        LocationModel locationModel = gson1.fromJson( chatModel.getBody(), LocationModel.class);
                        binding.image.tvText.setText(locationModel.getName());
                        Picasso.with(context).load(CommonUtils.local(String.valueOf(locationModel.getLatitude()),String.valueOf(locationModel.getLongitude()))).into( binding.image.imageView);
                        break;
                    case "contact":
                        binding.text.getRoot().setVisibility(View.GONE);
                        binding.audio.getRoot().setVisibility(View.GONE);
                        binding.video.getRoot().setVisibility(View.GONE);
                        binding.image.getRoot().setVisibility(View.GONE);
                        binding.contact.getRoot().setVisibility(View.VISIBLE);
                        Gson gson2 = new Gson();
                        ContactModel contactModel = gson2.fromJson( chatModel.getBody(), ContactModel.class);
                        binding.contact.tvName.setText(contactModel.getName());
                        binding.contact.tvNumber.setText(contactModel.getNumber());
                        break;
                    case "image":
                        binding.text.getRoot().setVisibility(View.GONE);
                        binding.audio.getRoot().setVisibility(View.GONE);
                        binding.video.getRoot().setVisibility(View.GONE);
                        binding.image.getRoot().setVisibility(View.VISIBLE);
                        binding.contact.getRoot().setVisibility(View.GONE);
                        binding.image.tvText.setVisibility(View.GONE);
                        Picasso.with(context).load( chatModel.getBody()).into( binding.image.imageView);

                        break;
                    case "doc":
                        binding.text.getRoot().setVisibility(View.VISIBLE);
                        binding.audio.getRoot().setVisibility(View.GONE);
                        binding.video.getRoot().setVisibility(View.GONE);
                        binding.image.getRoot().setVisibility(View.GONE);
                        binding.contact.getRoot().setVisibility(View.GONE);
                        binding.text.tvText.setText( chatModel.getBody());
                        break;
                }
            } else {
                binding.text.getRoot().setVisibility(View.VISIBLE);
                binding.audio.getRoot().setVisibility(View.GONE);
                binding.video.getRoot().setVisibility(View.GONE);
                binding.image.getRoot().setVisibility(View.GONE);
                binding.contact.getRoot().setVisibility(View.GONE);
                binding.text.tvText.setText( chatModel.getBody());
            }
            if ( chatModel.getReply_message() != null) {
                binding.reply.getRoot().setVisibility(View.VISIBLE);
                if ( chatModel.getReply_message().getUpload_type() != null &&  chatModel.getReply_message().getUpload_type().length() > 0) {
                    switch ( chatModel.getReply_message().getUpload_type()) {
                        case "audio":
                            binding.reply.tvRepyMessage.setText("Audio");
                            break;
                        case "video":
                            binding.reply.tvRepyMessage.setText("Video");
                            break;
                        case "location":
                            binding.reply.tvRepyMessage.setText("Location");
                            break;
                        case "contact":
                            binding.reply.tvRepyMessage.setText("Contact");
                            break;
                        case "image":
                            binding.reply.tvRepyMessage.setText("Image");
                            break;
                        case "doc":
                            binding.reply.tvRepyMessage.setText("Document");
                            break;
                    }
                } else {
                    binding.reply.tvRepyMessage.setText( chatModel.getReply_message().getBody());
                }
            } else {
                binding.reply.getRoot().setVisibility(View.GONE);
            }
            if ( chatModel.getCreated_at() != null &&  chatModel.getCreated_at().length() > 0) {
                switch ( chatModel.getCreated_at().length()) {
                    case 10:
                        binding.tvChatSenderTime.setText(TimeStampFormatter.getDayFromTS( chatModel.getCreated_at(), "HH:mm"));
                        break;
                    default:
                        String strDate = "Not availbale";
                        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
                        Date date = null;
                        try {
                            date = readDate.parse( chatModel.getCreated_at());
                            long commentDate = date.getTime();
                            long timeElapsed = CommonUtils.timeElapsedFromDate(context,commentDate);
                            if (timeElapsed == commentDate) {
                                SimpleDateFormat writeDate = new SimpleDateFormat("hh:mm a");
                                writeDate.setTimeZone(TimeZone.getDefault());
                                strDate = writeDate.format(date);
                            } else {
                                strDate = "" + String.valueOf(timeElapsed) + CommonUtils.getPreferencesString(context, AppConstants.TIME_ELAPSED_STRING);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        binding.tvChatSenderTime.setText(strDate);
                        break;
                }

            }
            if ( chatModel.getReceiver_image() != null && ! chatModel.getReceiver_image().equalsIgnoreCase("")) {
                Picasso.with(context).load( chatModel.getReceiver_image()).transform(new CircleTransformation()).into( binding.senderImage);
            } else {
                Picasso.with(context).load(R.drawable.user_placeholder).transform(new CircleTransformation()).into( binding.senderImage);
            } */
        }
    }
}
