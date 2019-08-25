package com.icar.launcher.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.matteobattilana.weather.PrecipType;
import com.github.matteobattilana.weather.WeatherView;
import com.github.matteobattilana.weather.WeatherViewSensorEventListener;
import com.icar.launcher.R;
import com.tunabaranurut.microdb.base.MicroDB;

import studios.codelight.weatherdownloaderlibrary.model.WeatherData;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MicroDB microDB;

    private OnFragmentInteractionListener mListener;

    WeatherViewSensorEventListener weatherSensor;
    WeatherView weatherView;
    WeatherData data;
    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        weatherView = rootView.findViewById(R.id.weather_view);

        TextView temp,pressure,humidity,cityName;
        ImageView weatherImg = rootView.findViewById(R.id.imageClimate);
        temp=rootView.findViewById(R.id.temp);
        pressure=rootView.findViewById(R.id.pressure);
        humidity=rootView.findViewById(R.id.humidity);
        cityName=rootView.findViewById(R.id.cityName);

        weatherSensor = new WeatherViewSensorEventListener(this.getContext(), weatherView);
        microDB = new MicroDB(getContext());

        doWeatherStuff(temp,pressure,humidity,cityName,weatherImg);

        return rootView;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void doWeatherStuff(TextView temp, TextView pressure, TextView humidity, TextView cityName, ImageView weatherImg){
        try{data = microDB.load("weatherInfo",WeatherData.class);}catch (Exception e){}

        if(Integer.parseInt(data.getWeather()[0].getId())>=200 && Integer.parseInt(data.getWeather()[0].getId())<300){
            //Thunderstorm
            weatherView.setWeatherData(PrecipType.RAIN);
            weatherView.setSpeed(1000);
            weatherView.setEmissionRate(1000);
            weatherImg.setImageResource(R.drawable.ic_storm);
        }
        else if(Integer.parseInt(data.getWeather()[0].getId())>=300 && Integer.parseInt(data.getWeather()[0].getId())<400){
            //Drizzle
            weatherView.setWeatherData(PrecipType.RAIN);
            weatherView.setSpeed(600);
            weatherView.setEmissionRate(200);
            weatherImg.setImageResource(R.drawable.ic_lightrain);

        }
        else if(Integer.parseInt(data.getWeather()[0].getId())>=500 && Integer.parseInt(data.getWeather()[0].getId())<600){
            weatherView.setWeatherData(PrecipType.RAIN);
            weatherView.setSpeed(800);
            weatherView.setEmissionRate(600);
            weatherImg.setImageResource(R.drawable.ic_rain);
        }
        else if(Integer.parseInt(data.getWeather()[0].getId())>=600 && Integer.parseInt(data.getWeather()[0].getId())<700){
            weatherView.setWeatherData(PrecipType.SNOW);
            weatherImg.setImageResource(R.drawable.ic_snow);
        }
        else if(Integer.parseInt(data.getWeather()[0].getId())==800){
            //limpo
            weatherView.setWeatherData(PrecipType.CLEAR);
            weatherImg.setImageResource(R.drawable.ic_sun);

        }
        else if(Integer.parseInt(data.getWeather()[0].getId())==803 || Integer.parseInt(data.getWeather()[0].getId())==804){
            //	lots of clouds
            weatherImg.setImageResource(R.drawable.ic_partlycloudy);
        }
        else{
            weatherImg.setImageResource(R.drawable.ic_sun);
        }
        temp.setText("Temperatura: "+(int)(Double.parseDouble(data.getMain().getTemp())-273.15) + " °C");
        pressure.setText("Pressão: "+(int)(Double.parseDouble(data.getMain().getPressure())) + " hPa");
        humidity.setText("Humidade: "+data.getMain().getHumidity() + " %");
        cityName.setText(data.getName());
    }
}