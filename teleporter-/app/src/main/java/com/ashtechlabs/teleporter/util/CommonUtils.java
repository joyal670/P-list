package com.ashtechlabs.teleporter.util;

import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ashtechlabs.teleporter.dialog_fragments.AlertFragmentDialog;
import com.ashtechlabs.teleporter.dialog_fragments.CallDialogFragment;
import com.ashtechlabs.teleporter.dialog_fragments.CustomerDetailDialogFragment;
import com.ashtechlabs.teleporter.dialog_fragments.SignUpDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by VIDHU on 12/21/2016.
 */

public class CommonUtils {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private static SimpleDateFormat simpleOldDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

//    public static void showDatePickerAlert(AppCompatActivity activity, Fragment fragment, String selected_date, String tag) {
//
//        DialogFragment newFragment = new DatePickerActivityDialog();
//        Bundle bundle = new Bundle();
//        bundle.putString("selected_date", selected_date);
//        newFragment.setArguments(bundle);
//        newFragment.setTargetFragment(fragment, 100);
//        newFragment.show(activity.getSupportFragmentManager(), tag);
//
//    }
//
//    public static void showDeclareOrderAlertDialog(AppCompatActivity activity, String orderId) {
//
//        DeclareOrderDialogFragment newFragment = DeclareOrderDialogFragment.newInstance(orderId);
//        newFragment.show(activity.getSupportFragmentManager(), "alert_dialog");
//    }

    public static void showAlertDialog(FragmentActivity activity, String title, String message) {

        AlertFragmentDialog newFragment = AlertFragmentDialog.newInstance(title, message);
        newFragment.show(activity.getSupportFragmentManager(), "alert_dialog");
    }

    public static void showCallDialog(AppCompatActivity activity, String title, String message) {

        CallDialogFragment newFragment = CallDialogFragment.newInstance(title, message);
        newFragment.show(activity.getSupportFragmentManager(), "call_dialog");
    }

    public static void showCustomerDetailDialog(FragmentActivity activity, String name, String phone) {

        CustomerDetailDialogFragment customerDetailDialogFragment = CustomerDetailDialogFragment.newInstance(name, phone);
        customerDetailDialogFragment.show(activity.getSupportFragmentManager(), "customer_detail");
    }


    public static void showSignUpDialogFragment(FragmentActivity activity) {

        SignUpDialogFragment signUpDialogFragment = SignUpDialogFragment.newInstance();
        signUpDialogFragment.show(activity.getSupportFragmentManager(), "sign_up_dialog");
    }


//    public static void showBookingSuccessDialog(AppCompatActivity activity, String title, String message) {
//
//        BookingSuccessDialog newFragment = BookingSuccessDialog.newInstance(title, message);
//        newFragment.show(activity.getSupportFragmentManager(), "alert_dialog");
//    }
//
//    public static void showProgressDialog(FragmentActivity activity, int message) {
//
//        ProgressDialogFragment newFragment = ProgressDialogFragment.newInstance(message);
//        newFragment.show(activity.getSupportFragmentManager(), "progress_dialog");
//    }

    public static void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static String isGoingToExpire(String rate_validity) throws ParseException {
        Log.e("DATE>>> ", rate_validity);
        Date endDate;
        try {
            endDate = simpleDateFormat.parse(rate_validity);
        }catch (ParseException e){
            endDate = simpleOldDateFormat.parse(rate_validity);
        }

        Date startDate = new Date();
        //milliseconds
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long different = (endDate.getTime()+daysInMilli) - startDate.getTime();

//        System.out.println("startDate : " + startDate);
//        System.out.println("endDate : " + endDate);
//        System.out.println("different : " + different);


        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;

//            long elapsedHours = different / hoursInMilli;
//            different = different % hoursInMilli;
//
//            long elapsedMinutes = different / minutesInMilli;
//            different = different % minutesInMilli;
//
//            long elapsedSeconds = different / secondsInMilli;

        if (elapsedDays <= 3) {

            if (elapsedDays == 1) {
                return "Expiring Tomorrow";
            }else if(elapsedDays == 0){
                return "Expiring Today";
            }else if(elapsedDays < 0){
                return "Expired";
            }

            return "Expires within " + elapsedDays + " Days";
        }

//            System.out.printf(
//                    "%d days, %d hours, %d minutes, %d seconds%n",
//                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return "";
    }

    public static String getVechicleType(String vehicleType, String vehicleSubType) {

        StringBuilder typeName = new StringBuilder();

        if ((TextUtils.isEmpty(vehicleType) || (TextUtils.isEmpty(vehicleType))))
            return "";

        if (vehicleType.equals("0")) {
            typeName.append("1 Ton Truck");
        } else if (vehicleType.equals("1")) {
            typeName.append("3 Ton Truck");
        } else if (vehicleType.equals("2")) {
            typeName.append("7 Ton Truck");
        } else if (vehicleType.equals("3")) {
            typeName.append("10 Ton Truck");
        } else if (vehicleType.equals("4")) {
            typeName.append("40 Feet Container Truck");
        } else if (vehicleType.equals("5")) {
            typeName.append("60 Feet Truck");
        } else if (vehicleType.equals("6")) {
            typeName.append("Bulker Truck");
        } else if (vehicleType.equals("7")) {
            typeName.append("Low bed Truck");
        } else if (vehicleType.equals("8")) {
            typeName.append("Tanker Truck");
        } else if (vehicleType.equals("9")) {
            typeName.append("Tipper Truck");
        } else if (vehicleType.equals("10")) {
            typeName.append("Bikes");
        }


        if (!vehicleSubType.equals("-1")) {

            switch (vehicleType) {
                case "0":

                    if (vehicleSubType.equals("0")) {
                        typeName.append("-").append("Close body");
                    } else {
                        typeName.append("-").append("Open body");
                    }

                    break;
                case "1":
                    if (vehicleSubType.equals("0")) {
                        typeName.append("-").append("Reefer");
                    } else if (vehicleSubType.equals("1")) {
                        typeName.append("-").append("Close body");
                    } else {
                        typeName.append("-").append("Open body");
                    }
                    break;
                case "2":
                    if (vehicleSubType.equals("0")) {
                        typeName.append("-").append("Reefer");
                    } else if (vehicleSubType.equals("1")) {
                        typeName.append("-").append("Close body");
                    } else if (vehicleSubType.equals("2")) {
                        typeName.append("-").append("Open body");
                    } else {
                        typeName.append("-").append("With Crane");
                    }
                    break;
                case "3":
                    if (vehicleSubType.equals("0")) {
                        typeName.append("-").append("Reefer");
                    } else if (vehicleSubType.equals("1")) {
                        typeName.append("-").append("Close body");
                    } else {
                        typeName.append("-").append("Open body");
                    }
                    break;
                case "4":
                    if (vehicleSubType.equals("0")) {
                        typeName.append("-").append("Reefer");
                    } else if (vehicleSubType.equals("1")) {
                        typeName.append("-").append("Close body");
                    } else if (vehicleSubType.equals("2")) {
                        typeName.append("-").append("Open body");
                    } else {
                        typeName.append("-").append("Side lifter");
                    }
                    break;
                case "5":
                    if (vehicleSubType.equals("0")) {
                        typeName.append("-").append("Close body");
                    } else if (vehicleSubType.equals("1")) {
                        typeName.append("-").append("Open body");
                    } else {
                        typeName.append("-").append("Side Lifter");
                    }
                    break;
                default:

                    break;

            }

        }

        return typeName.toString();
    }
}
