package com.proteinium.proteiniumdietapp.ui.main.home.home.add_deliveryaddress.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.blocks.Block
import kotlinx.android.synthetic.main.recycerview_address_layout.view.*


class RecyerviewSelectBlock (private var blockList: ArrayList<Block>,
                             private val funSelectedBlock: (Int) -> Unit) : RecyclerView.Adapter<RecyerviewSelectBlock.ViewHold>()
{

    private var selectedPosition : Int = 0
    private var context: Context? = null
    private var row_index = -1

    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycerview_address_layout, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int
    {
        return blockList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        holder.itemView.radioDialog.isChecked = selectedPosition == position

        holder.itemView.radioDialog.setOnClickListener {
            if(holder.itemView.radioDialog.isChecked)
            {
                row_index = position
                notifyDataSetChanged()
                funSelectedBlock.invoke(position)
            }
        }
        holder.itemView.radioDialog.isChecked = row_index==position

        holder.itemView.tvDialogAddress.text = blockList[position].name


    }
}