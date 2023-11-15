package com.ashtechlabs.teleporter.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by VIDHU on 11/3/2016.
 */

public class DatePickerActivityDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private String selectedDate = "";
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    private OnDatePickListener datePickerDialogListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            selectedDate = getArguments().getString("selected_date", "");
        }

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            datePickerDialogListener = (OnDatePickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "attaching d fragment failed!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        datePickerDialogListener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


        if (!selectedDate.equals("")) {
                getSplitedDate(pickerDialog, selectedDate, getTag());
        }
        // Create a new instance of DatePickerDialog and return it
        return pickerDialog;
    }

    private void getSplitedDate(DatePickerDialog pickerDialog, String selectedDate, String tag) {

            String str[] = selectedDate.split("/");
            int day = Integer.parseInt(str[0]);
            int month = Integer.parseInt(str[1]);
            int year = Integer.parseInt(str[2]);
            pickerDialog.updateDate(year, (month - 1), day);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //OnDatePickListener datePickerDialog = (OnDatePickListener) getTargetFragment();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        String strDate = format.format(calendar.getTime());
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
