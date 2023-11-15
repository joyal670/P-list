package com.property.propertyagent.agent_panel.ui.main.home.request

import android.Manifest
import android.annotation.SuppressLint
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
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.agent.agent_my_request_list.Requested
import com.property.propertyagent.utils.AppPreferences
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_request.*

class RequestFragment : BaseFragment() {
    private lateinit var requestViewModel: RequestViewModel
    private var requestList: ArrayList<Requested>? = null
    private var passedPosition = -1
    private lateinit var requestedApartmentAdapter: RequestApartmentAdapter
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_request, container, false)
    }

    override fun initData() {
        requestList = ArrayList()
        requestViewModel.fetchAgentRequestList()

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        requestViewModel = RequestViewModel()
    }

    private fun setRequestedRecyclerView() {
        requestfrgRecycerview.layoutManager = LinearLayoutManager(context)
        requestedApartmentAdapter = RequestApartmentAdapter(requireActivity(), requestList!!,
            { callPhone(it) },
            { id, type, pos -> acceptRequest(id, type, pos) },
            { id1, type1, pos1 -> rejectRequest(id1, type1, pos1) })
        requestfrgRecycerview.adapter = requestedApartmentAdapter
    }

    private fun rejectRequest(id1: String, type1: String, pos1: Int) {
        passedPosition = pos1
        requestViewModel.fetchAgentRejectRequest(id1, type1)
    }

    private fun acceptRequest(id: String, type: String, pos: Int) {
        passedPosition = pos
        requestViewModel.fetchAgentAcceptRequest(id, type)
    }

    private fun callPhone(it: String) {
        permissionsBuilder(Manifest.permission.CALL_PHONE).build().send { result ->
            if (result.allGranted()) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$it")
                startActivity(intent)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        requestViewModel.getAgentRequestListResponse().observe(this, {
            when (it.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> {
                    dismissProgress()
                    requestList!!.clear()
                    Log.e("response--details", Gson().toJson(it))
                    if (it.data != null) {
                        if (it.data.data != null) {
                            if (!(it.data.data.requested_list.isNullOrEmpty())) {
                                requestList = it.data.data.requested_list as ArrayList<Requested>
                                setRequestedRecyclerView()
                            } else {
                                llEmptyData.isVisible = true
                                requestfrgRecycerview.isVisible = false
                            }
                        }
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
        })
        requestViewModel.getAgentAcceptRequestResponse().observe(this, {
            when (it.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> {
                    dismissProgress()
                    Log.e("response--details", Gson().toJson(it))
                    if (it.data != null) {
                        Log.e("check pos", passedPosition.toString())
                        Log.e("check list", Gson().toJson(requestList))
                        requestList!!.removeAt(passedPosition)
                        Log.e("check pos", passedPosition.toString())
                        Log.e("check list", Gson().toJson(requestList))
                        requestedApartmentAdapter.notifyDataSetChanged()
                        AppPreferences.reload_home_api_for_request_accept = true
                        Toaster.showToast(
                            requireContext(), it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        if (requestList!!.isEmpty()) {
                            llEmptyData.isVisible = true
                            requestfrgRecycerview.isVisible = false
                        }
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
        })

        requestViewModel.getAgentRejectRequestResponse().observe(this, {
            when (it.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> {
                    dismissProgress()
                    Log.e("response--details", Gson().toJson(it))
                    if (it.data != null) {
                        requestList!!.removeAt(passedPosition)
                        requestedApartmentAdapter.notifyDataSetChanged()
                        Toaster.showToast(
                            requireContext(), it.data.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        if (requestList!!.isEmpty()) {
                            llEmptyData.isVisible = true
                            requestfrgRecycerview.isVisible = false
                        }
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
        })
    }

    override fun onClicks() {
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

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            requestViewModel.fetchAgentRequestList()
        }
    }
}