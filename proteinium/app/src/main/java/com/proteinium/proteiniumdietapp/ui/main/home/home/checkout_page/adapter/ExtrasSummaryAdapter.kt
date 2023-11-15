package com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page.adapter.modal.ExtrasModal
import com.proteinium.proteiniumdietapp.utils.toPriceRound
import kotlinx.android.synthetic.main.item_extras.view.*


class ExtrasSummaryAdapter(
    private var extras: List<ExtrasModal>
) : RecyclerView.Adapter<ExtrasSummaryAdapter.ViewHold>() {

    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_extras, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return extras.size
    }

    override fun onBindViewHolder(holder: ViewHold, index: Int) {
        holder.itemView.tvName.text=extras[index].name
        holder.itemView.tvPrice.text="${context!!.getString(R.string.kwd)+ extras[index].price.toDouble().toPriceRound()}"
        if(index==extras.size-1){
            holder.itemView.viewLine.visibility=View.GONE

        }
    }
}