package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.add_rate.UpdateRateVendorsFragment;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.my_logs.PartnerMyLogFragment;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.notifications.CargoNotificationActivity;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.order_info.PartnerOrderInfoFragment;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pending_jobs.VendorPendingJobsActivity;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorProfile;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorProfileDetail;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.profile.VendorProfileActivity;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.faq.Faq;
import com.ashtechlabs.teleporter.ui.pricing.PricingActivity;
import com.ashtechlabs.teleporter.ui.reviews.ReviewActivity;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.GlobalUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CargoDashBoardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, CargoControllerCallback, SharedPreferences.OnSharedPreferenceChangeListener,
        OnFragmentInteractionListener, IAvailableStatusControllerCallback,
        CompoundButton.OnCheckedChangeListener {

    public static ArrayList<VendorList> mOrders;
    public static ArrayList<VendorList> mlogs;
   // public static ArrayList<CargoLoad> mLoadDetails;
    ICargoController ICargoController;
    ArrayList<VendorProfileDetail> profi = new ArrayList<VendorProfileDetail>();
    CircleImageView circleImageView;
    TextView username, email;
    private DrawerLayout mDrawerLayout;
    private LinearLayout linContainer;
    private Toolbar toolbar;
    private SwitchCompat switchDutyStatus;
    private IAvailableStatusController iAvailableStatusController;

    private TabLayout tabLayout;
    private MyAdapter mAdapter;
    private ViewPager mPager;

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


        ICargoController = new CargoController(this);
        iAvailableStatusController = new AvailableStatusController(this);
        initViews();
        ICargoController.onVendorsDetails();
        ICargoController.onGetProfileDetails(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));

    }


    private void setupToolBar() {
        // Set up the toolbar.
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

        if (GlobalPreferManager.getInt(Constants.DUTY_STATUS_CARGO, 10) == 10) {
            switchDutyStatus.setChecked(false);
        } else {
            switchDutyStatus.setChecked(true);
        }
        switchDutyStatus.setOnCheckedChangeListener(this);
        iAvailableStatusController.onGetAvailableStatus(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""), 2);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CargoDashBoardActivity.this, VendorProfileActivity.class);
                startActivity(intent);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.container);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
            GlobalPreferManager.setString(GlobalPreferManager.Keys.TOKEN_CARGO, "");
            GlobalPreferManager.setString(GlobalPreferManager.Keys.USER_MODE, "");
            startActivity(new Intent(this, Dashboard_Main.class));
            finishAffinity();
        } else if (id == R.id.nav_pricing) {
            Intent intent = new Intent(this, PricingActivity.class);
            intent.putExtra("mode", "2");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));
            startActivity(intent);
        } else if (id == R.id.nav_my_rating) {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("mode", "2");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));
            startActivity(intent);
        } else if (id == R.id.nav_my_chat) {
            startChat();
        } else if (id == R.id.nav_my_account) {
            Intent intent = new Intent(this, VendorProfileActivity.class);
            intent.putExtra("mode", "2");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));
            startActivity(intent);
        } else if (id == R.id.nav_declared_orders) {
            Intent intent = new Intent(this, DeclaredOrdersActivity.class);
            intent.putExtra("mode", "2");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));
            startActivity(intent);
        } else if (id == R.id.nav_notifications) {
            Intent intent = new Intent(this, CargoNotificationActivity.class);
            intent.putExtra("mode", "2");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));
            startActivity(intent);
        } else if (id == R.id.nav_pending_jobs) {
            Intent intent = new Intent(this, VendorPendingJobsActivity.class);
            intent.putExtra("mode", "2");
            intent.putExtra("token", GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));
            startActivity(intent);
        } else if (id == R.id.nav_faq) {
            Intent intent = new Intent(this, Faq.class);
            startActivity(intent);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void divideOrders(ArrayList<VendorList> vendorList) {

        mlogs = new ArrayList<>();
        mOrders = new ArrayList<>();
        //mLoadDetails = new ArrayList<>();

        for (int i = 0; i < vendorList.size(); i++) {

            if (vendorList.get(i).getState().equals(GlobalUtils.JOB_ACTIVE)) {
                mOrders.add(vendorList.get(i));
                //for (int k = 0; k < vendorList.get(i).getCargoLoads().size(); k++) {
                   // mLoadDetails.add(vendorList.get(k).getCargoLoads().get(k));
                //}

            } else {
                mlogs.add(vendorList.get(i));
            }
        }

        //CommonUtils.showToast(getApplicationContext(), "" + mLoadDetails.size());
    }


    @Override
    public void onGetVendorsDetails(ArrayList<VendorList> services) {
        divideOrders(services);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mPager);

    }


    @Override
    public void onGetVendorProfileDetails(VendorProfile profile) {
        profi = profile.getProfile();
        Log.d("PROFILEPIC>>>", profi.get(0).getProfileimage());
        if (profi.get(0).getProfileimage() != null) {
            Picasso.with(CargoDashBoardActivity.this).load(Constants.IMAGE_PATH + profi.get(0).getProfileimage())
                    .error(R.drawable.ic_profile)
                    .placeholder(R.drawable.ic_profile)
                    .into(circleImageView);
        }
        GlobalPreferManager.setString(GlobalPreferManager.Keys.USERNAME_CARGO, profi.get(0).getCompanyName());
        GlobalPreferManager.setString(GlobalPreferManager.Keys.PHONE_CARGO, profi.get(0).getContactNumber());
        username.setText(profi.get(0).getCompanyName());
        email.setText(profi.get(0).getContactNumber());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        username.setText(GlobalPreferManager.getString(GlobalPreferManager.Keys.USERNAME_CARGO, ""));
        email.setText(GlobalPreferManager.getString(GlobalPreferManager.Keys.PHONE_CARGO, ""));
    }

    @Override
    public void onGetVendorsDetailsFailed(String message) {
        CommonUtils.showToast(this, message);
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setToolbarTitle(String toolbarTitle) {

    }

    @Override
    public void replaceFragment(String fragment_tag) {

    }

    @Override
    public void onAvailableStatusChanged(int status, String mobileNo, String message) {
        GlobalPreferManager.setInt(Constants.DUTY_STATUS_CARGO, status);
        GlobalPreferManager.setString(GlobalPreferManager.Keys.MOBILE_CARGO, mobileNo);
        if (status == 1) {
            switchDutyStatus.setChecked(true);
        } else {
            switchDutyStatus.setChecked(false);
        }
    }

    @Override
    public void onAvailableStatusChanged(int status, String message) {
        GlobalPreferManager.setInt(Constants.DUTY_STATUS_CARGO, status);
        if (status == 1) {
            switchDutyStatus.setChecked(true);
        } else {
            switchDutyStatus.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            iAvailableStatusController.onSetAvailableStatus(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""), 2, 1);
        } else {
            iAvailableStatusController.onSetAvailableStatus(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""), 2, 0);
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
                    return PartnerOrderInfoFragment.newInstance();
                case 1:
                    return PartnerMyLogFragment.newInstance();
                case 2:
                    return UpdateRateVendorsFragment.newInstance();
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
