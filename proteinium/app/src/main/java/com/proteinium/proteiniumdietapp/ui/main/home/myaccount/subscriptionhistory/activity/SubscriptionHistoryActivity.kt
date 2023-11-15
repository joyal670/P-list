
package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.activity

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.adapter.SubscriptionAdapter
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.Status
import com.proteinium.proteiniumdietapp.utils.isConnected
import kotlinx.android.synthetic.main.activity_subscription_history.*
import kotlinx.android.synthetic.main.no_internet.*
import kotlinx.android.synthetic.main.no_subscrption.*
import kotlinx.android.synthetic.main.toolbar_sub.*

class SubscriptionHistoryActivity : BaseActivity() {
    private lateinit var subscrptionHistoryViewModel: SubscriptionHistoryViewModel
    override val layoutId: Int
        get() = R.layout.activity_subscription_history
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbarSub)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if(AppPreferences.chooseLanguage== Constants.ENGLISH_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_toolbar24)
        }
        if(AppPreferences.chooseLanguage==Constants.ARABIC_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvSubToolbarTitle.text = getString(R.string.tvSubscriptionHistory)
        tvNoSubscription.text=getString(R.string.no_subscription)
        AppPreferences.user_id?.let { subscrptionHistoryViewModel.fetchSubscriptionsHistory(it) }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        subscrptionHistoryViewModel = ViewModelProviders.of(this).get(SubscriptionHistoryViewModel::class.java)
    }

    override fun setupObserver() {
        subscrptionHistoryViewModel.getSubscriptionsHistoryResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    noInternetLayoutSubscription.visibility=View.GONE
                    if (it.data?.status!!) {
                        if (it.data.data.subscriptions.isNotEmpty()) {
                            recycerViewSubscriptionHistory.visibility= View.VISIBLE
                            noSubscriptionLayout.visibility=View.GONE
                            recycerViewSubscriptionHistory.layoutManager = LinearLayoutManager(this)
                            recycerViewSubscriptionHistory.adapter = SubscriptionAdapter(it.data.data.subscriptions,false,this)
                        }else{
                            recycerViewSubscriptionHistory.visibility= View.GONE
                            noSubscriptionLayout.visibility=View.VISIBLE
                        }
                        if (it.data.data.renewals.isNotEmpty()) {
                            tvRenewal.visibility=View.VISIBLE
                            recycerViewSubscriptionRenewal.visibility= View.VISIBLE
                            noSubscriptionLayout.visibility=View.GONE
                            recycerViewSubscriptionRenewal.layoutManager = LinearLayoutManager(this)
                            recycerViewSubscriptionRenewal.adapter = SubscriptionAdapter(
                                it.data.data.renewals,
                                true,
                                this
                            )

                        }

                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                   if(this.isConnected){
                       recycerViewSubscriptionHistory.visibility= View.GONE
                       noSubscriptionLayout.visibility=View.VISIBLE

                    }else{
                        noInternetLayoutSubscription.visibility=View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.data_empty))
                }
            }
        })
    }

    override fun onClicks() {
        btnRetry.setOnClickListener {
            AppPreferences.user_id?.let { it1 -> subscrptionHistoryViewModel.fetchSubscriptionsHistory(it1)}
        }
    }

    override fun onResume() {
        super.onResume()
        AppPreferences.user_id?.let { subscrptionHistoryViewModel.fetchSubscriptionsHistory(it) }
    }

}
