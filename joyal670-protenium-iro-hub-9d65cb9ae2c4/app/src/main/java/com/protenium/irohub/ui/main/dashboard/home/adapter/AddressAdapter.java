package com.protenium.irohub.ui.main.dashboard.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.protenium.irohub.R;
import com.protenium.irohub.model.getAddress.Datum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHold> {

    private final List<com.protenium.irohub.model.getAddress.Datum> stringList;
    private final Context context;
    private SelectedValue selectedValue;

    public AddressAdapter(Context fragment, List<com.protenium.irohub.model.getAddress.Datum> dataList) {
        this.stringList = dataList;
        this.context = fragment;
        if (fragment instanceof SelectedValue) {
            selectedValue = (SelectedValue) fragment;
        }
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycerview_address, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        Datum datum = stringList.get(position);

        holder.rvAddressTv.setText(stringList.get(position).getTitle() + "," +
                stringList.get(position).getGovernorate() + "," +
                stringList.get(position).getArea() + "," +
                stringList.get(position).getBlock() + "," +
                stringList.get(position).getAvenue() + "," +
                stringList.get(position).getStreet() + "," +
                stringList.get(position).getBuilding() + "," +
                stringList.get(position).getFloor() + "," +
                stringList.get(position).getAppartment());

        holder.rvAddressRadioBtn.setChecked(stringList.get(position).getChecked());

        holder.rvAddressRadioBtn.setOnClickListener(v -> {
            if (!stringList.get(position).getChecked()) {
                holder.rvAddressRadioBtn.setChecked(true);
                for (int i = 0; i < stringList.size(); i++) {
                    stringList.get(i).setChecked(false);
                }
                stringList.get(position).setChecked(true);
                notifyDataSetChanged();
                selectedValue.setValue(datum);

            }
        });

        holder.rvAddressDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedValue.delete(datum);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public interface SelectedValue {
        void setValue(Datum datum);

        void delete(Datum datum);
    }

    public class ViewHold extends RecyclerView.ViewHolder {

        @BindView(R.id.rvAddressTv)
        TextView rvAddressTv;

        @BindView(R.id.rvAddressRadioBtn)
        RadioButton rvAddressRadioBtn;

        @BindView(R.id.rvAddressDeleteBtn)
        ImageView rvAddressDeleteBtn;


        public ViewHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            for (int i = 0; i < stringList.size(); i++) {
                stringList.get(i).setChecked(false);
            }

        }
    }

}
