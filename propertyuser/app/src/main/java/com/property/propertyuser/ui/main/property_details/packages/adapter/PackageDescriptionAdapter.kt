package com.property.propertyuser.ui.main.property_details.packages.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import kotlinx.android.synthetic.main.list_package_description_item.view.*

class PackageDescriptionAdapter(private val context: Context)
    : RecyclerView.Adapter<PackageDescriptionAdapter.ViewHolder>() {

    private  val packageDescriptionList= listOf<String>("Make A One Time Payment Per Week",
    "Just Pay For 5 Days And Get 2 Day Stay For Free",
    "Just Pay For 5 Days And Get 2 Day Stay For Free",
    "Just Pay For 5 Days And Get 2 Day Stay For Free")

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_package_description_item, parent, false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return packageDescriptionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvDescription.text =packageDescriptionList[position]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}