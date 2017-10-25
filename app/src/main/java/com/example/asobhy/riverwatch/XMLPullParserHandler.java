package com.example.asobhy.riverwatch;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 10/25/2017.
 */

public class XMLPullParserHandler {

    private List<StationRiverForcast> stationRiverForcasts = new ArrayList<StationRiverForcast>();
    private StationRiverForcast stationRiverForcast;
    private String text;

    public List<StationRiverForcast> getStationRiverForcasts() {
        return stationRiverForcasts;
    }

    public List<StationRiverForcast> parse(InputStream is) {

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
                            // create a new instance of stationRiverForcast
                            stationRiverForcast = new StationRiverForcast();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("station")) {
                            // add stationRiverForcast object to list
                            stationRiverForcasts.add(stationRiverForcast);
                        }else if (tagname.equalsIgnoreCase("stationID")) {
                            stationRiverForcast.setStationID(Integer.parseInt(text));
                        }  else if (tagname.equalsIgnoreCase("name")) {
                            stationRiverForcast.setStationName(text);
                        } else if (tagname.equalsIgnoreCase("forecast_cur")) {
                            stationRiverForcast.setForcast_cur(Float.parseFloat(text));
                        } else if (tagname.equalsIgnoreCase("forecast_24")) {
                            stationRiverForcast.setForcast_24(Float.parseFloat(text));
                        } else if (tagname.equalsIgnoreCase("forecast_48")) {
                            stationRiverForcast.setForcast_48(Float.parseFloat(text));
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

        return stationRiverForcasts;

    }

}
