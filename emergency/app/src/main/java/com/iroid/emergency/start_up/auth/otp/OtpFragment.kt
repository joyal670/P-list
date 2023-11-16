package com.iroid.emergency.start_up.auth.otp

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentOtpBinding
import com.iroid.emergency.main.home.HomeActivity
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.start_up.auth.AuthActivity
import com.iroid.emergency.start_up.auth.AuthViewModal
import com.iroid.emergency.start_up.utils.*

class OtpFragment : BaseFragment<AuthViewModal, FragmentOtpBinding>() {
    override fun initViews() {

    }

    override fun setUpObserver() {
        viewModel.otpLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    AppPreferences.userMobile = it.data!!.profile!!.mobile.toString()
                    AppPreferences.userName = it.data!!.profile!!.name.toString()
                    AppPreferences.userType = it.data!!.profile!!.type.toString()
                    AppPreferences.userToken = it.data!!.accessToken.toString()
                    AppPreferences.isLogin = true
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                    requireActivity().finishAffinity()
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
                        requireArguments().getString("mobile")!!, otp, it.result
                    )
                }


        }
        binding.etOtp1.doAfterTextChanged {
            if (it!!.length == 1) {
                binding.etOtp2.requestFocus()
            }
        }
        binding.etOtp2.doAfterTextChanged {
            if (it!!.length == 1) {
                binding.etOtp3.requestFocus()
            } else {
                binding.etOtp1.requestFocus()
            }
        }
        binding.etOtp3.doAfterTextChanged {
            if (it!!.length == 1) {
                binding.etOtp4.requestFocus()
            } else {
                binding.etOtp2.requestFocus()
            }
        }
        binding.etOtp4.doAfterTextChanged {
            if (it!!.length == 1) {
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
