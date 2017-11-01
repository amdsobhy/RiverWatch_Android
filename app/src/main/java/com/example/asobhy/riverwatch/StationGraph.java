package com.example.asobhy.riverwatch;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.icu.math.BigDecimal;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ahmed on 10/26/2017.
 */

public class StationGraph {

    private ArrayList<Date> d;
    private ArrayList<Float> y;
    private ArrayList<Float> yAlertLvl;
    private GraphView graph;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public StationGraph(ArrayList<String> dates, ArrayList<Float> yAxis, ArrayList<Float> yAxisAlert, GraphView g ){

        d= new ArrayList<>();
        y= new ArrayList<>();
        yAlertLvl = new ArrayList<>();

        // take English version of date only by jumping 2 places each loop
        for(int i=0; i<dates.size(); i+=2) {
            d.add(stringToDate(dates.get(i)));
        }

        for(int i=0; i<yAxis.size(); i++){
            y.add(yAxis.get(i));
        }

        for(int i=0; i<yAxisAlert.size(); i++){
            yAlertLvl.add(yAxisAlert.get(i));
        }

        graph = g;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void plot(){

        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(d.get(0), y.get(0)),
                new DataPoint(d.get(1), y.get(1)),
                new DataPoint(d.get(2), y.get(2))
        });
        graph.addSeries(series);


        // watch
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(DateUtil.addDays(d.get(0),-1), yAlertLvl.get(1)),
                new DataPoint(d.get(0), yAlertLvl.get(1)),
                new DataPoint(d.get(1), yAlertLvl.get(1)),
                new DataPoint(d.get(2), yAlertLvl.get(1)),
                new DataPoint(DateUtil.addDays(d.get(2),1), yAlertLvl.get(1))
        });
        graph.addSeries(series2);

        // warning
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(DateUtil.addDays(d.get(0),-1), yAlertLvl.get(2)),
                new DataPoint(d.get(0), yAlertLvl.get(2)),
                new DataPoint(d.get(1), yAlertLvl.get(2)),
                new DataPoint(d.get(2), yAlertLvl.get(2)),
                new DataPoint(DateUtil.addDays(d.get(2),1), yAlertLvl.get(2))
        });
        graph.addSeries(series3);

        // FloodLevel
        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(DateUtil.addDays(d.get(0),-1), yAlertLvl.get(3)),
                new DataPoint(d.get(0), yAlertLvl.get(3)),
                new DataPoint(d.get(1), yAlertLvl.get(3)),
                new DataPoint(d.get(2), yAlertLvl.get(3)),
                new DataPoint(DateUtil.addDays(d.get(2),1), yAlertLvl.get(3))
        });
        graph.addSeries(series4);


        Log.i("watch", Float.toString(yAlertLvl.get(1)));

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(DateUtil.addDays(d.get(0),-1).getTime());
        graph.getViewport().setMaxX(DateUtil.addDays(d.get(2),1).getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        // set manual y bounds to have nice steps
        // set maximum = floodlevel + 1
        graph.getViewport().setMinY((int)(findMinY()-1));
        graph.getViewport().setMaxY((int)(yAlertLvl.get(3)+1));
        graph.getViewport().setYAxisBoundsManual(true);

        // set width of labels so that numbers would fully appear
        graph.getGridLabelRenderer().setLabelVerticalWidth(120);
        // as we use dates as labels, the human rounding to nice readable numbers
        // is not nessesary
        graph.getGridLabelRenderer().setHumanRounding(false);

        // styling bargraph
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(75);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);

        // Styling Linegraph watch
        // styling series
        series2.setTitle("Watch");
        series2.setColor(Color.YELLOW);
        //series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(10);
        series2.setThickness(8);

        // Styling Linegraph warnings
        // styling series
        series3.setTitle("Warning");
        series3.setColor(Color.rgb(255,165,0)); // orange
        //series3.setDrawDataPoints(true);
        series3.setDataPointsRadius(10);
        series3.setThickness(8);

        // Styling Linegraph floodLevel
        // styling series
        series4.setTitle("FloodLevel");
        series4.setColor(Color.RED);
        //series4.setDrawDataPoints(true);
        series4.setDataPointsRadius(10);
        series4.setThickness(8);

    }


    public static class DateUtil
    {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public static Date addDays(Date date, int days)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days); //minus number would decrement the days
            return cal.getTime();

        }
    }


    private float findMinY(){

        // set min to the first element
        float min=y.get(0);

        for(int i=1; i<y.size();i++){
            // compare elements
            if( y.get(i) < min ){
                min = y.get(i);
            }

        }

        return min;

    }

    private float findMaxY(){

        // set max to the first element
        float max=y.get(0);

        for(int i=1; i<y.size();i++){
            // compare elements
            if( y.get(i) > max ){
                max = y.get(i);
            }

        }

        return max;

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public Date stringToDate(String s){

        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");

        // convert string to date format
        try {
            d = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return d;

    }

}
