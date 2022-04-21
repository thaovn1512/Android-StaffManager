package com.example.giuaky;

public class DayDetails {

    private Product product;
    private int numbertGood;
    private int numberBad;

    public DayDetails(Product product, int numbertGood, int numberBad) {
        this.product = product;
        this.numbertGood = numbertGood;
        this.numberBad = numberBad;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getNumbertGood() {
        return numbertGood;
    }

    public void setNumbertGood(int numbertGood) {
        this.numbertGood = numbertGood;
    }

    public int getNumberBad() {
        return numberBad;
    }

    public void setNumberBad(int numberBad) {
        this.numberBad = numberBad;
    }

    public double getTotalPrice(){
        return (double) numbertGood*product.getPrice()-numberBad*((double) product.getPrice()/2);
    }
}
