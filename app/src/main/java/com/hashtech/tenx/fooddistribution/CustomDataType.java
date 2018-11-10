package com.hashtech.tenx.fooddistribution;

public class CustomDataType {

    private String nameOfSupplier;
    private int phone;
    private String address;
    private String day;
    private int time;
    private int surplus;

    public CustomDataType(String nameOfSupplier, int phone, String address, String day, int time, int surplus) {
        this.nameOfSupplier = nameOfSupplier;
        this.phone = phone;
        this.address = address;
        this.day = day;
        this.time = time;
        this.surplus = surplus;
    }

    public String getNameOfSupplier() {
        return nameOfSupplier;
    }

    public int getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getDay() {
        return day;
    }

    public int getTime() {
        return time;
    }

    public int getSurplus() {
        return surplus;
    }
}
