package com.property.propertyuser.ui.main.maintenance.status_details.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.property.propertyuser.R
import com.property.propertyuser.ui.main.payment.PaymentActivity
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.VectorDrawableUtils
import com.property.propertyuser.utils.snack
import kotlinx.android.synthetic.main.list_status_timeline_detailed_item.view.*

class StatusDetailedTimeLineAdapter(
    private val context: Context,
    private var status: Int,
    private var idRequested: Int,
    private var estimateFile: String
)
    : RecyclerView.Adapter<StatusDetailedTimeLineAdapter.ViewHolder>() {

    private val timelineSize=4
    private var ifCalledOnce=true
    private var statusCodes= listOf<String>(context.getString(R.string.tvStatus_send),
        context.getString(R.string.tvStatus_inspection_completed),
            context.getString(R.string.tvStatus_payment_done),
                context.getString(R.string.tvStatus_completed)
    )
    private var statusCodesDescription= listOf<String>(context.getString(R.string.tvStatus_send_des),
        context.getString(R.string.tvStatus_inspection_completed_des),
        context.getString(R.string.tvStatus_payment_done_des),
        context.getString(R.string.tvStatus_completed_des)
    )
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_status_timeline_detailed_item, parent, false)
        return ViewHolder(v,viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun getItemCount(): Int {
        return timelineSize
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvStatusDetailed.text= statusCodes[position]
        holder.itemView.tvStatusDetailedDescription.text=statusCodesDescription[position]
        when (status) {
            0 -> {
                when (position) {
                    3 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.lightGray))
                        holder.itemView.btnPay.visibility=View.GONE
                        holder.itemView.tvDetails.visibility=View.GONE
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                    }
                    2 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.lightGray))
                        holder.itemView.btnPay.visibility=View.VISIBLE
                        holder.itemView.tvDetails.visibility=View.VISIBLE
                        holder.itemView.btnPay.isEnabled=false
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                    }
                    1 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.lightGray))
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                    }
                    0 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_active,
                            ContextCompat.getColor(context,R.color.green_light_1))
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                        holder.itemView.btnPay.visibility=View.GONE
                        holder.itemView.tvDetails.visibility=View.GONE
                    }
                }
            }
            1 -> {
                when (position) {
                    3 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.lightGray))
                        holder.itemView.btnPay.visibility=View.GONE
                        holder.itemView.tvDetails.visibility=View.GONE
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                    }
                    2 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.lightGray))
                        holder.itemView.btnPay.visibility=View.VISIBLE
                        holder.itemView.tvDetails.visibility=View.VISIBLE
                        holder.itemView.btnPay.isEnabled=true
                        holder.itemView.tvDetails.isEnabled=true
                        holder.itemView.tvDetails.setTextColor(Color.parseColor("#000000"))
                        holder.itemView.btnPay.backgroundTintList=ContextCompat.getColorStateList(context,R.color.green_light_1)
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                    }
                    1 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_active,
                            ContextCompat.getColor(context,R.color.green_light_1))
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                    }
                    0 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.green_light_1))
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                        holder.itemView.btnPay.visibility=View.GONE
                        holder.itemView.tvDetails.visibility=View.GONE
                    }
                }
            }
            2 -> {
                when (position) {
                    3 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.lightGray))
                        holder.itemView.btnPay.visibility=View.GONE
                        holder.itemView.tvDetails.visibility=View.GONE
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                    }
                    2 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_active,
                            ContextCompat.getColor(context,R.color.green_light_1))
                        holder.itemView.btnPay.visibility=View.VISIBLE
                        holder.itemView.tvDetails.visibility=View.VISIBLE
                        holder.itemView.btnPay.isEnabled=false
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                    }
                    1 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.green_light_1))
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                    }
                    0 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.green_light_1))
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                        holder.itemView.btnPay.visibility=View.GONE
                        holder.itemView.tvDetails.visibility=View.GONE
                    }
                }
            }
            3 -> {
                when (position) {
                    3 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_active,
                            ContextCompat.getColor(context,R.color.green_light_1))
                        holder.itemView.btnPay.visibility=View.GONE
                        holder.itemView.tvDetails.visibility=View.GONE
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                    }
                    2 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.green_light_1))
                        holder.itemView.btnPay.visibility=View.VISIBLE
                        holder.itemView.tvDetails.visibility=View.VISIBLE
                        holder.itemView.btnPay.isEnabled=false
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                    }
                    1 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.green_light_1))
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                    }
                    0 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.green_light_1))
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.green_light_1),holder.itemViewType)
                        holder.itemView.btnPay.visibility=View.GONE
                        holder.itemView.tvDetails.visibility=View.GONE
                    }
                }
            }
            4 -> {
                when (position) {
                    3 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.lightGray))
                        holder.itemView.btnPay.visibility=View.GONE
                        holder.itemView.tvDetails.visibility=View.GONE
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                    }
                    2 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.lightGray))
                        holder.itemView.btnPay.visibility=View.VISIBLE
                        holder.itemView.tvDetails.visibility=View.VISIBLE
                        holder.itemView.btnPay.isEnabled=false
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                    }
                    1 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.lightGray))
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                    }
                    0 -> {
                        holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive,
                            ContextCompat.getColor(context,R.color.lightGray))
                        holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                        holder.itemView.btnPay.visibility=View.GONE
                        holder.itemView.tvDetails.visibility=View.GONE
                    }
                }
            }
            else->{
                holder.itemView.timeline.visibility=View.GONE
                holder.itemView.timelineCard.visibility=View.GONE
                if(ifCalledOnce){
                    ifCalledOnce=false
                    Toaster.showToast(context!!,context!!.getString(R.string.wrong_status),Toaster.State.ERROR,
                        Toast.LENGTH_LONG)
                }
            }
        }

        holder.itemView.btnPay.setOnClickListener {
            if(status==1){
                val intent=Intent(context,PaymentActivity::class.java)
                intent.putExtra("request_id",idRequested.toString())
                intent.putExtra("passed_type","service_payment")
            Log.e("requestedId",idRequested.toString())
                context.startActivity(intent)
            }
        }

        holder.itemView.tvDetails.setOnClickListener {
            if (status >= 1) {
                if (estimateFile != "") {
                    val builder = CustomTabsIntent.Builder()
                    val colorInt: Int = Color.parseColor("#00822B")
                    builder.setToolbarColor(colorInt)
                    val customTabsIntent = builder.build()
                    customTabsIntent.launchUrl(context, Uri.parse(estimateFile))
                } else {
                    Toaster.showToast(
                        context,
                        context.getString(R.string.fileNotFound),
                        Toaster.State.WARNING,
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }
    }
    inner class ViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.timeline.initLine(viewType)
        }
    }
}