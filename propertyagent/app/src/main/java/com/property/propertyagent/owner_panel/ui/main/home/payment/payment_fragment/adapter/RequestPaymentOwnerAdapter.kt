package com.property.propertyagent.owner_panel.ui.main.home.payment.payment_fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyagent.R
import com.property.propertyagent.modal.owner.owner_payment_list_of_properties.OwnerPaymentListOfPropertiesProperty
import com.property.propertyagent.owner_panel.ui.main.home.payment.payment_history.activity.PaymentHistoryActivity
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.Constants
import kotlinx.android.synthetic.main.recycle_myrequest_payment_owner_list_item.view.*
import java.util.*

class RequestPaymentOwnerAdapter(
    private var requireContext: Context,
    private var paymentList: ArrayList<OwnerPaymentListOfPropertiesProperty>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_myrequest_payment_owner_list_item, parent, false)
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
        return paymentList.size
    }

    override fun getItemViewType(position: Int): Int {
        val viewtype = paymentList[position].id
        return when (viewtype) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        if (holder is ViewHolder) {

            Glide.with(requireContext).load(paymentList[position].property_priority_image.document)
                .into(holder.itemView.ivPropertyImage)
            holder.itemView.tvPropertyName.text = paymentList[position].property_name
            holder.itemView.tvPropertycCode.text = paymentList[position].property_reg_no

            try {
                holder.itemView.tvLocationName.text =
                    CommonUtils.getAddress(
                        paymentList[position].latitude,
                        paymentList[position].longitude,
                        requireContext
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }

            /* property_to -> 0 = Rent */
            /* property_to -> 1 = Sale */
            if (paymentList[position].property_to == 0) {
                holder.itemView.tvPropertyType.text =
                    requireContext.getString(R.string.propertyForRent)
                holder.itemView.tvPropertyPrice.text =
                    requireContext.getString(R.string.sarValue) + " " + paymentList[position].rent
                holder.itemView.tvPaymentPeriod.text =
                    paymentList[position].property_rent_frequency.type
            } else {
                holder.itemView.tvPropertyType.text =
                    requireContext.getString(R.string.propertyForSale)
                holder.itemView.tvPropertyPrice.text = requireContext.getString(R.string.sarValue) + " " +   paymentList[position].selling_price
                holder.itemView.rentLayout.visibility = View.GONE
                holder.itemView.mainLayout.visibility = View.GONE
            }

            if (paymentList[position].first_owner_payament.id == 0) {
                holder.itemView.rentLayout.visibility = View.GONE
            } else {
                holder.itemView.tvStartDate.text = paymentList[position].first_owner_payament.date
            }

            if (paymentList[position].last_owner_payament.id == 0) {
                holder.itemView.endLayout.visibility = View.GONE
            } else {
                holder.itemView.tvEndDate.text = paymentList[position].last_owner_payament.date
            }

            holder.itemView.owner_viewPayemnt_history.setOnClickListener {
                val intent = Intent(requireContext, PaymentHistoryActivity::class.java)
                intent.putExtra(Constants.SELECTED_PAYMENT_ID, paymentList[position].id)
                requireContext.startActivity(intent)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

