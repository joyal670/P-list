package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.notice

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentNoticeBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.dialogs.NoticeDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.parent.dashboard.ParentDashboardNotice
import com.iroid.jeetmeet.ui.main.parent_panel.home.viewmodel.ParentDashboardViewModel
import com.iroid.jeetmeet.ui.main.parent_panel.home.viewmodel.ParentNoticeViewModel
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster


class ParentNoticeFragment : BaseFragment() {
    private lateinit var binding: FragmentParentNoticeBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentDashboardViewModel: ParentDashboardViewModel
    private lateinit var parentNoticeViewModel: ParentNoticeViewModel
    private var noticeList = ArrayList<ParentDashboardNotice>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentNoticeBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* set title */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.notice))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        /* recyclerview */
        binding.rvParentNotice.layoutManager = LinearLayoutManager(context)

        setupObserver()

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* notice api */
        parentDashboardViewModel = ParentDashboardViewModel()
        parentDashboardViewModel.parentDashboard()
        parentDashboardViewModel.parentDashboardData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    binding.rvParentNotice.hideShimmer()
                    noticeList.clear()
                    noticeList.addAll(it.data!!.data.notice)
                    binding.rvParentNotice.adapter =
                        ParentNoticeAdapter(noticeList) { viewNotice(it) }

                    if (noticeList.size == 0) {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    }
                }
                Status.LOADING -> {
                    showProgress()
                    binding.rvParentNotice.showShimmer()
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    binding.rvParentNotice.hideShimmer()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    binding.rvParentNotice.hideShimmer()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.ERROR -> {
                    dismissProgress()
                    binding.rvParentNotice.hideShimmer()
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

    private fun viewNotice(noticeId: Int) {
        parentNoticeViewModel = ParentNoticeViewModel()
        parentNoticeViewModel.parentNotice(noticeId)
        parentNoticeViewModel.parentNoticeData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    val dialog =
                        NoticeDialogFragment(it.data!!.data.title, it.data.data.description)
                    dialog.show(parentFragmentManager, "TAG")
                }
                Status.LOADING -> {
                    showProgress()
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
                Status.ERROR -> {
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

    override fun onClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }
    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = false

    }
}