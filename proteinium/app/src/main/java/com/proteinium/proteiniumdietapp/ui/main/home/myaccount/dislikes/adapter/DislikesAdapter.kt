package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.dislikes.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.dislike_tags.Tag
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import kotlinx.android.synthetic.main.recycerview_dislikes.view.*


class DislikesAdapter(
    private var tagsList: List<Tag>,
    private val funSelectedTag: () -> Unit,
    private val functionLimit: () -> Unit
) :
    RecyclerView.Adapter<DislikesAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycerview_dislikes, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return tagsList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        var click = true
        holder.itemView.dislikeBtn.text = tagsList[position].name
        if (tagsList[position].disliked) {
            holder.itemView.dislikeBtn.backgroundTintList =
                ContextCompat.getColorStateList(context!!, R.color.colorPrimary)
            holder.itemView.dislikeBtn.strokeColor =
                ContextCompat.getColorStateList(context!!, R.color.colorPrimary)
            holder.itemView.dislikeBtn.setTextColor(Color.parseColor("#FFFFFF"))
            funSelectedTag.invoke()
        } else {
            holder.itemView.dislikeBtn.backgroundTintList =
                ContextCompat.getColorStateList(context!!, R.color.white)
            holder.itemView.dislikeBtn.strokeColor =
                ContextCompat.getColorStateList(context!!, R.color.grey4)
            holder.itemView.dislikeBtn.setTextColor(Color.parseColor("#707070"))
            funSelectedTag.invoke()
        }
        holder.itemView.dislikeBtn.setOnClickListener {
            if(tagsList[position].disliked){
                tagsList[position].disliked = !tagsList[position].disliked
                notifyDataSetChanged()
            }else{
                if (CommonUtils.countNumber(tagsList) < 7) {
                    tagsList[position].disliked = !tagsList[position].disliked
                    notifyDataSetChanged()
                } else {
                    functionLimit.invoke()
                }
            }


        }
    }
}