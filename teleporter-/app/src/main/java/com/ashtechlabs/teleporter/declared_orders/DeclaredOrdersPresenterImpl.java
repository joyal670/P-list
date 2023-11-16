package com.ashtechlabs.teleporter.declared_orders;

import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.DeclaredOrders;

import java.util.ArrayList;

/**
 * Created by VIDHU on 1/19/2017.
 */

public class DeclaredOrdersPresenterImpl implements DeclaredOrdersContract.DeclaredOrdersPresenter, DeclaredOrdersInteraction.OnDeclaredOrdersListener {

    private DeclaredOrdersContract.DeclaredOrdersView declareOrdersView;
    private DeclaredOrdersInteraction declareOrderInteraction;

    public DeclaredOrdersPresenterImpl(DeclaredOrdersContract.DeclaredOrdersView declareOrdersView) {
        this.declareOrdersView = declareOrdersView;
        this.declareOrderInteraction = new DeclaredOrdersInteractionImpl();
    }


    @Override
    public void onFail(String message) {
        if (declareOrdersView != null) {
            declareOrdersView.onLoadingItemFailed(message);
        }
    }

    @Override
    public void showProgress() {
        declareOrdersView.showProgress();
    }

    @Override
    public void dismissProgress() {
        declareOrdersView.dismissProgress();
    }

    @Override
    public void onSuccess(ArrayList<DeclaredOrders> declaredOrders) {
        if (declareOrdersView != null) {
            declareOrdersView.setDeclaredOrders(declaredOrders);
        }
    }

    @Override
    public void getDeclaredOrders(int mode, String token) {
        declareOrderInteraction.getDeclaredOrders(mode, token, this);
    }

    @Override
    public void onDestroy() {

    }
}
