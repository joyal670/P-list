package com.iroid.patrickstore.ui.productdetail.fragment


import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.coupon.ItemCoupon
import com.iroid.patrickstore.model.product.Product
import com.iroid.patrickstore.ui.coupon.adapter.CouponAdapter
import com.iroid.patrickstore.ui.home.adapter.ProductAdapter
import com.iroid.patrickstore.ui.home.adapter.ProductAdpater
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.MarginGridDecoration
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.fragment_product_detail.view.*
import kotlinx.android.synthetic.main.fragment_shop_reviews.*

class ProductDetailFragment : Fragment() {



    companion object{
        fun newInstance(description: String) = ProductDetailFragment().apply {
            arguments = bundleOf()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.fragment_product_detail, container, false)
//        view.tvDetails.text= Html.fromHtml(getString(R.string.nice_html))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(Constants.REQUEST_KEY_SIMILAR) { requestKey, bundle ->
            val productList: List<Product> =
                bundle.getParcelableArrayList<Product>(Constants.BUNDLE_KEY_SIMILAR)!!
//            rvRelatedProduct!!.addItemDecoration(
//                MarginGridDecoration(
//                    resources.getDimension(R.dimen.margin_small).toInt()
//                )
//            )
            Log.e("123456", productList.size.toString())
            view.rvRelatedProduct!!.layoutManager =
                GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            view.rvRelatedProduct!!.adapter =ProductAdapter(requireContext(), productList) { id, qty ->
//                functionAddToCart(
//                    id,
//                    qty
//                )
            }

        }
        setFragmentResultListener(Constants.REQUEST_KEY_DES){
                requestKey, bundle ->

            tvDetails.text=bundle.getString(Constants.BUNDLE_KEY_DES)

        }
    }

}
