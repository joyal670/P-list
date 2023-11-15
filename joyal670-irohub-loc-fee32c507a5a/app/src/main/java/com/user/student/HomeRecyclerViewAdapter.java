package com.user.student;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;


class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    // Array of images
    // Adding images from drawable folder
    private final Context ctx;
    private final List<Address> addresses;

    // Constructor of our ViewPager2Adapter class
    HomeRecyclerViewAdapter(Context ctx, List<Address> addressesList) {
        this.ctx = ctx;
        this.addresses = addressesList;
    }

    // This method returns our layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.home_student_list_item, parent, false);
        return new ViewHolder(view);
    }

    // This method binds the screen with the view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // This will set the images in imageview
        holder.images.setText("" + addresses.get(position));
    }

    // This Method returns the size of the Array
    @Override
    public int getItemCount() {
        return addresses.size();
    }

    // The ViewHolder class holds the view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView images;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.tvDetails);
        }
    }
}
