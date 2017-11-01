package com.example.asobhy.riverwatch;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String stationName;
    ListView stationListView;
    ArrayList<String> date;


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


        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, R.layout.center, R.id.text1, stationsNames){

            // the following code will iterate through rows and set the background color according to
            // alert level color codes

            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);

                // Set a background color for ListView regular row/item
                view.setBackgroundColor(colorCodes.get(position).getColor_cur());

                return view;

            }

        };

        stationListView.setAdapter(ad);


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

    }


}
