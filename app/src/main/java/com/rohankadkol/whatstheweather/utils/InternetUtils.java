package com.rohankadkol.whatstheweather.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.rohankadkol.whatstheweather.BuildConfig;
import com.rohankadkol.whatstheweather.pojos.WeatherResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class InternetUtils {
    private static final String API_KEY = BuildConfig.API_KEY;

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static WeatherResponse extractWeatherFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        double temp = 0, tempMin = 0, tempMax = 0;
        String mainWeather = null, description = null, icon = null;
        try {
            JSONObject root = new JSONObject(jsonResponse);

            JSONObject main = root.getJSONObject("main");
            temp = main.getDouble("temp");
            tempMin = main.getDouble("temp_min");
            tempMax = main.getDouble("temp_max");

            // TODO: Get the values of mainWeather, description, and the icon strings from the jsonResponse
            // To get a JSONObject from a JSONArray
            //  jsonArray.getJSONObject(index);
            JSONArray weatherList = root.getJSONArray("weather");
            JSONObject weather = weatherList.getJSONObject(0);
            mainWeather = weather.getString("main");
            description = weather.getString("description");
            icon = weather.getString("icon");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new WeatherResponse(temp, tempMin, tempMax, mainWeather, description, icon);
    }

    public static URL createUrl(String location) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.openweathermap.org");
        builder.path("data/2.5/weather");
        builder.appendQueryParameter("q", location);
        builder.appendQueryParameter("appid", API_KEY);
        builder.appendQueryParameter("units", "imperial");

        Uri uri = builder.build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    // To get JSON Object with a specific key
    //  JSONObject object = root.getJSONObject("key");
    // To get an int from a JSON Object with a particular key
    //  int integer = object.getInt("key");

    public static void loadWeather(Activity activity) {

    }
}
