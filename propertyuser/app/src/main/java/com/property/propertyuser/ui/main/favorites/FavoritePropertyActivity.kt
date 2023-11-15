package com.property.propertyuser.ui.main.favorites

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityFavoritePropertyBinding
import com.property.propertyuser.dialogs.book_a_tour.BookATourDateAndTimeDialogFragment
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.favorite_list.FaviourateProperty
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.favorites.adapter.FavoritePropertyItemAdapter
import com.property.propertyuser.ui.main.map_and_nearby.MapAndNearByActivity
import com.property.propertyuser.ui.main.property_details.PropertyDetailsActivity
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.activity_favorite_property.*
import kotlinx.android.synthetic.main.layout_no_network.*
import kotlinx.android.synthetic.main.list_home_property_item.*

class FavoritePropertyActivity : BaseActivity<ActivityFavoritePropertyBinding>(),ActivityListener {
    private lateinit var favoritePropertyViewModel: FavoritePropertyViewModel
    override fun getViewBinging(): ActivityFavoritePropertyBinding = ActivityFavoritePropertyBinding.inflate(layoutInflater)
    private lateinit var newFragment: DialogFragment
    private var propertyData= ArrayList<FaviourateProperty>()
    private var passedPosition=-1
    private var isLoading: Boolean = false
    private var i=0
    private var totalPageCount=0
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var favoriteHomePropertyItemAdapter: FavoritePropertyItemAdapter
    override val layoutId: Int
        get() = R.layout.activity_favorite_property
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        i=1
        setTitle(getString(R.string.favoriteProperty))
        propertyData= ArrayList()
        favoritePropertyViewModel.fetchFavoriteProperty(i,1)
    }

    private fun setPropertyItemListRecyclerView() {
        layoutManager=LinearLayoutManager(this)
        rvFavoritesPropertyList.layoutManager = layoutManager
        favoriteHomePropertyItemAdapter=FavoritePropertyItemAdapter(this,propertyData,
            { i: Int, s: String ->
                val intent=Intent(this,PropertyDetailsActivity::class.java)
                intent.putExtra(Constants.INTENT_PROPERTY_ID,i)
                intent.putExtra("propertyType",s)
                startActivity(intent)
            },
            { mapPropertyId -> openMapActivity(mapPropertyId) },
            {it->showDateAndTimeDialog(it)},{it,pos->addToFav(it,pos)},{it1,pos1->removeFromFav(it1,pos1)})
        rvFavoritesPropertyList.adapter = favoriteHomePropertyItemAdapter

        rvFavoritesPropertyList.addOnScrolledToEnd{
            if (!isLoading) {
                Log.e("end","reached")
                loadData()
                isLoading = true
            }
        }
    }

    private fun openMapActivity(mapPropertyId: Int) {
        val intent=Intent(this, MapAndNearByActivity::class.java)
        intent.putExtra("property_id",mapPropertyId.toString())
        intent.putExtra("from_type","home_property_list")
        startActivity(intent)
    }

    private fun loadData() {
        if(i<=totalPageCount && this.isConnected){
            propertyData.add(FaviourateProperty(-1,-1,-1,-1,
                -1,-1, emptyList(), -1,-1,-1,-1,"",
                -1, "","", -1,"",
                "","",""))
            favoriteHomePropertyItemAdapter.notifyItemInserted(propertyData.size-1)
            favoritePropertyViewModel.fetchFavoriteProperty(i,1)
        }

    }
    private fun removeFromFav(it: Int,pos1:Int) {
        passedPosition=pos1
        favoritePropertyViewModel.updateFavoriteProperty(it)
    }

    private fun addToFav(it: Int,pos:Int) {
        passedPosition=pos
        favoritePropertyViewModel.updateFavoriteProperty(it)
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
    override fun setupUI() {

    }

    override fun setupViewModel() {
        favoritePropertyViewModel= FavoritePropertyViewModel()
    }

    override fun setupObserver() {
        favoritePropertyViewModel.getFavoritePropertyListResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->{
                    if(propertyData.size==0){
                        showLoader()
                    }
                }
                Status.SUCCESS->{
                    if(propertyData.size==0){
                        dismissLoader()
                    }
                    if(it.data!=null){
                        if(it.data.property_data!=null){
                            totalPageCount=it.data.property_data.total_page_count
                            if(propertyData.size!=0){
                                isLoading = false
                                i+=1
                                propertyData.removeAt(propertyData.size - 1)
                                favoriteHomePropertyItemAdapter.notifyItemRemoved(propertyData.size)
                                if(!(it.data!!.property_data.faviourate_properties.isNullOrEmpty())){
                                    propertyData.addAll(it.data!!.property_data!!.faviourate_properties as ArrayList<FaviourateProperty>)
                                    favoriteHomePropertyItemAdapter.notifyDataSetChanged()
                                }
                            }else{
                                i+=1
                                if(!(it.data!!.property_data.faviourate_properties.isNullOrEmpty())){
                                    includeNoInternetFavorite.visibility=View.GONE
                                    linearNoPropertyFoundFavorite.visibility= View.GONE
                                    rvFavoritesPropertyList.visibility=View.VISIBLE
                                    propertyData=it.data.property_data.faviourate_properties as ArrayList<FaviourateProperty>
                                    setPropertyItemListRecyclerView()
                                }
                                else{
                                    linearNoPropertyFoundFavorite.visibility= View.VISIBLE
                                    rvFavoritesPropertyList.visibility=View.GONE
                                }
                            }
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
                        includeNoInternetFavorite.visibility=View.VISIBLE
                        rvFavoritesPropertyList.visibility=View.GONE
                        linearNoPropertyFoundFavorite.visibility=View.GONE
                    }
                }

            }
        })
        favoritePropertyViewModel.updateFavoritePropertyResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->{
                    //showLoader()
                    progressBarFavorite.visibility=View.VISIBLE
                }
                Status.SUCCESS->{
                   // dismissLoader()
                    Log.e("updateFav", Gson().toJson(it))
                    progressBarFavorite.visibility=View.GONE
                    propertyData.removeAt(passedPosition)
                    favoriteHomePropertyItemAdapter.notifyDataSetChanged()
                    if(propertyData.size==0){
                        linearNoPropertyFoundFavorite.visibility=View.VISIBLE
                        rvFavoritesPropertyList.visibility=View.GONE
                    }
                    AppPreferences.reload_property_list=true
                    favouriteMainLayout.snack(it.data!!.response)
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
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if(this.isConnected){
                includeNoInternetFavorite.visibility=View.GONE
                i=1
                favoritePropertyViewModel.fetchFavoriteProperty(i,1)
            }

        }
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
       initializeToolbar(title)
    }

}