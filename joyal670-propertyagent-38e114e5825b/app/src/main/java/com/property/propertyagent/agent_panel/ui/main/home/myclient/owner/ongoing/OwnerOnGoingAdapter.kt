package com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.ongoing

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_owner_ongoing_list.UserProperty
import com.property.propertyagent.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.recycle_owner_ongoing_list_item.view.*

class OwnerOnGoingAdapter(
    private var userPropertiesList: List<UserProperty>,
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
                    .inflate(R.layout.recycle_owner_ongoing_list_item, parent, false)
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
        return userPropertiesList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (userPropertiesList[position].id) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {

        if (holder is ViewHolder) {
            if (!userPropertiesList[position].user_property_related.equals(null)) {
                holder.itemView.tvPropertyNameOwnerOngoing.text =
                    userPropertiesList[position].user_property_related.property_name
                holder.itemView.tvPropertyCodeOwnerOngoing.text =
                    userPropertiesList[position].user_property_related.property_reg_no
            }
            try {
                if (!userPropertiesList[position].property_priority_image.equals(null)) {
                    if (!userPropertiesList[position].property_priority_image.document.equals(null))
                        holder.itemView.ivImageOwnerOngoing.loadImagesWithGlideExt(
                            userPropertiesList[position].property_priority_image.document
                        )
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }

            if (!userPropertiesList[position].owner_rel.equals(null)) {
                holder.itemView.ivProfilePicOwnerOngoing.loadImagesWithGlideExt(userPropertiesList[position].owner_rel.profile_image)
                holder.itemView.tvProfileOwnerName.text =
                    userPropertiesList[position].owner_rel.name
                holder.itemView.OwnerongoingRecyerview_email.text =
                    userPropertiesList[position].owner_rel.email
            }

            holder.itemView.setOnClickListener {
                Log.e("one", userPropertiesList[position].id.toString())
                function.invoke(userPropertiesList[position].id.toString())
            }

            holder.itemView.OwnerongoingRecyerview_CallTv.setOnClickListener {
                if (userPropertiesList[position].owner_rel.phone.isNotEmpty()) {
                    onclickDialer.invoke(userPropertiesList[position].owner_rel.phone)
                }
            }

            holder.itemView.OwnerongoingRecyerview_WhatsappTv.setOnClickListener {
                if (userPropertiesList[position].owner_rel.phone.isNotEmpty()) {
                    onclickWhatsapp.invoke(userPropertiesList[position].owner_rel.phone)
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
