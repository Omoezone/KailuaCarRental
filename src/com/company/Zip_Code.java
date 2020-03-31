package com.company;

public class Zip_Code {
    private int zip_code;
    private String city;

    // Constructor
    public Zip_Code(int zip_code, String city) {
        this.zip_code = zip_code;
        this.city = city;
    }

    public int getZip_code() {
        return zip_code;
    }

    public void setZip_code(int zip_code) {
        this.zip_code = zip_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

