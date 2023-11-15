package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.notifications;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.common_interfaces.OnRecyclerItemClickListener;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WarehouseNotificationList;
import com.ashtechlabs.teleporter.util.Constants;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class WarehouseNotifyAdapter extends RecyclerView.Adapter<WarehouseNotifyAdapter.CustomViewHolder> {
    Activity mContext;
    OnRecyclerItemClickListener itemClickListener;
    private ArrayList<WarehouseNotificationList> notif;

    public WarehouseNotifyAdapter(Activity context, ArrayList<WarehouseNotificationList> revs) {
        mContext = context;
        notif = revs;
    }

    public void setClickListener(OnRecyclerItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public WarehouseNotifyAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_notification, viewGroup, false);
        return new WarehouseNotifyAdapter.CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final WarehouseNotifyAdapter.CustomViewHolder holder, final int position) {

        WarehouseNotificationList warehouseNotificationList = notif.get(position);

        if(warehouseNotificationList.getNotificationType()==1){
            holder.tvUpdate.setVisibility(View.VISIBLE);
        }else{
            holder.tvUpdate.setVisibility(View.GONE);
        }

        holder.txt_title.setText(warehouseNotificationList.getTitle());
        holder.txt_description.setText(warehouseNotificationList.getDescription());
    }

    @Override
    public int getItemCount() {
        return (null != notif ? notif.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_title, tvUpdate;
        TextView txt_description;

        public CustomViewHolder(View itemView) {
            super(itemView);
            tvUpdate = (TextView) itemView.findViewById(R.id.tvUpdate);
            tvUpdate.setOnClickListener(this);
            txt_title = (TextView) itemView.findViewById(R.id.tvHead);
            txt_description = (TextView) itemView.findViewById(R.id.tvDescription);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onClick(v, getAdapterPosition(), Constants.SERVICE_STORAGE);
        }
    }
}


