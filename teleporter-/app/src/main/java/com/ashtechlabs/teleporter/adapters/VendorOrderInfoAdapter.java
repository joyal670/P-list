package com.ashtechlabs.teleporter.adapters;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
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
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.CargoDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.order_info.IVewOrderClickVendor;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.util.ArrayList;


/**
 * Created by IROID_ANDROID1 on 09-Feb-17.
 */

public class VendorOrderInfoAdapter extends RecyclerView.Adapter<VendorOrderInfoAdapter.CustomViewHolder> implements
        JobStateControllerCallback, CustomerDetailControllerCallback, CustomerDetailDialogListener {
    private IVewOrderClickVendor iVewOrderClick;
    private ArrayList<VendorList> morders;
    private IJobStateController IJobStateController;
    private ICustomerDetailController ICustomerDetailController;
    private FragmentActivity activity;
    private int pos;
    private OnOrderInfoAdapterCallback onOrderInfoAdapterCallback;
    private String[] commodityArray;


    public VendorOrderInfoAdapter(FragmentActivity context, ArrayList<VendorList> morder) {
        this.activity = context;
        this.morders = morder;
        this.IJobStateController = new JobStateController(this);
        this.ICustomerDetailController = new CustomerDetailController(this);
        this.commodityArray = context.getResources().getStringArray(R.array.array_commodity_copy);
    }

    @Override
    public VendorOrderInfoAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_vendor_order_info, viewGroup, false);
        return new VendorOrderInfoAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VendorOrderInfoAdapter.CustomViewHolder holder, final int position) {

        VendorList vendorList = morders.get(position);

        Log.d("CURENCY", "" + vendorList.getCurrency());
        // if (vendorList.getState().equals(GlobalUtils.JOB_ACTIVE)) {
        if (vendorList.getOrder_number().equals("")) {
            holder.txtOrderNum.setText(vendorList.getOrderid());
        } else {
            holder.txtOrderNum.setText(vendorList.getOrder_number());
        }
        if (vendorList.getDate().length() > 11) {
            holder.txtTime.setText(vendorList.getDate().substring(11));
        }
        holder.txtDate.setText(vendorList.getDate().substring(0, 10));
        holder.txtLocationFrom.setText(vendorList.getFromLocation());
        holder.txtLocationTo.setText(vendorList.getToLocation());
        holder.txtItemType.setText(commodityArray[Integer.parseInt(vendorList.getCommodityType())]);

    /*    holder.btnAccept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                pos = position;
                ICustomerDetailController.onCustomerController(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""), vendorList.getOrderid(), vendorList.getOrdertype());
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pos = position;
                IJobStateController.onJobState(vendorList.getId(), GlobalUtils.JOB_REJECTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));
            }
        });*/


    }

    @Override
    public int getItemCount() {
        //Log.e("ORDER INFO SIZE >> ", " " + morders.size());
        return (null != morders ? morders.size() : 0);
    }

    @Override
    public void onGetJobStateSuccess(String message) {
        onOrderInfoAdapterCallback.showMessageAlert("Successful", message);
        morders.get(pos).setState(GlobalUtils.JOB_ACCEPTED);
        CargoDashBoardActivity.mlogs.add(morders.get(pos));
        morders.remove(pos);
        notifyDataSetChanged();
    }

    public void setOnItemviewClickListner(IVewOrderClickVendor iVewOrderClick) {
        this.iVewOrderClick = iVewOrderClick;
    }


    @Override
    public void onGetJobStateFailed(String message) {
        onOrderInfoAdapterCallback.showMessageAlert("Failed", message);
    }

    @Override
    public void onGetCustomerDetails(String name, String phone) {
        CustomerDetailDialogFragment customerDetailDialogFragment = CustomerDetailDialogFragment.newInstance(name, phone);
        customerDetailDialogFragment.setListner(VendorOrderInfoAdapter.this);
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
        IJobStateController.onJobState(morders.get(pos).getId(), GlobalUtils.JOB_ACCEPTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtOrderNum;
        private TextView txtTime;
        private TextView txtDate;
        private TextView txtLocationFrom;
        private TextView txtLocationTo;
        private TextView txtItemType;
        private Button btnviewOrder;
     /*   private Button btnAccept;
        private Button btnReject;
*/

        public CustomViewHolder(View itemView) {
            super(itemView);
            txtOrderNum = (TextView) itemView.findViewById(R.id.txtOrderNum);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtLocationFrom = (TextView) itemView.findViewById(R.id.txtLocationFrom);
            txtLocationTo = (TextView) itemView.findViewById(R.id.txtLocationTo);
            txtItemType = (TextView) itemView.findViewById(R.id.txtItemType);

           /* btnAccept = (Button) itemView.findViewById(R.id.btnAccept);
            btnReject = (Button) itemView.findViewById(R.id.btnReject);*/
            btnviewOrder = (Button) itemView.findViewById(R.id.btnviewOrder);
          /*  btnAccepts = (Button) itemView.findViewById(R.id.btnAccept);
            btnRejects = (Button) itemView.findViewById(R.id.btnReject);*/
            btnviewOrder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iVewOrderClick.onViewOrder(morders.get(getAdapterPosition()), getAdapterPosition());

        }
    }
}








