package com.iroid.patrickstore.ui.productdetail.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.product.Product
import com.iroid.patrickstore.model.single_product.Reviews
import com.iroid.patrickstore.ui.productdetail.adapter.ProductReviewAdapter
import com.iroid.patrickstore.utils.Constants
import kotlinx.android.synthetic.main.fragment_product_detail.view.*
import kotlinx.android.synthetic.main.fragment_shop_reviews.*

/**
 * A simple [Fragment] subclass.
 */
class ReviewFragment : Fragment() {

    companion object {
        fun newInstance(similarProducts: List<Product>) = ReviewFragment().apply {
            arguments = bundleOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setFragmentResultListener(Constants.REQUEST_KEY_REVIEWS) { requestKey, bundle ->
            val reviewList: List<Reviews> =
                bundle.getParcelableArrayList<Reviews>(Constants.BUNDLE_KEY_REVIEWS)!!

            if(reviewList.isNotEmpty()){
                mainData.visibility=View.VISIBLE
                tvRating.text=reviewList.size.toString()
                rbReviewRating.rating=reviewList.size.toFloat()
                textView12.text="${reviewList.size} ratings and ${reviewList.size} reviews"
                rvShopReviews.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,true)
                rvShopReviews.adapter=ProductReviewAdapter(requireContext(),reviewList)
            }else{
                noData.visibility=View.VISIBLE
                rvShopReviews.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,true)
                rvShopReviews.adapter=ProductReviewAdapter(requireContext(),reviewList)
            }

//            view.rvRelatedProduct!!.layoutManager =
//                GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
//            view.rvRelatedProduct!!.adapter =R(requireContext(), productList) { id, qty ->
////                functionAddToCart(
////                    id,
////                    qty
////                )
//            }

        }


    }
}
