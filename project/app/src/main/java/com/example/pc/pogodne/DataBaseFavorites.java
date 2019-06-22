package com.example.pc.pogodne;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseFavorites extends SQLiteOpenHelper
{
    private static String dataBaseName = "favorites";

    private final Context appContext;
    public static String dataBasePath = "data/data/com.example.pc.pogodne/databases";
    public static final int dataBAseVersion = 1;

    public DataBaseFavorites(Context context)
    {
        super(context, dataBaseName, null, dataBAseVersion);
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String creareTable = "CREATE TABLE " + dataBaseName + "(name TEXT, game TEXT, number INT, notes TEXT)";
        db.execSQL(creareTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + dataBaseName);
        onCreate(db);
    }

    //@return max number of favorite games(how many games is in favorite)
    public int maxNumber(String name)
    {
        int number;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT MAX(number) FROM " + dataBaseName + " WHERE name = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        number = cursor.getInt(0);
        cursor.close();
        return number;
    }

    //add new game to favorite list
    public boolean addGametoFavorite(String name, String game)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int number;
        if (game.equals("remove"))
            number = 0;
        else
            number = maxNumber(name) + 1;

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("game", game);
        contentValues.put("number", number);

        long result = db.insert(dataBaseName, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    //@return number of game in favorite
    public int numberOfGame(String name, String game)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT number FROM " + dataBaseName + " WHERE name = '" + name
                + "' AND game = '" + game +"'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int number = cursor.getInt(0);
        return number;
    }

    public String gameFromNumber(int number, String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT game FROM " + dataBaseName + " WHERE name = '" + name
                + "' AND number = " + Integer.toString(number);
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String game = cursor.getString(0);
        cursor.close();
        return game;
    }

    //swap game with game which have lower number
    public void upGame(String name, String game)
    {
        int numberOfGame =numberOfGame(name, game);

        if(numberOfGame <= 1)
                return;

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE  " + dataBaseName + " SET number = " + Integer.toString(numberOfGame)
                + " WHERE name = '" + name + "' AND number = " + Integer.toString(numberOfGame - 1);
        db.execSQL(query);
        query = "UPDATE  " + dataBaseName + " SET number = " + Integer.toString(numberOfGame - 1)
                + " WHERE name = '" + name + "' AND game = '" + game +"'";
        db.execSQL(query);
    }

    //swap game with game which have higher number
    public void downGame(String name, String game)
    {
        int numberOfGame =numberOfGame(name, game);
        int maxNumber = maxNumber(name);

        if(numberOfGame >= maxNumber)
            return;

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE  " + dataBaseName + " SET number = " + Integer.toString(numberOfGame)
                + " WHERE name = '" + name + "' AND number = " + Integer.toString(numberOfGame + 1);
        db.execSQL(query);
        query = "UPDATE  " + dataBaseName + " SET number = " + Integer.toString(numberOfGame + 1)
                + " WHERE name = '" + name + "' AND game = '" + game +"'";
        db.execSQL(query);
    }

    //create empty list of favorite
    public void createFavorites(String name, String game)
    {
        addGametoFavorite(name, "remove");
        addGametoFavorite(name, game);
    }

    //check if favorite exists
    public boolean favoriteExist(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT count(*) FROM " + dataBaseName +" WHERE name = '" + name +"'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int counted = cursor.getInt(0);
        cursor.close();

        if(counted == 0)
            return false;
        else
            return true;

    }

    //check if game is in favorite
    public boolean gameInFavoriteExists(String name, String game)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT count(*) FROM " + dataBaseName +" WHERE name = '" + name
                + "' AND game = '" + game + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int counted = cursor.getInt(0);
        cursor.close();

        if(counted == 0)
            return false;
        else
            return true;

    }

    //@return list of games in favorite
    public ArrayList<String> getGamesInFavorite(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT game FROM " + dataBaseName + " WHERE name = '" + name
                + "' ORDER BY number";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> list = new ArrayList<>();

        while (cursor.moveToNext())
            if(!cursor.getString(0).equals("remove"))
                list.add(cursor.getString(0));

        return list;
    }

    //@return cursor to all names of favorites
    private Cursor getNames()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT name FROM " + dataBaseName + " GROUP BY name COLLATE LOCALIZED";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //@return list of favorite lists
    public ArrayList<String> getFavoritesList()
    {
        ArrayList<String> list = new ArrayList<>();
        Cursor data = getNames();

        while (data.moveToNext())
            list.add(data.getString(0));

        return list;
    }


    public void deleteFavorite(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + dataBaseName + " WHERE "
                + "name" + " = '" + name + "'";
        db.execSQL(query);
    }

    public void deleteGame(String name, String game)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int number = numberOfGame(name, game);
        String query = "DELETE FROM " + dataBaseName + " WHERE "
                + "name" + " = '" + name + "' AND game = '" + game +"'";

        db.execSQL(query);
        query = "UPDATE " + dataBaseName + " SET number = number - 1 WHERE "
                + "name" + " = '" + name + "' AND number > " + Integer.toString(number);

        db.execSQL(query);
    }

    public void editNameOfFavorite(String name, String newName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE  " + dataBaseName + " SET name = '" + newName + "' WHERE name = '"
                + name + "'";
        db.execSQL(query);
    }

    public String getNotes( String game, String playlist )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT notes FROM " + dataBaseName + " WHERE name = '"
                + playlist + "' AND game = '" + game + "'";
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        String notes = data.getString(0);
        data.close();
        return notes;
    }

    public void updateNotes( String notes, String game, String playlist )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE  " + dataBaseName + " SET notes = '" + notes + "' WHERE name = '"
                + playlist + "' AND game = '" + game + "'";
        db.execSQL(query);
    }

}
