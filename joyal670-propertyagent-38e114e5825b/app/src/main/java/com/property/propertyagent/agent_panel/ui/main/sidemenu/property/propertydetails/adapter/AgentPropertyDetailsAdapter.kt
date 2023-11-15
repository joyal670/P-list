package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.property.propertyagent.modal.agent.agent_assigned_property_details.PropertyDetail
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_Detail
import kotlinx.android.synthetic.main.recycer_main_pdetails.view.*

class AgentPropertyDetailsAdapter(
    private var amenityDetail: ArrayList<Amenity_Detail>,
    private var detailsListPassed: ArrayList<PropertyDetail>,
    private var getNoText: (Int, Int) -> Unit,
) : RecyclerView.Adapter<AgentPropertyDetailsAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(com.property.propertyagent.R.layout.recycer_main_pdetails, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return amenityDetail.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        Log.e("det1", Gson().toJson(amenityDetail))
        Log.e("det2", Gson().toJson(detailsListPassed))

        holder.itemView.textInputLayout.hint = amenityDetail[position].placeholder

        for (i in 0 until amenityDetail.size) {
            for (j in 0 until detailsListPassed.size) {
                if (amenityDetail[i].detail_id == detailsListPassed[j].detail_id) {
                    try {
                        holder.itemView.pDetailsEtx.setText(detailsListPassed[position].value.toString())
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        holder.itemView.pDetailsEtx.doAfterTextChanged {
            if (it != null) {
                if (it.length <= 4 && !it.isNullOrEmpty())
                    getNoText.invoke(
                        amenityDetail[position].detail_id,
                        holder.itemView.pDetailsEtx.text.toString().toInt()
                    )
            }
        }
    }
}


