package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.viewpackages.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_package_details.Package
import com.property.propertyagent.utils.AppPreferences.selected_package_id
import kotlinx.android.synthetic.main.item_package.view.*

class PackageSlideAdapter(
    private val context: Context,
    private var packageNameList: List<Package>,
    private val functionSelected: (Int) -> Unit,
) : RecyclerView.Adapter<PackageSlideAdapter.PackageBookingHolder>() {

    class PackageBookingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageBookingHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_package, parent, false)
        return PackageBookingHolder(v)
    }

    override fun getItemCount(): Int {
        return packageNameList.size
    }

    override fun onBindViewHolder(holder: PackageBookingHolder, position: Int) {

        holder.itemView.tvPropertyDurationType.text = packageNameList[position].offer_package_name
        holder.itemView.tvPropertyAmount.text = packageNameList[position].actual_amount
        holder.itemView.tvPropertyDiscountAmount.text = packageNameList[position].discount_amount

        holder.itemView.rvPackageDescriptionList.layoutManager = LinearLayoutManager(context)
        holder.itemView.rvPackageDescriptionList.adapter =
            PackageDescriptionAdapter(packageNameList[position].package_features)
        holder.itemView.btnSelect.setOnClickListener {
            selected_package_id = packageNameList[position].id.toString()
            functionSelected.invoke(position)
        }
    }
}