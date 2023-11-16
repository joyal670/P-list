package com.ncomfortsagent.ui.main.home.detailspage.enquirydetails.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ncomfortsagent.databinding.RecycerImageUploadItemBinding


class ImageUploadAdapter(
    private var requireActivity: Context,
    private val images: ArrayList<Uri?>,
    private val onItemClicked: (Int) -> Unit,
    private val viewImage: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        val view = RecycerImageUploadItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ViewHolder(var binding: RecycerImageUploadItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            Glide.with(requireActivity).load(images[position]).into(holder.binding.imageItemImg)

            holder.binding.imageItemImgRemove.setOnClickListener {
                onItemClicked.invoke(position)
            }

            holder.binding.imageItemImg.setOnClickListener {
                viewImage.invoke(images[position].toString())
            }

        }

    }

}

