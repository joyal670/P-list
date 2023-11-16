package com.iroid.patrickstore.ui.my_account.my_reviews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.delivered_order.Product
import com.iroid.patrickstore.utils.CommonUtils
import kotlinx.android.synthetic.main.recycle_unreviewed_item.view.*
import java.util.ArrayList

class UnReviewedListAdapter(
    private val context: Context,
    private val deliveredList: ArrayList<Product>,
    private val function: (String) -> Unit
) : RecyclerView.Adapter<UnReviewedListAdapter.ViewHold>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_unreviewed_item, parent, false)
        return ViewHold(view)
    }
    override fun getItemCount(): Int {
        return deliveredList!!.size
    }


    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.itemView.textView35.text= deliveredList!![position].name
        CommonUtils.setImageBase(context,deliveredList[position].imgUrl[0].publicUrl,holder.itemView.ivItemImage)
        holder.setViews(function,deliveredList!![position])
    }

    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun setViews(function: (String) -> Unit, product: Product) {
            itemView.btnWriteReview.setOnClickListener {
                function.invoke(product.id)
            }
        }
    }


}
