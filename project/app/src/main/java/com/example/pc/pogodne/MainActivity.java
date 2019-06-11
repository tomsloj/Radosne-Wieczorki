package com.example.pc.pogodne;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.Calendar;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set toolbar
        //toolbar with title
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        //copy database
        DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
        SettingsService settingsService = new SettingsService(getApplicationContext());
        String version = settingsService.getCurrentVersion();
        //update only when new version was installed
        if(!version.equals(settingsService.getPrevVersion()))
        {
            settingsService.setPrevVersion(version);
            dbHelper.getReadableDatabase();
            //copy db
            if(copyDatabase(this)) {
                //all right
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //define buttons
        final Button wholeListButton = (Button) findViewById(R.id.wholeListButton);
        final Button dancesButton = (Button) findViewById(R.id.dancesButton);
        final Button singsButton = (Button) findViewById(R.id.singsButton);
        final Button competitionButton = (Button) findViewById(R.id.competitionsButton);
        final Button integralsButton = (Button) findViewById(R.id.integralsButton);
        final Button otherButtons = (Button) findViewById(R.id.othersButton);

        final Button settingsButton = (Button) findViewById(R.id.settingsButton);
        final Button searchButton = (Button) findViewById(R.id.findButton);
        final Button favoritesButton = (Button) findViewById(R.id.favoritesButton);
        final Button gameOfTheDayButton = (Button) findViewById(R.id.gameOfTheDayButton);

        /*
         * set what buttons do
         */

        //show the whole list
        wholeListButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "all");
                startActivity(openList);
            }
        });

        //show games in category "tańce"
        dancesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "tańce");
                startActivity(openList);
            }
        });

        //show games in category "piosenki"
        singsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "piosenki");
                startActivity(openList);
            }
        });

        //show games in category "rywalizacja"
        competitionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "rywalizacja");
                startActivity(openList);
            }
        });

        //display games in category "integracyjne"
        integralsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "integracyjne");
                startActivity(openList);
            }
        });

        //show games in category "inne"
        otherButtons.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "inne");
                startActivity(openList);
            }
        });

        //show search engine
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openSearch = new Intent(getApplicationContext(), search.class);
                startActivity(openSearch);
            }
        });

        //show settings
        settingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openSettings = new Intent(getApplicationContext(), settings.class);
                startActivity(openSettings);
            }
        });

        //show favorites list
        favoritesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openListOfFavorites = new Intent(getApplicationContext(), listOfFavorites.class);
                startActivity(openListOfFavorites);
            }
        });

        //show random play in current day
        gameOfTheDayButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                int seed = Calendar.getInstance().get(Calendar.YEAR)*366+Calendar.getInstance().get(Calendar.MONTH)*31+Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                DataBaseHelper dbHelper = new DataBaseHelper(MainActivity.this);
                String gameName = dbHelper.getRandomGame(seed);

                Intent openGameOfTheDay = new Intent(getApplicationContext(), display.class);
                openGameOfTheDay.putExtra("zabawa", gameName);
                startActivity(openGameOfTheDay);
            }
        });
    }

    /*
     * copy database from assets to internal storage
     * @return true if copying was successful
     */
    private boolean copyDatabase(Context context)
    {
        try
        {
            InputStream inputStream = context.getAssets().open(DataBaseHelper.dataBaseName);
            String outFileName = DataBaseHelper.dataBasePath + DataBaseHelper.dataBaseName;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0)
            {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("listActivity","DB copied");
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}


