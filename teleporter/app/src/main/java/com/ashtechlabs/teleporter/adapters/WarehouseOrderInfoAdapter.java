package com.ashtechlabs.teleporter.adapters;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.common_interfaces.CustomerDetailDialogListener;
import com.ashtechlabs.teleporter.dialog_fragments.CustomerDetailDialogFragment;
import com.ashtechlabs.teleporter.ui.OnOrderInfoAdapterCallback;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.CustomerDetailController;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.CustomerDetailControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.ICustomerDetailController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.IJobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.WareHouseDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseList;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.warehouse_order_info.IVewWareHouseOrderClick;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.util.ArrayList;


/**
 * Created by IROID_ANDROID1 on 08-Feb-17.
 */

public class WarehouseOrderInfoAdapter extends RecyclerView.Adapter<WarehouseOrderInfoAdapter.CustomViewHolder> implements
        JobStateControllerCallback, CustomerDetailControllerCallback, CustomerDetailDialogListener {

    private OnOrderInfoAdapterCallback onOrderInfoAdapterCallback;
    private ArrayList<WareHouseList> morders;
    private IJobStateController IJobStateController;
    private FragmentActivity activity;
    private ICustomerDetailController ICustomerDetailController;
    private int pos;
    private IVewWareHouseOrderClick iVewOrderClick;
    private String[] commodityArray;


    public WarehouseOrderInfoAdapter(FragmentActivity context, ArrayList<WareHouseList> morder) {
        this.activity = context;
        this.morders = morder;
        this.IJobStateController = new JobStateController(this);
        this.ICustomerDetailController = new CustomerDetailController(this);
        this.commodityArray = context.getResources().getStringArray(R.array.array_commodity_copy);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public WarehouseOrderInfoAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_ware_house_order_info, viewGroup, false);
        return new WarehouseOrderInfoAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WarehouseOrderInfoAdapter.CustomViewHolder holder, final int position) {

        final WareHouseList wareHouseList = morders.get(position);

       // if (wareHouseList.getState().equals(GlobalUtils.JOB_ACTIVE)) {

        if (wareHouseList.getOrder_number().equals(""))
        {
            holder.txtOrderNum.setText(wareHouseList.getOrderid());
        }
        else {
            holder.txtOrderNum.setText(wareHouseList.getOrder_number());
        }
       
            holder.txtLocation.setText(wareHouseList.getLocation());
            holder.txtDateFrom.setText(wareHouseList.getFromDate());
            holder.txtDateTo.setText(wareHouseList.getToDate());
            holder.txtSpace.setText(wareHouseList.getCBM() + " CBM");
            holder.txtTotalCBM.setText(wareHouseList.getTotalCBMAvailable() + " (Available Space in CBM)");

            holder.txtItemType.setText(commodityArray[wareHouseList.getCommodityType()]);
/*

            holder.btnAccept.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    pos = position;
                    ICustomerDetailController.onCustomerController(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""), wareHouseList.getOrderid(), wareHouseList.getOrdertype());
                }
            });

            holder.btnReject.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    pos = position;
                    IJobStateController.onJobState(wareHouseList.getId(), GlobalUtils.JOB_REJECTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));
                }
            });
*/

       // }


    }

    public void setOnItemviewClickListner(IVewWareHouseOrderClick iVewOrderClick) {
        this.iVewOrderClick = iVewOrderClick;
    }

    @Override
    public int getItemCount() {
        return (null != morders ? morders.size() : 0);
    }

    @Override
    public void onGetJobStateSuccess(String message) {
        onOrderInfoAdapterCallback.showMessageAlert("Successful", message);
        morders.get(pos).setState(GlobalUtils.JOB_ACCEPTED);
        WareHouseDashBoardActivity.mlogs.add(morders.get(pos));
        morders.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public void onGetJobStateFailed(String message) {
        onOrderInfoAdapterCallback.showMessageAlert("Failed", message);
    }

    @Override
    public void onGetCustomerDetails(String name, String phone) {
        CustomerDetailDialogFragment customerDetailDialogFragment = CustomerDetailDialogFragment.newInstance(name, phone);
        customerDetailDialogFragment.setListner(WarehouseOrderInfoAdapter.this);
        customerDetailDialogFragment.show(activity.getSupportFragmentManager(), "customer_detail");
    }

    @Override
    public void onGetCustomerDetailsFailed(String message) {
        onOrderInfoAdapterCallback.showMessageAlert("Failed", message);
    }

    @Override
    public void onAcceptTheOrder() {
        IJobStateController.onJobState(morders.get(pos).getId(), GlobalUtils.JOB_ACCEPTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));
    }

    @Override
    public void showLoadingIndicator() {
        onOrderInfoAdapterCallback.showProgressAdapter();
    }

    @Override
    public void dismissLoadingIndicator() {
        onOrderInfoAdapterCallback.dismissProgressAdapter();

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtOrderNum;
        private TextView txtLocation;
        private TextView txtDateFrom;
        private TextView txtDateTo;
        private TextView txtSpace;
        private TextView txtTotalCBM;
        private TextView txtItemType;

        private Button btnviewOrder;
        private Button btnAccept;
        private Button btnReject;


        public CustomViewHolder(View itemView) {
            super(itemView);
            txtOrderNum = (TextView) itemView.findViewById(R.id.txtOrderNum);
            txtLocation = (TextView) itemView.findViewById(R.id.txtLocation);
            txtDateFrom = (TextView) itemView.findViewById(R.id.txtFromDate);
            txtDateTo = (TextView) itemView.findViewById(R.id.txtToDate);
            txtSpace = (TextView) itemView.findViewById(R.id.txtSpace);
            txtItemType = (TextView) itemView.findViewById(R.id.txtCommidity);
            txtTotalCBM = (TextView) itemView.findViewById(R.id.txtTotalCBM);
            /*btnAccept = (Button) itemView.findViewById(R.id.btnAccept);
            btnReject = (Button) itemView.findViewById(R.id.btnReject);*/
            btnviewOrder = (Button) itemView.findViewById(R.id.btnviewOrder);

            btnviewOrder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iVewOrderClick.onViewOrder(morders.get(getAdapterPosition()),getAdapterPosition());
        }
    }


}





