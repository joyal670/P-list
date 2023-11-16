package com.iroid.jeetmeet.start_up.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentLoginBinding
import com.iroid.jeetmeet.start_up.auth.viewmodel.ParentLoginViewModel
import com.iroid.jeetmeet.start_up.auth.viewmodel.StudentLoginViewModel
import com.iroid.jeetmeet.ui.main.parent_panel.home.activity.ParentDashboardActivity
import com.iroid.jeetmeet.ui.main.student_panel.home.activity.StudentDashboardActivity
import com.iroid.jeetmeet.utils.*
import com.iroid.jeetmeet.utils.AppPreferences.prefIsLogin
import com.iroid.jeetmeet.utils.AppPreferences.prefLoginType
import com.iroid.jeetmeet.utils.AppPreferences.prefUserToken
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var userPreferences: DataStoreUserPreferences
    private lateinit var studentLoginViewModel: StudentLoginViewModel
    private lateinit var parentLoginViewModel: ParentLoginViewModel

    companion object {

        private const val TAG = "LoginFragment"
        private var user: String? = null

        @JvmStatic
        fun newInstance(type: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString("user", type)
                }
            }
    }

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
        userPreferences = DataStoreUserPreferences(requireContext())
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

        val username = binding.EtUsername.text.toString()
        val password = binding.EtPassword.text.toString()

        studentLoginViewModel = StudentLoginViewModel()
        studentLoginViewModel.studentLoginApi(username, password)
        studentLoginViewModel.studentLoginData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    /* save values */
                    prefUserToken = it.data!!.message
                    prefIsLogin = true
                    prefLoginType = Constants.STUDENT

                    lifecycleScope.launch {
                        /* is login */
                        userPreferences.saveValue(
                            requireContext(),
                            Constants.IS_LOGIN,
                            Constants.TRUE
                        )

                        /* login type */
                        userPreferences.saveValue(
                            requireContext(),
                            Constants.STUDENT,
                            Constants.STUDENT
                        )

                        /* login token */
                        userPreferences.saveValue(
                            requireContext(),
                            Constants.TOKEN,
                            it.data.message
                        )
                    }

                    /* navigate to student dashboard */
                    val intent = Intent(requireContext(), StudentDashboardActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
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

    override fun onClicks() {
        binding.loginbtn.setOnClickListener {

            /* student */
            if (user.equals("Student")) {
                val username = binding.EtUsername.text.toString()
                val password = binding.EtPassword.text.toString()
                if (username.isBlank() || password.isBlank()) {
                    if (username.isBlank()) {
                        Toaster.showToast(
                            requireContext(),
                            "User name is required",
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                        binding.EtUsername.requestFocus()
                    }
                    if (password.isBlank()) {
                        Toaster.showToast(
                            requireContext(),
                            "Password is required",
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                        binding.EtPassword.requestFocus()
                    }
                } else {
                    setupObserver()
                }

            } else {

                /* parent */
                val username = binding.EtUsername.text.toString()
                val password = binding.EtPassword.text.toString()
                if (username.isBlank() || password.isBlank()) {
                    if (username.isBlank()) {
                        Toaster.showToast(
                            requireContext(),
                            "User name is required",
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                        binding.EtUsername.requestFocus()
                    }
                    if (password.isBlank()) {
                        Toaster.showToast(
                            requireContext(),
                            "Password is required",
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                        binding.EtPassword.requestFocus()
                    }
                } else {
                    setupParentObserver()
                }

            }
        }

        binding.tvForgotpassword.setOnClickListener {
            if (user.equals("Student")) {
                appCompatActivity?.replaceFragment(
                    fragment = ForgotPasswordFragment.newInstance("student"),
                    addToBackStack = true
                )
            } else {
                appCompatActivity?.replaceFragment(
                    fragment = ForgotPasswordFragment.newInstance("parent"),
                    addToBackStack = true
                )
            }
        }
    }

    /* Parent Login api */
    private fun setupParentObserver() {

        val username = binding.EtUsername.text.toString()
        val password = binding.EtPassword.text.toString()

        parentLoginViewModel = ParentLoginViewModel()
        parentLoginViewModel.parentLoginApi(username, password)
        parentLoginViewModel.parentLoginData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    /* save values */
                    prefUserToken = it.data!!.message
                    prefIsLogin = true
                    prefLoginType = Constants.PARENT

                    /* navigate to parent dashboard */
                    val intent = Intent(requireContext(), ParentDashboardActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
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
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getString("user")
        }

    }

}