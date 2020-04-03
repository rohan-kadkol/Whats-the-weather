package com.rohankadkol.whatstheweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.rohankadkol.whatstheweather.loaders.CustomLoader;
import com.rohankadkol.whatstheweather.pojos.WeatherResponse;
import com.rohankadkol.whatstheweather.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<WeatherResponse> {
    String mLocation = "";

    EditText etLocation;
    TextView tvMain;
    TextView tvDescription;
    TextView tvHigh;
    TextView tvTemp;
    TextView tvLow;
    ImageView ivIcon;
    ConstraintLayout mRootLayout;
    ProgressBar mProgressBar;
    TextView tvError;
    AdView mAdView;

    private static final int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLocation = findViewById(R.id.et_location);
        tvMain = findViewById(R.id.tv_main);
        tvDescription = findViewById(R.id.tv_description);
        tvHigh = findViewById(R.id.tv_high);
        tvTemp = findViewById(R.id.tv_temp);
        tvLow = findViewById(R.id.tv_low);
        ivIcon = findViewById(R.id.iv_icon);
        mRootLayout = findViewById(R.id.root_layout);
        mProgressBar = findViewById(R.id.progress_bar);
        tvError = findViewById(R.id.error_view);
        mAdView = findViewById(R.id.adView);

        if (savedInstanceState != null) {
            mLocation = savedInstanceState.getString(getString(R.string.key_location));
        }

        setupListeners();
        loadWeather(mLocation);
        loadAds();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(getString(R.string.key_location), mLocation);
        super.onSaveInstanceState(outState);
    }

    private void setupListeners() {
        mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEditTextFocus();
            }
        });

        etLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    etLocation.setText(etLocation.getText().toString().trim());
                    loadWeather(etLocation.getText().toString());
                }
            }
        });

        etLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearEditTextFocus();
                }
                return false;
            }
        });
    }

    public void clearEditTextFocus() {
        etLocation.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(etLocation.getWindowToken(), 0);
    }

    public void loadWeather(String location) {
        if (TextUtils.isEmpty(location)) {
            etLocation.setText(mLocation);
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        if (mLocation.equals(location)) {
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            mLocation = location.trim();
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    public void modifyMainViewVisibility(int visibility) {
        tvMain.setVisibility(visibility);
        tvDescription.setVisibility(visibility);
        tvHigh.setVisibility(visibility);
        tvTemp.setVisibility(visibility);
        tvLow.setVisibility(visibility);
        ivIcon.setVisibility(visibility);
    }

    public void modifyErrorViewVisibility(int visibility) {
        tvError.setVisibility(visibility);
    }

    public void loadAds() {
        List<String> testDeviceIds = Collections.singletonList("16E5769DF269BE0BB2C006411F23C641");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @NonNull
    @Override
    public Loader<WeatherResponse> onCreateLoader(int id, @Nullable Bundle args) {
        return new CustomLoader(this, mLocation.trim());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<WeatherResponse> loader, WeatherResponse weatherResponse) {
        mProgressBar.setVisibility(View.INVISIBLE);

        if (weatherResponse == null) {
            modifyMainViewVisibility(View.INVISIBLE);
            modifyErrorViewVisibility(View.VISIBLE);
            return;
        }

        modifyMainViewVisibility(View.VISIBLE);
        modifyErrorViewVisibility(View.INVISIBLE);

        tvMain.setText(weatherResponse.getWeather().getMain());
        tvDescription.setText(StringUtils.generateTitleCase(weatherResponse.getWeather().getDescription()));
        tvHigh.setText(StringUtils.getFormattedTemp(weatherResponse.getMain().getTempMax()));
        tvTemp.setText(StringUtils.getFormattedTemp(weatherResponse.getMain().getTemp()));
        tvLow.setText(StringUtils.getFormattedTemp(weatherResponse.getMain().getTempMin()));

        String icon = weatherResponse.getWeather().getIcon();
        final String iconUrl = "http://openweathermap.org/img/wn/" + icon + "@2x.png";
        Picasso.get().load(iconUrl).into(ivIcon, new Callback() {
            @Override
            public void onSuccess() {
                ivIcon.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {
                ivIcon.setVisibility(View.INVISIBLE);
            }
        });

        Toast.makeText(this, "Weather loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<WeatherResponse> loader) {

    }
}
