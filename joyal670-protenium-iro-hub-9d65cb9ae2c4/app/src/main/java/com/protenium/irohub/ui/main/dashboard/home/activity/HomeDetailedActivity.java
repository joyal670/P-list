package com.protenium.irohub.ui.main.dashboard.home.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.model.calender.CalenderModel;
import com.protenium.irohub.model.home_detailed.Carb;
import com.protenium.irohub.model.home_detailed.Data;
import com.protenium.irohub.model.home_detailed.Duration;
import com.protenium.irohub.model.home_detailed.MealPlan;
import com.protenium.irohub.model.home_detailed.Protein;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.ui.main.dashboard.home.adapter.OffDaysAdapter;
import com.protenium.irohub.ui.main.dashboard.home.adapter.PlanDurationAdapter;
import com.protenium.irohub.ui.main.dashboard.home.adapter.PlansAdapter;
import com.protenium.irohub.ui.main.dashboard.home.model.DateModel;
import com.protenium.irohub.ui.main.dashboard.home.viewmodel.HomeDetailedViewModel;
import com.protenium.irohub.ui.main.dashboard.profile.activity.DislikesActivity;
import com.protenium.irohub.utils.CommonUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeDetailedActivity extends BaseActivity implements PlanDurationAdapter.SelectedValue, PlansAdapter.SelectedDate {

    private final List<String> listMonths = new ArrayList<>();
    private final Calendar calendar = Calendar.getInstance();
    private final int planStartBuffer = 0;
    private final int start_date_range = 0;
    private final List<Date> dateList = new ArrayList<>();
    private final List<Date> dateListNew = new ArrayList<>();
    private final ArrayList<DateModel> dateModifiedList = new ArrayList<>();
    private final List<Protein> proteins = new ArrayList<>();
    private final List<Carb> carbs = new ArrayList<>();
    private final int proteinKWD = 0;
    @BindView(R.id.vpDetailsImage)
    ImageView vpDetailsImage;
    @BindView(R.id.tvMealPlanCategory)
    TextView tvMealPlanCategory;
    @BindView(R.id.toolbar_btn)
    MaterialButton toolbar_btn;
    @BindView(R.id.tvDescriptionContent)
    TextView tvDescriptionContent;
    @BindView(R.id.rvOffDays)
    RecyclerView rvOffDays;
    @BindView(R.id.rvPlanDuration)
    RecyclerView rvPlanDuration;
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.tv_month)
    TextView tv_month;
    @BindView(R.id.btnRight)
    ImageView btnRight;
    @BindView(R.id.btnLeft)
    ImageView btnLeft;
    @BindView(R.id.rv_single_row_calendar)
    RecyclerView rv_single_row_calendar;
    @BindView(R.id.proteinsCountBtn)
    MaterialButton proteinsCountBtn;
    @BindView(R.id.addProteinsBtn)
    MaterialButton addProteinsBtn;
    @BindView(R.id.removeProteinsBtn)
    MaterialButton removeProteinsBtn;
    @BindView(R.id.tvProteinPrice)
    TextView tvProteinPrice;
    @BindView(R.id.carbsCountBtn)
    MaterialButton carbsCountBtn;
    @BindView(R.id.tvCrabPrice)
    TextView tvCrabPrice;
    @BindView(R.id.addCarbsBtn)
    MaterialButton addCarbsBtn;
    @BindView(R.id.removeCarbsBtn)
    MaterialButton removeCarbsBtn;
    @BindView(R.id.contentDisikeBtn)
    MaterialButton contentDisikeBtn;
    @BindView(R.id.content)
    CoordinatorLayout content;
    @BindView(R.id.contentProceedBtn)
    MaterialButton contentProceedBtn;
    @BindView(R.id.etComments)
    EditText etComments;
    private HomeDetailedViewModel homeDetailedViewModel;
    private String mealsId = "";
    private int currentMonth = 0;
    private int scrollPosition;
    private int currentProteinIndex = 0;
    private int currentCarbIndex = 0;
    private double durationPrice = 0.0;
    private String apiStartDate = "";
    private String apiDuration = "";
    private String apiDurationPrice = "";
    private String apiDurationSuspend = "";

    private static List<CalenderModel> getDates(String dateString1, String dateString2) {
        ArrayList<Date> dates = new ArrayList<>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }

        DateFormat df12 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<CalenderModel> newDate = new ArrayList<>();

        for (int i = 0; i < dates.size(); i++) {
            newDate.add(new CalenderModel(df12.format(dates.get(i)), false));
        }
        return newDate;
    }

    @Override
    public int setLayout() {
        return R.layout.activity_home_detailed;
    }

    @Override
    public boolean setToolbar() {
        return false;
    }

    @Override
    public boolean hideStatusBar() {
        return false;
    }

    @Override
    public boolean setFullScreen() {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mealsId = getIntent().getStringExtra("id");
        homeDetailedViewModel = ViewModelProviders.of(this).get(HomeDetailedViewModel.class);
        homeDetailedViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });

        listMonths.add("Jan");
        listMonths.add("Feb");
        listMonths.add("Mar");
        listMonths.add("Apr");
        listMonths.add("May");
        listMonths.add("Jun");
        listMonths.add("July");
        listMonths.add("Aug");
        listMonths.add("Sep");
        listMonths.add("Oct");
        listMonths.add("Nov");
        listMonths.add("Dec");

        setupUi();
        setOnClicks();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setOnClicks() {

        addProteinsBtn.setOnClickListener(v -> {
            currentProteinIndex++;
            if (currentProteinIndex >= proteins.size()) {
                currentProteinIndex--;

                CommonUtils.showWarning(HomeDetailedActivity.this, "Maximum protein limit reached");
            }
            proteinsCountBtn.setText(proteins.get(currentProteinIndex).getProteins() + "g");
            tvProteinPrice.setText(proteins.get(currentProteinIndex).getProteinsPrice());

            double tempProtein = Double.parseDouble(proteins.get(currentProteinIndex).getProteinsPrice());
            double tempCarbs = Double.parseDouble(carbs.get(currentCarbIndex).getCarbsPrice());
            double tempSubTotal = Double.sum(tempProtein, tempCarbs);
            double tempTotal = Double.sum(tempSubTotal, durationPrice);
            tvTotalPrice.setText("" + tempTotal);


        });

        removeProteinsBtn.setOnClickListener(v -> {
            currentProteinIndex--;
            if (currentProteinIndex <= 0) {
                currentProteinIndex = 0;
                CommonUtils.showWarning(HomeDetailedActivity.this, "Minimum protein limit reached");
            }
            proteinsCountBtn.setText(proteins.get(currentProteinIndex).getProteins() + "g");
            tvProteinPrice.setText(proteins.get(currentProteinIndex).getProteinsPrice());

            double tempProtein = Double.parseDouble(proteins.get(currentProteinIndex).getProteinsPrice());
            double tempCarbs = Double.parseDouble(carbs.get(currentCarbIndex).getCarbsPrice());
            double tempSubTotal = Double.sum(tempProtein, tempCarbs);
            double tempTotal = Double.sum(tempSubTotal, durationPrice);
            tvTotalPrice.setText("" + tempTotal);


        });

        addCarbsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCarbIndex++;
                if (currentCarbIndex >= carbs.size()) {
                    currentCarbIndex--;
                    CommonUtils.showWarning(HomeDetailedActivity.this, "Maximum carb limit reached");
                }
                carbsCountBtn.setText(carbs.get(currentCarbIndex).getCarbs() + "g");
                tvCrabPrice.setText(carbs.get(currentCarbIndex).getCarbsPrice());

                double tempProtein = Double.parseDouble(proteins.get(currentProteinIndex).getProteinsPrice());
                double tempCarbs = Double.parseDouble(carbs.get(currentCarbIndex).getCarbsPrice());
                double tempSubTotal = Double.sum(tempProtein, tempCarbs);
                double tempTotal = Double.sum(tempSubTotal, durationPrice);
                tvTotalPrice.setText("" + tempTotal);

            }
        });

        removeCarbsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCarbIndex--;
                if (currentCarbIndex <= 0) {
                    currentCarbIndex = 0;
                    CommonUtils.showWarning(HomeDetailedActivity.this, "Minimum carb limit reached");
                }
                carbsCountBtn.setText(carbs.get(currentCarbIndex).getCarbs() + "g");
                tvCrabPrice.setText(carbs.get(currentCarbIndex).getCarbsPrice());

                double tempProtein = Double.parseDouble(proteins.get(currentProteinIndex).getProteinsPrice());
                double tempCarbs = Double.parseDouble(carbs.get(currentCarbIndex).getCarbsPrice());
                double tempSubTotal = Double.sum(tempProtein, tempCarbs);
                double tempTotal = Double.sum(tempSubTotal, durationPrice);
                tvTotalPrice.setText("" + tempTotal);
            }
        });

        contentDisikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeDetailedActivity.this, DislikesActivity.class);
                startActivity(intent);
            }
        });

        contentProceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (apiDuration.equals("")) {
                    CommonUtils.showWarning(HomeDetailedActivity.this, "Select Plan Duration");

                } else if (apiStartDate.equals("")) {
                    CommonUtils.showWarning(HomeDetailedActivity.this, "Select Start Date");
                } else {
                    Log.e("TAG", "user_id: " + SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""));
                    Log.e("TAG", "start_date: " + apiStartDate);
                    Log.e("TAG", "meal_plan_id: " + mealsId);
                    Log.e("TAG", "carbs: " + carbs.get(currentCarbIndex).getCarbs());
                    Log.e("TAG", "carbs_price: " + carbs.get(currentCarbIndex).getCarbsPrice());
                    Log.e("TAG", "proteins: " + proteins.get(currentProteinIndex).getProteins());
                    Log.e("TAG", "proteins_price: " + proteins.get(currentProteinIndex).getProteinsPrice());
                    Log.e("TAG", "comments: " + etComments.getText().toString());
                    Log.e("TAG", "duration: " + apiDuration);
                    Log.e("TAG", "base_price: " + apiDurationPrice);
                    Log.e("TAG", "code: ");
                    Log.e("TAG", "suspend: " + apiDurationSuspend);
                    Log.e("TAG", "lang_id: ");
                    Log.e("TAG", "enable_modification: ");

                    String userId = SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, "");
                    String startDate = apiStartDate;
                    String mealsID = mealsId;
                    String carb = String.valueOf(carbs.get(currentCarbIndex).getCarbs());
                    String carbPrice = String.valueOf(carbs.get(currentCarbIndex).getCarbsPrice());
                    String prot = String.valueOf(proteins.get(currentProteinIndex).getProteins());
                    String protPrice = String.valueOf(proteins.get(currentProteinIndex).getProteinsPrice());
                    String comt = etComments.getText().toString();
                    int dur = Integer.parseInt(apiDuration);
                    String durPrice = String.valueOf(apiDurationPrice);
                    String cod = "";
                    String durSusp = String.valueOf(apiDurationSuspend);
                    String lang = "en";
                    int not = 0;

                    homeDetailedViewModel.getSubscription(userId, startDate, Integer.parseInt(mealsID), "", carb, carbPrice, prot, protPrice, comt, dur, durPrice, cod, durSusp, lang, not).observe(HomeDetailedActivity.this, addSubscriptionResponse -> {
                        if (addSubscriptionResponse.getStatus()) {

                            String meal_plan_subscription_id = addSubscriptionResponse.getData().getMealPlanSubscriptionId().toString();
                            String duration = addSubscriptionResponse.getData().getPlanSummary().getDuration().getValue();
                            String plan = addSubscriptionResponse.getData().getPlanSummary().getPlan().getValue().toString();
                            String carbs = addSubscriptionResponse.getData().getPlanSummary().getCarbs().getValue().toString();
                            String proteinCarbs = addSubscriptionResponse.getData().getPlanSummary().getProtein().getValue().toString();
                            String nonStop = addSubscriptionResponse.getData().getPlanSummary().getNonStop().getValue().toString();
                            String total = addSubscriptionResponse.getData().getPlanSummary().getTotal().getValue().toString();
                            String unique_key = addSubscriptionResponse.getData().getUniqueKey();

                            ArrayList<String> valueList = new ArrayList<>();
                            valueList.add(meal_plan_subscription_id);
                            valueList.add(duration);
                            valueList.add(plan);
                            valueList.add(carbs);
                            valueList.add(proteinCarbs);
                            valueList.add(nonStop);
                            valueList.add(total);
                            valueList.add(unique_key);

                            String meal_plan_subscription_id_key = addSubscriptionResponse.getData().getMealPlanSubscriptionId().toString();
                            String duration_key = addSubscriptionResponse.getData().getPlanSummary().getDuration().getName();
                            String plan_key = addSubscriptionResponse.getData().getPlanSummary().getPlan().getName();
                            String carbs_key = addSubscriptionResponse.getData().getPlanSummary().getCarbs().getName();
                            String proteinCarbs_key = addSubscriptionResponse.getData().getPlanSummary().getProtein().getName();
                            String nonStop_key = addSubscriptionResponse.getData().getPlanSummary().getNonStop().getName();
                            String total_key = addSubscriptionResponse.getData().getPlanSummary().getTotal().getName();
                            String unique_key_key = addSubscriptionResponse.getData().getUniqueKey();

                            ArrayList<String> keyList = new ArrayList<>();
                            keyList.add(meal_plan_subscription_id_key);
                            keyList.add(duration_key);
                            keyList.add(plan_key);
                            keyList.add(carbs_key);
                            keyList.add(proteinCarbs_key);
                            keyList.add(nonStop_key);
                            keyList.add(total_key);
                            keyList.add(unique_key_key);

                            Intent intent = new Intent(HomeDetailedActivity.this, CheckoutActivity.class);
                            intent.putExtra("keys", keyList);
                            intent.putExtra("values", valueList);
                            intent.putExtra("meal_id", addSubscriptionResponse.getData().getMealPlanSubscriptionId().toString());
                            intent.putExtra("unique", addSubscriptionResponse.getData().getUniqueKey());
                            startActivity(intent);

                        } else {
                            CommonUtils.showWarning(HomeDetailedActivity.this, addSubscriptionResponse.getMessage());
                            //Toast.makeText(HomeDetailedActivity.this, "" + addSubscriptionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMonth--;
                if (currentMonth <= 0) {
                    currentMonth = 0;
                }
                tv_month.setText(listMonths.get(currentMonth));
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMonth++;
                if (currentMonth >= 11) {
                    currentMonth = 11;
                }
                tv_month.setText(listMonths.get(currentMonth));
            }
        });
    }

    private void setupUi() {
        homeDetailedViewModel.getMeals(mealsId, "en").observe(this, homeDetailedResponse -> {
            if (homeDetailedResponse.getStatus()) {
                content.setVisibility(View.VISIBLE);
                setupMeals(homeDetailedResponse.getData().getMealPlan());
                setUpCalender(homeDetailedResponse.getData());
                proteins.addAll(homeDetailedResponse.getData().getProteins());
                setupProteins(proteins);
                carbs.addAll(homeDetailedResponse.getData().getCarbs());
                setUpCarbs(carbs);

            } else {
                content.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "" + homeDetailedResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpCarbs(List<Carb> carbs) {
        carbsCountBtn.setText(carbs.get(currentCarbIndex).getCarbs() + "g");
        tvCrabPrice.setText(carbs.get(currentCarbIndex).getCarbsPrice());
    }

    private void setupProteins(List<Protein> proteins) {
        proteinsCountBtn.setText(proteins.get(currentProteinIndex).getProteins() + "g");
        tvProteinPrice.setText(proteins.get(currentProteinIndex).getProteinsPrice());
    }

    private void setupMeals(MealPlan mealPlan) {
        Glide.with(this).load(mealPlan.getImage()).into(vpDetailsImage);
        tvMealPlanCategory.setText(mealPlan.getMealCategoryName());
        toolbar_btn.setText(mealPlan.getMealCategoryName());
        tvDescriptionContent.setText(mealPlan.getDescription());

        rvOffDays.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvOffDays.setAdapter(new OffDaysAdapter(this, mealPlan.getOffDays()));

        rvPlanDuration.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPlanDuration.setAdapter(new PlanDurationAdapter(this, mealPlan.getDurations()));


    }

    private void setUpCalender(Data data) {

        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, data.getPlanStartBuffer());
        currentMonth = calendar.get(Calendar.MONTH);
        tv_month.setText(listMonths.get(currentMonth));
        /*planStartBuffer = data.getPlanStartBuffer();
        setDates(data.getPlanStartBuffer());

        rv_single_row_calendar.setLayoutManager(new LinearLayoutManager(this));
        rv_single_row_calendar.setHasFixedSize(true);
        start_date_range = data.getStartDateRange() + data.getPlanStartBuffer();*/

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());// HH:mm:ss");
        String reg_date = df.format(c.getTime());
        c.add(Calendar.DATE, (data.getPlanStartBuffer() + 1));
        String end_date = df.format(c.getTime());

        List<CalenderModel> d = getDates(reg_date, end_date);

        rv_single_row_calendar.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_single_row_calendar.setAdapter(new PlansAdapter(this, d));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setValue(Duration duration) {
        double tempProtein = Double.parseDouble(proteins.get(currentProteinIndex).getProteinsPrice());
        double tempCarbs = Double.parseDouble(carbs.get(currentCarbIndex).getCarbsPrice());
        double tempSubTotal = Double.sum(tempProtein, tempCarbs);
        durationPrice = Double.parseDouble(duration.getPrice());
        double tempTotal = Double.sum(tempSubTotal, durationPrice);
        tvTotalPrice.setText("" + tempTotal);

        apiDuration = duration.getDuration();
        apiDurationPrice = duration.getPrice();
        apiDurationSuspend = duration.getSuspend();
        Log.e("TAG", "setValue: " + duration.getSuspend());
    }

    @Override
    public void setStartValue(CalenderModel calenderModel) {
        apiStartDate = calenderModel.getDate();
        Log.e("TAG", "setStartValue: " + apiStartDate);
    }
}