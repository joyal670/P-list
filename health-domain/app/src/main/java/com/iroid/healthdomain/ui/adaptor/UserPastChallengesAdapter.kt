package com.iroid.healthdomain.ui.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.healthdomain.data.model_class.user_challenge.DataX
import com.iroid.healthdomain.databinding.ListItemUserPastChallengesBinding

class UserPastChallengesAdapter(val id: Int) :
    RecyclerView.Adapter<UserPastChallengesAdapter.MyViewHolder>() {

    var list: List<DataX> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(var binding: ListItemUserPastChallengesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setModel(data: DataX) {
            binding.model = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ListItemUserPastChallengesBinding.inflate(
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