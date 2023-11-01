package com.makent.trioangle.helper;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  helper
 * @category    GoogleStaticMapsAPIServices
 * @author      Trioangle Product Team
 * @version     1.1
 */

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;

public class GoogleStaticMapsAPIServices
{
    private static final double EARTH_RADIUS_KM = 6371;


    public  String getStaticMapURL(LatLng lat, int radiusMeters,String mapKey) {

        String pathString = "";
       /* System.out.println("locccccccc" +location);
        System.out.println("locccccccc111" +location.getLatitude());
        System.out.println("locccccccc222" +location.getLongitude());*/
        if (radiusMeters > 0)
        {
            // Add radius path
            ArrayList<LatLng> circlePoints = getCircleAsPolyline(lat, radiusMeters);

            if (circlePoints.size() > 0)
            {
                String encodedPathLocations = PolyUtil.encode(circlePoints);
                pathString = "&path=color:0x014741%7Cweight:2%7Cfillcolor:0x7fd2cc%7Cenc:" + encodedPathLocations;
            }
        }

        String staticMapURL = "https://maps.googleapis.com/maps/api/staticmap?size=640x480&" +
                lat.latitude + "," + lat.longitude +
                pathString +
                "&key=" + mapKey;

        System.out.println("locccccccyyyyyy" +staticMapURL);

        return staticMapURL;
    }

    private static ArrayList<LatLng> getCircleAsPolyline(LatLng lat, int radiusMeters)
    {
        ArrayList<LatLng> path = new ArrayList<>();

        double latitudeRadians = lat.latitude* Math.PI / 180.0;
        double longitudeRadians = lat.longitude* Math.PI / 180.0;
        double radiusRadians = radiusMeters / 1000.0 / EARTH_RADIUS_KM;

        double calcLatPrefix = Math.sin(latitudeRadians) * Math.cos(radiusRadians);
        double calcLatSuffix = Math.cos(latitudeRadians) * Math.sin(radiusRadians);

        for (int angle = 0; angle < 361; angle += 10)
        {
            double angleRadians = angle * Math.PI / 180.0;

            double latitude = Math.asin(calcLatPrefix + calcLatSuffix * Math.cos(angleRadians));
            double longitude = ((longitudeRadians + Math.atan2(Math.sin(angleRadians) * Math.sin(radiusRadians) * Math.cos(latitudeRadians), Math.cos(radiusRadians) - Math.sin(latitudeRadians) * Math.sin(latitude))) * 180) / Math.PI;
            latitude = latitude * 180.0 / Math.PI;

            path.add(new LatLng(latitude, longitude));
        }

        return path;
    }
}
