package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard;

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

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.map_controller.IMapFragmentController;
import com.ashtechlabs.teleporter.ui.dashboard.map_controller.MapFragmentController;
import com.ashtechlabs.teleporter.ui.dashboard.map_controller.MapFragmentControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingMpService;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.GlobalUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class BookingDetailTruckingActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        MapFragmentControllerCallback {

    private static final int MAP_PERMISSION_CODE = 1001;

    TextView txtOrderNum;
    TextView txtTime;
    TextView txtDate;
    TextView txtLabel;
    TextView txtItemType;
    TextView txtLocationFrom;
    TextView txtLocationTo;
    LatLngBounds.Builder builder;
    IMapFragmentController IMapFragmentController;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private GoogleMap google_map;
    private String fromLocation, toLocation;
    private String[] commodityArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingdriver);
        MapsInitializer.initialize(getApplicationContext());

        setUpToolbar();

        commodityArray = getResources().getStringArray(R.array.array_commodity_copy);

        builder = new LatLngBounds.Builder();

        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        //iGetLocationFromNameController = new GetLocationFromNameController(this);
        IMapFragmentController = new MapFragmentController(this);

        initUI();
        displayData();

    }

    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Booking Detail Trucking");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private void displayData() {
        TruckingMpService myLogsAndOrderInfoDriver = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            myLogsAndOrderInfoDriver = extras.getParcelable("my_log");
        }
        try {
            txtOrderNum.setText(myLogsAndOrderInfoDriver.getOrder_number());
            txtItemType.setText(commodityArray[myLogsAndOrderInfoDriver.getCommodityType()]);
            txtDate.setText(myLogsAndOrderInfoDriver.getDate().substring(0, 10));
            if (myLogsAndOrderInfoDriver.getDate().length() > 11) {
                txtTime.setText(myLogsAndOrderInfoDriver.getDate().substring(11));
            }
            txtLocationFrom.setText(myLogsAndOrderInfoDriver.getFromlocation());
            txtLocationTo.setText(myLogsAndOrderInfoDriver.getTolocation());
            Log.d("LOCATION: ", myLogsAndOrderInfoDriver.getFromlocation() + "  ---  " + myLogsAndOrderInfoDriver.getTolocation());
            fromLocation = myLogsAndOrderInfoDriver.getFromlocation();
            toLocation = myLogsAndOrderInfoDriver.getTolocation();


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void initUI() {

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
        if (fromLocation != null && fromLocation.length() > 0) {
            IMapFragmentController.getFromLatLng(fromLocation);
        }
        if (toLocation != null && toLocation.length() > 0) {
            IMapFragmentController.getToLatLng(toLocation);
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.google_map = googleMap;
        if (google_map != null) {

            try {
                showLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
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


    public void showLoadingDialog(boolean isShow) {
        if (isShow) showProgress();
        else dismissProgress();
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
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }
                            }

                    );
                    builder.setPositiveButton("APP SETTINGS", new DialogInterface.OnClickListener() {
                        @Override
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

        try {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();

                IMapFragmentController.onUpdateDriverLocation(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""), GlobalUtils.MODE_TRUCKING, String.valueOf(latitude), String.valueOf(longitude));
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        LatLng coordinate = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()); //Store these lat lng values somewhere. These should be constant.
        if (coordinate != null) {
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                    coordinate, 15);
            google_map.animateCamera(location);
        }
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
        CommonUtils.showToast(getApplicationContext(), message);
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
