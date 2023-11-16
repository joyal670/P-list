package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.building_view_details.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.building_view_details.AgentBuildingApartmentDetailsActivity
import com.property.propertyagent.modal.agent.agent_builder_details.Unit
import kotlinx.android.synthetic.main.recycle_agent_builder_apartment_item.view.*

class AgentBuilderApartmentAdapter(
    private val buildingList: ArrayList<Unit>,
) : RecyclerView.Adapter<AgentBuilderApartmentAdapter.ViewHold>() {
    private lateinit var context: Context

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_agent_builder_apartment_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return buildingList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        if ((holder.absoluteAdapterPosition) % 2 == 1) {
            holder.itemView.recyerview_details_adapter_dividerView.visibility = View.INVISIBLE
        }

        Glide.with(context).load(buildingList[position].property_priority_image.document_image)
            .into(holder.itemView.recyerview_details_adapter_building_image)
        holder.itemView.tvPropertyName.text = buildingList[position].unit_name
        holder.itemView.tvPropertyCode.text = buildingList[position].property_reg_no
        holder.itemView.tvAmount.text = buildingList[position].expected_amount

        if (buildingList[position].occupied == "0") {
            holder.itemView.tvVacantStatus.text =
                context.getString(R.string.vacant)
            holder.itemView.tvVacantStatus.setTextColor(
                Color.parseColor(
                    "#EABC6B"
                )
            )
        } else {
            holder.itemView.tvVacantStatus.text =
                context.getString(R.string.occupied)
            holder.itemView.tvVacantStatus.setTextColor(
                Color.parseColor(
                    "#6AC58C"
                )
            )
        }

        when (buildingList[position].furnished) {
            "0" -> {
                holder.itemView.tvFurnishedType.text = context.getString(R.string.not_furnished)
            }
            "1" -> {
                holder.itemView.tvFurnishedType.text = context.getString(R.string.semifurnished)
            }
            else -> {
                holder.itemView.tvFurnishedType.text = context.getString(R.string.fully)
            }
        }

        holder.itemView.recyerview_details_adapter_building_layout.setOnClickListener {
            val intent = Intent(context, AgentBuildingApartmentDetailsActivity::class.java)
            intent.putExtra("property_id", buildingList[position].id.toString())
            context.startActivity(intent)
        }
    }
}