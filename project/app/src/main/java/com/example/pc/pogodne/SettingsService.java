package com.example.pc.pogodne;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsService
{
    Context settingsContext;

    SettingsService(Context context)
    {
        settingsContext = context;
    }

    public void setSize(int size)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("size", size);
        editor.commit();
    }

    public int getSize()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getInt("size", 15);
    }
}
