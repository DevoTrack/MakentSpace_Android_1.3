package com.makent.trioangle.controller;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  controller
 * @category    LocalSharedPreferences
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by test on 10/7/16.
 */
public class LocalSharedPreferences {
    // variable to hold context
    private Context context;

//save the context recievied via constructor in a local variable

    public LocalSharedPreferences(Context context){
        this.context=context;
    }

    /*   *******************************************************************************

                                 Store data in shared preference

         ******************************************************************************** */


    public void saveSharedPreferences(String key,String value)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void saveSharedPreferences(String key,int value)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void saveSharedPreferences(String key,float value)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public void saveSharedPreferences(String key,long value)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void saveSharedPreferences(String key,boolean value)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /*   *******************************************************************************

                               Get data in shared preference

       ******************************************************************************** */
    public String getSharedPreferences(String key)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String value = settings.getString(key,null);
        return value;
    }


    public int getSharedPreferencesInt(String key)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        int value = settings.getInt(key,0);
        return value;
    }

    public float getSharedPreferencesFloat(String key)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        float value = settings.getFloat(key,0);
        return value;
    }

    public long getSharedPreferencesLong(String key)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        long value = settings.getLong(key,0);
        return value;
    }

    public Boolean getSharedPreferencesBool(String key)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean value = settings.getBoolean(key,false);
        return value;
    }

      /*   *******************************************************************************

                                 Clear data in shared preference

         ******************************************************************************** */

    public void clearSharedPreferences()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        editor.apply();
    }


    /* Key used in makent

    isHost  -> check is host or guest  to show switch to hosting or travelling
    houserules, payment, host  -> check steps remaing for request to book  to show the steps remaining
    isRequest -> Check house rules form request page or rooms details page to show or hide agree button


     */
}
