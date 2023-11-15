package com.iroid.healthdomain.ui.home.mainActivity.all_contacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.tabs.TabLayoutMediator
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.contacts_api.ContactData
import com.iroid.healthdomain.databinding.ActivityConatctBinding
import com.iroid.healthdomain.ui.viewpager_adaptor.ViewPagerAdaptor2

class ContactUsActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var binding: ActivityConatctBinding
    private lateinit var adaptor: ViewPagerAdaptor2
    private var fragmentList = arrayListOf<Fragment>()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_conatct)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        val conatctList = ArrayList<ContactData>()
        generateViewPager(conatctList)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun generateViewPager(data: List<ContactData>) {


        adaptor = ViewPagerAdaptor2(
            fragmentList,
            supportFragmentManager,
            lifecycle,
            data
        ) {

        }

        binding.homeViewPager.adapter = adaptor

        binding.homeViewPager.offscreenPageLimit=2

        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "All Contacts"
                1 -> tab.text = "Status"
            }

        }.attach()
    }
    }

