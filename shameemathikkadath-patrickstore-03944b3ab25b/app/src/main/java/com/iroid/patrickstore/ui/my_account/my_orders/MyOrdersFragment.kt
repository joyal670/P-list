package com.iroid.patrickstore.ui.my_account.my_orders


import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.FragmentMyOrdersBinding
import com.iroid.patrickstore.model.my_orders.Item
import com.iroid.patrickstore.ui.interfaces.FragmentTransInterface
import com.iroid.patrickstore.ui.my_account.MyAccountActivity
import com.iroid.patrickstore.ui.my_account.my_orders.adapter.MyOrderAdapter
import com.iroid.patrickstore.utils.SOMETHING_WENT_WRONG
import com.iroid.patrickstore.utils.Status
import com.iroid.patrickstore.utils.Toaster
import com.iroid.patrickstore.utils.isConnected
import androidx.fragment.app.setFragmentResult
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.ui.my_account.my_orders.order_details.OrderDetailActivity
import com.iroid.patrickstore.ui.my_account.my_orders.order_details.OrderDetailsFragment
import com.iroid.patrickstore.ui.viewall.ViewAllActivity
import kotlinx.android.synthetic.main.activity_search.view.*
import kotlinx.android.synthetic.main.fragment_cash_back.*
import kotlinx.android.synthetic.main.fragment_my_orders.*
import kotlinx.android.synthetic.main.fragment_my_orders.noData
import kotlinx.android.synthetic.main.layout_no_data.view.*


/**
 * A simple [Fragment] subclass.
 */
class MyOrdersFragment : BaseFragment<MyOrderViewModal, FragmentMyOrdersBinding>() {

    private lateinit var fragmentTransInterface: FragmentTransInterface


    override fun initViews() {
        fragmentTransInterface = activity as MyAccountActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.my_orders))
        viewModel.getMyOrder()
    }

    override fun setUpObserver() {
        viewModel.myOrderLiveData.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    dismissProgress()
                    setUpMyOrderAdapter(it.data?.data?.items)
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

    private fun setUpMyOrderAdapter(items: List<Item>?) {
        if(items!!.isNotEmpty()){
            noData.visibility= View.GONE
            rvMyOrders.visibility=View.VISIBLE
            binding.rvMyOrders.layoutManager = LinearLayoutManager(context)
            binding.rvMyOrders.adapter = MyOrderAdapter(requireContext(), items!!) {orderId->
                val result = "result"
                // Use the Kotlin extension in the fragment-ktx artifact
                AppPreferences.isFromOrder=false
                AppPreferences.order_id=orderId
                val intent = Intent(requireActivity(), OrderDetailActivity::class.java)
                requireActivity().startActivity(intent)


            }
        }else{
            noData.visibility= View.VISIBLE
            rvMyOrders.visibility=View.GONE
            noData.noDataLottie.setAnimation(R.raw.order_now_animation)
            noData.noDataLottie.loop(true)
            noData.noDataLottie.playAnimation()
            noData.tvNoData.text="No order found"
        }


    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentMyOrdersBinding {
        return FragmentMyOrdersBinding.inflate(layoutInflater)

    }

}
