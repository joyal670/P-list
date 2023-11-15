package com.protenium.irohub.ui.main.dashboard.calender;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.SubscriptionInfo.SubscriptionResponse;
import com.protenium.irohub.model.common_response.CommonResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CalenderViewModel extends BaseViewModel {

    public MutableLiveData<SubscriptionResponse> getSubscription(String userId, String lang) {
        MutableLiveData<SubscriptionResponse> subscriptionResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getSubscriptionInfo(Integer.parseInt(userId), lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SubscriptionResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(SubscriptionResponse response) {
                        setIsLoading(false);
                        subscriptionResponseMutableLiveData.postValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                        Log.e("TAG", "onError: " + e);
                    }

                });
        return subscriptionResponseMutableLiveData;
    }

    public MutableLiveData<CommonResponse> getSuspendUnsuspendDelivery(String userId, String date, String lang) {
        MutableLiveData<CommonResponse> commonResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().suspendUnsuspendDelivery(Integer.parseInt(userId), date, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CommonResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(CommonResponse response) {
                        setIsLoading(false);
                        commonResponseMutableLiveData.postValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                        Log.e("TAG", "onError: " + e);
                    }

                });
        return commonResponseMutableLiveData;
    }


}
