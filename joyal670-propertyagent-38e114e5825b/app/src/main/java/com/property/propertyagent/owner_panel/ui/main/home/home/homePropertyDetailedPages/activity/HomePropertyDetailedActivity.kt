package com.property.propertyagent.owner_panel.ui.main.home.home.homePropertyDetailedPages.activity


import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.activity.AddPropertyMainPageCopy
import com.property.propertyagent.owner_panel.ui.main.home.home.homePropertyDetailedPages.fragments.PropertyFragment
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.EnumFromPage
import com.shameem.projectstructure.listeners.ActivityListener
import com.property.propertyagent.utils.replaceFragment
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class HomePropertyDetailedActivity : BaseActivity(), ActivityListener, FragmentTransInterface {
    override val layoutId: Int
        get() = R.layout.activity_home_property_detailed_
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        launchFragment(intent.getStringExtra(Constants.TYPE))

        setSupportActionBar(owner_toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

    private fun launchFragment(stringExtra: String?) {
        when (stringExtra) {
            EnumFromPage.NOOFPROPERTY.name -> {
                replaceFragment(fragment = PropertyFragment.newInstance(1))
            }
            EnumFromPage.OCCUPIEDPROPERTY.name -> {
                replaceFragment(fragment = PropertyFragment.newInstance(2))
            }
            EnumFromPage.VACANTPROPERTY.name -> {
                replaceFragment(fragment = PropertyFragment.newInstance(3))
            }
            EnumFromPage.UNDERMAINTANCE.name -> {
                replaceFragment(fragment = PropertyFragment.newInstance(4))
            }
        }
    }

    override fun navigateToFragment(fragment: Fragment) {
        replaceFragment(fragment = fragment, addToBackStack = true)
    }

    override fun setTitleFromFragment(title: String) {
        tvToolbarTitleOwner.text = title
    }

    // option menu setup
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.custom_toolbar_menu_owner, menu)

        return super.onCreateOptionsMenu(menu)
    }

    // option menu clicks
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.customtoolbar_addProperty -> {
            val intent = Intent(this, AddPropertyMainPageCopy::class.java)
            startActivity(intent)
            true
        }
        R.id.customtoolbar_search -> {
            true
        }

        R.id.customtoolbar_translate -> {
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}