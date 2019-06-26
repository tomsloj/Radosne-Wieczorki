package com.example.pc.pogodne;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.shuhart.hoveringcallback.HoverItemDecoration;
import com.shuhart.hoveringcallback.HoveringCallback;

import java.util.ArrayList;

public class displayFavorite extends AppCompatActivity {

    String nameOfFavorite = "";
    Toolbar myToolbar;
    SwipeMenuListView listView;
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


        listView = (SwipeMenuListView)findViewById(R.id.listOfGames);

        final Button editButton = (Button) findViewById(R.id.editButton);

        DataBaseFavorites dbFavorites = new DataBaseFavorites(displayFavorite.this);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGame = new Intent(getApplicationContext(), editFavorites.class);
                openGame.putExtra("ulu", nameOfFavorite);
                startActivity(openGame);
            }
        });

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
                tv.setBackgroundColor(getResources().getColor( R.color.background ));
                tv.setTextSize(textSize);
                tv.setTextColor(Color.BLACK);

                return view;
            }
        };
        listView.setAdapter(arrayAdapter);
        if(list.size() == 0)
            listView.setEmptyView(findViewById(R.id.emptyListText));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String gameName = list.get(i);
                Intent openGame = new Intent(getApplicationContext(), display.class);
                openGame.putExtra("zabawa", gameName);
                openGame.putExtra("playlist", nameOfFavorite );
                openGame.putExtra("kategoria", "ulu");
                startActivity(openGame);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(150);
                // set item title
                openItem.setIcon(R.drawable.plus);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(150);
                // set a icon
                deleteItem.setIcon(R.drawable.minus);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        /*
        // set creator
        listOfFavorites.setMenuCreator(creator);

        listOfFavorites.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        String favoriteName = listFavorites.get(position);

                        Intent open_list = new Intent(getApplicationContext(), displayFavorite.class);
                        open_list.putExtra("ulu", favoriteName);
                        open_list.putExtra("ID", position);
                        open_list.putExtra("list", listFavorites);
                        sService.setNewNameOfFavorite("");
                        startActivity(open_list);
                        break;
                    case 1:
                        // delete
                        AlertDialog.Builder builder = new AlertDialog.Builder(listOfFavorites.this);
                        builder.setTitle("Czy na pewno chcesz usunąć tę listę?");
                        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelperFavorites.deleteFavorite(listFavorites.get(position));
                                listFavorites.remove(position);
                                Toast.makeText(getApplicationContext(),listFavorites.get(position),Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                        Button negativButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        negativButton.setTextColor( getResources().getColor( R.color.colorPrimary) );
                        positiveButton.setTextColor( getResources().getColor( R.color.colorPrimary) );
                        break;
                }
                return false;
            }
        });
        */
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        final SettingsService sService = new SettingsService(getApplicationContext());
        final int currentTextSize = sService.getTextSize();

        DataBaseFavorites dbFavorite = new DataBaseFavorites(displayFavorite.this);

        ArrayList<String> favoriteList = dbFavorite.getFavoritesList();

        if(listSize != favoriteList.size())
        {
            finish();
        }
        else
        {
            boolean toChange = false;
            String newNameOfFavorite = sService.getNewNameOfFavorite();
            if( !nameOfFavorite.equals(newNameOfFavorite) && !newNameOfFavorite.equals("") )
            {
                nameOfFavorite = newNameOfFavorite;
                toChange = true;
            }

            ArrayList<String> gamesList = dbFavorite.getGamesInFavorite(nameOfFavorite);
            if(list.size() != gamesList.size()) {
                list = gamesList;
                toChange = true;
            }

            myToolbar.setTitle(nameOfFavorite);
            if(currentTextSize != textSize || toChange)
            {
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(displayFavorite.this, android.R.layout.simple_list_item_1, list) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        tv.setBackgroundColor(getResources().getColor( R.color.background ));
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
