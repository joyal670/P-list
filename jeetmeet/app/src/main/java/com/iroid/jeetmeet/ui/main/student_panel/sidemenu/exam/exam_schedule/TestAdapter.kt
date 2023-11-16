package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_schedule

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycletestlistitemBinding


class TestAdapter(private var totalques: Int, private var curentqes: Int) : RecyclerView.Adapter<TestAdapter.ViewHold>()
{
    private var context: Context? = null

    class ViewHold(var binding : RecycletestlistitemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = RecycletestlistitemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int
    {
        return totalques
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        holder.binding.testBtn.text =  (position + 1).toString()
        if(curentqes >= position)
        {
            holder.binding.testView.setBackgroundColor(Color.parseColor("#EE2424"))
            holder.binding.testBtn.setBackgroundColor(Color.parseColor("#EE2424"))
        }

        if(position==(itemCount -1)){
            holder.binding.testView.visibility= View.GONE
        }

    }
}
