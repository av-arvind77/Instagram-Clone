package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.adapter.ChatScreenAdapter;
import com.yellowseed.databinding.AnonymousChatReciverBinding;
import com.yellowseed.databinding.AnonymousChatSenderBinding;
import com.yellowseed.databinding.ChatReceiverBinding;
import com.yellowseed.databinding.ChatSenderBinding;
import com.yellowseed.fragments.ChatsFragment;
import com.yellowseed.listener.OnItemLongClickListener;
import com.yellowseed.model.ChatScreenModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

public class AnonymousChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Themes themes;
    private Context context;
    private ArrayList<ChatScreenModel> chatScreenModels;
    private OnItemLongClickListener listener;
    private boolean darkThemeEnabled;
    private int layout = -1;
    public static boolean isCheck = false;


    /*   public ChatScreenAdapter(Activity context, ArrayList<ListObject> models, OnItemClickListener listener) {
        this.context = context;
        this.chatScreenModels = models;
        themes = new Themes(context);
        this.listener = listener;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
    }
*/
    public AnonymousChatAdapter(Context context, ArrayList<ChatScreenModel> chatScreenModels, OnItemLongClickListener listener) {
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
                layout = R.layout.anonymous_chat_sender;
                View view = LayoutInflater.from(context).inflate(layout, parent, false);

                return new AnonymousChatAdapter.MyViewHolder(view);

            case 1:
                layout = R.layout.anonymous_chat_reciver;
                View v = LayoutInflater.from(context).inflate(layout, parent, false);

                return new AnonymousChatAdapter.MyViewHolder1(v);

            default:
                break;
        }

        return null;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if(holder.getItemViewType()!= null) {
//            holder.chatSenderBinding.tvChatSender.setText(chatScreenModels.get(position).getMessages());
//        }
//        else if(holder.chatRecieverBinding != null) {
//            holder.chatRecieverBinding.tvChatReciver.setText(chatScreenModels.get(position).getMessages());
//        }


        switch (holder.getItemViewType()) {
            case 0:
                AnonymousChatAdapter.MyViewHolder myViewHolder = (AnonymousChatAdapter.MyViewHolder) holder;
                myViewHolder.chatSenderBinding.tvChatSender.setText(chatScreenModels.get(position).getMessages());

                if (ChatsFragment.isCheck) {
                    myViewHolder.chatSenderBinding.checkBoxChatsFraEdit.setVisibility(View.VISIBLE);
                    myViewHolder.chatSenderBinding.llRowSender.setEnabled(false);
                } else {
                    myViewHolder.chatSenderBinding.llRowSender.setEnabled(true);
                    myViewHolder.chatSenderBinding.checkBoxChatsFraEdit.setVisibility(View.GONE);
                    myViewHolder.chatSenderBinding.llRowSender.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            listener.onItemClick(myViewHolder.chatSenderBinding.llRowSender, holder.getAdapterPosition());
                        }
                    });
                }


                break;
            case 1:
                AnonymousChatAdapter.MyViewHolder1 myViewHolder1 = (AnonymousChatAdapter.MyViewHolder1) holder;
                myViewHolder1.chatRecieverBinding.tvChatReciver.setText(chatScreenModels.get(position).getMessages());

                if (ChatsFragment.isCheck) {
                    myViewHolder1.chatRecieverBinding.checkBoxChatsFraEdit.setVisibility(View.VISIBLE);
                    myViewHolder1.chatRecieverBinding.rlRow.setEnabled(false);
                } else {
                    myViewHolder1.chatRecieverBinding.rlRow.setEnabled(true);
                    myViewHolder1.chatRecieverBinding.checkBoxChatsFraEdit.setVisibility(View.GONE);
                    myViewHolder1.chatRecieverBinding.rlRow.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            listener.onItemClick(myViewHolder1.chatRecieverBinding.rlRow, holder.getAdapterPosition());
                        }
                    });
                    break;


                }
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
        private AnonymousChatSenderBinding chatSenderBinding;


        public MyViewHolder(View itemView) {
            super(itemView);
            chatSenderBinding = DataBindingUtil.bind(itemView);
            chatSenderBinding.llRowSender.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClick(chatSenderBinding.llRowSender, getAdapterPosition());
                    return false;
                }
            });
        }


    }

    class MyViewHolder1 extends RecyclerView.ViewHolder {

        private AnonymousChatReciverBinding chatRecieverBinding;

        public MyViewHolder1(View itemView) {
            super(itemView);
            chatRecieverBinding = DataBindingUtil.bind(itemView);
            chatRecieverBinding.rlRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClick(chatRecieverBinding.rlRow, getAdapterPosition());
                    return false;
                }
            });

        }


    }
}
