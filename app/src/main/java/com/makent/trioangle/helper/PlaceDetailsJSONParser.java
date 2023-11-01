package com.makent.trioangle.helper;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  helper
 * @category    PlaceDetailsJSONParser
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceDetailsJSONParser {
	

	public List<HashMap<String,String>> parse(JSONObject jObject){		
		
		Double lat = Double.valueOf(0);
		Double lng = Double.valueOf(0);
		String formattedAddress = "";
		
		HashMap<String, String> hm = new HashMap<String, String>();
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		
		try {			
			lat = (Double)jObject.getJSONObject("resultTemp").getJSONObject("geometry").getJSONObject("location").get("lat");
			lng = (Double)jObject.getJSONObject("resultTemp").getJSONObject("geometry").getJSONObject("location").get("lng");
			formattedAddress = (String) jObject.getJSONObject("resultTemp").get("formatted_address");
			
		} catch (JSONException e) {
			e.printStackTrace();			
		}catch(Exception e){			
			e.printStackTrace();
		}
		
		
		hm.put("lat", Double.toString(lat));
		hm.put("lng", Double.toString(lng));
		hm.put("formatted_address",formattedAddress);
		
		list.add(hm);
		
		return list;
	}	
	public boolean isOnline(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c
		.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		 
		if (ni != null && ni.isConnected())
		  return true;
		else
		  return false;
		}	
}
