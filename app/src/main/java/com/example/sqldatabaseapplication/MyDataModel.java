package com.example.sqldatabaseapplication;

public class MyDataModel {
    private int id;
    private String date;
    private String time;
    private String description;

    boolean status=false;

    public boolean isStatus() {
        return status;
    }

    public MyDataModel(String date, String time, String description) {
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(String editedDate) {

    }

    public void setTime(String editedTime) {


    }

    public void setDescription(String editedDescription) {

    }
}
