package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.yellowseed.R;
import com.yellowseed.database.RoomChatTable;
import com.yellowseed.databinding.NewlayoutchatfragmentrvBinding;
import com.yellowseed.fragments.ChatsFragment;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.ChatFragmentModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.PinEntryView;
import com.yellowseed.utils.Themes;

import java.text.Normalizer;
import java.util.ArrayList;

public class ChatFragmentAdapter extends RecyclerSwipeAdapter<ChatFragmentAdapter.ListViewHolder> {
    PinEntryView pinEntryView;
    private SwipeLayout swipe;
    //  private ArrayList<GetRoomResonse.RoomsBean> arrayList;
    private String searchText = "";
    private ArrayList<ChatFragmentModel> arrayList;

    private Context context;
    private OnItemClickListener listener;
    private RoomChatTable roomChatTable;
    private Boolean darkThemeEnabled;
    private Themes themes;
    private LocalBroadcastManager lbm;
    private ListViewHolder holder;
    private Animation slideUpAnimation, slideDownAnimation;



 /*   public ChatFragmentAdapter(ArrayList<GetRoomResonse.RoomsBean> arrayList, Context context, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
        roomChatTable = new RoomChatTable(context);

    }*/

    public ChatFragmentAdapter(ArrayList<ChatFragmentModel> models, Context context, OnItemClickListener listener) {

        this.context = context;
        this.arrayList = models;
        this.listener = listener;
    }

    public static CharSequence highlight(String search, String originalText) {
        // ignore case and accents
        // the same thing should have been done for the search text
        String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();

        int start = normalizedText.indexOf(search);
        if (start < 0) {
            // not found, nothing to to
            return originalText;
        } else {
            // highlight each appearance in the original text
            // while searching in normalized text
            Spannable highlighted = new SpannableString(originalText);
            while (start >= 0) {
                int spanStart = Math.min(start, originalText.length());
                int spanEnd = Math.min(start + search.length(), originalText.length());
                highlighted.setSpan(new ForegroundColorSpan(Color.BLUE), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                //   highlighted.setSpan(new BackgroundColorSpan(Color.parseColor("#ff0000")), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                start = normalizedText.indexOf(search, spanEnd);
            }

            return highlighted;
        }
    }

    @NonNull
    @Override
    public ChatFragmentAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newlayoutchatfragmentrv, null, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
        this.holder = holder;
        //   setCurrentTheme();
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);



        holder.binding.llMute.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setMuteBackground()));
        holder.binding.llPin.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setPinBackround()));
        if (darkThemeEnabled) {
            holder.binding.pinIcon.setImageResource(R.mipmap.pin_icon_sml);
        } else {
            holder.binding.pinIcon.setImageResource(R.mipmap.pin_dark);
        }
        if (darkThemeEnabled) {
            holder.binding.unmuteIcon.setImageResource(R.mipmap.unmute_icon);
        } else {
            holder.binding.unmuteIcon.setImageResource(R.mipmap.new_unmute_icon);
        }
        holder.binding.llChatFraUser.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        holder.binding.tvChatFraUserName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvChatFraUserTime.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setGreyHint()));
        holder.binding.tvChatFraUserMessage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setGreyHint()));
        holder.binding.swipe.addDrag(SwipeLayout.DragEdge.Left, holder.binding.swipe.findViewById(R.id.llSwipeHide));
        holder.binding.swipe.addDrag(SwipeLayout.DragEdge.Right, holder.binding.swipe.findViewById(R.id.llSwipe));
        holder.binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));


        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));





       /* sample1.addDrag(SwipeLayout.DragEdge.Left, sample1.findViewById(R.id.bottom_wrapper));
        sample1.addDrag(SwipeLayout.DragEdge.Right, sample1.findViewById(R.id.bottom_wrapper_2));
*/
        /*
        holder.binding.llMainLayout.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        holder.binding.tvChatFraUserName.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        holder.binding.tvChatFraUserTime.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        holder.binding.tvChatFraUserMessage.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));

*/

      /*  DirectModel model = arrayList.get(position);
        if (arrayList != null) {
            holder.binding.ivChatFraPic.setImageResource(model.getImage_url());
            holder.binding.tvChatFraUserMessage.setText(model.getLast_message_url());
            holder.binding.tvChatFraUserTime.setText(model.getTime_ago_url());
            holder.binding.tvChatFraUserMessage.setText(model.getLast_message_url());


        }


        if (arrayList.get(position).isIs_mute()) {
            holder.binding.unmuteIcon.setVisibility(View.VISIBLE);
            holder.binding.tvMute.setText(context.getString(R.string.unmute));
        } else {
            holder.binding.unmuteIcon.setVisibility(View.GONE);
            holder.binding.tvMute.setText(context.getString(R.string.mute));
        }

        if (arrayList.get(position).getIs_pinned() == 1) {
            holder.binding.pinIcon.setVisibility(View.VISIBLE);
            holder.binding.tvPin.setText("UnPin");
        } else {
            holder.binding.pinIcon.setVisibility(View.GONE);
            holder.binding.tvPin.setText("Pin");
        }


           *//* CommonUtils.otherUserProfile(context, holder.binding.ivChatFraPic, arrayList.get(position).getImage(), holder.binding.tvUserImage,
                    arrayList.get(position).getName());*//*
         *//*  if (arrayList.get(position).getImage() != null)
        {
            Picasso.with(context).load(arrayList.get(position).getImage()).placeholder(R.drawable.user_placeholder).transform(new CircleTransformation()).into(holder.binding.ivChatFraPic);
        }
        *//*
        if (searchText.equalsIgnoreCase("")) {

            holder.binding.tvChatFraUserName.setText(arrayList.get(position).getName_url());
        } else {
            holder.binding.tvChatFraUserName.setText(highlight(searchText, arrayList.get(position).getName_url()));

        }
        try {

            if (TimeStampFormatter.getDayFromTS(String.valueOf(System.currentTimeMillis() / 1000), "dd/MM/yyyy").equalsIgnoreCase(TimeStampFormatter.getDayFromTS(arrayList.get(position).getLast_message_url(), "dd/MM/yyyy"))) {
                holder.binding.tvChatFraUserTime.setText(TimeStampFormatter.getDayFromTS(arrayList.get(position).getTime_ago_url(), "hh:mm:aa"));

            } else {
                if (arrayList.get(position).getLast_message_url() != null && !arrayList.get(position).getLast_message_url().equalsIgnoreCase("")) {
                    holder.binding.tvChatFraUserTime.setText(TimeStampFormatter.getDayFromTS(arrayList.get(position).getTime_ago_url(), "dd/MM/yyyy"));
                }
            }



          *//*  if (arrayList.get(position).getLast_message_time() != null && !arrayList.get(position).getLast_message_time().equalsIgnoreCase("")) {
                holder.binding.tvChatFraUserTime.setText(TimeStampFormatter.getDayFromTS(arrayList.get(position).getLast_message_time(),"dd/MM/yyyy"));
            }*//*
        } catch (Exception e) {
            e.printStackTrace();
        }
      *//*  if (roomChatTable.getLastMessageDetail(arrayList.get(position).getRoom_id()).getUpload_type() != null && roomChatTable.getLastMessageDetail(arrayList.get(position).getRoom_id()).getUpload_type().length() > 0) {
            switch (roomChatTable.getLastMessageDetail(arrayList.get(position).getRoom_id()).getUpload_type()) {
                case "audio":
                    holder.binding.tvChatFraUserMessage.setText("Audio");
                    break;
                case "video":
                    holder.binding.tvChatFraUserMessage.setText("Video");
                    break;
                case "location":
                    holder.binding.tvChatFraUserMessage.setText("Location");
                    break;
                case "image":
                    holder.binding.tvChatFraUserMessage.setText("Image");
                    break;
                case "post":
                    holder.binding.tvChatFraUserMessage.setText("Image");
                    break;
                default:
                    holder.binding.tvChatFraUserMessage.setText(roomChatTable.getLastMessageDetail(arrayList.get(position).getRoom_id()).getBody());
                    break;
            }
        } else {
            holder.binding.tvChatFraUserMessage.setText(roomChatTable.getLastMessageDetail(arrayList.get(position).getRoom_id()).getBody());
        }*//*
        holder.binding.checkBoxChatsFraEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrayList.get(position).setIs_checked_for_delete(true);
                } else {
                    arrayList.get(position).setIs_checked_for_delete(false);
                }
            }
        });

        if (ChatsFragment.isCheck) {
            holder.binding.checkBoxChatsFraEdit.setVisibility(View.VISIBLE);
            holder.binding.llChatFraUser.setEnabled(false);
        } else {
            holder.binding.llChatFraUser.setEnabled(true);
            holder.binding.checkBoxChatsFraEdit.setVisibility(View.GONE);
            holder.binding.llChatFraUser.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    listener.onItemClick(holder.binding.llChatFraUser, holder.getAdapterPosition());
                }
            });
        }

        mItemManger.bindView(holder.binding.swipe, position);


        holder.binding.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(holder.binding.llDelete, holder.getAdapterPosition());
            }
        });

        holder.binding.llMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.swipe.close(true, true);
                listener.onItemClick(holder.binding.llMute, holder.getAdapterPosition());
            }
        });

        holder.binding.llPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.swipe.close(true, true);
                listener.onItemClick(holder.binding.llPin, holder.getAdapterPosition());
            }
        });
        holder.binding.llHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.swipe.close(true, true);
                listener.onItemClick(holder.binding.llHide, holder.getAdapterPosition());
            }
        });
*/

        //changing` theme color


        if (position == 0) {
            holder.binding.pinIcon.setVisibility(View.VISIBLE);
            holder.binding.unmuteIcon.setVisibility(View.VISIBLE);
        }
        holder.binding.ivChatFraPic.setImageResource(arrayList.get(position).getUserChatFraImage());
        holder.binding.tvChatFraUserName.setText(arrayList.get(position).getUserChatFraName());
        holder.binding.tvChatFraUserTime.setText(arrayList.get(position).getUserChatFraTime());
        holder.binding.tvChatFraUserMessage.setText(arrayList.get(position).getUserChatFraContent());
        holder.binding.checkBoxChatsFraEdit.setVisibility(View.VISIBLE);


        if (ChatsFragment.isCheck) {
            holder.binding.checkBoxChatsFraEdit.setVisibility(View.VISIBLE);
            holder.binding.llChatFraUser.setEnabled(false);
        } else {
            holder.binding.llChatFraUser.setEnabled(true);
            holder.binding.checkBoxChatsFraEdit.setVisibility(View.GONE);
            holder.binding.llChatFraUser.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    listener.onItemClick(holder.binding.llChatFraUser, holder.getAdapterPosition());
                }
            });
        }

        mItemManger.bindView(holder.binding.swipe, position);


        holder.binding.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llDelete, holder.getAdapterPosition());
            }
        });

        holder.binding.llMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llMute, holder.getAdapterPosition());
            }
        });

        holder.binding.llPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llPin, holder.getAdapterPosition());
            }
        });
        holder.binding.llHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llHide, holder.getAdapterPosition());
            }
        });
        holder.binding.llSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llHide, holder.getAdapterPosition());
            }
        });


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public void updatedList(ArrayList<ChatFragmentModel> filteresNames, String searchText) {
        arrayList = filteresNames;
        this.searchText = searchText;
        notifyDataSetChanged();


    }


    public SwipeLayout getLayout() {
        return swipe;
    }

    public void setCurrentTheme() {

    }

    public void updatedList(ArrayList<ChatFragmentModel> filteredName) {
        arrayList = filteredName;
        notifyDataSetChanged();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        public NewlayoutchatfragmentrvBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            swipe = itemView.findViewById(getSwipeLayoutResourceId(getAdapterPosition()));
        }
    }
}
