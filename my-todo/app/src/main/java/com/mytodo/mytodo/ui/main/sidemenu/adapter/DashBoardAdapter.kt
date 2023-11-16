package com.mytodo.mytodo.ui.main.sidemenu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.mytodo.mytodo.databinding.RecycleDashboardListItemBinding
import com.mytodo.mytodo.dialog.TaskDetailsDialog
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import com.mytodo.mytodo.utils.Toaster
import io.realm.RealmResults
import java.util.*


class DashBoardAdapter(private var taskList: RealmResults<TaskModel>?) : RecyclerView.Adapter<DashBoardAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleDashboardListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            RecycleDashboardListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return taskList!!.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        val lp: ViewGroup.LayoutParams = holder.binding.imageCard.layoutParams

        if (lp is FlexboxLayoutManager.LayoutParams) {
            val random = Random()
            var `val`: Int = random.nextInt(5)
            if (`val` == 0) {
                `val` = 1
            }
            lp.flexGrow = `val`.toFloat()
            lp.flexShrink = 1f
        }
        holder.binding.tvTaskName.text = taskList?.get(position)!!.title
        holder.binding.rvDashboardDes.text = taskList!![position]!!.description

        holder.itemView.setOnClickListener {
            val activity = context as FragmentActivity
            val fm: FragmentManager = activity.supportFragmentManager
            val dialog = TaskDetailsDialog(taskList!![position]!!.title, taskList!![position]!!.startDate, taskList!![position]!!.endDate, taskList!![position]!!.description)
            dialog.show(fm, "TAG")
        }
    }
}
