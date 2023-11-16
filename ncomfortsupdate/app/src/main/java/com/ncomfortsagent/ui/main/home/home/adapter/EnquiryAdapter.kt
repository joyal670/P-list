package com.ncomfortsagent.ui.main.home.home.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ncomfortsagent.R
import com.ncomfortsagent.databinding.ItemEnquiryBinding
import com.ncomfortsagent.model.enquiry.AgentHomeEnquiryUserProperty
import com.ncomfortsagent.ui.main.chat.ChatActivity
import com.ncomfortsagent.utils.CommonUtils
import com.ncomfortsagent.utils.loadImageToCoil

class EnquiryAdapter(
    private var requireActivity: Activity,
    private var selectedItem: (Int) -> Unit,
    private var enquiryData: ArrayList<AgentHomeEnquiryUserProperty>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view =
                    ItemEnquiryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        return enquiryData.size
    }

    override fun getItemViewType(position: Int): Int {
        val viewtype = enquiryData[position].id
        return when (viewtype) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    class ViewHolder(var binding: ItemEnquiryBinding) : RecyclerView.ViewHolder(binding.root)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            /* view_status 
            * 0 -> not read 
            * 1 -> read */
            if (enquiryData[position].view_status == 0) {
                holder.binding.mainCard.setCardBackgroundColor(Color.parseColor("#E9E9E9"))
            }
            if (enquiryData[position].view_status == 1) {
                holder.binding.mainCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            holder.binding.tvUserName.text = enquiryData[position].user_rel.name
            holder.binding.tvUserEmail.text = enquiryData[position].user_rel.email


            Glide.with(requireActivity).load(enquiryData[position].user_rel.profile_pic)
                .placeholder(R.drawable.user).into(holder.binding.userImg)

            holder.binding.imageView3.loadImageToCoil(enquiryData[position].property_priority_image.document)

            /* property_to value
            * 0 -> RENT
            * 1 -> BUY */
            if (enquiryData[position].property_details.property_to == 0) {
                holder.binding.tvPropertyType.text =
                    requireActivity.getString(R.string.property_for_rent)
                holder.binding.tvPropertyMainPrice.text =
                    requireActivity.getString(R.string.sar) + enquiryData[position].property_details.rent
            } else {
                holder.binding.tvPropertyType.text =
                    requireActivity.getString(R.string.property_for_sale)
                holder.binding.tvPropertyMainPrice.text =
                    requireActivity.getString(R.string.sar) + enquiryData[position].property_details.selling_price
            }

            holder.binding.tvPropertyDescription.text = enquiryData[position].feature_details
            when (enquiryData[position].enquiry_status) {
                0 -> holder.binding.textView2.text = requireActivity.getString(R.string.pending)
                1 -> holder.binding.textView2.text = requireActivity.getString(R.string.interested)
                2 -> holder.binding.textView2.text = requireActivity.getString(R.string.completed)
                3 -> holder.binding.textView2.text =
                    requireActivity.getString(R.string.notInterested)
            }

            holder.binding.tvRead.setOnClickListener {
                try {
                    selectedItem.invoke(enquiryData[position].id)
                } catch (ex: IndexOutOfBoundsException) {
                    ex.printStackTrace()
                }
            }
            holder.binding.ivTwoWayChat.setOnClickListener {
                enquiryData[position].user_rel?.let {
                    val intent = Intent(requireActivity, ChatActivity::class.java)
                    intent.putExtra("user_id", it.id.toString())
                    intent.putExtra("user_name", it.name)
                    intent.putExtra("user_image", it.profile_pic)
                    requireActivity.startActivity(intent)
                }
            }

            holder.itemView.setOnClickListener {
                if(CommonUtils.isOpenRecently()) return@setOnClickListener
                try {
                    selectedItem.invoke(enquiryData[position].id)
                } catch (ex: IndexOutOfBoundsException) {
                    ex.printStackTrace()
                }
            }
        }

    }

}