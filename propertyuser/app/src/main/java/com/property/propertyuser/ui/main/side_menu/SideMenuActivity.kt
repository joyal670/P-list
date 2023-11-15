package com.property.propertyuser.ui.main.side_menu

import android.util.Log
import androidx.fragment.app.Fragment
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivitySideMenuBinding
import com.property.propertyuser.databinding.ActivityTermsofStayBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.side_menu.become_owner.BecomeOwnerFragment
import com.property.propertyuser.ui.main.side_menu.find_property.FindPropertyFragment
import com.property.propertyuser.ui.main.side_menu.refer.ReferAFriendFragment
import com.property.propertyuser.ui.main.side_menu.requested_property.RequestedPropertyFragment
import com.property.propertyuser.ui.main.side_menu.rewards.RewardsFragment
import com.property.propertyuser.utils.Constants.TYPE
import com.property.propertyuser.utils.TYPE_MENUE
import com.property.propertyuser.utils.replaceFragment

class SideMenuActivity : BaseActivity<ActivitySideMenuBinding>(), ActivityListener {
    override fun getViewBinging(): ActivitySideMenuBinding = ActivitySideMenuBinding.inflate(layoutInflater)
    private lateinit var fragmentName:String
    override val layoutId: Int
        get() = R.layout.activity_side_menu
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() =false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        launchFragment(intent.getStringExtra(TYPE))
    }

    private fun launchFragment(stringExtra: String?) {
        when(stringExtra){
            TYPE_MENUE.FINDPROPERTY.name->  replaceFragment(fragment = FindPropertyFragment())
            TYPE_MENUE.BECOMEOWNER.name->  replaceFragment(fragment = BecomeOwnerFragment())
            TYPE_MENUE.REFERAL.name->  replaceFragment(fragment = ReferAFriendFragment())
            TYPE_MENUE.REQUESTEDPROPERTY.name->  replaceFragment(fragment = RequestedPropertyFragment())
            /* pass value 1 for attach rewards fragment in side menu */
            TYPE_MENUE.REWARDS.name->  replaceFragment(fragment = RewardsFragment.newInstance("1"))
        }

    }

    override fun setupUI() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

    override fun navigateToFragment(fragment1: Fragment) {
        replaceFragment(fragment = fragment1,addToBackStack = true)
    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}