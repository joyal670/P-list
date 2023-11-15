package com.iroid.patrickstore.ui.forgotpassword.numberverfication

import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.FragmentMobileNumberBinding
import com.iroid.patrickstore.ui.forgotpassword.viewmodel.ForgotViewModal
import com.iroid.patrickstore.utils.*

class MobileFragment : BaseFragment<ForgotViewModal, FragmentMobileNumberBinding>() {

    override fun getViewBinding(): FragmentMobileNumberBinding {
        return FragmentMobileNumberBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setOnTextChanged()
    }

    private fun setOnTextChanged() {
        binding.editPhone.doOnTextChanged { text, start, before, count ->
            viewModel.onEmailTextChanged(text.toString())
        }
    }

    override fun setOnClick() {
        binding.btnSend.setOnClickListener {
            viewModel.onForgot()
        }
    }

    override fun setUpObserver() {

        viewModel.emptyEmail?.observe(requireActivity(), Observer { emailErrorMessage ->
            if (emailErrorMessage != null) {
                binding.editPhone.error = emailErrorMessage
            } else {
                binding.editPhone.error = null
            }

        })
        viewModel.forgotLiveData.observe(requireActivity(), Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.data?.msg!!,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )
                    //Navigation.createNavigateOnClickListener(R.id.action_mobile)

                    val action = MobileFragmentDirections.actionMobile(it.data.data.otp.toString())
                    NavHostFragment.findNavController(this).navigate(action)

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
        })
    }
}