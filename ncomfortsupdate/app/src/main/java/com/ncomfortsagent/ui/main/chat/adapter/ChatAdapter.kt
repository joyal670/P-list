package com.ncomfortsagent.ui.main.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ncomfortsagent.databinding.RecycleChatAgentListItemBinding
import com.ncomfortsagent.databinding.RecyclerChatUserListItemBinding
import com.ncomfortsagent.ui.main.chat.model.ChatModelClass
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.getCurrentDateNew
import com.ncomfortsagent.utils.loadImagesProfileWithGlideExt


class ChatAdapter(private  val chatData: ArrayList<ChatModelClass>,
                    private var userImage:String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "ChatAdapter"
        val TYPE_USER: Int = 0
        val TYPE_AGENT: Int = 1
    }
    inner class UserViewHolder(var binding: RecyclerChatUserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setModel(chat: ChatModelClass) {
            binding.tvStudentMsg.text=chat.msg
            binding.msgDate.text= getCurrentDateNew(chat.time)
        }
    }

    inner class AdminViewHolder(var binding: RecycleChatAgentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setModel(chat: ChatModelClass) {
            binding.ivImg.loadImagesProfileWithGlideExt(userImage)
            binding.tvTeacherMsg.text=chat.msg
            binding.msgDate.text= getCurrentDateNew(chat.time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_AGENT) {
            return UserViewHolder(
                RecyclerChatUserListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return AdminViewHolder(
                RecycleChatAgentListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }

    }

    override fun getItemCount(): Int {
        return chatData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (chatData[position].type == Constants.TYPE_AGENT) {
            (holder as UserViewHolder).setModel(chatData[position])
        } else {
            (holder as AdminViewHolder).setModel(chatData[position])
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (chatData[position].type == Constants.TYPE_AGENT) {
            TYPE_AGENT
        } else {
            TYPE_USER
        }

    }

}

