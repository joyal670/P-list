package com.ncomfortsagent.ui.main.home.home.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textview.MaterialTextView
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseFragment
import com.ncomfortsagent.databinding.FragmentEnquiryBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.model.enquiry.AgentHomeEnquiryPropertyDetails
import com.ncomfortsagent.model.enquiry.AgentHomeEnquiryPropertyPriorityImage
import com.ncomfortsagent.model.enquiry.AgentHomeEnquiryUserProperty
import com.ncomfortsagent.model.enquiry.AgentHomeEnquiryUserRel
import com.ncomfortsagent.ui.main.home.detailspage.enquirydetails.activity.EnquiryDetailsActivity
import com.ncomfortsagent.ui.main.home.home.adapter.EnquiryAdapter
import com.ncomfortsagent.ui.main.home.home.viewmodel.HomeViewModel
import com.ncomfortsagent.utils.CommonUtils.Companion.showCookieBar
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Status
import com.ncomfortsagent.utils.addOnScrolledToEnd
import com.ncomfortsagent.utils.isConnected
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import android.view.animation.TranslateAnimation

class EnquiryFragment : BaseFragment() {
    private lateinit var binding: FragmentEnquiryBinding
    private lateinit var enquiryAdapter: EnquiryAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var enquiryData = ArrayList<AgentHomeEnquiryUserProperty>()
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnquiryBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        binding.swipeToRefresh.isRefreshing = false
        homeViewModel.enquiry(page.toString(),1)
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

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        homeViewModel.getAgentEnquiryResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {

                    if (enquiryData.size == 0) {
                        dismissProgress()
                    }

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
                        showProgress()
                    }
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireContext().isConnected) {
                        showCookieBar(
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
                    dismissProgress()
                    showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
            }
        }
    }

    override fun onClicks() {

        binding.swipeToRefresh.setOnRefreshListener {
            refreshData()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshData() {
        page = 1
        enquiryData.clear()
        try {
            enquiryAdapter.notifyDataSetChanged()
        } catch (ex: Exception) {
        }
        initData()
    }

    private fun selectedItem(selectedItemId: Int) {
        val intent = Intent(requireContext(), EnquiryDetailsActivity::class.java)
        intent.putExtra(Constants.ENQUIRY_ID, selectedItemId.toString())
        startActivity(intent)
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

        lv.setOnClickListener {
            motionLayout.transitionToEnd()
        }

        val moveAnim = TranslateAnimation(600F, 0F, 0F, 0F)
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


        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}