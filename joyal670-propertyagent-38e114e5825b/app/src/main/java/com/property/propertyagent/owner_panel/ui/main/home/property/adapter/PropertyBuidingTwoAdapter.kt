package com.property.propertyagent.owner_panel.ui.main.home.property.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingOwner
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.activity.PropertyOwnerBuildingDetailedActivity
import kotlinx.android.synthetic.main.owner_building.view.*

class PropertyBuidingTwoAdapter(
    position : Int ,
    propertyList : ArrayList<OwnerPropertyMainListingOwner>
) : RecyclerView.Adapter<PropertyBuidingTwoAdapter.ViewHold>()
{
    private var context: Context? = null
    private val taskList = listOf(
            "SAR 1000"

    )

    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.owner_building, parent, false)
        return ViewHold(
                view
        )
    }

    override fun getItemCount(): Int
    {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        val gridLayoutManager = GridLayoutManager(context, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        holder.itemView.rvOwnerBuildingImages.setLayoutManager(gridLayoutManager)
        //holder.itemView.rvOwnerBuildingImages.adapter = PropertyImagesAdapter(propertyList[position].bulding_appartments)

        holder.itemView.hideApartmentBtn.setOnClickListener {
            val str = holder.itemView.hideApartmentBtn.text.toString()
            if(str.equals(context?.getString(R.string.hide_appartment)))

            {
                holder.itemView.rvOwnerBuildingImages.visibility = View.GONE
                holder.itemView.hideApartmentBtn.text =  context?.getString(R.string.show_appartment)
            }
            else
            {
                holder.itemView.rvOwnerBuildingImages.visibility = View.VISIBLE
                holder.itemView.hideApartmentBtn.text = context?.getString(R.string.hide_appartment)
            }

        }

        holder.itemView.owner_property_image_viewDetailsbtn.setOnClickListener {
            context?.startActivity(Intent(context, PropertyOwnerBuildingDetailedActivity::class.java)) }
        }


}