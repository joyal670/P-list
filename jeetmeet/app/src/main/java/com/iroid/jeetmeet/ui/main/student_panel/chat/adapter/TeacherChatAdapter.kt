package com.iroid.jeetmeet.ui.main.student_panel.chat.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.databinding.RecycleStudentAdminChatListItemBinding
import com.iroid.jeetmeet.modal.student.chat_student_teacher.StudentChatTeacherData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class TeacherChatAdapter(
    private var chatList: ArrayList<StudentChatTeacherData>,
    private var data: (String, String, String, Int, Int) -> Unit
) : RecyclerView.Adapter<TeacherChatAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleStudentAdminChatListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentAdminChatListItemBinding.inflate(
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

        holder.itemView.setOnClickListener {
            if (chatList[position].teachers.pic_url == null) {
                data.invoke(
                    chatList[position].teachers.first_name,
                    "no",
                    chatList[position].chat,
                    chatList[position].teacher,
                    chatList[position].student
                )
            } else {
                data.invoke(
                    chatList[position].teachers.first_name,
                    chatList[position].teachers.pic_url,
                    chatList[position].chat,
                    chatList[position].teacher,
                    chatList[position].student
                )
            }
        }


        if (chatList[position].teachers.pic_url == null) {
            Glide.with(context!!).load(R.drawable.ic_profile_user)
                .into(holder.binding.circleImageView2)
        } else {
            Glide.with(context!!).load(chatList[position].teachers.pic_url)
                .into(holder.binding.circleImageView2)
        }

        holder.binding.materialTextView.text =
            chatList[position].teachers.first_name + " " + chatList[position].teachers.last_name
        holder.binding.materialTextView2.text = chatList[position].message

        val outputFormat: DateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate: Date? = inputFormat.parse(chatList[position].updated_at.take(10))
        val outputStartDate: String? = outputFormat.format(startDate)
        holder.binding.materialTextView3.text = outputStartDate
    }
}
