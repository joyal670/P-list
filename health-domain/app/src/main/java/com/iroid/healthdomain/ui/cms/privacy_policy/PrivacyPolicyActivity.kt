package com.iroid.healthdomain.ui.cms.privacy_policy

import android.content.Intent
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.ActivityPrivacyPolicyBinding
import com.iroid.healthdomain.ui.base.BaseActivity
import com.iroid.healthdomain.ui.cms.CmsRepository
import com.iroid.healthdomain.ui.cms.CmsViewModel
import com.iroid.healthdomain.ui.home.HomeActivity


class PrivacyPolicyActivity:BaseActivity<CmsViewModel,ActivityPrivacyPolicyBinding, CmsRepository>() {
    override fun getLayoutRes(): Int = R.layout.activity_privacy_policy

    override fun getViewModel(): Class<CmsViewModel> = CmsViewModel::class.java
    override fun getFragmentRepository(): CmsRepository {
        return CmsRepository(remoteDataSource.buildApi(ApiServices::class.java), userPreferences)
    }


    private fun addObserver() {
        viewModel.cmsLiveData.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    viewModel.setLoading(true)

                }
                is Resource.Success -> {
                    if (it.value.status == 200) {
                        viewModel.setLoading(false)
                        binding.cmsViewModal = it.value.data

                    }

                }
                is Resource.Failure -> {
                    viewModel.setLoading(false)
                }
            }
        })
    }

    override fun init() {
        viewModel.getPrivacyPolicy()
        addObserver()
        binding.tvToolbarTitle.text="Privacy Policy"
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.getItemId()) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }


}