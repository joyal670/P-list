package com.property.propertyagent.owner_panel.ui.main.home.home.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AADataLabels
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.owner.owner_home_property_list.OwnerHomePropertyListProperty
import com.property.propertyagent.modal.owner.owner_payment_list_of_properties.*
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.HomeOwnerActivity
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.SearchPropertyActivity
import com.property.propertyagent.owner_panel.ui.main.home.home.dialog.MonthYearPickerDialog
import com.property.propertyagent.owner_panel.ui.main.home.home.homePropertyDetailedPages.activity.HomePropertyDetailedActivity
import com.property.propertyagent.owner_panel.ui.main.home.home.viewmodel.OwnerHomeViewModel
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_home_owner.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class HomeOwnerFragment : BaseFragment() {

    private lateinit var ownerHomeViewModel: OwnerHomeViewModel
    private val aaChartModel: AAChartModel = AAChartModel()
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private var homePropertyList = ArrayList<OwnerHomePropertyListProperty>()

    private val propertyItemName = ArrayList<String>()
    private val propertyItemId = ArrayList<Int>()

    private var selectedProperty = ""
    private var selectedPropertyId = 0

    private var yearSelected: String = ""
    private var dateSelected: String = ""
    private var yearLiveData = MutableLiveData<String>()
    private var dateLiveData = MutableLiveData<String>()

    private var isLoading = false

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_home_owner, container, false)
    }

    override fun initData() {

        swipeRefreshLayout.setRefreshing(false)

        fragmentTransInterface = activity as HomeOwnerActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.nill))

        ownerHomeViewModel.homePropertyDetails()

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        ownerHomeViewModel = OwnerHomeViewModel()
    }

    override fun setupObserver() {

        /* property details api */
        ownerHomeViewModel.getHomePropertyDetailsResponse()
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {

                        tvNoOfProperty.text = it.data!!.data.property_count.toString()
                        tvOccoupiedProperty.text = it.data.data.occupied_count.toString()
                        tvVacaentProperty.text = it.data.data.vacant_count.toString()
                        tvUnderMaintaceProperty.text = it.data.data.maintenance_count.toString()

                        if (yearSelected.isNullOrBlank()) {
                            val calendar = Calendar.getInstance()
                            yearSelected = calendar[Calendar.YEAR].toString()
                            yearLiveData.value = yearSelected
                        }
                        ownerHomeViewModel.homeStatistics(yearSelected)
                    }
                    Status.LOADING -> {
                        if (!isLoading) {
                            showProgressOwner()
                            isLoading = true
                        }
                    }
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
                        if (requireActivity().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
            }

        /* statistics api */
        ownerHomeViewModel.getHomeStatisticsResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgressOwner()

                        val incomeData = arrayListOf<Int>()
                        val expenseData = arrayListOf<Int>()

                        incomeData.clear()
                        expenseData.clear()

                        it.data!!.data.revenue.forEach {
                            incomeData.add(it.toDouble().roundToInt())
                        }

                        it.data.data.expense.forEach {
                            expenseData.add(it.toDouble().roundToInt())
                        }

                        setUpStatisticsChart(incomeData, expenseData)

                        ownerHomeViewModel.homePropertyList()

                    }
                    Status.LOADING -> {
                        if (!isLoading) {
                            showProgressOwner()
                        }
                    }
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
                        if (requireActivity().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
            })

        /* property list api */
        ownerHomeViewModel.getHomePropertyListResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgressOwner()

                        homePropertyList.clear()
                        homePropertyList.addAll(it.data!!.properties)

                        setUpPropertyListSpinner()
                    }
                    Status.LOADING -> {
                        if (!isLoading) {
                            showProgressOwner()
                            isLoading = true
                        }
                    }
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
                        if (requireActivity().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()

                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
            })

        /* monthly account api */
        ownerHomeViewModel.getHomeMonthlyAccountResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgressOwner()
                        isLoading = false

                        setupHalfPieChart(it.data!!.data.revenue, it.data.data.expense)

                    }
                    Status.LOADING -> {
                        if (!isLoading) {
                            showProgressOwner()
                            isLoading = true
                        }
                    }
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
                        if (requireActivity().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
            })

        ownerHomeViewModel.getHomeYearlyReportResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgressOwner()
                        if (it.data!!.data.pdf.isNotBlank()) {
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(it.data!!.data.pdf))
                            startActivity(browserIntent)
                        }
                    }
                    Status.LOADING -> {
                        if (!isLoading) {
                            showProgressOwner()
                        }
                    }
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
                        if (requireActivity().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
            })

        ownerHomeViewModel.getHomeMonthlyReportResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgressOwner()
                        if (it.data!!.data.pdf.isNotBlank()) {
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(it.data!!.data.pdf))
                            startActivity(browserIntent)
                        }
                    }
                    Status.LOADING -> {
                        if (!isLoading) {
                            showProgressOwner()
                        }
                    }
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
                        if (requireActivity().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
            })

        yearLiveData.observe(this){
            homefrgOwner_MonthPickerBtn1.text = yearLiveData.value.toString()
        }

        dateLiveData.observe(this){
            homefrgOwner_MonthPickerBtn2.text = requireActivity().getDateFormatter(dateLiveData.value.toString())
        }
    }

    /* setup property list spinner */
    private fun setUpPropertyListSpinner() {

        propertyItemId.clear()
        propertyItemName.clear()

        homePropertyList.forEach {
            propertyItemName.add(it.property_name)
            propertyItemId.add(it.id)
        }

        owner_home_spinner.attachDataSource(propertyItemName)

        if (propertyItemName.size <= 1) {
            owner_home_spinner.visibility = View.INVISIBLE
        } else {
            owner_home_spinner.visibility = View.VISIBLE
        }

        if (propertyItemId.size != 0) {
            /* assign first element from arraylist */
            selectedPropertyId = propertyItemId[0]

            /* set property name on half pie chart */
            owner_home_propertyTv.text = propertyItemName[0]
        }


        val daySelected: Int
        val yearSelected: Int
        val monthSelected: Int
        val calendar = Calendar.getInstance()
        daySelected = calendar[Calendar.DAY_OF_MONTH]
        yearSelected = calendar[Calendar.YEAR]
        monthSelected = calendar[Calendar.MONTH]

        val selectedDate =
            daySelected.toString() + "-" + (monthSelected + 1).toString() + "-" + yearSelected.toString()

        if (dateSelected.isNullOrBlank()) {
            dateSelected = selectedDate
            dateLiveData.value = dateSelected
        }
        /* api call */
        ownerHomeViewModel.homeMonthlyAccount(selectedPropertyId, selectedDate)

        owner_home_spinner.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            selectedProperty = parent.getItemAtPosition(position) as String
            selectedPropertyId = propertyItemId[position]

            owner_home_propertyTv.text = selectedProperty

            ownerHomeViewModel.homeMonthlyAccount(selectedPropertyId, selectedDate)
        }
    }

    /* setup statistics chart */
    private fun setUpStatisticsChart(incomeData: ArrayList<Int>, expenseData: ArrayList<Int>) {
        aaChartModel
            .chartType(AAChartType.Column)
            .backgroundColor("#FFFFFF")
            .borderRadius(2f)
            .categories(
                arrayOf(
                    "jan",
                    "feb",
                    "mar",
                    "apr",
                    "may",
                    "jun",
                    "july",
                    "aug",
                    "sep",
                    "oct",
                    "nov",
                    "dec"
                )
            )
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Income")
                        .color("#27AE60")
                        .dataLabels(AADataLabels())
                        .data(incomeData.toArray()),
                    AASeriesElement()
                        .name("Expense")
                        .color("#F6A71A")
                        .data(expenseData.toArray())
                )
            )
        aa_chart_view.aa_drawChartWithChartModel(aaChartModel)
    }

    override fun onClicks() {

        homefrgOwner_noOfProperty.setOnClickListener {
            val intent = Intent(context, HomePropertyDetailedActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.NOOFPROPERTY.name)
            startActivity(intent)
        }
        homefrgOwner_occupiedProperty.setOnClickListener {
            val intent = Intent(context, HomePropertyDetailedActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.OCCUPIEDPROPERTY.name)
            startActivity(intent)
        }
        homefrgOwner_vacantProperty.setOnClickListener {
            val intent = Intent(context, HomePropertyDetailedActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.VACANTPROPERTY.name)
            startActivity(intent)
        }

        homefrgOwner_underMaintanceProperty.setOnClickListener {
            val intent = Intent(context, HomePropertyDetailedActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.UNDERMAINTANCE.name)
            startActivity(intent)

        }

        homefrgOwner_MonthPickerBtn1.setOnClickListener {
            setUpYearPickerDialog()
        }

        homefrgOwner_MonthPickerBtn2.setOnClickListener {
            setUpMonthYearPickerDialog()
        }

        svSearchView1.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val intent = Intent(context, SearchPropertyActivity::class.java)
                intent.putExtra(Constants.SEARCH_PROPERTY_NAME, svSearchView1.text.toString())
                startActivity(intent)
                return@setOnEditorActionListener true
            }
            false
        }

        btnYearlyReport.setOnClickListener {
            ownerHomeViewModel.homeYearlyReport(yearSelected)
        }

        btnMonthlyReport.setOnClickListener {
            ownerHomeViewModel.homeMonthlyReport(selectedPropertyId.toString(), dateSelected)
        }

        swipeRefreshLayout.setOnRefreshListener {
            initData()
        }
    }

    /* Dialog for year picker */
    private fun setUpYearPickerDialog() {
        MonthYearPickerDialog().apply {
            setListener(DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                ownerHomeViewModel.homeStatistics(year.toString())
                yearSelected = year.toString()
                yearLiveData.value = yearSelected
            })
            show(this@HomeOwnerFragment.parentFragmentManager, "MonthYearPickerDialog")
        }
    }

    private fun setUpMonthYearPickerDialog() {

        val daySelected: Int
        val yearSelected: Int
        val monthSelected: Int
        val calendar = Calendar.getInstance()
        daySelected = calendar[Calendar.DAY_OF_MONTH]
        yearSelected = calendar[Calendar.YEAR]
        monthSelected = calendar[Calendar.MONTH]

        val dialogFragment = MonthYearPickerDialogFragment.getInstance(monthSelected, yearSelected)

        dialogFragment.show(parentFragmentManager, null)

        dialogFragment.setOnDateSetListener { year, monthOfYear ->
            val selectedDate =
                daySelected.toString() + "-" + (monthOfYear + 1).toString() + "-" + year.toString()

            dateSelected = selectedDate
            dateLiveData.value = dateSelected

            /* api call */
            ownerHomeViewModel.homeMonthlyAccount(selectedPropertyId, selectedDate)
        }
    }

    private fun setupHalfPieChart(revenue: String, expense: String) {
        halfpiechart1.setBackgroundColor(Color.WHITE)
        halfpiechart1.setUsePercentValues(true)
        halfpiechart1.description.isEnabled = false

        halfpiechart1.isDrawHoleEnabled = true
        halfpiechart1.setHoleColor(Color.WHITE)

        halfpiechart1.setTransparentCircleColor(Color.WHITE)
        halfpiechart1.setTransparentCircleAlpha(110)

        halfpiechart1.holeRadius = 58f
        halfpiechart1.transparentCircleRadius = 61f

        halfpiechart1.setDrawCenterText(true)

        halfpiechart1.isRotationEnabled = false
        halfpiechart1.isHighlightPerTapEnabled = true

        halfpiechart1.maxAngle = 180f // HALF CHART

        halfpiechart1.rotationAngle = 180f
        halfpiechart1.setCenterTextOffset(0f, -20f)

        setData(1, revenue, expense)

        val l: Legend = halfpiechart1.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling
        halfpiechart1.setDrawEntryLabels(false)
        halfpiechart1.setEntryLabelColor(Color.WHITE)
        halfpiechart1.setEntryLabelTextSize(10f)

    }

    private fun setData(count: Int, revenue: String, expense: String) {

        val values = ArrayList<PieEntry>()
        for (i in 0 until count) {
            values.add(PieEntry(revenue.toFloat(), "Income"))
            values.add(PieEntry(expense.toFloat(), "Expense"))
        }

        val dataSet = PieDataSet(values, "")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        dataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
        //dataSet.setSelectionShift(0f);

        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setDrawValues(false)
        data.setValueTextColor(Color.WHITE)
        halfpiechart1.data = data

        halfpiechart1.invalidate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_lineView)
        item2.isVisible = false
    }

}