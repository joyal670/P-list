package com.iroid.patrickstore.ui.my_account.my_orders.order_details


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.FragmentOrderDetailsBinding
import com.iroid.patrickstore.ui.interfaces.FragmentTransInterface
import com.iroid.patrickstore.ui.my_account.MyAccountActivity
import com.iroid.patrickstore.ui.my_account.my_orders.order_details.adapter.OrdrerDetailAdapter
import com.iroid.patrickstore.utils.SOMETHING_WENT_WRONG
import com.iroid.patrickstore.utils.Status
import com.iroid.patrickstore.utils.Toaster
import com.iroid.patrickstore.utils.isConnected
import kotlinx.android.synthetic.main.fragment_order_details.*


class OrderDetailsFragment : BaseFragment<OrderDetailViewModal,FragmentOrderDetailsBinding>() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var orderId:String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        fragmentTransInterface = activity as MyAccountActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.order_details))

    }

    override fun initViews() {
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            orderId = bundle.getString("bundleKey").toString()
            viewModel.getSingleOrder(orderId)

        }

    }

    override fun setUpObserver() {
        viewModel.myOrderLiveData.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    dismissProgress()
                    setUpOrderProduct(it.data!!.data.cartItems)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
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

                    }
                }
            }
        })

    }



    override fun setOnClick() {

    }

    private fun setUpOrderProduct(cartItems: List<com.iroid.patrickstore.model.order_detail.CartItem>) {
//        rvOrderDetails.layoutManager = LinearLayoutManager(context)
//        rvOrderDetails.adapter = OrdrerDetailAdapter(requireContext(), cartItems) {
//
//        }

    }

    override fun getViewBinding(): FragmentOrderDetailsBinding {
        return FragmentOrderDetailsBinding.inflate(layoutInflater)
    }

}
