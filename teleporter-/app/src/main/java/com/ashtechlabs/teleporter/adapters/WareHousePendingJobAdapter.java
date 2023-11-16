package com.ashtechlabs.teleporter.adapters;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseList;

import java.util.ArrayList;


/**
 * Created by IROID_ANDROID1 on 23-Jan-17.
 */

public class WareHousePendingJobAdapter extends RecyclerView.Adapter<WareHousePendingJobAdapter.CustomViewHolder> {

    private FragmentActivity activity;
    private ArrayList<WareHouseList> mlogs;
    private String[] commodityArray;

    public WareHousePendingJobAdapter(FragmentActivity context, ArrayList<WareHouseList> mlog) {
        this.activity = context;
        this.mlogs = mlog;
        this.commodityArray = context.getResources().getStringArray(R.array.array_commodity_copy);
    }

    @Override
    public WareHousePendingJobAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_warehouse_pending_jobs, viewGroup, false);
        return new WareHousePendingJobAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WareHousePendingJobAdapter.CustomViewHolder holder, final int position) {
        final WareHouseList wareHouseList = mlogs.get(position);
        if (wareHouseList.getOrder_number().equals(""))
        {
            holder.txtOrderNum.setText(wareHouseList.getOrderid());
        }
        else {
            holder.txtOrderNum.setText(wareHouseList.getOrder_number());
        }
        holder.txtLocation.setText(mlogs.get(position).getLocation());
        holder.txtDateFrom.setText(mlogs.get(position).getFromDate());
        holder.txtDateTo.setText(mlogs.get(position).getToDate());
        holder.txtSpace.setText(mlogs.get(position).getCBM() + " CBM");
        holder.txtTotalCBM.setText(mlogs.get(position).getTotalCBMAvailable() + " (Available Space in  CBM)");
        holder.txtItemType.setText(commodityArray[mlogs.get(position).getCommodityType()]);


    }


    @Override
    public int getItemCount() {
        return (null != mlogs ? mlogs.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtOrderNum;
        private TextView txtLocation;
        private TextView txtDateFrom;
        private TextView txtDateTo;

        private TextView txtSpace;
        private TextView txtTotalCBM;
        private TextView txtItemType;


        public CustomViewHolder(View itemView) {
            super(itemView);
            txtOrderNum = (TextView) itemView.findViewById(R.id.txtOrderNum);
            txtLocation = (TextView) itemView.findViewById(R.id.txtLocation);
            txtDateFrom = (TextView) itemView.findViewById(R.id.txtFromDate);
            txtDateTo = (TextView) itemView.findViewById(R.id.txtToDate);
            txtSpace = (TextView) itemView.findViewById(R.id.txtSpace);
            txtTotalCBM = (TextView) itemView.findViewById(R.id.txtTotalCBM);
            txtItemType = (TextView) itemView.findViewById(R.id.txtCommidity);

        }

    }

}





