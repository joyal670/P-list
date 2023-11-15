package com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import kotlinx.android.synthetic.main.recycerview_offdays.view.*


class OffDaysAdapter(var offDays: List<String>) : RecyclerView.Adapter<OffDaysAdapter.ViewHold>() {

    private var context: Context? = null


    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycerview_offdays, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return offDays.size
    }

    override fun onBindViewHolder(holder: ViewHold, index: Int) {
        val  data=StringBuilder()
        data.append( CommonUtils.NonStopDays2(offDays[index], context!!) )
        if(index!=offDays.size-1){
            data.append(",")
        }
        holder.itemView.offDaysBtn.text =data.toString()

    }
}