package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.pojo.StorageSpace;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 31-Jan-17.
 */
public class SpaceAdapter extends RecyclerView.Adapter<SpaceAdapter.StorageSpaceViewHolder> {
    Activity mContext;
    ArrayList<StorageSpace> mStorageSpaceList;

    OnStorageListItemClickListener itemClickListener;

    public SpaceAdapter(Activity context, ArrayList<StorageSpace> storageSpaces) {
        this.mContext = context;
        this.mStorageSpaceList = storageSpaces;
    }

    @Override
    public StorageSpaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_storage_space, viewGroup, false);
        return new StorageSpaceViewHolder(view);
    }


    @Override
    public void onBindViewHolder(StorageSpaceViewHolder holder, int position) {

        StorageSpace storageSpace = mStorageSpaceList.get(position);

        holder.tvName.setText(storageSpace.getWarehouseName());
        holder.tvSpace.setText(MessageFormat.format("Capacity: {0}, Available: {1}", storageSpace.getWarehouseCapacity(), storageSpace.getSpaceAvailable()));
        holder.tvLocation.setText(storageSpace.getWarehouseLocation());
    }

    @Override
    public int getItemCount() {
        return (null != mStorageSpaceList ? mStorageSpaceList.size() : 0);
    }

    public void setClickListener(OnStorageListItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public class StorageSpaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName, tvSpace, tvLocation;


        public StorageSpaceViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            tvSpace = (TextView) itemView.findViewById(R.id.tvSpace);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onClick(view, getAdapterPosition(), mStorageSpaceList.get(getAdapterPosition()));
        }
    }
}


