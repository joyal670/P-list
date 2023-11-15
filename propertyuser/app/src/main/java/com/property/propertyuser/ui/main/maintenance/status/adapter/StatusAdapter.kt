package com.property.propertyuser.ui.main.maintenance.status.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.list_requested_service_details.UserService
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.maintenance.status_details.StatusDetailsActivity
import kotlinx.android.synthetic.main.list_status_item.view.*

class StatusAdapter(
    private val context: Context,
    private var userServices: List<UserService>,
    private var userPropertyId: String
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
                    .inflate(R.layout.list_status_item, parent, false)
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

    override fun getItemViewType(position: Int): Int {
        var viewtype = userServices[position].id
        return when (viewtype) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun getItemCount(): Int {
        return userServices.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is ViewHolder) {
            holder.itemView.tvOrderId.text =
                context.getString(R.string.tvOrderId) + userServices[position].id
            if (userServices[position].service_related != null) {
                holder.itemView.tvServiceName.text =
                    context.getString(R.string.tvServiceName_title) + " " + userServices[position].service_related.service
            }
            holder.itemView.tvTimeStatus.text = userServices[position].time
            holder.itemView.tvDateStatus.text = userServices[position].date
            userServices[position].property_name.let {
                holder.itemView.tvPropertyName.text =
                    context.getString(R.string.tvPropertyName_new) + " " + userServices[position].property_name
            }
            holder.itemView.mcvStatusCard.setOnClickListener {
                val intent = Intent(context, StatusDetailsActivity::class.java)
                Log.e("testid", userServices[position].id.toString())
                intent.putExtra("user_service_id", userServices[position].id.toString())
                context.startActivity(intent)
            }
            if (userServices[position].service_amount != null) {
                if (!userServices[position].service_amount.amount.isNullOrEmpty()) {
                    holder.itemView.tvServiceAmount.text =
                        context.getString(R.string.service_amount) + " " + userServices[position].service_amount.amount
                } else {
                    holder.itemView.tvServiceAmount.visibility = View.GONE
                }
            } else {
                holder.itemView.tvServiceAmount.visibility = View.GONE
            }


            if (AppPreferences.chooseLanguage == "arabic") {
                holder.itemView.ivLineTest.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_line_two_color_2)
            } else {
                holder.itemView.ivLineTest.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_line_two_color)
            }
            when (userServices[position].status) {
                0 -> {
                    holder.itemView.ivLineTest.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_line_two_color)
                    holder.itemView.tvStatus.text = context.getString(R.string.tvStatus_send)
                }
                1 -> {
                    holder.itemView.ivLineTest.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_line_two_color_full_green)
                    holder.itemView.ivLineTest1.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_line_two_color)
                    holder.itemView.tvStatus.text =
                        context.getString(R.string.tvStatus_inspection_completed)
                    holder.itemView.radio_button_1.isChecked = false
                    holder.itemView.radio_button_2.isChecked = true
                    holder.itemView.radio_button_1.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.green_light_1)
                    holder.itemView.radio_button_2.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.green_light_1)
                }
                2 -> {
                    holder.itemView.ivLineTest.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_line_two_color_full_green)
                    holder.itemView.ivLineTest1.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_line_two_color_full_green)
                    holder.itemView.ivLineTest2.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_line_two_color)
                    holder.itemView.tvStatus.text =
                        context.getString(R.string.tvStatus_payment_done)
                    holder.itemView.radio_button_1.isChecked = false
                    holder.itemView.radio_button_2.isChecked = false
                    holder.itemView.radio_button_3.isChecked = true
                    holder.itemView.radio_button_1.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.green_light_1)
                    holder.itemView.radio_button_2.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.green_light_1)
                    holder.itemView.radio_button_3.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.green_light_1)
                }
                3 -> {
                    holder.itemView.ivLineTest.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_line_two_color_full_green)
                    holder.itemView.ivLineTest1.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_line_two_color_full_green)
                    holder.itemView.ivLineTest2.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_line_two_color_full_green)
                    holder.itemView.tvStatus.text = context.getString(R.string.tvStatus_completed)
                    holder.itemView.radio_button_1.isChecked = false
                    holder.itemView.radio_button_2.isChecked = false
                    holder.itemView.radio_button_3.isChecked = false
                    holder.itemView.radio_button_4.isChecked = true
                    holder.itemView.radio_button_1.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.green_light_1)
                    holder.itemView.radio_button_2.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.green_light_1)
                    holder.itemView.radio_button_3.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.green_light_1)
                    holder.itemView.radio_button_4.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.green_light_1)
                }
                4 -> {
                    holder.itemView.ivLineTest.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_line_two_color_full_gray)
                    holder.itemView.ivLineTest1.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_line_two_color_full_gray)
                    holder.itemView.ivLineTest2.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_line_two_color_full_gray)
                    holder.itemView.tvStatus.text = context.getString(R.string.tvStatus_cancelled)
                    holder.itemView.radio_button_1.isChecked = false
                    holder.itemView.radio_button_2.isChecked = false
                    holder.itemView.radio_button_3.isChecked = false
                    holder.itemView.radio_button_4.isChecked = false
                    holder.itemView.radio_button_1.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.lightGray)
                    holder.itemView.radio_button_2.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.lightGray)
                    holder.itemView.radio_button_3.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.lightGray)
                    holder.itemView.radio_button_4.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.lightGray)
                }
            }
        }
    }

    private fun setupTimeLineRecyclerView(holder: ViewHolder) {
        holder.itemView.rvTimeLine.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        holder.itemView.rvTimeLine.adapter = StatusTimeLineAdapter(context)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}