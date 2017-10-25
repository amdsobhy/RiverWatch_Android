package com.example.asobhy.riverwatch;

/**
 * Created by ahmed on 10/24/2017.
 */

/*********************************************************************************************/
// Class to download web page src in string format

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

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

    /*********************************************************************************************/
    // Method to download web page src in string format
    // input: url address in string format
    // return: web page src in string format
    /*********************************************************************************************/
    public static String downloadWebPageSrc(String url){

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

}



