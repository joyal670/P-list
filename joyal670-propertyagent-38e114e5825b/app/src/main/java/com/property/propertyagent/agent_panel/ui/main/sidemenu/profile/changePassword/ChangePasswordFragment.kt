package com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.changePassword

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.home.HomeActivity
import com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.ProfileActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.start_up.auth.fragment.ForgotPasswordFragment
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import com.property.propertyagent.utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_changepassword.*

class ChangePasswordFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var changePasswordViewModel: ChangePasswordViewModel

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_changepassword, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as ProfileActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.ChangePassword))

        forgotPasswordBtn.setOnClickListener {
            appCompatActivity?.replaceFragment(
                fragment = ForgotPasswordFragment.newInstance(0, true),
                addToBackStack = true
            )
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        changePasswordViewModel = ChangePasswordViewModel()
    }

    override fun setupObserver() {
        changePasswordViewModel.getChangePasswordAgentResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> showProgress()
                    Status.SUCCESS -> {
                        dismissProgress()
                        Toast.makeText(requireContext(), it.data!!.response, Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(context, HomeActivity::class.java)
                        context?.startActivity(intent)
                        activity?.finishAffinity()
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.internal_server_error),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            it.data!!.response,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
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
                }
            }
    }

    override fun onClicks() {
        changePasswordBtn.setOnClickListener {
            when {
                etOldPassword.text.trim().toString().isEmpty() -> {
                    Toaster.showToast(
                        requireContext(), getString(R.string.old_password_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                }
                etNewPassword.text.trim().toString().isEmpty() -> {
                    Toaster.showToast(
                        requireContext(), getString(R.string.new_password_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                }
                etNewPasswordConfirm.text.trim().toString().isEmpty() -> {
                    Toaster.showToast(
                        requireContext(),
                        getString(R.string.confirm_password_required),
                        Toaster.State.WARNING,
                        Toast.LENGTH_LONG
                    )
                }
                etNewPassword.text.trim().toString() != etNewPasswordConfirm.text.trim()
                    .toString() -> {
                    Toaster.showToast(
                        requireContext(), getString(R.string.password_must_be_same),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                }
                else -> {
                    changePasswordViewModel.changePasswordAgent(
                        etOldPassword.text.trim().toString(),
                        etNewPassword.text.trim().toString(),
                        etNewPasswordConfirm.text.trim().toString()
                    )
                }
            }
        }
    }
}