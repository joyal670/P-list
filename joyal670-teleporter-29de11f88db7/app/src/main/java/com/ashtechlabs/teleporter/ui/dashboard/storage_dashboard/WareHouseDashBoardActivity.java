package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import com.ashtechlabs.teleporter.ui.OnFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.available_status.AvailableStatusController;
import com.ashtechlabs.teleporter.ui.available_status.IAvailableStatusController;
import com.ashtechlabs.teleporter.ui.available_status.IAvailableStatusControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.faq.Faq;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.add_rate.WarehouseUpdateRateFragment;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.notifications.WarehouseNotificationActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pending_jobs.WarehousePendingJobActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouse;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseList;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseProfile;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseProfileDetail;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.profile.WareHouseProfileActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.warehouse_my_logs.WarehouseLogFragment;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.warehouse_order_info.WarehouseOrderInfoFragment;
import com.ashtechlabs.teleporter.ui.pricing.PricingActivity;
import com.ashtechlabs.teleporter.ui.reviews.ReviewActivity;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.GlobalUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class WareHouseDashBoardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener,
        OnFragmentInteractionListener, WareHouseControllerCallback, IAvailableStatusControllerCallback,
        CompoundButton.OnCheckedChangeListener  {

    public static ArrayList<WareHouseList> mlogs;
    public static ArrayList<WareHouseList> mOrders;
    private ArrayList<WareHouseList> serviceArrayList;
    private IWareHouseController IWareHouseController;
    private IAvailableStatusController iAvailableStatusController;
    private CircleImageView circleImageView;
    private SwitchCompat switchDutyStatus;
    private TextView username, email;
    private DrawerLayout mDrawerLayout;
    private ArrayList<WareHouseProfileDetail> profil = new ArrayList<WareHouseProfileDetail>();

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Get Window
            final Window window = getWindow();
            // Set Fullscreen
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        setupToolBar();
        // Set up the toolbar.


        IWareHouseController = new WareHouseController(this);
        iAvailableStatusController = new AvailableStatusController(this);
        initViews();
        IWareHouseController.onWareHouseDetails();
        IWareHouseController.onGetProfileDetails();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WareHouseDashBoardActivity.this, WareHouseProfileActivity.class);
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
        circleImageView = (de.hdodenhof.circleimageview.CircleImageView) header.findViewById(R.id.profile_image);
        username = (TextView) header.findViewById(R.id.usernames);
        switchDutyStatus = (SwitchCompat) header.findViewById(R.id.switchDutyStatus);
        if (GlobalPreferManager.getInt(Constants.DUTY_STATUS_STORAGE, 10) == 10) {
            switchDutyStatus.setChecked(false);
        } else {
            switchDutyStatus.setChecked(true);
        }
        switchDutyStatus.setOnCheckedChangeListener(this);
        iAvailableStatusController.onGetAvailableStatus(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""), 3);
        email = (TextView) header.findViewById(R.id.emailid);

    }


    @Override
    public void onBackPressed() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        if (id == R.id.nav_logout) {
            GlobalPreferManager.setString(GlobalPreferManager.Keys.TOKEN_STORAGE, "");
            GlobalPreferManager.setString(GlobalPreferManager.Keys.USER_MODE, "");
            startActivity(new Intent(this, Dashboard_Main.class));
            finishAffinity();
        } else if (id == R.id.nav_pricing) {
            Intent intent = new Intent(this, PricingActivity.class);
            intent.putExtra("mode", "3");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));
            startActivity(intent);
        } else if (id == R.id.nav_declared_orders) {
            Intent intent = new Intent(this, DeclaredOrdersActivity.class);
            intent.putExtra("mode", "3");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));
            startActivity(intent);
        } else if (id == R.id.nav_my_rating) {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("mode", "3");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));
            startActivity(intent);
        } else if (id == R.id.nav_my_chat) {
            startChat();
        } else if (id == R.id.nav_my_account) {
            Intent intent = new Intent(this, WareHouseProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_notifications) {
            Intent intent = new Intent(this, WarehouseNotificationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pending_jobs) {
            Intent intent = new Intent(this, WarehousePendingJobActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_faq) {
            Intent intent = new Intent(this, Faq.class);
            startActivity(intent);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//    void startChat() {
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
//        ZopimChatActivity.startActivity(WareHouseDashBoardActivity.this, config);
//
//        // Sample breadcrumb
//        ZopimChat.trackEvent("Started chat with pre-set visitor information");
//    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setToolbarTitle(String toolbarTitle) {

    }

    @Override
    public void replaceFragment(String fragment_tag) {

    }

    @Override
    public void onGetWareHouseDetails(WareHouse wareHouse) {
        serviceArrayList = wareHouse.getWareHouseService();
        divideOrders();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());
        ViewPager mPager = (ViewPager) findViewById(R.id.container);
        mPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mPager);
    }

    @Override
    public void onGetWareHouseDetailsFailed(String message) {
        CommonUtils.showToast(getApplicationContext(), message);
    }


    private void divideOrders() {

        mlogs = new ArrayList<>();
        mOrders = new ArrayList<>();

        for (int i = 0; i < serviceArrayList.size(); i++) {
            if (serviceArrayList.get(i).getState().equals(GlobalUtils.JOB_ACTIVE))
                mOrders.add(serviceArrayList.get(i));
            else mlogs.add(serviceArrayList.get(i));
        }
    }

    @Override
    public void onGetStorageProfileDetails(WareHouseProfile profile) {
        profil = profile.getProfile();

        if (profil.get(0).getPicOfWarehouse() != null) {
            Picasso.with(WareHouseDashBoardActivity.this).load(Constants.IMAGE_PATH + profil.get(0).getPicOfWarehouse())
                    .error(R.drawable.ic_profile)
                    .placeholder(R.drawable.ic_profile)
                    .into(circleImageView);
        }

        GlobalPreferManager.setString(GlobalPreferManager.Keys.USERNAME_STORAGE, profil.get(0).getCompanyName());
        GlobalPreferManager.setString(GlobalPreferManager.Keys.PHONE_STORAGE, profil.get(0).getContactNumber());

        username.setText(profil.get(0).getCompanyName());
        email.setText(profil.get(0).getContactNumber());
    }

    @Override
    public void onGetStorageProfileDetailsFailed(String message) {

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
    public void onAvailableStatusChanged(int status,  String mobileNo, String message) {
        GlobalPreferManager.setInt(Constants.DUTY_STATUS_STORAGE, status);
        GlobalPreferManager.setString(GlobalPreferManager.Keys.MOBILE_STORAGE, mobileNo);
        if (status == 1) {
            switchDutyStatus.setChecked(true);
        } else {
            switchDutyStatus.setChecked(false);
        }
    }

    @Override
    public void onAvailableStatusChanged(int status, String message) {
        GlobalPreferManager.setInt(Constants.DUTY_STATUS_STORAGE, status);
        if (status == 1) {
            switchDutyStatus.setChecked(true);
        } else {
            switchDutyStatus.setChecked(false);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            iAvailableStatusController.onSetAvailableStatus(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""), 3, 1);
        } else {
            iAvailableStatusController.onSetAvailableStatus(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""), 3, 0);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        username.setText(GlobalPreferManager.getString(GlobalPreferManager.Keys.USERNAME_STORAGE, ""));
        email.setText(GlobalPreferManager.getString(GlobalPreferManager.Keys.PHONE_STORAGE, ""));
    }


    private class MyAdapter extends FragmentPagerAdapter {

        private MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return WarehouseOrderInfoFragment.newInstance();
                case 1:
                    return WarehouseLogFragment.newInstance();
                case 2:
                    return WarehouseUpdateRateFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {

            return 3;

        }

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
