package com.protenium.irohub.model.calender;

public class CalenderModel {
   public String date;
   public boolean isChecked;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public CalenderModel(String date, boolean isChecked) {
        this.date = date;
        this.isChecked = isChecked;
    }
}
