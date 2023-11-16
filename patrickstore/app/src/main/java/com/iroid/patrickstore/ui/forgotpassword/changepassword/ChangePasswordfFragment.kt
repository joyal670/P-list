package com.iroid.patrickstore.ui.forgotpassword.changepassword

import android.content.Intent
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.FragmentChangePasswordBinding
import com.iroid.patrickstore.ui.login.LoginActivity
import com.iroid.patrickstore.ui.my_account.MyAccountViewModal
import com.iroid.patrickstore.utils.*


class ChangePasswordfFragment : BaseFragment<MyAccountViewModal, FragmentChangePasswordBinding>() {


   /* override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_change_password, container, false)
        root.findViewById<Button>(R.id.btnSend)?.setOnClickListener {
            activity!!.finish()
        }
        return root
    }*/

    override fun initViews() {
        setOnTextChanged()
    }

    override fun setUpObserver() {
        viewModel.changePasswordLiveData.observe(requireActivity(), {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    startActivity(Intent(requireContext(),  LoginActivity::class.java))
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

    override fun setOnClick() {
        binding.btnChangePassword.setOnClickListener {
             viewModel.changePassword()

        }
    }

    private fun setOnTextChanged() {
        binding.edtOldPassword.doOnTextChanged { text, start, before, count ->
            viewModel.onOldTextChanged(text.toString())
        }
        binding.edtNewPassword.doOnTextChanged { text, start, before, count ->
            viewModel.onNewTextChanged(text.toString())
        }
    }

    override fun getViewBinding(): FragmentChangePasswordBinding {
       return FragmentChangePasswordBinding.inflate(layoutInflater)
    }

}
