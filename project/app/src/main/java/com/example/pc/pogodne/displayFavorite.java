package com.example.pc.pogodne;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class displayFavorite extends AppCompatActivity {

    String nameOfFavorite = "";
    Toolbar myToolbar;
    ListView listView;
    int favoriteID = -1;
    int listSize;
    int textSize;
    ArrayList<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_favorite);

        nameOfFavorite = getIntent().getStringExtra("ulu");
        favoriteID = (int) getIntent().getIntExtra("ID", -2);


        myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);

        final SettingsService sService = new SettingsService(getApplicationContext());
        textSize = sService.getTextSize();


        listView = (ListView) findViewById(R.id.listOfGames);
        final Button editButton = (Button) findViewById(R.id.editButton);

        DataBaseFavorites dbFavorites = new DataBaseFavorites(displayFavorite.this);

        ArrayList<String> favoriteList = getIntent().getStringArrayListExtra("list");

        listSize = favoriteList.size();

        list = dbFavorites.getGamesInFavorite(nameOfFavorite);

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(displayFavorite.this,android.R.layout.simple_list_item_1, list)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(textSize);
                tv.setTextColor(Color.BLACK);

                return view;
            }
        };
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String gameName = list.get(i);
                Intent openGame = new Intent(getApplicationContext(), display.class);
                openGame.putExtra("zabawa", gameName);
                startActivity(openGame);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGame = new Intent(getApplicationContext(), editFavorites.class);
                openGame.putExtra("ulu", nameOfFavorite);
                startActivity(openGame);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        final SettingsService sService = new SettingsService(getApplicationContext());
        final int currentTextSize = sService.getTextSize();

        DataBaseFavorites dbFavorite = new DataBaseFavorites(displayFavorite.this);

        ArrayList<String> favoriteList = dbFavorite.getFavoritesList();

        //TODO same like in listoffavorites: what with changed name
        if(listSize != favoriteList.size())
        {
            //Toast.makeText(getApplicationContext(), "nr 1", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            boolean toChange = false;
            ArrayList<String> gamesList = dbFavorite.getGamesInFavorite(nameOfFavorite);
            if(list.size() != gamesList.size()) {
                list = gamesList;
                toChange = true;
                //Toast.makeText(getApplicationContext(), "pppp", Toast.LENGTH_SHORT).show();
            }
                //Toast.makeText(getApplicationContext(), Integer.toString(textSize) + "," + Integer.toString(currentTextSize), Toast.LENGTH_SHORT).show();
            myToolbar.setTitle(favoriteList.get(favoriteID));
            if(currentTextSize != textSize || toChange)
            {
                //Toast.makeText(getApplicationContext(), "nr 3", Toast.LENGTH_SHORT).show();
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(displayFavorite.this, android.R.layout.simple_list_item_1, list) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        tv.setTextSize(currentTextSize);
                        tv.setTextColor(Color.BLACK);

                        return view;
                    }
                };
                listView.setAdapter(arrayAdapter);
            }

        }

    }


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
