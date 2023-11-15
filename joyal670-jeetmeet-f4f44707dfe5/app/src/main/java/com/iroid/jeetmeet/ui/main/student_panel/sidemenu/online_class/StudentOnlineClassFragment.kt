package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.online_class

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentOnlineClassBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.online_class.StudentOnlineClassData
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentOnlineClassViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.iroid.jeetmeet.utils.replaceFragment

class StudentOnlineClassFragment : BaseFragment() {

    private lateinit var binding: FragmentStudentOnlineClassBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentOnlineClassViewModel: StudentOnlineClassViewModel
    private var onlineClassList = ArrayList<StudentOnlineClassData>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentOnlineClassBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.online_class))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_extrabold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        binding.rvStudentOnlineClass.layoutManager = LinearLayoutManager(context)

        setupObserver()
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* Online class api call */
        studentOnlineClassViewModel = StudentOnlineClassViewModel()
        studentOnlineClassViewModel.studentOnlineClass()
        studentOnlineClassViewModel.studentOnlineClassData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    onlineClassList.clear()
                    onlineClassList.addAll(it.data!!.data)
                    binding.rvStudentOnlineClass.adapter =
                        StudentOnlineClassAdapter(onlineClassList) { startClass(it) }

                    if (onlineClassList.size == 0) {
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

    /* start online class */
    private fun startClass(Id: Int) {
        Log.e("TAG", "startClass: $Id")
        /* request permissions */
        permissionsBuilder(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
        ).build().send { result ->
            if (result.allGranted()) {

                /* start online class */
                studentOnlineClassViewModel = StudentOnlineClassViewModel()
                studentOnlineClassViewModel.studentOnlineClassStart(Id)
                studentOnlineClassViewModel.studentOnlineClassStartData.observe(this, Observer {
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
        item2.isVisible = true

    }

    /* option menu  */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.customtoolbar_search -> {

            true
        }
        R.id.customtoolbar_chat -> {
            val intent = Intent(requireContext(), StudentChatActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        appCompatActivity!!.supportActionBar!!.show()
        //appCompatActivity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

}