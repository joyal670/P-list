package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.students

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.databinding.RecycleParentStudentListItemBinding
import com.iroid.jeetmeet.listeners.AdaptorListener
import com.iroid.jeetmeet.modal.parent.students_list.ParentStudentsListData


class ParentStudentsAdapter(
    val listener: AdaptorListener,
    private var studentsList: ArrayList<ParentStudentsListData>
) : RecyclerView.Adapter<ParentStudentsAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleParentStudentListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            RecycleParentStudentListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return studentsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        Glide.with(context!!).load(studentsList[position].profile_image_url)
            .into(holder.binding.ivProfilePic)
        holder.binding.tvStudentName.text =
            studentsList[position].first_name + " " + studentsList[position].middle_name + " " + studentsList[position].last_name
        holder.binding.tvStudentDob.text = "DOB :" + studentsList[position].dob
        holder.binding.tvStudentGender.text = "Gender - " + studentsList[position].gender
        holder.binding.tvStudentPhone.text = "Phone - " + studentsList[position].phone

        holder.binding.sampleCardView2.setOnClickListener {
            listener.onItemViewClick(studentsList[position].id)
        }

    }

}
