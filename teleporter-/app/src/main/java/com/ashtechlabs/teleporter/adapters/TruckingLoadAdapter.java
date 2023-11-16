package com.ashtechlabs.teleporter.adapters;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingLoad;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by VIDHU on 2/14/2017.
 */
public class TruckingLoadAdapter extends RecyclerView.Adapter<TruckingLoadAdapter.LoadViewHolder> {

    private ArrayList<TruckingLoad> loadDetailList;
    private FragmentActivity mContext;
    private ArrayList<String> arrayPackageType, arrayPackageUnit, arrayPackageWeightUnit;


    public TruckingLoadAdapter(FragmentActivity context, ArrayList<TruckingLoad> loadDetails) {
        this.loadDetailList = loadDetails;
        this.mContext = context;
        arrayPackageType = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.packing_type)));
        arrayPackageUnit = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.package_unit)));
        arrayPackageWeightUnit = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.package_weight_unit)));
    }


    @Override
    public LoadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cargo_loaddetails, parent, false);
        return new LoadViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LoadViewHolder holder, int position) {

        TruckingLoad loadDetails = loadDetailList.get(position);


        holder.tv_packagetype.setText(arrayPackageType.get(Integer.parseInt(loadDetails.getPackingType().replace("\"", ""))));

        holder.tvUnit.setText(arrayPackageWeightUnit.get(Integer.parseInt(loadDetails.getPackageUnit().replace("\"", ""))));

        holder.tvCm.setText(arrayPackageUnit.get(Integer.parseInt(loadDetails.getPackageDimensionUnit().replace("\"", ""))));

        holder.edWeight.setText("" + loadDetails.getPackageWeight().replace("\"", ""));

        holder.edQty.setText("" + loadDetails.getPackageQty().replace("\"", "")+" Qty");

        holder.edWidth.setText("" + loadDetails.getPackageDimensionLength().replace("\"", "")+" W");

        holder.edBreadth.setText("" + loadDetails.getPackageDimensionBreadth().replace("\"", "")+" B");

        holder.edHeight.setText("" + loadDetails.getPackageDimensionHeight().replace("\"", "")+" H");


    }

    @Override
    public int getItemCount() {
        return loadDetailList.size();
    }

    class LoadViewHolder extends RecyclerView.ViewHolder {


        private TextView tv_packagetype, tvUnit, tvCm;
        private TextView edWeight, edQty, edWidth, edHeight, edBreadth;


        LoadViewHolder(View itemView) {
            super(itemView);

            edWidth = (TextView) itemView.findViewById(R.id.edWidth);
            edHeight = (TextView) itemView.findViewById(R.id.edHeight);
            edBreadth = (TextView) itemView.findViewById(R.id.edBreadth);
            edQty = (TextView) itemView.findViewById(R.id.edQty);
            edWeight = (TextView) itemView.findViewById(R.id.edWeight);
            tv_packagetype = (TextView) itemView.findViewById(R.id.tv_packagetype);
            tvCm = (TextView) itemView.findViewById(R.id.tvCm);
            tvUnit = (TextView) itemView.findViewById(R.id.tvUnit);
        }


    }
}

