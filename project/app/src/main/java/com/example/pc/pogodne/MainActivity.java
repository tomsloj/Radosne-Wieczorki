package com.example.pc.pogodne;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.Calendar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //setting toolbar
        //toolbar with title
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);


        //copy database
        DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());

        File database = getApplicationContext().getDatabasePath(DataBaseHelper.dataBaseName);


        if(!database.exists())
        {
            dbHelper.getReadableDatabase();
            //copy db
            if(copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else
        {
            Toast.makeText(this, "oooo", Toast.LENGTH_SHORT).show();
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

        //setting what buttons do

        //show the whole list
        wholeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "all");
                startActivity(openList);

            }
        });

        //show games in category "tańce"
        dancesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "tańce");
                startActivity(openList);

            }
        });

        //show games in category "piosenki"
        singsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "piosenki");
                startActivity(openList);

            }
        });

        //show games in category "rywalizacja"
        competitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "rywalizacja");
                startActivity(openList);

            }
        });

        //display games in category "integracyjne"
        integralsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "integracyjne");
                startActivity(openList);

            }
        });

        //show games in category "inne"
        otherButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "inne");
                startActivity(openList);

            }
        });

        //show searching
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSearch = new Intent(getApplicationContext(), search.class);
                startActivity(openSearch);
            }
        });

        //show settings
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSettings = new Intent(getApplicationContext(), settings.class);
                startActivity(openSettings);
            }
        });

        //show favorites list
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openListOfFavorites = new Intent(getApplicationContext(), listOfFavorites.class);

                startActivity(openListOfFavorites);

            }
        });

        //show random play in current day
        gameOfTheDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGameOfTheDay = new Intent(getApplicationContext(), display.class);
                int seed = Calendar.getInstance().get(Calendar.YEAR)*366+Calendar.getInstance().get(Calendar.MONTH)*31+Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                DataBaseHelper dbHelper = new DataBaseHelper(MainActivity.this);

                String gameName = dbHelper.getRandomGame(seed);

                openGameOfTheDay.putExtra("zabawa", gameName);
                startActivity(openGameOfTheDay);

            }
        });


        //if settingsFile doesn't exist, set the default text size at 15sp
        //and write it to settingFile
        String filename = "settingsFile";
        File file = new File(this.getFilesDir(), filename);

        if (!file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = "15".getBytes();
                fos.write(buffer);
                fos.close();
            } catch (IOException e) {
                Toast.makeText(this, "Error4", Toast.LENGTH_LONG).show();
            }
        }

    }

    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DataBaseHelper.dataBaseName);
            String outFileName = DataBaseHelper.dataBasePath + DataBaseHelper.dataBaseName;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("listActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


