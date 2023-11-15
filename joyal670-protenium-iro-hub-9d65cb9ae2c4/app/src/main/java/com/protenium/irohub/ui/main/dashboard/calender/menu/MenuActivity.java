package com.protenium.irohub.ui.main.dashboard.calender.menu;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.model.menu.Food;
import com.protenium.irohub.model.menu.MealPlanLimit;
import com.protenium.irohub.model.menu.MealType;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends BaseActivity implements MealsFragment.SelectedMeal {

    private final List<MealType> menuTypeList = new ArrayList<>();
    private final List<MealPlanLimit> mealPlanLimit = new ArrayList<>();
    private final List<Food> foodList = new ArrayList<>();
    private final List<Integer> foodIdList = new ArrayList<>();


    @BindView(R.id.meals_tabs)
    TabLayout meals_tabs;
    @BindView(R.id.meals_viewPager)
    ViewPager meals_viewPager;
    @BindView(R.id.tvmealsDate)
    TextView tvmealsDate;
    @BindView(R.id.tvSelected)
    TextView tvSelected;
    @BindView(R.id.btnSave)
    MaterialButton btnSave;
    int currentFoodSelectSize = 0;
    private MenuViewModel menuViewModel;
    private String intentDate = "";
    private String intentMealId = "";
    private ViewPagerAdapter viewPagerAdapter;
    private int tabPos = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        menuViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
        menuViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();

            } else {
                dismissProgress();

            }
        });

        intentDate = getIntent().getStringExtra("date");
        intentMealId = getIntent().getStringExtra("mealId");
        tvmealsDate.setText(intentDate);
        getMenu();
        setOnclick();

    }

    private void setOnclick() {

        meals_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabPos = position;
                setText(currentFoodSelectSize);
                Log.e("TAG", "onPageScrolled: " + menuTypeList.get(tabPos).getName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void validate() {
        foodIdList.clear();
        for (int i = 0; i < menuTypeList.size(); i++) {
            for (int j = 0; j < menuTypeList.get(i).getFoods().size(); j++) {
                if (menuTypeList.get(i).getFoods().get(j).getOrderedStatus()) {
                    foodIdList.add(Math.toIntExact(menuTypeList.get(i).getFoods().get(j).getId()));
                }
            }
        }

        Log.e("TAG", "validate: " + foodIdList);

        menuViewModel.getPlaceOrder(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""), intentDate, foodIdList, "en", intentMealId).observe(MenuActivity.this, orderPlaceResponse -> {
            if (orderPlaceResponse.getStatus()) {
                CommonUtils.showWarning(MenuActivity.this, orderPlaceResponse.getMessage());
            } else {
                CommonUtils.showWarning(MenuActivity.this, orderPlaceResponse.getMessage());
            }
        });
    }

    private void setText(int currentFoodSelectSize) {

       /* List<Food> food = menuTypeList.get(0).getFoods();
        for (int i = 0; i < food.size(); i++) {
            if (food.get(i).getOrderedStatus()) {
                currentFoodSelectSize++;
            }
        }*/

        for (int i = 0; i < mealPlanLimit.size(); i++) {
            if (Integer.parseInt(mealPlanLimit.get(i).getMealTypeId()) == Integer.parseInt(String.valueOf(menuTypeList.get(tabPos).getId()))) {
                tvSelected.setText(currentFoodSelectSize + "/" + mealPlanLimit.get(i).getQuantity());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getMenu() {
        menuViewModel.getMenuForDay(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""), intentDate, "en").observe(this, getMenuResponse -> {
            if (getMenuResponse.getStatus()) {

                menuTypeList.addAll(getMenuResponse.getData().getMealTypes());
                mealPlanLimit.addAll(getMenuResponse.getData().getMealPlanLimits());
                meals_tabs.setupWithViewPager(meals_viewPager);
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), menuTypeList, mealPlanLimit);
                meals_viewPager.setAdapter(viewPagerAdapter);

            } else {
                CommonUtils.showWarning(this, getMenuResponse.getMessage());
            }
        });

    }

    @Override
    public int setLayout() {
        return R.layout.activity_menu;
    }

    @Override
    public boolean setToolbar() {
        return false;
    }

    @Override
    public boolean hideStatusBar() {
        return false;
    }

    @Override
    public boolean setFullScreen() {
        return false;
    }

    @Override
    public void setValue(int totalCounter) {
        currentFoodSelectSize = totalCounter;
        Log.e("TAG", "setValue:currentFoodSelectSize " + currentFoodSelectSize);
        setText(currentFoodSelectSize);
    }
}