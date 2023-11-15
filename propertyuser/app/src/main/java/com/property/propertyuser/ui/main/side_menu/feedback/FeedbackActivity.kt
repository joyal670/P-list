package com.property.propertyuser.ui.main.side_menu.feedback

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityFeedbackBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import it.sephiroth.android.library.xtooltip.Tooltip
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : BaseActivity<ActivityFeedbackBinding>(), ActivityListener {
    private lateinit var feedbackViewModel: FeedbackViewModel
    override val layoutId: Int
        get() = R.layout.activity_feedback
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setTitle(getString(R.string.feedback))
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        feedbackViewModel = FeedbackViewModel()
    }

    override fun setupObserver() {
        feedbackViewModel.getFeedbackResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    Toaster.showToast(
                        this, it.data!!.response,
                        Toaster.State.SUCCESS, Toast.LENGTH_LONG
                    )
                    etFeedBack.setText("")

                }
                Status.ERROR -> {
                    dismissLoader()
                    Toaster.showToast(
                        this, getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        this,
                        getString(R.string.data_empty),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this, getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnSendFeedback.setOnClickListener {
            if (etFeedBack.text.trim().toString().isNullOrEmpty()) {

                if (AppPreferences.chooseLanguage == "arabic") {
                    Toaster.showToast(
                        this,
                        getString(R.string.add_feedback),
                        Toaster.State.WARNING,
                        Toast.LENGTH_SHORT
                    )
                } else {
                    val tooltip = Tooltip.Builder(this)
                        .anchor(etFeedBack, 0, 0, true)
                        .text(getString(R.string.add_feedback))
                        .styleId(R.style.ToolTipAltStyle)
                        .arrow(true)
                        .floatingAnimation(Tooltip.Animation.DEFAULT)
                        .showDuration(1500)
                        .overlay(true)
                        .create()

                    tooltip
                        .doOnHidden { }
                        .doOnFailure { }
                        .doOnShown { }
                        .show(etFeedBack, Tooltip.Gravity.TOP, true)
                }

            } else {
                feedbackViewModel.sendFeedback(etFeedBack.text.trim().toString())
            }

        }
    }

    override fun getViewBinging(): ActivityFeedbackBinding {
        return ActivityFeedbackBinding.inflate(layoutInflater)
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}