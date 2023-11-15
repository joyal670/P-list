package com.property.propertyuser.ui.main.side_menu.faq

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityFAQBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.faq.Faq
import com.property.propertyuser.modal.user_notification.Notification
import com.property.propertyuser.ui.main.side_menu.faq.adapter.FAQItemAdapter
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.addOnScrolledToEnd
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.activity_f_a_q.*
import kotlinx.android.synthetic.main.layout_no_network.*

class FAQActivity : BaseActivity<ActivityFAQBinding>(),ActivityListener {
    private var isLoading: Boolean = false
    private var page=0
    private var totalPageCount=0
    private var faqList=ArrayList<Faq>()
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var faqItemAdapter: FAQItemAdapter
    private lateinit var faqViewModel: FAQViewModel
    override val layoutId: Int
        get() = R.layout.activity_f_a_q
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setTitle(getString(R.string.faq))
        faqList=ArrayList()
        page=1
        faqViewModel.fetchFaqListDetail(page.toString())
    }

    override fun setupUI() {

    }
    private fun setFAQRecyclerView() {
        layoutManager= LinearLayoutManager(this)
        rvFAQListItems.layoutManager = layoutManager
        faqItemAdapter= FAQItemAdapter(this,faqList)
        rvFAQListItems.adapter =faqItemAdapter
        rvFAQListItems.addOnScrolledToEnd{
            if (!isLoading) {
                Log.e("end","reached")
                loadData()
                isLoading = true
            }
        }
    }
    fun loadData() {
        if(page<=totalPageCount && this.isConnected){
            faqList.add(Faq("",-1,""))
            faqItemAdapter.notifyItemInserted(faqList.size - 1)
            faqViewModel.fetchFaqListDetail(page.toString())
        }

    }
    override fun setupViewModel() {
        faqViewModel= FAQViewModel()
    }

    override fun setupObserver() {
        faqViewModel.getFaqListDetailsResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->{
                    if(faqList.size==0){
                        showLoader()
                    }
                }
                Status.SUCCESS->{
                    if(faqList.size==0){
                        dismissLoader()
                    }
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    if(it.data?.faq_data!=null){
                        totalPageCount=it.data.faq_data.total_page_count
                        if(faqList.size!=0){
                            isLoading = false
                            page+=1
                            faqList.removeAt(faqList.size - 1)
                            faqItemAdapter.notifyItemRemoved(faqList.size)
                            if(it.data?.faq_data.faqs.isNotEmpty()){
                                faqList.addAll(it.data!!.faq_data!!.faqs)
                                faqItemAdapter.notifyDataSetChanged()
                            }
                        }
                        else{
                            if(it.data?.faq_data.faqs.isNotEmpty()){
                                includeNoInternetFAQ.visibility= View.GONE
                                rvFAQListItems.visibility=View.VISIBLE
                                linearNoDataFoundFAQ.visibility=View.GONE
                                page += 1
                                faqList=it.data?.faq_data.faqs as ArrayList<Faq>
                                setFAQRecyclerView()
                            }
                            else{
                                linearNoDataFoundFAQ.visibility= View.VISIBLE
                                rvFAQListItems.visibility=View.GONE
                            }
                        }
                    }

                }
                Status.ERROR->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG)
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
                        includeNoInternetFAQ.visibility= View.VISIBLE
                        rvFAQListItems.visibility=View.GONE
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if(this.isConnected){
                page=1
                includeNoInternetFAQ.visibility= View.GONE
                rvFAQListItems.visibility=View.VISIBLE
                faqViewModel.fetchFaqListDetail(page.toString())
            }
        }
    }

    override fun getViewBinging(): ActivityFAQBinding {
        return ActivityFAQBinding.inflate(layoutInflater)
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}