package com.example.asobhy.riverwatch;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import java.util.ArrayList;


public class StationDataActivity extends AppCompatActivity {

    TextView selectedStationTextView;
    TextView StNameTextView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_data);

        Intent in = getIntent();

        // Store data passed from main activity
        StationRiverForecast selectedStation = (StationRiverForecast) in.getSerializableExtra("StationObject");
        StationRiverAlerts selectedAlerts = (StationRiverAlerts) in.getSerializableExtra("stationAlerts");
        AlertColorCode selectedStationColorCode = (AlertColorCode) in.getSerializableExtra("colorCode");
        ArrayList<String> date = (ArrayList<String>) in.getSerializableExtra("date");

        Log.i("test", Float.toString(selectedAlerts.getAdvisory()));

        selectedStationTextView = (TextView)findViewById(R.id.selectedStationTextView);
        // append year to date because date from xml is missing year
        ArrayList<String> fullDate = new ArrayList<>();
        fullDate = appendYrToDate(date);

        String line1 = "Level on " + fullDate.get(0)  + ": " + "<b>" + selectedStation.getForecast_cur() + "m </b>" + "<br/>";
        String line2 = "Level on " + fullDate.get(2)  + ": " + "<b>" + selectedStation.getForecast_24() + "m </b>" + "<br/>";
        String line3 = "Level on " + fullDate.get(4)  + ": " + "<b>" + selectedStation.getForecast_48() + "m";

        selectedStationTextView.setText(Html.fromHtml(line1+line2+line3));

        StNameTextView = (TextView)findViewById(R.id.StNameTextView);
        StNameTextView.setText(selectedStation.getStationName());


        // place forecast data into an ArrayList to be passed to StationGraph class
        ArrayList<Float> forecastArray = new ArrayList<>();
        forecastArray.add(selectedStation.getForecast_cur());
        forecastArray.add(selectedStation.getForecast_24());
        forecastArray.add(selectedStation.getForecast_48());

        // place alert levels data into an ArrayList to be passed to StationGraph class
        ArrayList<Float> alertLvlArray = new ArrayList<>();
        alertLvlArray.add(selectedAlerts.getAdvisory());
        alertLvlArray.add(selectedAlerts.getWatch());
        alertLvlArray.add(selectedAlerts.getWarning());
        alertLvlArray.add(selectedAlerts.getFloodLvl());

        GraphView gView = (GraphView) findViewById(R.id.graph);

        StationGraph graph = new StationGraph(fullDate, forecastArray, alertLvlArray, gView);

        graph.plot();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> appendYrToDate(ArrayList<String> dateWithoutYr){

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);

        // adjust year if data is old and date of view is in new year
        if( month < 3 ){
            year--;
        }
        // convert year to string then add it to date
        String stringYear = Integer.toString(year);

        ArrayList<String> fullDate = new ArrayList<>();

        for(int i=0; i<dateWithoutYr.size(); i++){

            fullDate.add(i, dateWithoutYr.get(i) + " " + stringYear );
            Log.i("date", fullDate.get(i));

        }

        return fullDate;

    }


}
