package com.iroid.patrickstore.ui.shop_details.products


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.FragmentShopProductBinding
import com.iroid.patrickstore.model.product.Product
import com.iroid.patrickstore.model.seller.SellingCategory
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.ui.cart.car_list.CartActivity
import com.iroid.patrickstore.ui.home.adapter.ProductAdapter
import com.iroid.patrickstore.ui.shop_details.ShopDetailsViewModal
import com.iroid.patrickstore.utils.*
import com.iroid.patrickstore.utils.Constants.ARGUMENT_CATEGORY
import kotlinx.android.synthetic.main.fragment_shop_product.*


class ShopProductFragment : BaseFragment<ShopDetailsViewModal,FragmentShopProductBinding>() {
    private var categoryList:ArrayList<String> =ArrayList<String>()
    private var categoryIdList:ArrayList<String> =ArrayList<String>()
    companion object{
        fun newInstance(sellingCategory: List<SellingCategory>) = ShopProductFragment().apply {
            arguments = bundleOf()
            arguments!!.putParcelableArrayList(ARGUMENT_CATEGORY,sellingCategory as ArrayList<out Parcelable>)
        }
    }




    override fun setUpObserver() {
        viewModel.allCategoriesLiveData.observe(requireActivity(), Observer { allCategoryResponse ->
            when (allCategoryResponse.status) {
                Status.SUCCESS -> {
                    categoryIdList.clear()
                    categoryList.clear()
                    allCategoryResponse.data!!.data.forEach {
                        requireArguments().getParcelableArrayList<SellingCategory>(Constants.ARGUMENT_CATEGORY)!!
                            .forEach { it2->
                                Log.e("#565656","${it._id}")
                                if(it2._id==it._id){
                                    categoryList.add(it.name)
                                    categoryIdList.add(it._id)
                                    it.children.forEach {it2->
                                        categoryList.add(it2.name)
                                        categoryIdList.add(it2._id)
                                    }
                                }


                        }


                    }
                    if(categoryList.isEmpty()){
                        allCategoryResponse.data!!.data.forEach {
                            requireArguments().getParcelableArrayList<SellingCategory>(Constants.ARGUMENT_CATEGORY)!!
                                .forEach { it2->
                                    Log.e("#565656","${it._id}")
                                        it.children.forEach {it3->
                                            if(it3._id==it2._id){
                                                categoryList.add(it3.name)
                                                categoryIdList.add(it3._id)
                                                categoryList.add(it.name)
                                                categoryIdList.add(it._id)
                                            }

                                        }



                                }


                        }
                    }
                    if(categoryList.isNotEmpty()){
                        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, categoryList)
                        adapter.notifyDataSetChanged()
                        edit_query.setAdapter(adapter)

                        edit_query.setOnItemClickListener(OnItemClickListener { parent, arg1, pos, id ->
                            if(categoryIdList.isNotEmpty()){
                                viewModel.getCategoryProduct(categoryIdList[pos],false,  AppPreferences.sellerId!!)
                            }

                        })
                        if(categoryList.size!=0){
                            edit_query.setText(categoryList[0])
                            viewModel.getCategoryProduct(categoryIdList[0],false,  AppPreferences.sellerId!!)
                        }
                    }

                }
                Status.LOADING -> {

                }
                Status.NO_INTERNET -> {


                }
                Status.ERROR -> {

                }
            }
        })
        viewModel.addToCartLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
//                    dismissProgress()
                    showNavigateToCart()
                }
                Status.LOADING -> {
//                    showProgress()
                }
                Status.ERROR -> {
//                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
//                    dismissProgress()
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
        }
        viewModel.categoryProductLiveData.observe(requireActivity(), Observer {
            when(it.status){
                Status.SUCCESS -> {

                    frameLoader.visibility=View.GONE
                    if(it.data?.data?.items!!.isNotEmpty()){
                        frameData.visibility=View.VISIBLE
                        noData.visibility=View.GONE
                        setProductAdapter(it.data?.data?.items)
                    }else{
                        frameData.visibility=View.GONE
                        noData.visibility=View.VISIBLE
                    }

                }
                Status.LOADING -> {
                    frameData.visibility=View.GONE
                    noData.visibility=View.GONE
                    frameLoader.visibility=View.VISIBLE
                }
                Status.ERROR -> {
                    frameLoader.visibility=View.GONE
                }
                Status.NO_INTERNET -> {
                    frameLoader.visibility=View.GONE
                    if (requireActivity().isConnected) {
                        Toaster.showToast(
                            requireContext(),
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {

                    }
                }
            }
        })

    }


    private fun setProductAdapter(productList: List<Product>?) {
        val controller: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation)
        binding.rvShopProducts.layoutAnimation = controller
        binding.rvShopProducts.layoutManager =
            GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.rvShopProducts.adapter = ProductAdapter(requireContext(), productList!!) { id, qty ->
            functionAddToCart(
                id,
                qty
            )
        }
    }

    private fun functionAddToCart(id: String, qty: String) {
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

    override fun setOnClick() {
    }

    override fun getViewBinding(): FragmentShopProductBinding {
        return FragmentShopProductBinding.inflate(layoutInflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        setUpObserver()
        viewModel.getAllCategories()
    }

    override fun initViews() {

    }


}
