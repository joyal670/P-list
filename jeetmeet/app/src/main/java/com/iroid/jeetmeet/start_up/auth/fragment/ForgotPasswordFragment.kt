package com.iroid.jeetmeet.start_up.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentForgotPasswordBinding
import com.iroid.jeetmeet.start_up.auth.viewmodel.ForgotPasswordViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster


class ForgotPasswordFragment : BaseFragment()
{
    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var forgotPasswordViewModel : ForgotPasswordViewModel

    companion object {

        private const val TAG = "ForgotPasswordFragment"
        private  var user : String? = null

        @JvmStatic
        fun newInstance(type: String) =
            ForgotPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString("user",type)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getString("user")
        }

    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater!!,container,false)
        return binding.root
    }

    override fun initData() {
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {
    }

    override fun onClicks() {

        binding.submitBtn.setOnClickListener {

            val email = binding.etEmail.text.toString()
            if(email.isBlank())
            {
                binding.etEmail.error = "Required"
            }
            else
            {
                setupForgotPasswordObserver(email, user!!)
            }
        }
    }

    private fun setupForgotPasswordObserver(email: String, user: String) {
        forgotPasswordViewModel= ForgotPasswordViewModel()
        forgotPasswordViewModel.forgotPassword(email, user)
        forgotPasswordViewModel.forgotPasswordData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{
                    dismissProgress()

                    Toaster.showToast(requireContext(), it.data!!.message, Toaster.State.SUCCESS, Toast.LENGTH_LONG)
                }
                Status.LOADING ->{
                    showProgress()
                }
                Status.ERROR ->{
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY ->{
                    dismissProgress()
                }
                Status.NO_INTERNET ->{
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })
    }

}