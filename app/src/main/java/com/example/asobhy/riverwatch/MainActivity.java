package com.example.asobhy.riverwatch;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String stationName;
    EditText stationEditText;
    TextView rLevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rLevelTextView = (TextView) findViewById(R.id.RiverLevelTextView);
        stationEditText = (EditText) findViewById(R.id.stationEditText);
        stationName = "";

    }


    public void getRiverLevel(View view) {

        String stringXML = Downloader.downloadWebPageSrc("http://www.gnb.ca/nosearch/0911/flood/alert.xml");

        // hide keypad
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(stationEditText.getWindowToken(), 0);

        String rLevel = "";

        stationName = stationEditText.getText().toString();
        stationName = stationName.toLowerCase();

        List<StationRiverForcast> stationRiverForcasts = null;


        InputStream is = null;
        try {

            XMLPullParserHandler parser = new XMLPullParserHandler();
            is = new ByteArrayInputStream(stringXML.getBytes("UTF-8"));
            stationRiverForcasts = parser.parse(is);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int index = -1;

        for(int i=0; i<stationRiverForcasts.size(); i++){

            if( stationName.equals(stationRiverForcasts.get(i).getstationName().toLowerCase())){
                index = i;
                break;
            }

        }

        if(index != -1) {
            rLevelTextView.setText(stationRiverForcasts.get(index).toString());
        } else {
            // error msg if location is not in the list
            Toast.makeText(getApplicationContext(),"Station not found", Toast.LENGTH_SHORT).show();
        }

    }
    
}
