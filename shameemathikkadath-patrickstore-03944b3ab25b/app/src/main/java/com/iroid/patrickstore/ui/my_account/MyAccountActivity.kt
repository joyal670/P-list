package com.iroid.patrickstore.ui.my_account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityMyAccountBinding
import com.iroid.patrickstore.ui.home.HomeActivity
import com.iroid.patrickstore.ui.interfaces.FragmentTransInterface
import com.iroid.patrickstore.ui.my_account.my_orders.order_details.OrderDetailsFragment
import com.iroid.patrickstore.ui.my_account.profile.ProfileFragment
import com.iroid.patrickstore.utils.Constants

class MyAccountActivity : BaseActivity<MyAccountViewModal,ActivityMyAccountBinding>(), FragmentTransInterface{

    override val layoutId: Int
        get() = R.layout.activity_my_account
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchFragment(ProfileFragment())

    }

    private fun launchFragment(fragment: Fragment){
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.flContainer, fragment)
        transaction.commit()
    }



    override fun replaceFragment(fragment: Fragment) {
        launchFragment(fragment)
    }

    override fun replaceFragment(fragmentTag: String) {

    }

    override fun popFragment() {

    }

    override fun popReplaceFragment(fragment: Fragment) {
        launchFragment(fragment)
    }

    override fun setTitleFromFragment(title: String) {
        supportActionBar?.title = title
    }

    override fun getViewBinding(): ActivityMyAccountBinding {
        return ActivityMyAccountBinding.inflate(layoutInflater)
    }

    override fun initViews() {

    }

}
