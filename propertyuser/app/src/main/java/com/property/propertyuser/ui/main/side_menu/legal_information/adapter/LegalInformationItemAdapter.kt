package com.property.propertyuser.ui.main.side_menu.legal_information.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.legal_informations.LegalInformation
import com.property.propertyuser.utils.setMargins
import kotlinx.android.synthetic.main.list_legal_information_item.view.*
import kotlinx.android.synthetic.main.list_privacy_policy_item.view.*

class LegalInformationItemAdapter(
    private val context: Context,
    private var legalInformations: List<LegalInformation>,
)
    : RecyclerView.Adapter<LegalInformationItemAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.list_legal_information_item,parent,false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return legalInformations.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvLegalInformationTitle.text=legalInformations[position].title
        if(position==legalInformations.size-1){
            holder.itemView.wvLegalInformation.setMargins(0,0,0,200)
        }
        holder.itemView.wvLegalInformation.settings.loadWithOverviewMode = true
        holder.itemView.wvLegalInformation.settings.allowFileAccess = true
        holder.itemView.wvLegalInformation.settings.javaScriptEnabled = true
        holder.itemView.wvLegalInformation.loadData(legalInformations[position].description, "text/html", "UTF-8")

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }
}