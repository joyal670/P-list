package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.recycle_item_for_agent_appartment.view.*

class BuildingApartmentAdapter(
    private var apartmentList: List<com.property.propertyagent.modal.agent.agent_builder_details.Unit>,
    private val editApartment: (Int) -> Unit,
) : RecyclerView.Adapter<BuildingApartmentAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_item_for_agent_appartment, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return apartmentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        if (!apartmentList[position].property_priority_image.equals(null)) {
            holder.itemView.ivApartment.loadImagesWithGlideExt(apartmentList[position].property_priority_image.document_image)
        }
        holder.itemView.tvApartmentName.text = apartmentList[position].unit_name
        holder.itemView.tvPropertyCode.text = apartmentList[position].property_reg_no

        holder.itemView.tvExpectedAmount.text =
            context!!.getString(R.string.sar) + " " + apartmentList[position].expected_amount

        when (apartmentList[position].furnished) {
            "0" -> {
                holder.itemView.tvFurnishedOwnerOngoingDetails.text =
                    ": " + context!!.getString(R.string.not_furnished)
            }
            "1" -> {
                holder.itemView.tvFurnishedOwnerOngoingDetails.text =
                    ": " + context!!.getString(R.string.semifurnished)
            }
            else -> {
                holder.itemView.tvFurnishedOwnerOngoingDetails.text =
                    ": " + context!!.getString(R.string.fully)
            }
        }

        when (apartmentList[position].occupied) {
            "0" -> {
                holder.itemView.tvOccupiedStatus.text = context!!.getString(R.string.vacant)
            }
            "1" -> {
                holder.itemView.tvOccupiedStatus.text = context!!.getString(R.string.occupied)
            }
        }

        holder.itemView.btnEdit.setOnClickListener {
            editApartment.invoke(apartmentList[position].id)
        }
    }
}
