package com.rohankadkol.whatstheweather.loaders;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.rohankadkol.whatstheweather.pojos.WeatherResponse;
import com.rohankadkol.whatstheweather.utils.InternetUtils;

import java.io.IOException;
import java.net.URL;

public class CustomLoader extends AsyncTaskLoader<WeatherResponse> {
    private String location;

    public CustomLoader(@NonNull Context context, String location) {
        super(context);
        this.location = location;
        forceLoad();
    }

    @Nullable
    @Override
    public WeatherResponse loadInBackground() {
        WeatherResponse weatherResponse = null;
        try {
            URL url = InternetUtils.createUrl(location);
            String jsonResponse = InternetUtils.makeHttpRequest(url);
            weatherResponse = InternetUtils.extractWeatherFromJson(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherResponse;
    }
}
