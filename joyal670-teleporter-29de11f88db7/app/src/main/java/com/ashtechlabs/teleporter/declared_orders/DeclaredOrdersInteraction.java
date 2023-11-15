package com.ashtechlabs.teleporter.declared_orders;

import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.DeclaredOrders;

import java.util.ArrayList;

/**
 * Created by VIDHU on 1/19/2017.
 */

public interface DeclaredOrdersInteraction {

    void getDeclaredOrders(int mode, String token, OnDeclaredOrdersListener listener);

    interface OnDeclaredOrdersListener {

        void onFail(String message);

        void showProgress();

        void dismissProgress();

        void onSuccess(ArrayList<DeclaredOrders> declaredOrders);
    }
}
