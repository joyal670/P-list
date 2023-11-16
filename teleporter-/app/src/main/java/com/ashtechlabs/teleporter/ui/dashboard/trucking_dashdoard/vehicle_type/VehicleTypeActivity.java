package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.add.AddFragment;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.list.ListFragment;
import com.ashtechlabs.teleporter.util.Constants;

import java.util.Hashtable;

public class VehicleTypeActivity extends BaseActivity implements View.OnClickListener, OnVehicleTypeFragmentInteractionListener {

    private final String mListFragmentTag = Constants.TAG_FRAGMENT_VEHICLE_TYPE_LIST;
    private final String mAddFragmentTag = Constants.TAG_FRAGMENT_VEHICLE_TYPE_ADD;

    private final Hashtable<String, Fragment> mFragmentStack = new Hashtable<>();
    private Fragment mActiveFragment;

    private FloatingActionButton fabAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_types);
        setupToolbar();
        initViews();
        initFragments();
        mActiveFragment = mFragmentStack.get(mListFragmentTag);
        launchFragment(mListFragmentTag);
    }

    private void initViews() {

        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void initFragments() {
        mFragmentStack.put(mListFragmentTag, ListFragment.newInstance());
        mFragmentStack.put(mAddFragmentTag, AddFragment.newInstance());
    }

    private void launchFragment(String mFragmentTag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, mActiveFragment, mFragmentTag);
        if (!mFragmentTag.equals(Constants.TAG_FRAGMENT_VEHICLE_TYPE_LIST)) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.close_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_close:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        mActiveFragment = mFragmentStack.get(Constants.TAG_FRAGMENT_VEHICLE_TYPE_ADD);
        launchFragment(Constants.TAG_FRAGMENT_STORAGE_SPACE_ADD);
    }

    @Override
    public void setToolbarTitle(String toolbarTitle) {
        if (getActionBar() != null)
            getActionBar().setTitle(toolbarTitle);
    }

    @Override
    public void replaceFragment(String fragment_tag) {
        mActiveFragment = mFragmentStack.get(fragment_tag);
        launchFragment(fragment_tag);
    }

    @Override
    public void showProgressIndicator(boolean show) {
        if (show)
            showProgress();
        else dismissProgress();
    }

    @Override
    public void showFab(boolean show) {
        if (show)
            fabAdd.show();
        else
            fabAdd.hide();
    }

    @Override
    public void onSelectVehicle(int vehicleType, String truckName, int vehicleSubType, String subTruckName) {

//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.TAG_FRAGMENT_VEHICLE_TYPE_ADD);
//        if(fragment != null)
//            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//
//        mActiveFragment = mFragmentStack.get(Constants.TAG_FRAGMENT_VEHICLE_TYPE_ADD);
//        Bundle args = new Bundle();
//        args.putInt(Constants.KEY_VEHICLE_TYPE, vehicleType);
//        args.putString(Constants.KEY_VEHICLE_TYPE_NAME, truckName);
//        args.putInt(Constants.KEY_VEHICLE_SUB_TYPE, vehicleSubType);
//        args.putString(Constants.KEY_VEHICLE_SUB_TYPE_NAME, subTruckName);
//        mActiveFragment.setArguments(args);
//        launchFragment(Constants.TAG_FRAGMENT_VEHICLE_TYPE_ADD);
    }
}
