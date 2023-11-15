package com.proteinium.proteiniumdietapp.ui.main.home.calender.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseFragment
import com.proteinium.proteiniumdietapp.listeners.ActivityListener
import com.proteinium.proteiniumdietapp.pojo.subscption_info.Food
import com.proteinium.proteiniumdietapp.pojo.subscption_info.OrderItem
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.user_id
import com.proteinium.proteiniumdietapp.ui.main.home.HomeActivity
import com.proteinium.proteiniumdietapp.ui.main.home.calender.fragment.adapter.CalenderAdapter
import com.proteinium.proteiniumdietapp.ui.main.home.calender.meals_selection.activity.MenuActivity
import com.proteinium.proteiniumdietapp.ui.start_up.auth.AuthActivity
import com.proteinium.proteiniumdietapp.ui.start_up.language_selection.LangaugeSelectionActivity
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.Status.*
import com.proteinium.proteiniumdietapp.utils.calendar.*
import kotlinx.android.synthetic.main.fragment_calender.*
import org.threeten.bp.LocalDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class CalenderFragment : BaseFragment(), OnDateSelectedListener {
    private var isLoaded = false
    private lateinit var fragmentTransInterface: ActivityListener
    private val oneDayDecorator: OneDayDecorator =
        OneDayDecorator()
    private lateinit var calendarViewModel: CalendarViewModel

    var calenderType = arrayListOf<String>()
    val completedDates = ArrayList<CalendarDay>()
    val pauseDates = ArrayList<CalendarDay>()
    val resumeDates = ArrayList<CalendarDay>()
    val holiDates = ArrayList<CalendarDay>()
    val offDates = ArrayList<CalendarDay>()
    var hashMapFood: HashMap<String, List<Food>> = HashMap<String, List<Food>>()
    private var saveId = ""
    private var completedList: List<OrderItem> = listOf()
    private var userSuspendedList: List<OrderItem> = listOf()
    private var pendingList: List<OrderItem> = listOf()
    private var nonStop = 0
    private lateinit var pauseDecorator: EventPauseDecorator
    private lateinit var resumeDecorator: EventResumeDecorator
    private lateinit var holidayDecorator: HolidayDecorator
    private lateinit var eventFreezeDecorator: EventFreezeDecorator
    private var emptyDisabledDays: List<String> = listOf()
    private var globalSuspensionList = ArrayList<String>()
    private var planEndDate = ""
    private var upComingStartDate = ""
    private var active_plan_end_date = ""
    private var upComingStatus = false
    private var isLodedCalendar = false
    private var freezeDate = ""
    private var freezeStatus = 0
    private var freezeDateList=ArrayList<CalendarDay>()


    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_calender, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as HomeActivity
        fragmentTransInterface.setTitle(getString(R.string.title_nill), 16f, R.font.segoe_ui, false)
        fragmentTransInterface.setBackButton(true)
        fragmentTransInterface.hideToolbar(true)
        setupViewModel()
        setupObserver()
        if (AppPreferences.chooseLanguage == Constants.ENGLISH_LAG) {
            ivArrow.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_arrow_right_white))
        } else {
            calendarView.rotationY = 180f
            ivArrow.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_arrow_back_toolbar))
        }

    }

    override fun onResume() {
        super.onResume()
        if (AppPreferences.isLogin) {
            calendarViewModel.fetchSubscriptionInfo(user_id!!)
        } else {

//            println("First day: " + today.withDayOfMonth(1))
//            println("Last day: " + today.withDayOfMonth(12-today.lengthOfMonth()-today.lengthOfMonth()))
            calendarViewModel.getGlobalSuspensionLiveData()

        }
    }

    override fun setupUI() {


        calendarView.setOnDateChangedListener(this)


    }

    override fun setupViewModel() {
        calendarViewModel = CalendarViewModel()

    }

    @SuppressLint("ResourceType")
    private fun setupCalender(
        planStartDate: String,
        planEndDate: String,
        completed: List<OrderItem>,
        userSuspended: List<OrderItem>,
        pending: List<OrderItem>,
        nonStopDeliveryStatus: Int,
        offDays: List<String>,
        freezeStatus: Int,
        freezeDate: String
    ) {
        if (AppPreferences.isLogin) {
            if (AppPreferences.isPlan) {
                llCstart.visibility = View.VISIBLE
                tvStartingDate.text = planStartDate
                tvEndDate.text = planEndDate
            }

        }

        noSubscriptionLayout.visibility = View.GONE
        mainLayout.visibility = View.VISIBLE
        val calndr = Calendar.getInstance()
        calndr.firstDayOfWeek = Calendar.WEDNESDAY
        calendarView.rotationY = -360f
        calendarView.removeDecorators()
        calendarView.invalidateDecorators()
        calendarView.setWeekDayLabels(resources.getTextArray(R.array.custom_weekdays))
        calendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
        calendarView.state().edit()
            .setMinimumDate(CalendarDay.from(LocalDate.parse(planStartDate)))
            .setMaximumDate(CalendarDay.from(LocalDate.parse(planEndDate)))
            .commit()
        calendarView.showOtherDates =
            MaterialCalendarView.SHOW_OUT_OF_RANGE or MaterialCalendarView.SHOW_DECORATED_DISABLED
        val instance = LocalDate.now()

        if (calendarView.selectedDate != null) {
            editMealsLayout.visibility = View.GONE
            resumeMealsLayout.visibility = View.GONE
            pauseMealsLayout.visibility = View.GONE
            successfullLayout.visibility = View.GONE
            noMealsLayout.visibility = View.VISIBLE
            calendarView.setSelectedDate(instance)
            calendarView.isSelected = false


        }

        eventFreezeDecorator=EventFreezeDecorator(activity,freezeDateList,calenderType,"")
        pauseDecorator = EventPauseDecorator(
            activity,
            getPauseEvents(userSuspended),
            calenderType, ""
        )
        resumeDecorator = EventResumeDecorator(
            activity,
            getRemumeEvents(pending),
            calenderType
        )
        holidayDecorator = HolidayDecorator(activity, getHoliday(globalSuspensionList))

        setOffDaysDecorator(offDays, planStartDate, planEndDate, nonStopDeliveryStatus, completed)

    }

    private fun getHoliday(globalSuspensionList: ArrayList<String>): ArrayList<CalendarDay> {
        globalSuspensionList.forEach {
            holiDates.add(CalendarDay.from(LocalDate.parse(it)))
        }
        return holiDates
    }

    private fun getDatesBetween(dateString1: String, dateString2: String): List<String> {
        val dates = ArrayList<String>()
        val input = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = input.parse(dateString1)
            date2 = input.parse(dateString2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        while (!cal1.after(cal2)) {
            val output = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dates.add(output.format(cal1.time))
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }

    private fun setOffDaysDecorator(
        offDays: List<String>,
        planStartDate: String,
        planEndDate: String,
        nonStopDeliveryStatus: Int,
        completed: List<OrderItem>,
    ) {

        if (offDays.isEmpty()) {
            if (nonStopDeliveryStatus == 1) {
                calendarView.addDecorators(
                    EventDecorator(
                        activity,
                        getEvents(completed),
                        calenderType
                    ),
                    MySelectorDecorator(
                        activity
                    ),
                    OneDayDecorator(),
                    pauseDecorator,
                    resumeDecorator,
                    holidayDecorator,
                    eventFreezeDecorator
                )
            } else {
                calendarView.addDecorators(
                    EventDecorator(
                        activity,
                        getEvents(completed),
                        calenderType
                    ),
                    MySelectorDecorator(
                        activity
                    ),
                    OneDayDecorator(),
                    NonStopDecorator(),
                    pauseDecorator,
                    resumeDecorator,
                    holidayDecorator,
                    eventFreezeDecorator
                )
            }
        } else {
            offDays.forEach {
                when (it.toUpperCase(Locale.ROOT)) {

                    "MONDAY" -> {
                        if (nonStopDeliveryStatus == 1) {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledMondayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        } else {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                NonStopDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledMondayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        }
                    }

                    "TUESDAY" -> {
                        if (nonStopDeliveryStatus == 1) {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledTuesdayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        } else {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                NonStopDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledTuesdayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        }
                    }

                    "WEDNESDAY" -> {
                        if (nonStopDeliveryStatus == 1) {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledWednesdayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        } else {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                NonStopDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledWednesdayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        }
                    }

                    "THURSDAY" -> {
                        if (nonStopDeliveryStatus == 1) {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledThursdayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        } else {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                NonStopDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledThursdayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        }
                    }

                    "FRIDAY" -> {
                        if (nonStopDeliveryStatus == 1) {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledFridayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        } else {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                NonStopDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledFridayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        }
                    }

                    "SATURDAY" -> {
                        if (nonStopDeliveryStatus == 1) {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledSaturdayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        } else {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                NonStopDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledSaturdayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        }
                    }

                    "SUNDAY" -> {
                        if (nonStopDeliveryStatus == 1) {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledSundayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        } else {
                            calendarView.addDecorators(
                                EventDecorator(
                                    activity,
                                    getEvents(completed),
                                    calenderType
                                ),
                                MySelectorDecorator(
                                    activity
                                ),
                                OneDayDecorator(),
                                NonStopDecorator(),
                                pauseDecorator,
                                resumeDecorator,
                                DisabledSundayDecorator(
                                    activity,
                                    getOffDays(getDatesBetween(planStartDate, planEndDate))
                                ),
                                holidayDecorator,
                                eventFreezeDecorator
                            )
                        }

                    }

                }
            }
        }


    }

    @SuppressLint("SimpleDateFormat", "NewApi")
    override fun setupObserver() {
        calendarViewModel.getGlobalSuspension().observe(this) {
            when (it.status) {
                SUCCESS -> {
                    dismissLoader()
                    globalSuspensionList.addAll(it.data!!.data)
                    val today = LocalDate.now()
                    val LastDatetoday = LocalDate.now().plusMonths(12 - today.monthValue.toLong())
                    setupCalender(
                        today.withDayOfMonth(1).toString(),
                        LastDatetoday.withDayOfMonth(LastDatetoday.lengthOfMonth()).toString(),
                        completedList,
                        userSuspendedList,
                        pendingList,
                        1,
                        emptyDisabledDays,
                        freezeStatus,
                        freezeDate
                    )

                }
                DATA_EMPTY -> {
                    dismissLoader()
                }
                ERROR -> {
                    val today = LocalDate.now()
                    val LastDatetoday = LocalDate.now().plusMonths(12 - today.monthValue.toLong())
                    setupCalender(
                        today.withDayOfMonth(1).toString(),
                        LastDatetoday.withDayOfMonth(LastDatetoday.lengthOfMonth()).toString(),
                        completedList,
                        userSuspendedList,
                        pendingList,
                        1,
                        emptyDisabledDays,
                        freezeStatus,
                        freezeDate
                    )
                    dismissLoader()

                }
                LOADING -> {
                    if (!isLodedCalendar) {
                        showLoader()
                    }


                }
                NO_INTERNET -> {
                    dismissLoader()

                }
            }
        }

        calendarViewModel.getSubscriptionResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                SUCCESS -> {
                    if (it.data!!.blocked == 1) {
                        showExitDialog()
                    }
                    isLoaded = false
                    AppPreferences.isPlan = true
                    dismissLoader()
                    tvStartingDate.text = it.data!!.data.plan_start_date
                    tvEndDate.text = it.data.data.plan_end_date
                    saveId = it.data.data.id.toString()
                    planEndDate = it.data.data.plan_end_date
                    upComingStartDate = it.data.data.upcoming_plan_start_date
                    upComingStatus = it.data.data.upcoming_status
                    active_plan_end_date = it.data.data.active_plan_end_date
                    globalSuspensionList.addAll(it.data.data.global_suspensions)
                    freezeStatus=it.data.data.freeze_status
                    if(freezeStatus==1){
                        freezeDate=it.data.data.freeze_date.toString()
                        getDatesBetween(it.data.data.freeze_date.toString(),it.data.data.plan_end_date).forEach {
                            freezeDateList.add(CalendarDay.from(LocalDate.parse(it)))
                        }
                    }
                    setupCalender(
                        it.data.data.plan_start_date,
                        it.data.data.plan_end_date,
                        it.data.data.order_statuses.completed,
                        it.data.data.order_statuses.user_suspended,
                        it.data.data.order_statuses.pending,
                        it.data.data.non_stop_delivery_status,
                        it.data.data.off_days,
                        freezeStatus,
                        freezeDate
                    )


                }
                DATA_EMPTY -> {
                    isLodedCalendar = true
                    if (!isLoaded) {
                        dismissLoader()
                    }
                    AppPreferences.isPlan = false
                    val today = LocalDate.now()
                    calendarViewModel.getGlobalSuspensionLiveData()
                }
                ERROR -> {
                    isLoaded = false
                    dismissLoader()

                }
                LOADING -> {
                    if (!isLoaded) {
                        isLoaded = true
                        showLoader()
                    }

                }
                NO_INTERNET -> {
                    isLoaded = false
                    dismissLoader()

                }
            }

        })
        calendarViewModel.getSuspendUnsuspendResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                SUCCESS -> {
                    completedDates.clear()
                    pauseDates.clear()
                    resumeDates.clear()
                    calendarViewModel.fetchSubscriptionInfo(user_id!!)
                    CommonUtils.warningToast(requireContext(), it.data!!.message)
                }
                DATA_EMPTY -> {
                    isLoaded = false
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(), it.message!!)

                }

            }

        })

    }

    private fun showExitDialog() {
        val dialog = context?.let { it1 -> Dialog(it1) }
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.alert_remove_an_item)

        val yesBtn = dialog?.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
        val closeBtn = dialog.findViewById(R.id.ivRemoveItemClose) as ImageView
        val tvContent = dialog.findViewById(R.id.tvContent) as TextView
        tvContent.text = requireActivity().getString(R.string.block_message)

        yesBtn.setOnClickListener {
            dialog.dismiss()
            AppPreferences.logoutClearPreference()
            val intent = Intent(requireContext(), LangaugeSelectionActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
            AppPreferences.logoutClearPreference()
            val intent = Intent(requireContext(), LangaugeSelectionActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onClicks() {

        editMealsBtn.setOnClickListener {
            if (AppPreferences.isLogin) {
                val intent = Intent(context, MenuActivity::class.java)
                intent.putExtra(
                    Constants.MEAL_SELECTED_DATE,
                    calendarView.selectedDate?.date.toString()
                )
                intent.putExtra("passedId", saveId)
                startActivity(intent)
            } else {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                startActivity(intent)
            }
        }

        chooseMealsBtn.setOnClickListener {
            if (globalSuspensionList.contains(calendarView.selectedDate?.date.toString())) {
                CommonUtils.warningToast(requireContext(), "The selected day is off-day")
            } else {
                val intent = Intent(context, MenuActivity::class.java)
                intent.putExtra(
                    Constants.MEAL_SELECTED_DATE,
                    calendarView.selectedDate?.date.toString()
                )
                intent.putExtra("passedId", saveId)
                startActivity(intent)
            }
        }
        chooseMealsBtn1.setOnClickListener {
            Log.e("TAG", "onClicks: $globalSuspensionList")
            if (globalSuspensionList.contains(calendarView.selectedDate?.date.toString())) {
                CommonUtils.warningToast(requireContext(), "The selected day is off-day")
            } else {
                val intent = Intent(context, MenuActivity::class.java)
                intent.putExtra(
                    Constants.MEAL_SELECTED_DATE,
                    calendarView.selectedDate?.date.toString()
                )
                intent.putExtra("passedId", saveId)
                startActivity(intent)
            }


        }
        pauseMealsLayout.setOnClickListener {
            if (AppPreferences.isLogin) {
                calendarViewModel.fetchSuspendUnsuspendLiveData(
                    user_id!!,
                    calendarView.selectedDate?.date.toString(),
                    planEndDate,
                    upComingStartDate,
                    upComingStatus,
                    active_plan_end_date
                )
            } else {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                startActivity(intent)
            }
        }
        resumeMealsLayout.setOnClickListener {
            if (AppPreferences.isLogin) {
                calendarViewModel.fetchSuspendUnsuspendLiveData(
                    user_id!!,
                    calendarView.selectedDate?.date.toString(),
                    planEndDate,
                    upComingStartDate,
                    upComingStatus,
                    active_plan_end_date
                )
            } else {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                startActivity(intent)
            }
        }
        pauseMealsLayoutEdit.setOnClickListener {
            if (AppPreferences.isLogin) {
                calendarViewModel.fetchSuspendUnsuspendLiveData(
                    user_id!!,
                    calendarView.selectedDate?.date.toString(),
                    planEndDate,
                    upComingStartDate,
                    upComingStatus,
                    active_plan_end_date
                )
            } else {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getRemumeEvents(pending: List<OrderItem>): ArrayList<CalendarDay> {
        pending.forEach {
            resumeDates.add(CalendarDay.from(LocalDate.parse(it.date)))
        }
        return resumeDates
    }

    private fun getPauseEvents(pending: List<OrderItem>): ArrayList<CalendarDay> {
        pending.forEach {
            pauseDates.add(CalendarDay.from(LocalDate.parse(it.date)))
        }
        return pauseDates
    }


    private fun getEvents(completed: List<OrderItem>): ArrayList<CalendarDay> {
        completed.forEach {
            completedDates.add(CalendarDay.from(LocalDate.parse(it.date)))
            hashMapFood[it.date] = it.foods
        }
        return completedDates
    }

    private fun getOffDays(offDay: List<String>): ArrayList<CalendarDay> {
        offDay.forEach {
            offDates.add(CalendarDay.from(LocalDate.parse(it)))
        }
        return offDates
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {

        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.date)
        widget.invalidateDecorators()

        Log.e("TAG", "onDateSelected: " + calendarView.selectedDate?.date)

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(Date())
        var convertedDate = Date()
        var convertedDate2 = Date()
        convertedDate = sdf.parse(date.date.toString())
        convertedDate2 = sdf.parse(currentDate)

        pauseMealsLayout.visibility = View.VISIBLE
        successfullLayout.visibility = View.GONE
        editMealsLayout.visibility = View.GONE
        resumeMealsLayout.visibility = View.GONE
        noMealsLayout.visibility = View.GONE

        completedDates.forEach {
            if (it.date == calendarView.selectedDate?.date) {
                successfullLayout.visibility = View.VISIBLE
                editMealsLayout.visibility = View.GONE
                resumeMealsLayout.visibility = View.GONE
                pauseMealsLayout.visibility = View.GONE
                noMealsLayout.visibility = View.GONE
                rvCalender.layoutManager = LinearLayoutManager(context)
                rvCalender.adapter = CalenderAdapter(
                    hashMapFood[calendarView.selectedDate?.date.toString()]!!,
                    requireContext()
                ) { food_id: Int, rating: Int ->
                    calendarViewModel.foodRating(
                        user_id!!,
                        food_id,
                        rating,
                        calendarView.selectedDate?.date.toString()
                    )
                }


            }
        }
        pauseDates.forEach {
            if (it.date == calendarView.selectedDate?.date) {
                resumeMealsLayout.visibility = View.VISIBLE
                editMealsLayout.visibility = View.GONE
                pauseMealsLayout.visibility = View.GONE
                successfullLayout.visibility = View.GONE
                noMealsLayout.visibility = View.GONE
            }

        }
        resumeDates.forEach {
            Log.e("123456", "onDateSelected: " + it.date.toString())
            if (it.date == calendarView.selectedDate?.date) {
                editMealsLayout.visibility = View.VISIBLE
                resumeMealsLayout.visibility = View.GONE
                pauseMealsLayout.visibility = View.GONE
                successfullLayout.visibility = View.GONE
                noMealsLayout.visibility = View.GONE
            }
        }
    }

}


