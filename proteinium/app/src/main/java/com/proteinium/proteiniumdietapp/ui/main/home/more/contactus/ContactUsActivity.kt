package com.proteinium.proteiniumdietapp.ui.main.home.more.contactus

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.user_email
import com.proteinium.proteiniumdietapp.preference.AppPreferences.user_phone
import com.proteinium.proteiniumdietapp.ui.start_up.auth.AuthActivity
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.Status
import com.proteinium.proteiniumdietapp.utils.isEmailValid
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.toolbar_sub.*
import java.net.URLEncoder


class ContactUsActivity : BaseActivity() {
    private lateinit var contactUsViewModel: ContactUsViewModel
    override val layoutId: Int
        get() = R.layout.activity_contact_us
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbarSub)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if(AppPreferences.chooseLanguage==Constants.ENGLISH_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_toolbar24)
        }
        if(AppPreferences.chooseLanguage==Constants.ARABIC_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvSubToolbarTitle.text = getString(R.string.tvContactUs)

        etcontactPhoneNumber.setText(user_phone)
        etcontactEmail.setText(user_email)

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        contactUsViewModel = ViewModelProviders.of(this).get(ContactUsViewModel::class.java)
    }

    override fun setupObserver() {
        contactUsViewModel.getUserInfoResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    if (it.data?.status!!) {
                        showSuccessMessage(it.data.message)
                    } else {
                        CommonUtils.warningToast(this, it.data.message)
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.message!!)
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

    override fun onClicks() {
        contactSubmitBtn.setOnClickListener {
            if (AppPreferences.isLogin) {
                if (validation()) {
                    contactUsViewModel.addCcontactUs(
                        AppPreferences.user_id!!,
                        etcontactName.text.toString().trim(), etcontactPhoneNumber.text.toString(),
                        etcontactEmail.text.toString().trim(), etcontactMessage.text.toString().trim()
                    )
                }
            } else {
                val intent = Intent(this, AuthActivity::class.java)
                intent.putExtra(Constants.INTENT_TYPE, Constants.TYPE_GUEST)
                startActivity(intent)
            }


        }
        tvWhatsApp.setOnClickListener {
            val packageManager: PackageManager = packageManager
            val i = Intent(Intent.ACTION_VIEW)

            try {
                val url =
                    "https://api.whatsapp.com/send?phone=" +96550903333 + "&text=" + URLEncoder.encode(
                        "Hai",
                        "UTF-8"
                    )
                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun showSuccessMessage(message: String) {
        val dialog = Dialog(this)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.alert_remove_an_item)

        val yesBtn = dialog?.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
        val closeBtn = dialog?.findViewById(R.id.ivRemoveItemClose) as ImageView
        val tvContent=dialog?.findViewById(R.id.tvContent) as TextView
        tvContent.text=message

        yesBtn.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    private fun validation(): Boolean {

        if (etcontactName.text.toString().trim().isNullOrEmpty()) {
            CommonUtils.warningToast(this, getString(R.string.no_name))
            return false
        } else if (etcontactPhoneNumber.text.toString().trim().isNullOrEmpty()) {
            CommonUtils.warningToast(this, getString(R.string.no_number))
            return false
        } else if (etcontactEmail.text.toString().trim().isNullOrEmpty()) {
            CommonUtils.warningToast(this, getString(R.string.empty_email))
            return false
        }
        else if(!etcontactEmail.text.toString().trim().isEmailValid()){
            CommonUtils.warningToast(this, getString(R.string.invalid_email))
            return false
        }
        else if (etcontactMessage.text.toString().trim().isNullOrEmpty()) {
            CommonUtils.warningToast(this, getString(R.string.no_message))
            return false
        } else {
            return true
        }
    }
}