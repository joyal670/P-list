package com.protenium.irohub.ui.main.dashboard.calender.menu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.gson.Gson;
import com.protenium.irohub.R;
import com.protenium.irohub.model.menu.Food;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHold> {

    private final List<Food> stringList;
    private final Context context;
    private boolean click = false;
    private SelectedValue selectedValue;

    public MealsAdapter(Context fragment, List<Food> dataList , SelectedValue listner) {
        this.stringList = dataList;
        this.context = fragment;
        if (fragment instanceof SelectedValue) {
            selectedValue = (SelectedValue) fragment;
        }
        this.selectedValue = listner;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycerview_meals, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        Food f = stringList.get(position);
        Glide.with(context).load(stringList.get(position).getImage()).into(holder.ivFood);
        holder.tvFoodName.setText(stringList.get(position).getName());
        holder.tvDescriptionContent.setText(stringList.get(position).getTagline());
        holder.ratingFood.setRating(stringList.get(position).getMyRating().floatValue());
        holder.tvAverage.setText(stringList.get(position).getAverageRating().toString());
        holder.tvTagExpand.setText(stringList.get(position).getDescription());

        holder.meals_selectionBtn.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_drop_down_white,
                0
        );
        holder.meals_selectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click) {
                    holder.tvTagExpand.setVisibility(View.GONE);
                    holder.meals_selectionBtn.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_arrow_drop_down_white,
                            0
                    );
                    click = false;
                } else {
                    holder.tvTagExpand.setVisibility(View.VISIBLE);
                    holder.meals_selectionBtn.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_arrow_up,
                            0
                    );
                    click = true;

                }
            }
        });

        holder.mealsSelectionRadioBtn.setChecked(stringList.get(position).getOrderedStatus());

        holder.mealsSelectionRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        holder.itemView.setOnClickListener(v -> {
            Log.e("TAG", "onClick: "+ new Gson().toJson(f) );
            //selectedValue.setValue(f);
            if(stringList.get(position).getOrderedStatus()){
                stringList.get(position).setOrderedStatus(false);
            }else {
                stringList.get(position).setOrderedStatus(true);
            }
            notifyDataSetChanged();
            selectedValue.setValue(f);
        });

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }


    public class ViewHold extends RecyclerView.ViewHolder {

        @BindView(R.id.ivFood)
        ImageView ivFood;

        @BindView(R.id.tvFoodName)
        TextView tvFoodName;

        @BindView(R.id.tvDescriptionContent)
        TextView tvDescriptionContent;

        @BindView(R.id.mealsSelectionRadioBtn)
        MaterialRadioButton mealsSelectionRadioBtn;

        @BindView(R.id.meals_selectionBtn)
        MaterialButton meals_selectionBtn;

        @BindView(R.id.tvTagExpand)
        TextView tvTagExpand;

        @BindView(R.id.ratingFood)
        ScaleRatingBar ratingFood;

        @BindView(R.id.tvAverage)
        TextView tvAverage;


        public ViewHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface SelectedValue  {
        void setValue(Food food);
    }


}
