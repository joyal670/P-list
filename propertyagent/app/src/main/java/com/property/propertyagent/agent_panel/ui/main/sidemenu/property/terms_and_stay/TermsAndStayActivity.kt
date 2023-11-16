package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.terms_and_stay

import android.widget.Toast
import androidx.core.view.isVisible
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.utils.AppPreferences.clicked_property_id
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_terms_and_stay.*
import kotlinx.android.synthetic.main.toolbar_main.*

@Suppress("UselessCallOnNotNull")
class TermsAndStayActivity : BaseActivity() {

    private lateinit var termsOfStayViewModel: TermsOfStayViewModel

    override val layoutId: Int
        get() = R.layout.activity_terms_and_stay
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {

        setSupportActionBar(toolbar)
        tvToolbarTitle.text = getString(R.string.termsofstay)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        termsOfStayViewModel.agentTermsOfStay(clicked_property_id.toInt())
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        termsOfStayViewModel = TermsOfStayViewModel()
    }

    override fun setupObserver() {
        termsOfStayViewModel.getAgentTermsOfStayResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    if (!it.data?.data!!.equals(null)) {
                        if (!it.data.data.terms_of_stay.isNullOrEmpty()) {
                            tvContent.text = it.data.data.terms_of_stay
                            container.isVisible = true
                        } else {
                            llEmptyData.isVisible = true
                        }
                    } else {
                        llEmptyData.isVisible = true
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)

                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.LOADING -> {
                    showProgress()
                }
            }
        }
    }

    override fun onClicks() {

    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            termsOfStayViewModel.agentTermsOfStay(clicked_property_id.toInt())
        }
    }
}