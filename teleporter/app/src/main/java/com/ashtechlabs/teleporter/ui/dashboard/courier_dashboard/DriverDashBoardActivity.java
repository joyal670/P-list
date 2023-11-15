package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard;

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
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.add_rate.UpdateRateCourierFragment;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.faq.Faq;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.my_logs.MyLogsFragment;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.notifications.CourierNotificationActivity;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.order_info.OrderInfoFragment;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pending_jobs.DriverPendingJobActivity;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DeliveryService;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverOrder;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverProfile;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverProfileDetail;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.MyLogsAndOrderInfoDriver;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.profile.DriverProfileActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.OnTDashFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.pricing.PricingActivity;
import com.ashtechlabs.teleporter.ui.reviews.ReviewActivity;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.GlobalUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverDashBoardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener,
        DriverControllerCallback, OnTDashFragmentInteractionListener, IAvailableStatusControllerCallback, CompoundButton.OnCheckedChangeListener {


    public static Location current_location;
    public static ArrayList<MyLogsAndOrderInfoDriver> myLogsList;
    public static ArrayList<MyLogsAndOrderInfoDriver> orderInfoList;
    public ArrayList<DriverProfileDetail> mProfileDetails;
    private String[] array_delivery;
    private Toolbar toolbar;
    private CircleImageView circleImageView;
    private TextView username, email;
    private DrawerLayout mDrawerLayout;
    private IDriverController IDriverController;
    private IAvailableStatusController iAvailableStatusController;
    private SwitchCompat switchDutyStatus;

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

        IDriverController = new DriverController(this);
        IDriverController.onGetJobsWithDriverToken(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));
        IDriverController.onGetProfileDetails(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverDashBoardActivity.this, DriverProfileActivity.class);
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
        if (GlobalPreferManager.getInt(Constants.DUTY_STATUS_COURIER, 10) == 10) {
            switchDutyStatus.setChecked(false);
        } else {
            switchDutyStatus.setChecked(true);
        }
        switchDutyStatus.setOnCheckedChangeListener(this);
        iAvailableStatusController.onGetAvailableStatus(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""), 0);

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
            Intent intent = new Intent(this, DriverProfileActivity.class);
            intent.putExtra("mode", "0");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));
            startActivity(intent);
        } else if (id == R.id.nav_my_chat) {
            startChat();
        } else if (id == R.id.nav_pricing) {
            Intent intent = new Intent(this, PricingActivity.class);
            intent.putExtra("mode", "0");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));
            startActivity(intent);
        } else if (id == R.id.nav_my_rating) {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("mode", "0");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));
            startActivity(intent);
        } else if (id == R.id.nav_declared_orders) {
            Intent intent = new Intent(this, DeclaredOrdersActivity.class);
            intent.putExtra("mode", "0");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));
            startActivity(intent);
        } else if (id == R.id.nav_pending_jobs) {
            Intent intent = new Intent(this, DriverPendingJobActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_notifications) {
            Intent intent = new Intent(this, CourierNotificationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            GlobalPreferManager.setString(GlobalPreferManager.Keys.TOKEN_COURIER, "");
            GlobalPreferManager.setString(GlobalPreferManager.Keys.USER_MODE, "");;
            startActivity(new Intent(this, Dashboard_Main.class));
            finishAffinity();
            startActivity(new Intent(this, Dashboard_Main.class));
        } else if (id == R.id.nav_faq) {
            Intent intent = new Intent(this, Faq.class);
            startActivity(intent);
        }


        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void startChat() {
//
//        VisitorInfo visitorInfo = new VisitorInfo.Builder()
//                .phoneNumber("+1800111222333")
//                .email("visitor@example.com")
//                .name(appUtils.getUserName())
//                .build();
//
//        // visitor info can be set at any point when that information becomes available
//        ZopimChat.setVisitorInfo(visitorInfo);
//
//        // set pre chat fields as mandatory
//        PreChatForm preChatForm = new PreChatForm.Builder()
//                .name(PreChatForm.Field.OPTIONAL)
//                .email(PreChatForm.Field.OPTIONAL)
//                .phoneNumber(PreChatForm.Field.OPTIONAL)
//                .department(PreChatForm.Field.OPTIONAL)
//                .message(PreChatForm.Field.OPTIONAL)
//                .build();
//
//        // build chat config
//        ZopimChat.SessionConfig config = new ZopimChat.SessionConfig().preChatForm(preChatForm).department("My memory");
//
//        // start chat activity with config
//        ZopimChatActivity.startActivity(TruckingDashBoardActivity.this, config);
//
//        // Sample breadcrumb
//        ZopimChat.trackEvent("Started chat with pre-set visitor information");
//    }


    @Override
    public void onGetDriverProfileDetails(DriverProfile profile) {
        mProfileDetails = profile.getProfile();
        Log.d("PROFILEPIC>>>", mProfileDetails.get(0).getProfileimage());
        if (!mProfileDetails.get(0).getProfileimage().equals(""))
            Picasso.with(this)
                    .load(Constants.IMAGE_PATH  + mProfileDetails.get(0).getProfileimage())
                    .error(R.drawable.ic_profile)
                    .placeholder(R.drawable.ic_profile)
                    .into(circleImageView);
        GlobalPreferManager.setString(GlobalPreferManager.Keys.USERNAME_COURIER, mProfileDetails.get(0).getName());
        GlobalPreferManager.setString(GlobalPreferManager.Keys.PHONE_COURIER, mProfileDetails.get(0).getContactNumber());

        username.setText(mProfileDetails.get(0).getName());
        email.setText(mProfileDetails.get(0).getContactNumber());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        username.setText(GlobalPreferManager.getString(GlobalPreferManager.Keys.USERNAME_COURIER, ""));
        email.setText(GlobalPreferManager.getString(GlobalPreferManager.Keys.PHONE_COURIER, ""));
    }

    @Override
    public void onGetDriverDetailsFailed(String message) {
        CommonUtils.showToast(this, message);
    }

    @Override
    public void onGetDriverDetails(DriverOrder deliveryServices) {

        ArrayList<DeliveryService> deliveryServiceArrayList = deliveryServices.getDeliveryService();
        // ArrayList<MpService> mpServiceArrayList = deliveryServices.getMpServices();

        myLogsList = new ArrayList<>();
        orderInfoList = new ArrayList<>();

        for (int i = 0; i < deliveryServiceArrayList.size(); i++) {

            if (deliveryServiceArrayList.get(i).getState().equals(GlobalUtils.JOB_ACTIVE)) {
                getMyOrderInfoDelivery(deliveryServiceArrayList.get(i));
            } else {
                getMyLogsDelivery(deliveryServiceArrayList.get(i));
            }
        }

        mAdapter = new MyAdapter(getSupportFragmentManager());
        if (mPager.getAdapter() != null) {
            mPager.setAdapter(null);
        }
        mPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mPager);

    }


    private void getMyOrderInfoDelivery(DeliveryService deliveryService) {

        orderInfoList.add(new MyLogsAndOrderInfoDriver(deliveryService.getId(),
                deliveryService.getDate().substring(11), deliveryService.getDate().substring(0, 10),
                deliveryService.getFromlocation(), deliveryService.getTolocation(),
                deliveryService.getState(), array_delivery[Integer.valueOf(deliveryService.getItemtype())],
                deliveryService.getOrderid(), deliveryService.getOrdertype(), deliveryService.getCommodity_description(),
                deliveryService.getPayment(), deliveryService.getWeight(), deliveryService.getWeight_unit(),
                deliveryService.getOrder_number(), deliveryService.getPrice(), deliveryService.getCurrency(), deliveryService.getDuration()));

    }


    private void getMyLogsDelivery(DeliveryService deliveryService) {

        myLogsList.add(new MyLogsAndOrderInfoDriver(deliveryService.getId(),
                deliveryService.getDate().substring(11), deliveryService.getDate().substring(0, 10),
                deliveryService.getFromlocation(), deliveryService.getTolocation(),
                deliveryService.getState(), array_delivery[Integer.valueOf(deliveryService.getItemtype())],
                deliveryService.getOrderid(), deliveryService.getOrdertype(), deliveryService.getCommodity_description(),
                deliveryService.getPayment(), deliveryService.getWeight(), deliveryService.getWeight_unit(),
                deliveryService.getOrder_number(), deliveryService.getPrice(), deliveryService.getCurrency(), deliveryService.getDuration()));
    }




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

    }

    @Override
    public void onAvailableStatusChanged(int status, String mobileNo, String message) {
        GlobalPreferManager.setInt(Constants.DUTY_STATUS_COURIER, status);
        GlobalPreferManager.setString(GlobalPreferManager.Keys.MOBILE_COURIER, mobileNo);
        if(status==1){
            switchDutyStatus.setChecked(true);
        }else{
            switchDutyStatus.setChecked(false);
        }
    }

    @Override
    public void onAvailableStatusChanged(int status, String message) {
        GlobalPreferManager.setInt(Constants.DUTY_STATUS_COURIER, status);
        if(status==1){
            switchDutyStatus.setChecked(true);
        }else{
            switchDutyStatus.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            iAvailableStatusController.onSetAvailableStatus(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""), 0, 1);
        } else {
            iAvailableStatusController.onSetAvailableStatus(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""), 0, 0);
        }
    }




    private class MyAdapter extends FragmentPagerAdapter {

        private MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return OrderInfoFragment.newInstance();
                case 1:
                    return MyLogsFragment.newInstance();
                case 2:
                    return UpdateRateCourierFragment.newInstance();
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
