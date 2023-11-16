package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.myproperty

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.details.OwnerPropertyViewDetailedActivity
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.building_view_details.BuildingViewDetailsActivity
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.PropertyViewDetailsActivity
import com.property.propertyagent.modal.agent.agent_assigned_property_list.UserProperty
import com.property.propertyagent.start_up.google_map.PropertyMapsActivity
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.recycle_myproperty_list_item.view.*

class MyPropertyAdapter(
    private val context: Context,
    private var assignedPropertyList: List<UserProperty>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_myproperty_list_item, parent, false)
                ViewHolder(view)
            }
            VIEW_TYPE_PROGRESS -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.loader, parent, false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount(): Int {
        return assignedPropertyList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (assignedPropertyList[position].id) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        if (holder is ViewHolder) {
            if (assignedPropertyList[position].property_to == 0) {
                holder.itemView.tvPropertyType.text =
                    context.getString(R.string.apartment_for_rent)
            } else {
                holder.itemView.tvPropertyType.text =
                    context.getString(R.string.apartment_for_sale)
            }

            if(assignedPropertyList[position].property_priority_image.equals(null) || assignedPropertyList[position].property_priority_image.equals("")){
                holder.itemView.ivAssignedPropertyImage.loadImagesWithGlideExt("")
            }else{
                Glide.with(context).load(assignedPropertyList[position].property_priority_image.document).error(R.drawable.ic_broken_image_new).into(holder.itemView.ivAssignedPropertyImage)
                //holder.itemView.ivAssignedPropertyImage.loadImagesWithGlideExt(assignedPropertyList[position].property_priority_image.document)
            }
           /* if(!assignedPropertyList[position].property_priority_image.document.isNullOrEmpty()){
                holder.itemView.ivAssignedPropertyImage.loadImagesWithGlideExt(assignedPropertyList[position].property_priority_image.document)
            }*/

        /*    if (!assignedPropertyList[position].property_priority_image.equals(null)) {
                holder.itemView.ivAssignedPropertyImage.loadImagesWithGlideExt(assignedPropertyList[position].property_priority_image.document)
            } else {
                holder.itemView.ivAssignedPropertyImage.loadImagesWithGlideExt("")
            }*/

            if (assignedPropertyList[position].is_builder == "0") {
                holder.itemView.tvPropertyTypeName.text =
                    context.getString(R.string.individualProperty)
            } else {
                holder.itemView.tvPropertyTypeName.text = context.getString(R.string.building)
            }
            holder.itemView.tvAssignedPropertyName.text =
                assignedPropertyList[position].property_name
            if (assignedPropertyList[position].latitude.isNotEmpty() && assignedPropertyList[position].longitude.isNotEmpty()) {
                try {
                    holder.itemView.myPropertyRecyerView_LocationName.text = CommonUtils.getAddress(
                        assignedPropertyList[position].latitude,
                        assignedPropertyList[position].longitude, context
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                holder.itemView.myPropertyRecyerView_LocationName.text = ""
            }
            holder.itemView.myPropertyRecyerView_LocationName.paintFlags =
                holder.itemView.myPropertyRecyerView_LocationName.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            holder.itemView.myPropertyRecyerView_ViewDetailsBtn.setOnClickListener {
                if (assignedPropertyList[position].is_builder == "0") {
                    val intent = Intent(context, PropertyViewDetailsActivity::class.java)
                    intent.putExtra("property_id", assignedPropertyList[position].id.toString())
                    context.startActivity(intent)
                } else {
                    val intent = Intent(context, BuildingViewDetailsActivity::class.java)
                    intent.putExtra("property_id", assignedPropertyList[position].id.toString())
                    context.startActivity(intent)
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
