package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.warehouse_order_info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.WarehouseOrderInfoAdapter;
import com.ashtechlabs.teleporter.ui.OnFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.OnOrderInfoAdapterCallback;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.WareHouseDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseList;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.order_info_detail.OrderInfoDetailsWareHouse;
import com.ashtechlabs.teleporter.util.CommonUtils;

public class WarehouseOrderInfoFragment extends Fragment implements  OnOrderInfoAdapterCallback, IVewWareHouseOrderClick {
    private  WarehouseOrderInfoAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private OnFragmentInteractionListener mListener;
    private TextView tvHolder;
    private WareHouseList wareHouseList;
    int pos;

    public WarehouseOrderInfoFragment() {
        // Required empty public constructor
    }


    public static WarehouseOrderInfoFragment newInstance() {
        return new WarehouseOrderInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.order_info_fragment, container, false);
        initViews(view);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WarehouseOrderInfoAdapter(getActivity(), WareHouseDashBoardActivity.mOrders);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemviewClickListner(this);

        if (adapter.getItemCount() > 0)
            tvHolder.setVisibility(View.GONE);
        else tvHolder.setVisibility(View.VISIBLE);

        return view;
    }

    private void initViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_order);
        tvHolder = (TextView) view.findViewById(R.id.tvHolder);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTDashFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showProgressAdapter() {
        mListener.showLoadingIndicator();
    }

    @Override
    public void dismissProgressAdapter() {
        mListener.dismissLoadingIndicator();
    }

    @Override
    public void showMessageAlert(String title, String message) {
        CommonUtils.showAlertDialog(getActivity(), title, message);
    }

    @Override
    public void onViewOrder(WareHouseList wareHouseList, int pos) {
        this.wareHouseList = wareHouseList;
        this.pos=pos;
        Intent i = new Intent(getActivity(), OrderInfoDetailsWareHouse.class);
        i.putExtra("pos",pos);

        i.putExtra("orderinfolist", wareHouseList);
        getActivity().startActivity(i);
    }


}
