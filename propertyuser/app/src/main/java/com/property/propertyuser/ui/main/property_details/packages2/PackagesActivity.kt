package com.property.propertyuser.ui.main.property_details.packages2

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityPackage2Binding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.other_package.Package
import com.property.propertyuser.ui.main.property_details.PropertyDetailsActivity
import com.property.propertyuser.ui.main.property_details.packages2.adapter.PackageSlideAdapter
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.activity_package2.*

class PackagesActivity : BaseActivity<ActivityPackage2Binding>(), ActivityListener {

    private lateinit var packagesViewModel: PackagesViewModel
    private var  packagesList=ArrayList<Package>()
    private var passingPropertyId=""
    override val layoutId: Int
        get() = R.layout.activity_package2
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() =false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setTitle(getString(R.string.packages))
        packagesList=ArrayList()
        passingPropertyId=intent.getStringExtra("property_id").toString()
        Log.e("propertyid",passingPropertyId)
        packagesViewModel.fetchPackagesList(passingPropertyId)
        setupViewPagerForPackage()
    }

    private fun setupViewPagerForPackage() {
        viewpagerPackage.orientation= ViewPager2.ORIENTATION_HORIZONTAL
        viewpagerPackage.adapter= PackageSlideAdapter(this,packagesList){amount, selectedId, packageName ->funSelectedPackage(amount, selectedId, packageName)}
        viewpagerPackage.offscreenPageLimit=3
        val pageMargin =
            resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
        val pageOffset =
            resources.getDimensionPixelOffset(R.dimen.offset).toFloat()
        viewpagerPackage.setPageTransformer { page, position ->
            val myOffset = position * -(2 * pageOffset + pageMargin)
            if(viewpagerPackage.orientation==ViewPager2.ORIENTATION_HORIZONTAL){
                if(ViewCompat.getLayoutDirection(viewpagerPackage)==ViewCompat.LAYOUT_DIRECTION_RTL){
                    page.translationX=-myOffset
                }else{
                    page.translationX=myOffset
                }
            }
            else{
                page.translationY=myOffset
            }
        }
        indicatorPackage.setViewPager2(viewpagerPackage)
    }

    private fun funSelectedPackage(discountAmount: String, selectedId: Int, packageName: String) {
        val intent = Intent(this, PropertyDetailsActivity::class.java)
        intent.putExtra("discountAmount", discountAmount)
        intent.putExtra("selectedId", selectedId)
        intent.putExtra("selectedPackageName", packageName)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        packagesViewModel= PackagesViewModel()
    }

    override fun setupObserver() {
        packagesViewModel.getPackagesListLiveData().observe(this, Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("updateFav", Gson().toJson(it))
                    if(it.data!=null){
                        if(!(it.data.packages.isNullOrEmpty())){
                            linearNoDataFound.visibility= View.GONE
                            viewpagerPackage.visibility=View.VISIBLE
                            indicatorPackage.visibility=View.VISIBLE
                            packagesList=it.data.packages as ArrayList<Package>
                            setupViewPagerForPackage()
                        }
                        else{
                            linearNoDataFound.visibility= View.VISIBLE
                            viewpagerPackage.visibility=View.GONE
                            indicatorPackage.visibility=View.GONE
                        }
                    }
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,it.data!!.status.toString(), Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(this,getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }

            }
        })
    }

    override fun onClicks() {

    }

    override fun getViewBinging(): ActivityPackage2Binding {
        return ActivityPackage2Binding.inflate(layoutInflater)
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}