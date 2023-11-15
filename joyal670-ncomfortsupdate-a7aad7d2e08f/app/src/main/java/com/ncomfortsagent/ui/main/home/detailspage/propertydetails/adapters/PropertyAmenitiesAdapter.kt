package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ncomfortsagent.R
import com.ncomfortsagent.databinding.ItemAmenitiesHomeBinding

class PropertyAmenitiesAdapter(
    private var activity: Activity,
    private var amenitiesData: ArrayList<Int>
) : RecyclerView.Adapter<PropertyAmenitiesAdapter.ViewHold>() {

    private var context: Context? = null
    private var list = arrayOf(R.drawable.ic_amenities_one, R.drawable.ic_amenities_one, R.drawable.ic_amenities_one)

    class ViewHold(var binding: ItemAmenitiesHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = ItemAmenitiesHomeBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return amenitiesData.size
    }


    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        //Glide.with(activity).load(list[position]).into(holder.binding.ivAmenities)
       // holder.binding.tvAmenitiesName.text = amenitiesData[position].value.toString()
    }

}