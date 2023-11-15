package com.property.propertyuser.ui.main.my_property.view_details.view_payment_history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.payment_history.PaymentHistory
import kotlinx.android.synthetic.main.list_view_payment_history_item.view.*

class PaymentHistoryAdapter(
    private val context: Context,
    private var paymentHistoryList: List<PaymentHistory>,
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
                    .inflate(R.layout.list_view_payment_history_item, parent, false)
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
        return paymentHistoryList.size
    }

    override fun getItemViewType(position: Int): Int {
        val viewtype = paymentHistoryList[position].id
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
            holder.itemView.tvServiceDatePayment.text = paymentHistoryList[position].date
            when (paymentHistoryList[position].payment_type) {
                1 -> {
                    holder.itemView.tvServiceNamePayment.text =
                        paymentHistoryList[position].service
                    holder.itemView.tvServiceDescriptionPayment.visibility = View.VISIBLE
                    holder.itemView.tvServiceDescriptionPayment.text =
                        paymentHistoryList[position].service_description
                    holder.itemView.tvServiceAmountPayment.text =
                        context.getString(R.string.sar) + " " + paymentHistoryList[position].amount
                }
                2 -> {
                    holder.itemView.tvServiceNamePayment.text =
                        context.getString(R.string.tvReservationAmountTextPayment)
                    holder.itemView.tvServiceDescriptionPayment.visibility = View.GONE
                    holder.itemView.tvServiceAmountPayment.text =
                        context.getString(R.string.sar) + " " + paymentHistoryList[position].amount
                }
                3 -> {
                    holder.itemView.tvServiceNamePayment.text =
                        context.getString(R.string.security_payment)
                    holder.itemView.tvServiceDescriptionPayment.visibility = View.GONE
                    holder.itemView.tvServiceAmountPayment.text =
                        context.getString(R.string.sar) + " " + paymentHistoryList[position].amount
                }
                4 -> {
                    holder.itemView.tvServiceNamePayment.text =
                        context.getString(R.string.full_amount_payment)
                    holder.itemView.tvServiceDescriptionPayment.visibility = View.GONE
                    holder.itemView.tvServiceAmountPayment.text =
                        context.getString(R.string.sar) + " " + paymentHistoryList[position].amount
                }
                5 -> {
                    holder.itemView.tvServiceNamePayment.text =
                        context.getString(R.string.rent_payment)
                    holder.itemView.tvServiceDescriptionPayment.visibility = View.GONE
                    holder.itemView.tvServiceAmountPayment.text =
                        context.getString(R.string.sar) + " " + paymentHistoryList[position].amount
                }
                8 -> {
                    holder.itemView.tvServiceNamePayment.text =
                        context.getString(R.string.tvMaintenanceAmount)
                    holder.itemView.tvServiceDescriptionPayment.visibility = View.GONE
                    holder.itemView.tvServiceAmountPayment.text =
                        context.getString(R.string.sar) + " " + paymentHistoryList[position].amount
                }
            }


        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}