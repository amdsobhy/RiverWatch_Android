package com.example.asobhy.riverwatch;

/**
 * Created by ahmed on 10/26/2017.
 */

import java.io.Serializable;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class StationRiverAlerts implements Serializable{

    private String stationName;
    private int stationID;
    private float advisory;
    private float watch;
    private float warning;
    private float floodLvl;

    public StationRiverAlerts getStationObj(){

        return this;

    }


    public String getStationName(){
        return stationName;
    }

    public void setStationName(String name){
        stationName = name;
    }


    public int getStationID(){
        return stationID;
    }

    public void setStationID(int id){
        stationID = id;
    }


    public float getAdvisory(){
        return advisory;
    }

    public void setAdvisory(float ad){
        advisory = ad;
    }


    public float getWatch(){
        return watch;
    }

    public void setWatch(float w){
        watch = w;
    }


    public float getWarning(){
        return warning;
    }

    public void setWarning(float w){
        warning = w;
    }


    public float getFloodLvl(){
        return floodLvl;
    }

    public void setFloodLvl(float fl){
        floodLvl = fl;
    }


}
