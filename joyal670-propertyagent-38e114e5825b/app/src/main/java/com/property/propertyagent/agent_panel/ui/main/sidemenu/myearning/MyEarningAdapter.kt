package com.property.propertyagent.agent_panel.ui.main.sidemenu.myearning

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.PropertyViewDetailsActivity
import com.property.propertyagent.modal.agent.my_earnings.Payment
import kotlinx.android.synthetic.main.recycle_myearnig_list_item.view.*

class MyEarningAdapter(
    private val context: Context,
    private var earningsList: List<Payment>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_myearnig_list_item, parent, false)
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
        return earningsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (earningsList[position].id) {
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
            holder.itemView.myearning_view_property.paintFlags =
                holder.itemView.myearning_view_property.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            holder.itemView.myearning_code.text =
                context.getString(R.string.sar) + " " + earningsList[position].amount
            holder.itemView.tvPropertyCode.text =
                earningsList[position].property_rel.property_reg_no

            holder.itemView.myearning_view_property.setOnClickListener {
                val intent = Intent(
                    context,
                    PropertyViewDetailsActivity::class.java
                )
                intent.putExtra("property_id", earningsList[position].property_id.toString())
                context.startActivity(intent)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

