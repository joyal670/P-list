package com.iroid.healthdomain.ui.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.index.PastChallenge
import com.iroid.healthdomain.databinding.ListItemPastChallengesBinding


class PastChallengesAdapter : RecyclerView.Adapter<PastChallengesAdapter.MyViewHolder>() {

    var list: List<PastChallenge> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(var binding: ListItemPastChallengesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setModel(allChallengeModel: PastChallenge) {
            binding.challenge = allChallengeModel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ListItemPastChallengesBinding.inflate(
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