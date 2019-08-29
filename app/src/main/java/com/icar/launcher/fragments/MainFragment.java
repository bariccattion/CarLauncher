package com.icar.launcher.fragments;

import android.app.Dialog;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.icar.launcher.R;
import com.tunabaranurut.microdb.base.MicroDB;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


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
    LinearLayout layoutWidget1,layoutWidget2,layoutChoosen;

    private static AppWidgetManager mAppWidgetManager;
    private static AppWidgetHost mAppWidgetHost;
    private static final int HARDCODED_ID = 0;
    private static final int REQUEST_PICK_APPWIDGET = 1;
    private static final int REQUEST_CREATE_APPWIDGET = 5;

    private OnFragmentInteractionListener mListener;

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
        //weatherView = rootView.findViewById(R.id.weather_view);

        layoutWidget1 = rootView.findViewById(R.id.widget1);
        layoutWidget2 = rootView.findViewById(R.id.widget2);

        layoutWidget1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFirstWidget();
            }
        });

        layoutWidget2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSecondWidget();
            }
        });

        mAppWidgetManager = AppWidgetManager.getInstance(getContext());
        mAppWidgetHost = new AppWidgetHost(getContext(), HARDCODED_ID);


        microDB = new MicroDB(getContext());

        //openWidgetChooser();

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
        mAppWidgetHost.stopListening();
        mAppWidgetHost = null;
        mListener = null;
    }

    void selectWidget() {
        int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        addEmptyData(pickIntent);
        startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
    }

    public void selectFirstWidget(){
        layoutChoosen=layoutWidget1;
        layoutChoosen.setClickable(false);
        layoutChoosen.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectFirstWidget();
                return false;
            }
        });
        selectWidget();
    }

    public void selectSecondWidget(){
        layoutChoosen=layoutWidget2;
        layoutChoosen.setClickable(false);
        layoutChoosen.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectSecondWidget();
                return false;
            }
        });
        selectWidget();
    }

    void addEmptyData(Intent pickIntent) {
        ArrayList<AppWidgetProviderInfo> customInfo =
                new ArrayList<AppWidgetProviderInfo>();
        pickIntent.putParcelableArrayListExtra(
                AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
        ArrayList<Bundle> customExtras = new ArrayList<Bundle>();
        pickIntent.putParcelableArrayListExtra(
                AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK ) {
            if (requestCode == REQUEST_PICK_APPWIDGET) {
                configureWidget(data);
            }
            else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                createWidget(data);
            }
        }
        else if (resultCode == RESULT_CANCELED && data != null) {
            int appWidgetId =
                    data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) {
                mAppWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
    }


    private void configureWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo =
                mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        if (appWidgetInfo.configure != null) {
            Intent intent =
                    new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
        } else {
            createWidget(data);
        }
    }

    public void createWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo =
                mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        AppWidgetHostView hostView =
                mAppWidgetHost.createView(getContext(), appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);
        layoutChoosen.removeAllViewsInLayout();
        layoutChoosen.addView(hostView);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAppWidgetHost.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAppWidgetHost.stopListening();
    }

    public void removeWidget(AppWidgetHostView hostView) {
        mAppWidgetHost.deleteAppWidgetId(hostView.getAppWidgetId());
        layoutChoosen.removeView(hostView);
    }

    public void openWidgetChooser() {

        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

        // Set Custom Title
        TextView title = new TextView(getContext());
        // Title Properties
        title.setText(R.string.choosewidgettitle);
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        TextView msg = new TextView(getContext());
        // Message Properties
        msg.setText(R.string.choosewidgettext);
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        alertDialog.setView(msg);

        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,getResources().getString(R.string.widget2), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                layoutChoosen=layoutWidget2;
                selectWidget();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,getResources().getString(R.string.widget1), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                layoutChoosen=layoutWidget1;
                selectWidget();
            }
        });

        new Dialog(getContext());
        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        positiveBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setTextColor(Color.BLUE);
        okBT.setLayoutParams(positiveBtnLP);

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) cancelBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.BLUE);
        cancelBT.setLayoutParams(negBtnLP);


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



}
