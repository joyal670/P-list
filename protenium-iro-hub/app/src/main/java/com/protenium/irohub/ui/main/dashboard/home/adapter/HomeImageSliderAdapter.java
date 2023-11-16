package com.protenium.irohub.ui.main.dashboard.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.protenium.irohub.R;
import com.protenium.irohub.model.home.Banner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeImageSliderAdapter extends RecyclerView.Adapter<HomeImageSliderAdapter.ViewHold> {

    private final List<Banner> banners;
    private final Context context;

    public HomeImageSliderAdapter(Context fragment, List<Banner> dataList) {
        this.banners = dataList;
        this.context = fragment;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_home_image_item, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        Banner banner = banners.get(position);
        Glide.with(context).load(banner.getImage()).into(holder.ivItemImage);

    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        @BindView(R.id.ivItemImage)
        ImageView ivItemImage;

        public ViewHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
