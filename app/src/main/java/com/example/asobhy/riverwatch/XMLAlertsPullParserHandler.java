package com.example.asobhy.riverwatch;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 10/26/2017.
 */

public class XMLAlertsPullParserHandler {

    private List<StationRiverAlerts> stationRiverAlertsList = new ArrayList<StationRiverAlerts>();
    private StationRiverAlerts stationRiverAlerts;
    private String text;
    private ArrayList<String> date;

    public List<StationRiverAlerts> getStationRiverAlertss() {
        return stationRiverAlertsList;
    }

    public ArrayList<String> getDate(){
        return date;
    }

    public List<StationRiverAlerts> parse(InputStream is) {

        date = new ArrayList<String>();

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("station")) {
                            // create a new instance of stationRiverAlerts
                            stationRiverAlerts = new StationRiverAlerts();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("station")) {
                            // add stationRiverAlerts object to list
                            stationRiverAlertsList.add(stationRiverAlerts);
                        }else if (tagname.equalsIgnoreCase("stationID")) {
                            stationRiverAlerts.setStationID(Integer.parseInt(text));
                        }  else if (tagname.equalsIgnoreCase("name")) {
                            stationRiverAlerts.setStationName(text);
                        } else if (tagname.equalsIgnoreCase("advisory")) {
                            stationRiverAlerts.setAdvisory(Float.parseFloat(text));
                        } else if (tagname.equalsIgnoreCase("watch")) {
                            stationRiverAlerts.setWatch(Float.parseFloat(text));
                        } else if (tagname.equalsIgnoreCase("warning")) {
                            stationRiverAlerts.setWarning(Float.parseFloat(text));
                        } else if (tagname.equalsIgnoreCase("Floodlvl")) {
                            stationRiverAlerts.setFloodLvl(Float.parseFloat(text));
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stationRiverAlertsList;

    }

}