package com.iroid.jeetmeet.ui.main.student_panel.chat.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleParentMessageParentItemBinding
import com.iroid.jeetmeet.databinding.RecycleParentMessageTeacherItemBinding
import com.iroid.jeetmeet.ui.main.student_panel.chat.model.TeacherChatModelClass


class TeacherStudentChatAdapter(private var chatMessages: ArrayList<TeacherChatModelClass>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "StudentChatAdapter"
        val TYPE_PARENT: Int = 0
        val TYPE_TEACHER: Int = 1
    }


    inner class StudentViewHolder(var binding: RecycleParentMessageParentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setModel(chatModel: TeacherChatModelClass, position: Int) {
            binding.tvStudentMsg.text = chatModel.message

        }

    }

    inner class ParentViewHolder(var binding: RecycleParentMessageTeacherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setModel(chatModel: TeacherChatModelClass, position: Int) {
            binding.tvTeacherMsg.text = chatModel.message
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_PARENT) {
            return StudentViewHolder(
                RecycleParentMessageParentItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ParentViewHolder(
                RecycleParentMessageTeacherItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }

    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (chatMessages[position].from_teacher!!.isBlank()) {
            (holder as StudentViewHolder).setModel(chatMessages[position], position)
        } else {
            (holder as ParentViewHolder).setModel(chatMessages[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {

        if (chatMessages[position].from_teacher!!.isBlank()) {
            return TYPE_PARENT
        } else {
            return TYPE_TEACHER
        }

    }


    fun setBackgroundDrawable(background: Drawable) {
        setBackgroundDrawable(background)
    }
}

