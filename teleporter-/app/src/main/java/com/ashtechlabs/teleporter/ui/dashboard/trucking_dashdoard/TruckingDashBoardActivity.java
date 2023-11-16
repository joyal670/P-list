package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.declared_orders.DeclaredOrdersActivity;
import com.ashtechlabs.teleporter.ui.Dashboard_Main;
import com.ashtechlabs.teleporter.ui.available_status.AvailableStatusController;
import com.ashtechlabs.teleporter.ui.available_status.IAvailableStatusController;
import com.ashtechlabs.teleporter.ui.available_status.IAvailableStatusControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.add_rate.UpdateRateTruckingFragment;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.faq.TruckingFaq;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.my_logs.TruckingMyLogsFragment;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.notifications.TruckingNotificationActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.order_info.TruckingOrderInfoFragment;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pending_jobs.TruckingPendingJobActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingMpService;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingProfile;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingProfileDetail;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.profile.TruckingProfileActivity;
import com.ashtechlabs.teleporter.ui.pricing.PricingActivity;
import com.ashtechlabs.teleporter.ui.reviews.ReviewActivity;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.GlobalUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TruckingDashBoardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, CompoundButton.OnCheckedChangeListener,
        TruckingControllerCallback, OnTDashFragmentInteractionListener, IAvailableStatusControllerCallback,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static Location current_location;
    public static ArrayList<TruckingMpService> myLogsList;
    public static ArrayList<TruckingMpService> orderInfoList;
    public ArrayList<TruckingProfileDetail> mProfileDetails;
    private String[] array_delivery;
    //private String[] array_mover;
    private Toolbar toolbar;
    private CircleImageView circleImageView;
    private TextView username, email;
    private DrawerLayout mDrawerLayout;
    private ITruckingController ITruckingController;
    private SwitchCompat switchDutyStatus;
    private IAvailableStatusController iAvailableStatusController;

    private ViewPager mPager;
    private MyAdapter mAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        array_delivery = getResources().getStringArray(R.array.array_delivery);
        //array_mover = getResources().getStringArray(R.array.array_driver_service);

        //startUpdateLocation();

        // Set up the toolbar.
        setupToolBar();

        Log.e("TOKEN", FirebaseInstanceId.getInstance().getToken());

        iAvailableStatusController = new AvailableStatusController(this);
        initViews();
        // Set up the navigation drawer.

        ITruckingController = new TruckingController(this);
        ITruckingController.onGetJobsWithDriverToken(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""));
        ITruckingController.onGetProfileDetails(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""));


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TruckingDashBoardActivity.this, TruckingProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Home");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void initViews() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        View header = navigationView.getHeaderView(0);
        circleImageView = (CircleImageView) header.findViewById(R.id.profile_image);
        username = (TextView) header.findViewById(R.id.usernames);
        email = (TextView) header.findViewById(R.id.emailid);

        switchDutyStatus = (SwitchCompat) header.findViewById(R.id.switchDutyStatus);
        if (GlobalPreferManager.getInt(Constants.DUTY_STATUS_TRUCKING, 10) == 10) {
            switchDutyStatus.setChecked(false);
        } else {
            switchDutyStatus.setChecked(true);
        }
        switchDutyStatus.setOnCheckedChangeListener(this);
        iAvailableStatusController.onGetAvailableStatus(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""), 1);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.container);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_my_account) {
            Intent intent = new Intent(this, TruckingProfileActivity.class);
            intent.putExtra("mode", "1");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""));
            startActivity(intent);
        } else if (id == R.id.nav_my_chat) {
            startChat();
        } else if (id == R.id.nav_pricing) {
            Intent intent = new Intent(this, PricingActivity.class);
            intent.putExtra("mode", "1");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""));
            startActivity(intent);
        } else if (id == R.id.nav_my_rating) {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("mode", "1");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""));
            startActivity(intent);
        } else if (id == R.id.nav_declared_orders) {
            Intent intent = new Intent(this, DeclaredOrdersActivity.class);
            intent.putExtra("mode", "1");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""));
            startActivity(intent);
        } else if (id == R.id.nav_pending_jobs) {
            Intent intent = new Intent(this, TruckingPendingJobActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_notifications) {
            Intent intent = new Intent(this, TruckingNotificationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            GlobalPreferManager.setString(GlobalPreferManager.Keys.TOKEN_TRUCKING, "");
            GlobalPreferManager.setString(GlobalPreferManager.Keys.USER_MODE, "");
            startActivity(new Intent(this, Dashboard_Main.class));
            finishAffinity();
        } else if (id == R.id.nav_faq) {
            Intent intent = new Intent(this, TruckingFaq.class);
            startActivity(intent);
        }


        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onGetDriverProfileDetails(TruckingProfile profile) {
        mProfileDetails = profile.getProfile();
        if (!mProfileDetails.get(0).getProfileImage().equals(""))
            Picasso.with(this)
                    .load(Constants.IMAGE_PATH_OLD + mProfileDetails.get(0).getProfileImage())
                    .error(R.drawable.ic_profile)
                    .placeholder(R.drawable.ic_profile)
                    .into(circleImageView);
        GlobalPreferManager.setString(GlobalPreferManager.Keys.USERNAME_TRUCKING, mProfileDetails.get(0).getName());
        GlobalPreferManager.setString(GlobalPreferManager.Keys.PHONE_TRUCKING, mProfileDetails.get(0).getContactNumber());
        username.setText(mProfileDetails.get(0).getName());
        email.setText(mProfileDetails.get(0).getContactNumber());
    }

    @Override
    public void onGetDriverDetailsFailed(String message) {
        CommonUtils.showToast(this, message);
    }

    @Override
    public void onGetDriverDetails(ArrayList<TruckingMpService> deliveryServices) {

        //ArrayList<TruckingMpService> mpServiceArrayList = deliveryServices.getTruckingMpServices();

        myLogsList = new ArrayList<>();
        orderInfoList = new ArrayList<>();


        for (int i = 0; i < deliveryServices.size(); i++) {

            if (deliveryServices.get(i).getState().equals(GlobalUtils.JOB_ACTIVE)) {
                orderInfoList.add(deliveryServices.get(i));
                //for (int k = 0; k < vendorList.get(i).getCargoLoads().size(); k++) {
                // mLoadDetails.add(vendorList.get(k).getCargoLoads().get(k));
                //}

            } else {
                myLogsList.add(deliveryServices.get(i));
            }

        }


        mAdapter = new MyAdapter(getSupportFragmentManager());
        if (mPager.getAdapter() != null) {
            mPager.setAdapter(null);
        }
        mPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mPager);

    }


//    private void getMyOrderInfoMp(TruckingMpService mpService) {
//
//        orderInfoList.add(new MyLogsAndOrderInfoTrucking(mpService.getId(),
//                mpService.getDate().substring(11), mpService.getDate().substring(0, 10),
//                mpService.getFromlocation(), mpService.getTolocation(),
//                mpService.getState(), array_mover[Integer.valueOf(mpService.getVehicletype())],
//                mpService.getOrderid(), mpService.getOrdertype(), mpService.getCommodity_description(),
//                mpService.getPayment(), mpService.getWeight(), mpService.getWeight_unit(), mpService.getOrder_number(),
//                mpService.getPrice(), mpService.getCurrency(), mpService.getDuration(),mpService.getSubVehicleType(),mpService.getAddInsurance(), mpService.getVolume(), mpService.getVolumeUnit()));
//
//    }


//    private void getMyLogsMp(TruckingMpService mpService) {
//
//        myLogsList.add(new MyLogsAndOrderInfoTrucking(mpService.getId(),
//                mpService.getDate().substring(11), mpService.getDate().substring(0, 10),
//                mpService.getFromlocation(), mpService.getTolocation(),
//                mpService.getState(), array_mover[Integer.valueOf(mpService.getVehicletype())],
//                mpService.getOrderid(), mpService.getOrdertype(), mpService.getCommodity_description(),
//                mpService.getPayment(), mpService.getWeight(), mpService.getWeight_unit(),
//                mpService.getOrder_number(), mpService.getPrice(), mpService.getCurrency(),
//                mpService.getDuration(),mpService.getSubVehicleType(),mpService.getAddInsurance(), mpService.getVolume(), mpService.getVolumeUnit()));
//    }


    @Override
    public void showLoadingIndicator() {
        showProgress();
    }

    @Override
    public void dismissLoadingIndicator() {
        dismissProgress();
    }

    @Override
    public void showVehicleTypeFragment(boolean show) {
        viewTruckTypeFragment(show);
        CommonUtils.showToast(getApplicationContext(), "///////////");
    }


    private void viewTruckTypeFragment(boolean show) {
//        if (show) {
//            someFragment = new ChooseVechicleFragment();
//            transaction = getSupportFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left);
//            transaction.add(R.id.containerVechicle, someFragment);
//            transaction.addToBackStack(null);
//
//            transaction.commit();
//        } else {
//            transaction.setCustomAnimations(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
//            transaction.hide(someFragment);
//        }

    }

    @Override
    public void onAvailableStatusChanged(int status,  String mobileNo, String message) {
        GlobalPreferManager.setInt(Constants.DUTY_STATUS_STORAGE, status);
        GlobalPreferManager.setString(GlobalPreferManager.Keys.MOBILE_TRUCKING, mobileNo);
        if(status==1){
            switchDutyStatus.setChecked(true);
        }else{
            switchDutyStatus.setChecked(false);
        }
    }

    @Override
    public void onAvailableStatusChanged(int status, String message) {
        GlobalPreferManager.setInt(Constants.DUTY_STATUS_STORAGE, status);
        if(status==1){
            switchDutyStatus.setChecked(true);
        }else{
            switchDutyStatus.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            iAvailableStatusController.onSetAvailableStatus(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""), 1, 1);
        } else {
            iAvailableStatusController.onSetAvailableStatus(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""), 1, 0);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        username.setText(GlobalPreferManager.getString(GlobalPreferManager.Keys.USERNAME_TRUCKING, ""));
        email.setText(GlobalPreferManager.getString(GlobalPreferManager.Keys.PHONE_TRUCKING, ""));
    }



    private class MyAdapter extends FragmentPagerAdapter {

        private MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TruckingOrderInfoFragment.newInstance();
                case 1:
                    return TruckingMyLogsFragment.newInstance();
                case 2:
                    return UpdateRateTruckingFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }


        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Order Info";
                case 1:
                    return "My Logs";
                case 2:
                    return "Update Rate";
            }
            return null;
        }
    }

}
