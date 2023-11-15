package com.iroid.emergency.main.settings.profile

import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentOtpBinding
import com.iroid.emergency.main.home.HomeActivity
import com.iroid.emergency.main.settings.SettingsViewModal
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.start_up.auth.AuthViewModal
import com.iroid.emergency.start_up.utils.*

class OtpFragmentProfile : BaseFragment<SettingsViewModal, FragmentOtpBinding>() {
    override fun initViews() {

    }

    override fun setUpObserver() {
        viewModel.otpLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    findNavController().navigate(R.id.action_otpFragmentProfile_to_nav_profile)
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(requireContext(), it.message!!, Toaster.State.ERROR)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireActivity().isConnected) {
                        Toaster.showToast(
                            requireContext(),
                            "Something went wrong",
                            Toaster.State.ERROR
                        )
                    } else {
                        requireActivity().netDialog(lifecycle)
                    }
                }

            }
        })

    }

    override fun setOnClick() {

        binding.btnSubmit.setOnClickListener {
            val otp = binding.etOtp1.text.toString() +
                binding.etOtp2.text.toString() +
                binding.etOtp3.text.toString() +
                binding.etOtp4.text.toString()
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener {
                    viewModel.verifyOtp(
                   "", otp, it.result
                    )
                }


        }
        binding.etOtp1.doOnTextChanged { text, start, before, count ->
            if (count == 1) {
                binding.etOtp2.requestFocus()
            }
        }
        binding.etOtp2.doOnTextChanged { text, start, before, count ->
            if (count == 1) {
                binding.etOtp3.requestFocus()
            } else {
                binding.etOtp1.requestFocus()
            }
        }
        binding.etOtp3.doOnTextChanged { text, start, before, count ->
            if (count == 1) {
                binding.etOtp4.requestFocus()
            } else {
                binding.etOtp2.requestFocus()
            }
        }
        binding.etOtp4.doOnTextChanged { text, start, before, count ->
            if (count == 1) {
                binding.btnSubmit.requestFocus()
            } else {
                binding.etOtp3.requestFocus()
            }
        }



    }

    override fun getViewBinding(): FragmentOtpBinding {
        return FragmentOtpBinding.inflate(layoutInflater)
    }
}
