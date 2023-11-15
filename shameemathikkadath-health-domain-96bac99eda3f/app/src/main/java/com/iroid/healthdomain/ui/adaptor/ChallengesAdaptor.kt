package com.iroid.healthdomain.ui.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.healthdomain.data.model_class.index.Challenge
import com.iroid.healthdomain.data.model_class.index.PendingChallenge
import com.iroid.healthdomain.databinding.ListItemChallengesBinding
import com.iroid.healthdomain.ui.adptorInterface.AdaptorInterface

class ChallengesAdaptor(val listener: AdaptorInterface) : RecyclerView.Adapter<ChallengesAdaptor.MyViewHolder>() {

    var list: List<PendingChallenge> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(var binding: ListItemChallengesBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun setModel(model: PendingChallenge) {
            binding.model = model

            binding.circularIndicator.value = model.challengeScore

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
                ListItemChallengesBinding.inflate(
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
