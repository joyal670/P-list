package com.iroid.patrickstore.ui.shop_details.reviews


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.R
import com.iroid.patrickstore.ui.shop_details.adapter.ShopReviewAdapter
import kotlinx.android.synthetic.main.fragment_shop_reviews.*


class ShopReviewsFragment : Fragment() {

    companion object{
        fun newInstance() = ShopReviewsFragment().apply {
            arguments = bundleOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvShopReviews.layoutManager = LinearLayoutManager(context)
        rvShopReviews.adapter =
            ShopReviewAdapter()
    }


}
