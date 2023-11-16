package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.order_info;

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
import com.ashtechlabs.teleporter.adapters.VendorOrderInfoAdapter;
import com.ashtechlabs.teleporter.ui.OnFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.OnOrderInfoAdapterCallback;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.CargoDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.order_info_detail.OrderDetailVendorActivity;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;
import com.ashtechlabs.teleporter.util.CommonUtils;

public class PartnerOrderInfoFragment extends Fragment implements OnOrderInfoAdapterCallback, IVewOrderClickVendor {
    LinearLayoutManager linearLayoutManager;
    private VendorOrderInfoAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvHolder;
    private OnFragmentInteractionListener mListener;
    int pos;

    public PartnerOrderInfoFragment() {
        // Required empty public constructor
    }


    public static PartnerOrderInfoFragment newInstance() {

        return new PartnerOrderInfoFragment();
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


        adapter = new VendorOrderInfoAdapter(getActivity(), CargoDashBoardActivity.mOrders);
        adapter.setOnItemviewClickListner(this);
        recyclerView.setAdapter(adapter);

        if (adapter.getItemCount() > 0)
            tvHolder.setVisibility(View.GONE);
        else tvHolder.setVisibility(View.VISIBLE);

        return view;
    }

    private void initViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_order);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
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
    public void onViewOrder(VendorList vendorList, int pos) {
        Intent i = new Intent(getActivity(), OrderDetailVendorActivity.class);
        i.putExtra("pos",pos);
        i.putExtra("vendor_order_info", vendorList);
        getActivity().startActivity(i);
    }

}
