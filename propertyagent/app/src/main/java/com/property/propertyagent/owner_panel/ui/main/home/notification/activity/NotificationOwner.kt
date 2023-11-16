package com.property.propertyagent.owner_panel.ui.main.home.notification.activity

import android.widget.Toast
import androidx.lifecycle.Observer
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.owner_panel.ui.main.home.notification.viewmodel.OwnerNotificationViewModel
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.layout_notification.*

class NotificationOwner : BaseActivity() {

    private lateinit var ownerNotificationViewModel : OwnerNotificationViewModel
    private var selectedId = ""
    private var title = ""
    private var description = ""

    override val layoutId : Int
        get() = R.layout.layout_notification
    override val setToolbar : Boolean
        get() = false
    override val hideStatusBar : Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        selectedId = intent.extras!!.get("id").toString()
        title = intent.extras!!.get("title").toString()
        description = intent.extras!!.get("des").toString()

        ownerNotificationViewModel.notificationUpdate(selectedId)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        ownerNotificationViewModel = OwnerNotificationViewModel()
    }

    override fun setupObserver() {
        ownerNotificationViewModel.getOwnerNotificationUpdateResponse().observe(this , Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    tvTitle.text = title
                    tvDes.text = description
                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this ,
                        getString(R.string.data_empty) ,
                        Toaster.State.ERROR ,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this , getString(R.string.something_wrong) ,
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
                        this , getString(R.string.internal_server_error) ,
                        Toaster.State.ERROR , Toast.LENGTH_LONG
                    )
                }
            }
        })
    }

    override fun onClicks() {

        ivClose.setOnClickListener {
            super.onBackPressed()
        }
    }
}