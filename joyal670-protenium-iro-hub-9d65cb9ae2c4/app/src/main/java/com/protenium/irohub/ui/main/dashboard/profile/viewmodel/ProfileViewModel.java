package com.protenium.irohub.ui.main.dashboard.profile.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.profile.changePassword.ChangePasswordResponse;
import com.protenium.irohub.model.profile.profileDetails.GetProfileDetailsResponse;
import com.protenium.irohub.model.profile.profileUpdate.UpdateProfileResponse;

import java.io.File;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileViewModel extends BaseViewModel {

    public MutableLiveData<GetProfileDetailsResponse> getProfileDetails(String userId, String lang) {
        MutableLiveData<GetProfileDetailsResponse> responseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getUserInfo(Integer.parseInt(userId), lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetProfileDetailsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(GetProfileDetailsResponse response) {
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

    public MutableLiveData<UpdateProfileResponse> updateCustomerProfile(String id,
                                                                        String name,
                                                                        String gender,
                                                                        String phone,
                                                                        String lang_id,
                                                                        String alternative_phone,
                                                                        File compressedImage) {
        MutableLiveData<UpdateProfileResponse> responseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().updateUserInfo(
                RequestBody.create(MediaType.parse("*/"), id),
                RequestBody.create(MediaType.parse("*/"), name),
                RequestBody.create(MediaType.parse("*/"), gender),
                RequestBody.create(MediaType.parse("*/"), phone),
                RequestBody.create(MediaType.parse("*/"), lang_id),
                RequestBody.create(MediaType.parse("*/"), alternative_phone),
                compressedImage != null ? MultipartBody.Part.createFormData("image", compressedImage.getName(),
                        RequestBody.create(MediaType.parse("*/"), compressedImage)) : null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UpdateProfileResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(UpdateProfileResponse responseObj) {
                        setIsLoading(false);
                        responseMutableLiveData.postValue(responseObj);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                    }
                });

        return responseMutableLiveData;
    }

    public MutableLiveData<ChangePasswordResponse> updatePassword(String email, String password, String confirm_password, String lang) {
        MutableLiveData<ChangePasswordResponse> responseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().updatePassword(email, password, confirm_password, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ChangePasswordResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(ChangePasswordResponse response) {
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
