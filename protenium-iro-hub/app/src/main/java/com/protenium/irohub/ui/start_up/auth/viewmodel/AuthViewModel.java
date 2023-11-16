package com.protenium.irohub.ui.start_up.auth.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.about_us.AboutUsResponse;
import com.protenium.irohub.model.forgotpassword.ForgotPasswordResponse;
import com.protenium.irohub.model.login.LoginResponse;
import com.protenium.irohub.model.register.RegisterResponse;
import com.protenium.irohub.shared_pref.SharedPrefs;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends BaseViewModel {

    /* LOGIN  */
    public MutableLiveData<LoginResponse> userLogin(String email, String password, String deviceToken) {
        MutableLiveData<LoginResponse> loginResponseMutableLiveData = new MutableLiveData<>();


        ApiClient.getApiInterface().customerLogin(email, password, deviceToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(LoginResponse loginResponse) {
                        setIsLoading(false);
                        loginResponseMutableLiveData.postValue(loginResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                    }
                });
        return loginResponseMutableLiveData;
    }


    /* FORGOT PASSWORD */
    public MutableLiveData<ForgotPasswordResponse> userForgotPassword(String email) {
        MutableLiveData<ForgotPasswordResponse> responseObjMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().forgotPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ForgotPasswordResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(ForgotPasswordResponse forgotPasswordResponse) {
                        setIsLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                    }
                });
        return responseObjMutableLiveData;
    }


    /* REGISTER USER */
    public MutableLiveData<RegisterResponse> userRegister(String name, String email, String phone, String alternative_phone, String password, String gender, int status, String lang_id, String device_token) {
        MutableLiveData<RegisterResponse> registerResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().customerSignup(name, email, phone, alternative_phone, password, gender, status, lang_id, device_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<RegisterResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(RegisterResponse registerResponse) {
                        setIsLoading(false);
                        registerResponseMutableLiveData.postValue(registerResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                    }
                });
        return registerResponseMutableLiveData;
    }

}
