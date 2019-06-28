package com.example.pc.pogodne;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;

import javax.sql.StatementEvent;

public class AddedGamesService extends SQLiteOpenHelper
{

    private static String dataBaseName = "added";

    private final Context appContext;
    public static String dataBasePath = "data/data/com.example.pc.pogodne/databases";
    public static final int dataBAseVersion = 1;

    AddedGamesService(Context context)
    {
        super(context, dataBaseName, null, dataBAseVersion);
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String creareTable = "CREATE TABLE " + dataBaseName + "(category TEXT, game TEXT, text TXT)";
        db.execSQL(creareTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + dataBaseName);
        onCreate(db);
    }

    public long addGame(String category, String game, String text)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("category", category);
        contentValues.put("game", game);
        contentValues.put("text", text);

        long result = db.insert(dataBaseName, null, contentValues);
        db.close();

        return result;
    }

    private Cursor getCursor()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT category, game, text  FROM " + dataBaseName;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public ArrayList<ArrayList<String> > getList ()
    {
        ArrayList<ArrayList<String> >list = new ArrayList<>();
        Cursor data = getCursor();

        while (data.moveToNext())
        {
            String category = data.getString(0);
            String game = data.getString(1);
            String text = data.getString(2);
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(category);
            tmp.add(game);
            tmp.add(text);
            list.add(tmp);
        }

        return list;
    }

    public boolean gameExist(String game)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT game FROM " + dataBaseName;
        Cursor cursor = db.rawQuery(query, null);
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
}
