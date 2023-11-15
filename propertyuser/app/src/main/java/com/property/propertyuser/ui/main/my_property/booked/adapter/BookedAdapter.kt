package com.property.propertyuser.ui.main.my_property.booked.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.my_property_list.BookedProperty
import com.property.propertyuser.utils.CommonMethods.isOpenRecently
import com.property.propertyuser.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.list_booked_item.view.*

class BookedAdapter(
    private val context: Context,
    private var bookedPropertyList: List<BookedProperty>,
    private val functionViewDetailsBooked: (Int) -> Unit,
    private val functionSelectedPropertyId: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DATA -> {//inflates row layout
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_booked_item, parent, false)
                ViewHolder(view)
            }
            VIEW_TYPE_PROGRESS -> {//inflates progressbar layout
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.loader, parent, false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount(): Int {
        return bookedPropertyList.size
    }

    override fun getItemViewType(position: Int): Int {
        var viewtype = bookedPropertyList[position].id
        return when (viewtype) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is ViewHolder) {
            holder.itemView.tvPropertyName.text = bookedPropertyList[position].property_name
            holder.itemView.tvPropertyCode.text =
                context.getString(R.string.tvPropertyCode) + " " + bookedPropertyList[position].property_reg_no
            if (bookedPropertyList[position].property_priority_image != null) {
                holder.itemView.roundedBookedPropertyImage.loadImagesWithGlideExt(bookedPropertyList[position].property_priority_image.document)
            }

            when (bookedPropertyList[position].status) {
                0 -> {
                    holder.itemView.btnProceedBooking.text = context.getString(R.string.contract_request)
                }
                1 -> {
                    holder.itemView.btnProceedBooking.text = context.getString(R.string.full_payment)
                }
                2 -> {
                    holder.itemView.btnProceedBooking.text = context.getString(R.string.document_rejected)
                }
            }
            holder.itemView.tvViewDetails.setOnClickListener {
                if (isOpenRecently()) return@setOnClickListener
                functionViewDetailsBooked.invoke(bookedPropertyList[position].id)
            }
            holder.itemView.btnProceedBooking.setOnClickListener {
                if (isOpenRecently()) return@setOnClickListener
                functionSelectedPropertyId.invoke(bookedPropertyList[position].id)
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}