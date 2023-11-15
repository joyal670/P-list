package com.iroid.patrickstore.ui.my_account.my_reviews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.product_reviews.Item
import com.iroid.patrickstore.utils.CommonUtils
import kotlinx.android.synthetic.main.recycle_reviewed_item.view.*

class ReviewedListAdapter(
    private val reviewList: List<Item>,
    private val functionDelete: (String) -> Unit,
    private val context: Context,
    private val functionEdit: (String,Float,String,String) -> Unit
) : RecyclerView.Adapter<ReviewedListAdapter.ViewHold>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_reviewed_item, parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.setViews(reviewList[position],context,functionEdit,functionDelete)
    }

    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun setViews(
            item: Item,
            context: Context,
            functionEdit: (String, Float, String, String) -> Unit,
            functionDelete: (String) -> Unit
        ) {
            itemView.textView35.text=item.productId.name
            itemView.ratingBar2.rating=item.rating.toFloat()
            CommonUtils.setImageBase(context,item.productId.imgUrl[0].publicUrl,itemView.ivItemImage)
            itemView.textView36.text=item.reviewTitle
            itemView.textView37.text=item.review
            itemView.textView39.text=item.rating.toString()
            itemView.tvEdit.setOnClickListener {
                functionEdit(item.id,item.rating,item.reviewTitle,item.review)
            }
            itemView.tvDelete.setOnClickListener {
                functionDelete.invoke(item.id)
            }
        }
    }
}
