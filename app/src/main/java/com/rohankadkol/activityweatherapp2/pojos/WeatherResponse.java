package com.rohankadkol.activityweatherapp2.pojos;

public class WeatherResponse {
    private Main main;

    public WeatherResponse(double temp) {
        main = new Main(temp);
    }
//    private int integer;
//
//    public WeatherResponse(int integer) {
//        this.integer = integer;
//    }
}