package com.ashtechlabs.teleporter.retrofit;


import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by IROID_ANDROID1 on 18-Jan-17.
 */

public class RetroClient {

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
  //private static final String ROOT_URL = "http://162.144.93.119/~teleport/apaladin1993/CargoBooking/index.php/CargoBookingService/";
    public static final String ROOT_URL = "http://3.6.98.217/CargoBookingNew/index.php/CargoBookingService_Test/";

    // private static final String ROOT_URL = "http://iroidtech.com/CargoBooking/index.php/CargoBookingService/";
    /**
     * Get Retrofit Instance
     */

    static OkHttpClient.Builder httpClient = null;


    private static Retrofit getRetrofitInstance() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }


    public static RequestBody createRequestBody(@NonNull File file) {
        return RequestBody.create(
                MediaType.parse("text/plain"), file);
    }

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(
                MediaType.parse("*/*"), s);
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
