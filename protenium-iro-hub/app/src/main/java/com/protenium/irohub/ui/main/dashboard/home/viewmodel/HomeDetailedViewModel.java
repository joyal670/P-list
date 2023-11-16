package com.protenium.irohub.ui.main.dashboard.home.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.add_initial_sub.AddSubscriptionResponse;
import com.protenium.irohub.model.home_detailed.HomeDetailedResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeDetailedViewModel extends BaseViewModel {

    public MutableLiveData<HomeDetailedResponse> getMeals(String mealsId, String lang) {
        MutableLiveData<HomeDetailedResponse> mealsItemListLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getMealPlan(Integer.parseInt(mealsId), lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<HomeDetailedResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(HomeDetailedResponse homeDetailedResponse) {
                        setIsLoading(false);
                        mealsItemListLiveData.postValue(homeDetailedResponse);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                        setIsLoading(false);
                    }
                });
        return mealsItemListLiveData;
    }

    public MutableLiveData<AddSubscriptionResponse> getSubscription(String userId, String start_date, int meal_plan_id, String non_stop_delivery_price, String carbs, String carbs_price,
                                                                    String proteins, String proteins_price, String comments, int duration, String base_price, String code, String suspend,
                                                                    String lang_id, int enable_modification) {
        MutableLiveData<AddSubscriptionResponse> addSubscriptionResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().addInitialSubscriptionPlan(Integer.parseInt(userId), start_date, meal_plan_id, non_stop_delivery_price, carbs, carbs_price, proteins, proteins_price, comments, duration, base_price, code, suspend, lang_id, enable_modification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AddSubscriptionResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(AddSubscriptionResponse addSubscriptionResponse) {
                        setIsLoading(false);
                        addSubscriptionResponseMutableLiveData.postValue(addSubscriptionResponse);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                        setIsLoading(false);
                    }
                });
        return addSubscriptionResponseMutableLiveData;
    }


}
