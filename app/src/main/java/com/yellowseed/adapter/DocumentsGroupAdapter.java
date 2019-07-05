package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.databinding.LayoutDocumentsBinding;
import com.yellowseed.databinding.LayoutNotificationsBinding;
import com.yellowseed.model.DocumentsModel;
import com.yellowseed.model.NotificationLayoutModel;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

public class DocumentsGroupAdapter extends RecyclerView.Adapter<DocumentsGroupAdapter.MyViewHolder> {
    private ArrayList<DocumentsModel> models;
    private Context context;

    public DocumentsGroupAdapter(ArrayList<DocumentsModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_documents, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       // holder.binding.tvDocumentDayTime.setText(models.get(position).getDayName());
        holder.binding.tvDocumentDayTime.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvdocumentPdf.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvdocumentWord.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LayoutDocumentsBinding binding;
        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
