package com.property.propertyagent.agent_panel.ui.main.sidemenu.inhandcash.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.PropertyViewDetailsActivity
import com.property.propertyagent.modal.agent.agent_cash_in_hand.CashData
import kotlinx.android.synthetic.main.recycle_cashinhand_list_item.view.*

class CashInHandAdapter(
    private val context: Context,
    private var cashInHandList: List<CashData>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_cashinhand_list_item, parent, false)
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
        return cashInHandList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (cashInHandList[position].id) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {

        if (holder is ViewHolder) {
            holder.itemView.cashinhand_view_property.paintFlags =
                holder.itemView.cashinhand_view_property.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            holder.itemView.cashinhand_SAR_value.text = cashInHandList[position].payment_amount
            Log.e("#Am12", "onBindViewHolder: ${cashInHandList[position].payment_amount}" )
            holder.itemView.tvPropertyCode.text =
                cashInHandList[position].property_rel.property_reg_no
            if (!cashInHandList[position].user_rel.name.equals(null))
                holder.itemView.tvName.text = cashInHandList[position].user_rel.name

            holder.itemView.cashinhand_view_property.setOnClickListener {
                val intent = Intent(
                    context,
                    PropertyViewDetailsActivity::class.java
                )
                intent.putExtra("property_id", cashInHandList[position].property_id.toString())
                context.startActivity(intent)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

