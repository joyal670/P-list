package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.map_controller.IMapFragmentController;
import com.ashtechlabs.teleporter.ui.dashboard.map_controller.MapFragmentController;
import com.ashtechlabs.teleporter.ui.dashboard.map_controller.MapFragmentControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;
import com.ashtechlabs.teleporter.util.CommonUtils;

import java.util.List;
import java.util.Locale;


public class BookingDetailVendorActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        MapFragmentControllerCallback {

    private static final int MAP_PERMISSION_CODE = 1001;

    private TextView txtOrderNum;
    private TextView txtTime;
    private TextView txtDate;
    private TextView txtLabel;
    private TextView txtItemType;
    private TextView txtLocationFrom;
    private TextView txtLocationTo;
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();
    private IMapFragmentController IMapFragmentController;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GoogleMap google_map;
    private String fromLocation, toLocation;
    private String[] commodityArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingpartner);
        MapsInitializer.initialize(getApplicationContext());

        builder = new LatLngBounds.Builder();

        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        commodityArray = getResources().getStringArray(R.array.array_commodity_copy);

        IMapFragmentController = new MapFragmentController(this);
        initUI();
        displayData();
    }

    private void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private void displayData() {

        VendorList vendorList = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vendorList = (VendorList) extras.getSerializable("my_log");
        }

        try {
            txtOrderNum.setText(vendorList.getOrderid());
            txtDate.setText(vendorList.getDate().substring(0, 9));
            txtTime.setText(vendorList.getDate().substring(10));
            txtItemType.setText(vendorList.getCommodityType());
            txtLocationFrom.setText(vendorList.getFromLocation());
            fromLocation = vendorList.getFromLocation();
            txtLocationTo.setText(vendorList.getToLocation());
            toLocation = vendorList.getToLocation();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void initUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Booking Detail Vendor");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        txtOrderNum = (TextView) findViewById(R.id.txtOrderNum);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtLabel = (TextView) findViewById(R.id.txtLabel);
        txtItemType = (TextView) findViewById(R.id.txtItemType);
        txtLocationFrom = (TextView) findViewById(R.id.txtLocationFrom);
        txtLocationTo = (TextView) findViewById(R.id.txtLocationTo);

        findViewById(R.id.btnSubmitLocation).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isMapPermissionGranted()) {
                    displayDirverLocation();
                } else {
                    requestMapPermission();
                }
            }
        });

        FragmentManager myFragmentManager = getSupportFragmentManager();
        SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager
                .findFragmentById(R.id.map);
        mySupportMapFragment.getMapAsync(this);

    }

    private void showLocation() {

        if(fromLocation!=null && fromLocation.length()>0){
            IMapFragmentController.getFromLatLng(fromLocation);
        }
        if(toLocation!=null && toLocation.length()>0){
            IMapFragmentController.getToLatLng(toLocation);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.google_map = googleMap;
        if (google_map != null) {
            showLocation();
        }

    }


    public LatLng getLatLongFromGivenAddress(String address) {

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {

                Log.d("Latitude", "" + addresses.get(0).getLatitude());
                Log.d("Longitude", "" + addresses.get(0).getLongitude());

                return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (isMapPermissionGranted()) {
            displayDirverLocation();
        } else {
            requestMapPermission();
        }

    }

    private boolean isMapPermissionGranted() {

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    private void requestMapPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == MAP_PERMISSION_CODE) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                displayDirverLocation();
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //allowed

                } else {
                    //set to never ask again
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
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


    private void displayDirverLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            IMapFragmentController.onUpdateVendorLocation(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""), String.valueOf(latitude), String.valueOf(longitude));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onGetMapDetails(String message) {

        CommonUtils.showToast(getApplicationContext(), message);

        google_map.addMarker(new MarkerOptions()
                .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_car))
        );
    }

    @Override
    public void onGetFromLatLng(LatLng from) {
        google_map.addMarker(new MarkerOptions()
                .position(from)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_start))
        );

        builder.include(from);
    }

    @Override
    public void onGetToLatLng(LatLng to) {
        google_map.addMarker(new MarkerOptions()
                .position(to)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_end))
        );

        builder.include(to);
    }

    @Override
    public void onGetMapDetailsFailed(String message) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showLoadingIndicator() {
        showProgress();
    }

    @Override
    public void dismissLoadingIndicator() {
        dismissProgress();
    }
}
