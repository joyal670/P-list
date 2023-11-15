package com.iroid.healthdomain.ui.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.healthdomain.data.model_class.index.ActiveChallenge

import com.iroid.healthdomain.databinding.ListItemActiveChallengesBinding

class ActiveChallengesAdapter : RecyclerView.Adapter<ActiveChallengesAdapter.MyViewHolder>() {

    var list: List<ActiveChallenge> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(var binding: ListItemActiveChallengesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setModel(allChallengeModel: ActiveChallenge) {
            binding.challenge = allChallengeModel
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActiveChallengesAdapter.MyViewHolder {
        return MyViewHolder(
            ListItemActiveChallengesBinding.inflate(
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
