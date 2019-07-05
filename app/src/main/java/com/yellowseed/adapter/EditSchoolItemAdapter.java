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
import com.yellowseed.databinding.EditSchoolItemLayoutBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.EditSchoolItemModel;
import com.yellowseed.model.reqModel.SchoolModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.List;

public class EditSchoolItemAdapter extends RecyclerView.Adapter<EditSchoolItemAdapter.MyViewHolder> {

    private List<SchoolModel> arlModel;
    private Context context;
    private OnItemClickListener listener;
    private boolean isRequestFocus;
    private   Themes themes;
    private Boolean darkThemeEnabled;



    public EditSchoolItemAdapter(Context context, List<SchoolModel> arlModel, OnItemClickListener listener) {
        this.arlModel = arlModel;
        this.context = context;
        this.listener = listener;
        themes = new Themes(context);
         darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);

    }


    @NonNull
    @Override
    public EditSchoolItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.edit_school_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditSchoolItemAdapter.MyViewHolder holder, int position) {

        holder.binding.tvAddSchool.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        holder.binding.etSchool.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        holder.binding.etSchool.setHintTextColor(ContextCompat.getColor(context, themes.setDarkEditProfileHint()));
        holder.binding.etSchool.setBackground(ContextCompat.getDrawable(context,themes.setDarkEditProfileBackground()));


        final SchoolModel model = arlModel.get(position);
        holder.binding.etSchool.setText(TextUtils.isEmpty(model.getSchool_name()) ? "" : model.getSchool_name());


           if (holder.getAdapterPosition() == arlModel.size() - 1) {
               holder.binding.tvAddSchool.setText("+");
               if(isRequestFocus) {
                   holder.binding.etSchool.requestFocus();
                   isRequestFocus = false;
               }
           } else {
               holder.binding.tvAddSchool.setText("-");
           }


    }

    @Override
    public int getItemCount() {
        if (arlModel != null && arlModel.size() > 0)
            return arlModel.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private EditSchoolItemLayoutBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
            binding.tvAddSchool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binding.tvAddSchool.getText().toString().equalsIgnoreCase(AppConstants.PLUS)) {
                        if (!binding.etSchool.getText().toString().isEmpty()) {
                            listener.onItemClick(binding.tvAddSchool, getAdapterPosition());
                            isRequestFocus = true;
                            notifyDataSetChanged();
                        } else {
                            CommonUtils.showToast(context, context.getString(R.string.enter_school_name));
                        }
                    }else {
                        arlModel.remove(getAdapterPosition());
                        isRequestFocus = true;
                        notifyDataSetChanged();
                    }
                }
            });

            binding.etSchool.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    arlModel.get(getAdapterPosition()).setSchool_name(s.toString().trim());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }
}
