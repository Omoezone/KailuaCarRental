package com.company;

public class Customers {
    private int customer_id;
    private String first_name;
    private String last_name;
    private String address;
    private String zip_code;
    private String mobile;
    private String phone;
    private String email;
    private String driver_since_date;

    // Constructor
    public Customers(int customer_id, String first_name, String last_name, String address, String zip_code, String mobile, String phone, String email, String driver_since_date) {
        this.customer_id = customer_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.zip_code = zip_code;
        this.mobile = mobile;
        this.phone = phone;
        this.email = email;
        this.driver_since_date = driver_since_date;
    }

    //toString
    public String toString(){
        return("\nName of driver: "+ first_name +"\nDriver's address: "+address+"\nCity: "+ zip_code +"\nMobile: "+mobile+"\nPhone: "+phone+"\nEmail: "+email);
    }
    public String toFile(){
        return (first_name +","+ address+","+ zip_code +","+ mobile+","+phone+","+email);
    }
    public String[] toArray(){
        String[] rentObj ={first_name, address, zip_code, mobile, phone,email};
        return rentObj;
    }
    //getters
    public String getFirst_name(){
        return first_name;
    }
    public String getDriverAddress(){
        return address;
    }

    public String getZip_code(){
        return zip_code;
    }
    public String getMobile(){
        return mobile;
    }
    public String getDriverPhone(){
        return phone;
    }
    public String getEmail(){
        return email;
    }

    //Setters
    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }
    public void setDriverAddress(String driverAddress){
        this.address = driverAddress;
    }

    public void setZip_code(String zip_code){
        this.zip_code = zip_code;
    }
    public void setMobile(String mobile){
        this.mobile = mobile;
    }
    public void setDriverPhone(String driverPhone){
        this.phone = driverPhone;
    }
    public void setEmail(String email){ this.email = email; }

}
