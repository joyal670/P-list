package com.property.propertyuser.ui.main.side_menu.privacy_policy.adapter

import android.content.Context
import android.graphics.Path
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.privacy_policy.PrivacyPolicy
import com.property.propertyuser.utils.setMargins
import kotlinx.android.synthetic.main.list_privacy_policy_item.view.*

class PrivacyPolicyItemAdapter(
    private val context: Context,
    private var privacyPolicies: List<PrivacyPolicy>
    )
    : RecyclerView.Adapter<PrivacyPolicyItemAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.list_privacy_policy_item,parent,false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return privacyPolicies.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        if(position==privacyPolicies.size-1){
            holder.itemView.wvPrivacyPolicy.setMargins(0,0,0,200)
        }
        holder.itemView.tvPrivacyPolicyTitle.text=privacyPolicies[position].title
        holder.itemView.wvPrivacyPolicy.settings.loadWithOverviewMode = true
        //holder.itemView.wvPrivacyPolicy.requestFocus()
        holder.itemView.wvPrivacyPolicy.settings.allowFileAccess = true
        holder.itemView.wvPrivacyPolicy.settings.javaScriptEnabled = true
        holder.itemView.wvPrivacyPolicy.loadData(privacyPolicies[position].description, "text/html", "UTF-8")
        //holder.itemView.svMain.scr

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }
}