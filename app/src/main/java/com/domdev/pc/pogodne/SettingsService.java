package com.domdev.pc.pogodne;

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

    public void setTextSize(int size)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("size", size);
        editor.commit();
    }

    public int getTextSize()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);
        return preferences.getInt("size", 15);
    }

    public void setNewNameOfFavorite(String name)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("NewNameOfFavorite", name);
        editor.commit();
    }

    public void setLastDatabaseUpdate(String lastDatabaseChange)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("prevLastDatabaseUpdate", getLastDatabaseUpdateFromSettings());
        editor.commit();

        editor.putString("lastDatabaseUpdate", lastDatabaseChange);
        editor.commit();
    }

    public String getLastDatabaseUpdateFromSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);

        String lastDatabaseChange = preferences.getString("lastDatabaseUpdate", "error");
        return lastDatabaseChange;
    }

    public String getPrevLastDatabaseUpdateFromSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);

        String prevLastDatabaseUpdate = preferences.getString("prevLastDatabaseUpdate", "error");
        return prevLastDatabaseUpdate;
    }


    public String getNewNameOfFavorite()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);
        return preferences.getString("NewNameOfFavorite", "");
    }

    public Boolean isDatabaseCreated()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);
        return preferences.getBoolean("isDatabaseCreated", false);
    }

    public void databaseCreated()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("isDatabaseCreated", true);
        editor.commit();
    }

}
