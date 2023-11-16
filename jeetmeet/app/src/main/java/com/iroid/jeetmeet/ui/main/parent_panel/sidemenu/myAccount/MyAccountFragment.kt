package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.myAccount

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentMyAccountBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.parent.students_list.ParentStudentsListData
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.iroid.jeetmeet.utils.replaceFragment


class MyAccountFragment : BaseFragment() {


    private lateinit var binding: FragmentMyAccountBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentMyAccountViewModel: ParentMyAccountViewModel
    private var studentList = ArrayList<ParentStudentsListData>()
    var studentNameList = ArrayList<String>()
    var studentIdList = ArrayList<Int>()

    private var selectedId = 0
    private var selectedName: String = ""

    private var studentCode = ""


    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyAccountBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* set title */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.myAccount))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)

        setupObserver()

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    /* val startForResult =
         registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
             if (result.resultCode == Activity.RESULT_OK) {
                 val intent = result.data
                 // Handle the Intent
             }
         }*/


    override fun setupObserver() {
        parentMyAccountViewModel = ParentMyAccountViewModel()
        parentMyAccountViewModel.parentStudentsList()
        parentMyAccountViewModel.parentStudentsListData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    studentList.clear()
                    studentNameList.clear()
                    studentIdList.clear()

                    studentList.addAll(it.data!!.data)

                    it.data.data.forEach {
                        studentNameList.addAll(listOf(it.first_name))
                        studentIdList.addAll(listOf(it.id))
                    }

                    binding.studentSpinner.clearSelectedItem()
                    binding.studentSpinner.setItems(studentNameList)
                    if (studentIdList.size > 0) {
                        binding.studentSpinner.selectItemByIndex(0)
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
            }
        })
    }

    override fun onClicks() {

        /* spinner listener */
        binding.studentSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            selectedId = 0
            selectedId = studentIdList[newIndex]
            selectedName = newText

            studentList.forEach {
                if (selectedId == it.id) {
                    binding.tvStudentName.text = it.first_name
                    Glide.with(requireContext()).load(it.profile_image_url)
                        .into(binding.circleImageView)
                }
            }

            setUpTimeTableObserver(selectedId)
        }

        /* payment button */
        binding.paymentBtn.setOnClickListener {
            //startForResult.launch(Intent(requireActivity(), PaymentActivity::class.java))

            appCompatActivity?.replaceFragment(
                fragment = ParentPaymentDetailsFragment.newInstance(
                    studentCode
                ), addToBackStack = true
            )
        }
    }


    /* my account details */
    @SuppressLint("SetTextI18n")
    private fun setUpTimeTableObserver(selectedId: Int) {

        parentMyAccountViewModel = ParentMyAccountViewModel()
        parentMyAccountViewModel.parentMyAccount(selectedId.toString())
        parentMyAccountViewModel.parentMyAccountData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    if (it.data!!.data.Details.payment_status == "paid") {
                        binding.statusBtn.setBackgroundColor(Color.parseColor("#EE2424"))
                        binding.paymentBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.comet_light)
                        binding.paymentBtn.isEnabled = false
                        binding.paymentBtn.isClickable = false
                        binding.paymentBtn.isActivated = false
                    }

                    binding.statusBtn.text = it.data.data.Details.payment_status
                    binding.tvTotal.text = "Total Amount : " + it.data.data.Details.total_due
                    binding.tvStudentName1.text = it.data.data.Details.student_name
                    binding.tvStudentCode.text = it.data.data.Details.student_code
                    binding.tvStudentClass.text =
                        ":" + " " + it.data.data.Details.`class` + it.data.data.Details.division
                    binding.tvPreviousBill.text = ":" + " " + it.data.data.Details.previous_due
                    binding.tvCurrentBill.text = ":" + " " + it.data.data.Details.current_due
                    binding.tvOutStanding.text =
                        ":" + " " + it.data.data.Details.currrent_outstanding

                    studentCode = it.data.data.Details.student_code

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