package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recycer_video_upload_item.view.*

class AgentVideoUploadAdapter(
    private val videos: ArrayList<Uri>,
    val onItemClicked: (Int) -> Unit,
) : RecyclerView.Adapter<AgentVideoUploadAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(com.property.propertyagent.R.layout.recycer_video_upload_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        context?.let {
            Glide.with(it).load(videos[position]).into(holder.itemView.video_itemVideoView)
        }

        holder.itemView.video_itemImg_remove.setOnClickListener {
            onItemClicked.invoke(position)
        }
    }
}
