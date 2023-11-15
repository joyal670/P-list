package com.iroid.patrickstore.ui.productdetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.single_product.Reviews
import kotlinx.android.synthetic.main.shop_review_item.view.*

class ProductReviewAdapter(private val requireContext: Context,
                           private val reviewList: List<Reviews>) : RecyclerView.Adapter<ProductReviewAdapter.ViewHold>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shop_review_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.setupViews(reviewList[position])
    }

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setupViews(reviews: Reviews) {
            itemView.ratingReview.rating=reviews.rating
            itemView.tvReviewDescription.text=reviews.review
            itemView.tvReviewAuthor.text=reviews.reviewTitle
        }
    }
}
