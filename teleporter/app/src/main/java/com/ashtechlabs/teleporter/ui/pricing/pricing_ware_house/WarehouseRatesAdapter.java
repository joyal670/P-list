package com.ashtechlabs.teleporter.ui.pricing.pricing_ware_house;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.common_interfaces.OnItemClickListener;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PricingWareHouse;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 01-Feb-17.
 */

public class WarehouseRatesAdapter extends RecyclerView.Adapter<WarehouseRatesAdapter.CustomViewHolder> {

    private ArrayList<PricingWareHouse> pricingList;
    private Activity mContext;
    private String[] currencyArray;
    private OnItemClickListener onItemClickListener;

    public WarehouseRatesAdapter(Activity context, ArrayList<PricingWareHouse> revs) {
        mContext = context;
        pricingList = revs;
        // this.commodityArray = context.getResources().getStringArray(R.array.array_commodity);
        this.currencyArray = context.getResources().getStringArray(R.array.array_currency);
    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    @Override
    public WarehouseRatesAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_rates_warehouse, viewGroup, false);
        return new WarehouseRatesAdapter.CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final WarehouseRatesAdapter.CustomViewHolder holder, final int position) {

        final PricingWareHouse pricingWareHouse = pricingList.get(position);
        holder.txtLocation.setText(pricingWareHouse.getLocation());
        holder.tvRateCardId.setText(pricingWareHouse.getRateCardID());
        try {
            if (pricingWareHouse.getRatecard_expiry_status().equals("0")) {
                holder.tvRateExpiry.setText(CommonUtils.isGoingToExpire(pricingWareHouse.getRate_validity()));
            } else {
                holder.tvRateExpiry.setText("Expired");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            holder.tvRateExpiry.setText("");
        }

        if (pricingWareHouse.getAdditional_info().equals("0")) {
            holder.txtCommodity.setText("General");
        } else if ((pricingWareHouse.getAdditional_info().equals("1"))) {
            holder.txtCommodity.setText("Hazardous");
        } else if ((pricingWareHouse.getAdditional_info().equals("2"))) {
            if (pricingWareHouse.getPerishable_det().equals("1")) {
                holder.txtCommodity.setText("Perishable - Cool");
            }
            if (pricingWareHouse.getPerishable_det().equals("2")) {
                holder.txtCommodity.setText("Perishable - Cold");
            }
            if (pricingWareHouse.getPerishable_det().equals("3")) {
                holder.txtCommodity.setText("Perishable - Frozen");
            }
        }

        holder.txtPCBM.setText(pricingWareHouse.getPCBM() + " " + currencyArray[Integer.parseInt(pricingWareHouse.getCurrency())].substring(0, 3));
        //holder.txtCommodity.setText(commodityArray[pricingWareHouse.getStorageType()]);

    }


    @Override
    public int getItemCount() {
        return (null != pricingList ? pricingList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLocation, tvRateCardId, tvRateExpiry;
        TextView txtPCBM;
        TextView txtCommodity;
        ImageView ivDelete;

        public CustomViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvRateCardId = (TextView) itemView.findViewById(R.id.tvRateCardId);
            tvRateExpiry = (TextView) itemView.findViewById(R.id.tvRateExpiry);
            txtLocation = (TextView) itemView.findViewById(R.id.txtLocation);
            txtPCBM = (TextView) itemView.findViewById(R.id.txtPCBM);
            txtCommodity = (TextView) itemView.findViewById(R.id.txtCommodity);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            ivDelete.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null) {
                if (v.getId() == R.id.ivDelete) {
                    onItemClickListener.onRateCardDelete(v, getAdapterPosition(), GlobalUtils.MODE_STORAGE);
                } else {
                    onItemClickListener.onItemClick(v, getAdapterPosition(), GlobalUtils.MODE_STORAGE);
                }
            }

        }
    }


}
