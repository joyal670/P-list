package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyrentetails.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_property_rent_details.OtherChargeData
import kotlinx.android.synthetic.main.item_rent_type_rent.view.*

class RentDetailsAdapter(private var rentDetailsList: List<OtherChargeData>) :
    RecyclerView.Adapter<RentDetailsAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rent_type_rent, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return rentDetailsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.itemView.tvHeading.text = rentDetailsList[position].charge_name
        holder.itemView.tvDescription.text = rentDetailsList[position].descriptions
        holder.itemView.tvAmount.text = "SAR " + rentDetailsList[position].amount.toString()
    }
}
