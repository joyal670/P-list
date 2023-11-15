package com.proteinium.proteiniumdietapp.ui.start_up.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseFragment
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.ui.main.home.HomeActivity
import com.proteinium.proteiniumdietapp.ui.start_up.auth.forgotpassword.ForgotPasswordFragment
import com.proteinium.proteiniumdietapp.ui.start_up.auth.signup.SignUpFragment
import com.proteinium.proteiniumdietapp.utils.*
import com.proteinium.proteiniumdietapp.utils.Constants.ARG_LOGIN
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment() {
    private lateinit var loginViewModel: LoginViewModel
    private var isGuest:Boolean = false
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_login, container, false)
    }

    companion object {
        fun newInstance(isGuest: Boolean) = LoginFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_LOGIN, isGuest)
            }
        }
    }




    override fun initData() {
        requireArguments().let {
            isGuest=requireArguments().getBoolean(ARG_LOGIN)
        }


        if (isGuest) {
            tvSignInSkip.visibility = View.GONE
        } else {
            tvSignInSkip.visibility = View.VISIBLE
        }
        if (AppPreferences.chooseLanguage == Constants.ARABIC_LAG) {
            etSignInPassword.gravity = Gravity.END
        } else {
            etSignInPassword.gravity = Gravity.START
        }
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun setupObserver() {

        loginViewModel.getLoginResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    if (it.data?.status!!) {
                        AppPreferences.user_id = it.data.user.id
                        AppPreferences.token = it.data.user.access_token
                        AppPreferences.user_phone = it.data.user.phone
                        AppPreferences.user_email = it.data.user.email
                        AppPreferences.isLogin = true
                        AppPreferences.isPlanActive = it.data.plan_active_status
                        if(isGuest){
                            if(it.data?.plan_active_status!!){
                                val intent = Intent(context, HomeActivity::class.java)
                                intent.putExtra(Constants.TYPE, EnumFromPage.HOME.name)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                                requireActivity().finish()
                            }else{
                                requireActivity().finish()
                            }

                        }else{
                            val intent = Intent(context, HomeActivity::class.java)
                            intent.putExtra(Constants.TYPE, EnumFromPage.HOME.name)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(), it.message.toString())
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(),  getString(R.string.no_internet))

                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(), getString(R.string.data_empty))

                }
            }
        })
    }

    override fun onClicks() {
        signInBtn.setOnClickListener {
            if (validation()) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loginViewModel.setLoginDetails(
                            etSignInName.text.toString().trim(),
                            etSignInPassword.text.toString().trim(),
                            task.result!!
                        )
                    }


                })

            }
        }

        tvSignInSignUp.setOnClickListener {
            appCompatActivity?.replaceFragment(
                fragment = SignUpFragment.newInstance(isGuest),
                addToBackStack = true
            )
        }
        tvSignInForgotPassword.setOnClickListener {
            appCompatActivity?.replaceFragment(
                fragment = ForgotPasswordFragment(),
                addToBackStack = true
            )
        }

        tvSignInSkip.setOnClickListener {
            AppPreferences.user_id = 0
            AppPreferences.token = ""
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.HOME.name)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun validation(): Boolean = if (etSignInName.text.toString().trim().isNullOrEmpty()) {
        CommonUtils.warningToast(requireContext(),getString(R.string.empty_email))
        false
    } else if (!etSignInName.text.toString().trim().isEmailValid()) {
        CommonUtils.warningToast(requireContext(), getString(R.string.invalid_email))
        false
    } else if (etSignInPassword.text.toString().trim().isNullOrEmpty()) {
        CommonUtils.warningToast(requireContext(), getString(R.string.empty_password))

        false
    } else {
        true
    }
}