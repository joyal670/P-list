package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.list;

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
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.OnStorageListItemClickListener;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.OnStorageSpaceFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.SpaceAdapter;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.pojo.StorageSpace;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;

import java.util.ArrayList;


public class ListFragment extends Fragment implements IListFragmentControllerCallback, OnStorageListItemClickListener {

    private RecyclerView rvStorageSpace;
    private TextView tvHolder;
    private SpaceAdapter mSpaceAdapter;
    private OnStorageSpaceFragmentInteractionListener mListener;
    private IListFragmentController iListFragmentController;
    private ArrayList<StorageSpace> mStorageSpaceList;

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
        mStorageSpaceList = new ArrayList<>();
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_storage_space_list, container, false);
        initViews(view);
        bindViews();
        iListFragmentController.getWarehouseList(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));
        return view;
    }

    private void bindViews() {
        rvStorageSpace.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvStorageSpace.addItemDecoration(new VerticalSpaceItemDecoration(12));

    }

    private void initViews(View view) {
        rvStorageSpace = (RecyclerView) view.findViewById(R.id.rvStorageSpace);
        tvHolder = (TextView) view.findViewById(R.id.tvHolder);
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.showFab(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStorageSpaceFragmentInteractionListener) {
            mListener = (OnStorageSpaceFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVehicleTypeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDeleteWarehouse(String message) {

    }

    @Override
    public void onListWarehouse(ArrayList<StorageSpace> mStorageSpaceList) {
        if (mStorageSpaceList.size() > 0) {
            tvHolder.setVisibility(View.GONE);
            this.mStorageSpaceList = mStorageSpaceList;
            mSpaceAdapter = new SpaceAdapter(getActivity(), mStorageSpaceList);
            mSpaceAdapter.setClickListener(this);
            rvStorageSpace.setAdapter(mSpaceAdapter);
            //mSpaceAdapter.notifyDataSetChanged();
        } else {
            tvHolder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showErrorMessage(String message) {
        CommonUtils.showToast(getActivity(), message);
    }

    @Override
    public void onClick(View view, int position, StorageSpace storageSpace) {
        if (getActivity().getIntent().getStringExtra(Constants.KEY_WHICH_ACTIVITY).equals("profile")) {
            mListener.showDetailFragment(storageSpace);
        } else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.KEY_WARE_HOUSE_ID, storageSpace.getId());
            returnIntent.putExtra(Constants.KEY_WARE_HOUSE_NAME, storageSpace.getWarehouseName());
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
            getActivity().finish();
        }

    }

    @Override
    public void showLoadingDialog(boolean show) {
        if (mListener != null) {
            mListener.showProgressIndicator(show);
        }
    }
}
