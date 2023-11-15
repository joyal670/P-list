package com.iroid.jeetmeet.ui.main.parent_panel.chat

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.databinding.RecycleStudentChatTeachersListItemBinding
import com.iroid.jeetmeet.modal.parent.chat_teachers_list.ParentChatTeachersListData


class ParentTeachersListAdapter(
    private var chatList: ArrayList<ParentChatTeachersListData>,
    private var data: (Int) -> Unit
) : RecyclerView.Adapter<ParentTeachersListAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleStudentChatTeachersListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentChatTeachersListItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        Glide.with(context!!).load(chatList[position].pro_pic).into(holder.binding.circleImageView2)
        holder.binding.materialTextView.text = chatList[position].teacher

        holder.itemView.setOnClickListener {
            data.invoke(chatList[position].id)
        }
    }
}
