package com.iroid.patrickstore.ui.home.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.FragmentFoodBinding
import com.iroid.patrickstore.model.product.Product
import com.iroid.patrickstore.ui.cart.car_list.CartActivity
import com.iroid.patrickstore.ui.home.HomeViewModel
import com.iroid.patrickstore.ui.home.adapter.ProductAdapter
import com.iroid.patrickstore.utils.*
import java.util.*

class FoodFragment : BaseFragment<HomeViewModel, FragmentFoodBinding>() {

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_PRODUCT_LIST = "arg_product_list"

        @JvmStatic
        fun newInstance(sectionNumber: String, products: List<Product>): FoodFragment {
            return FoodFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SECTION_NUMBER, sectionNumber)
                    putParcelableArrayList(ARG_PRODUCT_LIST, products as ArrayList<out Parcelable>)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setProductAdapter()
    }
    override fun initViews() {

    }

    override fun setUpObserver() {
        viewModel.addToCartLiveData.observe(requireActivity(), {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    showNavigateToCart()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireActivity().isConnected) {
                        Toaster.showToast(
                            requireContext(),
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            requireContext(),
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        })
    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentFoodBinding {
        return FragmentFoodBinding.inflate(layoutInflater)
    }

    private fun setProductAdapter() {
        val productList: List<Product> = requireArguments().getParcelableArrayList(
            ARG_PRODUCT_LIST
        )!!

        var productListModified:ArrayList<Product> = ArrayList()
        if(productList.isNotEmpty()){
            binding.rvFood.visibility= View.VISIBLE
            binding.noData.visibility= View.GONE
            binding.rvFood.layoutManager =
                GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            binding.rvFood.adapter = ProductAdapter(requireContext(), productList) { id, qty ->
                functionAddToCart(
                    id,
                    qty
                )
            }
        }else{
            binding.rvFood.visibility= View.GONE
            binding.noData.visibility= View.VISIBLE
        }

    }

    private fun functionAddToCart(id: String, qty: String) {
        Log.i(TAG, "functionAddToCart: $id $qty")
        viewModel.addToCart(id, qty)
    }

    private fun showNavigateToCart() {
        val snackBar = view?.let { Snackbar.make(it, "Item added to cart", Snackbar.LENGTH_LONG) }
        snackBar!!.view.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.colorPrimary
            )
        )
        snackBar.setTextColor(Color.WHITE)
        snackBar.setActionTextColor(Color.WHITE)
        snackBar.setAction("GO TO CART") {
            requireContext().startActivity(Intent(requireContext(), CartActivity::class.java))
        }.show()
    }
}
