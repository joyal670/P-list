package com.protenium.irohub.ui.main.dashboard.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseFragment;
import com.protenium.irohub.ui.main.dashboard.activity.NotificationActivity;
import com.protenium.irohub.ui.main.dashboard.home.adapter.HomeAdapter;
import com.protenium.irohub.ui.main.dashboard.home.adapter.HomeImageSliderAdapter;
import com.protenium.irohub.ui.main.dashboard.home.viewmodel.HomeViewModel;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends BaseFragment {

    private HomeViewModel homeViewModel;

    @BindView(R.id.vpHomeImageSlider)
    ViewPager2 vpHomeImageSlider;

    @BindView(R.id.dotsIndicatorFloor)
    DotsIndicator dotsIndicatorFloor;

    @BindView(R.id.rvHomeCategoryItems)
    RecyclerView rvHomeCategoryItems;

    @BindView(R.id.ivNotification)
    ImageView ivNotification;

    private HomeAdapter homeAdapter;

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        homeViewModel = ViewModelProviders.of((AppCompatActivity) context).get(HomeViewModel.class);
        homeViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });
        setUpRecyclerView();
        setOnClicks();
        return view;
    }

    private void setOnClicks() {
        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpRecyclerView() {
        rvHomeCategoryItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        homeViewModel.getMeals("en").observe(this, homeItem -> {
            homeAdapter = new HomeAdapter(requireContext(), homeItem);
            rvHomeCategoryItems.setAdapter(homeAdapter);
        });

        homeViewModel.getBanner("en").observe(this, banners -> {
            vpHomeImageSlider.setAdapter(new HomeImageSliderAdapter(requireContext(), banners));
            dotsIndicatorFloor.setViewPager2(vpHomeImageSlider);
        });

    }
}