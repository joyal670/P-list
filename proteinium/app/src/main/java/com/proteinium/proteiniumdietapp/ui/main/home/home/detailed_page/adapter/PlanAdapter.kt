package com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.meal_plan.SubscriptionPlan
import kotlinx.android.synthetic.main.recycerview_selectplan_layout.view.*


class PlanAdapter(
    private val subscriptionPlanList: List<SubscriptionPlan>,
    private val functionPlan: (Int) -> Unit
) : RecyclerView.Adapter<PlanAdapter.ViewHold>()
{

    private var selectedPosition : Int = 0

    private var context: Context? = null



    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycerview_selectplan_layout, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int
    {
        return subscriptionPlanList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        if(subscriptionPlanList[position].duration=="1"){
            holder.itemView.planBtn.text = subscriptionPlanList[position].duration+" "+context!!.getString(R.string.week)
        }else{
            holder.itemView.planBtn.text = subscriptionPlanList[position].duration+" "+context!!.getString(R.string.weeeks)
        }

        if(subscriptionPlanList[position].isChecked)
        {
            holder.itemView.planBtn.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.color_main)
        }
        else
        {
            holder.itemView.planBtn.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.grey2)
        }

        holder.itemView.planBtn.setOnClickListener {

            if(!subscriptionPlanList[position].isChecked)
            {

                holder.itemView.planBtn.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.color_main)

                subscriptionPlanList.forEach {
                    it.isChecked = false
                }
                subscriptionPlanList[position].isChecked = true
                functionPlan.invoke(position)
                selectedPosition = position
                notifyDataSetChanged()
            }

        }
    }
}