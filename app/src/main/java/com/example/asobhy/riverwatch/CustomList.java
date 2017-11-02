package com.example.asobhy.riverwatch;

/**
 * Created by ahmed on 11/1/2017.
 */

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> web;
    private final Integer[] imageId;
    private final List<AlertColorCode> colorCodes;

    public CustomList(Activity context,
                      ArrayList<String> web, List<AlertColorCode> cCode, Integer[] imageId) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
        this.colorCodes=cCode;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        txtTitle.setText(web.get(position));

        if(colorCodes.get(position).getColor_cur() == Color.GREEN){

            imageView.setImageResource(imageId[0]);

        } else if (colorCodes.get(position).getColor_cur() == Color.YELLOW){

            imageView.setImageResource(imageId[1]);

        } else if (colorCodes.get(position).getColor_cur() == Color.rgb(255,165,0)) {

            imageView.setImageResource(imageId[2]);

        } else {

            imageView.setImageResource(imageId[3]);

        }

        return rowView;

    }
}