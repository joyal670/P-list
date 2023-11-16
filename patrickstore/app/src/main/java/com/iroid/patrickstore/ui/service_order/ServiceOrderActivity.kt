package com.iroid.patrickstore.ui.service_order

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityAllProductBinding
import com.iroid.patrickstore.databinding.ActivityServiceOrderBinding
import com.iroid.patrickstore.model.service.service_order.ItemServiceOrder
import com.iroid.patrickstore.ui.service_order.adapter.ServiceOrderAdapter
import com.iroid.patrickstore.utils.*


class ServiceOrderActivity : BaseActivity<ServiceOrderViewModal,ActivityServiceOrderBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_all_product
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initViews() {
        setUpObserver()
        supportActionBar?.title =getString(R.string.service_order)
        viewModel.getServiceOrder()
    }
    private fun setUpObserver() {
        viewModel.serviceOrderLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    setServiceOrderAdapter(it.data!!.data.items)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this,
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        })
    }
    private fun setServiceOrderAdapter(items: List<ItemServiceOrder>) {
        if(items.isNotEmpty()){
            binding.noData.visibility = View.GONE
            binding.rvViewAll.visibility = View.VISIBLE
            val controller: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation)
            binding.rvViewAll.layoutAnimation = controller
            binding.rvViewAll.layoutManager =

                LinearLayoutManager(this,GridLayoutManager.VERTICAL, false)
            binding.rvViewAll.adapter = ServiceOrderAdapter(this,items) {
                val intent=Intent(this,ServiceOrderDetailActivity::class.java)
                intent.putExtra(Constants.INTENT_TYPE,it.toString())
                startActivity(intent)
            }
        }else{
            binding.noData.visibility = View.VISIBLE
            binding.rvViewAll.visibility = View.GONE
        }


    }


    override fun getViewBinding(): ActivityServiceOrderBinding {
        return ActivityServiceOrderBinding.inflate(layoutInflater)
    }



}
