package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.subscriptionhistorydetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.meal_plan.ExtrasModified
import com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details.ExtraModify
import kotlinx.android.synthetic.main.item_extras_modify.view.*


class ExtrasModificationAdapter(
    private val extras: ArrayList<ExtrasModified>,
    private val function: (String,String,String,String) -> Unit,
    private val notify:()->Unit,
    private val remove:(String)->Unit
) : RecyclerView.Adapter<ExtrasModificationAdapter.ViewHold>() {

    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_extras_modify, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return extras.size
    }

    override fun onBindViewHolder(holder: ViewHold, index: Int) {
        holder.itemView.tvSnacks.text = extras[index].name
        holder.itemView.rvSnacks.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL!!, false)

        holder.itemView.rvSnacks.adapter =
            ExtrasModificationSubAdapter(extras[index].extras_modify as ArrayList<ExtraModify>,{s1,s2,s3,s4->
                function.invoke(s1,s2,s3,s4)
            },{
                notify.invoke()
            },{
                remove.invoke(it)
            })

    }
}
