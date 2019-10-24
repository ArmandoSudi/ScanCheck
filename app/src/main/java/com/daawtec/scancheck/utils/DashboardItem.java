package com.daawtec.scancheck.utils;

public class DashboardItem {

    String title;
    String number;
    String backgroundColor;

    public DashboardItem(String title, String number, String backgroundColor) {
        this.title = title;
        this.number = number;
        this.backgroundColor = backgroundColor;
    }

    public DashboardItem() {
    }

    public String getTitle() {
        return title;
    }

    public String getNumber() {
        return number;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }
}
