package com.ashtechlabs.teleporter.ui.pricing.pricing_cargo;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.common_interfaces.OnItemClickListener;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceVendor;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */


public class PricingVendorAdapter extends RecyclerView.Adapter<PricingVendorAdapter.CustomViewHolder> {
    Activity mContext;
    ArrayList<PriceVendor> vendor = new ArrayList<PriceVendor>();
    private String[] commodityArray, currencyArray;
    private OnItemClickListener onItemClickListener;

    public PricingVendorAdapter(Activity context, ArrayList<PriceVendor> vendors) {
        mContext = context;
        vendor = vendors;
        this.commodityArray = context.getResources().getStringArray(R.array.array_commodity_copy);
        this.currencyArray = context.getResources().getStringArray(R.array.array_currency);
    }

    @Override
    public PricingVendorAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_rates_cargo, viewGroup, false);
        return new PricingVendorAdapter.CustomViewHolder(view);
    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(final PricingVendorAdapter.CustomViewHolder holder, final int position) {
        final PriceVendor priceVendor = vendor.get(position);
        holder.txtLocationFrom.setText(priceVendor.getFromLocation());
        holder.txtLocationTo.setText(priceVendor.getToLocation());
        holder.tvRateCardId.setText(priceVendor.getRateCardID());
        try {
            if (priceVendor.getRatecardExpiryStatus().equals("0")) {
                holder.tvRateExpiry.setText(CommonUtils.isGoingToExpire(priceVendor.getRateValidity()));
            } else {
                holder.tvRateExpiry.setText("Expired");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            holder.tvRateExpiry.setText("");
        }

        if(priceVendor.getTransport().equals("0")){
            holder.txtServiceMode.setText("Door to Door");
        }else if(priceVendor.getTransport().equals("1")){
            holder.txtServiceMode.setText("Port to Port");
        }else if(priceVendor.getTransport().equals("2")){
            holder.txtServiceMode.setText("Door to Port");
        }else if(priceVendor.getTransport().equals("3")){
            holder.txtServiceMode.setText("Port to Door");
        }

        if (priceVendor.getAdditionalInfo().equals("0")) {
            holder.txtItemType.setText("General");
        } else if ((priceVendor.getAdditionalInfo().equals("1"))) {
            holder.txtItemType.setText("Hazardous");
        } else if ((priceVendor.getAdditionalInfo().equals("2"))) {
            if (priceVendor.getPerishableDet().equals("1")) {
                holder.txtItemType.setText("Perishable - Cool");
            }
            if (priceVendor.getPerishableDet().equals("2")) {
                holder.txtItemType.setText("Perishable - Cold");
            }
            if (priceVendor.getPerishableDet().equals("3")) {
                holder.txtItemType.setText("Perishable - Frozen");
            }
        }

        holder.txtMinAmount.setText(priceVendor.getMinAmount() + " " + currencyArray[Integer.parseInt(priceVendor.getCurrency())].substring(0, 3));
        holder.txtPerKgAmount.setText(priceVendor.getPerKGAmount() + " " + currencyArray[Integer.parseInt(priceVendor.getCurrency())].substring(0, 3));
        //holder.txtPerKgAmount.setText(priceVendor.getPerKGAmount());
        holder.txtDuration.setText(priceVendor.getDuration() + " Hours");


    }

    @Override
    public int getItemCount() {
        return (null != vendor ? vendor.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLocationFrom, tvRateExpiry,tvRateCardId;
        TextView txtLocationTo, txtServiceMode;
        TextView txtItemType;
        TextView txtLabel;
        TextView txtDuration;
        TextView txtMinAmount;
        TextView txtPerKgAmount;
        ImageView ivDelete;


        public CustomViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtServiceMode = (TextView) itemView.findViewById(R.id.txtServiceMode);
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
                    onItemClickListener.onRateCardDelete(v, getAdapterPosition(), GlobalUtils.MODE_CARGO);
                } else {
                    onItemClickListener.onItemClick(v, getAdapterPosition(), GlobalUtils.MODE_CARGO);
                }
            }

        }
    }

}




