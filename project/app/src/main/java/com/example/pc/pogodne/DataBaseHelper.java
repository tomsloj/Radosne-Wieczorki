package com.example.pc.pogodne;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

public class DataBaseHelper  extends SQLiteOpenHelper {
    private SQLiteDatabase myBase;
    public static String dataBaseName = "baza.sqlite";
    private final Context appContext;
    public static String dataBasePath = "data/data/com.example.pc.pogodne/databases/";
    public static final int dataBAseVersion = 1;

    public  DataBaseHelper(Context context)
    {
        super(context, dataBaseName, null, dataBAseVersion);
        this.appContext = context;
        //dataBasePath = context.getDatabasePath(dataBaseName).getPath();
        //myBase = getWritableDatabase();
    }

    public void openDataBase()
    {
        //String dbPath = appContext.getDatabasePath(dataBaseName).getPath();
        if(myBase != null && myBase.isOpen())
            return;
        myBase = SQLiteDatabase.openDatabase(dataBasePath + dataBaseName, null, SQLiteDatabase.OPEN_READWRITE);
    }
    public void closeDataBase()
    {
        if(myBase != null)
            myBase.close();
    }

    public ArrayList<String> getListOfCategories()
    {
        String query = "SELECT DISTINCT  kategoria FROM DANE";
        return getList(query);
    }

    public String getText(String game)
    {
        String query = "SELECT tekst FROM DANE WHERE zabawa = '" + game + "'";
        ArrayList<String> aList = getList(query);
        if(aList.isEmpty())
            return "Error 73";
        return aList.get(0);
    }

    public String getRandomGame(int seed)
    {
        int counted = count();

        Random rand = new Random();
        rand.setSeed(seed);
        int randNumber = rand.nextInt();
        randNumber = ((randNumber % counted) + counted) % counted;
        String query = "SELECT zabawa FROM DANE WHERE _id = " + Integer.toString(randNumber + 1);
        ArrayList<String> aList = getList(query);
        if(aList.isEmpty())
            return "Error 73";
        return aList.get(0);
    }

    public ArrayList<String> getGamesInCategory(String category)
    {
        String query;
        if(category.equals("all"))
            query = "SELECT zabawa FROM DANE WHERE kategoria <> 'zz'";
        else
            query = "SELECT zabawa FROM DANE WHERE kategoria = '" + category + "'";
        return getList(query);
    }

    public ArrayList<String> getListOfFavorites()
    {
        String query = "SELECT name FROM FAVORITESLIST";
        return getList(query);
    }

    public ArrayList<String> getGameInFavorite(String favorite)
    {
        String query = "SELECT game FROM FAVORITES WHERE name = '" + favorite + "' ORDER BY number";
        return getList(query);
    }

    private ArrayList<String> getList(String query)
    {
        ArrayList<String> gamesList = new ArrayList<>();

        openDataBase();

        Cursor cursor = myBase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            String tmp = cursor.getString(0);
            gamesList.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        closeDataBase();
        return gamesList;
    }

    public ArrayList<String> find(String toFind, boolean name, boolean text)
    {
        String query = "SELECT zabawa FROM DANE WHERE ";
        if(name) 
        {
            query = query + "zabawa Like '%" + toFind + "%'";
            if (text)  
                query = query + " OR ";
        }
        if(text)
            query = query + "tekst Like '%" + toFind + "%'";
        return getList(query);
    }

    private int count()
    {
        String query = "SELECT count(*) FROM DANE";
        openDataBase();

        Cursor cursor = myBase.rawQuery(query, null);
        cursor.moveToFirst();

        int counted = cursor.getInt(0);

        return counted;
    }







    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
