package com.ncomfortsagent.ui.main.home.home.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ncomfortsagent.R
import com.ncomfortsagent.databinding.ItemSubBuildingBinding
import com.ncomfortsagent.model.property.PropertyNewBuildingApartment
import com.ncomfortsagent.ui.main.home.detailspage.enquirydetails.activity.EnquiryDetailsActivity
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.activity.PropertyDetailsActivity
import com.ncomfortsagent.utils.CommonUtils
import com.ncomfortsagent.utils.Constants

class BuildingSubAdapter(
    private var buildingApartments: List<PropertyNewBuildingApartment>,
    private var requireContext: Activity
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATA = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view =
                    ItemSubBuildingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount(): Int {
        return if (buildingApartments.size <= 6) {
            buildingApartments.size
        } else {
            6
        }
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_DATA
    }

    class ViewHolder(var binding: ItemSubBuildingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            Glide.with(requireContext)
                .load(buildingApartments[position].property_priority_image.document).placeholder(
                    R.drawable.no_image
                ).into(holder.binding.ivBuildingImage)

            /* occupied -> 1 */
            /* vacant -> 0 */
            if (buildingApartments[position].occupied == 1) {
                holder.binding.tvBuildingStatus.text = requireContext.getString(R.string.occupied)
                holder.binding.tvBuildingStatus.setTextColor(Color.parseColor("#6AC58C"))
            }
            if (buildingApartments[position].occupied == 0) {
                holder.binding.tvBuildingStatus.text = requireContext.getString(R.string.vacant)
                holder.binding.tvBuildingStatus.setTextColor(Color.parseColor("#EABC6B"))
            }

            holder.binding.tvBuildingType.text = buildingApartments[position].type_details.type

            holder.itemView.setOnClickListener {
                if(CommonUtils.isOpenRecently()) return@setOnClickListener

                when(buildingApartments[position].occupied){
                    0 -> {
                        val intent = Intent(requireContext, PropertyDetailsActivity::class.java)
                        intent.putExtra(Constants.PROPERTY_ID, buildingApartments[position].id)
                        requireContext.startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(requireContext, EnquiryDetailsActivity::class.java)
                        intent.putExtra(Constants.ENQUIRY_ID, "135")
                        intent.putExtra("enquiry_type","1")
                        requireContext.startActivity(intent)
                    }
                }


            }

        }

    }

}