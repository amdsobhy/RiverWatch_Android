package com.example.asobhy.riverwatch;

import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String stationName;
    ListView stationListView;
    ArrayList<String> date;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stationListView = (ListView) findViewById(R.id.stationListView);

        // download website xml contents to strings
        String stringXML = Downloader.downloadWebPageSrc("http://www.gnb.ca/nosearch/0911/flood/alert.xml");
        String stringAlertXML = Downloader.downloadWebPageSrc("http://www.gnb.ca/nosearch/0911/flood/alertlevels.xml");

        List<StationRiverForecast> stationRiverForecasts = null;
        List<StationRiverAlerts> stationRiverAlertsList = null;

        // parse xml levels
        InputStream is = null;

        try {

            XMLPullParserHandler Parser = new XMLPullParserHandler();
            is = new ByteArrayInputStream(stringXML.getBytes("UTF-8"));
            stationRiverForecasts = Parser.parse(is);
            date = Parser.getDate();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }


        // parse alerts levels xml
        InputStream inputStream = null;

        try {

            XMLAlertsPullParserHandler alertParser = new XMLAlertsPullParserHandler();
            inputStream = new ByteArrayInputStream(stringAlertXML.getBytes("UTF-8"));
            stationRiverAlertsList = alertParser.parse(inputStream);


        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        // extract station names into a separate arraylist to use it later to be displayed
        // on the ListView
        ArrayList<String> stationsNames = new ArrayList<>();

        for(int i=0; i<stationRiverForecasts.size(); i++){

            stationsNames.add(stationRiverForecasts.get(i).getStationName());

        }

        final List<StationRiverForecast> finalStationRiverForecasts = stationRiverForecasts;
        final List<StationRiverAlerts> finalStationRiverAlertsList = stationRiverAlertsList;
        final List<AlertColorCode> colorCodes = new ArrayList<>();

        for(int i=0; i<stationRiverAlertsList.size(); i++){

            AlertColorCode colorCode = new AlertColorCode(finalStationRiverAlertsList.get(i), finalStationRiverForecasts.get(i));
            colorCodes.add(colorCode);

        }


        Integer[] imageId = {
                R.drawable.green,
                R.drawable.yellow,
                R.drawable.orange,
                R.drawable.red
        };

        CustomList adapter = new CustomList(MainActivity.this, stationsNames, colorCodes, imageId);
        stationListView=(ListView)findViewById(R.id.stationListView);

        stationListView.setAdapter(adapter);

        stationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent stationDataIntent = new Intent(getApplicationContext(),StationDataActivity.class);

                // data to be passed to StationDataActivity class
                stationDataIntent.putExtra("StationObject", finalStationRiverForecasts.get(i) );
                stationDataIntent.putExtra("date", date );
                stationDataIntent.putExtra("stationAlerts", finalStationRiverAlertsList.get(i) );
                stationDataIntent.putExtra("colorCodes", colorCodes.get(i) );

                startActivity(stationDataIntent);

            }
        });


        TextView dateTV = (TextView) findViewById(R.id.DateTextView);

        dateTV.setText( "Last Update: " + appendYrToDate(date).get(0) );

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
