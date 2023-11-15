package com.protenium.irohub;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.multidex.MultiDexApplication;
import com.protenium.irohub.shared_pref.SharedPrefs;


public class App extends MultiDexApplication implements ViewModelStoreOwner {

    public static final String TAG = App.class.getSimpleName();
    private final ViewModelStore viewModelStore = new ViewModelStore();

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefs.initializeSharedPrefs(this);
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }
}
