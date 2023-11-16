package com.protenium.irohub.ui.main.dashboard.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.protenium.irohub.R;
import com.protenium.irohub.model.home.MealCategory;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHold> {

    private List<MealCategory> mealCategories;
    private Context context;
    private Boolean clicked;

    public HomeAdapter(Context fragment, List<MealCategory> dataList) {
        this.mealCategories = dataList;
        this.context = fragment;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_home_item, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        MealCategory mealCategory = mealCategories.get(position);
        holder.tvCategory.setText(mealCategory.getName());

        holder.rvCategorySubListItems.setLayoutManager(new LinearLayoutManager(context));
        holder.rvCategorySubListItems.setAdapter(new HomeSubAdapter(context, mealCategory.getMealPlans()));


        clicked = true;
        holder.fabArrowDown1.setOnClickListener(v -> {
            if(clicked){
                holder.rvCategorySubListItems.setVisibility(View.VISIBLE);
                holder.ivArrowDown1.setRotation(90f);
                holder.ivArrowDown1.setRotation(270f);
                clicked = false;
            }else {
                holder.rvCategorySubListItems.setVisibility(View.GONE);
                holder.ivArrowDown1.setRotation(270f);
                holder.ivArrowDown1.setRotation(90f);
                clicked = true;
            }

        });
    }

    @Override
    public int getItemCount() {
        return mealCategories.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCategory)
        TextView tvCategory;

        @BindView(R.id.rvCategorySubListItems)
        RecyclerView rvCategorySubListItems;

        @BindView(R.id.fabArrowDown1)
        FloatingActionButton fabArrowDown1;

        @BindView(R.id.ivArrowDown1)
        ImageView ivArrowDown1;


        public ViewHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
