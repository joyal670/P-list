package com.property.propertyuser.ui.main.my_property.view_details.service_details.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyuser.R
import kotlinx.android.synthetic.main.list_uploaded_files_item.view.*

class ImageUploadAdapter(
    private val context: Context,
    private val images: ArrayList<Uri?>,
    val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<ImageUploadAdapter.ViewHold>()
{
    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_uploaded_files_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int
    {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
         Glide.with(context).load(images[position]).into(holder.itemView.ivFileImage)

        holder.itemView.ivCloseButton.setOnClickListener {
            onItemClicked.invoke(position)
            Log.e("CLICK POS ADAPTER", "initData: "+ position)
        }
    }
}