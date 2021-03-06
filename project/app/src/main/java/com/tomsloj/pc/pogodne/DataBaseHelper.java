package com.tomsloj.pc.pogodne;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.Random;

public class DataBaseHelper  extends SQLiteOpenHelper {
    private SQLiteDatabase myBase;
    static  String dataBaseName = "baza.sqlite";
    private final Context appContext;
    public String dataBasePath ;//= "data/data/com.tomsloj.pc.pogodne/databases/";
    private static final int dataBAseVersion = 1;

    DataBaseHelper(Context context)
    {
        super(context, dataBaseName, null, dataBAseVersion);
        this.appContext = context;
        dataBasePath = context.getFilesDir().getPath();
        //myBase = getWritableDatabase();
    }

   private void openDataBase()
    {
        //String dbPath = appContext.getDatabasePath(dataBaseName).getPath();
        if (myBase != null && myBase.isOpen()) {
            return;
        }
        try
        {
            myBase = SQLiteDatabase.openDatabase(dataBasePath + dataBaseName, null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch (SQLiteCantOpenDatabaseException e)
        {
            Crashlytics.log(Log.ERROR, "opening database", "can;t open database " + dataBasePath);
            Toast.makeText(appContext, "nie można otworzyć bazy danych\nspróbuj uruchomić ponownie aplikację\n" +
                    "jeśli błąd będzie nadal występował skontaktuj się z developerem\n300268@pw.edu.pl", Toast.LENGTH_LONG).show();
        }
    }
    private void closeDataBase()
    {
        if(myBase != null)
            myBase.close();
    }

    ArrayList<String> getListOfCategories()
    {
        String query = "SELECT DISTINCT  kategoria FROM DANE";
        return getList(query);
    }

    String getText(String game)
    {
        String query = "SELECT tekst FROM DANE WHERE zabawa = '" + game + "'";
        ArrayList<String> aList = getList(query);
        if(aList.isEmpty())
            return "Error 73";
        return aList.get(0);
    }

    void addGame(String category, String game, String text)
    {
        String query = "INSERT INTO DANE (zabawa, kategoria, tekst) VALUES ('" + game + "','"+
                category + "','" + text + "')";
        openDataBase();
        myBase.execSQL(query);
        closeDataBase();

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

    ArrayList<String> getGamesInCategory(String category)
    {
        String query;
        if(category.equals("all"))
            query = "SELECT zabawa FROM DANE WHERE kategoria <> 'zz'";
        else
            query = "SELECT zabawa FROM DANE WHERE kategoria = '" + category + "'";
        query = query + " ORDER BY zabawa COLLATE LOCALIZED ASC";
        return getList(query);
    }

    String nextGame (String category, String game)
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

    String prevGame (String category, String game)
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

    ArrayList<String> find(String toFind, boolean name, boolean text)
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

        query = query + " ORDER BY zabawa COLLATE LOCALIZED ASC";
        return getList(query);
    }

    private int count()
    {
        String query = "SELECT count(*) FROM DANE";
        openDataBase();

        Cursor cursor = myBase.rawQuery(query, null);
        cursor.moveToFirst();

        int counted = cursor.getInt(0);

        cursor.close();
        closeDataBase();
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
        closeDataBase();
        return false;
    }

    String getCategory(String game)
    {
        String query = "SELECT kategoria FROM DANE WHERE zabawa = '" + game + "'";
        ArrayList<String> aList = getList(query);
        if(aList.isEmpty())
            return "";
        return aList.get(0);
    }

    void remove (String game)
    {
        //SQLiteDatabase db = this.getWritableDatabase();
        openDataBase();
        String query = "DELETE FROM DANE WHERE "
                + "zabawa" + " = '" + game + "'";
        myBase.execSQL(query);

        closeDataBase();
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
