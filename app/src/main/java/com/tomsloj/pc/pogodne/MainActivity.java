package com.tomsloj.pc.pogodne;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.InputStream;

//TODO filtrowanie zabaw po miejscu (wewnątrz/na zewnątrz), wiek, wielkość grupy
//TODO chwyty gitarowe
//TODO filmiki pokazujące jak powinny wyglądać tańce
//TODO melodie piosenek
//TODO udostępnianie list

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.tomsloj.pc.pogodne.Game;

public class MainActivity extends AppCompatActivity
{

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set toolbar
        //toolbar with title
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        /*
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomMenu);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.button1:

                        break;
                    case R.id.button2:

                        break;
                    case R.id.button3:

                        break;
                }
            }
        });
        */

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
            if(copyDatabase(this))
            {
                //all right

                DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                AddedGamesService addedGamesService = new AddedGamesService(getApplicationContext());
                ArrayList<ArrayList<String> > list = addedGamesService.getList();
                for ( int i = 0; i < list.size(); ++i )
                {
                    if(!dataBaseHelper.gameExist(list.get(i).get(1)))
                        dataBaseHelper.addGame(list.get(i).get(0), list.get(i).get(1), list.get(i).get(2) );
                }
            }
            else
            {
                Toast.makeText(this, "nie można otworzyć bazy danych\nspróbuj uruchomić ponownie aplikację\n" +
                        "jeśli błąd będzie nadal występował skontaktuj się z developerem\n300268@pw.edu.pl", Toast.LENGTH_LONG).show();
                return;
            }
        }

        //define buttons
        final Button dancesButton = (Button) findViewById(R.id.dancesButton);
        final Button singsButton = (Button) findViewById(R.id.singsButton);
        final Button competitionButton = (Button) findViewById(R.id.competitionsButton);
        final Button integralsButton = (Button) findViewById(R.id.integralsButton);
        final Button perceptivityButton = (Button) findViewById(R.id.perceptivityButton);
        final Button efficiencyButton = (Button) findViewById(R.id.efficiencyButton);

        final ImageButton wholeListButton = (ImageButton) findViewById(R.id.wholeListButton);
        final ImageButton searchButton = (ImageButton) findViewById(R.id.findButton);
        final ImageButton favoritesButton = (ImageButton) findViewById(R.id.favoritesButton);


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
        perceptivityButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "refleks");
                startActivity(openList);
            }
        });

        efficiencyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "sprawnościowe");
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

        /*
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
                openGameOfTheDay.putExtra("kategoria", "gameOfTheDay");
                startActivity(openGameOfTheDay);
            }
        });
        */

        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference("0");
        getdata();
    }

    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                Game value = snapshot.getValue(Game.class);

                // after getting the value we are setting
                // our value to our text view in below line.
                Toast.makeText(MainActivity.this, value.zabawa, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
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
            DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
            String outFileName = dataBaseHelper.dataBasePath + DataBaseHelper.dataBaseName;
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
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent otworz_ustawienia = new Intent(getApplicationContext(), settings.class);
            startActivity(otworz_ustawienia);
        }
        return super.onOptionsItemSelected(item);
    }


    //public void forceCrash(View view) {
    //    throw new RuntimeException("This is a crash");
    //}


}

