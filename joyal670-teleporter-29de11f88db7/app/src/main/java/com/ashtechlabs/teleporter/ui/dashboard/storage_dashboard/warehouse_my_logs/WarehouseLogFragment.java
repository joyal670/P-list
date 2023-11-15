package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.warehouse_my_logs;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.WareHouseMyLogsAdapter;
import com.ashtechlabs.teleporter.ui.OnFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.OnMyLogsAdapterCallback;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.MyLogsAndOrderInfoDriver;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.WareHouseDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseList;
import com.ashtechlabs.teleporter.util.CommonUtils;

import java.util.ArrayList;

public class WarehouseLogFragment extends Fragment implements OnMyLogsAdapterCallback {

    private WareHouseMyLogsAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EditText editText;
    private View noOrderFound;
    private OnFragmentInteractionListener mListener;
    String text;
    public WarehouseLogFragment() {
        // Required empty public constructor
    }

    public static WarehouseLogFragment newInstance() {
        return new WarehouseLogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.driver_my_logs_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void addTextListener() {

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final ArrayList<WareHouseList> filteredList = new ArrayList<>();

                if (WareHouseDashBoardActivity.mlogs != null) {
                    for (int i = 0; i < WareHouseDashBoardActivity.mlogs.size(); i++) {


                        if (WareHouseDashBoardActivity.mlogs.get(i).getOrder_number().equals("")) {
                            text = WareHouseDashBoardActivity.mlogs.get(i).getOrderid().toLowerCase();
                        }else {
                            text = WareHouseDashBoardActivity.mlogs.get(i).getOrder_number().toLowerCase();
                        }


                        if (text.contains(query)) {

                            filteredList.add(WareHouseDashBoardActivity.mlogs.get(i));
                        }
                    }
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter = new WareHouseMyLogsAdapter(getActivity(), filteredList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setOnItemviewClickListner(WarehouseLogFragment.this);
                }


                if (!(filteredList.size() > 0)) {
                    noOrderFound.setVisibility(View.VISIBLE);
                } else {
                    noOrderFound.setVisibility(View.GONE);
                }

            }
        });
    }

    private void initViews(View view) {
        noOrderFound = view.findViewById(R.id.noDataFound);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_order);
        editText = (EditText) view.findViewById(R.id.search);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (WareHouseDashBoardActivity.mlogs != null && WareHouseDashBoardActivity.mlogs.size() > 0) {
                noOrderFound.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                adapter = new WareHouseMyLogsAdapter(getActivity(), WareHouseDashBoardActivity.mlogs);
                adapter.setOnItemviewClickListner(this);
                recyclerView.setAdapter(adapter);
                addTextListener();
            } else {
                noOrderFound.setVisibility(View.VISIBLE);
                editText.setVisibility(View.GONE);
            }

        }
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
    public void onViewInMapClick(MyLogsAndOrderInfoDriver myLogsAndOrderInfoDriver) {

    }
}
