package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.mock_test.test_result

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentMockTestResultQuestionDetailsBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentMockTestResultViewDetailsViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster


class StudentMockTestResultQuestionDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentStudentMockTestResultQuestionDetailsBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentMockTestResultViewDetailsViewModel: StudentMockTestResultViewDetailsViewModel

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentMockTestResultQuestionDetailsBinding.inflate(
            inflater!!,
            container,
            false
        )
        return binding.root
    }

    companion object {
        private var mock_Id: Int? = null
        private var selected_Id: Int? = null

        @JvmStatic
        fun newInstance(mockId: Int, selectedId: Int) =
            StudentMockTestResultQuestionDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt("id", mockId)
                    putInt("sid", selectedId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            mock_Id = it.getInt("id")
            selected_Id = it.getInt("sid")
        }

    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.qanda))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {

        setupObserver()
    }

    override fun setupViewModel() {

    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {

        /* api call */
        studentMockTestResultViewDetailsViewModel = StudentMockTestResultViewDetailsViewModel()
        studentMockTestResultViewDetailsViewModel.studentTestResultViewDetails(
            mock_Id!!,
            selected_Id!!
        )
        studentMockTestResultViewDetailsViewModel.studentTestResultViewDetailsData.observe(
            this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgress()

                        binding.tvQuesMark.text =
                            "Question Mark - " + it.data!!.question.mark.toString()
                        binding.tvMark.text = "Mark - " + it.data.answer.answer.toString()
                        binding.tvques.text = it.data.question.question
                        binding.tvAns.text = it.data.answer.name

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

    override fun onClicks() {

    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = false

    }
}