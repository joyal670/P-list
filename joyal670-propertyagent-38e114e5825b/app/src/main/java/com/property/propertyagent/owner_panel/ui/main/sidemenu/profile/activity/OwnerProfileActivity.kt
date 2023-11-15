package com.property.propertyagent.owner_panel.ui.main.sidemenu.profile.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.owner_panel.ui.main.sidemenu.profile.viewmodel.OwnerProfileViewmodel
import com.property.propertyagent.utils.AppPreferences.user_account_number
import com.property.propertyagent.utils.AppPreferences.user_branch_name
import com.property.propertyagent.utils.AppPreferences.user_email
import com.property.propertyagent.utils.AppPreferences.user_id_image
import com.property.propertyagent.utils.AppPreferences.user_ifsc
import com.property.propertyagent.utils.AppPreferences.user_name
import com.property.propertyagent.utils.AppPreferences.user_phone
import com.property.propertyagent.utils.AppPreferences.user_profileImg
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_profile_owner.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*
import java.io.File
import java.util.regex.Pattern

class OwnerProfileActivity : BaseActivity() {

    private lateinit var ownerProfileViewmodel : OwnerProfileViewmodel

    private val PICK_IMAGES_CODE1 = 0
    private val PICK_IMAGES_CODE2 = 1
    private val PICK_IMAGES_CODE3 = 3

    private var file1 : File? = null
    private var file2 : File? = null

    /* for edit button */
    private var editCheck = true

    override val layoutId : Int
        get() = R.layout.activity_profile_owner
    override val setToolbar : Boolean
        get() = true
    override val hideStatusBar : Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = getString(R.string.my_profile)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        loadIdData()

        ownerProfileViewmodel.ownerProfileDetails()

    }

    override fun setupViewModel() {
        ownerProfileViewmodel = OwnerProfileViewmodel(this)
    }

    override fun setupObserver() {
        ownerProfileViewmodel.ownerProfileData.observe(this , Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    user_name = it.data!!.response.name
                    user_email = it.data.response.email
                    user_phone = it.data.response.phone
                    user_account_number = it.data.response.account_number
                    user_branch_name = it.data.response.branch_name
                    user_ifsc = it.data.response.ifsc
                    user_id_image = it.data.response.id_image

                    ownerNameEtx.setText(user_name)
                    ownerEmailEtx.setText(user_email)
                    ownerPhoneEtx.setText(user_phone)
                    ownerAccountNumberEtx.setText(user_account_number)
                    ownerBankNameEtx.setText(user_branch_name)
                    ownerIFSCEtx.setText(user_ifsc)

                    if (it.data.response.id_image == null || it.data.response.id_image == "") {
                        viewBtn.visibility = View.GONE
                    }

                    Glide.with(this)
                        .load(it.data.response.profile_image)
                        .placeholder(R.drawable.ic_static_user)
                        .into(profilefrgowner_circularView)

                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    val dialog = InternetDialogFragment(1)
                    dialog.show(supportFragmentManager , "TAG")
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager , "TAG")
                    }
                }
            }
        })
        ownerProfileViewmodel.profileData.observe(this , Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this ,
                        getString(R.string.updated_successfully) ,
                        Toaster.State.SUCCESS ,
                        Toast.LENGTH_LONG
                    )

                    /* change ui state */
                    tvOwnerEdit.text = getString(R.string.edit)
                    editCheck = false
                    uploadIdChip.visibility = View.GONE
                    ownerEmailEtx.isEnabled = false
                    ownerPhoneEtx.isEnabled = false
                    uploadBtn.isEnabled = false
                    uploadBtn.isClickable = false
                    OwnerProfileUpdateBtn.visibility = View.INVISIBLE
                    frame.visibility = View.INVISIBLE

                    /* save data to shared preference */
                    user_name = it.data!!.response.name
                    user_email = it.data.response.email
                    user_phone = it.data.response.phone
                    user_profileImg = it.data.response.profile_image
                    user_account_number = it.data.response.account_number
                    user_branch_name = it.data.response.branch_name
                    user_ifsc = it.data.response.ifsc
                    user_id_image = it.data.response.id_image

                    /* load image and upload id from response */
                    Glide.with(this)
                        .load(it.data.response.profile_image)
                        .placeholder(R.drawable.ic_static_user)
                        .into(profilefrgowner_circularView)

                    if (it.data.response.id_image == null || it.data.response.id_image == "") {
                        viewBtn.visibility = View.GONE
                    }

                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this ,
                        it.message!! ,
                        Toaster.State.ERROR ,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager , "TAG")
                    }
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this ,
                        it.message!! ,
                        Toaster.State.ERROR ,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })
    }


    override fun onActivityResult(requestCode : Int , resultCode : Int , data : Intent?) {
        super.onActivityResult(requestCode , resultCode , data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGES_CODE1) {
                val fileUri = data?.data

                Glide.with(this)
                    .load(fileUri)
                    .placeholder(R.drawable.ic_static_user)
                    .into(profilefrgowner_circularView)

                //for file object
                file1 = ImagePicker.getFile(data)!!
            }
            if (requestCode == PICK_IMAGES_CODE2) {
                val fileUri = data?.data

                /* for file object*/
                file2 = ImagePicker.getFile(data)!!

                /* You can also get File Path from intent */
                val filePath2 : String? = ImagePicker.getFilePath(data)

                /* set string */
                uploadIdChip.visibility = View.VISIBLE
                uploadIdChip.text = filePath2
            }
            if (requestCode == PICK_IMAGES_CODE3) {
                /* get file path as string */
                val filePath : String? = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)

                /* converting to a file object */
                file2 = File(filePath)

                /* setting string */
                uploadIdChip.visibility = View.VISIBLE
                uploadIdChip.text = filePath
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this , ImagePicker.getError(data) , Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this , "Task Cancelled" , Toast.LENGTH_SHORT).show()
        }
    }

    override fun setupUI() {
        ownerNameEtx.setText(user_name)
        ownerEmailEtx.setText(user_email)
        ownerPhoneEtx.setText(user_phone)
        ownerAccountNumberEtx.setText(user_account_number)
        ownerBankNameEtx.setText(user_branch_name)
        ownerIFSCEtx.setText(user_ifsc)
        if (user_id_image == null || user_id_image == "") {
            viewBtn.visibility = View.GONE
        }

        Glide.with(this)
            .load(user_profileImg)
            .placeholder(R.drawable.ic_static_user)
            .into(profilefrgowner_circularView)
    }

    override fun onClicks() {

        tvOwnerEdit.setOnClickListener {
            /* for edit button */
            if (editCheck) {
                tvOwnerEdit.text = getString(R.string.editing)
                editCheck = false
                ownerEmailEtx.isEnabled = true
                ownerPhoneEtx.isEnabled = true
                uploadBtn.isEnabled = true
                uploadBtn.isClickable = true
                OwnerProfileUpdateBtn.visibility = View.VISIBLE
                frame.visibility = View.VISIBLE
            } else {
                tvOwnerEdit.text = getString(R.string.edit)
                editCheck = true
                ownerEmailEtx.isEnabled = false
                ownerPhoneEtx.isEnabled = false
                uploadBtn.isEnabled = false
                uploadBtn.isClickable = false
                OwnerProfileUpdateBtn.visibility = View.INVISIBLE
                frame.visibility = View.INVISIBLE
            }
        }

        uploadBtn.setOnClickListener {
            setupDialog()
        }

        profilefrgowner_imageUpload.setOnClickListener {
            permissionsBuilder(Manifest.permission.CAMERA).build().send { result ->
                if (result.allGranted()) {
                    ImagePicker.with(this)
                        .crop()        //Crop image(Optional), Check Customization for more option
                        .compress(1024)   //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(
                            1080 ,
                            1080
                        ) //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(PICK_IMAGES_CODE1)
                }
            }
        }

        OwnerProfileUpdateBtn.setOnClickListener {
            val tempEmail = ownerEmailEtx.text.toString()
            val tempPhone = ownerPhoneEtx.text.toString()
            if (tempEmail.isEmpty() || tempPhone.isEmpty()) {
                Toaster.showToast(
                    this ,
                    "Please fill all fields" ,
                    Toaster.State.ERROR ,
                    Toast.LENGTH_LONG
                )
            } else {
                ownerProfileViewmodel.updateProfile(tempEmail , tempPhone , file1 , file2)
            }
        }

        uploadIdChip.setOnClickListener {
            uploadIdChip.visibility = View.GONE
        }

        viewBtn.setOnClickListener {
            if (user_id_image == null || user_id_image == "") {
                Toaster.showToast(this ,
                    getString(R.string.file_not_found) ,
                    Toaster.State.WARNING ,
                    Toast.LENGTH_SHORT)
            } else {
                val builder = CustomTabsIntent.Builder()
                val colorInt : Int = Color.parseColor("#2F80ED")
                builder.setToolbarColor(colorInt)
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(this , Uri.parse(user_id_image))
            }
        }
    }

    /* setup bottom menu */
    @SuppressLint("InflateParams")
    private fun setupDialog() {
        val bottom = BottomSheetDialog(
            this@OwnerProfileActivity ,
            R.style.ThemeOverlay_App_BottomSheetDialog
        )
        val bottomSheet : View = this.layoutInflater.inflate(R.layout.select_gallery , null)

        val ivCameraPick = bottomSheet.findViewById<ShapeableImageView>(R.id.ivCameraPick)
        val ivgalleryPick = bottomSheet.findViewById<ShapeableImageView>(R.id.ivgalleryPick)
        val ivDocPick = bottomSheet.findViewById<ShapeableImageView>(R.id.ivDocPick)

        ivCameraPick.setOnClickListener {
            permissionsBuilder(Manifest.permission.CAMERA).build().send { result ->
                if (result.allGranted()) {
                    ImagePicker.with(this@OwnerProfileActivity)
                        .crop()
                        .cameraOnly()
                        .compress(1024)   //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(
                            1080 ,
                            1080
                        ) //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(PICK_IMAGES_CODE2)
                    bottom.dismiss()
                }
            }
        }

        ivgalleryPick.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.CAMERA ,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).build().send { result ->
                if (result.allGranted()) {
                    ImagePicker.with(this@OwnerProfileActivity)
                        .crop()
                        .galleryOnly()
                        .compress(1024)   //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(
                            1080 ,
                            1080
                        ) //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(PICK_IMAGES_CODE2)
                    bottom.dismiss()
                }
            }
        }

        ivDocPick.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.CAMERA ,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).build().send { result ->
                if (result.allGranted()) {
                    MaterialFilePicker()
                        .withActivity(this)
                        .withCloseMenu(true)
                        .withHiddenFiles(true)
                        .withFilter(Pattern.compile(".*\\.(pdf|doc)$"))
                        .withFilterDirectories(false)
                        .withTitle("Select file")
                        .withRequestCode(PICK_IMAGES_CODE3)
                        .start()
                    bottom.dismiss()
                }
            }
        }
        bottom.setContentView(bottomSheet)
        bottom.show()
    }

    override fun onResume() {
        super.onResume()
        loadIdData()
    }

    /* load id data if available */
    private fun loadIdData() {
        if (user_id_image == null || user_id_image == "") {
            viewBtn.visibility = View.GONE
        } else {
            viewBtn.visibility = View.VISIBLE
        }
    }
}