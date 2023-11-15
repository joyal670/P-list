package com.property.propertyagent.owner_panel.ui.main.sidemenu.ownerservice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.owner.owner_request_service_for_approval_list.RequestDetail
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.recycle_owner_service_list_item.view.*

class OwnerServiceAdapter(
    private var requestDetails: ArrayList<RequestDetail>,
    private val functionAccept: (String, Int) -> Unit,
    private val functionReject: (String, Int) -> Unit
) : RecyclerView.Adapter<OwnerServiceAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_owner_service_list_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return requestDetails.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        when (requestDetails[position].status) {
            0 -> {
                holder.itemView.btnAcceptService.visibility = View.VISIBLE
                holder.itemView.btnRejectService.visibility = View.VISIBLE
                holder.itemView.btnAcceptService.text = context!!.getString(R.string.accept)
                holder.itemView.btnRejectService.text = context!!.getString(R.string.reject)
                holder.itemView.btnAcceptService.isEnabled = true
                holder.itemView.btnRejectService.isEnabled = true
            }
            1 -> {
                holder.itemView.btnAcceptService.visibility = View.VISIBLE
                holder.itemView.btnRejectService.visibility = View.GONE
                holder.itemView.btnAcceptService.text = context!!.getString(R.string.accepted)
                holder.itemView.btnAcceptService.isEnabled = false
            }
            2 -> {
                holder.itemView.btnAcceptService.visibility = View.GONE
                holder.itemView.btnRejectService.visibility = View.VISIBLE
                holder.itemView.btnRejectService.text = context!!.getString(R.string.rejected)
                holder.itemView.btnRejectService.isEnabled = false
            }
        }
        val builder = StringBuilder()
        val builderFullAddress = StringBuilder()
        if (requestDetails[position].property_details != null) {
            holder.itemView.tvPropertyNameService.text =
                requestDetails[position].property_details.property_name
            holder.itemView.tvPropertyCodeService.text =
                requestDetails[position].property_details.property_reg_no

            try {
                holder.itemView.tvPropertyLocationService.text = CommonUtils.getAddress(
                    requestDetails[position].property_details.latitude,
                    requestDetails[position].property_details.longitude,
                    context!!
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (requestDetails[position].property_details.property_to == 0) {
                holder.itemView.tvPropertyToService.text =
                    context!!.getString(R.string.appartment_for_rent)
            } else {
                holder.itemView.tvPropertyToService.text =
                    context!!.getString(R.string.appartment_for_sale)
            }



            if (requestDetails[position].service_related != null) {
                holder.itemView.ivServiceImage.loadImagesWithGlideExt(requestDetails[position].service_related.image)
                holder.itemView.tvServiceName.text =
                    requestDetails[position].service_related.service
            }
            holder.itemView.tvServiceDate.text = requestDetails[position].date
            holder.itemView.tvServiceTime.text = requestDetails[position].time

            holder.itemView.tvDescriptionService.text = requestDetails[position].description
            if (requestDetails[position].property_details.property_priority_image != null) {
                holder.itemView.ivPropertyImage.loadImagesWithGlideExt(
                    requestDetails[position].property_details.property_priority_image.document
                )
            }
        }
        holder.itemView.btnAcceptService.setOnClickListener {
            functionAccept.invoke(requestDetails[position].id.toString(), position)
        }
        holder.itemView.btnRejectService.setOnClickListener {
            functionReject.invoke(requestDetails[position].id.toString(), position)
        }

    }
}
