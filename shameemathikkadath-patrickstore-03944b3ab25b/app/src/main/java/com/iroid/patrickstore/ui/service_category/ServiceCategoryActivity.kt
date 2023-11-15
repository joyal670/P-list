package com.iroid.patrickstore.ui.service_category

import android.os.Bundle
import android.view.View
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityAllProductBinding
import com.iroid.patrickstore.ui.service_order.ServiceOrderViewModal


class ServiceCategoryActivity : BaseActivity<ServiceOrderViewModal, ActivityAllProductBinding>() {
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
//        setUpObserver()
        supportActionBar?.title = intent.getStringExtra("category_name")
        binding.noData.visibility = View.VISIBLE
        binding.rvViewAll.visibility = View.GONE
        binding.tvNoData.text="Our team is working hard to adding products soon"
//        viewModel.getServiceOrder()
    }
//    private fun setUpObserver() {
//        viewModel.serviceOrderLiveData.observe(this, Observer {
//            when (it.status) {
//                Status.SUCCESS -> {
//                    dismissProgress()
//                    setServiceOrderAdapter(it.data!!.data.items)
//                }
//                Status.LOADING -> {
//                    showProgress()
//                }
//                Status.ERROR -> {
//                    dismissProgress()
//                    Toaster.showToast(
//                        this,
//                        it.message!!,
//                        Toaster.State.ERROR,
//                        Toast.LENGTH_LONG
//                    )
//                }
//                Status.NO_INTERNET -> {
//                    dismissProgress()
//                    if (this.isConnected) {
//                        Toaster.showToast(
//                            this,
//                            SOMETHING_WENT_WRONG,
//                            Toaster.State.ERROR,
//                            Toast.LENGTH_LONG
//                        )
//                    } else {
//                        Toaster.showToast(
//                            this,
//                            NO_INTERNET_MESSAGE,
//                            Toaster.State.ERROR,
//                            Toast.LENGTH_LONG
//                        )
//                    }
//                }
//            }
//        })
//    }
//    private fun setServiceOrderAdapter(items: List<ItemServiceOrder>) {
//        if(items.isNotEmpty()){
//            binding.noData.visibility = View.GONE
//            binding.rvViewAll.visibility = View.VISIBLE
//            val controller: LayoutAnimationController =
//                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation)
//            binding.rvViewAll.layoutAnimation = controller
//            binding.rvViewAll.layoutManager =
//
//                LinearLayoutManager(this,GridLayoutManager.VERTICAL, false)
//            binding.rvViewAll.adapter = ServiceOrderAdapter(this,items) {
//                val intent=Intent(this,ServiceOrderDetailActivity::class.java)
//                intent.putExtra(Constants.INTENT_TYPE,it.toString())
//                startActivity(intent)
//            }
//        }else{
//            binding.noData.visibility = View.VISIBLE
//            binding.rvViewAll.visibility = View.GONE
//        }
//
//
//    }


    override fun getViewBinding(): ActivityAllProductBinding {
        return ActivityAllProductBinding.inflate(layoutInflater)
    }


}
