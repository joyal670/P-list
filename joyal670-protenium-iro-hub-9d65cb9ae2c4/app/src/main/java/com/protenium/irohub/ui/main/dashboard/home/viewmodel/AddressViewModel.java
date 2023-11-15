package com.protenium.irohub.ui.main.dashboard.home.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.addAddress.AddAddressResponse;
import com.protenium.irohub.model.getArea.GetAreaResponse;
import com.protenium.irohub.model.getGovernate.GetGovernateResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddressViewModel extends BaseViewModel {

    public MutableLiveData<GetGovernateResponse> getGonvernate(String lang) {
        MutableLiveData<GetGovernateResponse> getGovernateResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getGovernorates(lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetGovernateResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(GetGovernateResponse getGovernateResponse) {
                        setIsLoading(false);
                        getGovernateResponseMutableLiveData.postValue(getGovernateResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                        setIsLoading(false);
                    }
                });
        return getGovernateResponseMutableLiveData;
    }

    public MutableLiveData<GetAreaResponse> getArea(String govId, String lang) {
        MutableLiveData<GetAreaResponse> getAreaResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getAreas(govId, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetAreaResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(GetAreaResponse getAreaResponse) {
                        setIsLoading(false);
                        getAreaResponseMutableLiveData.postValue(getAreaResponse);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                        setIsLoading(false);
                    }
                });
        return getAreaResponseMutableLiveData;
    }

    public MutableLiveData<AddAddressResponse> addAddress(String user_id, int governorate_id, int area_id, String block, String avenue, String street, String building, String floor, String appartment, int default1, String lang_id) {
        MutableLiveData<AddAddressResponse> addAddressResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().addAddress(Integer.parseInt(user_id), governorate_id, area_id, block, avenue, street, building, floor, appartment, default1, lang_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AddAddressResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(AddAddressResponse addAddressResponse) {
                        setIsLoading(false);
                        addAddressResponseMutableLiveData.postValue(addAddressResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                        setIsLoading(false);
                    }
                });
        return addAddressResponseMutableLiveData;
    }


}
