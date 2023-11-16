package com.property.propertyagent.start_up.auth.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.home.HomeActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.databinding.FragmentLoginBinding
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.HomeOwnerActivity
import com.property.propertyagent.start_up.auth.viewmodel.LoginViewModel
import com.property.propertyagent.utils.AppPreferences.isLogin
import com.property.propertyagent.utils.AppPreferences.login_type
import com.property.propertyagent.utils.AppPreferences.token
import com.property.propertyagent.utils.AppPreferences.user_email
import com.property.propertyagent.utils.AppPreferences.user_id
import com.property.propertyagent.utils.AppPreferences.user_name
import com.property.propertyagent.utils.AppPreferences.user_phone
import com.property.propertyagent.utils.AppPreferences.user_profileImg
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import com.property.propertyagent.utils.replaceFragment

class LoginFragment : BaseFragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    private var doubleBackToExitPressedOnce = false
    private var userType: String = ""

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        when (binding.segBtn.position) {
            0 -> userType = "0"
            1 -> userType = "1"
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleBackToExitPressedOnce) {

                        val pid = Process.myPid()
                        Process.killProcess(pid)

                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        requireContext().startActivity(intent)
                        requireActivity().finish()
                        return
                    }
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(
                        requireContext(),
                        "Please click BACK again to exit",
                        Toast.LENGTH_SHORT
                    ).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 5000)
                }
            })
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
        loginViewModel = LoginViewModel()
    }

    override fun setupObserver() {
        loginViewModel.loginData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    /* Saving data to preference */
                    isLogin = true
                    token = it.data?.data?.api_token.toString()
                    login_type = it.data?.data?.type.toString()
                    user_id = it.data!!.data.id
                    user_name = it.data.data.name
                    user_email = it.data.data.email
                    user_phone = it.data.data.phone
                    user_profileImg = it.data.data.profile_image

                    /* start activity after successfully login */
                    if (login_type == "1") {
                        startActivity(Intent(requireActivity(), HomeOwnerActivity::class.java))
                        activity?.finishAffinity()
                    } else if (login_type == "0") {
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        activity?.finishAffinity()
                    }
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireContext().isConnected) {
                        Toaster.showToast(
                            requireContext(), getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(requireActivity())
                        dialog.show(parentFragmentManager, "TAG")
                    }
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
            }
        }
    }

    override fun onClicks() {
        binding.tvForgot.setOnClickListener {
            appCompatActivity?.replaceFragment(
                fragment = ForgotPasswordFragment.newInstance(binding.segBtn.position, false),
                addToBackStack = true
            )
        }

        binding.tvloginbtn.setOnClickListener {
            val email = binding.fragmentLoginEmailEtx.text.toString()
            val password = binding.fragmentLoginPasswordEtx.text.toString()
            if (email.isBlank() || password.isBlank()) {
                if (email.isBlank()) {
                    binding.tilEmail.error = getString(R.string.email_is_required)
                }
                if (password.isBlank()) {
                    binding.tilPassword.error =
                        getString(R.string.password_is_required)
                }
            } else {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        loginViewModel.login(
                            email, password, userType, task.result!!
                        )
                    }
                }
            }
        }

        binding.segBtn.setOnPositionChangedListener { position ->
            if (position == 1) {
                binding.segBtn.setSelectedBackground(Color.parseColor("#2F80ED"))
                binding.tvloginbtn.backgroundTintList = context?.let {
                    ContextCompat.getColorStateList(
                        it,
                        R.color.color_accent_blue_statusbar
                    )
                }
                binding.frameLogin.background = context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_loginbgblue
                    )
                }
                userType = "1"
            } else {
                binding.segBtn.setSelectedBackground(Color.parseColor("#6AC58C"))
                binding.tvloginbtn.backgroundTintList =
                    context?.let {
                        ContextCompat.getColorStateList(
                            it,
                            R.color.color_accent_green
                        )
                    }
                binding.frameLogin.background = context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_loginbg
                    )
                }
                userType = "0"
            }
        }
    }
}