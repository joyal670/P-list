package com.property.propertyagent.start_up.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.ProfileActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.databinding.FragmentForgotpasswordBinding
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.start_up.auth.viewmodel.ForgotPasswordViewModel
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import com.property.propertyagent.utils.replaceFragment

class ForgotPasswordFragment : BaseFragment() {
    private lateinit var binding: FragmentForgotpasswordBinding
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private var setToolBar = false

    companion object {

        private var user: Int? = null

        @JvmStatic
        fun newInstance(type: Int, setToolBar: Boolean) =
            ForgotPasswordFragment().apply {
                arguments = Bundle().apply {
                    putInt("user", type)
                    putBoolean("toolbar", setToolBar)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getInt("user")
            setToolBar = it.getBoolean("toolbar")
        }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotpasswordBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {

        if (setToolBar) {
            fragmentTransInterface = activity as ProfileActivity
            fragmentTransInterface.setTitleFromFragment(getString(R.string.forgot_password))
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        forgotPasswordViewModel = ForgotPasswordViewModel()
    }

    override fun setupObserver() {
        forgotPasswordViewModel.forgotPasswordData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.response,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )
                    appCompatActivity?.replaceFragment(
                        fragment = LoginFragment(),
                        addToBackStack = false
                    )
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
        /* values from instance
        * 0 -> agent
        * 1 -> owner */

        /* api passing values
        * 1 -> owner
        * 2 -> agent */
        binding.forgotPasswordBtn.setOnClickListener {
            var userType = ""
            when (user) {
                0 -> userType = "2"
                1 -> userType = "1"
            }
            val email = binding.fragmentForgotEmailEtx.text.toString()
            if (email.isBlank()) {
                binding.tilEmailForgot.error = getString(R.string.email_required)
            } else {
                forgotPasswordViewModel.forgotPassword(email, userType)
            }
        }
    }
}