package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.detail;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.dialog_fragments.ImageViewDialogFragment;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.OnStorageSpaceFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.add.AddFragmentController;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.add.IAddFragmentController;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.add.IAddFragmentControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.pojo.StorageSpace;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.OnDatePickListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;


public class DetailFragment extends Fragment implements  View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, OnDatePickListener, IAddFragmentControllerCallback {


    private TextView tvAddWarehouseImage, tvExpiryInsurance, tvExpiryTradeLicence;
    private TextView etName, etCapacity, etSpaceAvailable;
    private TextView actLocation;
    private ImageView ivUploadInsurance, ivUploadWarehouseImage, ivUploadTradeLicence;
    private IAddFragmentController iAddFragmentController;

    private String warehousePath, insurancePath, tradeLicencePath;
    private int selectedItem;

    private ArrayList<String> arrayCommodity;
    private StorageSpace storageSpace;

    private OnStorageSpaceFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance() {
        return  new DetailFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayCommodity = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.array_commodity_copy)));
        if (getArguments() != null) {
            storageSpace = getArguments().getParcelable(Constants.KEY_STORAGE_SPACE);
        }
        iAddFragmentController = new AddFragmentController(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.showFab(false);
        mListener.setToolbarTitle("Add Warehouse");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_storage_space_detail, container, false);
        initView(view);
        bindView();
        return view;
    }

    private void bindView() {

        if (storageSpace != null) {
            etName.setText(storageSpace.getWarehouseName());
            actLocation.setText(storageSpace.getWarehouseLocation());
            tvExpiryInsurance.setText(storageSpace.getInsuranceExpDate());
            etCapacity.setText(storageSpace.getWarehouseCapacity());
            etSpaceAvailable.setText(storageSpace.getSpaceAvailable());
            tvExpiryInsurance.setText(storageSpace.getInsuranceExpDate());
            tvExpiryTradeLicence.setText(storageSpace.getTradeLicenseExpiryDate());
            Picasso.with(getActivity())
                    .load(Constants.IMAGE_PATH + storageSpace.getWarehousePicture())
                    .error(R.drawable.ic_camera)
                    .placeholder(R.drawable.ic_camera)
                    .into(ivUploadWarehouseImage);

            Picasso.with(getActivity())
                    .load(Constants.IMAGE_PATH + storageSpace.getInsuranceNumber())
                    .error(R.drawable.ic_camera)
                    .placeholder(R.drawable.ic_camera)
                    .into(ivUploadInsurance);

            Picasso.with(getActivity())
                    .load(Constants.IMAGE_PATH + storageSpace.getTradeLicense())
                    .error(R.drawable.ic_camera)
                    .placeholder(R.drawable.ic_camera)
                    .into(ivUploadTradeLicence);


        }
    }

    private void initView(View view) {
        actLocation = (TextView) view.findViewById(R.id.actLocation);
        tvExpiryInsurance = (TextView) view.findViewById(R.id.tvExpiryInsurance);
        tvExpiryTradeLicence = (TextView) view.findViewById(R.id.tvExpiryTradeLicence);
        etName = (TextView) view.findViewById(R.id.etName);
        etCapacity = (TextView) view.findViewById(R.id.etCapacity);
        etSpaceAvailable = (TextView) view.findViewById(R.id.etSpaceAvailable);
        ivUploadInsurance = (ImageView) view.findViewById(R.id.ivUploadInsurance);
        ivUploadInsurance.setOnClickListener(this);
        ivUploadWarehouseImage = (ImageView) view.findViewById(R.id.ivUploadWarehouseImage);
        ivUploadWarehouseImage.setOnClickListener(this);
        ivUploadTradeLicence = (ImageView) view.findViewById(R.id.ivUploadTradeLicence);
        ivUploadTradeLicence.setOnClickListener(this);
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
    public void setDate(String date, String tag) {

        if (tag.equals("expiry_insurance")) {
            tvExpiryInsurance.setText(date);
        }
        if (tag.equals("expiry_trade_licence")) {
            tvExpiryTradeLicence.setText(date);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private String saveToSd(Bitmap bm, String filename) {

        if (bm != null) {
            File sd = Environment.getExternalStorageDirectory();
            Log.d("pathsd", " " + sd.getAbsolutePath().toString());
            File dest = new File(sd, filename);
            Log.d("pathdest", " " + dest.getAbsolutePath().toString());
            try {
                FileOutputStream out = new FileOutputStream(dest);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Environment.getExternalStorageDirectory().toString() + File.separator + filename;
        }
        return "";
    }


    @Override
    public void warehouseAdded(String message) {
        CommonUtils.showToast(getActivity(), message);
    }

    @Override
    public void showLoadingDialog(boolean show) {
        mListener.showProgressIndicator(show);
    }

    @Override
    public void onClick(View v) {

        if (storageSpace == null)
            return;

        if (v.getId() == R.id.ivUploadInsurance) {
            ImageViewDialogFragment newFragment = ImageViewDialogFragment.newInstance(storageSpace.getInsuranceNumber());
            newFragment.show(getActivity().getSupportFragmentManager(), "image_view_dialog");
        }
        if (v.getId() == R.id.ivUploadTradeLicence) {
            ImageViewDialogFragment newFragment = ImageViewDialogFragment.newInstance(storageSpace.getTradeLicense());
            newFragment.show(getActivity().getSupportFragmentManager(), "image_view_dialog");
        }
        if (v.getId() == R.id.ivUploadWarehouseImage) {
            ImageViewDialogFragment newFragment = ImageViewDialogFragment.newInstance(storageSpace.getWarehousePicture());
            newFragment.show(getActivity().getSupportFragmentManager(), "image_view_dialog");
        }
    }
}
