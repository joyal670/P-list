package com.iroid.emergency.main.home.accepted.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.emergency.R
import com.iroid.emergency.databinding.ItemAcceptedBinding
import com.iroid.emergency.modal.common.home.AcceptedRequest
import com.iroid.emergency.start_up.utils.CommonUtils

class AcceptedAdapter(
    private val context: Context,
    private val acceptedRequestList: ArrayList<AcceptedRequest>
) : RecyclerView.Adapter<AcceptedAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemAcceptedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(acceptedRequest: AcceptedRequest, context: Context) {
            binding.tvName.text = acceptedRequest.patient_name
            try {
                binding.tvProfile.text= acceptedRequest.patient_name[0].toString()+acceptedRequest.patient_name[1].toString()
            } catch (e: Exception) {
                Log.e("TAG", "bindItems: " )
            }
            val drawable: Drawable = context.getDrawable(R.drawable.circle_text)!!
            drawable.mutate().setColorFilter(CommonUtils.roundBg(), PorterDuff.Mode.SRC_IN)
            binding.tvProfile.background=drawable
            binding.tvAddress.text = acceptedRequest.address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemAcceptedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(acceptedRequestList[position],context)
    }

    override fun getItemCount(): Int {
        return acceptedRequestList.size
    }
}
