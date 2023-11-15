package com.property.propertyuser.ui.main.home.search_property

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivitySearchPropertyBinding
import com.property.propertyuser.dialogs.book_a_tour.BookATourDateAndTimeDialogFragment
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.property_list.Event
import com.property.propertyuser.modal.property_list.Property
import com.property.propertyuser.modal.testing.HomePropertyEventModel
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.home.adapter.TestMainRecyclerViewAdapter
import com.property.propertyuser.ui.main.map_and_nearby.MapAndNearByActivity
import com.property.propertyuser.ui.main.property_details.PropertyDetailsActivity
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.activity_search_property.*
import kotlinx.android.synthetic.main.layout_no_network.*
import kotlinx.android.synthetic.main.toolbar_search.*

class SearchPropertyActivity : BaseActivity<ActivitySearchPropertyBinding>(),ActivityListener {
    private lateinit var searchPropertyViewModel: SearchPropertyViewModel

    private var homePropertyEventModelList=ArrayList<HomePropertyEventModel>()
    private lateinit var newFragment: DialogFragment
    private val listEvent : List<Event> = listOf()
    private val listProperty : List<Property> = listOf()
    private var pageNo=0
    private var totalPageCount=0
    private var isLoading: Boolean = false
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var testMainRecyclerViewAdapter: TestMainRecyclerViewAdapter
    private var searchNameEditText=""
    private var searchLiveData = MutableLiveData<String>()


    override val layoutId: Int
        get() = R.layout.activity_search_property
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        homePropertyEventModelList= ArrayList()
    }
    private fun setPropertyItemListRecyclerViewTest() {
        layoutManager = LinearLayoutManager(this)
        rvSearchPropertyList.layoutManager = layoutManager
        testMainRecyclerViewAdapter=TestMainRecyclerViewAdapter(this,homePropertyEventModelList,
            { i: Int, s: String ->
                val intent= Intent(this, PropertyDetailsActivity::class.java)
                intent.putExtra(Constants.INTENT_PROPERTY_ID,i)
                startActivity(intent)
            },
            { propertyId ->openMapActivity(propertyId) },
            {showDateAndTimeDialog(it)},{it,pos->addToFav(it,pos)},{it,pos->removeFromFav(it,pos)})
        rvSearchPropertyList.adapter = testMainRecyclerViewAdapter

        rvSearchPropertyList.addOnScrolledToEnd{
            if (!isLoading) {
                Log.e("end","reached")
                loadData()
                isLoading = true
            }
        }
    }
    private fun openMapActivity(propertyId: Int) {
        val intent=Intent(this, MapAndNearByActivity::class.java)
        intent.putExtra("property_id",propertyId.toString())
        intent.putExtra("from_type","home_property_list")
        startActivity(intent)
    }
    private fun removeFromFav(remFav: Int,pos:Int) {
        Log.e("remove", remFav.toString())
        searchPropertyViewModel.updateFavoriteProperty(remFav)
    }

    private fun addToFav(addFav: Int,pos:Int) {
        Log.e("addToFav", addFav.toString())
        searchPropertyViewModel.updateFavoriteProperty(addFav)
    }

    private fun showDateAndTimeDialog(it: Int) {
        var isLargeLayout = resources.getBoolean(R.bool.large_layout)
        val fragmentManager =supportFragmentManager
        BookATourDateAndTimeDialogFragment.newInstance(it.toString())
        newFragment = BookATourDateAndTimeDialogFragment(this)
        if (!isLargeLayout) {
            newFragment.show(fragmentManager, "dialog")
        } else {
            val transaction = fragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction
                .add(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
        }
    }
    private fun loadData() {
        if(pageNo<=totalPageCount && this.isConnected){
            pageNo+= 1
            homePropertyEventModelList.add(HomePropertyEventModel(3,listEvent,
                Property(-1,-1,"",-1,-1,-1,-1,
                    emptyList(),-1,-1,-1,-1,"",-1,
                    "","",-1,"","","",-2)))
            testMainRecyclerViewAdapter.notifyItemInserted(homePropertyEventModelList.size-1)
            searchPropertyViewModel.fetchSearchNamePropertyList(
                pageNo.toString(),searchNameEditText)
        }
    }
    override fun setupUI() {

    }

    override fun setupViewModel() {
        searchPropertyViewModel= SearchPropertyViewModel()
    }

    override fun setupObserver() {
        searchPropertyViewModel.getSearchNamePropertyListLiveData().observe(this, Observer {
            when(it.status){
                Status.LOADING->
                    if(homePropertyEventModelList.size==0){
                        showLoader()
                    }
                Status.SUCCESS->{
                    if(homePropertyEventModelList.size==0){
                        dismissLoader()
                    }
                    if(it.data!=null){
                        if(it.data.property_data!=null){
                            totalPageCount=it.data.property_data.total_page_count
                            if(homePropertyEventModelList.size!=0){
                                isLoading = false
                                homePropertyEventModelList.removeAt(homePropertyEventModelList.size-1)
                                testMainRecyclerViewAdapter.notifyItemRemoved(homePropertyEventModelList.size)
                                if(!(it.data.property_data.properties.isNullOrEmpty())){
                                    for(j in it.data.property_data.properties.indices){
                                        homePropertyEventModelList.
                                        add(HomePropertyEventModel(1,listEvent,it.data.property_data.properties[j]))
                                    }
                                    testMainRecyclerViewAdapter.notifyDataSetChanged()
                                }
                            }
                            else{
                                if(!(it.data.property_data.properties.isNullOrEmpty())){
                                    includeNoInternetSearch.visibility=View.GONE
                                    linearNoPropertyFoundSearch.visibility=View.GONE
                                    linearSearchMain.visibility= View.VISIBLE
                                    for(j in it.data.property_data.properties.indices){
                                        homePropertyEventModelList.
                                        add(HomePropertyEventModel(1,listEvent,it.data.property_data.properties[j]))
                                    }
                                    setPropertyItemListRecyclerViewTest()
                                }
                                else{
                                    /*searchMainConstraint.snackWhite(getString(R.string.no_property_found))*/
                                    linearNoPropertyFoundSearch.visibility=View.VISIBLE
                                    linearSearchMain.visibility= View.GONE
                                }
                            }
                        }
                    }
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.data_empty),
                        Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR,Toast.LENGTH_LONG)
                    }else{
                        includeNoInternetSearch.visibility=View.VISIBLE
                        linearSearchMain.visibility=View.GONE
                        linearNoPropertyFoundSearch.visibility=View.GONE
                    }
                }

            }
        })
        searchPropertyViewModel.updateFavoritePropertyResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("updateFav", Gson().toJson(it))
                    AppPreferences.reload_property_list=true
                    searchMainConstraint.snack(it.data!!.response)
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,it.data!!.response, Toaster.State.ERROR,Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR,Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(this,getString(R.string.no_internet),
                            Toaster.State.ERROR,Toast.LENGTH_LONG)
                    }
                }

            }
        })

        searchLiveData.observe(this){
            if(it.isNullOrEmpty()){
                ivSearchClear.visibility = View.GONE
            }else {
                ivSearchClear.visibility = View.VISIBLE
            }
        }
    }

    override fun onClicks() {
        ivBackSearch.setOnClickListener {
            onBackPressed()
        }
        etSearchProperty.setOnEditorActionListener { searchItem:TextView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchNameEditText=searchItem.text.trim().toString()
                Log.e("searchItem=",searchItem.text.toString())
                homePropertyEventModelList= ArrayList()
                pageNo=1
                searchPropertyViewModel.fetchSearchNamePropertyList(
                    pageNo.toString(),searchNameEditText)
            }
            true
        }
        ivSearchClear.setOnClickListener {
            etSearchProperty.text.clear()
        }
        btnTryAgain.setOnClickListener {
            if(this.isConnected){
                includeNoInternetSearch.visibility=View.GONE
                linearSearchMain.visibility=View.VISIBLE
            }
        }
        etSearchProperty.doOnTextChanged { text, start, before, count ->
            searchLiveData.value = text.toString()
        }
    }

    override fun getViewBinging(): ActivitySearchPropertyBinding {
        return ActivitySearchPropertyBinding.inflate(layoutInflater)
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}