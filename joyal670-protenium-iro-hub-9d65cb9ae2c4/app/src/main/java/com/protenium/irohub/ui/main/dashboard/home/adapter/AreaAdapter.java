package com.protenium.irohub.ui.main.dashboard.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.protenium.irohub.R;
import com.protenium.irohub.model.getArea.Datum;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHold> {

    private final List<Datum> stringList;
    private final Context context;
    private SelectedValue selectedValue;

    public AreaAdapter(Context fragment, ArrayList<Datum> dataList) {
        this.stringList = dataList;
        this.context = fragment;
        if (fragment instanceof SelectedValue) {
            selectedValue = (SelectedValue) fragment;
        }
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycerview_address_layout, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        Datum datum = stringList.get(position);

        holder.tvDialogAddress.setText(stringList.get(position).getName());

        holder.radioDialog.setChecked(stringList.get(position).getChecked());

        holder.radioDialog.setOnClickListener(v -> {
            if (!stringList.get(position).getChecked()) {
                holder.radioDialog.setChecked(true);
                for (int i = 0; i < stringList.size(); i++) {
                    stringList.get(i).setChecked(false);
                }
                stringList.get(position).setChecked(true);
                notifyDataSetChanged();
                selectedValue.setValue(datum);

            }
        });

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public interface SelectedValue {
        void setValue(Datum datum);
    }

    public class ViewHold extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDialogAddress)
        TextView tvDialogAddress;

        @BindView(R.id.radioDialog)
        RadioButton radioDialog;


        public ViewHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            for (int i = 0; i < stringList.size(); i++) {
                stringList.get(i).setChecked(false);
            }

        }
    }

}
