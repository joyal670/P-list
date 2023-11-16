package com.property.propertyagent.agent_panel.ui.main.home.myclient.users.completed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_user_booking_property_completed.UserProperty
import com.property.propertyagent.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.recycle_user_ongoing_list_item.view.*

class UserCompletedAdapter(
    private var userProperties: List<UserProperty>,
    private val function: (String) -> Unit,
    private val onClickWhatsapp: (String) -> Unit,
    private val onClickDialer: (String) -> Unit,
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
        return userProperties.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (userProperties[position].id) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {

        if (holder is ViewHolder) {
            if (!userProperties[position].property_priority_image.equals(null)) {
                if (!userProperties[position].property_priority_image.document.equals(null)) {
                    holder.itemView.ivPropertyImageOngoing.loadImagesWithGlideExt(
                        userProperties[position].property_priority_image.document
                    )
                }
            }
            if (!userProperties[position].property_details.equals(null)) {
                holder.itemView.tvPropertyNameOngoing.text =
                    userProperties[position].property_details.property_name
                holder.itemView.tvPropertyCodeOngoing.text =
                    userProperties[position].property_details.property_reg_no
            }
            if (!userProperties[position].user_rel.equals(null)) {
                holder.itemView.ivProfilePicAgent.loadImagesWithGlideExt(userProperties[position].user_rel.profile_pic)
                holder.itemView.tvProfileNameOngoing.text = userProperties[position].user_rel.name
                holder.itemView.userOngoing_EmailTv.text = userProperties[position].user_rel.email
            }
            holder.itemView.setOnClickListener {
                function.invoke(userProperties[position].id.toString())
            }
            holder.itemView.userOngoing_WhatsappBtn.setOnClickListener {
                if (!userProperties[position].user_rel.equals(null)) {
                    if (userProperties[position].user_rel.phone.isNotBlank()) {
                        onClickWhatsapp.invoke(userProperties[position].user_rel.phone)
                    }
                }
            }
            holder.itemView.userOngoing_CallBtn.setOnClickListener {
                if (!userProperties[position].user_rel.equals(null)) {
                    if (userProperties[position].user_rel.phone.isNotBlank()) {
                        onClickDialer.invoke(userProperties[position].user_rel.phone)
                    }
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
