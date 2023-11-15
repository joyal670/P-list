package com.ashtechlabs.teleporter;

import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.google.android.libraries.places.api.Places;
import com.zopim.android.sdk.api.ZopimChat;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by VIDHU on 1/11/2017.
 */

public class TeleporterApp extends MultiDexApplication {

    public static final String BASE_URL = "http://app.teleport.ooo/CargoBookingNew/index.php/CargoBookingService_Test/";
    //public static final String BASE_URL = "http://162.144.93.119/~teleport/apaladin1993/CargoBookingNew/index.php/CargoBookingService_Test/";
    //public static final String BASE_URL = "http://162.144.93.119/~teleport/apaladin1993/CargoBookingNew/index.php/CargoBookingService_Test/";
    //public static final String BASE_URL = "http://iroidtech.com/CargoBooking/index.php/CargoBookingService_Test/";
    static OkHttpClient.Builder httpClient = null;
    private static Retrofit retrofit = null;
    private static TeleporterApp instance;

    public static String[] ARRAY_DELIVERY;
    public static String[] ARRAY_COMMODITY;
    //public static String[] ARRAY_MOVER;

    public static synchronized TeleporterApp getInstance() {
        return instance;
    }


    public static Retrofit getMapClient() {
        String MAP_URL = "https://maps.googleapis.com/";


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MAP_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

//    public static Retrofit getMapClient() {
//        String MAP_URL = "https://maps.google.com/maps/api/";
//
//
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(MAP_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(httpClient.build())
//                    .build();
//        }
//        return retrofit;
//    }

    public static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        ZopimChat.init("4fzkwHIJZK32RxU63H3B0X1hIla9fVpw");
        Fabric.with(this, new Crashlytics());

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);// <-- this is the important line!

        ARRAY_DELIVERY = getApplicationContext().getResources().getStringArray(R.array.array_delivery);
        ARRAY_COMMODITY = getApplicationContext().getResources().getStringArray(R.array.array_commodity_copy);
        //ARRAY_MOVER = getApplicationContext().getResources().getStringArray(R.array.array_driver_service);

        instance = this;

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.US);
        }
    }

}










