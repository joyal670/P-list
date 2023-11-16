package com.iroid.healthdomain.ui.viewpager_adaptor

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iroid.healthdomain.data.model_class.contacts_api.ContactData
import com.iroid.healthdomain.ui.home.mainActivity.all_contacts.AllContactsFragment
import com.iroid.healthdomain.ui.home.mainActivity.status.StatusFragment
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.hds.HdsFragment
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.steps.StepsFragment

class ViewPagerAdaptor2(
    list: ArrayList<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val data: List<ContactData>,
    private val function: () -> Unit,
) :
        FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            return AllContactsFragment.newInstance(data) {
                function.invoke()
            }

        } else {
            StatusFragment.newInstance(data)
        }
    }


}
