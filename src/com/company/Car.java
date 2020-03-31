package com.company;

public class Car {
    private String reg_number;
    private String type;
    private String brand;
    private String model;
    private boolean cruise_control;
    private boolean autogear;
    private int horse_power;
    private String seat_material;
    private int seat_number;
    private boolean ac;
    private int ccm;
    private String fuel_type;
    private String regi_date;
    private String odometer;


    //Car Constructor
    public Car(String brand, String model, String fuel_type, String regiNmbr, String regi_date, String odometer){
        this.brand = brand;
        this.model = model;
        this.fuel_type = fuel_type;
        this.reg_number = regiNmbr;
        this.regi_date = regi_date;
        this.odometer = odometer;
    }
    //to file method
    public String toFile(){
        return (brand+","+ model+","+ fuel_type +","+ reg_number+","+ ac+","+ odometer);
    }
    //to array method
    public String[] toArray(){
        String[] carObj ={brand, model, fuel_type, reg_number, regi_date, odometer};
        return carObj;
    }
    //toString
    public String toString(){
        return("Brand: "+brand+"\nModel: "+model+"\nType of fuel: "+ fuel_type +"\nRegistration number: "+reg_number+"\nRegistration Y/M: "+regi_date+"\nOdometer: "+odometer+"\nGear Man/Auto: "+getGearType());
    }

    //getters
    public String getBrand(){
        return brand;
    }
    public String getModel(){
        return model;
    }
    public String getFuel_type(){
        return fuel_type;
    }
    public String getReg_number(){
        return reg_number;
    }
    public String getRegiYM(){
        return regi_date;
    }
    public String getOdometer(){
        return odometer;
    }
    public String getGearType(){
        return "Manual";
    }

    //Setters
    public void setBrand(String brand){
        this.brand=brand;
    }
    public void setModel(String model){
        this.model=model;
    }
    public void setFuel_type(String fuel_type){
        this.fuel_type = fuel_type;
    }
    public void setReg_number(String regiNmbr){
        this.reg_number=regiNmbr;
    }
    public void setRegiYM(String regiYM){
        this.regi_date=regiYM;
    }
    public void setOdometer(String odometer){
        this.odometer=odometer;
    }
}
