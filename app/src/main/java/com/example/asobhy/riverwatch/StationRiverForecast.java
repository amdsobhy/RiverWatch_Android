package com.example.asobhy.riverwatch;

/**
 * Created by ahmed on 10/25/2017.
 */


import java.io.Serializable;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class StationRiverForecast implements Serializable{

    private String stationName;
    private int stationID;
    private float forecast_cur;
    private float forecast_24;
    private float forecast_48;

/*
    public StationRiverForecast( StationRiverForecast x ){

        this.setForecast_cur(x.getForecast_cur());
        this.setForecast_24(x.getForecast_24());
        this.setForecast_48(x.getForecast_48());
        this.setStationID(x.getStationID());
        this.setStationName(x.getStationName());

    }
*/

    public int getStationID(){
        return stationID;
    }

    public void setStationID(int id){
        stationID = id;
    }

    public float getForecast_cur(){
        return forecast_cur;
    }


    public void setForecast_cur(float forecast){
        forecast_cur = forecast;
    }

    public float getForecast_24(){
        return forecast_24;
    }

    public void setForecast_24(float forecast){
        forecast_24 = forecast;
    }

    public float getForecast_48(){
        return forecast_48;
    }

    public void setForecast_48(float forecast){
        forecast_48 = forecast;
    }


    public String getStationName(){
        return stationName;
    }

    public void setStationName(String name){
        stationName = name;
    }


    @Override
    public String toString(){
        return ("Station ID " + stationID + "\nLocation: " + stationName + "\nCurrent Level: " + forecast_cur +
                "\n+24hrs: " + forecast_24 + "\n+48hrs: " + forecast_48);
    }

}

