package com.domdev.pc.pogodne;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceLastUpdate;
    DatabaseReference databaseReferenceGames;

    ValueEventListener lastUpdateEventListener;

    ChildEventListener gamesEventListener;
    boolean gamesEventListenerAdded = false;

    SettingsService settingsService;

    String lastUpdate;

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
        firebaseDatabase = FirebaseDatabase.getInstance();

        DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
        settingsService = new SettingsService(getApplicationContext());
        if( !dbHelper.ifDatabaseCreated() )
        {
            settingsService.databaseCreated();
            dbHelper.getReadableDatabase();
            //copy db
            if(copyDatabase(this))
            {
                //all right

                DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                AddedGamesService addedGamesService = new AddedGamesService(getApplicationContext());
                ArrayList<Game> list = addedGamesService.getList();
                for ( int i = 0; i < list.size(); ++i )
                {
                    if(!dataBaseHelper.gameExist(list.get(i).zabawa))
                        dataBaseHelper.addGameOrUpdate( list.get(i) );
                }
            }
        }

        //dodanie listenerów do bazy danych
        addDatabaseListeners();

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
            String outFileName = dataBaseHelper.dataBasePath + "/" + DataBaseHelper.dataBaseName;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length;
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

    void addDatabaseListeners()
    {
        databaseReferenceLastUpdate = firebaseDatabase.getReference("lastUpdate");

        gamesEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
                try {
                    Game game = dataSnapshot.getValue(Game.class);

                    DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                    if(game != null)
                    {
                        if(game.kategoria.equals("removed"))
                        {
                            dataBaseHelper.remove(game);
                        }
                        else if (!game.lastUpdate.equals(dataBaseHelper.getLastUpdate(game)))
                            dataBaseHelper.addGameOrUpdate(game);
                    }
                }
                catch (Exception e)
                {
                    settingsService.setLastDatabaseUpdate(settingsService.getPrevLastDatabaseUpdateFromSettings());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // TODO zostawić puste
                System.out.println("==================onChildChanged");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                System.out.println("==================onChildRemoved");
//                            Game game = snapshot.getValue(Game.class);
//
//                            DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
//                            if (game != null && dataBaseHelper.gameExist(game.zabawa))
//                                dataBaseHelper.remove(game.zabawa);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("==================onChildMoved");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                settingsService.setLastDatabaseUpdate(settingsService.getPrevLastDatabaseUpdateFromSettings());
                Toast.makeText(getApplicationContext(), "Nie udało się połączyć z bazą danych", Toast.LENGTH_LONG).show();
            }
        };

        lastUpdateEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lastUpdate = dataSnapshot.getValue(String.class);

                databaseReferenceGames = firebaseDatabase.getReference("games");

                if(!lastUpdate.equals(settingsService.getLastDatabaseUpdateFromSettings())) {
                    settingsService.setLastDatabaseUpdate(lastUpdate);
                    databaseReferenceGames.addChildEventListener(gamesEventListener);
                    gamesEventListenerAdded = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Nie udało się połączyć z bazą danych", Toast.LENGTH_LONG).show();
            }
        };

        databaseReferenceLastUpdate.addListenerForSingleValueEvent(lastUpdateEventListener);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        databaseReferenceLastUpdate.removeEventListener(lastUpdateEventListener);
        if(gamesEventListenerAdded)
            databaseReferenceGames.removeEventListener(gamesEventListener);
    }
}


