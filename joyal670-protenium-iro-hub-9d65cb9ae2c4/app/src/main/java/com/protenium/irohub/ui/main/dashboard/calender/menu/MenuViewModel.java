package com.protenium.irohub.ui.main.dashboard.calender.menu;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.menu.GetMenuResponse;
import com.protenium.irohub.model.place_order.OrderPlaceResponse;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MenuViewModel extends BaseViewModel {

    public MutableLiveData<GetMenuResponse> getMenuForDay(String userId, String date, String lang) {
        MutableLiveData<GetMenuResponse> getMenuResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getMenuForDay(Integer.parseInt(userId), date, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetMenuResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(GetMenuResponse response) {
                        setIsLoading(false);
                        getMenuResponseMutableLiveData.postValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                        Log.e("TAG", "onError: " + e);
                    }

                });
        return getMenuResponseMutableLiveData;
    }

    public MutableLiveData<OrderPlaceResponse> getPlaceOrder(String userId, String date, List<Integer> foodId, String lang, String mealId) {
        MutableLiveData<OrderPlaceResponse> orderPlaceResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getPlaceOrder(Integer.parseInt(userId), date, foodId, lang, mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<OrderPlaceResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(OrderPlaceResponse response) {
                        setIsLoading(false);
                        orderPlaceResponseMutableLiveData.postValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                        Log.e("TAG", "onError: " + e);
                    }

                });
        return orderPlaceResponseMutableLiveData;
    }


}
