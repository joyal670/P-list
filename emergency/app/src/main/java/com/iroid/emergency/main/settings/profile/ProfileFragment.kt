package com.iroid.emergency.main.settings.profile

import androidx.lifecycle.Observer
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentViewProfileBinding
import com.iroid.emergency.main.settings.SettingsViewModal
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.start_up.utils.Status

class ProfileFragment : BaseFragment<SettingsViewModal, FragmentViewProfileBinding>() {
    override fun initViews() {
        viewModel.getProfile()
    }

    override fun setUpObserver() {
        viewModel.profileLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    binding.etName.setText(it.data!!.user.name)
                    binding.etMobile.setText(it.data!!.user.mobile.toString())
                    binding.etUserType.setText(AppPreferences.userType)
                }
                Status.ERROR -> {
                    dismissProgress()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()

                }
            }
        })

    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentViewProfileBinding {
        return FragmentViewProfileBinding.inflate(layoutInflater)
    }

}
