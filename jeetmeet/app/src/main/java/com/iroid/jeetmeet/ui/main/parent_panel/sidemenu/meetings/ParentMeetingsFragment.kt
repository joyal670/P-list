package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.meetings

import android.Manifest
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentMeetingsBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.parent.meetings.ParentMeetingsData
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentMeetingsViewModel
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.online_class.StudentOnlineClassWebViewFragment
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.iroid.jeetmeet.utils.replaceFragment


class ParentMeetingsFragment : BaseFragment() {
    private lateinit var binding: FragmentParentMeetingsBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentMeetingsViewModel: ParentMeetingsViewModel
    private var meetingList = ArrayList<ParentMeetingsData>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentMeetingsBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* set title */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.meetings))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {

        binding.rvMeetings.layoutManager = LinearLayoutManager(context)

        setupObserver()
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {
        binding.swipeRefreshLayout.setRefreshing(false)

        /* Online class api call */
        parentMeetingsViewModel = ParentMeetingsViewModel()
        parentMeetingsViewModel.parentMeetings()
        parentMeetingsViewModel.parentMeetingsData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    meetingList.clear()
                    meetingList.addAll(it.data!!.data)
                    binding.rvMeetings.adapter =
                        ParentMeetingsAdapter(meetingList) { startMeeting(it) }

                    if (meetingList.size == 0) {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    }

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })
    }

    /* start meeting */
    private fun startMeeting(id: Int) {

        /* request permissions */
        permissionsBuilder(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
        ).build().send { result ->
            if (result.allGranted()) {

                /* start meeting */
                parentMeetingsViewModel = ParentMeetingsViewModel()
                parentMeetingsViewModel.parentMeetingStart(id)
                parentMeetingsViewModel.parentMeetingStartData.observe(this, Observer {
                    when (it.status) {
                        Status.SUCCESS -> {
                            dismissProgress()

                            if (it.data!!.data == "false") {
                                Toaster.showToast(
                                    requireContext(),
                                    "Unable to connect",
                                    Toaster.State.WARNING,
                                    Toast.LENGTH_LONG
                                )
                            } else {
                                appCompatActivity?.replaceFragment(
                                    fragment = StudentOnlineClassWebViewFragment.newInstance(
                                        it.data.data
                                    ), addToBackStack = true
                                )
                            }
                        }
                        Status.LOADING -> {
                            showProgress()
                        }
                        Status.ERROR -> {
                            dismissProgress()
                            Toaster.showToast(
                                requireContext(),
                                it.message!!,
                                Toaster.State.ERROR,
                                Toast.LENGTH_LONG
                            )
                        }
                        Status.DATA_EMPTY -> {
                            dismissProgress()
                        }
                        Status.NO_INTERNET -> {
                            dismissProgress()
                            Toaster.showToast(
                                requireContext(),
                                it.message!!,
                                Toaster.State.ERROR,
                                Toast.LENGTH_LONG
                            )
                        }
                    }
                })
            }
        }
    }

    override fun onClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }
    }

    override fun onResume() {
        super.onResume()
        appCompatActivity!!.supportActionBar!!.show()
        //appCompatActivity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        if (item1 != null) item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        if (item2 != null) item2.isVisible = true

    }


}