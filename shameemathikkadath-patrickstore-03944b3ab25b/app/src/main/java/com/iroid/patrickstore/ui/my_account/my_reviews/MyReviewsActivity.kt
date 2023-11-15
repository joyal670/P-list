package com.iroid.patrickstore.ui.my_account.my_reviews

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityWalletBinding
import com.iroid.patrickstore.ui.my_account.reward_points.WalletViewModal
import com.iroid.patrickstore.utils.Status
import com.iroid.patrickstore.utils.Toaster
import com.iroid.patrickstore.utils.isConnected
import kotlinx.android.synthetic.main.activity_seller.*

class MyReviewsActivity : BaseActivity<WalletViewModal,ActivityWalletBinding>() {
    override val layoutId: Int
        get() =  R.layout.activity_wallet
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.reviews)

    }

    private fun getTitles(): List<String>{
        return listOf(
            getString(R.string.product_review),
            getString(R.string.store_review))
    }

    override fun getViewBinding(): ActivityWalletBinding {
        return ActivityWalletBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setUpObserver()
        viewModel.getDeliveredOrder()
    }

    private fun setUpObserver() {
        viewModel.deliveredLiveData.observe(this, Observer { deliveredLiveData->
            when(deliveredLiveData.status){
                Status.SUCCESS->{
                    dismissProgress()
                    pagerSeller.adapter = ReviewsPagerAdapter(supportFragmentManager, getTitles(),deliveredLiveData.data!!.data.items)
                }
                Status.LOADING->{
                    showProgress()
                }
                Status.ERROR->{
                    dismissProgress()
                    Toaster.showToast(this, deliveredLiveData.message!!,Toaster.State.ERROR,Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissProgress()
                    if(isConnected){
                        Toaster.showToast(this, "Something went wrong",Toaster.State.ERROR,Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(this, "Please check network connectivity",Toaster.State.ERROR,Toast.LENGTH_LONG)

                    }

                }
            }

        })
    }
}
