package com.proteinium.proteiniumdietapp.ui.start_up.auth.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_verify.*

class VerifyFragment : BaseFragment() {
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_verify, container, false)
    }

    override fun initData() {

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks()
    {
        ivBackVerifiy.setOnClickListener {
            activity?.onBackPressed()
        }

        otp1.addTextChangedListener ( object :TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                otp2.requestFocus()
            }

        })

        otp2.addTextChangedListener ( object :TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                otp3.requestFocus()
            }

        })

        otp3.addTextChangedListener ( object :TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                otp4.requestFocus()
            }

        })

        submitOtpBtn.setOnClickListener {

        }

    }


}