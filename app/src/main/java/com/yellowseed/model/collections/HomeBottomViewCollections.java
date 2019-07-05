package com.yellowseed.model.collections;

import com.yellowseed.R;
import com.yellowseed.model.HomeBottomViewModel;

import java.util.ArrayList;

public class HomeBottomViewCollections {
    public static ArrayList<HomeBottomViewModel> getPostItem() {
        ArrayList<HomeBottomViewModel> homeBottomViewModels = new ArrayList<>();

        HomeBottomViewModel homeBottomViewModel = new HomeBottomViewModel();

        homeBottomViewModel.setProfileImage(R.mipmap.profile_icon);
        homeBottomViewModel.setPostUserName("Jenny");
        homeBottomViewModel.setPostTime("Today at 3:00Pm");
        homeBottomViewModel.setPostContent("One small positive thought in the morning can change the entire outcome of your day!");
        homeBottomViewModel.setPostImage(R.mipmap.group_background);
        homeBottomViewModel.setPostLike("11");
        homeBottomViewModel.setPostComment("22");
        homeBottomViewModel.setPostShare("33");

        homeBottomViewModels.add(homeBottomViewModel);

        homeBottomViewModel = new HomeBottomViewModel();

        homeBottomViewModel.setProfileImage(R.mipmap.profile_icon2);
        homeBottomViewModel.setPostUserName("Renmark");
        homeBottomViewModel.setPostTime("Today at 9:00PM");
        homeBottomViewModel.setPostContent("Today, wherever you go, carry the intention of peace, love, and harmony in your heart.\n" +
                "\n" +
                "Best Positive Thoughts And More");
        homeBottomViewModel.setPostImage(R.mipmap.group_background);
        homeBottomViewModel.setPostLike("11");
        homeBottomViewModel.setPostComment("22");
        homeBottomViewModel.setPostShare("33");

        homeBottomViewModels.add(homeBottomViewModel);

        homeBottomViewModel = new HomeBottomViewModel();


        homeBottomViewModel.setProfileImage(R.mipmap.profile_icon3);
        homeBottomViewModel.setPostUserName("Peter");
        homeBottomViewModel.setPostTime("Today at 8:00PM");
        homeBottomViewModel.setPostContent("Today, wherever you go, carry the intention of peace, love, and harmony in your heart.\n" +
                "\n" +
                "Best Positive Thoughts And More");
        homeBottomViewModel.setPostImage(R.mipmap.group_background);
        homeBottomViewModel.setPostLike("11");
        homeBottomViewModel.setPostComment("22");
        homeBottomViewModel.setPostShare("33");

        homeBottomViewModels.add(homeBottomViewModel);

        homeBottomViewModel = new HomeBottomViewModel();


        homeBottomViewModel.setProfileImage(R.mipmap.profile_icon4);
        homeBottomViewModel.setPostUserName("Daziel");
        homeBottomViewModel.setPostTime("Today at 4:00PM");
        homeBottomViewModel.setPostContent("One small positive thought in the morning can change the entire outcome of your day!");
        homeBottomViewModel.setPostImage(R.mipmap.group_background);
        homeBottomViewModel.setPostLike("11");
        homeBottomViewModel.setPostComment("22");
        homeBottomViewModel.setPostShare("33");

        homeBottomViewModels.add(homeBottomViewModel);

        homeBottomViewModel = new HomeBottomViewModel();


        homeBottomViewModel.setProfileImage(R.mipmap.profile_icon);
        homeBottomViewModel.setPostUserName("Dolly");
        homeBottomViewModel.setPostTime("Today at 11:00PM");
        homeBottomViewModel.setPostContent("Today, wherever you go, carry the intention of peace, love, and harmony in your heart.\n" +
                "\n" +
                "Best Positive Thoughts And More");
        homeBottomViewModel.setPostImage(R.mipmap.group_background);
        homeBottomViewModel.setPostLike("11");
        homeBottomViewModel.setPostComment("22");
        homeBottomViewModel.setPostShare("33");


        homeBottomViewModels.add(homeBottomViewModel);


        return homeBottomViewModels;


    }
}
