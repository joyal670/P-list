package com.protenium.irohub.data;

import com.protenium.irohub.shared_pref.SharedPrefs;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    public static final String Base_url = "https://iroidtechnologies.in/protienium/api/v1/";


    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient().build())
                .build();
    }

    private static OkHttpClient.Builder getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        logging.getLevel();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        System.setProperty("http.keepAlive", "false");
        httpClient.addInterceptor(logging);
        if (!(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_TOKEN, "").equals(""))) {
            httpClient.addInterceptor(chain -> {
                Request newRequest = chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer " +
                                SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_TOKEN, ""))
                        .addHeader("lang_id", "en")
                        .build();
                return chain.proceed(newRequest);
            });
        }
        return httpClient;
    }

    public static ApiInterface getApiInterface() {
        return getRetrofit().create(ApiInterface.class);
    }

}
