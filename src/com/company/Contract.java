package com.company;

import java.util.*;
//Contract class
public class Contract{
    private String customer_id;
    private String fromDate;
    private String toDate;
    private String maxKm;
    private String reg_number; //(plate)

    //constructor
    public Contract(String customer_id, String fromDate, String toDate, String maxKm, String reg_number){
        this.customer_id = customer_id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.maxKm = maxKm;
        this.reg_number = reg_number;
    }
    //'to-' methods
    @Override
    public String toString(){
        return("Name of Driver: "+ customer_id +"\nFrom date&time: "+ fromDate +"\nTo date&time: "+ toDate +"\nMax km allowed: "+maxKm+"\nOdometer at start: "+"\nregistration number of rental car: "+ reg_number);
    }
    public String toFile(){
        return (customer_id +","+ fromDate +","+ toDate +","+ maxKm+","+ reg_number);
    }
    public String[] toArray(){
        String[] contObj ={customer_id, fromDate, toDate, maxKm, reg_number};
        return contObj;
    }

    //getters
    public String getCustomer_id(){
        return customer_id;
    }
    public String getFromDate(){
        return fromDate;
    }
    public String getToDate(){
        return toDate;
    }
    public String getMaxKm(){
        return maxKm;
    }
    public String getReg_number(){
        return reg_number;
    }

    //setters
    public void setCustomer_id(String customer_id){
        this.customer_id = customer_id;
    }
    public void setFromDate(String fromDate){
        this.fromDate = fromDate;
    }
    public void setToDate(String toDate){
        this.toDate = toDate;
    }
    public void setMaxKm(String maxKm){
        this.maxKm = maxKm;
    }
}

