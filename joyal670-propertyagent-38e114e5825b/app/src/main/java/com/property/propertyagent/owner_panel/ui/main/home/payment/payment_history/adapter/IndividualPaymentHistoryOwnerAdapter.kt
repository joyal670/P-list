package com.property.propertyagent.owner_panel.ui.main.home.payment.payment_history.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.owner.owner_payment_history_list.OwnerPaymentHistoryListPayment
import kotlinx.android.synthetic.main.recycle_indivigual_payment_history_list_item.view.*


class IndividualPaymentHistoryOwnerAdapter(
    private var requireContext : Context ,
    private var paymentHistoryList : ArrayList<OwnerPaymentHistoryListPayment> ,
    private var selectedId : (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent : ViewGroup , viewtype : Int) : RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_indivigual_payment_history_list_item , parent , false)
                ViewHolder(view)
            }
            VIEW_TYPE_PROGRESS -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.loader , parent , false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount() : Int {
        return paymentHistoryList.size
    }

    override fun getItemViewType(position : Int) : Int {
        val viewtype = paymentHistoryList[position].id
        return when (viewtype) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder : RecyclerView.ViewHolder ,
        position : Int
    ) {
        if (holder is ViewHolder) {

            holder.itemView.indivigual_payment_detailsTv.paintFlags =
                holder.itemView.indivigual_payment_detailsTv.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            holder.itemView.tvDate.text = paymentHistoryList[position].date

            if (paymentHistoryList[position].amount_type == 1) {
                holder.itemView.tvPaymentType.text = requireContext.getString(R.string.expense)
                holder.itemView.tvAmountValue.text = requireContext.getString(R.string.sarValue) + " " + paymentHistoryList[position].amount
                holder.itemView.tvAmountValue.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext ,
                        R.color.color_accent_red
                    )
                )
            } else {
                holder.itemView.tvPaymentType.text = requireContext.getString(R.string.income)
                holder.itemView.tvAmountValue.text = requireContext.getString(R.string.sarValue) + " " + paymentHistoryList[position].amount
                holder.itemView.tvAmountValue.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext ,
                        R.color.color_accent_green
                    )
                )
            }

            holder.itemView.indivigual_payment_detailsTv.setOnClickListener {
                selectedId.invoke(paymentHistoryList[position].id)
            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
}


