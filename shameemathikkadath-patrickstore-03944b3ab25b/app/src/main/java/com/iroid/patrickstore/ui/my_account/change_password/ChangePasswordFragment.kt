package com.iroid.patrickstore.ui.my_account.change_password

import android.content.Intent
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.FragmentChangePasswordBinding
import com.iroid.patrickstore.ui.forgotpassword.otp.OtpActivity
import com.iroid.patrickstore.ui.interfaces.FragmentTransInterface
import com.iroid.patrickstore.ui.my_account.MyAccountActivity
import com.iroid.patrickstore.ui.my_account.MyAccountViewModal
import com.iroid.patrickstore.utils.*

class ChangePasswordFragment : BaseFragment<MyAccountViewModal, FragmentChangePasswordBinding>() {

    private lateinit var fragmentTransInterface: FragmentTransInterface

    override fun initViews() {
        fragmentTransInterface = activity as MyAccountActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.change_password))
        setOnTextChanged()
    }

    override fun setUpObserver() {
        viewModel.changePasswordLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    val intent = Intent(requireActivity(), OtpActivity::class.java)
                    intent.putExtra(Constants.INTENT_OTP, "")
                    startActivity(intent)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
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
                        Toaster.showToast(
                            requireContext(),
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }

        viewModel.emptyOldPassword?.observe(this) { message ->
            if (message != null)
                binding.tilNewPassword.error = message
            else
                binding.tilNewPassword.isErrorEnabled = false
        }

        viewModel.emptyNewPassword?.observe(this) { message ->
            if (message != null)
                binding.tilConfirmPassword.error = message
            else
                binding.tilConfirmPassword.isErrorEnabled = false
        }
    }

    override fun setOnClick() {
        binding.btnChangePassword.setOnClickListener {
            viewModel.changePassword()
        }
    }

    override fun getViewBinding(): FragmentChangePasswordBinding {
        return FragmentChangePasswordBinding.inflate(layoutInflater)
    }

    private fun setOnTextChanged() {
        binding.edtOldPassword.doOnTextChanged { text, start, before, count ->
            viewModel.onOldTextChanged(text.toString())
        }
        binding.edtNewPassword.doOnTextChanged { text, start, before, count ->
            viewModel.onNewTextChanged(text.toString())
        }
    }
}
