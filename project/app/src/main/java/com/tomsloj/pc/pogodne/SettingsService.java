package com.tomsloj.pc.pogodne;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import java.util.Date;

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
        //SharedPreferences.Editor editor = preferences.edit();
        return preferences.getInt("size", 15);
    }

    private long getLongTime(Date date)
    {
        return date.getTime();
    }

    public void setPrevVersion(String version)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("prevVersion", version);
        editor.commit();
    }

    public String getPrevVersion()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);

        return preferences.getString("prevVersion", "0");
    }

    public String getCurrentVersion()
    {
        String version = "error";
        PackageInfo pinfo = null;
        try {
            pinfo = settingsContext.getPackageManager().getPackageInfo( settingsContext.getPackageName(), 0);
            version = pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public void setNewNameOfFavorite(String name)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("NewNameOfFavorite", name);
        editor.commit();
    }

    public String getNewNameOfFavorite()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsContext);
        return preferences.getString("NewNameOfFavorite", "");

    }

}
