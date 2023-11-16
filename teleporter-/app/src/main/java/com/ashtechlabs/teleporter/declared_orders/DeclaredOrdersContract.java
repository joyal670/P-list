package com.ashtechlabs.teleporter.declared_orders;

import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.DeclaredOrders;

import java.util.ArrayList;

/**
 * Created by VIDHU on 1/27/2017.
 */

public interface DeclaredOrdersContract {

    interface DeclaredOrdersView {

        void showProgress();

        void dismissProgress();

        void onLoadingItemFailed(String message);

        void setDeclaredOrders(ArrayList<DeclaredOrders> declaredOrders);
    }

    interface DeclaredOrdersPresenter {

        void getDeclaredOrders(int mode, String token);

        void onDestroy();
    }
}
