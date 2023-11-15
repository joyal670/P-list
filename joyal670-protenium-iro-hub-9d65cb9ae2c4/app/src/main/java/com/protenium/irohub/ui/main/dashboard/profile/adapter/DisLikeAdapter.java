package com.protenium.irohub.ui.main.dashboard.profile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.protenium.irohub.R;
import com.protenium.irohub.model.dislike.getdislike.Tag;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisLikeAdapter extends RecyclerView.Adapter<DisLikeAdapter.ViewHold> {

    private final List<Tag> dataList;
    private final Context context;
    private SelectedTag selectedValue;

    public DisLikeAdapter(Context fragment, List<Tag> dataList) {
        this.dataList = dataList;
        this.context = fragment;
        if (fragment instanceof SelectedTag) {
            selectedValue = (SelectedTag) fragment;
        }
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycerview_dislike, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        Tag tag = dataList.get(position);
        holder.dislikeBtn.setText(tag.getName());


        if (dataList.get(position).getDisliked()) {
            holder.dislikeBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
            holder.dislikeBtn.setTextColor(Color.parseColor("#FFFFFF"));
            selectedValue.setValue(tag);
        } else {
            holder.dislikeBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey2));
            holder.dislikeBtn.setTextColor(Color.parseColor("#707070"));
            selectedValue.setValue(tag);
        }

        holder.dislikeBtn.setOnClickListener(v -> {
            if (tag.getDisliked()) {
                tag.setDisliked(!tag.getDisliked());
                notifyDataSetChanged();
            } else {
                tag.setDisliked(!tag.getDisliked());
                notifyDataSetChanged();
            }

        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {

        @BindView(R.id.dislikeBtn)
        MaterialButton dislikeBtn;


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
