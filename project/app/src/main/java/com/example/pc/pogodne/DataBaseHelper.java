package com.example.pc.pogodne;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void addGame(String category, String game, String text)
    {
        String query = "INSERT INTO DANE (zabawa, kategoria, tekst) VALUES ('" + game + "','"+
                category + "','" + text + "')";
        openDataBase();
        myBase.execSQL(query);
        closeDataBase();

        /*
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("zabawa", game);
        contentValues.put("kategoria", category);
        contentValues.put("tekst", text);

        long result = db.insert(dataBaseName, null, contentValues);
        db.close();
        return result;
        */

    }

    public String getRandomGame(int seed)
    {
        int counted = count();

        Random rand = new Random();
        rand.setSeed(seed);
        int randNumber = rand.nextInt();
        randNumber = ((randNumber % counted) + counted) % counted;
        ArrayList<String> aList = new ArrayList<String>();
        while( aList.isEmpty() )
        {
            String query = "SELECT zabawa FROM DANE WHERE _id = " + Integer.toString(randNumber + 1) + " AND kategoria <> 'zz'";
            aList = getList(query);
            randNumber = ((randNumber % counted) + counted) % counted;
        }
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
        query = query + " ORDER BY zabawa COLLATE LOCALIZED ASC";
        return getList(query);
    }

    public String nextGame (String category, String game)
    {
        ArrayList<String> list = getGamesInCategory(category);
        for( int i = 0; i < list.size(); ++i )
        {
            if(game.equals(list.get(i)))
            {
                if( i < list.size()-1 )
                {
                    return list.get(i+1);
                }
                else
                    return "";
            }
        }
        return "";
    }

    public String prevGame (String category, String game)
    {
        ArrayList<String> list = getGamesInCategory(category);
        for( int i = 0; i < list.size(); ++i )
        {
            if(game.equals(list.get(i)))
            {
                if( i == 0 )
                {
                    return "";
                }
                else
                    return list.get(i-1);
            }
        }
        return "";
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

    boolean gameExist(String game)
    {
        String query = "SELECT zabawa FROM DANE";
        openDataBase();

        Cursor cursor = myBase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            String tmp = cursor.getString(0);
            if(tmp.toLowerCase().equals(game.toLowerCase()))
            {
                cursor.close();
                return true;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return false;
    }

    public String getCategory(String game)
    {
        String query = "SELECT kategoria FROM DANE WHERE zabawa = '" + game + "'";
        ArrayList<String> aList = getList(query);
        if(aList.isEmpty())
            return "";
        return aList.get(0);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }


}
