package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.databinding.EditSchoolItemLayoutBinding;
import com.yellowseed.databinding.RowAddSchoolBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.reqModel.SchoolModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;

import java.util.List;

public class AddSchoolItemsAdapter extends RecyclerView.Adapter<AddSchoolItemsAdapter.MyViewHolder> {

    private List<SchoolModel> arlModel;
    private Context context;
    private String type;


    public AddSchoolItemsAdapter(Context context,String type, List<SchoolModel> arlModel) {
        this.arlModel = arlModel;
        this.context = context;
        this.type = type;

    }


    @NonNull
    @Override
    public AddSchoolItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_add_school, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddSchoolItemsAdapter.MyViewHolder holder, int position) {
        final SchoolModel model = arlModel.get(position);

        if(type.equalsIgnoreCase(AppConstants.SCHOOL_TYPE)) {
            holder.binding.tvSchool.setText(TextUtils.isEmpty(model.getSchool_name()) ? "" : model.getSchool_name());
        }
        if(type.equalsIgnoreCase(AppConstants.COLLEGE_TYPE)){
            holder.binding.tvSchool.setText(TextUtils.isEmpty(model.getCollege_name()) ? "" : model.getCollege_name());
        }
        if(type.equalsIgnoreCase(AppConstants.WORK_AT_TYPE))
        {
            holder.binding.tvSchool.setText(TextUtils.isEmpty(model.getWork_at()) ? "" : model.getWork_at());
        }



    }

    @Override
    public int getItemCount() {
            return arlModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowAddSchoolBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.tvTitle.setText(type);
        }
    }
}
