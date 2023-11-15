package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.update_rate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.StorageSpacesActivity;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PricingWareHouse;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.DatePickerActivityDialog;
import com.ashtechlabs.teleporter.util.OnDatePickListener;
import com.ashtechlabs.teleporter.util.SingleChoiceActivitytDialog;
import com.ashtechlabs.teleporter.util.SingleChoiceItemDialogListener;

import java.util.ArrayList;
import java.util.Arrays;

public class UpdateRateWarehouseActivity extends BaseActivity implements SingleChoiceItemDialogListener,
        IUpdateRateWareHouseActivityControllerCallback, OnDatePickListener,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView editLocation;
    private IUpdateRateWareHouseActivityController mController;
    private TextView tvCurrency, tvRateValidity;
    private ImageView ivCurrency;
    private EditText editPerCBMAmount, etInsurancePercentage, etMinInsuranceAmt;
    private int selected_currency;
    private ArrayList<String> arrayCurrency;
    private TextView txtDateFrom;
    private TextView txtDateTo;
    private EditText editTotalCBM;
    private Button btnSubmit;
    private int additional_info = -1;
    private int perishable_det = 0;

    private int warehouseId = -1;
    private String warehouseName = "";

    private RadioButton rbCool, rbCold, rbFrozen;
    private CheckBox cbGeneral, cbHazardous, cbPerishable;
    private LinearLayout layRadiopersiable;

    //    ImageView air, cargo, truck, trolley;
    private String token, id;
    private String dateFroms, dateTos, insPercentage, insMinAmt, locations, perCBMAmounts, totalCBMAvailables, rateValidity, currency;
    private PricingWareHouse pricingWareHouse;


    private int rateCardType = 0; //0 == UPDATE, 1==ADD


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rate_warehouse);
        arrayCurrency = new ArrayList(Arrays.asList(this.getResources().getStringArray(R.array.array_currency)));
        setUpToolbar();

        mController = new UpdateRateWareHouseActivityController(this);


        editLocation = (TextView) findViewById(R.id.editLocation);
        editLocation.setOnClickListener(this);
        editPerCBMAmount = (EditText) findViewById(R.id.editPerCBMAmount);
        tvCurrency = (TextView) findViewById(R.id.tvCurrency);
        tvCurrency.setOnClickListener(this);
        ivCurrency = (ImageView) findViewById(R.id.ivCurrency);
        ivCurrency.setOnClickListener(this);
        txtDateFrom = (TextView) findViewById(R.id.txtDateFrom);
        txtDateFrom.setOnClickListener(this);
        txtDateTo = (TextView) findViewById(R.id.txtDateTo);
        txtDateTo.setOnClickListener(this);
        editTotalCBM = (EditText) findViewById(R.id.editTotalCBM);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        etInsurancePercentage = (EditText) findViewById(R.id.etInsurancePercentage);
        etMinInsuranceAmt = (EditText) findViewById(R.id.etMinInsuranceAmt);
        tvRateValidity = (TextView) findViewById(R.id.tvRateValidity);
        tvRateValidity.setOnClickListener(this);

        rbCool = (RadioButton) findViewById(R.id.rbCool);
        rbCool.setOnCheckedChangeListener(this);
        rbCold = (RadioButton) findViewById(R.id.rbCold);
        rbCold.setOnCheckedChangeListener(this);
        rbFrozen = (RadioButton) findViewById(R.id.rbFrozen);
        rbFrozen.setOnCheckedChangeListener(this);

        layRadiopersiable = (LinearLayout) findViewById(R.id.layRadiopersiable);
        cbHazardous = (CheckBox) findViewById(R.id.cbHazardous);
        cbGeneral = (CheckBox) findViewById(R.id.cbGeneral);
        cbPerishable = (CheckBox) findViewById(R.id.cbPerishable);
        cbGeneral.setOnCheckedChangeListener(this);
        cbPerishable.setOnCheckedChangeListener(this);
        cbHazardous.setOnCheckedChangeListener(this);


        token = GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, "");

        Bundle extras = getIntent().getExtras();
        pricingWareHouse = extras.getParcelable("pricingWareHouse");
        rateCardType = extras.getInt("rate_card_type");


        if (rateCardType == 0) {

            id = pricingWareHouse.getID();
            warehouseId = pricingWareHouse.getWarehouseDetId();
            editLocation.setText(pricingWareHouse.getWarehouseName());
            txtDateFrom.setText(pricingWareHouse.getFromDate());
            txtDateTo.setText(pricingWareHouse.getToDate());
            editPerCBMAmount.setText(pricingWareHouse.getPCBM());
            editTotalCBM.setText(pricingWareHouse.getTotalCBMAvailable());
            etInsurancePercentage.setText(pricingWareHouse.getIns_percent());
            etMinInsuranceAmt.setText(pricingWareHouse.getMin_insurance_amt());
            tvRateValidity.setText(pricingWareHouse.getRate_validity());

            selected_currency = Integer.parseInt(pricingWareHouse.getCurrency());
            additional_info = Integer.parseInt(pricingWareHouse.getAdditional_info());
            if (TextUtils.isEmpty(pricingWareHouse.getPerishable_det())) {
                perishable_det = 0;
            } else {
                perishable_det = Integer.parseInt(pricingWareHouse.getPerishable_det());
            }
            tvCurrency.setText(arrayCurrency.get(selected_currency));


            // additional_info = pricingWareHouse.getAdditionalInfo();
            if (additional_info == 0) {
                cbGeneral.setChecked(true);
            } else if (additional_info == 1) {
                cbHazardous.setChecked(true);
            } else if (additional_info == 2) {
                cbPerishable.setChecked(true);
            }

            if (cbPerishable.isChecked()) {
                layRadiopersiable.setVisibility(View.VISIBLE);
                if (perishable_det == 1) {
                    rbCool.setChecked(true);
                } else if (perishable_det == 2) {
                    rbCold.setChecked(true);
                } else if (perishable_det == 3) {
                    rbFrozen.setChecked(true);
                }
            }


        } else {

            btnSubmit.setText("ADD NEW RATE");
            id = extras.getString("id");
            //editLocation.setText(extras.getString("location"));
            txtDateFrom.setText(extras.getString("from_date"));
            txtDateTo.setText(extras.getString("to_date"));

            additional_info = extras.getInt("additional_info",-1);
            perishable_det = extras.getInt("perishable_data",0);

            // additional_info = pricingWareHouse.getAdditionalInfo();
            if (additional_info == 0) {
                cbGeneral.setChecked(true);
            } else if (additional_info == 1) {
                cbHazardous.setChecked(true);
            } else if (additional_info == 2) {
                cbPerishable.setChecked(true);
            }

            if (cbPerishable.isChecked()) {
                layRadiopersiable.setVisibility(View.VISIBLE);
                if (perishable_det == 1) {
                    rbCool.setChecked(true);
                } else if (perishable_det == 2) {
                    rbCold.setChecked(true);
                } else if (perishable_det == 3) {
                    rbFrozen.setChecked(true);
                }
            }

        }

        // editLocation.setAdapter(new GooglePlacesAdapter(this, R.layout.list_item));
        //editLocation.setAdapter(new PlaceAutocompleteAdapter(this, mGoogleApiClient, null, null));

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                uploadRateCard();
            }
        });


    }


    private void uploadRateCard() {

        dateFroms = txtDateFrom.getText().toString();
        dateTos = txtDateTo.getText().toString();
        insPercentage = etInsurancePercentage.getText().toString();
        insMinAmt = etMinInsuranceAmt.getText().toString();
        locations = editLocation.getText().toString();
        perCBMAmounts = editPerCBMAmount.getText().toString();
        totalCBMAvailables = editTotalCBM.getText().toString();
        rateValidity = tvRateValidity.getText().toString();
        currency = tvCurrency.getText().toString();

        if (warehouseId == -1) {
            Toast.makeText(this, "Please select a warehouse", Toast.LENGTH_LONG).show();
            return;
        }

        if (dateFroms.equals("")) {
            Toast.makeText(this, "Please input Date From", Toast.LENGTH_LONG).show();
            return;
        }

        if (dateTos.equals("")) {
            Toast.makeText(this, "Please input Date To", Toast.LENGTH_LONG).show();
            return;
        }

        if (insPercentage.equals("")) {
            Toast.makeText(this, "Please input Insurance Percentage!", Toast.LENGTH_LONG).show();
            return;
        }

        if (insMinAmt.equals("")) {
            Toast.makeText(this, "Please input Minimum Insurance Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (perCBMAmounts.equals("")) {
            Toast.makeText(this, "Please input Per CBM Amount!", Toast.LENGTH_LONG).show();
            return;
        }


        if (totalCBMAvailables.equals("")) {
            Toast.makeText(this, "Please input Total CBM", Toast.LENGTH_LONG).show();
            return;
        }
        if (tvRateValidity.equals("")) {
            Toast.makeText(this, "Please input rate Card Validity!", Toast.LENGTH_LONG).show();
            return;

        }
        if (currency.equals("")) {
            Toast.makeText(this, "Please input the currency!", Toast.LENGTH_LONG).show();
            return;
        }

        mController.getUpdateWareHouseRateCard(rateCardType, token, id, warehouseId, perCBMAmounts, dateFroms, dateTos, additional_info, perishable_det, insMinAmt, insPercentage, rateValidity, totalCBMAvailables, selected_currency);

    }

    private void formatData() {

        txtDateFrom.setText("");
        txtDateTo.setText("");
        editLocation.setText("");
        editTotalCBM.setText("");
        editPerCBMAmount.setText("");

    }

    @Override
    public void setOnSelectItem(String item, int position, String tag) {

        if (tag.equals("currency_dialog")) {
            tvCurrency.setText(item);
            selected_currency = position;
        }
    }


    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Update Rate");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showLoadingIndicator() {
        showProgress();
    }

    @Override
    public void dismissLoadingIndicator() {
        dismissProgress();
    }

    @Override
    public void onUpdateWareHouseRateSuccess(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpdateWareHouseRateFailed(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCurrency:

                SingleChoiceActivitytDialog currencyListFragment = new SingleChoiceActivitytDialog();
                Bundle argsCurrency = new Bundle();
                argsCurrency.putString("title", "Currency");
                argsCurrency.putStringArrayList("list_array", arrayCurrency);
                currencyListFragment.setArguments(argsCurrency);
                currencyListFragment.show(this.getSupportFragmentManager(), "currency_dialog");

                break;

            case R.id.tvCurrency:
                SingleChoiceActivitytDialog currencyList = new SingleChoiceActivitytDialog();
                Bundle args2 = new Bundle();
                args2.putString("title", "Currency");
                args2.putStringArrayList("list_array", arrayCurrency);
                currencyList.setArguments(args2);
                currencyList.show(this.getSupportFragmentManager(), "currency_dialog");

                break;
            case R.id.tvRateValidity:
                DialogFragment datePickerDialogFragment = new DatePickerActivityDialog();
                datePickerDialogFragment.show(getSupportFragmentManager(), "date_picker_dialog");
                break;

            case R.id.txtDateFrom:
                DialogFragment fromDate = new DatePickerActivityDialog();
                fromDate.show(getSupportFragmentManager(), "from_date_pick");
                break;

            case R.id.txtDateTo:
                DialogFragment todate = new DatePickerActivityDialog();
                todate.show(getSupportFragmentManager(), "to_date_pick");
                break;

            case R.id.editLocation:
                Intent intent = new Intent(this, StorageSpacesActivity.class);
                intent.putExtra(Constants.KEY_WHICH_ACTIVITY, "rate_card");
                startActivityForResult(intent, 501);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;


        if (requestCode == 501) {

            warehouseId = data.getIntExtra(Constants.KEY_WARE_HOUSE_ID, -1);
            warehouseName = data.getStringExtra(Constants.KEY_WARE_HOUSE_NAME);

            editLocation.setText(warehouseName);

        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cbGeneral) {
            if (isChecked) {
                cbPerishable.setChecked(false);
                cbHazardous.setChecked(false);
                layRadiopersiable.setVisibility(View.GONE);
                additional_info = 0;
            }

        } else if (buttonView.getId() == R.id.cbPerishable) {
            if (isChecked) {
                cbGeneral.setChecked(false);
                cbHazardous.setChecked(false);
                layRadiopersiable.setVisibility(View.VISIBLE);
                additional_info = 2;
            } else {
                layRadiopersiable.setVisibility(View.GONE);
            }
        } else if (buttonView.getId() == R.id.cbHazardous) {
            if (isChecked) {
                cbGeneral.setChecked(false);
                cbPerishable.setChecked(false);
                layRadiopersiable.setVisibility(View.GONE);
                additional_info = 1;
            }

        } else if (buttonView.getId() == R.id.rbCool) {
            if (isChecked) {
                perishable_det = 1;
            }
        } else if (buttonView.getId() == R.id.rbCold) {
            if (isChecked) {
                perishable_det = 2;
            }
        } else if (buttonView.getId() == R.id.rbFrozen) {
            if (isChecked) {
                perishable_det = 3;
            }
        }
    }


    @Override
    public void setDate(String date, String tag) {
        if (tag.equals("date_picker_dialog")) {
            tvRateValidity.setText(date);
        } else if (tag.equals("from_date_pick")) {
            txtDateFrom.setText(date);
        } else {
            txtDateTo.setText(date);
        }
    }
}
