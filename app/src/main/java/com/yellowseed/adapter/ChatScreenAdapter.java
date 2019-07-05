package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.databinding.ChatReceiverBinding;
import com.yellowseed.databinding.ChatSenderBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.OnItemLongClickListener;
import com.yellowseed.model.ChatScreenModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

public class ChatScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static boolean isCheck = false;
    Themes themes;
    private Context context;
    private ArrayList<ChatScreenModel> chatScreenModels;
    private OnItemLongClickListener listener;
    private boolean darkThemeEnabled;
    private int layout = -1;


    /*   public ChatScreenAdapter(Activity context, ArrayList<ListObject> models, OnItemClickListener listener) {
        this.context = context;
        this.chatScreenModels = models;
        themes = new Themes(context);
        this.listener = listener;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
    }
*/
    public ChatScreenAdapter(Context context, ArrayList<ChatScreenModel> chatScreenModels, OnItemLongClickListener listener) {
        this.context = context;
        this.chatScreenModels = chatScreenModels;
        themes = new Themes(context);
        this.listener = listener;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
    }

  /*  public void setDataChange(ArrayList<ListObject> asList) {
        this.chatScreenModels = asList;
        notifyDataSetChanged();
    }*/


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                layout = R.layout.chat_sender;

                View view = LayoutInflater.from(context).inflate(layout, parent, false);

                return new MyViewHolder(view);

            case 1:
                layout = R.layout.chat_receiver;
                View v = LayoutInflater.from(context).inflate(layout, parent, false);

                return new MyViewHolder1(v);

            default:
                break;
        }

        return null;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        switch (holder.getItemViewType()) {
            case 0:
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                myViewHolder.chatSenderBinding.tvChatSender.setText(chatScreenModels.get(position).getMessages());


/*

                if (chatScreenModels.get(position).isSelected()) {
                    myViewHolder.chatSenderBinding.chec kBox.setVisibility(View.VISIBLE);
                } else {
                    myViewHolder.chatSenderBinding.checkBox.setVisibility(View.GONE);
                }
*/
/*
                myViewHolder.chatSenderBinding.llSender.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        listener.onLongClick(myViewHolder.chatSenderBinding.llSender, holder.getAdapterPosition());
                        return chatScreenModels.get(position).isSelected();
                    }
                });
                myViewHolder.chatSenderBinding.llSender.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        listener.onItemClick(myViewHolder.chatSenderBinding.llSender, holder.getAdapterPosition());
                    }
                });*/


                /*if (ChatsScreenFrgActivity.isCheck) {
                    myViewHolder.chatSenderBinding.ch.setVisibility(View.VISIBLE);
                    myViewHolder.chatSenderBinding.llRowSender.setEnabled(false);
                } else {
                    myViewHolder.chatSenderBinding.llRowSender.setEnabled(true);
                    myViewHolder.chatSenderBinding.ch.setVisibility(View.GONE);
                    myViewHolder.chatSenderBinding.llRowSender.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            listener.onItemClick(myViewHolder.chatSenderBinding.llRowSender, holder.getAdapterPosition());
                        }
                    });*/


                break;
            case 1:
                MyViewHolder1 myViewHolder1 = (MyViewHolder1) holder;
                myViewHolder1.chatRecieverBinding.tvChatReciver.setText(chatScreenModels.get(position).getMessages());




                /*if (chatScreenModels.get(position).isSelected()) {
                    myViewHolder1.chatRecieverBinding.checkBoxReciver.setVisibility(View.VISIBLE);

                } else {
                    myViewHolder1.chatRecieverBinding.checkBoxReciver.setVisibility(View.GONE);
                }*/
                /*myViewHolder1.chatRecieverBinding.llReciver.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        listener.onLongClick(myViewHolder1.chatRecieverBinding.llReciver, holder.getAdapterPosition());
                        return true;
                    }
                });
                myViewHolder1.chatRecieverBinding.llReciver.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        listener.onItemClick(myViewHolder1.chatRecieverBinding.llReciver, holder.getAdapterPosition());
                    }
                });
*/
                /*if (ChatsScreenFrgActivity.isCheck) {
                    myViewHolder1.chatRecieverBinding.ch.setVisibility(View.VISIBLE);
                    myViewHolder1.chatRecieverBinding.rlRow.setEnabled(false);
                } else {
                    myViewHolder1.chatRecieverBinding.rlRow.setEnabled(true);
                    myViewHolder1.chatRecieverBinding.checkBoxChatsFraEdit.setVisibility(View.GONE);
                    myViewHolder1.chatRecieverBinding.rlRow.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            listener.onItemClick(myViewHolder1.chatRecieverBinding.rlRow, holder.getAdapterPosition());
                        }
                    });*/
                break;


        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return chatScreenModels.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private ChatSenderBinding chatSenderBinding;


        public MyViewHolder(View itemView) {
            super(itemView);
            chatSenderBinding = DataBindingUtil.bind(itemView);


            chatSenderBinding.llSender.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongClick(chatSenderBinding.llSender,getAdapterPosition());
                    return true;
                }
            });


          /*  chatSenderBinding.llSender.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClick(chatSenderBinding.llSender, getAdapterPosition());
                    return true;
                }
            });*/
            chatSenderBinding.llSender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(chatSenderBinding.llSender, getAdapterPosition());
                }
            });





/*
            chatSenderBinding.llSender.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongClick(chatSenderBinding.llSender, getAdapterPosition());
                    return true;
                }
            });
            chatSenderBinding.llSender.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    listener.onItemClick(chatSenderBinding.llSender, getAdapterPosition());
                }
            });*/

        }
        public void bind(ChatScreenModel chatScreenModels) {

            if (chatScreenModels.isSelected()) {
                chatSenderBinding.checkBox.setVisibility(View.VISIBLE);

                chatSenderBinding.llSender.setBackgroundColor(context.getResources().getColor(R.color.transparentBlack40));
            } else {
                chatSenderBinding.checkBox.setVisibility(View.GONE);

                chatSenderBinding.llSender.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            }
        }



    }





        class MyViewHolder1 extends RecyclerView.ViewHolder {

        private ChatReceiverBinding chatRecieverBinding;

        public MyViewHolder1(View itemView) {
            super(itemView);
            chatRecieverBinding = DataBindingUtil.bind(itemView);


            chatRecieverBinding.llReciver.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClick(chatRecieverBinding.llReciver, getAdapterPosition());
                    return true;
                }
            });
            chatRecieverBinding.llReciver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(chatRecieverBinding.llReciver, getAdapterPosition());
                }
            });





/*

            chatRecieverBinding.llReciver.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemClick(chatRecieverBinding.llReciver, getAdapterPosition());
                    return true;
                }
            });
            chatRecieverBinding.llReciver.(new View.OnClickListener() {

                public void onClick(View v) {
                    listener.onItemClick(chatRecieverBinding.llReciver, getAdapterPosition());
                }
            });
*/


        }
            public void bind(ChatScreenModel chatScreenModels) {

                if (chatScreenModels.isSelected()) {
                    chatRecieverBinding.checkBoxReciver.setVisibility(View.VISIBLE);

                    chatRecieverBinding.llReciver.setBackgroundColor(context.getResources().getColor(R.color.transparentBlack40));
                } else {
                    chatRecieverBinding.checkBoxReciver.setVisibility(View.GONE);

                    chatRecieverBinding.llReciver.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                }
            }





    }


  /*  @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ListObject.TYPE_GENERAL_RIGHT:
                View currentUserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_sender, parent, false);
                viewHolder = new SenderViewHolder(currentUserView); // view holder for normal items
                break;
            case ListObject.TYPE_GENERAL_LEFT:
                View otherUserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_receiver, parent, false);
                viewHolder = new ReceiverViewHolder(otherUserView); // view holder for normal items
                break;
            case ListObject.TYPE_DATE:
                View v2 = inflater.inflate(R.layout.row_date, parent, false);
                viewHolder = new DateViewHolder(v2);
                break;





        }
        return viewHolder;
    }*/

  /*  @Override
    public int getItemViewType(int position) {
        return chatScreenModels.get(position).getType(new PrefManager(context).getUserId());
    }

    public ListObject getItem(int position) {
        return chatScreenModels.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case ListObject.TYPE_GENERAL_RIGHT:
                ChatModelObject generalItem = (ChatModelObject) chatScreenModels.get(position);
                SenderViewHolder chatViewHolder = (SenderViewHolder) viewHolder;
                chatViewHolder.bind(generalItem.getChatModel());


                break;
            case ListObject.TYPE_GENERAL_LEFT:
                ChatModelObject generalItemLeft = (ChatModelObject) chatScreenModels.get(position);
                ReceiverViewHolder chatLeftViewHolder = (ReceiverViewHolder) viewHolder;
                chatLeftViewHolder.bind(generalItemLeft.getChatModel());
                break;
            case ListObject.TYPE_DATE:
                DateObject dateItem = (DateObject) chatScreenModels.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;
                dateViewHolder.bind(dateItem.getDate());
                break;


        }
    }

    @Override
    public int getItemCount() {
        return chatScreenModels.size();
    }

    private String yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return String.valueOf(cal.getTimeInMillis());
    }

    private String today() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        return String.valueOf(cal.getTimeInMillis());
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        private ChatSenderBinding chatSenderBinding;

        public SenderViewHolder(View itemView) {
            super(itemView);
            chatSenderBinding = DataBindingUtil.bind(itemView);

            chatSenderBinding.rlRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClick(chatSenderBinding.rlRow, getAdapterPosition());
                    return true;
                }
            });

            chatSenderBinding.llView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClick(chatSenderBinding.rlRow, getAdapterPosition());
                    return true;
                }
            });
            chatSenderBinding.llChatReciever.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(chatSenderBinding.llChatReciever, getAdapterPosition());
                }
            });





        }




        public void bind(GetChatResonse.UserInfoBean chatModel) {
            if (chatModel.getIs_stared()) {
                chatSenderBinding.ivStared.setVisibility(View.VISIBLE);
            } else {
                chatSenderBinding.ivStared.setVisibility(View.GONE);
            }

            if (chatModel.isSelected()) {
                chatSenderBinding.rlRow.setBackgroundColor(context.getResources().getColor(R.color.transparentBlack40));
            } else {
                chatSenderBinding.rlRow.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            }

            if (chatModel.isRead_status()) {
                chatSenderBinding.tvReadStatus.setVisibility(View.VISIBLE);
            } else {
                chatSenderBinding.tvReadStatus.setVisibility(View.INVISIBLE);
            }

            if (chatModel.getUpload_type() != null && chatModel.getUpload_type().length() > 0) {
                Gson gson = null;
                switch (chatModel.getUpload_type()) {
                    case "audio":
                        chatSenderBinding.text.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.audio.getRoot().setVisibility(View.VISIBLE);
                        chatSenderBinding.video.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.image.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.contact.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.post.getRoot().setVisibility(View.GONE);
                        try {
                            gson = new Gson();
                            AudioModel audioModel = gson.fromJson(chatModel.getBody(), AudioModel.class);
                            chatSenderBinding.audio.audioPlayer.setAudioTarget(audioModel.getUrl());

                        } catch (Exception e) {
                            chatSenderBinding.audio.audioPlayer.setAudioTarget(chatModel.getBody());
                            e.printStackTrace();
                        }
                        break;

                    case "post":
                        gson = new Gson();
                        PostChatBody postChatBody = gson.fromJson(chatModel.getBody(), PostChatBody.class);
                        chatSenderBinding.text.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.audio.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.video.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.image.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.post.getRoot().setVisibility(View.VISIBLE);
                        chatSenderBinding.contact.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.image.tvText.setVisibility(View.GONE);

                        if (postChatBody.getUser_image() != null && postChatBody.getUser_image().length() > 0) {
                            Picasso.with(context).load(postChatBody.getUser_image()).transform(new CircleTransformation()).into(chatSenderBinding.post.ivContactUserPic);
                        } else {
                           //Picasso.with(context).load(R.drawable.user_placeholder).transform(new CircleTransformation()).into(chatSenderBinding.post.ivContactUserPic);

                             CommonUtils.otherUserProfile(context,chatSenderBinding.post.ivContactUserPic,postChatBody.getImage(),chatSenderBinding.post.tvUserImage,
                                     postChatBody.getUser_name());

                        }
                        if (postChatBody.getUser_name() != null && postChatBody.getUser_name().length() > 0) {
                            chatSenderBinding.post.tvTagUserName.setText(postChatBody.getUser_name());
                        }
                        if (postChatBody.getImage() != null && postChatBody.getImage().length() > 0 &&
                                postChatBody.getDescription() != null && postChatBody.getDescription().length() > 0) {
                            chatSenderBinding.post.imageView.setVisibility(View.VISIBLE);
                            Picasso.with(context).load(postChatBody.getImage()).into(chatSenderBinding.post.imageView);
                            chatSenderBinding.post.tvText.setText(postChatBody.getDescription());
                        } else if (postChatBody.getImage() != null && postChatBody.getImage().length() > 0) {
                            chatSenderBinding.post.imageView.setVisibility(View.VISIBLE);
                            Picasso.with(context).load(postChatBody.getImage()).into(chatSenderBinding.post.imageView);
                        } else if (postChatBody.getDescription() != null && postChatBody.getDescription().length() > 0) {
                            chatSenderBinding.post.imageView.setVisibility(View.GONE);
                            chatSenderBinding.post.tvText.setText(postChatBody.getDescription());
                        }
                        break;

                    case "video":
                        chatSenderBinding.text.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.audio.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.video.getRoot().setVisibility(View.VISIBLE);
                        chatSenderBinding.image.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.contact.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.image.tvText.setVisibility(View.GONE);
                        chatSenderBinding.post.getRoot().setVisibility(View.GONE);
                        Picasso.with(context).load(chatModel.getThumbnail()).into(chatSenderBinding.video.imageView);
                        break;
                    case "location":
                        chatSenderBinding.text.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.audio.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.video.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.image.getRoot().setVisibility(View.VISIBLE);
                        chatSenderBinding.contact.getRoot().setVisibility(View.GONE);
                        //Picasso.with(context).load( chatModel.getBody()).into(myViewHolder.chatSenderBinding.image.imageView);
                        gson = new Gson();
                        LocationModel locationModel = gson.fromJson(chatModel.getBody(), LocationModel.class);
                        chatSenderBinding.image.tvText.setText(locationModel.getName());
                        chatSenderBinding.image.tvText.setVisibility(View.VISIBLE);
                        chatSenderBinding.post.getRoot().setVisibility(View.GONE);
                        Picasso.with(context).load(CommonUtils.local(String.valueOf(locationModel.getLatitude()), String.valueOf(locationModel.getLongitude()))).into(chatSenderBinding.image.imageView);

                        break;
                    case "contact":
                        chatSenderBinding.text.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.audio.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.video.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.image.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.post.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.contact.getRoot().setVisibility(View.VISIBLE);
                        gson = new Gson();
                        ContactModel contactModel = gson.fromJson(chatModel.getBody(), ContactModel.class);
                        chatSenderBinding.contact.tvName.setText(contactModel.getName());
                        chatSenderBinding.contact.tvNumber.setText(contactModel.getNumber());

                        break;
                    case "image":
                        chatSenderBinding.text.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.audio.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.video.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.image.getRoot().setVisibility(View.VISIBLE);
                        chatSenderBinding.contact.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.image.tvText.setVisibility(View.GONE);
                        chatSenderBinding.post.getRoot().setVisibility(View.GONE);
                        Picasso.with(context).load(chatModel.getBody()).into(chatSenderBinding.image.imageView);
                        break;
                    case "doc":
                        chatSenderBinding.text.getRoot().setVisibility(View.VISIBLE);
                        chatSenderBinding.audio.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.video.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.image.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.contact.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.post.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.text.tvText.setText(chatModel.getBody());
                        break;

                    case "link":

                        chatSenderBinding.text.getRoot().setVisibility(View.VISIBLE);
                        chatSenderBinding.audio.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.video.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.image.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.post.getRoot().setVisibility(View.GONE);
                        chatSenderBinding.contact.getRoot().setVisibility(View.GONE);
                        Spanned html = Html.fromHtml("<a href='" + chatModel.getBody() + "'>" + chatModel.getBody() + "</a>");
                        chatSenderBinding.text.tvText.setMovementMethod(LinkMovementMethod.getInstance());
                        // Set TextView text from html
                        chatSenderBinding.text.tvText.setText(html);


                        break;
                }
            } else {
                chatSenderBinding.text.getRoot().setVisibility(View.VISIBLE);
                chatSenderBinding.audio.getRoot().setVisibility(View.GONE);
                chatSenderBinding.video.getRoot().setVisibility(View.GONE);
                chatSenderBinding.image.getRoot().setVisibility(View.GONE);
                chatSenderBinding.post.getRoot().setVisibility(View.GONE);
                chatSenderBinding.contact.getRoot().setVisibility(View.GONE);
                chatSenderBinding.text.tvText.setText(chatModel.getBody());

            }


            if (chatModel.getReply_message() != null) {
                chatSenderBinding.reply.getRoot().setVisibility(View.VISIBLE);
                if (chatModel.getReply_message().getUpload_type() != null && chatModel.getReply_message().getUpload_type().length() > 0) {
                    switch (chatModel.getReply_message().getUpload_type()) {
                        case "audio":
                            chatSenderBinding.reply.tvRepyMessage.setText("Audio");
                            break;
                        case "video":
                            chatSenderBinding.reply.tvRepyMessage.setText("Video");
                            break;
                        case "location":
                            chatSenderBinding.reply.tvRepyMessage.setText("Location");
                            break;
                        case "contact":
                            chatSenderBinding.reply.tvRepyMessage.setText("Contact");
                            break;
                        case "image":
                            chatSenderBinding.reply.tvRepyMessage.setText("Image");
                            break;
                        case "doc":
                            chatSenderBinding.reply.tvRepyMessage.setText("Document");
                            break;
                    }
                } else {
                    chatSenderBinding.reply.tvRepyMessage.setText(chatModel.getReply_message().getBody());
                }
            } else {
                chatSenderBinding.reply.getRoot().setVisibility(View.GONE);
            }


            if (chatModel.getCreated_at() != null && chatModel.getCreated_at().length() > 0) {
                switch (chatModel.getCreated_at().length()) {
                    case 10:
                        chatSenderBinding.tvChatReciverTime.setText(TimeStampFormatter.getDayFromTS(chatModel.getCreated_at(), "HH:mm"));
                        break;
                    default:
                        String strDate = "Not availbale";
                        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
                        Date date = null;
                        try {
                            date = readDate.parse(chatModel.getCreated_at());
                            long commentDate = date.getTime();
                            long timeElapsed = CommonUtils.timeElapsedFromDate(context, commentDate);
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
                        chatSenderBinding.tvChatReciverTime.setText(strDate);
                        break;
                }

            }
            if (chatModel.getAvatar_image() != null && chatModel.getAvatar_image().length() > 0) {
                Picasso.with(context).load(chatModel.getAvatar_image()).transform(new CircleTransformation()).into(chatSenderBinding.ivUserReciever);
            } else {
                if (new PrefManager(context).getUserPic() != null && !(new PrefManager(context).getUserPic()).equalsIgnoreCase("")) {
                    Picasso.with(context).load(new PrefManager(context).getUserPic()).transform(new CircleTransformation()).into(chatSenderBinding.ivUserReciever);
                } else {
                   Picasso.with(context).load(R.drawable.user_placeholder).transform(new CircleTransformation()).into(chatSenderBinding.ivUserReciever);



                   *//* CommonUtils.otherUserProfile(context,chatSenderBinding.ivUserReciever,new PrefManager(context).getUserPic(),chatSenderBinding.tvUserImage,
                            new PrefManager(context).getUserNameNum());*//*

                }
            }


        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {

        private ChatReceiverBinding chatRecieverBinding;


        public ReceiverViewHolder(View itemView) {
            super(itemView);
            chatRecieverBinding = DataBindingUtil.bind(itemView);
            //chatRecieverBinding.llView.setBackgroundResource(ContextCompat.getDrawable(context, themes.setChatCardColor(darkThemeEnabled)));
            chatRecieverBinding.llRowSender.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClick(chatRecieverBinding.llRowSender, getAdapterPosition());
                    return true;
                }
            });
            chatRecieverBinding.llView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClick(chatRecieverBinding.llRowSender, getAdapterPosition());
                    return true;
                }
            });
            chatRecieverBinding.llAddView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(chatRecieverBinding.llAddView, getAdapterPosition());
                }
            });
        }

        public void bind(GetChatResonse.UserInfoBean chatModel) {
            if (chatModel.isSelected()) {
                chatRecieverBinding.llRowSender.setBackgroundColor(context.getResources().getColor(R.color.transparentBlack40));
            } else {
                chatRecieverBinding.llRowSender.setBackgroundColor(context.getResources().getColor(R.color.transparent));

            }
            if (chatModel.getIs_stared()) {
                chatRecieverBinding.ivStared.setVisibility(View.VISIBLE);
            } else {
                chatRecieverBinding.ivStared.setVisibility(View.GONE);
            }
            if (chatModel.getUpload_type() != null && chatModel.getUpload_type().length() > 0) {
                Gson gson = null;

                switch (chatModel.getUpload_type()) {
                    case "audio":
                        chatRecieverBinding.text.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.audio.getRoot().setVisibility(View.VISIBLE);
                        chatRecieverBinding.video.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.image.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.contact.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.post.getRoot().setVisibility(View.GONE);
                        try {
                            gson = new Gson();
                            AudioModel audioModel = gson.fromJson(chatModel.getBody(), AudioModel.class);
                            chatRecieverBinding.audio.audioPlayer.setAudioTarget(audioModel.getUrl());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case "post":

                        gson = new Gson();
                        PostChatBody postChatBody = gson.fromJson(chatModel.getBody(), PostChatBody.class);
                        chatRecieverBinding.text.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.audio.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.video.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.image.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.post.getRoot().setVisibility(View.VISIBLE);
                        chatRecieverBinding.contact.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.image.tvText.setVisibility(View.GONE);

                        if (postChatBody.getUser_image() != null && postChatBody.getUser_image().length() > 0) {
                            Picasso.with(context).load(postChatBody.getImage()).into(chatRecieverBinding.post.ivContactUserPic);
                        }
                        else {

                            CommonUtils.otherUserProfile(context,chatRecieverBinding.post.ivContactUserPic,postChatBody.getImage(),chatRecieverBinding.post.tvUserImage,
                                    postChatBody.getUser_name());


                        }
                        if (postChatBody.getUser_name() != null && postChatBody.getUser_name().length() > 0) {
                            chatRecieverBinding.post.tvTagUserName.setText(postChatBody.getUser_name());
                        }
                        if (postChatBody.getImage() != null && postChatBody.getImage().length() > 0 &&
                                postChatBody.getDescription() != null && postChatBody.getDescription().length() > 0) {
                            chatRecieverBinding.post.imageView.setVisibility(View.VISIBLE);
                            Picasso.with(context).load(postChatBody.getImage()).into(chatRecieverBinding.post.imageView);
                            chatRecieverBinding.post.tvText.setText(postChatBody.getDescription());
                        } else if (postChatBody.getImage() != null && postChatBody.getImage().length() > 0) {
                            chatRecieverBinding.post.imageView.setVisibility(View.VISIBLE);
                            Picasso.with(context).load(postChatBody.getImage()).into(chatRecieverBinding.post.imageView);
                        } else if (postChatBody.getDescription() != null && postChatBody.getDescription().length() > 0) {
                            chatRecieverBinding.post.imageView.setVisibility(View.GONE);
                            chatRecieverBinding.post.tvText.setText(postChatBody.getDescription());
                        }
                        break;
                    case "video":
                        chatRecieverBinding.text.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.audio.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.video.getRoot().setVisibility(View.VISIBLE);
                        chatRecieverBinding.image.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.contact.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.image.tvText.setVisibility(View.GONE);
                        chatRecieverBinding.post.getRoot().setVisibility(View.GONE);
                        Picasso.with(context).load(chatModel.getThumbnail()).into(chatRecieverBinding.video.imageView);
                        break;
                    case "location":
                        chatRecieverBinding.text.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.audio.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.video.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.image.getRoot().setVisibility(View.VISIBLE);
                        chatRecieverBinding.contact.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.image.tvText.setVisibility(View.VISIBLE);
                        chatRecieverBinding.post.getRoot().setVisibility(View.GONE);
                        gson = new Gson();
                        LocationModel locationModel = gson.fromJson(chatModel.getBody(), LocationModel.class);
                        chatRecieverBinding.image.tvText.setText(locationModel.getName());
                        Picasso.with(context).load(CommonUtils.local(String.valueOf(locationModel.getLatitude()), String.valueOf(locationModel.getLongitude()))).into(chatRecieverBinding.image.imageView);
                        break;
                    case "contact":
                        chatRecieverBinding.text.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.audio.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.video.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.image.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.contact.getRoot().setVisibility(View.VISIBLE);
                        chatRecieverBinding.post.getRoot().setVisibility(View.GONE);
                        gson = new Gson();
                        ContactModel contactModel = gson.fromJson(chatModel.getBody(), ContactModel.class);
                        chatRecieverBinding.contact.tvName.setText(contactModel.getName());
                        chatRecieverBinding.contact.tvNumber.setText(contactModel.getNumber());
                        break;
                    case "image":
                        chatRecieverBinding.text.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.audio.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.video.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.image.getRoot().setVisibility(View.VISIBLE);
                        chatRecieverBinding.contact.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.image.tvText.setVisibility(View.GONE);
                        chatRecieverBinding.post.getRoot().setVisibility(View.GONE);
                        Picasso.with(context).load(chatModel.getBody()).into(chatRecieverBinding.image.imageView);

                        break;
                    case "doc":
                        chatRecieverBinding.text.getRoot().setVisibility(View.VISIBLE);
                        chatRecieverBinding.audio.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.video.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.image.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.contact.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.post.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.text.tvText.setText(chatModel.getBody());
                        break;
                    case "link":
                        chatRecieverBinding.text.getRoot().setVisibility(View.VISIBLE);
                        chatRecieverBinding.audio.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.video.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.image.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.post.getRoot().setVisibility(View.GONE);
                        chatRecieverBinding.contact.getRoot().setVisibility(View.GONE);
                        Spanned html = Html.fromHtml("<a href='" + chatModel.getBody() + "'>" + chatModel.getBody() + "</a>");
                        chatRecieverBinding.text.tvText.setMovementMethod(LinkMovementMethod.getInstance());
                        // Set TextView text from html
                        chatRecieverBinding.text.tvText.setText(html);

                        break;
                }
            } else {
                chatRecieverBinding.text.getRoot().setVisibility(View.VISIBLE);
                chatRecieverBinding.audio.getRoot().setVisibility(View.GONE);
                chatRecieverBinding.video.getRoot().setVisibility(View.GONE);
                chatRecieverBinding.image.getRoot().setVisibility(View.GONE);
                chatRecieverBinding.contact.getRoot().setVisibility(View.GONE);
                chatRecieverBinding.post.getRoot().setVisibility(View.GONE);
                chatRecieverBinding.text.tvText.setText(chatModel.getBody());
            }
            if (chatModel.getReply_message() != null) {
                chatRecieverBinding.reply.getRoot().setVisibility(View.VISIBLE);
                if (chatModel.getReply_message().getUpload_type() != null && chatModel.getReply_message().getUpload_type().length() > 0) {
                    switch (chatModel.getReply_message().getUpload_type()) {
                        case "audio":
                            chatRecieverBinding.reply.tvRepyMessage.setText("Audio");
                            break;
                        case "video":
                            chatRecieverBinding.reply.tvRepyMessage.setText("Video");
                            break;
                        case "location":
                            chatRecieverBinding.reply.tvRepyMessage.setText("Location");
                            break;
                        case "contact":
                            chatRecieverBinding.reply.tvRepyMessage.setText("Contact");
                            break;
                        case "image":
                            chatRecieverBinding.reply.tvRepyMessage.setText("Image");
                            break;
                        case "doc":
                            chatRecieverBinding.reply.tvRepyMessage.setText("Document");
                            break;
                    }
                } else {
                    chatRecieverBinding.reply.tvRepyMessage.setText(chatModel.getReply_message().getBody());
                }
            } else {
                chatRecieverBinding.reply.getRoot().setVisibility(View.GONE);
            }
            if (chatModel.getCreated_at() != null && chatModel.getCreated_at().length() > 0) {
                switch (chatModel.getCreated_at().length()) {
                    case 10:
                        chatRecieverBinding.tvChatSenderTime.setText(TimeStampFormatter.getDayFromTS(chatModel.getCreated_at(), "HH:mm"));
                        break;
                    default:
                        String strDate = "Not availbale";
                        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
                        Date date = null;
                        try {
                            date = readDate.parse(chatModel.getCreated_at());
                            long commentDate = date.getTime();
                            long timeElapsed = CommonUtils.timeElapsedFromDate(context, commentDate);
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
                        chatRecieverBinding.tvChatSenderTime.setText(strDate);
                        break;
                }

            }
            if (chatModel.getAvatar_image() != null && chatModel.getAvatar_image().length() > 0) {
                Picasso.with(context).load(chatModel.getAvatar_image()).transform(new CircleTransformation()).into(chatRecieverBinding.senderImage);
            } else {
                if (chatModel.getSender_image() != null && !chatModel.getSender_image().equalsIgnoreCase("")) {
                    Picasso.with(context).load(chatModel.getSender_image()).transform(new CircleTransformation()).into(chatRecieverBinding.senderImage);
                } else {
                    Picasso.with(context).load(R.drawable.user_placeholder).transform(new CircleTransformation()).into(chatRecieverBinding.senderImage);
                }
            }
        }
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        private RowDateBinding rowDateBinding;

        public DateViewHolder(View itemView) {
            super(itemView);
            rowDateBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(final String date) {
            if (TimeStampFormatter.getDayFromTS(today(), "dd-MM-yyyy").equalsIgnoreCase(date)) {
                rowDateBinding.tvText.setText("Today");
            } else if (TimeStampFormatter.getDayFromTS(yesterday(), "dd-MM-yyyy").equalsIgnoreCase(date)) {
                rowDateBinding.tvText.setText("Yesterday");
            } else {
                rowDateBinding.tvText.setText(date);
            }
        }
    }*/
}