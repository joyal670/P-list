package com.ashtechlabs.teleporter.declared_orders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.TeleporterApp;
import com.ashtechlabs.teleporter.common_interfaces.OnRecyclerItemClickListener;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.DeclaredOrders;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;

import java.util.ArrayList;

/**
 * Created by VIDHU on 2/14/2017.
 */
public class DeclaredOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnRecyclerItemClickListener itemClickListener;
    private ArrayList<DeclaredOrders> declaredOrders;
    private String[] arrayDelivery;
    private String[] arrayCommidity;
   // private String[] arrayMover;
    private Context mContext;
    private String[] deliveryType;

    public DeclaredOrdersAdapter(Context context, ArrayList<DeclaredOrders> declaredOrders) {
        this.declaredOrders = declaredOrders;
        this.mContext = context;
        arrayDelivery = TeleporterApp.ARRAY_DELIVERY;
        arrayCommidity = TeleporterApp.ARRAY_COMMODITY;
       // arrayMover = TeleporterApp.ARRAY_MOVER;
        deliveryType = context.getResources().getStringArray(R.array.array_delivery);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("onCreateViewHolder  ", ""+declaredOrders.size());

        if (viewType == Constants.SERVICE_COURIER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_delivery_declared_orders, parent, false);
            return new DeliveryDeclaredOrderHolder(view); // view holder for normal items
        } else if (viewType == Constants.SERVICE_TRUCKING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mover_declared_orders,parent, false);
            return new MoverDeclaredOrderHolder(view); // view holder for header items
        } else if (viewType == Constants.SERVICE_CARGO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cargo_declared_orders, parent, false);
            return new CargoDeclaredOrderHolder(view); // view holder for header items
        } else if (viewType == Constants.SERVICE_STORAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_storage_declared_orders, parent, false);
            return new StorageDeclaredOrderHolder(view); // view holder for header items
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DeclaredOrders order = declaredOrders.get(position);
        Log.d("onBindViewHolder  ", ""+declaredOrders.size());
        int itemType = getItemViewType(position);
        if (itemType == Constants.SERVICE_COURIER) {
            ((DeliveryDeclaredOrderHolder) holder).bindData(order);
        } else if (itemType == Constants.SERVICE_CARGO) {
            ((CargoDeclaredOrderHolder) holder).bindData(order);
        } else if (itemType == Constants.SERVICE_TRUCKING) {
            ((MoverDeclaredOrderHolder) holder).bindData(order);
        } else if (itemType == Constants.SERVICE_STORAGE) {
            ((StorageDeclaredOrderHolder) holder).bindData(order);
        }

    }

    @Override
    public int getItemViewType(int position) {

        Log.d("getItemViewType  ", ""+declaredOrders.size());
        DeclaredOrders order = declaredOrders.get(position);

        if (order.getServiceType() == Constants.SERVICE_COURIER) {
            return Constants.SERVICE_COURIER;
        } else if (order.getServiceType() == Constants.SERVICE_CARGO) {
            return Constants.SERVICE_CARGO;
        } else if (order.getServiceType() == Constants.SERVICE_TRUCKING) {
            return Constants.SERVICE_TRUCKING;
        } else {
            return Constants.SERVICE_STORAGE;
        }

    }

    public void setClickListener(OnRecyclerItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        Log.d("getItemCount  ", ""+declaredOrders.size());
        return declaredOrders.size();
    }

    private class DeliveryDeclaredOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvDate, tvTime, tvType, tvFromLocation, tvToLocation, tvServiceType, tvWeight;
        private ImageView ivServiceIcon;

        DeliveryDeclaredOrderHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ivServiceIcon = (ImageView) itemView.findViewById(R.id.ivServiceIcon);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvFromLocation = (TextView) itemView.findViewById(R.id.tvFromLocation);
            tvToLocation = (TextView) itemView.findViewById(R.id.tvToLocation);
            tvWeight = (TextView) itemView.findViewById(R.id.tvWeight);
            tvServiceType = (TextView) itemView.findViewById(R.id.tvServiceType);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onClick(view, getAdapterPosition(),Constants.SERVICE_COURIER);
        }

        void bindData(DeclaredOrders order) {
            tvDate.setText(order.getDate().substring(0, 10));
            tvTime.setText(order.getDate().substring(11));
            //deliveryType[extras.getInt("delivery_type")
            tvType.setText(deliveryType[order.getItemType()]);
            tvFromLocation.setText(order.getFromLocation());
            tvToLocation.setText(order.getToLocation());
            tvWeight.setText(order.getWeight());
            tvWeight.setText("Delivery");
        }
    }

    private class CargoDeclaredOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvDate, tvTime, tvType, tvFromLocation, tvToLocation, tvServiceType, tvCbm;
        private ImageView ivServiceIcon;
        private LinearLayout containerAccessoriesCharges;
        private CheckBox cbInsurance;

        CargoDeclaredOrderHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ivServiceIcon = (ImageView) itemView.findViewById(R.id.ivServiceIcon);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvCbm = (TextView) itemView.findViewById(R.id.tvCbm);
            tvFromLocation = (TextView) itemView.findViewById(R.id.tvFromLocation);
            tvToLocation = (TextView) itemView.findViewById(R.id.tvToLocation);
            tvServiceType = (TextView) itemView.findViewById(R.id.tvServiceType);
            containerAccessoriesCharges = (LinearLayout) itemView.findViewById(R.id.containerAccessoriesCharges);
            cbInsurance = (CheckBox) itemView.findViewById(R.id.cbInsurance);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onClick(view, getAdapterPosition(),Constants.SERVICE_CARGO);
        }

        void bindData(DeclaredOrders order) {
            tvDate.setText(order.getDate().substring(0, 10));
            tvTime.setText(order.getDate().substring(11));
            tvType.setText(arrayCommidity[order.getCommodityType()]);
            tvFromLocation.setText(order.getFromLocation());
            tvToLocation.setText(order.getToLocation());
            tvCbm.setText(order.getCbm());
            if(order.getServiceMode()== 0){
                tvCbm.setText("Door to Door");
            }else if(order.getServiceMode()== 1){
                tvCbm.setText("Port to Port");
            }else if(order.getServiceMode()== 2){
                tvCbm.setText("Door to Port");
            }else if(order.getServiceMode()== 3){
                tvCbm.setText("Port to Door");
            }

            tvServiceType.setText("Cargo");
//            if (order.getHazardous() == 1 || order.getAddInsurance() == 1) {
//                containerAccessoriesCharges.setVisibility(View.VISIBLE);
//            } else {
//                containerAccessoriesCharges.setVisibility(View.GONE);
//            }
//            if (order.getHazardous() == 1) {
//                cbHazardous.setVisibility(View.VISIBLE);
//            } else {
//                cbHazardous.setVisibility(View.GONE);
//            }
            if (order.getAddInsurance() == 1) {
                cbInsurance.setVisibility(View.VISIBLE);
            } else {
                cbInsurance.setVisibility(View.GONE);
            }
        }
    }

    private class MoverDeclaredOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvDate, tvTime, tvType, tvFromLocation, tvToLocation, tvServiceType, tvWeight;
        private ImageView ivServiceIcon;


        MoverDeclaredOrderHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ivServiceIcon = (ImageView) itemView.findViewById(R.id.ivServiceIcon);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvFromLocation = (TextView) itemView.findViewById(R.id.tvFromLocation);
            tvToLocation = (TextView) itemView.findViewById(R.id.tvToLocation);
            tvWeight = (TextView) itemView.findViewById(R.id.tvWeight);
            tvServiceType = (TextView) itemView.findViewById(R.id.tvServiceType);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onClick(view, getAdapterPosition(),Constants.SERVICE_TRUCKING);
        }

        void bindData(DeclaredOrders order) {
            tvDate.setText(order.getDate().substring(0, 10));
            tvTime.setText(order.getDate().substring(11));
            tvType.setText(CommonUtils.getVechicleType(order.getVehicleType(), order.getSubVehicleType()));
            tvFromLocation.setText(order.getFromLocation());
            tvToLocation.setText(order.getToLocation());
            tvWeight.setText(order.getWeight());
            tvWeight.setText("Trucking");
        }
    }

    private class StorageDeclaredOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvDate, tvType, tvLocation, Location, tvServiceType, tvCbm;
        private ImageView ivServiceIcon;

        StorageDeclaredOrderHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ivServiceIcon = (ImageView) itemView.findViewById(R.id.ivServiceIcon);
            tvServiceType = (TextView) itemView.findViewById(R.id.tvServiceType);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            tvCbm = (TextView) itemView.findViewById(R.id.tvCbm);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onClick(view, getAdapterPosition(),Constants.SERVICE_STORAGE);
        }

        void bindData(DeclaredOrders order) {
            tvServiceType.setText("Storage");
            tvType.setText(arrayCommidity[order.getCommodityType()]);
            tvLocation.setText(order.getLocation());
            tvCbm.setText(order.getCbm() + " CBM "+order.getId());
            tvDate.setText(order.getFromDate() + " - " + order.getToDate());
        }
    }
}
