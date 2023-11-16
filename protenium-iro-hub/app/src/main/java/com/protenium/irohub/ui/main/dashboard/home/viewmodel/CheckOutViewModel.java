package com.protenium.irohub.ui.main.dashboard.home.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.defaultAddress.SetDefaultAddressResponse;
import com.protenium.irohub.model.deleteAddress.DeleteAddressResponse;
import com.protenium.irohub.model.finalSub.AddFinalSubscriptionResponse;
import com.protenium.irohub.model.getAddress.GetAddressResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CheckOutViewModel extends BaseViewModel {

    public MutableLiveData<GetAddressResponse> getAddress(String userId, String lang) {
        MutableLiveData<GetAddressResponse> getAddressResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getUserAddresses(Integer.parseInt(userId), lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetAddressResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(GetAddressResponse getAddressResponse) {
                        setIsLoading(false);
                        getAddressResponseMutableLiveData.postValue(getAddressResponse);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                        setIsLoading(false);
                    }
                });
        return getAddressResponseMutableLiveData;
    }

    public MutableLiveData<DeleteAddressResponse> deleteAddress(int addres_id, String lang) {
        MutableLiveData<DeleteAddressResponse> deleteAddressReponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().deleteAddress(addres_id, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DeleteAddressResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(DeleteAddressResponse deleteAddressReponse) {
                        setIsLoading(false);
                        deleteAddressReponseMutableLiveData.postValue(deleteAddressReponse);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                        setIsLoading(false);
                    }
                });
        return deleteAddressReponseMutableLiveData;
    }

    public MutableLiveData<SetDefaultAddressResponse> setDefaultAddress(String userId, String addres_id, String lang) {
        MutableLiveData<SetDefaultAddressResponse> setDefaultAddressResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().setDefaultAddress(Integer.parseInt(userId), Integer.parseInt(addres_id), lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SetDefaultAddressResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(SetDefaultAddressResponse deleteAddressReponse) {
                        setIsLoading(false);
                        setDefaultAddressResponseMutableLiveData.postValue(deleteAddressReponse);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                        setIsLoading(false);
                    }
                });
        return setDefaultAddressResponseMutableLiveData;
    }

    public MutableLiveData<AddFinalSubscriptionResponse> addFinalSub(String user_id, int meal_plan_subscription_id, int payment_status, int adress_id, String payment_method, String promo_code, String lang_id, String payment_reference, String unique_key, int renewal) {
        MutableLiveData<AddFinalSubscriptionResponse> addFinalSubscriptionResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().addFinalSubscription(Integer.parseInt(user_id), meal_plan_subscription_id, payment_status, adress_id, payment_method, promo_code, lang_id, payment_reference, unique_key, renewal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AddFinalSubscriptionResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(AddFinalSubscriptionResponse addFinalSubscriptionResponse) {
                        setIsLoading(false);
                        addFinalSubscriptionResponseMutableLiveData.postValue(addFinalSubscriptionResponse);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                        setIsLoading(false);
                    }
                });
        return addFinalSubscriptionResponseMutableLiveData;
    }


}
