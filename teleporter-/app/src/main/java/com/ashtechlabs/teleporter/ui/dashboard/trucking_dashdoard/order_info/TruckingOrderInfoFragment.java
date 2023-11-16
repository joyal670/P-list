package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.order_info;

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
import com.ashtechlabs.teleporter.adapters.TruckingOrderInfoAdapter;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.OnTDashFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.TruckingDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.order_info_detail.OrderDetailTruckingActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingMpService;
import com.ashtechlabs.teleporter.util.CommonUtils;

/**
 * Created by IROID_ANDROID1 on 07-Feb-17.
 */

public class TruckingOrderInfoFragment extends Fragment implements ITruckingVewOrderClick {

    private TruckingOrderInfoAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView tvHolder;
    String orderid, ordertype,id;
    int pos;
    private TruckingMpService myLogsAndOrderInfoDriver;
    private OnTDashFragmentInteractionListener mListener;


    public TruckingOrderInfoFragment() {
        // Required empty public constructor
    }


    public static TruckingOrderInfoFragment newInstance() {
        return new TruckingOrderInfoFragment();
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
        adapter = new TruckingOrderInfoAdapter(getActivity(), TruckingDashBoardActivity.orderInfoList);
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
    public void onViewOrder(TruckingMpService myLogsAndOrderInfoDriver, int pos) {
        this.myLogsAndOrderInfoDriver = myLogsAndOrderInfoDriver;
        this.pos=pos;
        Intent i = new Intent(getActivity(), OrderDetailTruckingActivity.class);
        i.putExtra("pos",pos);

        i.putExtra("orderinfolist",myLogsAndOrderInfoDriver);
        getActivity().startActivity(i);
    }


}
