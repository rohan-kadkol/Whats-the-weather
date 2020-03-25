package com.rohankadkol.activityweatherapp2.pojos;

public class WeatherResponse {
    private Main main;
    private Weather weather;

    public WeatherResponse(double temp, double tempMin, double tempMax, String mainWeather, String description, String icon) {
        main = new Main(temp, tempMin, tempMax);
        weather = new Weather(mainWeather, description, icon);
    }

    public Main getMain() {
        return main;
    }

    public Weather getWeather() {
        return weather;
    }

    //    private int integer;
//
//    public WeatherResponse(int integer) {
//        this.integer = integer;
//    }
}
