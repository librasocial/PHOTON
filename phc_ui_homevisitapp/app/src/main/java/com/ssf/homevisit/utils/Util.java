package com.ssf.homevisit.utils;

import static com.ssf.homevisit.requestmanager.AppDefines.ACCESS_TOKEN;
import static com.ssf.homevisit.requestmanager.AppDefines.APICONTENTTYPE;
import static com.ssf.homevisit.requestmanager.AppDefines.APICONTENTTYPEVALUECOGNITO;
import static com.ssf.homevisit.requestmanager.AppDefines.ID_TOKEN;
import static com.ssf.homevisit.requestmanager.AppDefines.PREFERENCES;
import static com.ssf.homevisit.requestmanager.AppDefines.X_AMZ_TARGET;
import static com.ssf.homevisit.requestmanager.AppDefines.X_AMZ_TARGET_VALUE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonParser;
import com.ssf.homevisit.controller.AppController;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.google.gson.JsonParser.parseString;
import static com.ssf.homevisit.requestmanager.AppDefines.*;

import org.json.JSONObject;

import io.jsonwebtoken.Jwt;

public class Util {

    public static Map<String, String> getHeadersCongnito() {
        Map<String, String> map = new HashMap<>();
        map.put(APICONTENTTYPE, APICONTENTTYPEVALUECOGNITO);
        map.put(X_AMZ_TARGET, X_AMZ_TARGET_VALUE);
        return map;
    }

    public static String getHeader() {
        try {
            SharedPreferences prefs = AppController.getInstance().getMainActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            String token = prefs.getString(ACCESS_TOKEN, "");
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getIdToken() {
        try {
            SharedPreferences prefs = AppController.getInstance().getMainActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            String token = prefs.getString(ID_TOKEN, "");
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressLint("NewApi")
    public static String getDecodeId() {
        try {
            byte[] actualByte = Base64.decode(getHeader(), Base64.DEFAULT);
            String newString = new String(actualByte);
            String salted = newString;
            String[] seperator = salted.split("(^[\\w-]*\\.[\\w-]*\\.[\\w-]*$)");
            String[] trees = salted.split("\"");

            return Arrays.toString(trees);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Not working";
    }

    /**
     * Degree Decimal to Degree Minute Seconds
     */
    public static String DDtoDMS(double coordinate, String type) {
        // Set flag if number is negative
        boolean neg = coordinate < 0d;

        // Work with a positive number
        coordinate = Math.abs(coordinate);

        // Get d/m/s components
        double d = Math.floor(coordinate);
        coordinate -= d;
        coordinate *= 60;
        double m = Math.floor(coordinate);
        coordinate -= m;
        coordinate *= 60;
        double s = Math.round(coordinate * 100.0) / 100.0;

        // Create padding character

        // Create d/m/s strings
        String dd = (d + "").split("\\.")[0];
        String mm = (m + "").split("\\.")[0];
        String ss = s + "";

        // Append d/m/s
        String dms = dd + "°" + mm + "'" + ss + "''";

        // Append compass heading
        switch (type) {
            case "longitude":
                dms += neg ? "W" : "E";
                break;
            case "latitude":
                dms += neg ? "S" : "N";
                break;
        }

        // Return formated string
        return dms;
    }

    public static String getUuid() {
        String idToken = getIdToken();
        try {
            String[] split = idToken.split("\\.");

            JSONObject jsonObject = new JSONObject(getJson(split[1]));

            return jsonObject.getString("sub");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
