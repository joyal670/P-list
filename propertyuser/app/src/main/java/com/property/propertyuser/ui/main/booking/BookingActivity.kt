package com.property.propertyuser.ui.main.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityBookingBinding
import com.property.propertyuser.databinding.ActivitySplashBinding
import com.property.propertyuser.dialogs.book_a_tour.BookATourDateAndTimeDialogFragment
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.booking.book_a_property.BookAPropertyFragment
import com.property.propertyuser.ui.main.booking.book_a_tour.BookATourFragment
import com.property.propertyuser.ui.main.side_menu.become_owner.BecomeOwnerFragment
import com.property.propertyuser.ui.main.side_menu.find_property.FindPropertyFragment
import com.property.propertyuser.ui.main.side_menu.refer.ReferAFriendFragment
import com.property.propertyuser.ui.main.side_menu.requested_property.RequestedPropertyFragment
import com.property.propertyuser.ui.main.side_menu.rewards.RewardsFragment
import com.property.propertyuser.ui.startup.auth.otp.OtpFragment
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.TYPE_BOOKING
import com.property.propertyuser.utils.TYPE_MENUE
import com.property.propertyuser.utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_dialog_book_tour_date_time.*

class BookingActivity : BaseActivity<ActivityBookingBinding>(),ActivityListener {

    override fun getViewBinging(): ActivityBookingBinding = ActivityBookingBinding.inflate(layoutInflater)
    override val layoutId: Int
        get() = R.layout.activity_booking
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
        launchFragment(intent.getStringExtra(Constants.TYPE_BOOKING))
    }
    private fun launchFragment(stringExtra: String?) {
        when(stringExtra){
            TYPE_BOOKING.TOUR.name->{
                val property_id = intent.getStringExtra("property_id")
                val tour_id = intent.getStringExtra("tour_id")
                val fragment = BookATourFragment.newInstance(property_id.toString(),tour_id.toString())
                replaceFragment(fragment =fragment)
            }
            TYPE_BOOKING.PROPERTY.name->{
                val property_id = intent.getStringExtra("property_id")
                val package_amount=intent.getStringExtra("package_amount")
                val fragment = BookAPropertyFragment.newInstance(property_id.toString(),package_amount.toString())
                replaceFragment(fragment =fragment)
            }
        }

    }
    override fun initData() {

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}