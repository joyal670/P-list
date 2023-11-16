package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.order_info;

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
import com.ashtechlabs.teleporter.adapters.DriverOrderInfoAdapter;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.DriverDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.order_info_detail.OrderDetailDriverActivity;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.MyLogsAndOrderInfoDriver;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.OnTDashFragmentInteractionListener;
import com.ashtechlabs.teleporter.util.CommonUtils;

/**
 * Created by IROID_ANDROID1 on 07-Feb-17.
 */

public class OrderInfoFragment extends Fragment implements IVewOrderClick {

    private DriverOrderInfoAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView tvHolder;
    String orderid, ordertype,id;
    int pos;
    private MyLogsAndOrderInfoDriver myLogsAndOrderInfoDriver;
    private OnTDashFragmentInteractionListener mListener;


    public OrderInfoFragment() {
        // Required empty public constructor
    }


    public static OrderInfoFragment newInstance() {
        return new OrderInfoFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_info_fragment, container, false);
        initViews(view);


        return view;
    }

    private void initViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_order);
        tvHolder = (TextView) view.findViewById(R.id.tvHolder);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new DriverOrderInfoAdapter(getActivity(), DriverDashBoardActivity.orderInfoList);
        adapter.setOnItemviewClickListner(this);
        recyclerView.setAdapter(adapter);

        if (adapter.getItemCount() > 0)
            tvHolder.setVisibility(View.GONE);
        else tvHolder.setVisibility(View.VISIBLE);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTDashFragmentInteractionListener) {
            mListener = (OnTDashFragmentInteractionListener) context;
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
    public void onViewOrder(MyLogsAndOrderInfoDriver myLogsAndOrderInfoDriver, int pos) {
        this.myLogsAndOrderInfoDriver = myLogsAndOrderInfoDriver;
        this.pos=pos;
        Intent i = new Intent(getActivity(), OrderDetailDriverActivity.class);
        i.putExtra("pos",pos);

        i.putExtra("orderinfolist",myLogsAndOrderInfoDriver);
        getActivity().startActivity(i);
    }


}
