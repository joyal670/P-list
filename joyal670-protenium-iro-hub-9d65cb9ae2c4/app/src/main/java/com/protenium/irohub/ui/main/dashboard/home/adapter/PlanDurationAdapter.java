package com.protenium.irohub.ui.main.dashboard.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.protenium.irohub.R;
import com.protenium.irohub.model.home_detailed.Duration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlanDurationAdapter extends RecyclerView.Adapter<PlanDurationAdapter.ViewHold> {

    private List<Duration> durations;
    private final Context context;
    private SelectedValue selectedValue;

    public PlanDurationAdapter(Context fragment, List<Duration> dataList) {
        this.durations = dataList;
        this.context = fragment;
        if (fragment instanceof SelectedValue){
            selectedValue = (SelectedValue) fragment;
        }
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycerview_selectplan_layout, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {


        Duration duration = durations.get(position);
        holder.planBtn.setText(duration.getDuration() + " "+ "weeks");

        if(durations.get(position).getIsChecked()){
            holder.planBtn.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.colorPrimary));
        }else {
            holder.planBtn.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.grey2));
        }

        holder.planBtn.setOnClickListener(v -> {
            if(!durations.get(position).getIsChecked()){
                holder.planBtn.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.colorPrimary));
                for(int i=0; i<durations.size(); i++){
                   durations.get(i).setIsChecked(false);
                }
                durations.get(position).setIsChecked(true);
                notifyDataSetChanged();
                selectedValue.setValue(duration);

            }
        });
    }

    @Override
    public int getItemCount() {
        return durations.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {

        @BindView(R.id.planBtn)
        TextView planBtn;


        public ViewHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            for(int i=0; i<durations.size(); i++){
                durations.get(i).setIsChecked(false);
            }
        }
    }

    public interface SelectedValue{
        void setValue(Duration duration);
    }

}
