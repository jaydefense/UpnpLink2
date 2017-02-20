package com.application.upnplink.utils;

import android.content.SharedPreferences;

/**
 * Created by jperraudeau on 15/02/2017.
 */

public class PreferencesUtils {
    public static void putPreference(SharedPreferences sharedPreferences, String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        }
        editor.commit();
    }

    public static String getPreference(SharedPreferences sharedPreferences, String key) {
        return sharedPreferences.getString(key, null);
    }
}
