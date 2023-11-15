package com.property.propertyuser.ui.main.terms_of_stay

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityAuthBinding
import com.property.propertyuser.databinding.ActivityTermsofStayBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.terms_of_stay.adapter.TermsofStayAdapter
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.activity_termsof_stay.*

class TermsofStayActivity : BaseActivity<ActivityTermsofStayBinding>(),ActivityListener {

    private lateinit var termsOfStayViewModel: TermsofStayViewModel
    private var passedPropertyId=""
    override fun getViewBinging(): ActivityTermsofStayBinding = ActivityTermsofStayBinding.inflate(layoutInflater)
    override val layoutId: Int
        get() = R.layout.activity_termsof_stay
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
      setTitle(getString(R.string.termsofstay))
        passedPropertyId=intent.getStringExtra("property_id").toString()
        termsOfStayViewModel.fetchTermsOfStay(passedPropertyId)
        //setTermsStayRecyclerView()
    }

    private fun setTermsStayRecyclerView() {
        rvTermsOfStayList.layoutManager = LinearLayoutManager(this)
        rvTermsOfStayList.adapter =TermsofStayAdapter(this)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        termsOfStayViewModel= TermsofStayViewModel()
    }

    override fun setupObserver() {
        termsOfStayViewModel.getTermsOfStayResponse().observe(this, Observer {
            when(it.status){
                Status.LOADING->{
                    showLoader()
                }
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responsecheck", Gson().toJson(it))
                    if(it.data!=null){
                        if(it.data.data!=null){
                            linearNoPropertyFoundTerms.visibility= View.GONE
                            constraintTermsOfStay.visibility=View.VISIBLE
                            wvTermsOfStay.settings.loadWithOverviewMode = true
                            wvTermsOfStay.requestFocus()
                            wvTermsOfStay.settings.allowFileAccess = true
                            wvTermsOfStay.settings.javaScriptEnabled = true
                            wvTermsOfStay.
                            loadData(it.data.data.terms_of_stay, "text/html", "UTF-8")
                        }
                        else{
                            linearNoPropertyFoundTerms.visibility= View.VISIBLE
                            constraintTermsOfStay.visibility=View.GONE
                        }
                    }

                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.data_empty), Toaster.State.ERROR, Toast.LENGTH_LONG)
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

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
       initializeToolbar(title)
    }

}