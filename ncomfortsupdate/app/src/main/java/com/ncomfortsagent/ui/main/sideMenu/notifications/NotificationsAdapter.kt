package com.ncomfortsagent.ui.main.sideMenu.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ncomfortsagent.databinding.RecyclerNotificationItemBinding


class NotificationsAdapter(private var requireContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        val view = RecyclerNotificationItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    class ViewHolder(var binding: RecyclerNotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

        }

    }
}

