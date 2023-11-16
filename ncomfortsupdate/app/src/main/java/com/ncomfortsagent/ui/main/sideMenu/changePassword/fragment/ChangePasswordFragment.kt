package com.ncomfortsagent.ui.main.sideMenu.changePassword.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseFragment
import com.ncomfortsagent.databinding.FragmentChangepasswordBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.listeners.FragmentTransInterface
import com.ncomfortsagent.ui.main.home.home.activity.HomeActivity
import com.ncomfortsagent.ui.main.sideMenu.activity.SideMenuActivity
import com.ncomfortsagent.ui.main.sideMenu.changePassword.viewmodel.ChangePasswordViewModel
import com.ncomfortsagent.utils.CommonUtils.Companion.dismissKeyboard
import com.ncomfortsagent.utils.CommonUtils.Companion.showCookieBar
import com.ncomfortsagent.utils.Status
import com.ncomfortsagent.utils.isConnected
import java.util.*


class ChangePasswordFragment : BaseFragment() {
    private lateinit var binding: FragmentChangepasswordBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var changePasswordViewModel: ChangePasswordViewModel

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangepasswordBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as SideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.change_password))
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        changePasswordViewModel = ChangePasswordViewModel(requireActivity())
    }

    override fun setupObserver() {
        changePasswordViewModel.getAgentChangePasswordResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    showCookieBar(
                        requireActivity(),
                        getString(R.string.success),
                        it.data!!.response,
                        R.color.de_york
                    )
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            startActivity(Intent(requireContext(), HomeActivity::class.java))
                        }
                    }, 1000)

                }

                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireContext().isConnected) {
                        showCookieBar(
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
                Status.ERROR -> {
                    dismissProgress()
                    showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
            }
        }
    }

    override fun onClicks() {

        binding.submitPassword.setOnClickListener {
            val currentPassword = binding.EtCurrentPassword.text.trim().toString()
            val password = binding.EtNewPassword.text.trim().toString()
            val confirmPassword = binding.EtConfirmPassword.text.trim().toString()

            if (currentPassword.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                if (currentPassword.isBlank()) {
                    binding.EtCurrentPassword.error = getString(R.string.required)
                }
                if (password.isBlank()) {
                    binding.EtNewPassword.error = getString(R.string.required)
                }
                if (confirmPassword.isBlank()) {
                    binding.EtConfirmPassword.error = getString(R.string.required)
                }
            } else {
                if (password == confirmPassword) {
                    requireActivity().dismissKeyboard()
                    changePasswordViewModel.changePassword(
                        currentPassword,
                        password,
                        confirmPassword
                    )
                } else {
                    requireActivity().dismissKeyboard()
                    binding.EtConfirmPassword.requestFocus()
                    showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        getString(R.string.passwordnotmatched),
                        R.color.pomegranate
                    )
                }
            }
        }
    }

}