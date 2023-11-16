package com.iroid.jeetmeet.ui.main.student_panel.chat.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentTeacherChatBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.chat_student_teacher.StudentChatTeacherData
import com.iroid.jeetmeet.modal.student.chat_student_teachers_list.StudentChatTeachersListData
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.TeacherChatDetailsActivity
import com.iroid.jeetmeet.ui.main.student_panel.chat.adapter.TeacherChatAdapter
import com.iroid.jeetmeet.ui.main.student_panel.chat.adapter.TeachersListAdapter
import com.iroid.jeetmeet.ui.main.student_panel.chat.viewmodel.StudentChatNewTeachersViewModel
import com.iroid.jeetmeet.ui.main.student_panel.chat.viewmodel.StudentChatTeachersListViewModel
import com.iroid.jeetmeet.ui.main.student_panel.chat.viewmodel.StudentTeacherChatViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster


class StudentTeacherChatFragment : BaseFragment() {
    private lateinit var binding: FragmentStudentTeacherChatBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentTeacherChatViewModel: StudentTeacherChatViewModel
    private lateinit var studentChatTeachersListViewModel: StudentChatTeachersListViewModel
    private lateinit var studentChatNewTeachersViewModel: StudentChatNewTeachersViewModel
    private var chatList = ArrayList<StudentChatTeacherData>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentTeacherChatBinding.inflate(inflater!!, container, false)
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
        binding.rvStudentTeacherChat.layoutManager = LinearLayoutManager(context)

        setupObserver()
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        studentTeacherChatViewModel = StudentTeacherChatViewModel()
        studentTeacherChatViewModel.studentAdminChat()
        studentTeacherChatViewModel.studentTeacherData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    chatList.clear()
                    chatList.addAll(it.data!!.data)

                    binding.rvStudentTeacherChat.adapter =
                        TeacherChatAdapter(chatList) { name: String, pic_url: String, chat: String, teacher: Int, student: Int ->
                            getChat(
                                name,
                                pic_url,
                                chat,
                                teacher,
                                student
                            )
                        }
                }
                Status.LOADING -> {
                    showProgress()
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

            }
        })
    }

    private fun getChat(name: String, picUrl: String, chat: String, teacher: Int, student: Int) {
        val intent = Intent(requireContext(), TeacherChatDetailsActivity::class.java)
        intent.putExtra("name", name)
        intent.putExtra("pic_url", picUrl)
        intent.putExtra("chat", chat)
        intent.putExtra("teacher", teacher)
        intent.putExtra("student", student)
        startActivity(intent)

    }

    override fun onClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }

        /* select teachers btn */
        binding.addTeachersBtn.setOnClickListener {
            setupGetTeachersListObserver()
        }
    }

    /* get teachers list */
    private fun setupGetTeachersListObserver() {

        studentChatTeachersListViewModel = StudentChatTeachersListViewModel()
        val teachersList = ArrayList<StudentChatTeachersListData>()

        val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.teachers_list_layout)

        val closeBtn = dialog.findViewById(R.id.ivClose) as ImageView
        val searchBtn = dialog.findViewById(R.id.searchView) as androidx.appcompat.widget.SearchView
        val rvTeachersList = dialog.findViewById(R.id.rvTeachersList) as RecyclerView
        rvTeachersList.layoutManager = LinearLayoutManager(context)

        studentChatTeachersListViewModel.studentChatTeacherList("")
        studentChatTeachersListViewModel.studentTeacherListData.observe(
            this@StudentTeacherChatFragment,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        teachersList.clear()
                        teachersList.addAll(it.data!!.data)

                        rvTeachersList.adapter =
                            TeachersListAdapter(teachersList) { selectedId(it) }

                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                    }
                    Status.DATA_EMPTY -> {
                    }
                    Status.NO_INTERNET -> {

                    }
                }
            })

        searchBtn.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                studentChatTeachersListViewModel.studentChatTeacherList(newText!!)
                studentChatTeachersListViewModel.studentTeacherListData.observe(
                    this@StudentTeacherChatFragment,
                    Observer {
                        when (it.status) {
                            Status.SUCCESS -> {
                                teachersList.clear()
                                teachersList.addAll(it.data!!.data)

                                rvTeachersList.adapter =
                                    TeachersListAdapter(teachersList) { selectedId(it) }

                            }
                            Status.LOADING -> {
                            }
                            Status.ERROR -> {
                            }
                            Status.DATA_EMPTY -> {
                            }
                            Status.NO_INTERNET -> {

                            }
                        }
                    })
                return true
            }
        })

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog.window!!.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.pomegranate)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window?.attributes = layoutParams
    }

    /* Get Chat of selected teacher */
    private fun selectedId(selId: Int) {

        studentChatNewTeachersViewModel = StudentChatNewTeachersViewModel()
        studentChatNewTeachersViewModel.studentChatTeacher(selId)
        studentChatNewTeachersViewModel.studentTeacherData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    val name = it.data!!.teacher.first_name
                    val picUrl = it.data.teacher.pic_url
                    val chatId = it.data.chat.chat
                    val teacher = it.data.chat.teacher
                    val student = it.data.chat.student
                    getChat(name, picUrl, chatId, teacher, student)

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