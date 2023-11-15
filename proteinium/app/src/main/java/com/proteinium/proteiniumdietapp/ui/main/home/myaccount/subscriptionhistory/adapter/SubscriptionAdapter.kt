package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.subscriptions_history.SubscriptionsHistory
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.subscriptionhistorydetails.SubscriptionDetailedActivity
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import kotlinx.android.synthetic.main.recycerview_subscriptionhistory.view.*

class SubscriptionAdapter(
    private var subscriptionHistoryList: List<SubscriptionsHistory>,
    private val isActive: Boolean,
    private val mContext: Context
) :
    RecyclerView.Adapter<SubscriptionAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycerview_subscriptionhistory, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return subscriptionHistoryList.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.itemView.ivSubscriptionHistory.load(subscriptionHistoryList[position].image) {
            placeholder(R.drawable.ic_profile)
            error(R.drawable.ic_image_not)
        }
        if (AppPreferences.chooseLanguage == "ar") {
            holder.itemView.tvSubscriptionHistoryId.gravity = Gravity.END
        }
        subscriptionHistoryList[position].plan_status.also {
            holder.itemView.tvStatus.text = it
            if (AppPreferences.chooseLanguage == "ar" && subscriptionHistoryList[position].plan_status == "pending") {
                holder.itemView.tvStatus.text = context!!.getString(R.string.pending)
            }
        }


        when (subscriptionHistoryList[position].plan_status) {
//            context!!.getString(R.string.not_yet_started) -> {
//                holder.itemView.tvStatus.setTextColor(context!!.getColor(R.color.orange1))
//            }
//            context!!.getString(R.string.canceled) -> {
//                holder.itemView.tvStatus.setTextColor(context!!.getColor(R.color.green1))
//            }
//            context!!.getString(R.string.completed) -> {
//                holder.itemView.tvStatus.setTextColor(context!!.getColor(R.color.green))
//            }


        }
        holder.itemView.tvStartDate.text =
            context!!.getString(R.string.start_date) + " " + subscriptionHistoryList[position].plan_start_date
        holder.itemView.tvSubscriptionHistoryId.text =
            "#" + subscriptionHistoryList[position].id.toString()
        val categoryAndPlan =
            subscriptionHistoryList[position].meal_category_name + "-" + subscriptionHistoryList[position].meal_plan_name
        holder.itemView.tvSubscriptionHistoryCategoryPlan.text = categoryAndPlan
        holder.itemView.rvsubscriptionHistoryViewDetailsBtn.setOnClickListener {
            val intent = Intent(context, SubscriptionDetailedActivity::class.java)
            Log.e("meal_plan_sub_id", subscriptionHistoryList[position].id.toString())
            intent.putExtra("meal_plan_subscription_id", subscriptionHistoryList[position].id)
            intent.putExtra("is_renewal", isActive)
            context?.startActivity(intent)

        }
    }
}
