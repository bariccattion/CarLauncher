package com.icar.launcher.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.github.matteobattilana.weather.WeatherViewSensorEventListener;
import com.icar.launcher.R;
import com.icar.launcher.content.AppListContent;
import com.icar.launcher.fragments.ListFragment;
import com.icar.launcher.fragments.MainFragment;
import com.location.aravind.getlocation.GeoLocator;
import com.tunabaranurut.microdb.base.MicroDB;

import studios.codelight.weatherdownloaderlibrary.WeatherDownloader;
import studios.codelight.weatherdownloaderlibrary.model.WeatherData;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener , ListFragment.OnListFragmentInteractionListener, WeatherDownloader.WeatherDataDownloadListener {
    ImageButton button1,button2,button3;
    TabHost tabHost;
    FragmentManager fm;
    MicroDB microDB;
    WeatherViewSensorEventListener weatherSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        tabHost = findViewById(R.id.tabhost);

        fm = getSupportFragmentManager();

        microDB = new MicroDB(this);

        MainFragment();

        GeoLocator geoLocator = new GeoLocator(getApplicationContext(),this);
        String coordinatesQuery = geoLocator.getLattitude()+":"+geoLocator.getLongitude();
        WeatherDownloader downloader = new WeatherDownloader(this, WeatherDownloader.Mode.COORDINATES);
        downloader.getCurrentWeatherData(getResources().getString(R.string.apikey), coordinatesQuery);



    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    public void onListFragmentInteraction(AppListContent.AppItem uri){
        //you can leave it empty
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    public void onWeatherDownloadComplete(WeatherData data, WeatherDownloader.Mode mode) {
        try{microDB.save("weatherInfo",data);}catch (Exception e){
            Log.d("D","D");
        }
    }

    @Override
    public void onWeatherDownloadFailed(Exception e) {
        Log.d("D","D");
    }
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void tabHandler(View target){
        button1.setSelected(false);
        button2.setSelected(false);
        button3.setSelected(false);
        if(target.getId() == R.id.button1){
            tabHost.setCurrentTab(0);
            button1.setSelected(true);
        } else if(target.getId() == R.id.button2){
            if(tabHost.getCurrentTab()!=1){
                MainFragment();
            }
        } else if(target.getId() == R.id.button3){
            tabHost.setCurrentTab(2);
            button3.setSelected(true);
            startActivity(new Intent(Intent.ACTION_VOICE_COMMAND)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void MainFragment(){
        tabHost.setCurrentTab(1);
        button2.setSelected(true);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new MainFragment());
        ft.commit();
    }

}
