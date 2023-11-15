package com.property.propertyagent.agent_panel.ui.main.sidemenu.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.github.dhaval2404.imagepicker.ImagePicker
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.AppPreferences.isProfileChanged
import com.property.propertyagent.utils.AppPreferences.user_email
import com.property.propertyagent.utils.AppPreferences.user_name
import com.property.propertyagent.utils.AppPreferences.user_profileImg
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var profileViewModel: ProfileViewModel

    private var imagesPassed: String = ""
    private var isLoadingMain: Boolean = false
    private var editCheck = true
    private var isUpdateDone = false

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_profile, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as ProfileActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.my_profile))

        headerfrg_tvedit.paintFlags = headerfrg_tvedit.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        etProfileName.isEnabled = false
        etProfileEmail.isEnabled = false
        etProfileMobile.isEnabled = false
        profilefrg_uploadIDTv.isEnabled = false
        etUserAccountNumber.isEnabled = false
        etUserBankName.isEnabled = false
        etUserBankIFSC.isEnabled = false
        btnUpdateProfileDetails.visibility = View.INVISIBLE
        profilefrg_CircularImageView.isClickable = false
        profilefrg_CircularImageView.isEnabled = false
        profilefrg_UploadImage.isEnabled = false
        profilefrg_UploadImage.isClickable = false

        profilefrg_UploadImage.isGone = true
        btnRemoveImage.isGone = true

        profileViewModel.agentProfile()

        profilefrg_uploadIDTv.onRightDrawableClicked()
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        profileViewModel = ProfileViewModel()
    }

    override fun setupObserver() {
        profileViewModel.getAgentProfileDetailsResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            showProgress()
                        }
                    }
                    Status.SUCCESS -> {
                        dismissProgress()
                        isLoadingMain = false
                        if (it.data?.response != null) {

                            if (isUpdateDone) {
                                isUpdateDone = false
                                requireActivity().onBackPressed()
                            }

                            Glide.with(this)
                                .load(it.data.response.image)
                                .placeholder(R.drawable.ic_static_user)
                                .into(profilefrg_CircularImageView)

                            etProfileName.setText(it.data.response.name)
                            etProfileEmail.setText(it.data.response.email)
                            etProfileMobile.setText(it.data.response.phone)
                            profilefrg_uploadIDTv.setText(it.data.response.image)
                            etUserAccountNumber.setText(it.data.response.account_number)
                            etUserBankName.setText(it.data.response.bank_name)
                            etUserBankIFSC.setText(it.data.response.ifsc)
                            user_profileImg = it.data.response.image
                            user_email = it.data.response.email
                            user_name = it.data.response.name
                        }
                        container.isVisible = true
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
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
            })
        profileViewModel.getAgentProfileUpdateResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            showProgress()
                        }
                    }
                    Status.SUCCESS -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        imagesPassed = ""
                        isProfileChanged = true
                        isUpdateDone = true
                        profileViewModel.agentProfile()
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
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
            })

        profileViewModel.getAgentRemoveProfileImageResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            showProgress()
                            isLoadingMain = true
                        }
                    }
                    Status.SUCCESS -> {
                        Toaster.showToast(
                            requireContext(), it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        imagesPassed = ""
                        profileViewModel.agentProfile()
                        isProfileChanged = true
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
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
            })
    }

    override fun onClicks() {
        profilefrg_UploadImage.setOnClickListener {
            permissionsBuilder(Manifest.permission.CAMERA).build().send { result ->
                if (result.allGranted()) {
                    ImagePicker.with(this)
                        .crop()
                        .start()
                }
            }
        }

        btnRemoveImage.setOnClickListener {
            if (!user_profileImg.equals(null)) {
                profileViewModel.removeAgentProfileImage()
            }
        }

        btnUpdateProfileDetails.setOnClickListener {
            when {
                etProfileName.text.trim().toString().equals(null) -> {
                    Toaster.showToast(
                        requireContext(), getString(R.string.name_required),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                etProfileEmail.text.trim().toString().equals(null) -> {
                    Toaster.showToast(
                        requireContext(), getString(R.string.email_required),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                etProfileMobile.text.trim().toString().equals(null) -> {
                    Toaster.showToast(
                        requireContext(), getString(R.string.phone_required),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                etUserAccountNumber.text.trim().toString().equals(null) -> {
                    Toaster.showToast(
                        requireContext(), getString(R.string.accountnumber_required),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                etUserBankName.text.trim().toString().equals(null) -> {
                    Toaster.showToast(
                        requireContext(), getString(R.string.bankname_required),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                etUserBankIFSC.text.trim().toString().equals(null) -> {
                    Toaster.showToast(
                        requireContext(), getString(R.string.bankifsc_required),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                else -> {
                    profileViewModel.agentProfileUpdate(
                        requireContext(),
                        etProfileName.text.trim().toString(),
                        etProfileEmail.text.trim().toString(),
                        etProfileMobile.text.trim().toString(),
                        etUserAccountNumber.text.trim().toString(),
                        etUserBankName.text.trim().toString(),
                        etUserBankIFSC.text.trim().toString(),
                        imagesPassed
                    )
                }
            }
        }
        headerfrg_tvedit.setOnClickListener {
            if (editCheck) {
                profilefrg_UploadImage.isGone = false
                btnRemoveImage.isGone = false
                editCheck = false
                etProfileName.isEnabled = true
                etProfileEmail.isEnabled = true
                etProfileMobile.isEnabled = true
                profilefrg_uploadIDTv.isEnabled = false
                etUserAccountNumber.isEnabled = true
                etUserBankName.isEnabled = true
                etUserBankIFSC.isEnabled = true
                btnUpdateProfileDetails.visibility = View.VISIBLE
                profilefrg_CircularImageView.isClickable = true
                profilefrg_CircularImageView.isEnabled = true
                profilefrg_UploadImage.isClickable = true
                profilefrg_UploadImage.isEnabled = true
            } else {
                profilefrg_UploadImage.isGone = true
                btnRemoveImage.isGone = true
                editCheck = true
                etProfileName.isEnabled = false
                etProfileEmail.isEnabled = false
                etProfileMobile.isEnabled = false
                profilefrg_uploadIDTv.isEnabled = false
                etUserAccountNumber.isEnabled = false
                etUserBankName.isEnabled = false
                etUserBankIFSC.isEnabled = false
                btnUpdateProfileDetails.visibility = View.INVISIBLE
                profilefrg_CircularImageView.isClickable = false
                profilefrg_CircularImageView.isEnabled = false
                profilefrg_UploadImage.isClickable = false
                profilefrg_UploadImage.isEnabled = false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (data!!.clipData != null) {
                val imageUri = data.clipData!!.getItemAt(0).uri
                imagesPassed = imageUri?.path.toString()
                Glide.with(this).load(imageUri).into(profilefrg_CircularImageView)
            } else {
                val imageUri = data.data
                imagesPassed = imageUri?.path.toString()
                Glide.with(this).load(imageUri).into(profilefrg_CircularImageView)
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun EditText.onRightDrawableClicked() {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            profileViewModel.agentProfile()
        }
    }
}