package com.protenium.irohub.ui.main.dashboard.home.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.home.Banner;
import com.protenium.irohub.model.home.HomeResponse;
import com.protenium.irohub.model.home.MealCategory;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends BaseViewModel {

    public MutableLiveData<List<MealCategory>> getMeals(String lang) {
        MutableLiveData<List<MealCategory>> mealsItemListLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getHomeDetails(lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<HomeResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(HomeResponse homeResponse) {
                        setIsLoading(false);
                        mealsItemListLiveData.setValue(homeResponse.getData().getMealCategories());

                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                    }
                });
        return mealsItemListLiveData;
    }

    public MutableLiveData<List<Banner>> getBanner(String lang) {
        MutableLiveData<List<Banner>> bannerItemListLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getHomeDetails(lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<HomeResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(HomeResponse homeResponse) {
                        setIsLoading(false);
                        bannerItemListLiveData.setValue(homeResponse.getData().getBanners());

                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                    }
                });
        return bannerItemListLiveData;
    }


}
