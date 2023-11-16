package com.ashtechlabs.teleporter.adapters;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 09-Feb-17.
 */

public class VendorPendingJobAdapter extends RecyclerView.Adapter<VendorPendingJobAdapter.CustomViewHolder> {

    ArrayList<VendorList> mlogs;
    FragmentActivity activity;
    private String[] commodityArray;

    public VendorPendingJobAdapter(FragmentActivity context, ArrayList<VendorList> mlog) {
        this.activity = context;
        this.mlogs = mlog;
        this.commodityArray = context.getResources().getStringArray(R.array.array_commodity_copy);
    }

    @Override
    public VendorPendingJobAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_vendor_pending_jobs, viewGroup, false);
        return new VendorPendingJobAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VendorPendingJobAdapter.CustomViewHolder holder, final int position) {
        final VendorList vendorList = mlogs.get(position);
        if (vendorList.getOrder_number().equals("")){
            holder.txtOrderNum.setText(vendorList.getOrderid());
        }else {
            holder.txtOrderNum.setText(vendorList.getOrder_number());
        }
        holder.txtTime.setText(mlogs.get(position).getDate().substring(11));
        holder.txtDate.setText(mlogs.get(position).getDate().substring(0, 10));
        holder.txtLocationFrom.setText(mlogs.get(position).getFromLocation());
        holder.txtLocationTo.setText(mlogs.get(position).getToLocation());
        holder.txtItemType.setText(commodityArray[Integer.parseInt(mlogs.get(position).getCommodityType())]);


    }

    @Override
    public int getItemCount() {
        return (null != mlogs ? mlogs.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderNum;
        TextView txtTime;
        TextView txtDate;
        TextView txtLocationFrom;
        TextView txtLocationTo;
        TextView txtItemType;


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



