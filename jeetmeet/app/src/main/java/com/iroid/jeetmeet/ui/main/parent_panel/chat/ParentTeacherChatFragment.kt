package com.iroid.jeetmeet.ui.main.parent_panel.chat

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentTeacherChatBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.parent.chat_teacher.ParentTeacherChatData
import com.iroid.jeetmeet.modal.parent.chat_teachers_list.ParentChatTeachersListData
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentTeacherChatViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster

class ParentTeacherChatFragment : BaseFragment() {

    private lateinit var binding: FragmentParentTeacherChatBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentTeacherChatViewModel: ParentTeacherChatViewModel
    private lateinit var parentChatTeachersListViewModel: ParentChatTeachersListViewModel
    private lateinit var parentChatTeacherSelectedViewModel: ParentChatTeacherSelectedViewModel
    private var chatList = ArrayList<ParentTeacherChatData>()

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParentTeacherChatFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {

        }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentTeacherChatBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* set title */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.teacher_chat))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        /* recyclerview */
        binding.rvParentTeacherChat.layoutManager = LinearLayoutManager(context)

        setupObserver()

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        parentTeacherChatViewModel = ParentTeacherChatViewModel()
        parentTeacherChatViewModel.parentTeacherChat()
        parentTeacherChatViewModel.parentTeacherData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    chatList.clear()
                    chatList.addAll(it.data!!.data)
                    binding.rvParentTeacherChat.adapter =
                        ParentTeacherChatAdapter(chatList) { name: String, pic_url: String, chat: String, teacher: Int, parent: Int ->
                            getChat(
                                name,
                                pic_url,
                                chat,
                                teacher,
                                parent
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
            }
        })

    }

    private fun getChat(name: String, picUrl: String, chat: String, teacher: Int, parent: Int) {
        val intent = Intent(requireContext(), ParentChatActivity::class.java)
        intent.putExtra("name", name)
        intent.putExtra("pic_url", picUrl)
        intent.putExtra("chat", chat)
        intent.putExtra("teacher", teacher)
        intent.putExtra("parent", parent)
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

        parentChatTeachersListViewModel = ParentChatTeachersListViewModel()
        val teachersList = ArrayList<ParentChatTeachersListData>()

        val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.teachers_list_layout)

        val closeBtn = dialog.findViewById(R.id.ivClose) as ImageView
        val searchBtn = dialog.findViewById(R.id.searchView) as androidx.appcompat.widget.SearchView
        val rvTeachersList = dialog.findViewById(R.id.rvTeachersList) as RecyclerView
        rvTeachersList.layoutManager = LinearLayoutManager(context)

        parentChatTeachersListViewModel.parentChatTeacherList("")
        parentChatTeachersListViewModel.parentTeacherListData.observe(
            this@ParentTeacherChatFragment,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        teachersList.clear()
                        teachersList.addAll(it.data!!.data)

                        rvTeachersList.adapter =
                            ParentTeachersListAdapter(teachersList) { selectedId(it) }

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

                parentChatTeachersListViewModel.parentChatTeacherList(newText!!)
                parentChatTeachersListViewModel.parentTeacherListData.observe(
                    this@ParentTeacherChatFragment,
                    Observer {
                        when (it.status) {
                            Status.SUCCESS -> {
                                teachersList.clear()
                                teachersList.addAll(it.data!!.data)

                                rvTeachersList.adapter =
                                    ParentTeachersListAdapter(teachersList) { selectedId(it) }

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

        parentChatTeacherSelectedViewModel = ParentChatTeacherSelectedViewModel()
        parentChatTeacherSelectedViewModel.parentChatTeacher(selId)
        parentChatTeacherSelectedViewModel.parentTeacherData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    val name = it.data!!.teacher.first_name
                    val picUrl = it.data.teacher.pic_url
                    val chatId = it.data.chat.chat
                    val teacher = it.data.chat.teacher
                    val parent = it.data.chat.parent
                    getChat(name, picUrl, chatId, teacher, parent)

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

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = false

    }

}