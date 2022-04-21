package com.example.giuaky;

import java.io.Serializable;
import java.util.ArrayList;

public class Staff implements Serializable {

    private String id;
    private String fName;
    private String lName;
    private int factory;
    private String image;

    //temperature
    private int tmpSumDay;

    private ArrayList<Day> days = new ArrayList<>();

    public Staff(){

    }

    public Staff(String id, String fName, String lName, int factory, String image) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.factory = factory;
        this.image = image;
    }

    public Staff(String id, String fName, String lName, int factory, String image, int tmpSumDay) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.factory = factory;
        this.image = image;
        this.tmpSumDay = tmpSumDay;
    }

    public Staff(String id, String fName, String lName, int factory, String image, ArrayList<Day> days) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.factory = factory;
        this.image = image;
        this.days = days;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public int getFactory() {
        return factory;
    }

    public void setFactory(int factory) {
        this.factory = factory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public Day getDay(String id){
        for (Day d:days){
           if(d.getId().equals(id)) return d;
        }
        return null;
    }

    public int getTmpSumDay() {
        return tmpSumDay;
    }

    public void setTmpSumDay(int tmpSumDay) {
        this.tmpSumDay = tmpSumDay;
    }

}
