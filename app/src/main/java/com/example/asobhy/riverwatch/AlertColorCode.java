package com.example.asobhy.riverwatch;

import android.graphics.Color;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ahmed on 10/31/2017.
 */

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class AlertColorCode implements Serializable {

    private int color_cur;
    private int color_24;
    private int color_48;

    // constructor
    public AlertColorCode(StationRiverAlerts alert, StationRiverForecast forecast) {

        color_cur = retColorCode(
                alert.getWarning(),
                alert.getWatch(),
                alert.getAdvisory(),
                forecast.getForecast_cur()
        );

        color_24 = retColorCode(
                alert.getWarning(),
                alert.getWatch(),
                alert.getAdvisory(),
                forecast.getForecast_24()
        );

        color_48 = retColorCode(
                alert.getWarning(),
                alert.getWatch(),
                alert.getAdvisory(),
                forecast.getForecast_48()
        );

    }

    // constructor
    public AlertColorCode(){

        color_cur = 0 ;
        color_24 = 0 ;
        color_48 = 0 ;

    }

    public void setColorCode(StationRiverAlerts alert, StationRiverForecast forecast){

        color_cur = retColorCode(
                alert.getWarning(),
                alert.getWatch(),
                alert.getAdvisory(),
                forecast.getForecast_cur()
        );

        color_24 = retColorCode(
                alert.getWarning(),
                alert.getWatch(),
                alert.getAdvisory(),
                forecast.getForecast_24()
        );

        color_48 = retColorCode(
                alert.getWarning(),
                alert.getWatch(),
                alert.getAdvisory(),
                forecast.getForecast_48()
        );

    }


    public int getColor_cur(){
        return color_cur;
    }

    public int getColor_24(){
        return color_24;
    }

    public int getColor_48(){
        return color_48;
    }


    // given the high, mid and low levels and level to compare it returns a color code
    private int retColorCode(
            float h,
            float m,
            float l,
            float level
    ){

        int retVal = 0;

        if (level < l ){

            retVal = Color.GREEN;

        } else if( (level >= l) && (level < m) ){

            retVal = Color.YELLOW;

        } else if ( (level >= m) && (level < h) ) {

            retVal = Color.rgb(255,165,0); //Orange

        } else if ( level >= h ) {

            retVal = Color.RED;

        } else{}

        return retVal;

    }

}