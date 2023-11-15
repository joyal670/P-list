package com.protenium.irohub.ui.main.dashboard.more.contact_us;

import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.contactUs.ContactUsResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactUsViewModel extends BaseViewModel {

    public MutableLiveData<ContactUsResponse> getContactUs(String user_id, String name, String phone, String email, String message, String lang) {
        MutableLiveData<ContactUsResponse> responseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().contactUs(Integer.parseInt(user_id), name, phone, email, message, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ContactUsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(ContactUsResponse response) {
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
