package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.type;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;

import net.cachapa.expandablelayout.ExpandableLayout;

public class VehicleSelectActivity extends BaseActivity implements View.OnClickListener {


    ExpandableLayout ex_one_ton, ex_three_ton, ex_seven_ton, ex_ten_ton, ex_fourty_ton, ex_sixty_ton;
    ImageView iv_one, iv_two, iv_three, iv_four, iv_five, iv_six;
    RadioGroup rg_one, rg_three, rg_fourty, rg_ten, rg_seven, rg_sixty;
    RadioButton rb_oneOne, rb_oneTwo, rb_threeOne, rb_threeTwo, rb_threeThree, rb_sevenOne, rb_sevenTwo, rb_sevenThree,
            rb_sevenFour, rb_tenOne, rb_tenTwo, rb_tenThree, rb_fourtyOne, rb_fourtyTwo, rb_fourtyThree, rb_fourtyFour,
            rb_sixtyOne, rb_sixtyTwo, rb_sixtyThree;
    int truck = -1;
    int subtruck = -1;
    String truckName, subtruckName;
    Button btAdd;

    public VehicleSelectActivity() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static VehicleSelectActivity newInstance() {

        return new VehicleSelectActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_select);
        setupToolbar();
        initViews();

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void initViews() {
        ex_one_ton = (ExpandableLayout) findViewById(R.id.ex_one_ton);
        ex_three_ton = (ExpandableLayout) findViewById(R.id.ex_three_ton);
        ex_seven_ton = (ExpandableLayout) findViewById(R.id.ex_seven_ton);
        ex_ten_ton = (ExpandableLayout) findViewById(R.id.ex_ten_ton);
        ex_fourty_ton = (ExpandableLayout) findViewById(R.id.ex_fourty_ton);
        ex_sixty_ton = (ExpandableLayout) findViewById(R.id.ex_sixty_ton);


        btAdd = (Button) findViewById(R.id.btAdd);
        btAdd.setOnClickListener(this);
        rg_one = (RadioGroup) findViewById(R.id.rg_one);
        rg_three = (RadioGroup) findViewById(R.id.rg_three);
        rg_seven = (RadioGroup) findViewById(R.id.rg_seven);
        rg_ten = (RadioGroup) findViewById(R.id.rg_ten);
        rg_fourty = (RadioGroup) findViewById(R.id.rg_fourty);
        rg_sixty = (RadioGroup) findViewById(R.id.rg_sixty);
        rb_oneOne = (RadioButton) findViewById(R.id.rb_oneOne);
        rb_oneTwo = (RadioButton) findViewById(R.id.rb_oneTwo);
        rb_threeOne = (RadioButton) findViewById(R.id.rb_threeOne);
        rb_threeTwo = (RadioButton) findViewById(R.id.rb_threeTwo);
        rb_threeThree = (RadioButton) findViewById(R.id.rb_threeThree);
        rb_sevenOne = (RadioButton) findViewById(R.id.rb_sevenOne);
        rb_sevenTwo = (RadioButton) findViewById(R.id.rb_sevenTwo);
        rb_sevenThree = (RadioButton) findViewById(R.id.rb_sevenThree);
        rb_sevenFour = (RadioButton) findViewById(R.id.rb_sevenFour);
        rb_tenOne = (RadioButton) findViewById(R.id.rb_tenOne);
        rb_tenTwo = (RadioButton) findViewById(R.id.rb_tenTwo);
        rb_tenThree = (RadioButton) findViewById(R.id.rb_tenThree);
        rb_fourtyOne = (RadioButton) findViewById(R.id.rb_fourtyOne);
        rb_fourtyTwo = (RadioButton) findViewById(R.id.rb_fourtyTwo);
        rb_fourtyThree = (RadioButton) findViewById(R.id.rb_fourtyTree);
        rb_fourtyFour = (RadioButton) findViewById(R.id.rb_fourtyFour);
        rb_sixtyOne = (RadioButton) findViewById(R.id.rb_sixtyOne);
        rb_sixtyTwo = (RadioButton) findViewById(R.id.rb_sixtyTwo);
        rb_sixtyThree = (RadioButton) findViewById(R.id.rb_sixtyThree);

        iv_one = (ImageView) findViewById(R.id.iv_one);
        iv_two = (ImageView) findViewById(R.id.iv_two);
        iv_three = (ImageView) findViewById(R.id.iv_three);
        iv_four = (ImageView) findViewById(R.id.iv_four);
        iv_five = (ImageView) findViewById(R.id.iv_five);
        iv_six = (ImageView) findViewById(R.id.iv_six);


        CardView one_ton = (CardView) findViewById(R.id.one_ton);
        one_ton.setOnClickListener(this);
        CardView three_ton = (CardView) findViewById(R.id.three_ton);
        three_ton.setOnClickListener(this);
        CardView seven_ton = (CardView) findViewById(R.id.seven_ton);
        seven_ton.setOnClickListener(this);
        CardView ten_ton = (CardView) findViewById(R.id.ten_ton);
        ten_ton.setOnClickListener(this);
        CardView fourty_ton = (CardView) findViewById(R.id.fourty_ton);
        fourty_ton.setOnClickListener(this);
        CardView sixty_ton = (CardView) findViewById(R.id.sixty_ton);
        sixty_ton.setOnClickListener(this);
        CardView buker_truck = (CardView) findViewById(R.id.buker_truck);
        buker_truck.setOnClickListener(this);
        CardView low_bed_truck = (CardView) findViewById(R.id.low_bed_truck);
        low_bed_truck.setOnClickListener(this);
        CardView tanker_truck = (CardView) findViewById(R.id.tanker_truck);
        tanker_truck.setOnClickListener(this);
        CardView tripper_truck = (CardView) findViewById(R.id.tripper_truck);
        tripper_truck.setOnClickListener(this);
        CardView bikes = (CardView) findViewById(R.id.bikes);
        bikes.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.one_ton:
                //   mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 0);
                truck = 0;
                truckName = "1 Ton Truck";
                float deg = iv_one.getRotation() + 180F;
                iv_one.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                if (ex_one_ton.isExpanded()) {
                    ex_one_ton.collapse();
                } else {
                    ex_one_ton.expand();
                    ex_three_ton.collapse();
                    ex_seven_ton.collapse();
                    ex_ten_ton.collapse();
                    ex_fourty_ton.collapse();
                    ex_sixty_ton.collapse();
                }

//                if (ex_one_ton.isExpanded()) {
//                    fabTick.setVisibility(View.VISIBLE);
//                } else {
//                    fabTick.setVisibility(View.GONE);
//
//                }


                break;
            case R.id.three_ton:
                //   mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 1);
                truck = 1;
                truckName = "3 Ton Truck";
                float deg2 = iv_two.getRotation() + 180F;
                iv_two.animate().rotation(deg2).setInterpolator(new AccelerateDecelerateInterpolator());

                if (ex_three_ton.isExpanded()) {
                    ex_three_ton.collapse();
                } else {
                    ex_one_ton.collapse();
                    ex_three_ton.expand();
                    ex_seven_ton.collapse();
                    ex_ten_ton.collapse();
                    ex_fourty_ton.collapse();
                    ex_sixty_ton.collapse();
                }

//                if (ex_three_ton.isExpanded()) {
//                    fabTick.setVisibility(View.VISIBLE);
//                } else {
//                    fabTick.setVisibility(View.GONE);
//
//                }
                break;
            case R.id.seven_ton:
                //   mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 2);
                truck = 2;
                truckName = "7 Ton Truck";
                float deg3 = iv_three.getRotation() + 180F;
                iv_three.animate().rotation(deg3).setInterpolator(new AccelerateDecelerateInterpolator());
                if (ex_seven_ton.isExpanded()) {
                    ex_seven_ton.collapse();
                } else {
                    ex_one_ton.collapse();
                    ex_three_ton.collapse();
                    ex_seven_ton.expand();
                    ex_ten_ton.collapse();
                    ex_fourty_ton.collapse();
                    ex_sixty_ton.collapse();
                }
//                if (ex_seven_ton.isExpanded()) {
//                    fabTick.setVisibility(View.VISIBLE);
//                } else {
//                    fabTick.setVisibility(View.GONE);
//
//                }
                break;
            case R.id.ten_ton:
                //   mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 3);
                truck = 3;
                truckName = "10 Ton Truck";
                float deg4 = iv_four.getRotation() + 180F;
                iv_four.animate().rotation(deg4).setInterpolator(new AccelerateDecelerateInterpolator());
                if (ex_ten_ton.isExpanded()) {
                    ex_ten_ton.collapse();
                } else {
                    ex_one_ton.collapse();
                    ex_three_ton.collapse();
                    ex_seven_ton.collapse();
                    ex_ten_ton.expand();
                    ex_fourty_ton.collapse();
                    ex_sixty_ton.collapse();
                }
//                if (ex_ten_ton.isExpanded()) {
//                    fabTick.setVisibility(View.VISIBLE);
//                } else {
//                    fabTick.setVisibility(View.GONE);
//
//                }
                break;
            case R.id.fourty_ton:
                //   mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 4);
                truck = 4;
                truckName = "40 Feet Container Truck";
                float deg5 = iv_five.getRotation() + 180F;
                iv_five.animate().rotation(deg5).setInterpolator(new AccelerateDecelerateInterpolator());
                if (ex_fourty_ton.isExpanded()) {
                    ex_fourty_ton.collapse();
                } else {
                    ex_one_ton.collapse();
                    ex_three_ton.collapse();
                    ex_seven_ton.collapse();
                    ex_ten_ton.collapse();
                    ex_fourty_ton.expand();
                    ex_sixty_ton.collapse();
                }
//                if (ex_fourty_ton.isExpanded()) {
//                    fabTick.setVisibility(View.VISIBLE);
//                } else {
//                    fabTick.setVisibility(View.GONE);
//
//                }

                break;
            case R.id.sixty_ton:
                //  mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 5);
                truck = 5;
                truckName = "60 Feet Truck";
                float deg6 = iv_six.getRotation() + 180F;
                iv_six.animate().rotation(deg6).setInterpolator(new AccelerateDecelerateInterpolator());
                if (ex_sixty_ton.isExpanded()) {
                    ex_sixty_ton.collapse();
                } else {
                    ex_one_ton.collapse();
                    ex_three_ton.collapse();
                    ex_seven_ton.collapse();
                    ex_ten_ton.collapse();
                    ex_fourty_ton.collapse();
                    ex_sixty_ton.expand();
                }
//                if (ex_sixty_ton.isExpanded()) {
//                    fabTick.setVisibility(View.VISIBLE);
//                } else {
//                    fabTick.setVisibility(View.GONE);
//
//                }
                break;
         /*   case R.id.ton_truck:
                mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 6);
                break;
            case R.id.feet_container_truck:
                mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 7);
                break;
            case R.id.feet_open_body_truck:
                mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 8);
                break;
            case R.id.feet_reefer_truck:
                mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 9);
                break;
            case R.id.feet_truck:
                mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 10);
                break;*/
            case R.id.buker_truck:
                truck = 6;
                truckName = "Bulker Truck";
                subtruckName = "";
               // mListener.onSelectVehicle(11,"", -2,"");
                startActivityWithResult();

                // mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 11);
                break;
            case R.id.low_bed_truck:
                truckName = "Low bed Truck";
                subtruckName = "";
                truck = 7;
                startActivityWithResult();
                // mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 12);
                break;
            case R.id.tanker_truck:
                truckName = "Tanker Truck";
                subtruckName = "";
                truck = 8;
                startActivityWithResult();
                // mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 13);
                break;
            case R.id.tripper_truck:
                truckName = "Tipper Truck";
                truck = 9;
                subtruckName = "";
                startActivityWithResult();
                // mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 14);
                break;
            case R.id.bikes:
                truckName = "Bikes";
                truck = 10;
                subtruckName = "";
                startActivityWithResult();
                //mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, 15);
                break;

            case R.id.btAdd:

                if(truck==-1){
                    CommonUtils.showToast(this, "Select A Vehicle");
                    return;
                }

                if (ex_one_ton.isExpanded()) {
                    if (rb_oneOne.isChecked()) {
                        subtruck = 0;
                        subtruckName = "Open Body";
                    } else if (rb_oneTwo.isChecked()) {
                        subtruck = 1;
                        subtruckName = "Close Body";
                    }
                    // CommonUtils.showToast(getActivity(),truck+"....."+subtruck);
                    //  mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, truck,subtruck);

                } else if (ex_three_ton.isExpanded()) {
                    if (rb_threeOne.isChecked()) {
                        subtruck = 0;
                        subtruckName = "Reefer";
                    } else if (rb_threeTwo.isChecked()) {
                        subtruck = 1;
                        subtruckName = "Close Body";
                    } else if (rb_threeThree.isChecked()) {
                        subtruck = 2;
                        subtruckName = "Open Body";
                    }  // CommonUtils.showToast(getActivity(),truck+"....."+subtruck);

                    //  mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, truck,subtruck);

                } else if (ex_fourty_ton.isExpanded()) {
                    if (rb_fourtyOne.isChecked()) {
                        subtruck = 0;
                        subtruckName = "Reefer";
                    } else if (rb_fourtyTwo.isChecked()) {
                        subtruck = 1;
                        subtruckName = "Close Body";
                    } else if (rb_fourtyThree.isChecked()) {
                        subtruck = 2;
                        subtruckName = "Open Body";
                    } else if (rb_fourtyFour.isChecked()) {
                        subtruck = 3;
                        subtruckName = "Side Lifter";
                    }
                } else if (ex_seven_ton.isExpanded()) {
                    if (rb_sevenOne.isChecked()) {
                        subtruck = 0;
                        subtruckName = "Reefer";
                    } else if (rb_sevenTwo.isChecked()) {
                        subtruck = 1;
                        subtruckName = "Close Body";
                    } else if (rb_sevenThree.isChecked()) {
                        subtruck = 2;
                        subtruckName = "Open Body";
                    } else if (rb_sevenFour.isChecked()) {
                        subtruck = 3;
                        subtruckName = "Wit Crane";
                    }
                    //  CommonUtils.showToast(getActivity(),truck+"....."+subtruck);
                    //  mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, truck,subtruck);

                } else if (ex_ten_ton.isExpanded()) {
                    if (rb_tenOne.isChecked()) {
                        subtruck = 0;
                        subtruckName = "Reefer";
                    } else if (rb_tenTwo.isChecked()) {
                        subtruck = 1;
                        subtruckName = "Close Body";
                    } else if (rb_tenThree.isChecked()) {
                        subtruck = 2;
                        subtruckName = "Open Body";
                    }
                    // CommonUtils.showToast(getActivity(),truck+"....."+subtruck);
                    // mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, truck,subtruck);

                } else if (ex_sixty_ton.isExpanded()) {
                    if (rb_sixtyOne.isChecked()) {
                        subtruck = 0;
                        subtruckName = "Reefer";
                    } else if (rb_sixtyTwo.isChecked()) {
                        subtruck = 1;
                        subtruckName = "Close Body";
                    } else if (rb_sixtyThree.isChecked()) {
                        subtruck = 2;
                        subtruckName = "Open Body";
                    }
                    //  CommonUtils.showToast(getActivity(),truck+"....."+subtruck);

                }
                //mListener.onSelectVehicle(truck,truckName, subtruck, subtruckName);
                startActivityWithResult();
                //mListener.replaceFragment(Constants.TAG_FRAGMENT_SET_ROUTE, truck,subtruck);
                break;

        }


    }

    private void startActivityWithResult() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.KEY_VEHICLE_TYPE_NAME,truckName);
        returnIntent.putExtra(Constants.KEY_VEHICLE_TYPE,truck);
        returnIntent.putExtra(Constants.KEY_VEHICLE_SUB_TYPE,subtruck);
        returnIntent.putExtra(Constants.KEY_VEHICLE_SUB_TYPE_NAME,subtruckName);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}

