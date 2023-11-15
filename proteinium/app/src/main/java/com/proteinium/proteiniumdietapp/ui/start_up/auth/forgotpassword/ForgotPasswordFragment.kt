package com.proteinium.proteiniumdietapp.ui.start_up.auth.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseFragment
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.utils.*
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class ForgotPasswordFragment : BaseFragment()
{
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun initData() {
        if(AppPreferences.chooseLanguage== Constants.ENGLISH_LAG){
                ivBack.setImageResource(R.drawable.ic_arrow_back_toolbar24)
        }
        if(AppPreferences.chooseLanguage==Constants.ARABIC_LAG){
            ivBack.setImageResource(R.drawable.ic_baseline_arrow_forward_white)
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        forgotPasswordViewModel= ForgotPasswordViewModel()
    }

    override fun setupObserver() {
        forgotPasswordViewModel.getForgotLiveData().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(),it.data!!.message)
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(),it.message.toString())
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(),getString(R.string.no_internet))
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(),it.data!!.message)
                }
            }
        })
    }

    override fun onClicks() {
        btnSend.setOnClickListener {
           if(tvEmail.text.toString().isNotEmpty()){
               forgotPasswordViewModel.getForgotLiveData(tvEmail.text.toString().trim())
           }
        }

        ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}