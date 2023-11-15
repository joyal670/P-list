package com.protenium.irohub.ui.main.dashboard.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.protenium.irohub.R;
import com.protenium.irohub.model.home.MealPlan;
import com.protenium.irohub.ui.main.dashboard.home.activity.HomeDetailedActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeSubAdapter extends RecyclerView.Adapter<HomeSubAdapter.ViewHold> {

    private final List<MealPlan> mealPlans;
    private final Context context;

    public HomeSubAdapter(Context fragment, List<MealPlan> dataList) {
        this.mealPlans = dataList;
        this.context = fragment;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_home_sub_item, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        MealPlan mealPlan = mealPlans.get(position);
        holder.tvCategoryItemTitle1.setText(mealPlan.getName());
        holder.tvCategoryItems.setText(mealPlan.getMealTypes());

        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeDetailedActivity.class);
                intent.putExtra("id", mealPlan.getId().toString());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mealPlans.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCategoryItemTitle1)
        TextView tvCategoryItemTitle1;

        @BindView(R.id.tvCategoryItems)
        TextView tvCategoryItems;

        @BindView(R.id.content)
        ConstraintLayout content;

        public ViewHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
