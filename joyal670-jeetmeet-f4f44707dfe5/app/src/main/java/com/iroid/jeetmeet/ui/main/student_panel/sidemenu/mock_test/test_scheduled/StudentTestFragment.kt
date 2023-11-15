package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.mock_test.test_scheduled

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentTestBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.mock_test_start.StudentMockTestStartQuestion
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_schedule.StudentExamCompleteFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_schedule.TestAdapter
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentMockTestSaveViewModel
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentMockTestStartViewModel
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentMockTestTimeOutViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster


class StudentTestFragment : BaseFragment() {
    private lateinit var binding: FragmentStudentTestBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentMockTestStartViewModel: StudentMockTestStartViewModel
    private lateinit var studentMockTestSaveViewModel: StudentMockTestSaveViewModel
    private lateinit var studentMockTestTimeOutViewModel: StudentMockTestTimeOutViewModel
    private var questionList = ArrayList<StudentMockTestStartQuestion>()
    var totalQuestion = 0
    var currentQes = 0
    private val objectiveAnswerHashMap: HashMap<Int, Int> = HashMap()

    companion object {

        private const val TAG = "StudentMockTestFragment"
        private var testId: Int? = null
        private var testTimeInMilliSeconds: Long? = null
        private var testTimeInMilliString: String? = null

        @JvmStatic
        fun newInstance(type: Int, testTimeInMilliSeconds: Long, testTimeInString: String) =
            StudentTestFragment().apply {
                arguments = Bundle().apply {
                    putInt("id", type)
                    putLong("examTimeMilliseconds", testTimeInMilliSeconds)
                    putString("examTimeStrings", testTimeInString)
                }
            }
    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            testId = it.getInt("id")
            testTimeInMilliSeconds = it.getLong("examTimeMilliseconds")
            testTimeInMilliString = it.getString("examTimeStrings")
        }

        /* disable back press */
        requireActivity().onBackPressedDispatcher.addCallback(this) {
        }
    }


    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentTestBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.test_scheduled))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
        /* disable back arrow */
        appCompatActivity!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    }

    override fun setupUI() {
        /* disable viewpager swipe */
        //binding.questionsViewPager.isUserInputEnabled = false

        setupObserver()
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {

        studentMockTestStartViewModel = StudentMockTestStartViewModel()
        studentMockTestStartViewModel.studentMockTestList(testId!!)
        studentMockTestStartViewModel.studentMockTestStartData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    questionList.clear()
                    questionList.addAll(it.data!!.questions)

                    binding.questionsViewPager.adapter =
                        StudentMockTestQuestionsAdapter(questionList) { quesId: Int, ansId ->
                            selected(
                                quesId,
                                ansId
                            )
                        }

                    totalQuestion = it.data.questions.size

                    binding.rvStep.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.rvStep.adapter = TestAdapter(totalQuestion, currentQes)

                    setUpTimer()

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

    /* save answer to hashmap
    * navigate to next question */
    private fun selected(quesId: Int, ansId: Int) {
        objectiveAnswerHashMap[quesId] = ansId
        binding.questionsViewPager.currentItem = binding.questionsViewPager.currentItem + 1
        currentQes += 1
        binding.rvStep.adapter = TestAdapter(totalQuestion, currentQes)
    }

    /* start exam timer */
    private fun setUpTimer() {

        var i = 0
        val timer = object : CountDownTimer(testTimeInMilliSeconds!!, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                i++
                binding.progressBar.progress = (i * 100) / (testTimeInMilliSeconds!! / 1000).toInt()

                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24

                val elapsedDays = diff / daysInMilli
                diff %= daysInMilli

                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli

                binding.tvTimer.text = "$elapsedHours:$elapsedMinutes:$elapsedSeconds"
            }

            override fun onFinish() {
                setUpMockTestTimeOutObserver()
            }
        }
        timer.start()

    }

    override fun onClicks() {

        binding.completeButton.setOnClickListener {

            if (objectiveAnswerHashMap.size == totalQuestion) {
                setUpMockTestCompleteObserver()
            } else {
                Toaster.showToast(
                    requireContext(),
                    "Select any option",
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
            }


        }

        binding.questionsViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if ((position + 1) == totalQuestion) {
                    binding.completeButton.visibility = View.VISIBLE
                }
            }

        })
    }

    /* api for time out */
    private fun setUpMockTestTimeOutObserver() {

        /* convert hashmap to arrayList */
        val objectiveAnswerEntrySet: Set<Map.Entry<Int, Int>> = objectiveAnswerHashMap.entries
        val objectiveListOfEntry: ArrayList<Map.Entry<Int, Int>> =
            ArrayList(objectiveAnswerEntrySet)
        val keysArray = ArrayList<Int>()
        val ansArray = ArrayList<Int>()

        objectiveListOfEntry.forEach {
            keysArray.addAll(listOf(it.key))
            ansArray.addAll(listOf(it.value))
        }

        studentMockTestTimeOutViewModel = StudentMockTestTimeOutViewModel()
        studentMockTestTimeOutViewModel.studentTestTimeOut(testId!!, keysArray, ansArray)
        studentMockTestTimeOutViewModel.studentTestTimeOutData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    val dialogFragment = StudentExamCompleteFragment(totalQuestion)
                    dialogFragment.show(childFragmentManager, "sd")
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

    /* api for mock test complete */
    private fun setUpMockTestCompleteObserver() {

        /* convert hashmap to arrayList */
        val objectiveAnswerEntrySet: Set<Map.Entry<Int, Int>> = objectiveAnswerHashMap.entries
        val objectiveListOfEntry: ArrayList<Map.Entry<Int, Int>> =
            ArrayList(objectiveAnswerEntrySet)
        val keysArray = ArrayList<Int>()
        val ansArray = ArrayList<Int>()

        objectiveListOfEntry.forEach {
            keysArray.addAll(listOf(it.key))
            ansArray.addAll(listOf(it.value))
        }

        studentMockTestSaveViewModel = StudentMockTestSaveViewModel()
        studentMockTestSaveViewModel.studentTestSave(testId!!, keysArray, ansArray)
        studentMockTestSaveViewModel.studentTestSaveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    val dialogFragment = StudentExamCompleteFragment(totalQuestion)
                    dialogFragment.show(childFragmentManager, "sd")
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

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = false

    }

}