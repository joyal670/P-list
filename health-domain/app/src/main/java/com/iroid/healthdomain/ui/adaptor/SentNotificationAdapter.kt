package com.iroid.healthdomain.ui.adaptor

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.notification_receive.ReceiveNotificationData
import com.iroid.healthdomain.data.model_class.notification_sent.SendNotificationData
import com.iroid.healthdomain.databinding.ListItemReceivedNotificationBinding
import com.iroid.healthdomain.databinding.ListItemSentNotificationBinding
import com.iroid.healthdomain.ui.preference.AppPreferences

class SentNotificationAdapter :
    RecyclerView.Adapter<SentNotificationAdapter.MyViewHolder>() {

    var list: List<SendNotificationData> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(var binding: ListItemSentNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setModel(model: SendNotificationData) {
            binding.notification = model
            when (model.challenge.current_state) {
                "PENDING" -> {
                    binding.btnStatus.text = model.challenge.current_state
                    binding.btnStatus.setBackgroundColor(ContextCompat.getColor(binding.btnStatus.context, R.color.challenge_text_color))
                }
                "ACCEPTED_LATER" -> {
                    binding.btnStatus.text = "Pending"
                    binding.btnStatus.setBackgroundColor(ContextCompat.getColor(binding.btnStatus.context, R.color.challenge_text_color))
                }
                "INITIATED" -> {
                    binding.btnStatus.text = "Pending"
                    binding.btnStatus.setBackgroundColor(ContextCompat.getColor(binding.btnStatus.context, R.color.challenge_text_color))
                }
                "COMPLETED" -> {
                    if(AppPreferences.key_user_id!!.toInt()==model.challenge.challenge_win_user.toInt()){
                        binding.btnStatus.text = "WON"
                        binding.btnStatus.setBackgroundColor(ContextCompat.getColor(binding.btnStatus.context, R.color.splash_green))
                    }else{
                        binding.btnStatus.text = "LOST"
                        binding.btnStatus.setBackgroundColor(ContextCompat.getColor(binding.btnStatus.context, R.color.color_pink))
                    }


                }
                "DECLINED" -> {
                    binding.btnStatus.text = model.challenge.current_state
                    binding.btnStatus.setBackgroundColor(ContextCompat.getColor(binding.btnStatus.context, R.color.color_btn_red))
                }
                "EXPIRED" -> {
                    binding.btnStatus.text = model.challenge.current_state
                    binding.btnStatus.setBackgroundColor(ContextCompat.getColor(binding.btnStatus.context, R.color.color_btn_red))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ListItemSentNotificationBinding.inflate(
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