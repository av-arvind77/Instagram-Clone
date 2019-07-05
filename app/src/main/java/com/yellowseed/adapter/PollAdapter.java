package com.yellowseed.adapter;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.databinding.RowPollDataBinding;
import com.yellowseed.model.MessageEvent;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.MyViewHolder> {

    private String pollId = "";
    private List<String> alPoll;
    private Context context;
    private Themes themes;

    public PollAdapter(Context context, List<String> alPoll, String pollId) {
        this.alPoll = alPoll;
        this.context = context;
        this.pollId = pollId;
        themes = new Themes(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_poll_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvRowData.setText(alPoll.get(position));
   holder.binding.tvRowData.setTextColor(ContextCompat.getColor(context, themes.setToolbarWhiteText()));

        holder.binding.tvRowData.setTextColor(ContextCompat.getColor(context, themes.setLightTheme()));
        holder.binding.llMain.setBackground(ContextCompat.getDrawable(context, themes.setPollUnSelectedDrawable()));

    }

    @Override
    public int getItemCount() {
        return alPoll.size();
    }

    private void giveVoteToPoll(int position) {


        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("poll_id", "" + pollId);
            jsonObject.addProperty("option", "" + position);

            Call<ApiResponse> call = ApiExecutor.getClient(context).giveVoteToPoll(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {


                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.body() != null) {
                        if (response.body().getResponseCode() == 200) {

                            if (response.body().getResponseMessage() != null && !response.body().getResponseMessage().equalsIgnoreCase("")) {
                                EventBus.getDefault().post(new MessageEvent("vote"));
                                CommonUtils.showToast(context, response.body().getResponseMessage());
                            }

                        } else {
                            CommonUtils.showToast(context, response.body().getResponseMessage());

                        }
                    } else {
                        CommonUtils.showToast(context, response.message());

                    }

                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastLong(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            CommonUtils.showToast(context, context.getString(R.string.internet_alert_msg));
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RowPollDataBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.llMain.setOnClickListener(v -> /*giveVoteToPoll(getAdapterPosition()+1)*/


                    ToastUtils.showToastShort(context, "working in progress...")
            );
        }
    }


}
