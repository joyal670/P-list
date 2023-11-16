package com.property.propertyagent.owner_panel.ui.main.home.property.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingOwner
import com.property.propertyagent.owner_panel.ui.main.home.property.property_main_details.PropertyMainDetailedActivity
import kotlinx.android.synthetic.main.owner_property.view.*

class PropertyBuildingOneAdapter(
    private var pos : Int ,
    private var propertyList : ArrayList<OwnerPropertyMainListingOwner>
) : RecyclerView.Adapter<PropertyBuildingOneAdapter.ViewHold>()
{
    private var context: Context? = null

    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.owner_property, parent, false)
        return ViewHold(
                view
        )
    }

    override fun getItemCount(): Int
    {
        return propertyList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        holder.itemView.tvPropertyName.text = ""

        holder.itemView.owner_building_recycerview_view_details.setOnClickListener {
            context?.startActivity(Intent(context, PropertyMainDetailedActivity::class.java)) }
    }
}