package com.iroid.patrickstore.ui.home.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.seller.SingleSeller
import com.iroid.patrickstore.ui.home.adapter.ShopAdapter
import com.iroid.patrickstore.utils.MarginGridDecoration
import com.iroid.patrickstore.utils.MarginItemDecoration
import java.util.ArrayList


class ShopFragment : Fragment() {

    private var rvShop: RecyclerView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shop, container, false)
        rvShop = root.findViewById(R.id.rvShop)
        setProductAdapter()
        return root
    }

    private fun setProductAdapter() {
        try {
            val shopList: List<SingleSeller> = requireArguments().getParcelableArrayList(
                ARG_PRODUCT_LIST
            )!!
            rvShop!!.addItemDecoration(
                MarginGridDecoration(
                    resources.getDimension(R.dimen.margin_small).toInt()
                )
            )
            rvShop!!.addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.margin_small).toInt()))
            rvShop!!.layoutManager =
                GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            rvShop!!.adapter = ShopAdapter(requireContext(),shopList)
        }catch (ex:Exception){

        }


    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_PRODUCT_LIST = "arg_product_list"
        fun newInstance(sectionNumber: String, shopX: List<SingleSeller>): ShopFragment {
            return ShopFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SECTION_NUMBER, sectionNumber)
                    putParcelableArrayList(ARG_PRODUCT_LIST, shopX as ArrayList<out Parcelable>)
                }
            }
        }
    }
}
