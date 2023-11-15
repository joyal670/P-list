package com.ashtechlabs.teleporter.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.TextUtils;
import android.widget.DatePicker;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by VIDHU on 11/3/2016.
 */

public class DatePickerFragmentDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private String selectedDate = "";
    private String fromDate = "";
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            selectedDate = getArguments().getString("selected_date", "");
            fromDate = getArguments().getString("from_date", "");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        if (!TextUtils.isEmpty(selectedDate)) {
            getSplitedDate(pickerDialog, selectedDate);
        }
        if (!TextUtils.isEmpty(fromDate)) {
            try {
                c.setTime(format.parse(fromDate));
                c.add(Calendar.DATE, 1);
                pickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // Create a new instance of DatePickerDialog and return it
        return pickerDialog;
    }

    private void getSplitedDate(DatePickerDialog pickerDialog, String selectedDate) {

        String str[] = selectedDate.split("/");
        int day = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int year = Integer.parseInt(str[2]);

        pickerDialog.updateDate(year, (month - 1), day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        OnDatePickListener datePickerDialogListener = (OnDatePickListener) getTargetFragment();
        //30 Mar 17 01:31
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        String strDate = format.format(calendar.getTime());
        //String strDate = getDay(day) + "/" + getDay(month + 1) + "/" + year;
        datePickerDialogListener.setDate(strDate, this.getTag());
        dismiss();
    }

    private String getDay(int day) {
        if (day > 9) {
            return String.valueOf(day);
        } else {
            return "0" + day;
        }
    }
}
