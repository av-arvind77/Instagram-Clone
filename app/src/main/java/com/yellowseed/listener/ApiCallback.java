package com.yellowseed.listener;

import com.yellowseed.webservices.ApiResponse;

public interface ApiCallback {
    void onSuccess(ApiResponse apiResponse);
    void onFailure(ApiResponse apiResponse);
}
