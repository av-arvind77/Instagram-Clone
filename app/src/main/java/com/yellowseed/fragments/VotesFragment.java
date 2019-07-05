package com.yellowseed.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.activity.HomeActivity;
import com.yellowseed.activity.VotesActivity;
import com.yellowseed.adapter.VotesAdapter;
import com.yellowseed.databinding.FragmentVotesBinding;


import com.yellowseed.R;
import com.yellowseed.listener.OnClickListener;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.VotesModel;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VotesFragment extends BaseFragment {
    private Context context;
    private FragmentVotesBinding  binding;
    private int[] img={R.mipmap.profile_icon2,R.mipmap.profile_icon,R.mipmap.profile_icon3,R.mipmap.profile_icon4};
    private String[] name={"Jenny Koul","Mike Keel","Julie Kite","Tiny Tim"};
    private VotesAdapter adapter;
    private List<VotesModel> arVotesList=new ArrayList<>();
    private OnItemClickListener listener;

    public VotesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_votes, container, false);

      initializedControl();

        return binding.getRoot();
    }

    @Override
    public void initializedControl() {
        setRecycler();

    }

    private void setRecycler() {
        binding.rvVotes.setHasFixedSize(true);
        LinearLayoutManager manager=new LinearLayoutManager(context);
        binding.rvVotes.setLayoutManager(manager);
        arVotesList.addAll(voterData());
        adapter=new VotesAdapter(context,arVotesList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        binding.rvVotes.setAdapter(adapter);
    }

    private List< VotesModel> voterData() {
        List<VotesModel> votesModels=new ArrayList<>();
        for(int i=0;i<img.length;i++)
        {
            VotesModel modelList=new VotesModel();
            modelList.setImg(img[i]);
            modelList.setName(name[i]);
            votesModels.add(modelList);
        }
        return votesModels;
    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }
}
