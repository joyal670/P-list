package com.iroid.healthdomain.ui.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.healthdomain.data.model_class.index.PendingChallenge
import com.iroid.healthdomain.data.model_class.notification_receive.ReceiveNotificationData
import com.iroid.healthdomain.databinding.ListItemReceivedNotificationBinding

class ReceivedNotificationAdapter :
    RecyclerView.Adapter<ReceivedNotificationAdapter.MyViewHolder>() {

    var list: List<ReceiveNotificationData> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(var binding: ListItemReceivedNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setModel(model: ReceiveNotificationData) {
            binding.notification = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ListItemReceivedNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
           holder.setModel(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}