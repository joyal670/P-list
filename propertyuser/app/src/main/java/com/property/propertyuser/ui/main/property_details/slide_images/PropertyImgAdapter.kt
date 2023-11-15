package com.property.propertyuser.ui.main.property_details.slide_images

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.property_details.Document
import kotlinx.android.synthetic.main.list_property_img.view.*
import kotlinx.android.synthetic.main.list_property_img2.view.*
import kotlinx.android.synthetic.main.list_property_img3.view.*

class PropertyImgAdapter(
    private val context: Context,
    private val documents: List<Document>,
    private val currentPosition: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 0
        const val VIEW_TYPE_TWO = 1
        const val VIEW_TYPE_THREE = 4
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_ONE -> {
                return ViewHolder1(
                    LayoutInflater.from(context)
                        .inflate(R.layout.list_property_img, parent, false)
                )
            }
            VIEW_TYPE_TWO -> {
                return ViewHolder2(
                    LayoutInflater.from(context)
                        .inflate(R.layout.list_property_img2, parent, false)
                )
            }
            else -> {
                return ViewHolder3(
                    LayoutInflater.from(context)
                        .inflate(R.layout.list_property_img3, parent, false)
                )
            }
        }
    }

    private inner class ViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            if (position == currentPosition) {
                itemView.cardView.setCardBackgroundColor(Color.parseColor("#009639"))
                itemView.cardView.layoutParams = ConstraintLayout.LayoutParams(70, 35)
            } else {
                itemView.cardView.setCardBackgroundColor(Color.parseColor("#C1C1C1"))
                itemView.cardView.layoutParams = ConstraintLayout.LayoutParams(35, 35)
            }
        }
    }

    private inner class ViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            if (position == currentPosition) {
                itemView.ivVideo.borderWidth = 4
                itemView.ivVideo.borderColor = Color.parseColor("#009639")
            } else {
                itemView.ivVideo.borderWidth = 0
                itemView.ivVideo.borderColor = Color.parseColor("#ffffff")
            }
        }
    }

    private inner class ViewHolder3(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            if (position == currentPosition) {
                itemView.ivYoutube.borderWidth = 4
                itemView.ivYoutube.borderColor = Color.parseColor("#009639")
            } else {
                itemView.ivYoutube.borderWidth = 0
                itemView.ivYoutube.borderColor = Color.parseColor("#ffffff")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return documents[position].type
    }

    override fun getItemCount(): Int {
        return documents.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (documents[position].type) {
            VIEW_TYPE_ONE -> {
                (holder as ViewHolder1).bind(position)
            }
            VIEW_TYPE_TWO -> {
                (holder as ViewHolder2).bind(position)
            }
            else -> {
                (holder as ViewHolder3).bind(position)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}