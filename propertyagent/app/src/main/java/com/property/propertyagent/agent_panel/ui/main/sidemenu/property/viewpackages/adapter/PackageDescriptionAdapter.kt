package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.viewpackages.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_package_details.PackageFeature
import kotlinx.android.synthetic.main.list_package_description_item.view.*

class PackageDescriptionAdapter(
    private val packageDescriptionList: List<PackageFeature>,
) : RecyclerView.Adapter<PackageDescriptionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_package_description_item, parent, false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return packageDescriptionList.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.itemView.tvDescription.text = packageDescriptionList[position].package_feature
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}