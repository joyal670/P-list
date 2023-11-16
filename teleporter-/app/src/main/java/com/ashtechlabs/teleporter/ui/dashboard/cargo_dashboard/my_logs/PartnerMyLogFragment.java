package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.my_logs;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.VendorMyLogsAdapter;
import com.ashtechlabs.teleporter.common_interfaces.OnMyLogsVendorAdapterCallback;
import com.ashtechlabs.teleporter.ui.OnFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.CargoDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.BookingDetailDriverActivity;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.BookingDetailVendorActivity;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;
import com.ashtechlabs.teleporter.util.CommonUtils;

import java.util.ArrayList;

public class PartnerMyLogFragment extends Fragment implements OnMyLogsVendorAdapterCallback {

    private static final int MAP_PERMISSION_CODE = 1001;
    private VendorMyLogsAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EditText editText;
    private View noOrderFound;
    private OnFragmentInteractionListener mListener;
    private VendorList myLog;
    String text;

    public PartnerMyLogFragment() {
        // Required empty public constructor
    }


    public static PartnerMyLogFragment newInstance() {
        return new PartnerMyLogFragment();
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

    private void initViews(View view) {

        noOrderFound = view.findViewById(R.id.noDataFound);
        editText = (EditText) view.findViewById(R.id.search);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_order);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (CargoDashBoardActivity.mlogs != null && CargoDashBoardActivity.mlogs.size() > 0) {
                noOrderFound.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                adapter = new VendorMyLogsAdapter(getActivity(), CargoDashBoardActivity.mlogs);
                adapter.setOnItemviewClickListner(this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
        Log.e("onResume", ">>>>onResume");
        super.onResume();
    }


    private void addTextListener() {

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final ArrayList<VendorList> filteredList = new ArrayList<>();

                if (CargoDashBoardActivity.mlogs != null) {

                    for (int i = 0; i < CargoDashBoardActivity.mlogs.size(); i++) {
                        if (CargoDashBoardActivity.mlogs.get(i).getOrder_number().equals("")) {
                            text = CargoDashBoardActivity.mlogs.get(i).getOrderid().toLowerCase();
                        } else {
                            text = CargoDashBoardActivity.mlogs.get(i).getOrder_number().toLowerCase();
                        }
                        if (text.contains(query)) {

                            filteredList.add(CargoDashBoardActivity.mlogs.get(i));
                        }
                    }

                    adapter = new VendorMyLogsAdapter(getActivity(), filteredList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setOnItemviewClickListner(PartnerMyLogFragment.this);
                }

                if (!(filteredList.size() > 0)) {
                    noOrderFound.setVisibility(View.VISIBLE);
                } else {
                    noOrderFound.setVisibility(View.GONE);
                }

            }
        });
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
    public void onViewInMapClick(VendorList myLog) {
        this.myLog = myLog;

        if (isMapPermissionGranted()) {
            //If permission is already having then showing the toast
            Intent intent = new Intent(getActivity(), BookingDetailVendorActivity.class);
            intent.putExtra("my_log", myLog);
            startActivity(intent);
            //Existing the method with return
        } else {
            //If the app has not the permission then asking for the permission
            requestMapPermission();
        }

    }

    private void requestMapPermission() {

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION_CODE);
    }

    private boolean isMapPermissionGranted() {

        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == MAP_PERMISSION_CODE) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                //denied
                Log.e("denied", "permission");
            } else {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                    Intent intent = new Intent(getActivity(), BookingDetailDriverActivity.class);
                    intent.putExtra("my_log", myLog);
                    startActivity(intent);
                    Log.e("allowed", "permission");
                } else {
                    //set to never ask again
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("This permission is important to view map. Goto application Setting and enable location permission.")
                            .setTitle("Location Permission required");
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }
                            }

                    );
                    builder.setPositiveButton("APP SETTINGS", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            }


                    );
                    builder.show();
                    //do something here.
                }
            }
        }
    }

}
