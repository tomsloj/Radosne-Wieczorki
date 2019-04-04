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
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class listOfFavorites extends AppCompatActivity
{
    int textSize;
    ArrayList<String> listFavorites;
    ListView listOfFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_favorites);

        //set toolbar with back, search and settings buttons
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        myToolbar.setTitle("Ulubione");
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);

        final SettingsService sService = new SettingsService(getApplicationContext());
        textSize = sService.getTextSize();

        listOfFavorites = (ListView) findViewById(R.id.listOfFavorites);

        //get list of names of favorites from favorites base
        DataBaseFavorites dbHelperFavorites = new DataBaseFavorites(listOfFavorites.this);
        listFavorites = dbHelperFavorites.getFavoritesList();

        //display list of favorites
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(listOfFavorites.this,android.R.layout.simple_list_item_1, listFavorites)
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
        listOfFavorites.setAdapter(arrayAdapter);

        /*
         * list choosing
         * send to displayFavorites Activity:
         * ulu  -   name of choosen favorites list
         * ID   -   ID of favorite list
         * list -   list of all favorites names
         */
        listOfFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                String favoriteName = listFavorites.get(i);

                Intent open_list = new Intent(getApplicationContext(), displayFavorite.class);
                open_list.putExtra("ulu", favoriteName);
                open_list.putExtra("ID", i);
                open_list.putExtra("list", listFavorites);
                sService.setNewNameOfFavorite("");
                startActivity(open_list);
            }
        });
    }

    protected void onResume()
    {
        super.onResume();

        boolean toChange = false;
        DataBaseFavorites dbFavorite = new DataBaseFavorites(getApplicationContext());
        ArrayList<String> favoritesList = dbFavorite.getFavoritesList();
        //if we can other number of favorites we had to change list
        //TODO when we change only names of favorites without removing it doesn't change list, but it should (FIXED but it's not optimal)
        //if(listFavorites.size() != favoritesList.size())
        //{
            listFavorites = favoritesList;
        //   toChange = true;
        //}

        final SettingsService sService = new SettingsService(getApplicationContext());
        final int currentTextSize = sService.getTextSize();
        //if size of text is changed of number of favorites is changed create new list
        //if(currentTextSize != textSize  || toChange)
        //{
            final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(listOfFavorites.this,android.R.layout.simple_list_item_1, listFavorites)
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent)
                {
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    tv.setTextSize(currentTextSize);
                    tv.setTextColor(Color.BLACK);

                    return view;
                }
            };
            listOfFavorites.setAdapter(arrayAdapter);
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_search) //open search activity
        {
            Intent openSearch = new Intent(getApplicationContext(), search.class);
            startActivity(openSearch);
        }
        else
        if(id == R.id.action_settings) // open settings activity
        {
            Intent openSettings = new Intent(getApplicationContext(), settings.class);
            startActivity(openSettings);
        }

        return super.onOptionsItemSelected(item);
    }
}
