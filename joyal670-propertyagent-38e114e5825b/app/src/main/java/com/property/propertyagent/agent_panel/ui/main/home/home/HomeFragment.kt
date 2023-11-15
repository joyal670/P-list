package com.property.propertyagent.agent_panel.ui.main.home.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.ProfileActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.MenueListener
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {
    private lateinit var menueListener: MenueListener
    private lateinit var homeViewModel: HomeViewModel

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (AppPreferences.reload_home_api_for_request_accept) {
            AppPreferences.reload_home_api_for_request_accept = false
            homeViewModel.agentHomeDetails()
        } else if (isConnectionRestored) {
            isConnectionRestored = false
            homeViewModel.agentHomeDetails()
        }
    }

    override fun initData() {

        container.setRefreshing(false)

        menueListener = activity as HomeActivity
        menueListener.menueHide(true)
        homeViewModel.agentHomeDetails()

        Log.i("TOKEN", AppPreferences.token.toString())

        homefrg_TotalPropertyLayout.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.MYPROFERTIES.name)
            startActivity(intent)
        }
    }

    private fun openDialer(phone: String) {
        permissionsBuilder(Manifest.permission.CALL_PHONE).build().send { result ->
            if (result.allGranted()) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                startActivity(intent)
            }
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        homeViewModel = HomeViewModel()
    }

    override fun setupObserver() {
        homeViewModel.getAgentHomeDetailsResponse().observe(this) {
            when (it.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> {
                    dismissProgress()
                    if (it.data?.response != null) {
                        Log.e("response", Gson().toJson(it))
                        agentTotalAssignedPropertyCount.text =
                            it.data.response.total_assigned_count.toString()
                        tvZone.text = it.data.response.zone
                        tvTodaysAppointment.text = it.data.response.appointment_count.toString()
                        if (!(it.data.response.appoinments.isNullOrEmpty())) {
                            // daily appointments
                            animationView1.visibility = View.GONE
                            tvNoAppoinments.visibility = View.GONE
                            homefrgRecycerview.visibility = View.VISIBLE
                            homefrgRecycerview.layoutManager = LinearLayoutManager(context)
                            homefrgRecycerview.adapter =
                                PropertyLocationAdapter(requireActivity(),
                                    it.data.response.appoinments
                                )
                        }else {
                            animationView1.visibility = View.VISIBLE
                            tvNoAppoinments.visibility = View.VISIBLE
                            homefrgRecycerview.visibility = View.GONE
                        }
                        if (!it.data.response.rent_over_due.isNullOrEmpty()) {
                            // rent overdue
                            animationView2.visibility = View.GONE
                            tvNoRentOverDue.visibility = View.GONE
                            homefrgRecycerview2.visibility = View.VISIBLE
                            homefrgRecycerview2.layoutManager = LinearLayoutManager(context)
                            homefrgRecycerview2.adapter =
                                PropertyNameAdapter(requireActivity(),
                                    { phone -> openDialer(phone) },
                                    it.data.response.rent_over_due
                                )
                        }else {
                            animationView2.visibility = View.VISIBLE
                            tvNoRentOverDue.visibility = View.VISIBLE
                            homefrgRecycerview2.visibility = View.GONE
                        }
                        container.isVisible = true
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(), getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        getString(R.string.data_empty),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireContext().isConnected) {
                        Toaster.showToast(
                            requireContext(), getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(requireActivity())
                        dialog.show(parentFragmentManager, "TAG")
                    }
                }
            }
        }
    }

    override fun onClicks() {
        container.setOnRefreshListener {
            initData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.customtoolbar_calender)
        item.isVisible = false
    }
}