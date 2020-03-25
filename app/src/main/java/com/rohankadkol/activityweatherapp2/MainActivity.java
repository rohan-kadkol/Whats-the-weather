package com.rohankadkol.activityweatherapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.rohankadkol.activityweatherapp2.pojos.WeatherResponse;
import com.rohankadkol.activityweatherapp2.utils.InternetUtils;
import com.rohankadkol.activityweatherapp2.utils.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String location = "baton rouge";

        WeatherResponse weatherResponse = null;
        try {
            URL url = InternetUtils.createUrl(location);
            String jsonResponse = InternetUtils.makeHttpRequest(url);
            weatherResponse = InternetUtils.extractWeatherFromJson(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (weatherResponse != null) {
            TextView textView = findViewById(R.id.tv_weather);
            textView.setText(StringUtils.generateWeatherString(weatherResponse));
        }
    }
}
