package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.status.status

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.owner.owner_list_requested_services.OwnerService
import kotlinx.android.synthetic.main.recycle_owner_status_adapter_list_item.view.*


class StatusAdapter(
    private var ownerServices : ArrayList<OwnerService> ,
    private val function : (String) -> Unit
) : RecyclerView.Adapter<StatusAdapter.ViewHold>() {
    private var context : Context? = null

    class ViewHold(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_owner_status_adapter_list_item , parent , false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount() : Int {
        return ownerServices.size
    }

    override fun onBindViewHolder(holder : ViewHold , position : Int) {
        holder.itemView.tvStatusOrderId.text = ownerServices[position].id.toString()
        if (ownerServices[position].service_related != null) {
            holder.itemView.recyerview_adapter_service_name.text =
                ownerServices[position].service_related.service
        }
        holder.itemView.tvStatusTime.text = ownerServices[position].time
        holder.itemView.tvStatusDate.text = ownerServices[position].date
        holder.itemView.tvStatusPropertyName.text = ownerServices[position].property_name

        holder.itemView.setOnClickListener {
            function.invoke(ownerServices[position].id.toString())
        }
        /*if(AppPreferences.lang=="arabic"){
            holder.itemView.ivLineTest.background = ContextCompat.getDrawable(context,R.drawable.bg_line_two_color_2)
        }else{
            holder.itemView.ivLineTest.background = ContextCompat.getDrawable(context,R.drawable.bg_line_two_color)
        }*/
        if (ownerServices[position].status == 0) {
            holder.itemView.ivLineTest.background =
                ContextCompat.getDrawable(context!! , R.drawable.bg_line_two_color)
            holder.itemView.tvStatus.text = context!!.getString(R.string.tvStatus_send)
        } else if (ownerServices[position].status == 1) {
            holder.itemView.ivLineTest.background =
                ContextCompat.getDrawable(context!! , R.drawable.bg_line_two_color_full_green)
            holder.itemView.ivLineTest1.background =
                ContextCompat.getDrawable(context!! , R.drawable.bg_line_two_color)
            holder.itemView.tvStatus.text =
                context!!.getString(R.string.tvStatus_inspection_completed)
            holder.itemView.radio_button_1.isChecked = false
            holder.itemView.radio_button_2.isChecked = true
            holder.itemView.radio_button_1.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.green_light_1)
            holder.itemView.radio_button_2.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.green_light_1)
        } else if (ownerServices[position].status == 2) {
            holder.itemView.ivLineTest.background =
                ContextCompat.getDrawable(context!! , R.drawable.bg_line_two_color_full_green)
            holder.itemView.ivLineTest1.background =
                ContextCompat.getDrawable(context!! , R.drawable.bg_line_two_color_full_green)
            holder.itemView.ivLineTest2.background =
                ContextCompat.getDrawable(context!! , R.drawable.bg_line_two_color)
            holder.itemView.tvStatus.text = context!!.getString(R.string.tvStatus_payment_done)
            holder.itemView.radio_button_1.isChecked = false
            holder.itemView.radio_button_2.isChecked = false
            holder.itemView.radio_button_3.isChecked = true
            holder.itemView.radio_button_1.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.green_light_1)
            holder.itemView.radio_button_2.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.green_light_1)
            holder.itemView.radio_button_3.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.green_light_1)
        } else if (ownerServices[position].status == 3) {
            holder.itemView.ivLineTest.background =
                ContextCompat.getDrawable(context!! , R.drawable.bg_line_two_color_full_green)
            holder.itemView.ivLineTest1.background =
                ContextCompat.getDrawable(context!! , R.drawable.bg_line_two_color_full_green)
            holder.itemView.ivLineTest2.background =
                ContextCompat.getDrawable(context!! , R.drawable.bg_line_two_color_full_green)
            holder.itemView.tvStatus.text = context!!.getString(R.string.tvStatus_completed)
            holder.itemView.radio_button_1.isChecked = false
            holder.itemView.radio_button_2.isChecked = false
            holder.itemView.radio_button_3.isChecked = false
            holder.itemView.radio_button_4.isChecked = true
            holder.itemView.radio_button_1.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.green_light_1)
            holder.itemView.radio_button_2.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.green_light_1)
            holder.itemView.radio_button_3.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.green_light_1)
            holder.itemView.radio_button_4.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.green_light_1)
        } else if (ownerServices[position].status == 4) {
            holder.itemView.ivLineTest.background =
                ContextCompat.getDrawable(context!! , R.drawable.bg_line_two_color_full_gray)
            holder.itemView.ivLineTest1.background =
                ContextCompat.getDrawable(context!! , R.drawable.bg_line_two_color_full_gray)
            holder.itemView.ivLineTest2.background =
                ContextCompat.getDrawable(context!! , R.drawable.bg_line_two_color_full_gray)
            holder.itemView.tvStatus.text = context!!.getString(R.string.tvStatus_cancelled)
            holder.itemView.radio_button_1.isChecked = false
            holder.itemView.radio_button_2.isChecked = false
            holder.itemView.radio_button_3.isChecked = false
            holder.itemView.radio_button_4.isChecked = false
            holder.itemView.radio_button_1.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.lightGray)
            holder.itemView.radio_button_2.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.lightGray)
            holder.itemView.radio_button_3.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.lightGray)
            holder.itemView.radio_button_4.buttonTintList =
                ContextCompat.getColorStateList(context!! , R.color.lightGray)
        }

    }
}

