package com.example.asobhy.riverwatch;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    String cityName;
    String[] cityList;
    EditText cityEditText;
    TextView rLevelTextView;
    //String rLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rLevelTextView = (TextView) findViewById(R.id.RiverLevelTextView);

        cityEditText = (EditText) findViewById(R.id.cityEditText);

        cityName ="";

        cityList = new String[]{
                "ST. LEONARD",
                "PERTH ANDOVER",
                "SIMONDS",
                "HARTLAND",
                "WOODSTOCK",
                "NASHWAAK",
                "KENNEBECASIS",
                "FREDERICTON",
                "MAUGERVILLE",
                "JEMSEG",
                "GRAND LAKE",
                "SHEFFIELD-LAKEVILLE CORNER",
                "OAK POINT",
                "QUISPAMSIS-SAINT JOHN"
        };

    }



    public void getRiverLevel(View view) {

        // hide keypad
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);

        String rLevel="";

        boolean inCityList = false;

        String webSrc = downloadWebPageSrc("http://www.gnb.ca/cnb/update/levels-e.asp");

        cityName = cityEditText.getText().toString();
        cityName = cityName.toUpperCase();

        for(int i=0;i<cityList.length;i++){

            if( cityName.equals(cityList[i]) ){
                inCityList = true;
                break;
            }
        }

        if( inCityList == false ){

            Toast.makeText(getApplicationContext(),"Location not found, Plz retry.", Toast.LENGTH_SHORT).show();

        } else {

            rLevel = extractNStore(webSrc, cityName);

            if(rLevel.isEmpty()){
                rLevel = "N/A";
            }else{
                rLevel += "m";
            }

            rLevelTextView.setText("River Level: " + rLevel );

        }

    }

    /*********************************************************************************************/
    // Method to extract and store river level from html src http://www.gnb.ca/cnb/update/levels-e.asp
    // input: reference to the ArrayLists and website source code
    // return: String
    /*********************************************************************************************/
    public String extractNStore(String websiteSrc , String city ){

        String riverLevel="";
        String locationDataUnfiltered="";
        ArrayList<String> locationDataNumbers = new ArrayList<String>();

        // extract the line that contains the data of location we need
        Pattern p = Pattern.compile(city + "\t</TD><TD ALIGN=center VALIGN=bottom NOWRAP><FONT SIZE=-1>\tW/L\t</TD><TD ALIGN=center VALIGN=bottom NOWRAP><FONT SIZE=-1>\t(.*?)</TR>");
        Matcher m = p.matcher(websiteSrc);

        while(m.find()){
            locationDataUnfiltered= m.group(1);
        }

        // extracts the data from the line we extracted from the website's source
        Pattern p1 = Pattern.compile("-?\\d+(,\\d+)*?\\.?\\d+?");
        Matcher m1 = p1.matcher(locationDataUnfiltered);

        while(m1.find()){

            locationDataNumbers.add(m1.group());

        }

        // overflow level is at index 0
        // current level is at index 1
        // 1+ day forcast is at index 2
        // 2+ day forcast is at index 3
        riverLevel = locationDataNumbers.get(1);

        return riverLevel;

    }


    /*********************************************************************************************/
    // Method to download web page src in string format
    // input: url address in string format
    // return: web page src in string format
    /*********************************************************************************************/
    public String downloadWebPageSrc(String url){

        Downloader task = new Downloader();
        String result = null;

        try {

            result = task.execute(url).get();


        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

        return result;

    }


    /*********************************************************************************************/
    // Class to download web page src in string format
    /*********************************************************************************************/
    public class Downloader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while ( data != -1 ){

                    char current = (char)data;
                    result += current;
                    data = reader.read();

                }

                return result;

            }catch (Exception e) {

                e.printStackTrace();
                return "Failed";
            }

        }
    }


}
