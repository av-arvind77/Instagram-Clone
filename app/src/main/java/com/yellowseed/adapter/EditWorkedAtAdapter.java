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
import com.yellowseed.databinding.RowWorkatLayoutBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.reqModel.SchoolModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;

import java.util.List;

public class EditWorkedAtAdapter extends RecyclerView.Adapter<EditWorkedAtAdapter.MyViewHolder> {

    private List<SchoolModel> arlModel;
    private Context mContext;
    private OnItemClickListener listener;
    private boolean isRequestFocus = false;
    private   Themes themes;
    private Boolean darkThemeEnabled;

    public EditWorkedAtAdapter(Context mContext, List<SchoolModel> arlModel, OnItemClickListener listener) {
        this.arlModel = arlModel;
        this.mContext = mContext;
        this.listener = listener;
        themes = new Themes(mContext);
       darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);

    }

    @NonNull
    @Override
    public EditWorkedAtAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_workat_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditWorkedAtAdapter.MyViewHolder holder, int position) {

        holder.binding.tvWorked.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        holder.binding.tvDesignation.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        holder.binding.etDesignation.setBackground(ContextCompat.getDrawable(mContext,themes.setDarkEditProfileBackground()));
        holder.binding.tvAddSchool.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        holder.binding.etWorkAt.setBackground(ContextCompat.getDrawable(mContext,themes.setDarkEditProfileBackground()));
        holder.binding.etWorkAt.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        holder.binding.etDesignation.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));

        final SchoolModel model = arlModel.get(position);
        holder.binding.etWorkAt.setText(TextUtils.isEmpty(model.getWork_at()) ? "" : model.getWork_at());
        holder.binding.etDesignation.setText(TextUtils.isEmpty(model.getDesignation()) ? "" : model.getDesignation());

        if (holder.getAdapterPosition() == arlModel.size()-1){
            holder.binding.tvAddSchool.setText("+");
            if(isRequestFocus) {
                holder.binding.etWorkAt.requestFocus();
                isRequestFocus = false;
            }
        }else {
            holder.binding.tvAddSchool.setText("-");
        }




    }

    @Override
    public int getItemCount() {

        if (arlModel !=null && arlModel.size()>0)


        return arlModel.size();
        else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowWorkatLayoutBinding binding;

        public MyViewHolder(View itemView) {

            super(itemView);

            binding = DataBindingUtil.bind(itemView);
            binding.tvAddSchool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binding.tvAddSchool.getText().toString().equalsIgnoreCase(AppConstants.PLUS)) {
                        if (TextUtils.isEmpty(binding.etWorkAt.getText().toString())) {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.enter_work_at));
                        } else if (TextUtils.isEmpty(binding.etDesignation.getText().toString())) {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.enter_designation));

                        } else {
                            listener.onItemClick(binding.tvAddSchool, getAdapterPosition());
                            isRequestFocus = true;

                        }

                    }
                    else {
                        arlModel.remove(getAdapterPosition());
                        isRequestFocus = true;
                        notifyDataSetChanged();
                    }
                }

            });

            binding.etDesignation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    arlModel.get(getAdapterPosition()).setDesignation(s.toString().trim());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            binding.etWorkAt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    arlModel.get(getAdapterPosition()).setWork_at(s.toString().trim());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        }
    }
}
