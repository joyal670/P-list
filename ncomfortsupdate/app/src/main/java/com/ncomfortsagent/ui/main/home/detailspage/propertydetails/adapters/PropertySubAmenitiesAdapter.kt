package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ncomfortsagent.databinding.ItemSubAmenitiesBinding
import com.ncomfortsagent.model.property_details.AgentPropertyDetailsAmenityDetail

class PropertySubAmenitiesAdapter(private var amenityDetails: List<AgentPropertyDetailsAmenityDetail>) : RecyclerView.Adapter<PropertySubAmenitiesAdapter.ViewHold>() {

    private var context: Context? = null

    class ViewHold(var binding: ItemSubAmenitiesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
           ItemSubAmenitiesBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return amenityDetails.size
    }


    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvSubTitle.text = amenityDetails[position].name

        Glide.with(context!!).load(amenityDetails[position].image).into(holder.binding.ivImage)

    }

}