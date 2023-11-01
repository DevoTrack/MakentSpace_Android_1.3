package com.makent.trioangle.model;

import android.graphics.drawable.Drawable;

public class EventTypeModel {
  private String EventTypeTitle;
  private Drawable EventTypeImage;
  private String Events_hours_Rate;
  private String Events_Fullday_Rate;
  private String Min_Booking_hours;

    public EventTypeModel(String eventTypeTitle, Drawable eventTypeImage, String events_hours_Rate, String events_Fullday_Rate, String min_Booking_hours) {
        EventTypeTitle = eventTypeTitle;
        EventTypeImage = eventTypeImage;
        Events_hours_Rate = events_hours_Rate;
        Events_Fullday_Rate = events_Fullday_Rate;
        Min_Booking_hours = min_Booking_hours;
    }

    public String getEventTypeTitle() {
        return EventTypeTitle;
    }

    public void setEventTypeTitle(String eventTypeTitle) {
        EventTypeTitle = eventTypeTitle;
    }

    public Drawable getEventTypeImage() {
        return EventTypeImage;
    }

    public void setEventTypeImage(Drawable eventTypeImage) {
        EventTypeImage = eventTypeImage;
    }

    public String getEvents_hours_Rate() {
        return Events_hours_Rate;
    }

    public void setEvents_hours_Rate(String events_hours_Rate) {
        Events_hours_Rate = events_hours_Rate;
    }

    public String getEvents_Fullday_Rate() {
        return Events_Fullday_Rate;
    }

    public void setEvents_Fullday_Rate(String events_Fullday_Rate) {
        Events_Fullday_Rate = events_Fullday_Rate;
    }

    public String getMin_Booking_hours() {
        return Min_Booking_hours;
    }

    public void setMin_Booking_hours(String min_Booking_hours) {
        Min_Booking_hours = min_Booking_hours;
    }
}
