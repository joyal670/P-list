package com.ashtechlabs.teleporter.ui.pricing.prcing_trucking;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.common_interfaces.OnItemClickListener;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceTrucking;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 01-Feb-17.
 */
public class TruckingRatesAdapter extends RecyclerView.Adapter<TruckingRatesAdapter.CustomViewHolder> {

    ArrayList<PriceTrucking> pricedriver;
    Activity mParentActivity;
    private String[] currencyType;
    private OnItemClickListener onItemClickListener;

    public TruckingRatesAdapter(Activity context, ArrayList<PriceTrucking> pricedrive) {
        mParentActivity = context;
        this.pricedriver = pricedrive;

        this.currencyType = context.getResources().getStringArray(R.array.array_currency);
    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_rates_trucking, viewGroup, false);

        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {


       final PriceTrucking priceDriver = pricedriver.get(position);


        holder.txtLocationFrom.setText(priceDriver.getFromLocation());
        holder.txtLocationTo.setText(priceDriver.getToLocation());
        holder.txtItemType.setText(CommonUtils.getVechicleType(priceDriver.getVehicleType(), priceDriver.getSubVehicleType()));
       /* if (pricedriver.get(position).getDeliveryType() != null) {
            holder.txtLabel.setText("Delivery Type");
            holder.txtItemType.setText(deliveryType[Integer.parseInt(priceDriver.getDeliveryType())]);
        } else if (pricedriver.get(position).getVehicleType() != null) {
            holder.txtLabel.setText("Vehicle Type ");
            holder.txtItemType.setText(vehicleType[Integer.parseInt(priceDriver.getVehicleType())]);
        holder.txtItemType.setText(getVechicleType(priceDriver.getVehicleType(), priceDriver.getSubVehicleType()));
*/
        holder.tvRateCardId.setText(priceDriver.getRateCardID());
        try {
            if (priceDriver.getRatecard_expiry_status().equals("0")) {
                holder.tvRateExpiry.setText(CommonUtils.isGoingToExpire(priceDriver.getRateValidity()));
            } else {
                holder.tvRateExpiry.setText("Expired");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            holder.tvRateExpiry.setText("");
        }

        holder.txtMinAmount.setText(priceDriver.getMinAmount() + " "+currencyType[Integer.parseInt(priceDriver.getCurrency())].substring(0,3));
        holder.txtDuration.setText(priceDriver.getDuration() + " hours");

    }

    @Override
    public int getItemCount() {
        return (null != pricedriver ? pricedriver.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLocationFrom, tvRateCardId, tvRateExpiry;
        TextView txtLocationTo;
        TextView txtItemType;
        TextView txtLabel;
        TextView txtDuration;
        TextView txtMinAmount;
        TextView txtPerKgAmount;
        ImageView ivDelete;


        public CustomViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvRateCardId = (TextView) itemView.findViewById(R.id.tvRateCardId);
            tvRateExpiry = (TextView) itemView.findViewById(R.id.tvRateExpiry);
            txtLocationFrom = (TextView) itemView.findViewById(R.id.txtLocationFrom);
            txtLocationTo = (TextView) itemView.findViewById(R.id.txtLocationTo);
            txtItemType = (TextView) itemView.findViewById(R.id.txtItemType);
            txtLabel = (TextView) itemView.findViewById(R.id.txtLabel);
            txtDuration = (TextView) itemView.findViewById(R.id.txtDuration);
            txtMinAmount = (TextView) itemView.findViewById(R.id.txtMinAmount);
            txtPerKgAmount = (TextView) itemView.findViewById(R.id.txtPerKgAmount);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            ivDelete.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null) {
                if (v.getId() == R.id.ivDelete) {
                    onItemClickListener.onRateCardDelete(v, getAdapterPosition(), GlobalUtils.MODE_TRUCKING);
                } else {
                    onItemClickListener.onItemClick(v, getAdapterPosition(), GlobalUtils.MODE_TRUCKING);
                }
            }

        }
    }



}