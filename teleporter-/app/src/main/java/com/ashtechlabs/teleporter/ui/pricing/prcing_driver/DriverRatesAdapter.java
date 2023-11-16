package com.ashtechlabs.teleporter.ui.pricing.prcing_driver;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.common_interfaces.OnItemClickListener;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceCourier;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 01-Feb-17.
 */
public class DriverRatesAdapter extends RecyclerView.Adapter<DriverRatesAdapter.CustomViewHolder> {

    ArrayList<PriceCourier> pricedriver = new ArrayList<>();
    Activity mParentActivity;
    private String[] deliveryType, currencyType;
    private OnItemClickListener onItemClickListener;

    public DriverRatesAdapter(Activity context, ArrayList<PriceCourier> pricedrivers) {
        mParentActivity = context;
        pricedriver = pricedrivers;
       // this.vehicleType = context.getResources().getStringArray(R.array.array_driver_service);
        this.deliveryType = context.getResources().getStringArray(R.array.array_delivery);
        this.currencyType = context.getResources().getStringArray(R.array.array_currency);
    }

    @Override
    public DriverRatesAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_rates_driver, viewGroup, false);
        return new DriverRatesAdapter.CustomViewHolder(view);
    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }


    @Override
    public void onBindViewHolder(final DriverRatesAdapter.CustomViewHolder holder, final int position) {

        final PriceCourier priceDriver = pricedriver.get(position);
        holder.txtLocationFrom.setText(priceDriver.getFromLocation());
        holder.txtLocationTo.setText(priceDriver.getToLocation());

        holder.tvRateCardId.setText(priceDriver.getRateCardID());
        try {
            if (priceDriver.getRatecardExpiryStatus().equals("0")) {
                holder.tvRateExpiry.setText(CommonUtils.isGoingToExpire(priceDriver.getRateValidity()));
            } else {
                holder.tvRateExpiry.setText("Expired");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            holder.tvRateExpiry.setText("");
        }
/*
        if (pricedriver.get(position).getDeliveryType() != null) {
            holder.txtLabel.setText("Delivery Type");
            holder.txtItemType.setText(deliveryType[Integer.parseInt(priceDriver.getDeliveryType())]);
        } else if (pricedriver.get(position).getVehicleType() != null) {
            holder.txtLabel.setText("Vehicle Type ");
            holder.txtItemType.setText(vehicleType[Integer.parseInt(priceDriver.getVehicleType())]);
        }
*/
        holder.txtMinAmount.setText(priceDriver.getMinAmount() + " " + currencyType[priceDriver.getCurrency()].substring(0, 3));
        holder.txtPerKgAmount.setText(priceDriver.getPerKGAmount() + " " + currencyType[priceDriver.getCurrency()].substring(0, 3));
        // holder.txtPerKgAmount.setText(priceDriver.getPerKGAmount());
        if (TextUtils.isEmpty(priceDriver.getDeliveryType())) {
            holder.txtItemType.setText(deliveryType[0]);
        } else {
            holder.txtItemType.setText(deliveryType[Integer.parseInt(priceDriver.getDeliveryType())]);
        }
        holder.txtDuration.setText(priceDriver.getDuration() + " hours");

    }


    @Override
    public int getItemCount() {
        return (null != pricedriver ? pricedriver.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLocationFrom;
        TextView tvRateCardId, tvRateExpiry;
        TextView txtLocationTo;
        TextView txtItemType;
        TextView txtLabel;
        TextView txtDuration;
        TextView txtMinAmount;
        ImageView ivDelete;
        TextView txtPerKgAmount;

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
                    onItemClickListener.onRateCardDelete(v, getAdapterPosition(), GlobalUtils.MODE_COURIER);
                } else {
                    onItemClickListener.onItemClick(v, getAdapterPosition(), GlobalUtils.MODE_COURIER);
                }
            }

        }
    }

}