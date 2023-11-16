package com.protenium.irohub.ui.main.dashboard.calender.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.protenium.irohub.model.menu.MealPlanLimit;
import com.protenium.irohub.model.menu.MealType;

import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter  {

    private final List<MealType> mealType;
    private final List<MealPlanLimit> _mealPlanLimit;

    public ViewPagerAdapter(FragmentManager supportFragmentManager, List<MealType> mainTitleList, List<MealPlanLimit> mealPlanLimits) {
        super(supportFragmentManager);
        this.mealType = mainTitleList;
        this._mealPlanLimit = mealPlanLimits;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return MealsFragment.newInstance(mealType.get(position).getFoods(), _mealPlanLimit, mealType.get(position).getId());
    }

    @Override
    public int getCount() {
        return mealType.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mealType.get(position).getName();
    }

}
