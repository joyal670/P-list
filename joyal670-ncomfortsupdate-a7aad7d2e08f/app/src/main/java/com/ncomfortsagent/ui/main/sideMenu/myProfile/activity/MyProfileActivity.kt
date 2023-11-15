package com.ncomfortsagent.ui.main.sideMenu.myProfile.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseActivity
import com.ncomfortsagent.databinding.ActivityMyProfileBinding
import com.ncomfortsagent.databinding.SelectGalleryBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.ui.main.sideMenu.myProfile.viewmodel.ProfileViewModel
import com.ncomfortsagent.utils.AppPreferences.prefProfileImage
import com.ncomfortsagent.utils.AppPreferences.prefUserEmail
import com.ncomfortsagent.utils.AppPreferences.prefUserName
import com.ncomfortsagent.utils.CommonUtils.Companion.showCookieBar
import com.ncomfortsagent.utils.Status
import com.ncomfortsagent.utils.isConnected
import java.io.File

class MyProfileActivity : BaseActivity<ActivityMyProfileBinding>() {

    private lateinit var profileViewModel: ProfileViewModel
    private val cameraPickCode = 100
    private val galleryPickCode = 101
    private var imageFile: File? = null
    private var profileImageUrl = ""

    override val layoutId: Int
        get() = R.layout.activity_my_profile
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityMyProfileBinding =
        ActivityMyProfileBinding.inflate(layoutInflater)

    override fun initData() {

        setSupportActionBar(binding.tool.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.tool.tvToolbarTitle.text = getString(R.string.my_profile)

        binding.swipeToRefresh.isRefreshing = false
        profileViewModel.profile()

    }

    override fun fragmentLaunch() {

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        profileViewModel = ProfileViewModel(this)
    }

    override fun setupObserver() {

        /* profile details api */
        profileViewModel.getAgentProfileResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    Glide.with(this).load(it.data!!.data.profile_pic)
                        .placeholder(R.drawable.ic_profile_user).into(binding.ivProfileImg)
                    binding.etName.setText(it.data.data.name)
                    binding.etEmail.setText(it.data.data.email)
                    binding.etPhone.setText(it.data.data.phone)
                    binding.etAddress.setText(it.data.data.address)
                    profileImageUrl = it.data.data.profile_pic

                    /* save data to preferences */
                    prefUserName = it.data.data.name
                    prefUserEmail = it.data.data.email
                    prefProfileImage = it.data.data.profile_pic

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        showCookieBar(
                            this,
                            getString(R.string.error),
                            getString(R.string.something_wrong),
                            R.color.pomegranate
                        )

                    } else {
                        val dialog = InternetDialogFragment(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
            }
        })

        /* Profile image remove api */
        profileViewModel.getAgentProfileImageRemoveResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    prefProfileImage = ""
                    showCookieBar(
                        this,
                        getString(R.string.success),
                        it.data!!.response,
                        R.color.de_york
                    )

                    initData()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        showCookieBar(
                            this,
                            getString(R.string.error),
                            getString(R.string.something_wrong),
                            R.color.pomegranate
                        )

                    } else {
                        val dialog = InternetDialogFragment(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
            }
        })

        /* profile update api */
        profileViewModel.getAgentProfileUpdateResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    showCookieBar(
                        this,
                        getString(R.string.success),
                        it.data!!.response,
                        R.color.de_york
                    )

                    initData()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        showCookieBar(
                            this,
                            getString(R.string.error),
                            getString(R.string.something_wrong),
                            R.color.pomegranate
                        )

                    } else {
                        val dialog = InternetDialogFragment(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
            }
        })

    }

    override fun onClicks() {

        /* remove profile image */
        binding.removeImgBtn.setOnClickListener {
            profileViewModel.profileImageRemove()
        }

        /* profile update */
        binding.btnUpdate.setOnClickListener {
            updateProfile()
        }

        /* profile image upload */
        binding.changeImgBtn.setOnClickListener {
            setUpImagePickerDialog()
        }

        binding.swipeToRefresh.setOnRefreshListener {
            refreshUi()
        }
    }

    /* Remove current data */
    private fun refreshUi() {

        Glide.with(this).load(R.drawable.ic_profile_user).into(binding.ivProfileImg)
        binding.etName.text.clear()
        binding.etEmail.text.clear()
        binding.etPhone.text.clear()
        binding.etAddress.text.clear()
        initData()

    }

    /* Bottom sheet dialog for image picker */
    @SuppressLint("InflateParams")
    private fun setUpImagePickerDialog() {
        val bottom = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)

        val bottomSheetBinding = SelectGalleryBinding.inflate(layoutInflater)

        /* camera pick action */
        bottomSheetBinding.ivCameraPick.setOnClickListener {
            permissionsBuilder(Manifest.permission.CAMERA).build().send { result ->
                if (result.allGranted()) {
                    ImagePicker.with(this)
                        .crop()
                        .cameraOnly()
                        .compress(1024)
                        .maxResultSize(
                            1080,
                            1080
                        )
                        .start(cameraPickCode)
                    bottom.dismiss()
                }
            }
        }

        /* gallery pick action */
       bottomSheetBinding.ivgalleryPick.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).build().send { result ->
                if (result.allGranted()) {
                    ImagePicker.with(this)
                        .crop()
                        .galleryOnly()
                        .compress(1024)
                        .maxResultSize(
                            1080,
                            1080
                        )
                        .start(galleryPickCode)
                    bottom.dismiss()
                }
            }
        }

        bottom.setContentView(bottomSheetBinding.root)
        bottom.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == cameraPickCode) {

                imageFile = null
                imageFile = ImagePicker.getFile(data)!!
                Glide.with(this).load(imageFile).into(binding.ivProfileImg)

            }

            if (requestCode == galleryPickCode) {

                imageFile = null
                imageFile = ImagePicker.getFile(data)!!
                Glide.with(this).load(imageFile).into(binding.ivProfileImg)

            }
            if (resultCode == ImagePicker.RESULT_ERROR) {
                showCookieBar(
                    this,
                    getString(R.string.error),
                    ImagePicker.getError(data),
                    R.color.pomegranate
                )
            }
        }

    }

    /* profile update */
    private fun updateProfile() {
        val name = binding.etName.text.trim().toString()
        val email = binding.etEmail.text.trim().toString()
        val phone = binding.etPhone.text.trim().toString()
        val address = binding.etAddress.text.trim().toString()

        if (name.isBlank() || email.isBlank() || phone.isBlank() || address.isBlank()) {
            if (name.isBlank()) {
                binding.etName.error = getString(R.string.nameisrequired)
            }
            if (email.isBlank()) {
                binding.etEmail.error = getString(R.string.emalisrequired)
            }
            if (phone.isBlank()) {
                binding.etPhone.error = getString(R.string.Phonenumberisrequired)
            }
            if (address.isBlank()) {
                binding.etAddress.error = getString(R.string.addressisrequired)
            }
        } else {
            profileViewModel.profileUpdate(name, email, address, phone, imageFile)
        }
    }
}