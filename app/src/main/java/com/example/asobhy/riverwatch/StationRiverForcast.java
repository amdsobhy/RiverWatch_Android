package com.example.asobhy.riverwatch;

/**
 * Created by ahmed on 10/25/2017.
 */


public class StationRiverForcast {

    private String stationName;
    private int stationID;
    private float forcast_cur;
    private float forcast_24;
    private float forcast_48;


    public int getStationID(){
        return stationID;
    }

    public void setStationID(int id){
        stationID = id;
    }

    public float getForcast_cur(){
        return forcast_cur;
    }

    public void setForcast_cur(float forcast){
        forcast_cur = forcast;
    }

    public float getForcast_24(){
        return forcast_24;
    }

    public void setForcast_24(float forcast){
        forcast_24 = forcast;
    }

    public float getForcast_48(){
        return forcast_48;
    }

    public void setForcast_48(float forcast){
        forcast_48 = forcast;
    }


    public String getstationName(){
        return stationName;
    }

    public void setStationName(String name){
        stationName = name;
    }


    @Override

    public String toString(){
        return ("Station ID " + stationID + "\nLocation: " + stationName + "\nCurrent Level: " + forcast_cur +
                "\n+24hrs: " + forcast_24 + "\n+48hrs: " + forcast_48);
    }

}

