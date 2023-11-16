package com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.completed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_owner_completed_list.UserProperty
import com.property.propertyagent.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.recycle_user_ongoing_list_item.view.*

class OwnerCompletedAdapter(
    private var userPropertiesListCompleted: List<UserProperty>,
    private val function: (String) -> Unit,
    private val onclickWhatsapp: (String) -> Unit,
    private val onclickDialer: (String) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_user_ongoing_list_item, parent, false)
                ViewHolder(view)
            }
            VIEW_TYPE_PROGRESS -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.loader, parent, false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount(): Int {
        return userPropertiesListCompleted.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (userPropertiesListCompleted[position].id) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {

        if (holder is ViewHolder) {
            if (!userPropertiesListCompleted[position].user_property_related.equals(null)) {
                holder.itemView.tvPropertyNameOngoing.text =
                    userPropertiesListCompleted[position].user_property_related.property_name
                holder.itemView.tvPropertyCodeOngoing.text =
                    userPropertiesListCompleted[position].user_property_related.property_reg_no
            }
            if (!userPropertiesListCompleted[position].property_priority_image.equals(null)) {
                holder.itemView.ivPropertyImageOngoing.loadImagesWithGlideExt(
                    userPropertiesListCompleted[position].property_priority_image.document
                )
            }
            if (!userPropertiesListCompleted[position].owner_rel.equals(null)) {
                holder.itemView.ivProfilePicAgent.loadImagesWithGlideExt(userPropertiesListCompleted[position].owner_rel.profile_image)
                holder.itemView.tvProfileNameOngoing.text =
                    userPropertiesListCompleted[position].owner_rel.name
                holder.itemView.userOngoing_EmailTv.text =
                    userPropertiesListCompleted[position].owner_rel.email
            }
            holder.itemView.setOnClickListener {
                function.invoke(userPropertiesListCompleted[position].id.toString())
            }

            holder.itemView.userOngoing_CallBtn.setOnClickListener {
                if (userPropertiesListCompleted[position].owner_rel.phone.isNotEmpty()) {
                    onclickDialer.invoke(userPropertiesListCompleted[position].owner_rel.phone)
                }
            }
            holder.itemView.userOngoing_WhatsappBtn.setOnClickListener {
                if (userPropertiesListCompleted[position].owner_rel.phone.isNotEmpty()) {
                    onclickWhatsapp.invoke(userPropertiesListCompleted[position].owner_rel.phone)
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
