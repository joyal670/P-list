package com.protenium.irohub.ui.main.dashboard.profile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.button.MaterialButton;
import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.model.dislike.getdislike.Tag;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.ui.main.dashboard.home.viewmodel.DisLikeViewModel;
import com.protenium.irohub.ui.main.dashboard.profile.adapter.DisLikeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DislikesActivity extends BaseActivity implements DisLikeAdapter.SelectedTag {

    private DisLikeViewModel disLikeViewModel;
    private final List<Tag> tagList = new ArrayList<>();

    private final List<Integer> selectedTagsList = new ArrayList<>();

    @BindView(R.id.recycerViewDislikes)
    RecyclerView recyclerViewDislikes;

    @BindView(R.id.btnSave)
    MaterialButton btnSave;

    @Override
    public int setLayout() {
        return R.layout.activity_dislikes;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        disLikeViewModel = ViewModelProviders.of(this).get(DisLikeViewModel.class);
        disLikeViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });

        getDislikes();

        onClicks();
    }

    private void onClicks() {

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disLikeViewModel.setDisLike(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""), selectedTagsList, "en").observe(DislikesActivity.this, setDislikeResponse -> {
                    if (setDislikeResponse.getStatus()) {

                        DislikesActivity.super.onBackPressed();
                        Toast.makeText(DislikesActivity.this, "" + setDislikeResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DislikesActivity.this, "" + setDislikeResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    private void getDislikes() {
        disLikeViewModel.getDisLike(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""), "en").observe(this, getDisLikeResponse -> {
            if (getDisLikeResponse.getStatus()) {

                FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
                flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
                flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
                flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
                flexboxLayoutManager.setAlignItems(AlignItems.CENTER);
                recyclerViewDislikes.setLayoutManager(flexboxLayoutManager);
                tagList.addAll(getDisLikeResponse.getData().getTags());
                recyclerViewDislikes.setAdapter(new DisLikeAdapter(this, tagList));

            } else {
                Toast.makeText(this, "" + getDisLikeResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setValue(Tag tag) {

        selectedTagsList.clear();
        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).getDisliked()) {
                selectedTagsList.add(tagList.get(i).getId());
            }
        }
    }
}