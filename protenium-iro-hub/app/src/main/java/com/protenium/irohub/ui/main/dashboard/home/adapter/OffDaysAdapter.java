package com.protenium.irohub.ui.main.dashboard.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.protenium.irohub.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OffDaysAdapter extends RecyclerView.Adapter<OffDaysAdapter.ViewHold> {

    private List<String> stringList;
    private Context context;
    private Boolean clicked;

    public OffDaysAdapter(Context fragment, List<String> dataList) {
        this.stringList = dataList;
        this.context = fragment;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycerview_offdays, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        holder.offDaysBtn.setText(stringList.get(position));

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {

        @BindView(R.id.offDaysBtn)
        TextView offDaysBtn;


        public ViewHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
