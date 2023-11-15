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
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.TruckingDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.order_info.ITruckingVewOrderClick;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingMpService;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.util.ArrayList;
import java.util.Arrays;


public class TruckingOrderInfoAdapter extends RecyclerView.Adapter<TruckingOrderInfoAdapter.CustomViewHolder> implements
        JobStateControllerCallback, CustomerDetailControllerCallback, CustomerDetailDialogListener {

    private ITruckingVewOrderClick iVewOrderClick;
    private OnOrderInfoAdapterCallback onOrderInfoAdapterCallback;
    private ArrayList<TruckingMpService> orderInfoList;
    private IJobStateController IJobStateController;
    private FragmentActivity activity;
    private ICustomerDetailController ICustomerDetailController;
    private int pos;
    private ArrayList<String> arrayCommodity;


    public TruckingOrderInfoAdapter(FragmentActivity context, ArrayList<TruckingMpService> orderInfoList) {
        this.activity = context;
        this.orderInfoList = orderInfoList;
        this.IJobStateController = new JobStateController(this);
        this.ICustomerDetailController = new CustomerDetailController(this);
        arrayCommodity = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.array_commodity_copy)));
    }

    @Override
    public TruckingOrderInfoAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_order_info_trucking, viewGroup, false);
        return new TruckingOrderInfoAdapter.CustomViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final TruckingOrderInfoAdapter.CustomViewHolder holder, final int position) {

        final TruckingMpService orderInfo = orderInfoList.get(position);
        pos = position;

  /*      holder.btnAccepts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pos = position;
                ICustomerDetailController.onCustomerController(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""), orderInfo.getOrderid(), orderInfo.getOrdertype());
            }
        });

        holder.btnRejects.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pos = position;
                IJobStateController.onJobState(orderInfo.getId(), GlobalUtils.JOB_REJECTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));
            }
        });*/
        if (orderInfo.getOrder_number().equals("")) {
            holder.txtOrderNum.setText(orderInfo.getOrderid());
        } else {
            holder.txtOrderNum.setText(orderInfo.getOrder_number());
        }
        holder.txtDate.setText(orderInfo.getDate().substring(0, 10));
        if (orderInfo.getDate().length() > 11) {
            holder.txtTime.setText(orderInfo.getDate().substring(11));
        }
        holder.txtLocationFrom.setText(orderInfo.getFromlocation());
        holder.txtLocationTo.setText(orderInfo.getTolocation());
        holder.txtCommodity.setText(arrayCommodity.get(orderInfo.getCommodityType()));

        holder.txtItemType.setText(CommonUtils.getVechicleType(String.valueOf(orderInfo.getVehicletype()), String.valueOf(orderInfo.getSubVehicleType())));

    }

    @Override
    public int getItemCount() {
        return (null != orderInfoList ? orderInfoList.size() : 0);
    }

    @Override
    public void onGetJobStateSuccess(String message) {
        onOrderInfoAdapterCallback.showMessageAlert("Successful", message);
        orderInfoList.get(pos).setState(GlobalUtils.JOB_ACCEPTED);
        TruckingDashBoardActivity.myLogsList.add(orderInfoList.get(pos));
        orderInfoList.remove(pos);
        notifyDataSetChanged();
    }

    public void setOnItemviewClickListner(ITruckingVewOrderClick iVewOrderClick) {
        this.iVewOrderClick = iVewOrderClick;
    }


    @Override
    public void onGetJobStateFailed(String message) {
        onOrderInfoAdapterCallback.showMessageAlert("Failed", message);
    }

    @Override
    public void onGetCustomerDetails(String name, String phone) {
        CustomerDetailDialogFragment customerDetailDialogFragment = CustomerDetailDialogFragment.newInstance(name, phone);
        customerDetailDialogFragment.setListner(TruckingOrderInfoAdapter.this);
        customerDetailDialogFragment.show(activity.getSupportFragmentManager(), "customer_detail");
    }

    @Override
    public void onGetCustomerDetailsFailed(String message) {
        onOrderInfoAdapterCallback.showMessageAlert("Failed", message);
    }


    @Override
    public void showLoadingIndicator() {
        onOrderInfoAdapterCallback.showProgressAdapter();
    }

    @Override
    public void dismissLoadingIndicator() {
        onOrderInfoAdapterCallback.dismissProgressAdapter();
    }

    @Override
    public void onAcceptTheOrder() {
        IJobStateController.onJobState(orderInfoList.get(pos).getId(), GlobalUtils.JOB_ACCEPTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtOrderNum;
        private TextView txtTime;
        private TextView txtDate;
        private TextView txtLocationFrom;
        private TextView txtLocationTo;
        private Button btnviewOrder;
        /*private Button btnAccepts;
        private Button btnRejects;*/
        private TextView txtItemType;
        private TextView txtCommodity;


        public CustomViewHolder(View itemView) {
            super(itemView);
            txtOrderNum = (TextView) itemView.findViewById(R.id.txtOrderNum);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtLocationFrom = (TextView) itemView.findViewById(R.id.txtLocationFrom);
            txtLocationTo = (TextView) itemView.findViewById(R.id.txtLocationTo);
            txtItemType = (TextView) itemView.findViewById(R.id.txtItemType);
            txtCommodity = (TextView) itemView.findViewById(R.id.txtCommodity);

            btnviewOrder = (Button) itemView.findViewById(R.id.btnviewOrder);
          /*  btnAccepts = (Button) itemView.findViewById(R.id.btnAccept);
            btnRejects = (Button) itemView.findViewById(R.id.btnReject);*/
            btnviewOrder.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            iVewOrderClick.onViewOrder(orderInfoList.get(getAdapterPosition()), getAdapterPosition());
        }
    }
}



