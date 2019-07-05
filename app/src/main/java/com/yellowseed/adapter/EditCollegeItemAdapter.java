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
import com.yellowseed.model.EditCollegeItemModel;
import com.yellowseed.model.reqModel.SchoolModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.List;

public class EditCollegeItemAdapter extends RecyclerView.Adapter<EditCollegeItemAdapter.MyViewHolder> {

    private Context context;
    private List<SchoolModel> arlModel;
    private OnItemClickListener listener;
    private boolean isRequestFocus;
    private Themes themes;

    public EditCollegeItemAdapter(Context context, List<SchoolModel> arlModel, OnItemClickListener listener) {
        this.context = context;
        themes = new Themes(context);
        this.arlModel = arlModel;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EditCollegeItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.edit_school_item_layout,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull EditCollegeItemAdapter.MyViewHolder holder, int position) {
        Boolean darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        holder.binding.tvAddSchool.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        holder.binding.etSchool.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
       holder.binding.etSchool.setHintTextColor(ContextCompat.getColor(context, themes.setDarkEditProfileHint()));
        holder.binding.etSchool.setBackground(ContextCompat.getDrawable(context,themes.setDarkEditProfileBackground()));


        final SchoolModel model = arlModel.get(position);
        holder.binding.etSchool.setText(TextUtils.isEmpty(model.getCollege_name()) ? "" : model.getCollege_name());


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
        return arlModel.size();

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
                        } else {
                            CommonUtils.showToast(context, context.getString(R.string.enter_college_name));
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
                    arlModel.get(getAdapterPosition()).setCollege_name(s.toString().trim());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        }
    }
}
