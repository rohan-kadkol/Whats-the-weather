package com.rohankadkol.activityweatherapp2.utils;

import com.rohankadkol.activityweatherapp2.pojos.WeatherResponse;

public final class StringUtils {
    private StringUtils() {}

    public static String generateWeatherString (WeatherResponse weatherResponse) {
        // TODO: Generate a String of the weather data from the weather response
        return "Temp: " + weatherResponse.getMain().getTemp() + " F\n" +
                "Temp Min: " + weatherResponse.getMain().getTempMin() + " F\n" +
                "Temp Max: " + weatherResponse.getMain().getTempMax() + " F\n" +
                "Main: " + weatherResponse.getWeather().getMain() + "\n" +
                "Description: " + weatherResponse.getWeather().getDescription();
    }
}
