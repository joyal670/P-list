package com.ashtechlabs.teleporter.adapters;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.OnMyLogsAdapterCallback;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.IJobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseList;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.util.ArrayList;


/**
 * Created by IROID_ANDROID1 on 23-Jan-17.
 */

public class WareHouseMyLogsAdapter extends RecyclerView.Adapter<WareHouseMyLogsAdapter.CustomViewHolder> implements JobStateControllerCallback {

    private FragmentActivity activity;
    private ArrayList<WareHouseList> mlogs;
    private IJobStateController IJobStateController;
    private OnMyLogsAdapterCallback onMyLogsAdapterCallback;
    private int pos;
    private String state;
    private String[] commodityArray;

    public WareHouseMyLogsAdapter(FragmentActivity context, ArrayList<WareHouseList> mlog) {
        this.activity = context;
        this.mlogs = mlog;
        this.IJobStateController = new JobStateController(this);
        this.commodityArray = context.getResources().getStringArray(R.array.array_commodity_copy);
    }

    @Override
    public WareHouseMyLogsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_my_logs_warehouse_new, viewGroup, false);
        return new WareHouseMyLogsAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WareHouseMyLogsAdapter.CustomViewHolder holder, final int position) {

       final WareHouseList wareHouseList = mlogs.get(position);

        holder.txtStatus.setVisibility(View.VISIBLE);
        if (wareHouseList.getOrder_number().equals("")){
            holder.txtOrderNum.setText(wareHouseList.getOrderid());

        }else {
            holder.txtOrderNum.setText(wareHouseList.getOrder_number());
        }
        holder.txtLocation.setText(wareHouseList.getLocation());
        holder.txtDateFrom.setText(wareHouseList.getFromDate());
        holder.txtDateTo.setText(mlogs.get(position).getToDate());
        holder.txtSpace.setText(wareHouseList.getCBM() + " CBM");
        holder.txtTotalCBM.setText(wareHouseList.getTotalCBMAvailable() + " CBM");
        holder.txtItemType.setText(commodityArray[wareHouseList.getCommodityType()]);
        
        if (mlogs.get(position).getState().equals(GlobalUtils.JOB_ACCEPTED)) {
            holder.btnEndService.setVisibility(View.VISIBLE);
            holder.layoutAcceptReject.setVisibility(View.VISIBLE);
            holder.btnStartService.setVisibility(View.VISIBLE);
            holder.btnStartService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    IJobStateController.onJobState(wareHouseList.getId(), GlobalUtils.JOB_STARTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));
                    pos = position;
                    state = GlobalUtils.JOB_STARTED;
                }
            });
            holder.btnEndService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    IJobStateController.onJobState(wareHouseList.getId(), GlobalUtils.JOB_ENDED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));
                    pos = position;
                    state = GlobalUtils.JOB_ENDED;
                }
            });
            holder.txtStatus.setText("You accepted Job. Please arrive to Customer and start Service.");
        }
        if (wareHouseList.getState().equals(GlobalUtils.JOB_REJECTED)) {
            holder.layoutAcceptReject.setVisibility(View.GONE);
            holder.layoutAcceptReject.setVisibility(View.GONE);
            holder.btnStartService.setVisibility(View.GONE);
            holder.txtStatus.setText("You rejected this Job.");
        }
        if (wareHouseList.getState().equals(GlobalUtils.JOB_STARTED)) {
            holder.layoutAcceptReject.setVisibility(View.VISIBLE);
            holder.btnStartService.setVisibility(View.GONE);
            holder.btnEndService.setVisibility(View.VISIBLE);
            holder.btnEndService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    IJobStateController.onJobState(wareHouseList.getId(), GlobalUtils.JOB_ENDED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));
                    pos = position;
                    state = GlobalUtils.JOB_ENDED;
                }
            });
            holder.txtStatus.setText("Started Job. Please provide service to Customer.");
        }
        if (wareHouseList.getState().equals(GlobalUtils.JOB_ENDED)) {
            holder.layoutAcceptReject.setVisibility(View.GONE);
            holder.btnStartService.setVisibility(View.GONE);
            holder.btnEndService.setVisibility(View.GONE);
            holder.txtStatus.setText("Ended Job.Awaiting customer's feedback now.");
        }
        if (wareHouseList.getState().equals(GlobalUtils.JOB_FINISHED)) {
            holder.layoutAcceptReject.setVisibility(View.GONE);
            holder.btnStartService.setVisibility(View.GONE);
            holder.btnEndService.setVisibility(View.GONE);
            holder.txtStatus.setText("Customer provided feedback to you.");
        }
    }

    public void setOnItemviewClickListner(OnMyLogsAdapterCallback onMyLogsAdapterCallback) {
        this.onMyLogsAdapterCallback = onMyLogsAdapterCallback;
    }

    @Override
    public int getItemCount() {
        return (null != mlogs ? mlogs.size() : 0);
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
    public void showLoadingIndicator() {
        onMyLogsAdapterCallback.showProgressAdapter();
    }

    @Override
    public void dismissLoadingIndicator() {
        onMyLogsAdapterCallback.dismissProgressAdapter();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtOrderNum;
        private TextView txtLocation;
        private TextView txtDateFrom;
        private TextView txtDateTo;
        private TextView txtStatus;
        private TextView txtSpace;
        private TextView txtTotalCBM;
        private TextView txtItemType;

        private Button btnStartService;
        private Button btnEndService;

        private LinearLayout layoutAcceptReject;


        public CustomViewHolder(View itemView) {
            super(itemView);
            txtOrderNum = (TextView) itemView.findViewById(R.id.txtOrderNum);
            txtLocation = (TextView) itemView.findViewById(R.id.txtLocation);
            txtDateFrom = (TextView) itemView.findViewById(R.id.txtFromDate);
            txtDateTo = (TextView) itemView.findViewById(R.id.txtToDate);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            txtSpace = (TextView) itemView.findViewById(R.id.txtSpace);
            txtTotalCBM = (TextView) itemView.findViewById(R.id.txtTotalCBM);
            txtItemType = (TextView) itemView.findViewById(R.id.txtCommidity);

            btnStartService = (Button) itemView.findViewById(R.id.btnAccept);
            btnEndService = (Button) itemView.findViewById(R.id.btnReject);

            layoutAcceptReject = (LinearLayout) itemView.findViewById(R.id.layoutAcceptReject);

        }

    }

}





