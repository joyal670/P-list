package com.protenium.irohub.ui.main.dashboard.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.ui.main.dashboard.home.adapter.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends BaseActivity {

    @BindView(R.id.rvNotification)
    RecyclerView rvNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        List<String> notification = new ArrayList<>();
        notification.add("This is test notification ");
        notification.add("Welcome");

        List<String> notificationMessage = new ArrayList<>();
        notificationMessage.add("contents to be added");
        notificationMessage.add("hello.....");
        rvNotification.setLayoutManager(new LinearLayoutManager(this));
        rvNotification.setAdapter(new NotificationAdapter(this, notification, notificationMessage));
    }

    @Override
    public int setLayout() {
        return R.layout.activity_notification;
    }

    @Override
    public boolean setToolbar() {
        return false;
    }

    @Override
    public boolean hideStatusBar() {
        return false;
    }

    @Override
    public boolean setFullScreen() {
        return false;
    }
}