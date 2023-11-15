package com.protenium.irohub.ui.main.dashboard.profile.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.subscription_history.GetSubscriptionHistoryResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SubscriptionHistoryViewModel extends BaseViewModel {

    public MutableLiveData<GetSubscriptionHistoryResponse> getSubscription(String userId, String lang) {
        MutableLiveData<GetSubscriptionHistoryResponse> responseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getSubscriptionHistory(Integer.parseInt(userId), lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetSubscriptionHistoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(GetSubscriptionHistoryResponse response) {
                        setIsLoading(false);
                        responseMutableLiveData.postValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: "+e );
                        setIsLoading(false);
                    }
                });
        return responseMutableLiveData;
    }


}
