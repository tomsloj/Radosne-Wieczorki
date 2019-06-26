package com.example.pc.pogodne;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

public class listOfFavorites extends AppCompatActivity
{
    int textSize;
    ArrayList<String> listFavorites;
    SwipeMenuListView listOfFavorites;

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

        listOfFavorites = (SwipeMenuListView) findViewById(R.id.listOfFavorites);
        //get list of names of favorites from favorites base
        final DataBaseFavorites dbHelperFavorites = new DataBaseFavorites(listOfFavorites.this);
        listFavorites = dbHelperFavorites.getFavoritesList();

        //display list of favorites
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(listOfFavorites.this,android.R.layout.simple_list_item_1, listFavorites)
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
        listOfFavorites.setAdapter(arrayAdapter);
        if(listFavorites.size() == 0)
            listOfFavorites.setEmptyView(findViewById(R.id.emptyListText));

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



        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(getResources().getColor(R.color.iconbackground)));
                // set item width
                openItem.setWidth(150);

                openItem.setIcon(R.drawable.edit);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.iconbackground)));
                // set item width
                deleteItem.setWidth(150);
                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

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


        final ImageButton wholeListButton = (ImageButton) findViewById(R.id.wholeListButton);
        final ImageButton searchButton = (ImageButton) findViewById(R.id.findButton);
        final ImageButton favoritesButton = (ImageButton) findViewById(R.id.favoritesButton);

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

        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(listOfFavorites.this);
                final LayoutInflater inflater = listOfFavorites.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.create_new_favorite, null);
                builder.setView(dialogView);
                final AlertDialog dialog2 = builder.create();
                dialog2.show();
                Button createButton = (Button) dialogView.findViewById(R.id.newGameButton);
                Button cancelButton = (Button) dialogView.findViewById(R.id.cancel);
                final TextView name = (TextView) dialogView.findViewById(R.id.newListName);

                createButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataBaseFavorites dbFavoritesHelper = new DataBaseFavorites(getApplicationContext());

                        String nameOfFavorite = name.getText().toString();
                        //list can't be empty, and can't contain %<>@#$|
                        if(nameOfFavorite.isEmpty() || nameOfFavorite.replace(" ", "").isEmpty())
                        {
                            Toast.makeText(listOfFavorites.this, "nazwa nie może być pusta",Toast.LENGTH_LONG).show();
                        }
                        else
                        if(nameOfFavorite.contains("%") || nameOfFavorite.contains(">") ||
                                nameOfFavorite.contains("@") || nameOfFavorite.contains("<") ||
                                nameOfFavorite.contains("#") || nameOfFavorite.contains("|") ||
                                nameOfFavorite.contains("$") || nameOfFavorite.contains("'") )
                        {
                            Toast.makeText(listOfFavorites.this, "nazwa nie może zawierać:\n%<>@#$|'",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if(dbFavoritesHelper.favoriteExist(nameOfFavorite))
                                Toast.makeText(listOfFavorites.this, "taka nazwa listy ulubionych już istnieje",Toast.LENGTH_SHORT).show();
                            else
                            {
                                //create new list of favorites
                                dbFavoritesHelper.createFavorites(nameOfFavorite, null);

                                final AlertDialog.Builder builder = new AlertDialog.Builder(listOfFavorites.this);
                                final LayoutInflater inflater = listOfFavorites.this.getLayoutInflater();
                                final View dialogView = inflater.inflate(R.layout.creation_finished, null);
                                builder.setView(dialogView);
                                final AlertDialog dialog3 = builder.create();
                                dialog3.show();
                                final Button finish = (Button) dialogView.findViewById(R.id.finish);
                                Button back = (Button) dialogView.findViewById(R.id.back);

                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });

                                finish.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog3.dismiss();
                                        dialog2.dismiss();
                                    }
                                });



                                Toast.makeText(listOfFavorites.this, "nowa lista ulubionych została utworzona",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });


            }
        });

    }

    protected void onResume()
    {
        super.onResume();

        boolean toChange = false;
        DataBaseFavorites dbFavorite = new DataBaseFavorites(getApplicationContext());
        ArrayList<String> favoritesList = dbFavorite.getFavoritesList();
        final SettingsService sService = new SettingsService(getApplicationContext());
        final int currentTextSize = sService.getTextSize();
        final String newName = sService.getNewNameOfFavorite();

        //if we can other number of favorites we had to change list
        //or we changed name of one favorite
        if(listFavorites.size() != favoritesList.size() || !newName.equals(""))
        {
            listFavorites = favoritesList;
            toChange = true;
        }

        //if is changed: text size, number of favorites or name of one favorite display new list
        if(currentTextSize != textSize  || toChange)
        {
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
        }
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