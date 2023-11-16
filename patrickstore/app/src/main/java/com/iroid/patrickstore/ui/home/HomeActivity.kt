package com.iroid.patrickstore.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat.START
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityHomeBinding
import com.iroid.patrickstore.model.all_categories.AllCategories
import com.iroid.patrickstore.model.home.Home
import com.iroid.patrickstore.model.service.ServiceCategory
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.ui.allcategories.AllCategoryActivity
import com.iroid.patrickstore.ui.cart.car_list.CartActivity
import com.iroid.patrickstore.ui.dailydeal.DailyDealActivity
import com.iroid.patrickstore.ui.home.adapter.HomeNewAdapter
import com.iroid.patrickstore.ui.home.adapter.navadapter.CategoriesNavAdapter
import com.iroid.patrickstore.ui.home.adapter.navadapter.ShopCategoryListAdapter
import com.iroid.patrickstore.ui.my_account.MyAccountActivity
import com.iroid.patrickstore.ui.offer.OfferActivity
import com.iroid.patrickstore.ui.search.SearchActivity
import com.iroid.patrickstore.ui.seller.SellerActivity
import com.iroid.patrickstore.ui.service_category.ServiceCategoryActivity
import com.iroid.patrickstore.ui.viewall.ViewAllActivity
import com.iroid.patrickstore.ui.wholesalemarket.WholeSaleActivity
import com.iroid.patrickstore.utils.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.categories_layout.*
import kotlinx.android.synthetic.main.custom_drawer.*
import kotlinx.android.synthetic.main.layout_home_new.*
import kotlinx.android.synthetic.main.toolbar_home.*

class HomeActivity : BaseActivity<HomeViewModel, ActivityHomeBinding>(), View.OnClickListener {
    private lateinit var homeViewModel: HomeViewModel

    override val layoutId: Int
        get() = R.layout.activity_home
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        imgBurger.visibility = View.VISIBLE
        imgBurger.setOnClickListener(this)
        llFestival.setOnClickListener(this)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeButtonEnabled(true)

        homeViewModel = HomeViewModel()
        setUpObserver()
        homeViewModel.getDashboard()

        //setNavAdapter

        llStores.setOnClickListener(this)
        llDeals.setOnClickListener(this)
        imgClose.setOnClickListener(this)
        tvViewAllShop.setOnClickListener(this)
        tvViewAllCategories.setOnClickListener(this)
        llAllCategories.setOnClickListener(this)
        llMarket.setOnClickListener(this)


        binding.layoutHome.layoutToolbar.editSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        tvNoService.setOnClickListener {
            finish()
        }

        binding.layoutHome.rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var state = IntArray(10)
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                state[0] = newState
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.e("123456", "onScrolled: ${state[0]}" )
                if (dy>0 && (state[0] == 0 || state[0] == 2)) {
                    binding.layoutHome.layoutToolbar.layoutCategories.constraintCategory.visibility =
                        View.GONE
                    binding.layoutHome.layoutToolbar.constraintTop.visibility =
                        View.GONE


                } else {
                    binding.layoutHome.layoutToolbar.layoutCategories.constraintCategory.visibility =
                        View.VISIBLE
                    binding.layoutHome.layoutToolbar.constraintTop.visibility =
                        View.VISIBLE


                }
            }
        })



    }

    override fun onClick(v: View?) {
        when (v) {
            llStores -> startActivity(Intent(this, SellerActivity::class.java))
            imgBurger -> {
                if(AppPreferences.franchiseId.isNotEmpty()){
                    if (drawer_layout.isDrawerOpen(START)) {
                        drawer_layout.closeDrawer(START)
                    } else {
                        drawer_layout.openDrawer(START)
                    }
                }else{
                    showNavigateToLocation()
                }

            }
            imgClose -> {
                if (drawer_layout.isDrawerOpen(START)) {
                    drawer_layout.closeDrawer(START)
                } else {
                    drawer_layout.openDrawer(START)
                }
            }
            tvViewAllShop -> startActivity(Intent(this, SellerActivity::class.java))
            llAllCategories -> startActivity(Intent(this, AllCategoryActivity::class.java))
            tvViewAllCategories -> startActivity(Intent(this, AllCategoryActivity::class.java))
            llDeals -> startActivity(Intent(this, DailyDealActivity::class.java))
            llFestival -> startActivity(Intent(this, OfferActivity::class.java))
            llMarket ->{
                Toaster.showToast(
                    this,
                    COMING_SOON,
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menue_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_cart -> {
            if(AppPreferences.franchiseId.isNotEmpty()){
                startActivity(Intent(this, CartActivity::class.java))
            }else{
                showNavigateToLocation()
            }

            true
        }
        R.id.action_call -> {
            val intent = Intent(Intent.ACTION_DIAL)
            startActivity(intent)
            true
        }
        R.id.action_profile -> {
            if(AppPreferences.franchiseId.isNotEmpty()){
                startActivity(Intent(this, MyAccountActivity::class.java))
            }else{
                showNavigateToLocation()
            }

            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun setUpObserver() {
        homeViewModel.homeLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    try {
                        AppPreferences.franchiseId=it.data!!.franchiseId
                        rvHome.visibility=View.VISIBLE
                        constraintLocation.visibility=View.GONE
                        setData(it.data!!.data)
                    }catch (ex:Exception){
                        rvHome.visibility=View.GONE
                        constraintLocation.visibility=View.VISIBLE
                        setData(it.data!!.data)
                    }



                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    rvHome.visibility=View.GONE
//                    constraintNoData.visibility=View.VISIBLE
                }
                Status.NO_INTERNET -> {
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this,
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }
        homeViewModel.serviceCategory.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    setServiceCategory(it.data!!.data)
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        }

        homeViewModel.viewProfileLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    AppPreferences.mobile=it.data!!.data.mobile
                    AppPreferences.first_name=it.data!!.data.firstName
                    AppPreferences.last_name=it.data!!.data.lastName
                    try {
                        AppPreferences.image_url=it.data!!.data.profileImageId.publicUrl
                    }catch (ex:Exception){

                    }


                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this,
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }
        homeViewModel.allCategoriesLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    setAllCategory(it.data!!.data)
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        }
    }

    private fun setAllCategory(allCategories: List<AllCategories>) {
        rvNavAll.layoutManager = LinearLayoutManager(this)
        rvNavAll.adapter = CategoriesNavAdapter(this,allCategories) { id, isPerishable,name ->
            val intent = Intent(this, ViewAllActivity::class.java)
            intent.putExtra("id",id)
            intent.putExtra("category_name",name)
            intent.putExtra("type",isPerishable)
            startActivity(intent)
        }
    }

    private fun setServiceCategory(serviceCategory: List<ServiceCategory>) {
        rvNavShop.layoutManager = LinearLayoutManager(this)
        rvNavShop.adapter = ShopCategoryListAdapter(serviceCategory) {id,name->
            val intent = Intent(this, ServiceCategoryActivity::class.java)
            intent.putExtra("id",id)
            intent.putExtra("category_name",name)
            startActivity(intent)
        }


    }

    private fun setData(homeList: List<Home>) {
        rvHome!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvHome!!.adapter = HomeNewAdapter(this, homeList)
        rvHome!!.setHasFixedSize(true)
    }

    override fun getViewBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun initViews() {

    }
    private fun showNavigateToLocation() {
        val snackBar = Snackbar.make(binding.drawerLayout, "Here service is not available", Snackbar.LENGTH_LONG)
        snackBar!!.view.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        snackBar.setTextColor(Color.WHITE)
        snackBar.setActionTextColor(Color.WHITE)
        snackBar.setAction("Change location") {
            finish()
        }.show()
    }

}
