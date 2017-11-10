package com.totalplay.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class Prefs {

    private static String PREFS_NAME;
    private static Prefs singleton;
    private final SharedPreferences preferences;
    private Context context;

    private Prefs(Context context) {
        preferences = context
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void setDefaultContext(Context context) {
        with(context);
    }

    public static Prefs instance() {
        if (singleton == null || singleton.context == null) {
            throw new IllegalArgumentException("Call setDefaultContext(context) first");
        }
        return singleton;
    }

    public static Prefs with(Context context) {
        if (singleton == null) {
            synchronized (Prefs.class) {
                if (singleton == null) {
                    if (context == null) {
                        throw new IllegalArgumentException("Context must not be null");
                    }
                    singleton = new Prefs(context);
                    singleton.context = context.getApplicationContext();
                }
            }
        }
        return singleton;
    }

    /**
     * @param key Key for preferences
     * @return intValue
     */
    public int integer(String key) {
        return preferences.getInt(key, -1);
    }

    public int integer(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public String string(String key) {
        return preferences.getString(key, "");
    }

    public String string(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public boolean bool(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean bool(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public long number(String key) {
        return preferences.getLong(key, -1L);
    }

    public long number(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public Object object(String key, Class<?> fromClass) {
        try {
            String json = preferences.getString(key, null);
            if (json != null) {
                Gson gson = new Gson();
                return gson.fromJson(json, fromClass);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public void putInt(String key, int value) {
        preferences.edit()
                .putInt(key, value)
                .apply();
    }

    public void putBool(String key, boolean value) {
        preferences.edit()
                .putBoolean(key, value)
                .apply();
    }

    public void putString(String key, String value) {
        preferences.edit()
                .putString(key, value)
                .apply();
    }

    public void putLong(String key, long value) {
        preferences.edit()
                .putLong(key, value)
                .apply();
    }

    public void putObject(String key, Object value, Class<?> fromClass) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(value, fromClass);
            preferences.edit()
                    .putString(key, json)
                    .apply();
        } catch (Exception e) {
            throw new IllegalArgumentException("Only plain objects are supported");
        }
    }

    public long getLong(String key) {
        return preferences.getLong(key, 0L);
    }
}
