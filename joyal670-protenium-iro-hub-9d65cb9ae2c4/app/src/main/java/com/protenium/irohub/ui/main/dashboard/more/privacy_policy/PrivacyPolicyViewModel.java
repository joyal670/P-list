package com.protenium.irohub.ui.main.dashboard.more.privacy_policy;

import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.about_us.AboutUsResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PrivacyPolicyViewModel extends BaseViewModel {

    public MutableLiveData<AboutUsResponse> getPrivacyPolicy(String lang) {
        MutableLiveData<AboutUsResponse> responseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getPrivacyPolicy(lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AboutUsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(AboutUsResponse response) {
                        setIsLoading(false);
                        responseMutableLiveData.postValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                    }
                });
        return responseMutableLiveData;
    }

}
