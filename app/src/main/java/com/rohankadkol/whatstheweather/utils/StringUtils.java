package com.rohankadkol.whatstheweather.utils;

import com.rohankadkol.whatstheweather.pojos.WeatherResponse;

public final class StringUtils {
    private StringUtils() {
    }

    public static String generateWeatherString(WeatherResponse weatherResponse, String location) {
        // TODO: Generate a String of the weather data from the weather response
        return "Location: " + location + "\n" +
                "Temp: " + weatherResponse.getMain().getTemp() + " F\n" +
                "Temp Min: " + weatherResponse.getMain().getTempMin() + " F\n" +
                "Temp Max: " + weatherResponse.getMain().getTempMax() + " F\n" +
                "Main: " + weatherResponse.getWeather().getMain() + "\n" +
                "Description: " + weatherResponse.getWeather().getDescription();
    }

    // alpha is bigger than bravo
    // character = A
    // str = "A" + "lpha"
    // Alpha Is Bigger Than Bravo
    public static String generateTitleCase(String string) {
        String[] strings = string.split(" ");
        StringBuilder stringBuilder = new StringBuilder(" ");
        char character;
        for (String str : strings) {
            character = (char) (str.charAt(0) - 32);
            str = character + str.substring(1);
            stringBuilder.append(str).append(" ");
        }
        return stringBuilder.toString();
    }

    public static String getFormattedTemp(double temp) {
        return (int) temp + " F";
    }
}
