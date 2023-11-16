package com.ncomfortsagent.start_up.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseFragment
import com.ncomfortsagent.databinding.FragmentLoginBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.start_up.auth.viewmodel.LoginViewModel
import com.ncomfortsagent.ui.main.home.home.activity.HomeActivity
import com.ncomfortsagent.utils.AppPreferences.prefIsLogin
import com.ncomfortsagent.utils.AppPreferences.prefProfileImage
import com.ncomfortsagent.utils.AppPreferences.prefUserEmail
import com.ncomfortsagent.utils.AppPreferences.prefUserName
import com.ncomfortsagent.utils.AppPreferences.prefUserToken
import com.ncomfortsagent.utils.AppPreferences.agent_id
import com.ncomfortsagent.utils.CommonUtils.Companion.showCookieBar
import com.ncomfortsagent.utils.Status
import com.ncomfortsagent.utils.isConnected
import com.ncomfortsagent.utils.replaceFragment


class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
        loginViewModel = LoginViewModel(requireActivity())
    }

    override fun setupObserver() {

        /* login api */
        loginViewModel.getAgentLoginResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    /* save data to preferences */
                    prefUserToken = it.data!!.data.api_token
                    prefIsLogin = true
                    prefProfileImage = it.data.data.profile_pic
                    prefUserName = it.data.data.name
                    prefUserEmail = it.data.data.email
                    agent_id = it.data.data.agent_id

                    /* navigate to home page */
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    ActivityCompat.finishAffinity(requireActivity())
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
                Status.DATA_EMPTY -> {
                    dismissProgress()

                    showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )

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
            }
        })

    }

    override fun onClicks() {

        binding.loginBtn.setOnClickListener {

            val phone = binding.EtPhone.text.toString()
            val password = binding.EtPassword.text.toString()

            if (phone.isBlank() || password.isBlank()) {
                if (phone.isBlank()) {
                    binding.EtPhone.error = getString(R.string.required)
                }
                if (password.isBlank()) {
                    binding.EtPassword.error = getString(R.string.required)
                }
            } else {
                loginViewModel.login(phone, password)
            }

        }

        binding.tvForgotPassword.setOnClickListener {
            appCompatActivity?.replaceFragment(
                fragment = ForgotPasswordFragment(),
                addToBackStack = true
            )
        }
    }
}