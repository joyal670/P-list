package com.property.propertyuser.ui.main.side_menu.rewards.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.user_rewards.Data
import com.property.propertyuser.ui.main.my_property.view_details.service_details.ServiceDetailsActivity
import com.property.propertyuser.ui.startup.welcome_slider.WelcomeSliderActivity
import com.property.propertyuser.utils.CommonMethods
import com.property.propertyuser.utils.copyToClipboard
import kotlinx.android.synthetic.main.list_reward_item.view.*
import kotlinx.android.synthetic.main.list_service_item.view.*

class RedeemedAdapter(private val context: Context,
                      private var rewardsList:List<Data>,
                      private val functionGetReward: (result: String) -> Unit)
    : RecyclerView.Adapter<RedeemedAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_reward_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return rewardsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        if(rewardsList[position].coupon_type == "0"){
            holder.itemView.tvRewardAmount.text=
                context.getString(R.string.flat)+" "+rewardsList[position].percentage+"%"
        }else{
            holder.itemView.tvRewardAmount.text =
                context.getString(R.string.sar)+" "+rewardsList[position].amount
        }
        holder.itemView.tvReferalCode.setText(rewardsList[position].coupon_code)
        holder.itemView.tvExpiaryDate.text =
            context.getString(R.string.tvExpiaryDate)+" "+rewardsList[position].expiry_date
        holder.itemView.tvReferalCode.setOnLongClickListener {
            context.copyToClipboard(holder.itemView.tvReferalCode.text)
            Toast.makeText(context, context.getString(R.string.copied), Toast.LENGTH_SHORT).show()
            true
        }
        holder.itemView.mcvReward.setOnClickListener {
            if(CommonMethods.isOpenRecently()) return@setOnClickListener
            holder.itemView.mcvReward.strokeColor=ContextCompat.getColor(context,R.color.green_solid)
            functionGetReward.invoke(holder.itemView.tvRewardAmount.text.toString())
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}