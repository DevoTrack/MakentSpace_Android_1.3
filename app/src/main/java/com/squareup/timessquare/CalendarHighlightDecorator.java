package com.squareup.timessquare;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bowshulsheikrahaman on 27/01/17.
 */

public class CalendarHighlightDecorator implements CalendarCellDecorator {
    private Context context;

    private ArrayList<Date> dates = new ArrayList<>();

    public CalendarHighlightDecorator(Context context, ArrayList<Date> dates) {
        this.context = context;
        if(dates!=null)
        {
          this.dates=dates;//.addAll(Arrays.asList(dates));
        }
    }

    @Override
    public void decorate(CalendarCellView calendarCellView, Date date) {
       /* if (calendarCellView.isHighlighted()) {
            calendarCellView.setBackgroundResource(R.drawable.cog);
        }*/
    }
}