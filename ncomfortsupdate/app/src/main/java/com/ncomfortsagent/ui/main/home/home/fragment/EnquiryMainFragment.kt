package com.ncomfortsagent.ui.main.home.home.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseFragment
import com.ncomfortsagent.databinding.FragmentEnquiryMainBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.model.enquiry.AgentHomeEnquiryPropertyDetails
import com.ncomfortsagent.model.enquiry.AgentHomeEnquiryPropertyPriorityImage
import com.ncomfortsagent.model.enquiry.AgentHomeEnquiryUserProperty
import com.ncomfortsagent.model.enquiry.AgentHomeEnquiryUserRel
import com.ncomfortsagent.ui.main.home.detailspage.enquirydetails.activity.EnquiryDetailsActivity
import com.ncomfortsagent.ui.main.home.home.adapter.EnquiryAdapter
import com.ncomfortsagent.ui.main.home.home.viewmodel.HomeViewModel
import com.ncomfortsagent.utils.*
import com.ncomfortsagent.utils.AppPreferences.user_lang
import com.ncomfortsagent.utils.Constants.ARABIC_LAG
import android.widget.Toast
import android.widget.RadioButton
import com.ncomfortsagent.databinding.LayoutFilterBinding


class EnquiryMainFragment : BaseFragment() {

    private lateinit var binding: FragmentEnquiryMainBinding
    private lateinit var enquiryAdapter: EnquiryAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var enquiryData = ArrayList<AgentHomeEnquiryUserProperty>()
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    private var enquiryStatus = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnquiryMainBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        homeViewModel.enquiry(page.toString(),enquiryStatus)

    }

    private fun setupRecyclerView() {
        enquiryAdapter = EnquiryAdapter(requireActivity(), { selectedItem(it) }, enquiryData)
        binding.rvEnquiry.adapter = enquiryAdapter
        binding.rvEnquiry.scheduleLayoutAnimation()
        binding.rvEnquiry.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    private fun loadData() {
        if (page <= totalPages) {
            enquiryData.add(
                AgentHomeEnquiryUserProperty(
                    "", -1, AgentHomeEnquiryPropertyDetails(-1, "", "", "", -1, "", ""), -1,
                    AgentHomeEnquiryPropertyPriorityImage("", -1), -1,
                    AgentHomeEnquiryUserRel("", -1, "", "", ""), -1, -1
                )
            )
            enquiryAdapter.notifyItemInserted(enquiryData.size - 1)
            homeViewModel.enquiry(page.toString(),1)
        }
    }

    override fun setupUI() {
        binding.rvEnquiry.layoutManager = LinearLayoutManager(context)
    }

    override fun setupViewModel() {
        homeViewModel = HomeViewModel(requireActivity())
    }

    override fun setupObserver() {
        homeViewModel.getAgentEnquiryResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (enquiryData.size == 0) {

                    }

                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvEnquiry.visibility = View.VISIBLE

                    if (it.data!!.tour_data != null) {
                        totalPages = it.data.tour_data.total_page_count
                        if (enquiryData.size != 0) {
                            isLoading = false
                            page += 1
                            enquiryData.removeAt(enquiryData.size - 1)
                            enquiryAdapter.notifyItemRemoved(enquiryData.size)
                            enquiryData.addAll(it.data.tour_data.user_properties)
                            enquiryAdapter.notifyDataSetChanged()
                        } else {
                            page += 1
                            enquiryData =
                                it.data.tour_data.user_properties as ArrayList<AgentHomeEnquiryUserProperty>
                            setupRecyclerView()
                        }

                        if (enquiryData.size == 0) {
                            binding.rvEnquiry.visibility = View.GONE
                            binding.noResult.noData.visibility = View.VISIBLE
                        } else {
                            binding.rvEnquiry.visibility = View.VISIBLE
                            binding.noResult.noData.visibility = View.GONE
                        }
                    }
                }
                Status.LOADING -> {
                    if (enquiryData.size == 0) {

                    }
                    binding.shimmerLayout.startShimmer()
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.rvEnquiry.visibility = View.GONE
                    binding.noResult.noData.visibility = View.GONE
                }
                Status.NO_INTERNET -> {
                    binding.shimmerLayout.stopShimmer()
                    if (requireContext().isConnected) {
                        CommonUtils.showCookieBar(
                            requireActivity(),
                            getString(R.string.error),
                            getString(R.string.something_wrong),
                            R.color.pomegranate
                        )

                    } else {
                        val dialog = InternetDialogFragment(requireActivity())
                        dialog.show(parentFragmentManager, "TAG")
                    }
                }
                Status.ERROR -> {
                    binding.shimmerLayout.stopShimmer()
                    CommonUtils.showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.DATA_EMPTY -> {
                    binding.shimmerLayout.stopShimmer()
                    CommonUtils.showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onClicks() {
        binding.sgEnquiry.setOnPositionChangedListener { position ->
            when (position){
                0 -> {
                    enquiryStatus = 0
                    recallEnquiryData()
                }
                1 -> {
                    enquiryStatus = 1
                    recallEnquiryData()
                }
                2 -> {
                    enquiryStatus = 2
                    recallEnquiryData()
                }
                3 -> {
                    enquiryStatus = 3
                    recallEnquiryData()
                }
            }
        }
    }

    private fun selectedItem(selectedItemId: Int) {
        val intent = Intent(requireContext(), EnquiryDetailsActivity::class.java)
        intent.putExtra(Constants.ENQUIRY_ID, selectedItemId.toString())
        intent.putExtra("enquiry_type", binding.sgEnquiry.position.toString())
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun recallEnquiryData(){
        page = 1
        enquiryData.clear()
        enquiryAdapter.notifyDataSetChanged()
        homeViewModel.enquiry(page.toString(),enquiryStatus)
    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customToolbarSearch)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customToolbarFilter)
        item2.isVisible = false

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.custom_menu, menu)
        val one =
            menu.findItem(R.id.customToolbarFilter)?.actionView?.findViewById<LinearLayout>(R.id.layout)

        one!!.setOnClickListener {
            setUpFilterDialog()
        }
    }

    /* filter dialog */
    @SuppressLint("ClickableViewAccessibility")
    private fun setUpFilterDialog() {
        val dialog = Dialog(requireContext(), R.style.customDialog)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.layout_filter_new)

        val filterName = dialog.findViewById(R.id.tvFilterName) as MaterialTextView
        val etFilterName = dialog.findViewById(R.id.etFilterName) as EditText

        val filterType = dialog.findViewById(R.id.tvFilterType) as MaterialTextView
        val rbType = dialog.findViewById(R.id.rbType) as RadioGroup

        val filterCategory = dialog.findViewById(R.id.tvFilterCategory) as MaterialTextView
        val rbFilterCategory = dialog.findViewById(R.id.rbFilterCategory) as RadioGroup

        val filterStatus = dialog.findViewById(R.id.tvFilterStatus) as MaterialTextView
        val rbFilterStatus = dialog.findViewById(R.id.rbFilterStatus) as RadioGroup

        val motionLayout = dialog.findViewById(R.id.motionLayout) as MotionLayout
        val lv = dialog.findViewById(R.id.lv) as LinearLayout
        val constraintLayout = dialog.findViewById(R.id.constraintLayout) as ConstraintLayout

        val applyBtn = dialog.findViewById(R.id.applyBtn) as MaterialButton
        val clearBtn = dialog.findViewById(R.id.clearBtn) as MaterialButton

        lv.setOnClickListener {
            motionLayout.transitionToEnd()
        }
        val moveAnim: TranslateAnimation = if (user_lang == ARABIC_LAG) {
            TranslateAnimation(-600F, 0F, 0F, 0F)
        } else {
            TranslateAnimation(600F, 0F, 0F, 0F)
        }
        moveAnim.duration = 600
        moveAnim.fillAfter = true
        constraintLayout.startAnimation(moveAnim)

        //constraintLayout.animate().translationZBy (1000f).duration = 1000

        var startValue = ""


        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                startValue = p1.toString()
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                dialog.show()
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if (startValue == p1.toString()) {
                    dialog.show()
                } else {
                    dialog.dismiss()
                }
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }
        })
        /* motionLayout.addTransitionListener(object :MotionLayout.TransitionListener{
             override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                 Log.e("TAG", "onTransitionStarted: " )
             }

             override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                 Log.e("TAG", "onTransitionChange: " )
             }

             override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                 Log.e("TAG", "onTransitionCompleted: " )
             }

             override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                 Log.e("TAG", "onTransitionTrigger: ")
             }

         })*/

        /*motionLayout.setOnConstraintsChanged(object : ConstraintsChangedListener(){
            override fun preLayoutChange(stateId: Int, constraintId: Int) {

                Log.e("TAG", "preLayoutChange: " )
                dialog.show()
                super.preLayoutChange(stateId, constraintId)
            }

            override fun postLayoutChange(stateId: Int, constraintId: Int) {

                Log.e("TAG", "postLayoutChange: " )
                dialog.dismiss()
                super.postLayoutChange(stateId, constraintId)
            }
        })*/
        /* motionLayout.setOnTouchListener { v, event ->
             Log.e("TAG", "setUpFilterDialog: ")
             when(event.actionMasked){
                 MotionEvent.ACTION_DOWN ->{
                     dialog.dismiss()
                 }
             }


             true
         }*/

        dialog.show()

        var openFilterName = false
        filterName.setOnClickListener {
            if (openFilterName) {
                openFilterName = false
                etFilterName.visibility = View.GONE
            } else {
                openFilterName = true
                etFilterName.visibility = View.VISIBLE
            }
        }

        var openFilterType = false
        filterType.setOnClickListener {
            if (openFilterType) {
                openFilterType = false
                rbType.visibility = View.GONE
            } else {
                openFilterType = true
                rbType.visibility = View.VISIBLE
            }
        }

        var openFilterCategory = false
        filterCategory.setOnClickListener {
            if (openFilterCategory) {
                openFilterCategory = false
                rbFilterCategory.visibility = View.GONE
            } else {
                openFilterCategory = true
                rbFilterCategory.visibility = View.VISIBLE
            }
        }

        var openFilterStatus = false
        filterStatus.setOnClickListener {
            if (openFilterStatus) {
                openFilterStatus = false
                rbFilterStatus.visibility = View.GONE
            } else {
                openFilterStatus = true
                rbFilterStatus.visibility = View.VISIBLE
            }
        }

        var selectedType = -1
        rbType.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById(checkedId) as RadioButton
            when (radioButton.text.toString())
            {
                getString(R.string.rent) -> {
                    selectedType = 0
                }
                getString(R.string.sale) -> {
                    selectedType = 1
                }
            }
        }

        var selectedCategory = -1
        rbFilterCategory.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById(checkedId) as RadioButton
            when (radioButton.text.toString())
            {
                getString(R.string.reidential) -> {
                    selectedCategory = 0
                }
                getString(R.string.commercial) -> {
                    selectedCategory = 1
                }
            }
        }

        var selectedStatus = -1
        rbFilterStatus.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById(checkedId) as RadioButton
            when (radioButton.text.toString())
            {
                getString(R.string.pending) -> {
                    selectedStatus = 0
                }
                getString(R.string.completed) -> {
                    selectedStatus = 1
                }
            }
        }



        /* apply button */
        applyBtn.setOnClickListener {
            val searchName = etFilterName.text.trim().toString()

            Log.e("TAG",
                "setUpFilterDialog: $searchName---$selectedType----$selectedCategory----$selectedStatus"
            )
            
        }

        /* clear button */
        clearBtn.setOnClickListener {
            etFilterName.setText("")
            dialog.findViewById<RadioButton>(R.id.radioRent).isChecked = false
            dialog.findViewById<RadioButton>(R.id.radioSale).isChecked = false
            dialog.findViewById<RadioButton>(R.id.rbResidential).isChecked = false
            dialog.findViewById<RadioButton>(R.id.rbCommercial).isChecked = false
            dialog.findViewById<RadioButton>(R.id.rbPending).isChecked = false
            dialog.findViewById<RadioButton>(R.id.rvCompleted).isChecked = false
            selectedType = -1
            selectedCategory = -1
            selectedStatus = -1
        }

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}