package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.academic.time_table

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleParentTimeTableListItemBinding
import com.iroid.jeetmeet.modal.parent.time_table.ParentTimeTable
import java.util.*


class ParentTimeTableAdapter(private var list: ArrayList<ParentTimeTable>) :
    RecyclerView.Adapter<ParentTimeTableAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleParentTimeTableListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleParentTimeTableListItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvPeriod.text = list[position].slot.toString()
        holder.binding.tvSubject.text = list[position].subjectname.name
        holder.binding.tvTeacherName.text =
            list[position].teachername.first_name + " " + list[position].teachername.last_name
        holder.binding.tvRoomNo.text = list[position].roomname.name
    }
}
