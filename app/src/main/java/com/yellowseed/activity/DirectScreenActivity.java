package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.DirectAdapter;
import com.yellowseed.databinding.ActivityDirectScreenBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.DirectModel;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class DirectScreenActivity extends BaseActivity {
    private ActivityDirectScreenBinding binding;
    private Context context;
    private ArrayList<DirectModel> models;
    private  DirectAdapter adapter;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Tiny Tim", "Tiny Tim", "Tiny Tim", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon4, R.mipmap.profile_icon2, R.mipmap.profile_icon2, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon2};
    private String[] time = {"2 h ago", "1 h ago", "3 h ago", "monday", "monday", "monday", "monday", "monday", ""};
    private String[] message = {"i am good", "hi, how are you?", " you are so cute", "Dear friend... meet me", "Dear friend... meet me", "Dear friend... meet me", "Dear friend... meet me", "", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_direct_screen);
        context = DirectScreenActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();

        directRecyclerView();

        binding.etDirectSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setCurrentTheme() {
        binding.toolbarDirect.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.toolbarDirect.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.etDirectSearch.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.tvPendingRequest.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setSeeAll()));
        binding.tvDirectNewGroup.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setSeeAll()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));

        Themes.getInstance(context).changeIconColor(context, binding.toolbarDirect.ivLeft);
        Themes.getInstance(context).changeRightIcon(context, binding.toolbarDirect.ivEdit);
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.etDirectSearch.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkStoryBackground()));

    }


    public void filter(String text){

        ArrayList<DirectModel> filteredName = new ArrayList<>();

        for (DirectModel model : models){
            if (model.getName_url().toLowerCase().contains(text.toLowerCase())){
                filteredName.add(model);
            }
        }
        adapter.updatedList(filteredName);

    }

    private void directRecyclerView() {
        //binding.fastScroller.setRecyclerView(binding.rvDirect);
        binding.rvDirect.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvDirect.setLayoutManager(layoutManager);

        //For divide the recycler item
      /*  DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvDirect.addItemDecoration(itemDecorator);

*/
        models = new ArrayList<>();
        models.addAll(prepareData());
         adapter = new DirectAdapter(models, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llDirectUser:
                       // ActivityController.startActivity(context, ChatScreenActivity.class);
                        ActivityController.startActivity(context, ChatsScreenFrgActivity.class);
                        break;
                        default:
                            break;
                }
            }
        });
        binding.rvDirect.setAdapter(adapter);
    }

    private ArrayList<DirectModel> prepareData() {
        ArrayList<DirectModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            DirectModel model = new DirectModel();
            model.setImage_url(images[i]);
            model.setName_url(names[i]);
            model.setTime_ago_url(time[i]);
            model.setLast_message_url(message[i]);
            data.add(model);
        }
        return data;
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void setToolBar() {


        binding.toolbarDirect.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarDirect.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarDirect.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarDirect.tvHeader.setText(R.string.directshare);
        binding.toolbarDirect.ivRight.setVisibility(View.GONE);
        binding.toolbarDirect.ivEdit.setVisibility(View.VISIBLE);

    }

    @Override
    public void setOnClickListener() {
        binding.toolbarDirect.ivLeft.setOnClickListener(this);
        binding.toolbarDirect.ivRight.setOnClickListener(this);
        binding.toolbarDirect.ivEdit.setOnClickListener(this);
binding.tvDirectNewGroup.setOnClickListener(this);
        binding.tvPendingRequest.setOnClickListener(this);
        binding.tvDirectNewGroup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;

            case R.id.ivEdit:
                ActivityController.startActivity(context, DirectUserAddActivity.class);
                break;

            case R.id.tvDirectNewGroup:
                ActivityController.startActivity(context,NewGroupNextActivity.class);
                break;

            default:
                break;
        }
    }


}
