package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.notifications;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.common_interfaces.OnRecyclerItemClickListener;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingNotificationList;
import com.ashtechlabs.teleporter.util.Constants;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class TruckingNotifyAdapter extends RecyclerView.Adapter<TruckingNotifyAdapter.CustomViewHolder> {

    OnRecyclerItemClickListener itemClickListener;
    Activity mContext;
    private ArrayList<TruckingNotificationList> notif;

    public TruckingNotifyAdapter(Activity context, ArrayList<TruckingNotificationList> revs) {
        mContext = context;
        notif = revs;
    }

    public void setClickListener(OnRecyclerItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_notification, viewGroup, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        TruckingNotificationList notificationList = notif.get(position);

        if(notificationList.getNotificationType()==1){
            holder.tvUpdate.setVisibility(View.VISIBLE);
        }else{
            holder.tvUpdate.setVisibility(View.GONE);
        }

        holder.txt_title.setText(notificationList.getTitle());
        holder.txt_description.setText(notificationList.getDescription());
    }

    @Override
    public int getItemCount() {
        return (null != notif ? notif.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            if(itemClickListener!=null)
                itemClickListener.onClick(v,  getAdapterPosition(), Constants.SERVICE_TRUCKING);
        }
    }


}


