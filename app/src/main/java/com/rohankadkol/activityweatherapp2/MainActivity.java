package com.rohankadkol.activityweatherapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.rohankadkol.activityweatherapp2.pojos.WeatherResponse;
import com.rohankadkol.activityweatherapp2.utils.InternetUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String location = "baton rouge";

//        StringBuilder builder = new StringBuilder();
//        builder.append("as");
//        String string = builder.toString();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.openweathermap.org");
        builder.appendPath("data");
        builder.appendPath("2.5");
        builder.appendPath("weather");
        builder.appendQueryParameter("q", location);
        // TODO: Enter your API Key below
        builder.appendQueryParameter("appid", "");
        Uri uri = builder.build();
        URL url = null;
        WeatherResponse weatherResponse;
        try {
            url = new URL(uri.toString());
            String jsonResponse = InternetUtils.makeHttpRequest(url);
            weatherResponse = InternetUtils.extractWeatherFromJson(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView textView = findViewById(R.id.tv_weather);
        textView.setText(temp);
    }
}
