package com.property.propertyuser.ui.main.rating

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityRatingBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.home.dashboard.DashboardActivity
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.activity_rating.*

class RatingActivity : BaseActivity<ActivityRatingBinding>(), ActivityListener {
    private var dialog: Dialog? = null
    private lateinit var ratingViewModel: RatingViewModel
    private var passedPropertyIdRating = ""
    override val layoutId: Int
        get() = R.layout.activity_rating
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setTitle(getString(R.string.feedback))
        passedPropertyIdRating = intent.getStringExtra("passedPropertyIdRating").toString()
        Log.e("property_id", passedPropertyIdRating)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        ratingViewModel = RatingViewModel()
    }

    override fun setupObserver() {
        ratingViewModel.getRatingResponse().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showLoader()
                }
                Status.SUCCESS -> {
                    dismissLoader()
                    Log.e("responsecheck", Gson().toJson(it))
                    if (it.data != null) {
                        openFeedbackDialog()
                    }

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
        btnSubmitFeedback.setOnClickListener {
            if (rvFeedbackRating.rating.equals(0.0)) {
                Toaster.showToast(
                    this, getString(R.string.rating_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG
                )
            } else {
                ratingViewModel.fetchRatingDetails(
                    passedPropertyIdRating, rvFeedbackRating.rating.toString(),
                    etAdditionalDetails.text.toString()
                )
            }
        }
    }

    private fun openFeedbackDialog() {
        dialog = Dialog(this)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.layout_feedback_success)

        dialog?.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams

        Handler(Looper.getMainLooper()).postDelayed({
            dialog?.dismiss()
            startActivity(Intent(this, DashboardActivity::class.java))
            finishAffinity()
        }, 2000)

    }

    override fun getViewBinging(): ActivityRatingBinding =
        ActivityRatingBinding.inflate(layoutInflater)

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}