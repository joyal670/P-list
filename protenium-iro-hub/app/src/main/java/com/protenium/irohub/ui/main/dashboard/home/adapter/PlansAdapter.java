package com.protenium.irohub.ui.main.dashboard.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.protenium.irohub.R;
import com.protenium.irohub.model.calender.CalenderModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.ViewHold> {

    private final List<CalenderModel> stringList;
    private final Context context;
    private SelectedDate selectedValue;

    public PlansAdapter(Context fragment, List<CalenderModel> dataList) {
        this.stringList = dataList;
        this.context = fragment;
        if (fragment instanceof SelectedDate) {
            selectedValue = (SelectedDate) fragment;
        }
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        CalenderModel calenderModel = stringList.get(position);

        Log.e("TAG", "onBindViewHolder: " + calenderModel.getDate());
        DateFormat df12 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat df1 = new SimpleDateFormat("dd-E", Locale.getDefault());
        try {
            Date date = df12.parse(calenderModel.getDate());
            String date1 = df1.format(date);
            holder.tv_date_calendar_item.setText("" + date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (stringList.get(position).getIsChecked()) {
            holder.cl_calendar_item.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
            holder.tv_date_calendar_item.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.cl_calendar_item.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey2));
            holder.tv_date_calendar_item.setTextColor(Color.parseColor("#7C7D7E"));
        }
        holder.cl_calendar_item.setOnClickListener(v -> {
            if (!stringList.get(position).getIsChecked()) {
                holder.cl_calendar_item.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
                holder.tv_date_calendar_item.setTextColor(Color.parseColor("#FFFFFF"));
                for (int i = 0; i < stringList.size(); i++) {
                    stringList.get(i).setChecked(false);
                }
                stringList.get(position).setChecked(true);
                notifyDataSetChanged();
                selectedValue.setStartValue(calenderModel);

            }
        });

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_date_calendar_item)
        TextView tv_date_calendar_item;

        @BindView(R.id.cl_calendar_item)
        LinearLayout cl_calendar_item;


        public ViewHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            for (int i = 0; i < stringList.size(); i++) {
                stringList.get(i).setChecked(false);
            }

        }
    }

    public interface SelectedDate {
        void setStartValue(CalenderModel calenderModel);
    }
}
