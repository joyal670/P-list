package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.pojo.Vehicles;
import com.ashtechlabs.teleporter.util.CommonUtils;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 31-Jan-17.
 */
public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleTypeViewHolder> {
    Activity mContext;
    ArrayList<Vehicles> mVehiclesList;

    OnVehicleListItemClickListener itemClickListener;

    public VehicleAdapter(Activity context, ArrayList<Vehicles> vehicles) {
        this.mContext = context;
        this.mVehiclesList = vehicles;
    }

    @Override
    public VehicleTypeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_vehicle_type, viewGroup, false);
        return new VehicleTypeViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final VehicleTypeViewHolder holder, final int position) {

        Vehicles vehicles = mVehiclesList.get(position);
        holder.tvNumber.setText(vehicles.getVehicleNumber());
        holder.tvType.setText(CommonUtils.getVechicleType(vehicles.getVehicleType(), vehicles.getVehicleSubType()));
       // holder.tvType.setText(vehicles.getVehicleType()+vehicles.getVehicleSubType());
    }


    @Override
    public int getItemCount() {
        return (null != mVehiclesList ? mVehiclesList.size() : 0);
    }

    public void setClickListener(OnVehicleListItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    protected class VehicleTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvType, tvNumber;


        public VehicleTypeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onClick(view, getAdapterPosition(),
                        CommonUtils.getVechicleType(mVehiclesList.get(getAdapterPosition()).getVehicleType(),mVehiclesList.get(getAdapterPosition()).getVehicleSubType()),
                                mVehiclesList.get(getAdapterPosition()).getVehicleType(),
                                mVehiclesList.get(getAdapterPosition()).getVehicleSubType());
        }
    }
}


