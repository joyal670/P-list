package com.protenium.irohub.ui.main.dashboard.calender;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseFragment;
import com.protenium.irohub.model.SubscriptionInfo.Data;
import com.protenium.irohub.model.SubscriptionInfo.Pending;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.ui.main.dashboard.calender.menu.MenuActivity;
import com.protenium.irohub.utils.CommonUtils;
import com.protenium.irohub.utils.CompletedDecorator;
import com.protenium.irohub.utils.MySelectorDecorator;
import com.protenium.irohub.utils.OneDayDecorator;
import com.protenium.irohub.utils.PauseDecorator;
import com.protenium.irohub.utils.ResumeDecorator;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CalenderFragment extends BaseFragment implements OnDateSelectedListener {

    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;

    @BindView(R.id.tvStartingDate)
    TextView tvStartingDate;

    @BindView(R.id.tvEndDate)
    TextView tvEndDate;

    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    @BindView(R.id.editMealsBtn)
    MaterialButton editMealsBtn;

    @BindView(R.id.editMealsLayout)
    LinearLayout editMealsLayout;

    @BindView(R.id.resumeMealsLayout)
    LinearLayout resumeMealsLayout;

    @BindView(R.id.pauseMealsLayout)
    LinearLayout pauseMealsLayout;

    @BindView(R.id.successfullLayout)
    LinearLayout successfullLayout;

    @BindView(R.id.noMealsLayout)
    LinearLayout noMealsLayout;

    @BindView(R.id.chooseMealsBtn1)
    MaterialButton chooseMealsBtn1;

    @BindView(R.id.pauseMealsLayoutEdit)
    LinearLayout pauseMealsLayoutEdit;

    private CalenderViewModel calenderViewModel;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private String mealSubId = "";
    private PauseDecorator pauseDecorator;
    private ResumeDecorator resumeDecorator;
    private CompletedDecorator completedDecorator;
    private final ArrayList<CalendarDay> pauseDates = new ArrayList<>();
    private final ArrayList<CalendarDay> resumeDates = new ArrayList<>();
    private final ArrayList<CalendarDay> completedDates = new ArrayList<>();

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calender, container, false);
        ButterKnife.bind(this, view);
        calenderViewModel = ViewModelProviders.of(this).get(CalenderViewModel.class);
        calenderViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
                mainLayout.setVisibility(View.GONE);

            } else {
                dismissProgress();
                mainLayout.setVisibility(View.VISIBLE);
            }
        });
        LocalDate instance = LocalDate.now();
        calendarView.setSelectedDate(instance);

        init();

        return view;
    }

    private void init() {
        pauseDates.clear();
        resumeDates.clear();
        completedDates.clear();
        getSubscription();
        setOnClick();
    }

    private void setOnClick() {
        editMealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), MenuActivity.class);
                intent.putExtra("date", calendarView.getSelectedDate().getDate().toString());
                intent.putExtra("mealId", mealSubId);
                startActivity(intent);
            }
        });

        chooseMealsBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), MenuActivity.class);
                intent.putExtra("date", calendarView.getSelectedDate().getDate().toString());
                intent.putExtra("mealId", mealSubId);
                startActivity(intent);
            }
        });

        pauseMealsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calenderViewModel.getSuspendUnsuspendDelivery(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""),calendarView.getSelectedDate().getDate().toString(), "en").observe(CalenderFragment.this, commonResponse -> {
                    CommonUtils.showWarning(requireContext(), commonResponse.getMessage());
                    init();
                });
            }
        });

        resumeMealsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calenderViewModel.getSuspendUnsuspendDelivery(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""),calendarView.getSelectedDate().getDate().toString(), "en").observe(CalenderFragment.this, commonResponse -> {
                    CommonUtils.showWarning(requireContext(), commonResponse.getMessage());
                    init();
                });
            }
        });

        pauseMealsLayoutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calenderViewModel.getSuspendUnsuspendDelivery(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""),calendarView.getSelectedDate().getDate().toString(), "en").observe(CalenderFragment.this, commonResponse -> {
                    CommonUtils.showWarning(requireContext(), commonResponse.getMessage());
                    init();
                });
            }
        });
    }

    private void getSubscription() {
        calenderViewModel.getSubscription(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""), "en").observe(this, response -> {
            if (response.getStatus()) {
                setUpUi(response.getData());
            } else {
                CommonUtils.showWarning(requireContext(), response.getMessage());
            }
        });
    }

    private void setUpUi(Data data) {
        tvStartingDate.setText(data.getPlanStartDate());
        tvEndDate.setText(data.getPlanEndDate());
        calendarView.state().edit().setMinimumDate(CalendarDay.from(LocalDate.parse(data.getPlanStartDate()))).setMaximumDate(CalendarDay.from(LocalDate.parse(data.getPlanEndDate()))).commit();
        calendarView.removeDecorators();
        calendarView.invalidateDecorators();
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE | MaterialCalendarView.SHOW_DECORATED_DISABLED);

        pauseDecorator = new PauseDecorator(requireActivity(), getPauseEvents(data.getOrderStatuses().getUserSuspended()));
        resumeDecorator = new ResumeDecorator(requireActivity(), getResumeEvents(data.getOrderStatuses().getPending()));
        completedDecorator = new CompletedDecorator(requireActivity(), getCompletedEvents(data.getOrderStatuses().getCompleted()));

        calendarView.addDecorators(oneDayDecorator, new MySelectorDecorator(requireActivity()), completedDecorator, pauseDecorator, resumeDecorator);
        editMealsLayout.setVisibility(View.GONE);
        mealSubId = String.valueOf(data.getId());
        calendarView.setOnDateChangedListener(this);
    }

    private ArrayList<CalendarDay> getCompletedEvents(List<Pending> completed) {
        for (int i = 0; i < completed.size(); i++) {
            completedDates.add(CalendarDay.from(LocalDate.parse(completed.get(i).getDate())));
        }
        return completedDates;
    }

    private ArrayList<CalendarDay> getResumeEvents(List<Pending> pending) {
        for (int i = 0; i < pending.size(); i++) {
            resumeDates.add(CalendarDay.from(LocalDate.parse(pending.get(i).getDate())));
        }
        return resumeDates;
    }

    private ArrayList<CalendarDay> getPauseEvents(List<Pending> userSuspended) {
        for (int i = 0; i < userSuspended.size(); i++) {
            pauseDates.add(CalendarDay.from(LocalDate.parse(userSuspended.get(i).getDate())));
        }
        return pauseDates;
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();

        /*editMealsLayout.setVisibility(View.GONE);
        resumeMealsLayout.setVisibility(View.GONE);
        pauseMealsLayout.setVisibility(View.GONE);*/

       // noMealsLayout.setVisibility(View.GONE);

        successfullLayout.setVisibility(View.GONE);
        editMealsLayout.setVisibility(View.GONE);
        resumeMealsLayout.setVisibility(View.GONE);
        pauseMealsLayout.setVisibility(View.VISIBLE);
        noMealsLayout.setVisibility(View.GONE);


        for (int i=0; i<completedDates.size(); i++)
        {
            if(completedDates.get(i).getDate().equals(calendarView.getSelectedDate().getDate())){
                successfullLayout.setVisibility(View.VISIBLE);
                editMealsLayout.setVisibility(View.GONE);
                resumeMealsLayout.setVisibility(View.GONE);
                pauseMealsLayout.setVisibility(View.GONE);
                noMealsLayout.setVisibility(View.GONE);
                break;
            }
        }

        for (int i=0; i<pauseDates.size(); i++)
        {
            if(pauseDates.get(i).getDate().equals(calendarView.getSelectedDate().getDate())){
                Log.e("TAG", "onDateSelected: ");
                resumeMealsLayout.setVisibility(View.VISIBLE);
                successfullLayout.setVisibility(View.GONE);
                editMealsLayout.setVisibility(View.GONE);
                pauseMealsLayout.setVisibility(View.GONE);
                noMealsLayout.setVisibility(View.GONE);
                break;
            }
        }

        for (int i=0; i<resumeDates.size(); i++)
        {
            if(resumeDates.get(i).getDate().equals(calendarView.getSelectedDate().getDate())){
                editMealsLayout.setVisibility(View.VISIBLE);
                resumeMealsLayout.setVisibility(View.GONE);
                successfullLayout.setVisibility(View.GONE);
                pauseMealsLayout.setVisibility(View.GONE);
                noMealsLayout.setVisibility(View.GONE);
                break;
            }
        }

    }

   /* @Override
    public void onResume() {
        super.onResume();
        init();
    }*/
}