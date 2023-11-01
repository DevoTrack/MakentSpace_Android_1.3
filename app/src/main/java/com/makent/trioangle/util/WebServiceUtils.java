package com.makent.trioangle.util;
/**
 * @package com.trioangle.gofereats
 * @subpackage utils
 * @category WebServiceUtils
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.Gson;
import com.makent.trioangle.controller.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;


/*****************************************************************
 WebServiceUtils
 ****************************************************************/

public class WebServiceUtils {
    public @Inject
    Gson gson;

    public WebServiceUtils() {
        AppController.getAppComponent().inject(this);
    }

    public Object getJsonObjectModel(String jsonString, Object object) {
        Object objct = object;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            objct = gson.fromJson(jsonObject.toString(), (Class<Object>) object);
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
        return objct;
    }

    public Object getJsonValue(String jsonString, String key, Object object) {
        Object objct = object;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has(key)) objct = jsonObject.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
        return objct;
    }

    public Object getJsonArrayModel(String jsonString, Object object) {
        Object[] objects = null;
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            if (jsonArray.length() > 0) {
                objects = gson.fromJson(jsonArray.toString(), (Class<Object[]>) object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
        return new java.util.ArrayList(Arrays.asList(objects));
    }
}
