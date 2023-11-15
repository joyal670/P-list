package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.mock_test.test_result

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textview.MaterialTextView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentMockTestResultDetailsBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.mock_test_result_view.StudentMockTestResultViewResult
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentMockTestResultViewViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.iroid.jeetmeet.utils.replaceFragment


class StudentMockTestResultDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentStudentMockTestResultDetailsBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentMockTestResultViewViewModel: StudentMockTestResultViewViewModel
    private var mockList = ArrayList<StudentMockTestResultViewResult>()

    companion object {
        private var mockId: Int? = null

        @JvmStatic
        fun newInstance(id: Int) =
            StudentMockTestResultDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt("id", id)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            mockId = it.getInt("id")
        }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentMockTestResultDetailsBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.mock_test_result))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        /* recyclerview */
        binding.rvStudentExamResultDetails.layoutManager = LinearLayoutManager(context)

        setupObserver()
    }

    override fun setupViewModel() {

    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {

        /* api call */
        studentMockTestResultViewViewModel = StudentMockTestResultViewViewModel()
        studentMockTestResultViewViewModel.studentTestResultView(mockId!!)
        studentMockTestResultViewViewModel.studentTestResultViewData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvExamTitle).text =
                        ": " + it.data!!.test.name
                    binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvTotalQues).text =
                        ": " + it.data.test.question_count
                    binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvTotalMark).text =
                        ": " + it.data.test.total_time


                    binding.tvGrade.text = "Gained Mark - " + it.data.gainedmark.toString()
                    binding.tvTotalMar.text = "Total Mark - " + it.data.totalmark.toString()

                    mockList.clear()
                    mockList.addAll(it.data.results)
                    binding.rvStudentExamResultDetails.adapter =
                        StudentMockTestResultViewDetailsAdapter(mockList) {
                            ques(it)
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

    /* question view page */
    private fun ques(selectedId: Int) {
        appCompatActivity?.replaceFragment(
            fragment = StudentMockTestResultQuestionDetailsFragment.newInstance(
                mockId!!,
                selectedId
            ), addToBackStack = true
        )
    }

    override fun onClicks() {
        /* expand and collapse */
        var clicked = false
        binding.expandable.parentLayout.setOnClickListener {
            if (!clicked) {
                binding.expandable.expand()
                clicked = true
            } else {
                binding.expandable.collapse()
                clicked = false
            }
        }
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = false

    }


}