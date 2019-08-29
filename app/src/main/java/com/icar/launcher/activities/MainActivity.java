package com.icar.launcher.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.icar.launcher.R;
import com.icar.launcher.content.AppListContent;
import com.icar.launcher.fragments.CarFragment;
import com.icar.launcher.fragments.ListFragment;
import com.icar.launcher.fragments.MainFragment;
import com.icar.launcher.location.LocationInfo;
import com.icar.launcher.receiver.MusicReceiver;
import com.tunabaranurut.microdb.base.MicroDB;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import studios.codelight.weatherdownloaderlibrary.WeatherDownloader;
import studios.codelight.weatherdownloaderlibrary.model.WeatherData;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, ListFragment.OnListFragmentInteractionListener, WeatherDownloader.WeatherDataDownloadListener, LocationListener {
    ImageButton button1,button2,button3;
    TabHost tabHost;
    FragmentManager fm;
    MicroDB microDB;
    Double latitude, longitude;
    MusicReceiver musicReceiver = new MusicReceiver();
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


        new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.settings_menu)
                .withGravity(SlideGravity.RIGHT)
                .withDragDistance(120)
                .inject();

        //GeoLocator geoLocator = new GeoLocator(getApplicationContext(),MainActivity.this);
        //String coordinatesQuery = geoLocator.getLattitude()+":"+geoLocator.getLongitude();


        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("fm.last.android.metachanged");
        iF.addAction("fm.last.android.playbackpaused");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.nullsoft.winamp.playstatechanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.amazon.mp3.playstatechanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.miui.player.playstatechanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.real.IMP.playstatechanged");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.sonyericsson.music.playstatechanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.rdio.android.playstatechanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.playstatechanged");
        iF.addAction("com.andrew.apollo.metachanged");
        iF.addAction("com.andrew.apollo.playstatechanged");
        iF.addAction("com.htc.music.metachanged");
        iF.addAction("com.htc.music.playstatechanged");
        iF.addAction("com.spotify.music.playbackstatechanged");
        iF.addAction("com.spotify.music.metadatachanged");
        iF.addAction("com.spotify.music.queuechanged");
        iF.addAction("com.rhapsody.playstatechanged");

        registerReceiver(musicReceiver, iF);

        LocationManager locationManager = LocationInfo.getLocation(this);

        String coordinatesQuery = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude() + ":" + locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
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

        }
        MainFragment();
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

    @Override
    public void onLocationChanged(Location location) {

        //locationText = location.getLatitude() + "," + location.getLongitude();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(OfflineGPS.this, "Please Enable GPS", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onDestroy(){
        unregisterReceiver(musicReceiver);
        super.onDestroy();
    }

    public void tabHandler(View target){
        button1.setSelected(false);
        button2.setSelected(false);
        button3.setSelected(false);
        if(target.getId() == R.id.button1){
            tabHost.setCurrentTab(0);
            button1.setSelected(true);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new CarFragment());
            ft.commit();
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
