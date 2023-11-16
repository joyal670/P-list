package com.iroid.patrickstore.ui.edit_profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityEditProfileBinding
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.preference.AppPreferences.email
import com.iroid.patrickstore.preference.AppPreferences.first_name
import com.iroid.patrickstore.preference.AppPreferences.last_name
import com.iroid.patrickstore.utils.*
import java.io.File
import java.lang.Exception


class EditProfileActivity : BaseActivity<EditProfileViewModal, ActivityEditProfileBinding>() {

    private var profileImageId=""

    override val layoutId: Int
        get() = R.layout.activity_edit_profile
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    private var imageFile: File? = null

    override fun getViewBinding(): ActivityEditProfileBinding {
        return ActivityEditProfileBinding.inflate(layoutInflater)
    }


    override fun initViews() {
        setSupportActionBar(binding.layoutToolbar.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.layoutToolbar.tvToolbarTitle.text = getString(R.string.profile)
        setUpObserver()
        setOnTextChanged()
        setOnClick()

        viewModel.viewProfile()
    }


    private fun setUpObserver() {
        /* view profile */
        viewModel.viewProfileLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    binding.etFirstName.setText(it.data!!.data.firstName)
                    binding.etLastName.setText(it.data.data.lastName)
                    binding.etEmail.setText(it.data.data.email)
                    binding.etMobile.setText(it.data.data.mobile)
                    try {
                        AppPreferences.image_url=it.data.data.profileImageId.publicUrl
                        CommonUtils.setImageBase(this,it.data.data.profileImageId.publicUrl,binding.ivProfileImage)
                    }catch (ex:Exception){
                        CommonUtils.setImageBase(this,"",binding.ivProfileImage)
                    }

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this,
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }

        /* update profile */
        viewModel.updateProfileLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    viewModel.viewProfile()
                    binding.etFirstName.setText(it.data!!.data.firstName)
                    binding.etLastName.setText(it.data.data.lastName)
                    binding.etEmail.setText(it.data.data.email)
                    binding.etMobile.setText(it.data.data.mobile)


                    first_name = it.data.data.firstName
                    last_name = it.data.data.lastName
                    email = it.data.data.email
                    AppPreferences.mobile = it.data.data.mobile
                    Toaster.showToast(this, it.data.msg, Toaster.State.SUCCESS, Toast.LENGTH_LONG)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this,
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }

        /* update profile picture */
        viewModel.updatePictureLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    profileImageId=it.data!!.data._id

                    Toaster.showToast(this, it.data!!.msg, Toaster.State.SUCCESS, Toast.LENGTH_LONG)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this,
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }

        viewModel.emptyFirstName?.observe(this, { message ->
            if (message != null)
                binding.tilFirstName.error = message
            else
                binding.tilFirstName.isErrorEnabled = false
        })

        viewModel.emptyLastName?.observe(this, { message ->
            if (message != null)
                binding.tilLastName.error = message
            else
                binding.tilLastName.isErrorEnabled = false
        })
        viewModel.emptyEmailId?.observe(this, { message ->
            if (message != null)
                binding.tilEmail.error = message
            else
                binding.tilEmail.isErrorEnabled = false
        })
        viewModel.emptyMobileNumber?.observe(this, { message ->
            if (message != null)
                binding.tilMobile.error = message
            else
                binding.tilMobile.isErrorEnabled = false
        })

    }

    private fun setOnTextChanged() {
        binding.etFirstName.doOnTextChanged { text, start, before, count ->
            viewModel.onFirstNameTextChanged(text.toString())
        }

        binding.etLastName.doOnTextChanged { text, start, before, count ->
            viewModel.onLastNameTextChanged(text.toString())
        }

        binding.etEmail.doOnTextChanged { text, start, before, count ->
            viewModel.onEmailIdTextChanged(text.toString())
        }
        binding.etMobile.doOnTextChanged { text, start, before, count ->
            viewModel.onMobileNumberTextChanged(text.toString())
        }

    }

    private fun setOnClick() {

        /* update btn */
        binding.btnRegister.setOnClickListener {
            viewModel.updateProfile(profileImageId

            )
        }

        /* profile image btn */
        binding.ivProfileUpdate.setOnClickListener {
            setUpImagePickUpDialog()
        }
    }

    /* BottomSheet */
    private fun setUpImagePickUpDialog() {
        /* setup bottom sheet dialog */
        val bottom = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)
        val bottomSheet: View = this.layoutInflater.inflate(R.layout.select_gallery, null)

        val ivCameraPick = bottomSheet.findViewById<ShapeableImageView>(R.id.ivCameraPick)
        val ivGalleryPick = bottomSheet.findViewById<ShapeableImageView>(R.id.ivgalleryPick)

        /* camera pick action */
        ivCameraPick.setOnClickListener {
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
                        .start(101)
                    bottom.dismiss()
                }
            }
        }
        /* gallery pick action */
        ivGalleryPick.setOnClickListener {
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
                        .start(102)
                    bottom.dismiss()
                }
            }
        }

        bottom.setContentView(bottomSheet)
        bottom.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                val fileUri = data?.data

                //for image file
                imageFile = null
                imageFile = ImagePicker.getFile(data)!!

                CommonUtils.setImageBase(this, imageFile.toString(), binding.ivProfileImage)

                /* Api call */
                viewModel.updateProfilePicture(imageFile!!, "customer_profile_image")

            }


            if (requestCode == 102) {
                val fileUri = data?.data

                //for image file
                imageFile = null
                imageFile = ImagePicker.getFile(data)!!

                CommonUtils.setImageBase(this, imageFile.toString(), binding.ivProfileImage)

                /* Api call */
                viewModel.updateProfilePicture(imageFile!!, "customer_profile_image")

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
        }

    }

}
