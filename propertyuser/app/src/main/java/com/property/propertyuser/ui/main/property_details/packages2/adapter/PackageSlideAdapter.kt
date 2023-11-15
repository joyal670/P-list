package com.property.propertyuser.ui.main.property_details.packages2.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.event_package.EventPackage
import com.property.propertyuser.modal.other_package.Package
import com.property.propertyuser.ui.main.event.book_event.EventBookingActivity
import com.property.propertyuser.ui.main.property_details.packages.adapter.PackageDescriptionAdapter
import com.property.propertyuser.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.fragment_weekly.*
import kotlinx.android.synthetic.main.list_event_booking_item.view.*
import kotlinx.android.synthetic.main.list_package_booking_item.view.*

class PackageSlideAdapter(private val context: Context,
                          private var  packagesList: List<Package>,
                          private val functionSelected:(String, Int, String)->Unit
) : RecyclerView.Adapter<PackageSlideAdapter.PackageBookingHolder>() {


    class PackageBookingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageBookingHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_package_booking_item, parent, false)
        return PackageBookingHolder(v)
    }

    override fun getItemCount(): Int {
        return packagesList.size
    }

    override fun onBindViewHolder(holder: PackageBookingHolder, position: Int) {

        holder.itemView.tvPropertyDurationType.text=packagesList[position].offer_package_name
        holder.itemView.tvPropertyAmount.text=
            context.getString(R.string.sar)+""+packagesList[position].discount_amount
        holder.itemView.tvPropertyDiscountAmount.text=
            context.getString(R.string.sar)+""+packagesList[position].actual_amount
        holder.itemView.tvPropertyDiscountAmount.paintFlags =
            holder.itemView.tvPropertyDiscountAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.itemView.rvPackageDescriptionList.layoutManager = LinearLayoutManager(context)
        holder.itemView.rvPackageDescriptionList.adapter =
            com.property.propertyuser.ui.main.property_details.packages2.adapter.PackageDescriptionAdapter(context,packagesList[position].package_features)
        holder.itemView.btnSelect.setOnClickListener {
            functionSelected.invoke(packagesList[position].discount_amount, packagesList[position].id, packagesList[position].offer_package_name)
        }
    }
}