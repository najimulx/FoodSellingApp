package com.hashtech.tenx.fooddistribution;

public class CustomDataType {

    private String nameOfSupplier;
    private String phone;
    private String address;
    private String day;
    private Long time;
    private String surplus;

    public CustomDataType(String nameOfSupplier, String phone, String address, String day, Long time, String surplus) {
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

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getDay() {
        return day;
    }

    public Long getTime() {
        return time;
    }

    public String getSurplus() {
        return surplus;
    }
}
