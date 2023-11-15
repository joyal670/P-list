package com.protenium.irohub.ui.main.dashboard.home.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.protenium.irohub.base.BaseViewModel;
import com.protenium.irohub.data.ApiClient;
import com.protenium.irohub.model.dislike.getdislike.GetDisLikeResponse;
import com.protenium.irohub.model.dislike.setDislike.SetDislikeResponse;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DisLikeViewModel extends BaseViewModel {

    public MutableLiveData<GetDisLikeResponse> getDisLike(String userId, String lang) {
        MutableLiveData<GetDisLikeResponse> getListMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().getTags(Integer.parseInt(userId), lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetDisLikeResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(GetDisLikeResponse getDisLikeResponse) {
                        setIsLoading(false);
                        getListMutableLiveData.setValue(getDisLikeResponse);

                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                    }
                });
        return getListMutableLiveData;
    }

    public MutableLiveData<SetDislikeResponse> setDisLike(String userId, List<Integer> selectedTagsList, String lang) {
        MutableLiveData<SetDislikeResponse> setDislikeResponseMutableLiveData = new MutableLiveData<>();
        ApiClient.getApiInterface().setDislikedTags(Integer.parseInt(userId), selectedTagsList, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SetDislikeResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setIsLoading(true);
                    }

                    @Override
                    public void onSuccess(SetDislikeResponse setDislikeResponse) {
                        setIsLoading(false);
                        setDislikeResponseMutableLiveData.setValue(setDislikeResponse);

                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                    }
                });
        return setDislikeResponseMutableLiveData;
    }


}
