package com.protenium.irohub.ui.main.dashboard.profile.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.protenium.irohub.R;
import com.protenium.irohub.model.dislike.getdislike.Tag;
import com.protenium.irohub.model.subscription_history.Subscription;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHold> {

    private final List<Subscription> dataList;
    private final Context context;
    private SelectedId selectedId;

    public SubscriptionAdapter(Context fragment, List<Subscription> dataList) {
        this.dataList = dataList;
        this.context = fragment;
        if(fragment instanceof SelectedId){
            selectedId = (SelectedId) fragment;
        }
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycerview_subscriptionhistory, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        Log.e("TAG", "onBindViewHolder: " + dataList);
        Glide.with(context).load(dataList.get(position).getImage()).into(holder.ivSubscriptionHistory);

        holder.tvSubscriptionHistoryId.setText("#" + " " + dataList.get(position).getId());

        holder.tvStatus.setText(dataList.get(position).getPlanStatus());

        holder.tvSubscriptionHistoryCategoryPlan.setText(dataList.get(position).getMealPlanName() + " - " + dataList.get(position).getMealCategoryName());

        holder.rvsubscriptionHistoryViewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedId.setValue(dataList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {

        @BindView(R.id.ivSubscriptionHistory)
        ImageView ivSubscriptionHistory;

        @BindView(R.id.tvSubscriptionHistoryId)
        TextView tvSubscriptionHistoryId;

        @BindView(R.id.tvStatus)
        TextView tvStatus;

        @BindView(R.id.tvSubscriptionHistoryCategoryPlan)
        TextView tvSubscriptionHistoryCategoryPlan;

        @BindView(R.id.rvsubscriptionHistoryViewDetailsBtn)
        MaterialButton rvsubscriptionHistoryViewDetailsBtn;


        public ViewHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Log.e("TAG", "ViewHold: " + dataList);
        }
    }

    public interface SelectedId {
        void setValue(int selectedId);
    }

}
