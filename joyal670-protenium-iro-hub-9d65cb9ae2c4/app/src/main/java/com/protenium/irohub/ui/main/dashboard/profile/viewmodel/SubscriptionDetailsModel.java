package com.protenium.irohub.ui.main.dashboard.profile.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.subscription_details.SubscriptionDetailsResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SubscriptionDetailsModel extends BaseViewModel {

    public MutableLiveData<SubscriptionDetailsResponse> getSubscription(String meal_plan_subscription_id, String lang) {
        MutableLiveData<SubscriptionDetailsResponse> responseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getSubscriptionPreviewDetails(Integer.parseInt(meal_plan_subscription_id), lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SubscriptionDetailsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(SubscriptionDetailsResponse response) {
                        setIsLoading(false);
                        responseMutableLiveData.postValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                        setIsLoading(false);
                    }
                });
        return responseMutableLiveData;
    }


}
