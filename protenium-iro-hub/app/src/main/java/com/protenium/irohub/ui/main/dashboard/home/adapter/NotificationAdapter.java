package com.protenium.irohub.ui.main.dashboard.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.protenium.irohub.R;
import com.protenium.irohub.model.dislike.getdislike.Tag;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHold> {

    private final List<String> dataList;
    private final List<String> dataListHeading;
    private final Context context;
    private SelectedTag selectedValue;

    public NotificationAdapter(Context fragment, List<String> notification, List<String> dataList) {
        this.dataList = dataList;
        this.context = fragment;
        this.dataListHeading = notification;
        if (fragment instanceof SelectedTag) {
            selectedValue = (SelectedTag) fragment;
        }
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycerview_notification, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        holder.tvNotificationTitle.setText(dataList.get(position));
        holder.tvNotificationMessage.setText(dataListHeading.get(position));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {

        @BindView(R.id.tvNotificationTitle)
        TextView tvNotificationTitle;

        @BindView(R.id.tvNotificationMessage)
        TextView tvNotificationMessage;


        public ViewHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

          /*  for (int i = 0; i < dataList.size(); i++) {
                dataList.get(i).setDisliked(false);
            }*/

        }
    }

    public interface SelectedTag {
        void setValue(Tag tag);
    }
}
