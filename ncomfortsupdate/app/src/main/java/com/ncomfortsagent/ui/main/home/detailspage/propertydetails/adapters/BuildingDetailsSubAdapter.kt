package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ncomfortsagent.R
import com.ncomfortsagent.databinding.ItemSubBuildingDetailsBinding
import com.ncomfortsagent.model.building_details.AgentBuildingDetailsBuildingApartment
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.activity.PropertyDetailsActivity
import com.ncomfortsagent.utils.Constants


class BuildingDetailsSubAdapter(
    private var requireContext: Activity,
    private var buildingApartments: List<AgentBuildingDetailsBuildingApartment>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATA = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view =
                    ItemSubBuildingDetailsBinding.inflate(
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
        return buildingApartments.size
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_DATA
    }

    class ViewHolder(var binding: ItemSubBuildingDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            Glide.with(requireContext)
                .load(buildingApartments[position].property_priority_image.document).placeholder(
                    R.drawable.no_image
                ).into(holder.binding.ivBuildingImage)

            holder.binding.tvPropertyName.text = buildingApartments[position].property_name

            /* occupied -> 1 */
            /* vacant -> 0 */
            /* property_to
               * 0 -> RENT
               * 1 -> BUY */
            if (buildingApartments[position].occupied == 1) {
                if (buildingApartments[position].property_to == 0) {
                    holder.binding.tvPropertyStatus.text =
                        requireContext.getString(R.string.occupied) + " / " + requireContext.getString(
                            R.string.rent
                        )
                    holder.binding.tvPropertyStatus.setTextColor(Color.parseColor("#6AC58C"))
                }
                if (buildingApartments[position].property_to == 1) {
                    holder.binding.tvPropertyStatus.text =
                        requireContext.getString(R.string.occupied) + " / " + requireContext.getString(
                            R.string.sale
                        )
                    holder.binding.tvPropertyStatus.setTextColor(Color.parseColor("#6AC58C"))
                }

            }
            if (buildingApartments[position].occupied == 0) {
                if (buildingApartments[position].property_to == 0) {
                    holder.binding.tvPropertyStatus.text =
                        requireContext.getString(R.string.vacant) + " / " + requireContext.getString(
                            R.string.rent
                        )
                    holder.binding.tvPropertyStatus.setTextColor(Color.parseColor("#EABC6B"))
                }
                if (buildingApartments[position].property_to == 1) {
                    holder.binding.tvPropertyStatus.text =
                        requireContext.getString(R.string.vacant) + " / " + requireContext.getString(
                            R.string.sale
                        )
                    holder.binding.tvPropertyStatus.setTextColor(Color.parseColor("#EABC6B"))
                }

            }

            holder.binding.tvBuildingType.text = buildingApartments[position].type_details.type

            holder.itemView.setOnClickListener {
                val intent = Intent(requireContext, PropertyDetailsActivity::class.java)
                intent.putExtra(Constants.PROPERTY_ID, buildingApartments[position].id)
                requireContext.startActivity(intent)
            }

        }

    }

}