package com.example.giuaky;

import java.util.ArrayList;

public class Day {

    private String id;
    private String day;
    private ArrayList<DayDetails> detailsList = new ArrayList<>();

    public Day(){

    }

    public Day(String id, String day){
        this.id = id;
        this.day = day;
    }

    public Day(String id, String day, ArrayList<DayDetails> detailsList) {
        this.id = id;
        this.day = day;
        this.detailsList = detailsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<DayDetails> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(ArrayList<DayDetails> detailsList) {
        this.detailsList = detailsList;
    }

    public int getCountGoodProduct(){
        int sum=0;
        for(DayDetails dDetail:this.detailsList){
            sum+=dDetail.getNumbertGood();
        }
        return sum;
    }

    public int getCountBadProduct(){
        int sum=0;
        for(DayDetails dDetail:this.detailsList){
            sum+=dDetail.getNumberBad();
        }
        return sum;
    }

    public double getSumPrice(){
        int sum=0;
        for(DayDetails dDetail:this.detailsList){
            sum+=dDetail.getTotalPrice();
        }
        return sum;
    }

}
