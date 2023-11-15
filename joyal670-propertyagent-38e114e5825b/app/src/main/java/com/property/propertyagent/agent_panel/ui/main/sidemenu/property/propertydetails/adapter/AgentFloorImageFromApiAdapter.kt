package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyagent.modal.agent.agent_assigned_property_details.FloorPlan
import kotlinx.android.synthetic.main.recycer_image_upload_item.view.*

class AgentFloorImageFromApiAdapter(
    private val images: ArrayList<FloorPlan>,
    val onItemClicked: (Int, Int) -> Unit,
) : RecyclerView.Adapter<AgentFloorImageFromApiAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(com.property.propertyagent.R.layout.recycer_image_upload_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        context?.let {
            Glide.with(it).load(images[position].document_image).into(holder.itemView.image_itemImg)
        }

        holder.itemView.image_itemImg_remove.setOnClickListener {
            onItemClicked.invoke(position, images[position].id)
        }
    }
}

