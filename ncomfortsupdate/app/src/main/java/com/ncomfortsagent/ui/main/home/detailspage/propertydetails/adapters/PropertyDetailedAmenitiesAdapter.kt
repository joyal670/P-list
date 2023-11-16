package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ncomfortsagent.databinding.ItemAmenitiesBinding
import com.ncomfortsagent.model.property_details.AgentPropertyDetailsAmenityCategory

class PropertyDetailedAmenitiesAdapter(
  private var activity: Activity,
  private var amenitiesCategory: ArrayList<AgentPropertyDetailsAmenityCategory>
) : RecyclerView.Adapter<PropertyDetailedAmenitiesAdapter.ViewHold>() {

    private var context: Context? = null

    class ViewHold(var binding: ItemAmenitiesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            ItemAmenitiesBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return amenitiesCategory.size
    }


    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvMainTitle.text = amenitiesCategory[position].name

        holder.binding.rvSubAmenities.layoutManager = LinearLayoutManager(context)
        holder.binding.rvSubAmenities.adapter = PropertySubAmenitiesAdapter(amenitiesCategory[position].amenity_details)
    }

}