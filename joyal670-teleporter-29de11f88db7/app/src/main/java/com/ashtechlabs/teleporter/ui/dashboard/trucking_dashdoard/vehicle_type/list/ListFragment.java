package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.list;

import android.app.Activity;
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
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.item_deceration.VerticalSpaceItemDecoration;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.OnVehicleListItemClickListener;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.OnVehicleTypeFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.VehicleAdapter;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.pojo.Vehicles;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;

import java.util.ArrayList;


public class ListFragment extends Fragment implements IListFragmentControllerCallback, OnVehicleListItemClickListener {

    private RecyclerView rvVehicleType;
    private TextView tvHolder;
    private VehicleAdapter mVehicleAdapter;
    private OnVehicleTypeFragmentInteractionListener mListener;
    private IListFragmentController iListFragmentController;
    private ArrayList<Vehicles> mVehiclesList;

    public ListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iListFragmentController = new ListFragmentController(this);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_vehicle_type_list, container, false);
        initViews(view);
        bindViews();
        iListFragmentController.getTruckList(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING,""));
        return view;
    }

    private void bindViews() {
        rvVehicleType.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initViews(View view) {
        rvVehicleType = (RecyclerView) view.findViewById(R.id.rvVehicleType);
        rvVehicleType.addItemDecoration(new VerticalSpaceItemDecoration(12));
        tvHolder = (TextView) view.findViewById(R.id.tvHolder);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVehicleTypeFragmentInteractionListener) {
            mListener = (OnVehicleTypeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVehicleTypeFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.showFab(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDeleteTruck(String message) {

    }

    @Override
    public void onListTruck(ArrayList<Vehicles> mVehiclesList) {
        if(mVehiclesList.size()>0){
            tvHolder.setVisibility(View.GONE);
            this.mVehiclesList = mVehiclesList;
            mVehicleAdapter = new VehicleAdapter(getActivity(), mVehiclesList);
            rvVehicleType.setAdapter(mVehicleAdapter);
            mVehicleAdapter.setClickListener(this);
        }else {
            tvHolder.setVisibility(View.VISIBLE);

        }
    }



    @Override
    public void showErrorMessage(String message) {
        CommonUtils.showToast(getActivity(), message);
    }

    @Override
    public void onClick(View view, int position, String name, String vehicleType, String vehicleSubType) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.KEY_VEHICLE_TYPE_NAME,name);
        returnIntent.putExtra(Constants.KEY_VEHICLE_TYPE,vehicleType);
        returnIntent.putExtra(Constants.KEY_VEHICLE_SUB_TYPE,vehicleSubType);
        getActivity().setResult(Activity.RESULT_OK,returnIntent);
        getActivity().finish();
    }

    @Override
    public void showLoadingDialog(boolean show) {
        mListener.showProgressIndicator(show);
    }
}
