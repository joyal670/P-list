package com.ncomfortsagent.ui.main.home.home.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseFragment
import com.ncomfortsagent.databinding.FragmentMyPropertyBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.model.property.*
import com.ncomfortsagent.ui.main.home.detailspage.enquirydetails.activity.EnquiryDetailsActivity
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.activity.PropertyBuildingDetailsActivity
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.activity.PropertyDetailsActivity
import com.ncomfortsagent.ui.main.home.home.adapter.PropertyAdapter
import com.ncomfortsagent.ui.main.home.home.viewmodel.HomeViewModel
import com.ncomfortsagent.utils.*
import com.ncomfortsagent.utils.CommonUtils.Companion.showCookieBar

class MyPropertyFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentMyPropertyBinding
    private lateinit var homeViewModel: HomeViewModel
    private var myProperty = ArrayList<PropertyNewUserProperty>()
    private lateinit var propertyAdapter: PropertyAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPropertyBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        binding.swipeToRefresh.isRefreshing = false
        homeViewModel.myProperty(page.toString())

        /* *//* show tutorial dialog *//*
        *//* only for one time *//*
        if (!prefIsTutorial) {
            val dialog = TutorialDialogFragment()
            dialog.show(parentFragmentManager, "TAG")
            prefIsTutorial = true
        }*/

    }

    private fun setupRecyclerView() {
        propertyAdapter = PropertyAdapter(
            requireActivity(),
            { item: Int, type: Int, occupiedStatus: Int -> selectedItem(item, type, occupiedStatus) },
            myProperty
        )
        binding.rvProperty.adapter = propertyAdapter
        binding.rvProperty.scheduleLayoutAnimation()
        binding.rvProperty.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    private fun loadData() {
        if (page <= totalPages) {
            myProperty.add(
                PropertyNewUserProperty(
                    -1,
                    listOf(
                        PropertyNewBuildingApartment(
                            -1,
                            -1,
                            PropertyNewPropertyPriorityImage("", 1),
                            -1, PropertyNewTypeDetails(-1, "")
                        )
                    ),
                    "",
                    -1,
                    -1,
                    "",
                    -1,
                    -1,
                    "",
                    PropertyNewPropertyPriorityImageX("", -1),
                    -1,
                    "",
                    PropertyNewTypeDetails(-1, ""),
                    -1,
                    -1
                )
            )
            propertyAdapter.notifyItemInserted(myProperty.size - 1)
            homeViewModel.myProperty(page.toString())
        }
    }

    override fun setupUI() {
        binding.rvProperty.layoutManager = LinearLayoutManager(context)
    }

    override fun setupViewModel() {
        homeViewModel = HomeViewModel(requireActivity())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {

        /* my property */
        homeViewModel.getAgentMyPropertyResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (myProperty.size == 0) {
                        dismissProgress()
                    }

                    if (it.data!!.response != null) {
                        totalPages = it.data.response.total_page_count
                        if (myProperty.size != 0) {
                            isLoading = false
                            page += 1
                            myProperty.removeAt(myProperty.size - 1)
                            propertyAdapter.notifyItemRemoved(myProperty.size)
                            myProperty.addAll(it.data.response.user_properties)
                            propertyAdapter.notifyDataSetChanged()
                        } else {
                            page += 1
                            myProperty =
                                it.data.response.user_properties as ArrayList<PropertyNewUserProperty>
                            setupRecyclerView()
                        }

                        if (myProperty.size == 0) {
                            binding.rvProperty.visibility = View.GONE
                            binding.noResult.noData.visibility = View.VISIBLE
                        } else {
                            binding.rvProperty.visibility = View.VISIBLE
                            binding.noResult.noData.visibility = View.GONE
                        }
                    }
                }
                Status.LOADING -> {
                    if (myProperty.size == 0) {
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

        /* my property search */
        homeViewModel.getAgentMyPropertySearchResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {

                    myProperty.clear()
                    propertyAdapter.notifyDataSetChanged()

                    if (it.data!!.response != null) {
                        totalPages = it.data.response.total_page_count
                        if (myProperty.size != 0) {
                            isLoading = false
                            page += 1
                            myProperty.removeAt(myProperty.size - 1)
                            propertyAdapter.notifyItemRemoved(myProperty.size)
                            myProperty.addAll(it.data.response.user_properties)
                            propertyAdapter.notifyDataSetChanged()
                        } else {
                            page += 1
                            myProperty =
                                it.data.response.user_properties as ArrayList<PropertyNewUserProperty>
                            setupRecyclerView()
                        }

                        if (myProperty.size == 0) {
                            binding.rvProperty.visibility = View.GONE
                            binding.noResult.noData.visibility = View.VISIBLE
                        } else {
                            binding.rvProperty.visibility = View.VISIBLE
                            binding.noResult.noData.visibility = View.GONE
                        }
                    }
                }
                Status.LOADING -> {

                }
                Status.NO_INTERNET -> {

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

                    showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.DATA_EMPTY -> {

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
        myProperty.clear()
        propertyAdapter.notifyDataSetChanged()
        initData()
    }

    private fun selectedItem(selectedItemId: Int, type: Int, occupiedStatus: Int) {
        /* type-> 0 = Individual property */
        /* type-> 1 = Building */
        if (type == 0) {

            when (occupiedStatus){
                0 ->{
                    val intent = Intent(requireContext(), PropertyDetailsActivity::class.java)
                    intent.putExtra(Constants.PROPERTY_ID, selectedItemId)
                    startActivity(intent)
                }

                1 ->{
                    val intent = Intent(requireContext(), EnquiryDetailsActivity::class.java)
                    intent.putExtra(Constants.ENQUIRY_ID, "135")
                    intent.putExtra("enquiry_type","1")
                    startActivity(intent)
                }
            }

        } else if (type == 1) {
            val intent = Intent(requireContext(), PropertyBuildingDetailsActivity::class.java)
            intent.putExtra(Constants.PROPERTY_ID, selectedItemId)
            startActivity(intent)
        }

    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customToolbarSearch)
        item1.isVisible = true

        val item2: MenuItem = menu.findItem(R.id.customToolbarFilter)
        item2.isVisible = false

    }

    /* hide text in search */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.custom_menu, menu)
        val one =
            menu.findItem(R.id.customToolbarSearch)?.actionView?.findViewById<ImageView>(R.id.ivSearch)
        val two =
            menu.findItem(R.id.customToolbarSearch)?.actionView?.findViewById<TextView>(R.id.tvSearch)
        val three =
            menu.findItem(R.id.customToolbarSearch)?.actionView?.findViewById<SearchView>(R.id.sdd)

        one!!.setOnClickListener {
            one.visibility = View.GONE
            two!!.visibility = View.GONE
            three!!.isVisible = true
            three.queryHint = requireActivity().getString(R.string.search)
        }

        two!!.setOnClickListener {
            one.visibility = View.GONE
            two.visibility = View.GONE
            three!!.isVisible = true
            three.queryHint = requireActivity().getString(R.string.search)
        }

        three!!.setIconifiedByDefault(false)
        val clearButton: ImageView = three.findViewById(androidx.appcompat.R.id.search_close_btn)
        clearButton.setOnClickListener {
            three.isVisible = false
            one.isVisible = true
            two.isVisible = true
            three.setQuery("", true)

        }

        three.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        page = 1
        homeViewModel.myPropertySearch(newText!!, page.toString())
        return false
    }

}