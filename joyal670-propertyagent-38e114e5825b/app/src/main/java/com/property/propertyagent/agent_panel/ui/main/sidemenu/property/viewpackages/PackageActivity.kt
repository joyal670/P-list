package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.viewpackages

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.viewpackages.adapter.PackageSlideAdapter
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.agent.agent_package_details.Package
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import com.shameem.projectstructure.listeners.ActivityListener
import kotlinx.android.synthetic.main.activity_package.*
import kotlinx.android.synthetic.main.toolbar_main.*

class PackageActivity : BaseActivity(), ActivityListener {

    private lateinit var packageViewModel: PackageViewModel

    private var passedPropertyId = ""

    override val layoutId: Int
        get() = R.layout.activity_package
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {

        setSupportActionBar(toolbar)
        tvToolbarTitle.text = getString(R.string.packages)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        passedPropertyId = intent.getStringExtra("property_id").toString()

        packageViewModel.agentPackages(passedPropertyId.toInt())
    }

    private fun funSelectedPackage(pos: Int) {
        Log.e("Selected pacage", pos.toString())
        val intent = Intent()
        intent.putExtra("statuscode", pos)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        packageViewModel = PackageViewModel()
    }

    override fun setupObserver() {
        packageViewModel.getAgentPackagesResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    if (it.data?.packages != null) {
                        if (it.data.packages.isNullOrEmpty()) {
                            llEmptyData.isVisible = true
                        } else {
                            setUpViewPager(it.data.packages)
                            container.isVisible = true
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
        })
    }

    private fun setUpViewPager(packages: List<Package>) {
        viewpagerPackage.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewpagerPackage.adapter =
            PackageSlideAdapter(this, packages) { pos -> funSelectedPackage(pos) }

        val pageMargin =
            resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
        val pageOffset =
            resources.getDimensionPixelOffset(R.dimen.offset).toFloat()
        viewpagerPackage.setPageTransformer { page, position ->
            val myOffset = position * -(2 * pageOffset + pageMargin)
            if (viewpagerPackage.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewpagerPackage) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -myOffset
                } else {
                    page.translationX = myOffset
                }
            } else {
                page.translationY = myOffset
            }
        }
        indicatorPackage.setViewPager2(viewpagerPackage)
    }

    override fun onClicks() {

    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            packageViewModel.agentPackages(passedPropertyId.toInt())
        }
    }
}