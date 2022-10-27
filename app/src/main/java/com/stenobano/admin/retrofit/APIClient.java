package com.stenobano.admin.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.stenobano.admin.config.BaseUrl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIClient {


    private  static APIInterface api_;

    public  static APIInterface getInstance()
    {
        if(api_ ==null)
        {
            OkHttpClient okHttpClient= new OkHttpClient().newBuilder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit= new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(BaseUrl.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            APIInterface api_ =  retrofit.create(APIInterface.class);
            return api_;

        }
        else
            return api_;
    }
}
