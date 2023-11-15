package com.property.propertyagent.agent_panel.ui.main.home.myclient

import android.content.Intent
import android.os.Bundle
import android.view.*
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.OwnerActivity
import com.property.propertyagent.agent_panel.ui.main.home.myclient.users.UsersActivity
import com.property.propertyagent.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_myclient.*

class MyClientFragment : BaseFragment() {
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_myclient, container, false)
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
        // navigation to users activity
        myclientfrg_User.setOnClickListener {
            val intent = Intent(context, UsersActivity::class.java)
            startActivity(intent)
        }

        // navigation to owners activity
        myclientfrg_Owner.setOnClickListener {
            val intent = Intent(context, OwnerActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_calender)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_notification)
        item2.isVisible = false
    }
}