package com.ashtechlabs.teleporter.adapters;

import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.common_interfaces.CustomerDetailDialogListener;
import com.ashtechlabs.teleporter.common_interfaces.OnMyLogsVendorAdapterCallback;
import com.ashtechlabs.teleporter.dialog_fragments.CustomerDetailDialogFragment;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.CustomerDetailControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.IJobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.update_track_trace.DeliveryMileStoneActivity;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 09-Feb-17.
 */

public class VendorMyLogsAdapter extends RecyclerView.Adapter<VendorMyLogsAdapter.CustomViewHolder> implements
        JobStateControllerCallback, CustomerDetailControllerCallback, CustomerDetailDialogListener {

    private ArrayList<VendorList> mlogs;
    private IJobStateController IJobStateController;
    private FragmentActivity activity;
    private String[] commodityArray;
    private OnMyLogsVendorAdapterCallback onMyLogsAdapterCallback;
    private int pos;
    private String state;


    public VendorMyLogsAdapter(FragmentActivity context, ArrayList<VendorList> mlog) {
        this.activity = context;
        this.mlogs = mlog;
        this.IJobStateController = new JobStateController(this);
        this.commodityArray = context.getResources().getStringArray(R.array.array_commodity_copy);
    }

    @Override
    public VendorMyLogsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_my_logs_vendor_new, viewGroup, false);
        return new VendorMyLogsAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VendorMyLogsAdapter.CustomViewHolder holder, final int position) {

        final VendorList vendorList = mlogs.get(position);

        holder.txtStatus.setVisibility(View.VISIBLE);
        if (vendorList.getOrder_number().equals("")){
            holder.txtOrderNum.setText(vendorList.getOrderid());
        }else {
            holder.txtOrderNum.setText(vendorList.getOrder_number());
        }
        if (vendorList.getDate().length() > 11) {
            holder.txtTime.setText(vendorList.getDate().substring(11));
        }
        holder.txtDate.setText(vendorList.getDate().substring(0, 10));
        holder.txtLocationFrom.setText(vendorList.getFromLocation());
        holder.txtLocationTo.setText(vendorList.getToLocation());
        holder.txtItemType.setText(commodityArray[Integer.parseInt(vendorList.getCommodityType())]);

        if (vendorList.getState().equals(GlobalUtils.JOB_ACCEPTED)) {
            holder.layoutAcceptReject.setVisibility(View.VISIBLE);
            holder.btnStartService.setVisibility(View.VISIBLE);
            holder.btnUpdateOrder.setVisibility(View.VISIBLE);
            holder.btnEndService.setVisibility(View.VISIBLE);
            holder.btnStartService.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    IJobStateController.onJobState(vendorList.getId(), GlobalUtils.JOB_STARTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));
                    pos = position;
                    state = GlobalUtils.JOB_STARTED;

                }
            });

            holder.btnUpdateOrder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                  //  onMyLogsAdapterCallback.onViewInMapClick(vendorList);
                    Intent i = new Intent(activity,DeliveryMileStoneActivity.class);
                    i.putExtra("transport",vendorList.getTransport());
                    i.putExtra("OrderID",vendorList.getOrderid());
                    i.putExtra("order_status",vendorList.getOrder_status());
                    activity.startActivity(i);
                }
            });

            holder.txtStatus.setText("You accepted Job. Please arrive to Customer and start Service.");
        }else if (vendorList.getState().equals(GlobalUtils.JOB_REJECTED)) {

            holder.layoutAcceptReject.setVisibility(View.GONE);
            holder.btnStartService.setVisibility(View.GONE);
            holder.btnEndService.setVisibility(View.GONE);
            holder.btnUpdateOrder.setVisibility(View.GONE);
            holder.txtStatus.setText("You rejected this Job.");
        } else if (vendorList.getState().equals(GlobalUtils.JOB_STARTED)) {

            holder.layoutAcceptReject.setVisibility(View.VISIBLE);
            holder.btnStartService.setVisibility(View.GONE);
            holder.btnEndService.setVisibility(View.VISIBLE);
            holder.btnUpdateOrder.setVisibility(View.VISIBLE);
            holder.btnEndService.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    IJobStateController.onJobState(vendorList.getId(), GlobalUtils.JOB_ENDED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));
                    pos = position;
                    state = GlobalUtils.JOB_ENDED;
                }
            });

            holder.btnUpdateOrder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    //  onMyLogsAdapterCallback.onViewInMapClick(vendorList);
                    Intent i = new Intent(activity,DeliveryMileStoneActivity.class);
                    i.putExtra("transport",vendorList.getTransport());
                    i.putExtra("OrderID",vendorList.getOrderid());
                    i.putExtra("order_status",vendorList.getOrder_status());
                    activity.startActivity(i);
                }
            });

            holder.txtStatus.setText("Started Job. Please provide service to Customer.");
        } else if (vendorList.getState().equals(GlobalUtils.JOB_ENDED)) {
            holder.layoutAcceptReject.setVisibility(View.GONE);
            holder.btnStartService.setVisibility(View.GONE);
            holder.btnEndService.setVisibility(View.GONE);
            holder.btnUpdateOrder.setVisibility(View.GONE);
            holder.txtStatus.setText("Ended Job.Awaiting customer's feedback now.");
        }  else if (vendorList.getState().equals(GlobalUtils.JOB_FINISHED)) {
            holder.layoutAcceptReject.setVisibility(View.GONE);
            holder.btnStartService.setVisibility(View.GONE);
            holder.btnEndService.setVisibility(View.GONE);
            holder.btnUpdateOrder.setVisibility(View.GONE);
            holder.txtStatus.setText("Customer provided feedback to you.");
        }


    }

    @Override
    public int getItemCount() {
        Log.e("ORDER INFO SIZE >> ", " " + mlogs.size());
        return (null != mlogs ? mlogs.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onGetJobStateSuccess(String message) {
        onMyLogsAdapterCallback.showMessageAlert("Successful", message);
        mlogs.get(pos).setState(state);
        notifyDataSetChanged();
    }

    @Override
    public void onGetJobStateFailed(String message) {
        onMyLogsAdapterCallback.showMessageAlert("Failed", message);
    }

    @Override
    public void onGetCustomerDetails(String name, String phone) {
        CustomerDetailDialogFragment customerDetailDialogFragment = CustomerDetailDialogFragment.newInstance(name, phone);
        customerDetailDialogFragment.setListner(VendorMyLogsAdapter.this);
        customerDetailDialogFragment.show(activity.getSupportFragmentManager(), "customer_detail");
    }

    @Override
    public void onGetCustomerDetailsFailed(String message) {
        onMyLogsAdapterCallback.showMessageAlert("Failed", message);
    }


    public void setOnItemviewClickListner(OnMyLogsVendorAdapterCallback onMyLogsAdapterCallback) {
        this.onMyLogsAdapterCallback = onMyLogsAdapterCallback;
    }

    @Override
    public void onAcceptTheOrder() {
        IJobStateController.onJobState(mlogs.get(pos).getId(), GlobalUtils.JOB_STARTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));
    }

    @Override
    public void showLoadingIndicator() {
        onMyLogsAdapterCallback.showProgressAdapter();
    }

    @Override
    public void dismissLoadingIndicator() {
        onMyLogsAdapterCallback.dismissProgressAdapter();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtOrderNum;
        private TextView txtTime;
        private TextView txtDate;
        private TextView txtLocationFrom;
        private TextView txtLocationTo;
        private TextView txtStatus;
        private TextView txtItemType;

        private Button btnStartService;
        private Button btnEndService;
        private Button btnUpdateOrder;
        private LinearLayout layoutAcceptReject;


        public CustomViewHolder(View itemView) {
            super(itemView);
            txtOrderNum = (TextView) itemView.findViewById(R.id.txtOrderNum);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtLocationFrom = (TextView) itemView.findViewById(R.id.txtLocationFrom);
            txtLocationTo = (TextView) itemView.findViewById(R.id.txtLocationTo);
            txtItemType = (TextView) itemView.findViewById(R.id.txtItemType);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);


            btnStartService = (Button) itemView.findViewById(R.id.btnStartService);
            btnEndService = (Button) itemView.findViewById(R.id.btnEndService);
            btnUpdateOrder = (Button) itemView.findViewById(R.id.btnViewInMap);
            layoutAcceptReject = (LinearLayout) itemView.findViewById(R.id.layoutAcceptReject);

        }

    }
}



