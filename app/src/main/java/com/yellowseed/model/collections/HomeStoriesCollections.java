package com.yellowseed.model.collections;

import com.yellowseed.R;
import com.yellowseed.model.HomeStoriesModel;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

public class HomeStoriesCollections {
    public static List<HomeStoriesModel> getStoriesImage() {
        List<HomeStoriesModel> homeStoriesModels = new ArrayList<>();
        HomeStoriesModel homeStoriesModel = new HomeStoriesModel();

        homeStoriesModel.setStoriesImage(R.mipmap.profile_img2);
        homeStoriesModel.setStoriesUserName("Peter");
        homeStoriesModels.add(homeStoriesModel);

        homeStoriesModel = new HomeStoriesModel();

        homeStoriesModel.setStoriesImage(R.mipmap.profile_img2);
        homeStoriesModel.setStoriesUserName("Rekha");
        homeStoriesModels.add(homeStoriesModel);

        homeStoriesModel = new HomeStoriesModel();

        homeStoriesModel.setStoriesImage(R.mipmap.profile_img2);
        homeStoriesModel.setStoriesUserName("Daziel");
        homeStoriesModels.add(homeStoriesModel);

        homeStoriesModel = new HomeStoriesModel();

        homeStoriesModel.setStoriesImage(R.mipmap.profile_img2);
        homeStoriesModel.setStoriesUserName("Larry Page");
        homeStoriesModels.add(homeStoriesModel);

        homeStoriesModel = new HomeStoriesModel();

        homeStoriesModel.setStoriesImage(R.mipmap.profile_img2);
        homeStoriesModel.setStoriesUserName("Lara");
        homeStoriesModels.add(homeStoriesModel);

        homeStoriesModel = new HomeStoriesModel();

        homeStoriesModel.setStoriesImage(R.mipmap.profile_img2);
        homeStoriesModel.setStoriesUserName("Watson");
        homeStoriesModels.add(homeStoriesModel);

        homeStoriesModel = new HomeStoriesModel();

        homeStoriesModel.setStoriesImage(R.mipmap.profile_img2);
        homeStoriesModel.setStoriesUserName("Windson");
        homeStoriesModels.add(homeStoriesModel);

        homeStoriesModel = new HomeStoriesModel();

        homeStoriesModel.setStoriesImage(R.mipmap.profile_img2);
        homeStoriesModel.setStoriesUserName("Sachin");
        homeStoriesModels.add(homeStoriesModel);

        homeStoriesModel = new HomeStoriesModel();

        homeStoriesModel.setStoriesImage(R.mipmap.profile_img2);
        homeStoriesModel.setStoriesUserName("Jonny");
        homeStoriesModels.add(homeStoriesModel);

        homeStoriesModel = new HomeStoriesModel();

        homeStoriesModel.setStoriesImage(R.mipmap.profile_img2);
        homeStoriesModel.setStoriesUserName("Johnson");
        homeStoriesModels.add(homeStoriesModel);


        return homeStoriesModels;
    }
}
