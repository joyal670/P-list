package com.ashtechlabs.teleporter.adapters;

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
import com.ashtechlabs.teleporter.dialog_fragments.CustomerDetailDialogFragment;
import com.ashtechlabs.teleporter.ui.OnMyLogsAdapterCallback;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.MyLogsAndOrderInfoDriver;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.CustomerDetailControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.IJobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateControllerCallback;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Feb-17.
 */

public class DriverMyLogsAdapter extends RecyclerView.Adapter<DriverMyLogsAdapter.CustomViewHolder> implements
        JobStateControllerCallback, CustomerDetailControllerCallback, CustomerDetailDialogListener {

    private ArrayList<MyLogsAndOrderInfoDriver> myLogsList;
    private IJobStateController IJobStateController;
    private FragmentActivity activity;
    private OnMyLogsAdapterCallback onMyLogsAdapterCallback;
    private int pos;
    private String state;


    public DriverMyLogsAdapter(FragmentActivity context, ArrayList<MyLogsAndOrderInfoDriver> myLogList) {
        this.activity = context;
        this.myLogsList = myLogList;
        this.notifyDataSetChanged();
        Log.e("CONSTRUCTOR", ">>>>>>>>>>");
        this.IJobStateController = new JobStateController(this);
    }

    @Override
    public DriverMyLogsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_my_logs_driver_new, viewGroup, false);
        return new DriverMyLogsAdapter.CustomViewHolder(view);
    }

    public void updateData(ArrayList<MyLogsAndOrderInfoDriver> viewModels) {
        myLogsList.clear();
        myLogsList.addAll(viewModels);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final DriverMyLogsAdapter.CustomViewHolder holder, final int position) {
        holder.txtStatus.setVisibility(View.VISIBLE);

        final MyLogsAndOrderInfoDriver myLog = myLogsList.get(position);
        if (myLog.getOrder_number().equals("")) {
            holder.txtOrderNum.setText(myLog.getOrderid());
        } else {
            holder.txtOrderNum.setText(myLog.getOrder_number());
        }

        holder.txtTime.setText(myLog.getTime());
        holder.txtDate.setText(myLog.getDate());
        holder.txtLocationFrom.setText(myLog.getFromlocation());
        holder.txtLocationTo.setText(myLog.getTolocation());
        holder.txtItemType.setText(myLog.getItemtype());

        if (myLog.getState().equals(GlobalUtils.JOB_ACCEPTED)) {

            holder.btnStartService.setVisibility(View.VISIBLE);
            holder.btnViewInMap.setVisibility(View.VISIBLE);
            holder.layoutAcceptReject.setVisibility(View.VISIBLE);
            holder.btnEndService.setVisibility(View.VISIBLE);
            holder.btnStartService.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    IJobStateController.onJobState(myLog.getId(), GlobalUtils.JOB_STARTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));
                    pos = position;
                    state = GlobalUtils.JOB_STARTED;
                }
            });

            holder.btnViewInMap.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    onMyLogsAdapterCallback.onViewInMapClick(myLog);
                }
            });

            holder.txtStatus.setText("You accepted Job. Please arrive to Customer and start Service.");
        }


        if (myLog.getState().equals(GlobalUtils.JOB_REJECTED)) {
            holder.layoutAcceptReject.setVisibility(View.GONE);
            holder.btnStartService.setVisibility(View.GONE);
            holder.btnEndService.setVisibility(View.GONE);
            holder.btnViewInMap.setVisibility(View.GONE);
            holder.txtStatus.setText("You rejected this Job.");
        }

        if (myLog.getState().equals(GlobalUtils.JOB_STARTED)) {
            holder.layoutAcceptReject.setVisibility(View.VISIBLE);
            holder.btnStartService.setVisibility(View.GONE);
            holder.btnEndService.setVisibility(View.VISIBLE);
            holder.btnViewInMap.setVisibility(View.VISIBLE);
            holder.btnEndService.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    IJobStateController.onJobState(myLog.getId(), GlobalUtils.JOB_ENDED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));
                    pos = position;
                    state = GlobalUtils.JOB_ENDED;

                }
            });

            holder.btnViewInMap.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    onMyLogsAdapterCallback.onViewInMapClick(myLog);
                }
            });

            holder.txtStatus.setText("Started Job. Please provide service to Customer.");
        }

        if (myLog.getState().equals(GlobalUtils.JOB_ENDED)) {
            holder.layoutAcceptReject.setVisibility(View.GONE);
            holder.btnStartService.setVisibility(View.GONE);
            holder.btnEndService.setVisibility(View.GONE);
            holder.btnViewInMap.setVisibility(View.GONE);
            holder.txtStatus.setText("Ended Job.Awaiting customer's feedback now.");
        }


        if (myLog.getState().equals(GlobalUtils.JOB_FINISHED)) {
            holder.layoutAcceptReject.setVisibility(View.GONE);
            holder.btnStartService.setVisibility(View.GONE);
            holder.btnEndService.setVisibility(View.GONE);
            holder.btnViewInMap.setVisibility(View.GONE);
            holder.txtStatus.setText("Customer provided feedback to you.");
        }

    }

    @Override
    public int getItemCount() {
        Log.e("SIZEEEEEEEEEE", " " + myLogsList.size());
        return (null != myLogsList ? myLogsList.size() : 0);
    }

    @Override
    public void onGetJobStateSuccess(String message) {
        onMyLogsAdapterCallback.showMessageAlert("Successful", message);
        myLogsList.get(pos).setState(state);
        notifyDataSetChanged();
    }

    @Override
    public void onGetJobStateFailed(String message) {
        onMyLogsAdapterCallback.showMessageAlert("Failed", message);
    }

    @Override
    public void onGetCustomerDetails(String name, String phone) {
        CustomerDetailDialogFragment customerDetailDialogFragment = CustomerDetailDialogFragment.newInstance(name, phone);
        customerDetailDialogFragment.setListner(DriverMyLogsAdapter.this);
        customerDetailDialogFragment.show(activity.getSupportFragmentManager(), "customer_detail");
    }

    @Override
    public void onGetCustomerDetailsFailed(String message) {
        onMyLogsAdapterCallback.showMessageAlert("Failed", message);
    }


    public void setOnItemviewClickListner(OnMyLogsAdapterCallback onMyLogsAdapterCallback) {
        this.onMyLogsAdapterCallback = onMyLogsAdapterCallback;
    }

    @Override
    public void showLoadingIndicator() {
        onMyLogsAdapterCallback.showProgressAdapter();
    }

    @Override
    public void dismissLoadingIndicator() {
        onMyLogsAdapterCallback.dismissProgressAdapter();
    }

    @Override
    public void onAcceptTheOrder() {
        IJobStateController.onJobState(myLogsList.get(pos).getId(), GlobalUtils.JOB_STARTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));
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
        private Button btnViewInMap;
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
            btnViewInMap = (Button) itemView.findViewById(R.id.btnViewInMap);
            layoutAcceptReject = (LinearLayout) itemView.findViewById(R.id.layoutAcceptReject);

        }

    }
}



