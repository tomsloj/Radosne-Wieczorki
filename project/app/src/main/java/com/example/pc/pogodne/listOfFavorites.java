package com.example.pc.pogodne;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class listOfFavorites extends AppCompatActivity {

    Toolbar myToolbar;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_favorites);

        myToolbar = (Toolbar) findViewById(R.id.main_bar);
        myToolbar.setTitle("Ulubione");
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        //actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        //actionBar.setDisplayShowHomeEnabled(true);

        /*
        String filename = "settingsFile";
        final File file = new File(this.getFilesDir(), filename);
        int textSize = 15;

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textOfFile = new String(buffer);
            textSize = Integer.parseInt(textOfFile);
        }
        catch (IOException e)
        {
            Toast.makeText(listOfFavorites.this, "Error29",Toast.LENGTH_LONG).show();
        }
        */
        final SettingsService sService = new SettingsService(getApplicationContext());
        textSize = sService.getSize();

        final ListView listOfFavorites = (ListView) findViewById(R.id.listOfFavorites);
        final File favoritesFile = new File(listOfFavorites.this.getFilesDir(), "ulu");

        DataBaseFavorites dbHelperFavorites = new DataBaseFavorites(listOfFavorites.this);

        ArrayList<String> listFavorites = dbHelperFavorites.getFavoritesList();

        final ArrayList<String> list = listFavorites;

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(listOfFavorites.this,android.R.layout.simple_list_item_1, list)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(textSize);

                return view;
            }
        };

        listOfFavorites.setAdapter(arrayAdapter);

        listOfFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String favoriteName = list.get(i);

                    Intent otworz_liste = new Intent(getApplicationContext(), displayFavorite.class);
                    otworz_liste.putExtra("ulu", favoriteName);
                    otworz_liste.putExtra("ID", i);
                    otworz_liste.putExtra("list", list);
                    startActivity(otworz_liste);

            }
        });


    }

    protected void onResume()
    {
        super.onResume();

    }


    /*
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_list_of_favorites);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        myToolbar.setTitle("Ulubione");
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        //actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        //actionBar.setDisplayShowHomeEnabled(true);

        String filename = "settingsFile";
        final File file = new File(this.getFilesDir(), filename);
        int textSize = 15;

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textOfFile = new String(buffer);
            textSize = Integer.parseInt(textOfFile);
        }
        catch (IOException e)
        {
            Toast.makeText(listOfFavorites.this, "Error29",Toast.LENGTH_LONG).show();
        }

        final int tSize = textSize;

        final ListView listOfFavorites = (ListView) findViewById(R.id.listOfFavorites);
        final File favoritesFile = new File(listOfFavorites.this.getFilesDir(), "ulu");

        ArrayList<String> listFavorites = FileHelper.listOfFavorites(favoritesFile);

        final ArrayList<String> list = listFavorites;

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(listOfFavorites.this,android.R.layout.simple_list_item_1, list)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(tSize);

                return view;
            }
        };

        listOfFavorites.setAdapter(arrayAdapter);

        listOfFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nazwaulu = list.get(i);

                Intent openEditFavorites = new Intent(getApplicationContext(), editFavorites.class);
                openEditFavorites.putExtra("ulu", nazwaulu);
                startActivity(openEditFavorites);

            }
        });


    }
    */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_search)
        {
            Intent openSearch = new Intent(getApplicationContext(), search.class);
            startActivity(openSearch);
        }
        else
        if(id == R.id.action_settings)
        {
            Intent openSettings = new Intent(getApplicationContext(), settings.class);
            startActivity(openSettings);
        }

        return super.onOptionsItemSelected(item);
    }
}
