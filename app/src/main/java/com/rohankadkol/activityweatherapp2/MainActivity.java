package com.rohankadkol.activityweatherapp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Context;
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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<WeatherResponse> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String location = "mumbai";

        Bundle bundle = new Bundle();
        bundle.putString("location", location);
        getSupportLoaderManager().initLoader(0, bundle, this);
    }

    @NonNull
    @Override
    public Loader<WeatherResponse> onCreateLoader(int id, @Nullable Bundle args) {
        String location = args.getString("location");
        return new CustomLoader(this, location);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<WeatherResponse> loader, WeatherResponse weatherResponse) {
        if (weatherResponse != null) {
            TextView textView = findViewById(R.id.tv_weather);
            textView.setText(StringUtils.generateWeatherString(weatherResponse));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<WeatherResponse> loader) {

    }


    public static class CustomLoader extends AsyncTaskLoader<WeatherResponse> {
        String location;

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
}
