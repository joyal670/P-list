package com.ashtechlabs.teleporter.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.MyLogsAndOrderInfoTrucking;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Feb-17.
 */

public class TruckingPendingJobsAdapter extends RecyclerView.Adapter<TruckingPendingJobsAdapter.CustomViewHolder>  {

    private ArrayList<MyLogsAndOrderInfoTrucking> myLogsList;
    private Activity activity;


    public TruckingPendingJobsAdapter(Activity context, ArrayList<MyLogsAndOrderInfoTrucking> myLogsList) {
        this.activity = context;
        this.myLogsList = myLogsList;
    }

    @Override
    public TruckingPendingJobsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.driver_pending_jobs, viewGroup, false);
        return new TruckingPendingJobsAdapter.CustomViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final TruckingPendingJobsAdapter.CustomViewHolder holder, final int position) {

        final MyLogsAndOrderInfoTrucking myLog = myLogsList.get(position);

        if (myLog.getOrder_number().equals("")){
            holder.txtOrderNum.setText(myLog.getOrderid());
        }else {
            holder.txtOrderNum.setText(myLog.getOrder_number());
        }
        holder.txtTime.setText(myLog.getTime());
        holder.txtDate.setText(myLog.getDate());
        holder.txtLocationFrom.setText(myLog.getFromlocation());
        holder.txtLocationTo.setText(myLog.getTolocation());
        holder.txtItemType.setText(myLog.getItemtype());

    }

    @Override
    public int getItemCount() {
        return (null != myLogsList ? myLogsList.size() : 0);
    }



    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtOrderNum;
        private TextView txtTime;
        private TextView txtDate;
        private TextView txtLocationFrom;
        private TextView txtLocationTo;
        private TextView txtItemType;



        public CustomViewHolder(View itemView) {
            super(itemView);
            txtOrderNum = (TextView) itemView.findViewById(R.id.txtOrderNum);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtLocationFrom = (TextView) itemView.findViewById(R.id.txtLocationFrom);
            txtLocationTo = (TextView) itemView.findViewById(R.id.txtLocationTo);
            txtItemType = (TextView) itemView.findViewById(R.id.txtItemType);

        }

    }
}



