package com.iroid.healthdomain.ui.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.iroid.healthdomain.data.model_class.user_profile.Contact
import com.iroid.healthdomain.databinding.ListItemFollowersBinding
import androidx.databinding.BindingAdapter




class ProfileFollowersAdapter: RecyclerView.Adapter<ProfileFollowersAdapter.MyViewHolder>() {

    var list: List<Contact> = arrayListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    inner class MyViewHolder(var binding: ListItemFollowersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setModel(allContactModel: Contact) {
            binding.contact = allContactModel
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ListItemFollowersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setModel(list[position])
    }

    override fun getItemCount(): Int = list.size
}