package com.rohankadkol.whatstheweather.pojos;

public class Main {
    private double temp;
    private double tempMin;
    private double tempMax;

    public Main(double temp, double tempMin, double tempMax) {
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public double getTemp() {
        return temp;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }
}
