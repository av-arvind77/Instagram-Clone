package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.databinding.RowCreatePollBinding;
import com.yellowseed.listener.ClearErrorListener;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.CreatePollModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.List;

public class CreatePollAdapter extends RecyclerView.Adapter<CreatePollAdapter.MyViewHolder> {

    private Context context;
    private List<CreatePollModel> alCreatePollModel;
    private OnItemClickListener listener;
    private ClearErrorListener clearErrorListener;
    private Themes themes;
    private boolean isRequestFocus;


/*    public CreatePollAdapter(Context context, List<CreatePollModel> alCreatePollModel, OnItemClickListener listener, ClearErrorListener clearErrorListener) {
        this.context = context;
        this.alCreatePollModel = alCreatePollModel;
        this.listener = listener;
        this.clearErrorListener = clearErrorListener;
        themes = new Themes(context);
    }*/

    public CreatePollAdapter(Context context, List<CreatePollModel> alCreatePoll, OnItemClickListener listener) {

        this.context = context;
        this.alCreatePollModel = alCreatePoll;
        this.listener = listener;
        themes = new Themes(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_create_poll, parent, false);
        return new MyViewHolder(view);
        //  return new MyViewHolder(view, new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (alCreatePollModel != null) {
            for (int i = 0; i < alCreatePollModel.size(); i++) {
                alCreatePollModel.get(i).setHintText("Choice " + (i + 1));

            }
        }
        //   holder.binding.tvAddOptions.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        holder.binding.etOptions.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        holder.binding.etOptions.setHintTextColor(ContextCompat.getColor(context, themes.setDarkEditProfileHint()));
        holder.binding.etOptions.setHint(alCreatePollModel.get(position).getHintText());
        holder.binding.etOptions.setBackground(ContextCompat.getDrawable(context, themes.setDarkEditProfileBackground()));
        final CreatePollModel model = alCreatePollModel.get(position);
        holder.binding.etOptions.setText(TextUtils.isEmpty(model.getOptions()) ? "" : model.getOptions());

        if (position == 0) {
            holder.binding.tvAddOptions.setText("");
            holder.binding.tvAddOptions.setVisibility(View.INVISIBLE);
        } else if (position == alCreatePollModel.size() - 1 && alCreatePollModel.size() < 4) {
            holder.binding.tvAddOptions.setText("");
            alCreatePollModel.get(position).setAdd(true);
            holder.binding.tvAddOptions.setBackgroundResource(R.mipmap.yellow_add);
            holder.binding.tvAddOptions.setVisibility(View.VISIBLE);


        } else {
            alCreatePollModel.get(position).setAdd(false);
            holder.binding.tvAddOptions.setBackground(null);
            if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
                holder.binding.tvAddOptions.setTextColor(ContextCompat.getColor(context, R.color.white));

            } else {
                holder.binding.tvAddOptions.setTextColor(ContextCompat.getColor(context, R.color.black));

            }
            holder.binding.tvAddOptions.setText("-");
            holder.binding.tvAddOptions.setVisibility(View.VISIBLE);
        }



       /* if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            holder.binding.tvAddOptions.setBackgroundResource(R.mipmap.yellow_add);

        } else {
            holder.binding.tvAddOptions.setBackground(null);

        }*/

       /* if (holder.getAdapterPosition() == alCreatePollModel.size() - 1) {
            holder.binding.tvAddOptions.setText("+");
            if (isRequestFocus) {
                holder.binding.etOptions.requestFocus();
                isRequestFocus = false;


            }
        } else {
            if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
                holder.binding.tvAddOptions.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));

            }


            holder.binding.tvAddOptions.setBackground(null);

            holder.binding.tvAddOptions.setText("-");
        }*/


    }

    @Override
    public int getItemCount() {
        if (alCreatePollModel != null && alCreatePollModel.size() > 0)

            return alCreatePollModel.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowCreatePollBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);


            binding.tvAddOptions.setOnClickListener(v -> {
                if (alCreatePollModel.size() > 1 && alCreatePollModel.size() < 5) {
                    if (alCreatePollModel.get(getAdapterPosition()).isAdd()) {
                        //if (binding.tvAddOptions.getText().toString().equalsIgnoreCase(AppConstants.PLUS)) {
                       /* if (!binding.etOptions.getText().toString().isEmpty()) {
                            listener.onItemClick(binding.tvAddOptions, getAdapterPosition());
                            isRequestFocus = true;
                            notifyDataSetChanged();
                        } else {
                            CommonUtils.showToast(context, context.getString(R.string.enter_option));
                        }*/
                        listener.onItemClick(binding.tvAddOptions, getAdapterPosition());
                        isRequestFocus = true;
                        notifyDataSetChanged();
                    } else {
                        alCreatePollModel.remove(getAdapterPosition());
                        isRequestFocus = true;
                        notifyDataSetChanged();
                    }
                }

            });

            binding.etOptions.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    alCreatePollModel.get(getAdapterPosition()).setOptions(s.toString().trim());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }


/*

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            CreatePollActivity.firstAddress = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

*/

}
