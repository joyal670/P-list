package com.protenium.irohub.ui.main.dashboard.home.model;

public class DateModel {
    private int date;
    private boolean isActive;
    private boolean isDisabled;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public DateModel(int date, boolean isActive, boolean isDisabled) {
        this.date = date;
        this.isActive = isActive;
        this.isDisabled = isDisabled;
    }
}
