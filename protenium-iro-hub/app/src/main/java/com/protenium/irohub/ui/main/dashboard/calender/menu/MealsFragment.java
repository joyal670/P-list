package com.protenium.irohub.ui.main.dashboard.calender.menu;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseFragment;
import com.protenium.irohub.model.menu.Food;
import com.protenium.irohub.model.menu.MealPlanLimit;
import com.protenium.irohub.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MealsFragment extends BaseFragment implements MealsAdapter.SelectedValue {

    @BindView(R.id.rvMeals)
    RecyclerView rvMeals;
    private List<Food> _foods = new ArrayList<>();
    private List<MealPlanLimit> mealPlanLimits = new ArrayList<>();
    private MealsAdapter mealsAdapter;
    private int totalCounter = 0;
    private String selectedMealId;
    private String mealId="";
    private String mealQty="";
    private SelectedMeal selectedMeal;

    public static MealsFragment newInstance(List<Food> foods, List<MealPlanLimit> _mealPlanLimit, Long mealId) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("foods", (ArrayList<? extends Parcelable>) foods);
        args.putParcelableArrayList("_mealPlanLimit", (ArrayList<? extends Parcelable>) _mealPlanLimit);
        args.putString("mealId", String.valueOf(mealId));
        MealsFragment fragment = new MealsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meals, container, false);
        ButterKnife.bind(this, view);

        if (requireActivity() instanceof SelectedMeal) {
            selectedMeal = (SelectedMeal) requireActivity();
        }

        if (getArguments() != null) {
            _foods = getArguments().getParcelableArrayList("foods");
            mealPlanLimits = getArguments().getParcelableArrayList("_mealPlanLimit");
            selectedMealId = getArguments().getString("mealId");


            for (int i=0; i<mealPlanLimits.size(); i++){
                if(selectedMealId.equals(mealPlanLimits.get(i).getMealTypeId())){
                    mealId = mealPlanLimits.get(i).getMealTypeId();
                    mealQty = mealPlanLimits.get(i).getQuantity();
                }
            }

            rvMeals.setLayoutManager(new LinearLayoutManager(requireContext()));
            mealsAdapter = new MealsAdapter(requireActivity(), _foods, this);
            rvMeals.setAdapter(mealsAdapter);
            mealsAdapter.notifyDataSetChanged();

            totalCounter = 0;
            for (int i = 0; i < _foods.size(); i++) {
                if (_foods.get(i).getOrderedStatus()) {
                    totalCounter++;
                }
            }

            selectedMeal.setValue(totalCounter);
        }


        return view;
    }

    @Override
    public void setValue(Food food) {

        totalCounter = 0;
        for (int i = 0; i < _foods.size(); i++) {
            if (_foods.get(i).getOrderedStatus()) {
                totalCounter++;
            }
        }

        Log.e("TAG", "setValue: totalCounter " + totalCounter);
        Log.e("TAG", "setValue: selectedMealId" + selectedMealId);
        Log.e("TAG", "setValue: mealId" + mealId);
        Log.e("TAG", "setValue: mealQty" + mealQty);

        if (food.getOrderedStatus()) {
            if (mealId.equals(selectedMealId)) {
                if (totalCounter <= Integer.parseInt(mealQty)) {
                    Log.e("TAG", "setValue: true "+ totalCounter );
                } else {
                    CommonUtils.showWarning(requireContext(), "Please remove an item from your selection and select again");
                    food.setOrderedStatus(false);
                    mealsAdapter.notifyDataSetChanged();
                    totalCounter--;
                    Log.e("TAG", "setValue: false "+ totalCounter );
                }
            }
        }
        selectedMeal.setValue(totalCounter);

    }

    public interface SelectedMeal {
        void setValue(int totalCounter);
    }

}