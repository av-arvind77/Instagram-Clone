package com.yellowseed.webservices;

import android.content.Context;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.PrefManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiExecutor {

    /**
     * this method is used for calling  APIs
     */

    public static ApiService getClient(final Context mContext) {
//         =========== show the log message========
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        ========================================
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(120, TimeUnit.SECONDS);
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .addHeader(AppConstants.AUTHTOKEN,PrefManager.getInstance(mContext).getKeyAccessToken()/*"bd42da3c2206ae67b227149ffe6b9db9"*/)
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
        httpClient.addInterceptor(interceptor); // show the log
        OkHttpClient client = httpClient.build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        return retrofit.create(ApiService.class);
    }





    public static ApiService getMultipartClient() {
//         =========== show the log message========
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        ========================================
        // concatenate username and password with colon for authentication
        String credentials = "admin" + ":" + "1234";
        // create Base64 encode string
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .addHeader("Authorization", basic)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
        httpClient.addInterceptor(interceptor); // show the log
        OkHttpClient client = httpClient.build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        return retrofit.create(ApiService.class);
    }

    public static ApiService getMultipartClientWithHeader(final Context mContext) {
//         =========== show the log message========
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        ========================================
        // concatenate username and password with colon for authentication
        String credentials = "admin" + ":" + "1234";
        // create Base64 encode string
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .addHeader(AppConstants.AUTHTOKEN,PrefManager.getInstance(mContext).getKeyAccessToken())
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
        httpClient.addInterceptor(interceptor); // show the log
        OkHttpClient client = httpClient.build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        return retrofit.create(ApiService.class);
    }

}