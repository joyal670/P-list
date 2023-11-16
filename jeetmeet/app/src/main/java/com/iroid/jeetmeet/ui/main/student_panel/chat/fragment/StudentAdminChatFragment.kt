package com.iroid.jeetmeet.ui.main.student_panel.chat.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentAdminChatBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.chat_admin.StudentChatAdminData
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.AdminChatDetailsActivity
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.chat.adapter.AdminChatAdapter
import com.iroid.jeetmeet.ui.main.student_panel.chat.viewmodel.StudentAdminChatViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster


class StudentAdminChatFragment : BaseFragment() {

    private lateinit var binding: FragmentStudentAdminChatBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentAdminChatViewModel: StudentAdminChatViewModel
    private var chatList = ArrayList<StudentChatAdminData>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentAdminChatBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentChatActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.chat))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        /* recyclerview */
        binding.rvStudentAdminChat.layoutManager = LinearLayoutManager(context)

        setupObserver()

    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        studentAdminChatViewModel = StudentAdminChatViewModel()
        studentAdminChatViewModel.studentAdminChat()
        studentAdminChatViewModel.studentAdminData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    chatList.clear()
                    chatList.addAll(it.data!!.data)
                    binding.rvStudentAdminChat.adapter =
                        AdminChatAdapter(chatList) { name: String, pic_url: String, chat: String, admin: Int, student: Int ->
                            getChat(
                                name,
                                pic_url,
                                chat,
                                admin,
                                student
                            )
                        }
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
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

    private fun getChat(name: String, pic_url: String, chat: String, admin: Int, student: Int) {
        val intent = Intent(requireContext(), AdminChatDetailsActivity::class.java)
        intent.putExtra("name", name)
        intent.putExtra("pic_url", pic_url)
        intent.putExtra("chat", chat)
        intent.putExtra("admin", admin)
        intent.putExtra("student", student)
        startActivity(intent)
    }

    override fun onClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }
    }

}