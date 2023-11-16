package com.iroid.emergency.main.home.rejected.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.emergency.databinding.ItemRejectedBinding
import com.iroid.emergency.modal.common.RejectedRequest
import java.util.ArrayList

class RejectedAdapter(private val context: Context,private val rejectedList: ArrayList<RejectedRequest>):RecyclerView.Adapter<RejectedAdapter.ViewHolder> (){
    class ViewHolder(var binding: ItemRejectedBinding) :RecyclerView.ViewHolder(binding.root){
        fun bindItems(rejectedRequest: RejectedRequest) {
            binding.tvName.text=rejectedRequest.patient_name
            binding.tvAddress.text=rejectedRequest.address
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  binding=ItemRejectedBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(rejectedList[position])
    }

    override fun getItemCount(): Int {
        return rejectedList.size
    }
}
