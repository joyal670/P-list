package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.myprofile


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.load
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.pojo.myprofile.MyProfile
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.ui.start_up.language_selection.LangaugeSelectionActivity
import com.proteinium.proteiniumdietapp.utils.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.edit_profile.*
import kotlinx.android.synthetic.main.toolbar_sub.*
import java.io.File


class MyProfileActivity : BaseActivity() {
    private lateinit var myProfileViewModel: MyProfileViewModel
    private lateinit var myProfile: MyProfile
    private var loadImageCode = "00"
    private var file: File? = null
    private lateinit var bottom: BottomSheetDialog
    private lateinit var bottomChange: BottomSheetDialog
    override val layoutId: Int
        get() = R.layout.activity_my_profile
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbarSub)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if(AppPreferences.chooseLanguage== Constants.ENGLISH_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_toolbar24)
        }
        if(AppPreferences.chooseLanguage==Constants.ARABIC_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvSubToolbarTitle.text = getString(R.string.tvMyProfile)
        if(AppPreferences.chooseLanguage== Constants.ENGLISH_LAG){
            tvChangePassword.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_change_password),null,getDrawable(R.drawable.ic_arrow_right),null)
            tvProfile.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_et_profile),null,getDrawable(R.drawable.ic_arrow_right),null)
        }
        if(AppPreferences.chooseLanguage== Constants.ARABIC_LAG){
            tvChangePassword.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_primary),null,getDrawable(R.drawable.ic_change_password),null)
            tvProfile.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_primary),null,getDrawable(R.drawable.ic_et_profile),null)
        }
        AppPreferences.user_id?.let { myProfileViewModel.fetchUserInfo(it) }
        btnDelete.setOnClickListener {
            showExitDialog1()
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        myProfileViewModel = ViewModelProviders.of(this).get(MyProfileViewModel::class.java)
    }

    override fun setupObserver() {
        myProfileViewModel.getChangePassword().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoader()
                    bottomChange.dismiss()
                    CommonUtils.warningToast(this, it.data!!.message)
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.data!!.message)
                }

            }
        })
        myProfileViewModel.getUserInfoResponse().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoader()
                    if(it.data!!.blocked == 1){
                        showExitDialog()
                    }

                    noInternetLayoutMyprofile.visibility = View.GONE
                    llMyprofileMain.visibility = View.VISIBLE
                    if (it.data?.status!!) {
                        if (it.data.data != null) {
                            myProfile = it.data.data
                            tvUserName.text = it.data.data.first_name+" "+
                                    it.data.data.middle_name+" "+
                                    it.data.data.last_name+" "
                            tvUserEmail.text = it.data.data.email
                            tvUserGender.text =
                                if (it.data.data.gender.equals("M")) getString(R.string.male) else getString(R.string.female)
                            tvUserMobile.text = it.data.data.phone
                            val media = it.data.data.image
                            myProfileImageView.load(media) {
                                placeholder(R.drawable.ic_profile)
                                error(R.drawable.ic_profile)
                            }

                        }
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    if (this.isConnected) {
                        CommonUtils.warningToast(this, getString(R.string.something_wrong))

                    } else {
                        noInternetLayoutMyprofile.visibility = View.VISIBLE
                        llMyprofileMain.visibility = View.GONE
                    }

                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.no_internet))
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.data_empty))
                }
            }
        })
        myProfileViewModel.getUpdateUserInfoResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    if (it.data?.status!!) {
                        myProfile = it.data.data
                        myProfileImageView.load(it.data.data.image) {
                            placeholder(R.drawable.ic_profile)
                            error(R.drawable.ic_image_not)
                        }
                        tvUserName.text = it.data.data.first_name+" "+
                                it.data.data.middle_name+" "+
                                it.data.data.last_name+" "
                        tvUserEmail.text = it.data.data.email
                        tvUserGender.text =
                            if (it.data.data.gender.equals("M")) "Male" else "Female"
                        tvUserMobile.text = it.data.data.phone
                        bottom.dismiss()
                        CommonUtils.warningToast(this, it.data.message.toString())
                    } else {
                        CommonUtils.warningToast(this, it.data.message.toString())
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.message.toString())
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.no_internet))
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.data_empty))
                }
            }
        })
    }

    private fun showExitDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_remove_an_item)

        val yesBtn = dialog.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
        val closeBtn = dialog.findViewById(R.id.ivRemoveItemClose) as ImageView
        val tvContent = dialog.findViewById(R.id.tvContent) as TextView
        tvContent.text = getString(R.string.block_message)

        yesBtn.setOnClickListener {
            dialog.dismiss()
            AppPreferences.logoutClearPreference()
            val intent= Intent(this, LangaugeSelectionActivity::class.java)
            startActivity(intent)
            finish()
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
            AppPreferences.logoutClearPreference()
            val intent= Intent(this, LangaugeSelectionActivity::class.java)
            startActivity(intent)
            finish()
        }


        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onClicks() {
        tvChangePassword.setOnClickListener {
            changePassword()
        }
        tvProfile.setOnClickListener {
            editProfileDetails()
        }
    }

    private fun changePassword() {
        bottomChange = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)
        val bottomSheet: View = this.layoutInflater.inflate(R.layout.change_password, null)

        val close = bottomSheet.findViewById<ImageView>(R.id.ivClose)
        val etNewPassword = bottomSheet.findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = bottomSheet.findViewById<EditText>(R.id.etConfirmPassword)
        val btnSubmit = bottomSheet.findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            when {
                etNewPassword.text.toString().isEmpty() -> {
                    CommonUtils.warningToast(this, getString(R.string.please_enter_password))
                }
                etConfirmPassword.text.toString().trim()!=etNewPassword.text.toString().trim()->{
                    CommonUtils.warningToast(this, getString(R.string.passwords_does_not_match))
                }
                etNewPassword.text.toString().trim().length<6 -> {
                    CommonUtils.warningToast(this, getString(R.string.password_length))

                }
                etConfirmPassword.text.toString().trim().length<6 -> {
                    CommonUtils.warningToast(this, getString(R.string.password_length))

                }
                else -> {
                    myProfileViewModel.changePassword(tvUserEmail.text.toString().trim(),
                        etNewPassword.text.toString().trim(),
                        etConfirmPassword.text.toString().trim())
                }
            }
        }
        close.setOnClickListener {
            bottomChange.dismiss()
        }

        bottomChange.setContentView(bottomSheet)
        bottomChange.show()

    }

    private fun editProfileDetails() {
        bottom = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)
        bottom.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        val bottomSheet: View = this.layoutInflater.inflate(R.layout.edit_profile, null)

        val close = bottomSheet.findViewById<ImageView>(R.id.ivCloseEdit)
        val submit = bottomSheet.findViewById<Button>(R.id.btnSubmitProfile)
        val fabCameraProfile = bottomSheet.findViewById<FloatingActionButton>(R.id.fabCameraProfile)
        val spProfileGender = bottomSheet.findViewById<EditText>(R.id.spProfileGender)

        val etFirstName = bottomSheet.findViewById<EditText>(R.id.etFirstName)
        val etMiddleName = bottomSheet.findViewById<EditText>(R.id.etMiddleName)
        val etLastName = bottomSheet.findViewById<EditText>(R.id.etLastName)
        val etProfileMobile = bottomSheet.findViewById<EditText>(R.id.etProfileMobile)
        val etProfileAlternativeMobile =
            bottomSheet.findViewById<EditText>(R.id.etProfileAlternativeMobile)
        val myProfileImageEdit = bottomSheet.findViewById<ImageView>(R.id.myProfileImageEdit)
        etFirstName.setText(myProfile.first_name)
        etMiddleName.setText(myProfile.middle_name)
        etLastName.setText(myProfile.last_name)
        spProfileGender.setText(if (myProfile.gender.equals("M", true)) getString(R.string.male) else  getString(R.string.female))
        etProfileMobile.setText(myProfile.phone)
        etProfileAlternativeMobile.setText(myProfile.alternative_phone)
        val media = myProfile.image
        if (media != null) {
            Log.e("media--", "inside $media")
            myProfileImageEdit.load(media) {
                placeholder(R.drawable.ic_profile)
                error(R.drawable.ic_profile)
            }
        } else {
            Log.e("media-- error", "inside $media")
            myProfileImageView.setImageResource(R.drawable.ic_profile)
        }

        spProfileGender.setOnClickListener {
            val bottom = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)
            val bottomSheet: View = this.layoutInflater.inflate(R.layout.select_gender, null)

            val maleBtn = bottomSheet.findViewById<RadioButton>(R.id.SelectGenderMale)
            val femaleBtn = bottomSheet.findViewById<RadioButton>(R.id.SelectGenderFemale)

            val close = bottomSheet.findViewById<ImageView>(R.id.ivCloseSelectGender)

            close.setOnClickListener {
                bottom.dismiss()
            }

            val selectBtn = bottomSheet.findViewById<MaterialButton>(R.id.SelectGenderBtn)
            if (myProfile.gender.equals("M", true)) maleBtn.isChecked =
                true else femaleBtn.isChecked = true
            selectBtn.setOnClickListener {
                if (maleBtn.isChecked) {
                    spProfileGender.setText(getString(R.string.male))
                    bottom.dismiss()
                } else if (femaleBtn.isChecked) {
                    spProfileGender.setText(getString(R.string.female))
                    bottom.dismiss()
                } else {
                    Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show()
                }
            }


            bottom.setContentView(bottomSheet)
            bottom.show()


        }

        close.setOnClickListener {
            bottom.dismiss()
        }
        submit.setOnClickListener {
            if (etFirstName.text.toString().trim().isBlank()) {
                CommonUtils.warningToast(this, getString(R.string.no_name_first))
            }
            else if (etMiddleName.text.toString().trim().isBlank()) {
                CommonUtils.warningToast(this, getString(R.string.no_name_middle))
            }
            else if (etLastName.text.toString().trim().isBlank()) {
                CommonUtils.warningToast(this, getString(R.string.no_name_last))
            }
            else if (spProfileGender.text.toString().trim().isNullOrEmpty()) {
                CommonUtils.warningToast(this, getString(R.string.select_gender))
            } else if (etProfileMobile.text.toString().trim().isNullOrEmpty()) {
                CommonUtils.warningToast(this, getString(R.string.invalid_number))
            }
            else if(!isValidMobile(etProfileMobile.text.trim().toString())){
                CommonUtils.warningToast(this, getString(R.string.invalid_number))

            }


            else if(!etProfileAlternativeMobile.text.toString().trim().isNullOrEmpty()){
                if(etProfileMobile.text.toString().trim()==etProfileAlternativeMobile.text.toString().trim()) {
                    CommonUtils.warningToast(this, getString(R.string.not_same_phone))

                }else if(!isValidMobile(etProfileAlternativeMobile.text.trim().toString())){
                    CommonUtils.warningToast(this, getString(R.string.invalid_alternate_number))

            }     else{
                    myProfileViewModel.fetchUpdateUserInfo(
                        AppPreferences.user_id!!, etFirstName.text.trim().toString(),
                        etMiddleName.text.trim().toString(),
                        etLastName.text.trim().toString(),
                      "M" +
                              "" +
                              "",
                        etProfileMobile.text.trim().toString(),
                        etProfileAlternativeMobile.text.trim().toString(), file
                    )
                }

            } else {
                myProfileViewModel.fetchUpdateUserInfo(
                    AppPreferences.user_id!!, etFirstName.text.trim().toString(),
                    etMiddleName.text.trim().toString(),
                    etLastName.text.trim().toString(),
                    if (spProfileGender.text.trim().toString().equals("Male", true)) "M" else "F",
                    etProfileMobile.text.trim().toString(),
                    etProfileAlternativeMobile.text.trim().toString(), file
                )
            }
        }
        fabCameraProfile.setOnClickListener {
            ImagePicker.with(this)
                .crop()        //Crop image(Optional), Check Customization for more option
                .compress(1024)   //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                ) //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
            loadImageCode = "09"
        }
        bottom.setContentView(bottomSheet)
        bottom.show()

    }

    fun selectGender(): String {
        var gender = ""
        val bottom = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)
        val bottomSheet: View = this.layoutInflater.inflate(R.layout.select_gender, null)

        val maleBtn = bottomSheet.findViewById<RadioButton>(R.id.SelectGenderMale)
        val femaleBtn = bottomSheet.findViewById<RadioButton>(R.id.SelectGenderFemale)

        val close = bottomSheet.findViewById<ImageView>(R.id.ivCloseSelectGender)
        close.setOnClickListener {
            bottom.dismiss()
        }

        val selectBtn = bottomSheet.findViewById<MaterialButton>(R.id.SelectGenderBtn)
        selectBtn.setOnClickListener {
            if (maleBtn.isChecked) {
                gender = getString(R.string.male)
                bottom.dismiss()
            } else if (femaleBtn.isChecked) {
                gender = getString(R.string.female)
                bottom.dismiss()
            } else {
                Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show()
            }
        }


        bottom.setContentView(bottomSheet)
        bottom.show()

        return gender
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (loadImageCode == "09") {
            if (resultCode == Activity.RESULT_OK) {
                val selectedImage: Uri = data?.data!!
                file = File(selectedImage.path)
                bottom.myProfileImageEdit.setImageURI(selectedImage)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showExitDialog1() {
        val dialog = Dialog(this) //  val dialog = Dialog(this)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.logout_layout)

        val close = dialog?.findViewById(R.id.ivCloseLogout) as ImageView
        val yesBtn = dialog.findViewById(R.id.okBtnLogout) as Button
        val cancelBtn = dialog.findViewById(R.id.cancelBtnLogout) as Button
        val tvMessage=dialog.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text=getString(R.string.are_you_sample)
        dialog.show()


        close.setOnClickListener {
            dialog.dismiss()
        }

        yesBtn.setOnClickListener {
            AppPreferences.logoutClearPreference()
            val intent = Intent(this, LangaugeSelectionActivity::class.java)
            startActivity(intent)
            finish()

        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.getWindow()?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}
