package com.iroid.patrickstore.ui.my_account.reward_points

import android.os.Bundle
import androidx.lifecycle.Observer
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityWalletBinding
import com.iroid.patrickstore.utils.Status
import kotlinx.android.synthetic.main.activity_wallet.*

class WalletActivity : BaseActivity<WalletViewModal,ActivityWalletBinding>() {
    override val layoutId: Int
        get() =  R.layout.activity_wallet
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.wallet)

    }

    private fun getTitles(): List<String>{
        return listOf(
            getString(R.string.reward_point),
            getString(R.string.cash_back))
    }

    override fun getViewBinding(): ActivityWalletBinding {
        return ActivityWalletBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        viewModel.getReward()
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.rewardLiveData.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    dismissProgress()
                    pagerSeller.adapter = WalletPagerAdapter(supportFragmentManager, getTitles(),
                    it.data!!.data.totalRewardPoints)
                }
                Status.ERROR->{
                    dismissProgress()
                }
                Status.LOADING->{
                    showProgress()
                }
                Status.NO_INTERNET->{
                    dismissProgress()
                }
            }
        })
    }
}
