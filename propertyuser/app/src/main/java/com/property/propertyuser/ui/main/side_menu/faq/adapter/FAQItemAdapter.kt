package com.property.propertyuser.ui.main.side_menu.faq.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.faq.Faq
import com.property.propertyuser.ui.main.notification.adapter.NotificationAdapter
import kotlinx.android.synthetic.main.list_faq_item.view.*

class FAQItemAdapter(
    private val context: Context,
    private var faqitemsList: List<Faq>
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object
    {
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1
    }
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        return when (viewType)
        {
            VIEW_TYPE_DATA ->
            {//inflates row layout
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_faq_item,parent,false)
                ViewHolder(view)
            }
            VIEW_TYPE_PROGRESS ->
            {//inflates progressbar layout
                val view = LayoutInflater.from(parent.context).inflate(R.layout.loader,parent,false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount(): Int {
        return faqitemsList.size
    }
    override fun getItemViewType(position: Int): Int
    {
        var viewtype = faqitemsList[position].id
        return when(viewtype)
        {
            -1  -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        if (holder is ViewHolder){
            holder.itemView.tvFaqItemTitle.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(faqitemsList[position].question, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(faqitemsList[position].question)
                //HtmlCompat.fromHtml(faqitemsList[position].question, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
            holder.itemView.setOnClickListener {
                if(holder.itemView.tvFaqItemDescription.visibility==0){
                    holder.itemView.tvFaqItemDescription.visibility=View.GONE
                    holder.itemView.ivPlus.visibility=View.VISIBLE
                    holder.itemView.ivClose.visibility=View.GONE
                }else{
                    holder.itemView.ivPlus.visibility=View.GONE
                    holder.itemView.ivClose.visibility=View.VISIBLE
                    holder.itemView.tvFaqItemDescription.visibility=View.VISIBLE
                    holder.itemView.tvFaqItemDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(faqitemsList[position].answer, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(faqitemsList[position].answer)
                    }
                }
                Log.e("visible value",holder.itemView.tvFaqItemDescription.visibility.toString())
            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }
    class ProgressViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }
}