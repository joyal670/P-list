package com.ncomfortsagent.start_up.auth.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseFragment
import com.ncomfortsagent.databinding.FragmentForgotPasswordBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.start_up.auth.viewmodel.ForgotPasswordViewModel
import com.ncomfortsagent.utils.CommonUtils
import com.ncomfortsagent.utils.CommonUtils.Companion.dismissKeyboard
import com.ncomfortsagent.utils.Status
import com.ncomfortsagent.utils.isConnected
import com.ncomfortsagent.utils.replaceFragment


class ForgotPasswordFragment : BaseFragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
    private var password = ""

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        forgotPasswordViewModel = ForgotPasswordViewModel(requireActivity())
    }

    override fun setupObserver() {

        /* forgot password */
        forgotPasswordViewModel.getAgentForgotPasswordResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    password = it.data!!.data
                    binding.tvPassword.text = it.data.data
                    binding.tvPassword.visibility = View.VISIBLE
                    binding.copyBtn.visibility = View.VISIBLE

                    requireActivity().dismissKeyboard()

                    CommonUtils.showCookieBar(
                        requireActivity(),
                        getString(R.string.success),
                        it.data.response,
                        R.color.de_york
                    )

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireContext().isConnected) {

                        CommonUtils.showCookieBar(
                            requireActivity(),
                            getString(R.string.error),
                            getString(R.string.something_wrong),
                            R.color.pomegranate
                        )

                    } else {
                        val dialog = InternetDialogFragment(requireActivity())
                        dialog.show(parentFragmentManager, "TAG")
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()

                    CommonUtils.showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )

                }
                Status.ERROR -> {
                    dismissProgress()

                    CommonUtils.showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )

                }
            }
        }
    }

    override fun onClicks() {

        binding.forgotPasswordBtn.setOnClickListener {
            val phone = binding.EtPhone.text.trim().toString()
            if (phone.isBlank()) {
                binding.EtPhone.error = getString(R.string.Phonenumberisrequired)
            } else {
                forgotPasswordViewModel.forgotPassword(phone)
            }
        }

        binding.copyBtn.setOnClickListener {
            setClipboard()
        }
    }

    private fun setClipboard() {
        if (password == "") {
            CommonUtils.showCookieBar(
                requireActivity(),
                getString(R.string.error),
                getString(R.string.something_wrong),
                R.color.pomegranate
            )
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                val clipboard =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
                clipboard.text = password
            } else {
                val clipboard =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", password)
                clipboard.setPrimaryClip(clip)
            }

            CommonUtils.showCookieBar(
                requireActivity(),
                getString(R.string.success),
                getString(R.string.copiedtoclipboard),
                R.color.de_york
            )

            appCompatActivity?.replaceFragment(
                fragment = LoginFragment(),
                addToBackStack = false
            )
        }


    }

}