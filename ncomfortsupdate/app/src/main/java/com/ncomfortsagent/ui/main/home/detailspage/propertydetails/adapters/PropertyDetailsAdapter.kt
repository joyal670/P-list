package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ncomfortsagent.databinding.RecycleDetailsListItemBinding
import com.ncomfortsagent.model.property_details.AgentPropertyDetailsList
import com.ncomfortsagent.utils.loadImagesWithGlideExt


class PropertyDetailsAdapter(private var detailsList: ArrayList<AgentPropertyDetailsList>) :
    RecyclerView.Adapter<PropertyDetailsAdapter.ViewHold>() {

    private var context: Context? = null

    class ViewHold(var binding: RecycleDetailsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            RecycleDetailsListItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }


    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.ivBedRoom.loadImagesWithGlideExt(detailsList[position].icon)
        holder.binding.tvBedRoom.text = detailsList[position].value.toString()

    }

}