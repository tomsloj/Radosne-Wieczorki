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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
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


        DataBaseFavorites dbFavorites = new DataBaseFavorites(displayFavorite.this);

        final ArrayList<String> favoriteList = getIntent().getStringArrayListExtra("list");

        listSize = favoriteList.size();

        list = dbFavorites.getGamesInFavorite(nameOfFavorite);

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(displayFavorite.this,android.R.layout.simple_list_item_1, list)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setBackground(getResources().getDrawable( R.drawable.list_background ));
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
                // create "add" item
                SwipeMenuItem addItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                addItem.setBackground(new ColorDrawable(getResources().getColor(R.color.iconbackground)));
                // set item width
                addItem.setWidth(150);
                // set item title
                addItem.setIcon(R.drawable.add);
                // add to menu
                menu.addMenuItem(addItem);
            }
        };


        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

                final String game = favoriteList.get(position);

                final AlertDialog.Builder builder = new AlertDialog.Builder(displayFavorite.this);
                final LayoutInflater inflater = displayFavorite.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_to_favorites, null);

                //final Button stworz = (Button) dialogView.findViewById(R.id.createFavorite);
                //final EditText nazwa =(EditText) dialogView.findViewById(R.id.nameOfFavorite);
                final ExpandableListView listaulu = (ExpandableListView) dialogView.findViewById(R.id.dialogLisOfFavorites);

                final DataBaseFavorites dbFavoritesHelper = new DataBaseFavorites(displayFavorite.this);

                final ArrayList<String> list = dbFavoritesHelper.getFavoritesList();
                final ArrayAdapter arrayAdapter = new ArrayAdapter<>(displayFavorite.this,android.R.layout.simple_list_item_1, list);


                builder.setView(dialogView);
                final AlertDialog dialog1 = builder.create();
                dialog1.show();

                Button positiveButton = dialog1.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativButton = dialog1.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativButton.setTextColor( getResources().getColor( R.color.colorPrimary) );
                positiveButton.setTextColor( getResources().getColor( R.color.colorPrimary) );

                //display info that user hasn't any list of favorites
                //final TextView text = (TextView) dialog.findViewById(R.id.textChooseExistingFavorite);
                    /*
                    if(list.size() == 0)
                    {
                        text.setText("Nie posiadasz jeszcze żadnej listy ulubionych");
                    }
                    */

                Button createButton = (Button) dialogView.findViewById(R.id.create);
                Button addGameButton = (Button) dialogView.findViewById(R.id.addGameButton);
                Button cancelButton = (Button) dialogView.findViewById(R.id.cancel);
                final ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(getApplicationContext(), list.get(0), list, listaulu);

                if(list.isEmpty()) {
                    listaulu.setVisibility(View.GONE);
                    addGameButton.setVisibility(View.INVISIBLE);
                }
                else
                {
                    listaulu.setAdapter(adapter);
                }



                createButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(displayFavorite.this);
                        final LayoutInflater inflater = displayFavorite.this.getLayoutInflater();
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
                                String nameOfFavorite = name.getText().toString();
                                //list can't be empty, and can't contain %<>@#$|
                                if(nameOfFavorite.isEmpty() || nameOfFavorite.replace(" ", "").isEmpty())
                                {
                                    Toast.makeText(displayFavorite.this, "nazwa nie może być pusta",Toast.LENGTH_LONG).show();
                                }
                                else
                                if(nameOfFavorite.contains("%") || nameOfFavorite.contains(">") ||
                                        nameOfFavorite.contains("@") || nameOfFavorite.contains("<") ||
                                        nameOfFavorite.contains("#") || nameOfFavorite.contains("|") ||
                                        nameOfFavorite.contains("$") || nameOfFavorite.contains("'") )
                                {
                                    Toast.makeText(displayFavorite.this, "nazwa nie może zawierać:\n%<>@#$|'",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    if(dbFavoritesHelper.favoriteExist(nameOfFavorite))
                                        Toast.makeText(displayFavorite.this, "taka nazwa listy ulubionych już istnieje",Toast.LENGTH_SHORT).show();
                                    else
                                    {
                                        //create new list of favorites
                                        dbFavoritesHelper.createFavorites(nameOfFavorite, game);

                                        final AlertDialog.Builder builder = new AlertDialog.Builder(displayFavorite.this);
                                        final LayoutInflater inflater = displayFavorite.this.getLayoutInflater();
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
                                                dialog1.dismiss();
                                            }
                                        });



                                        //Toast.makeText(displayFavorite.this, "nowa lista ulubionych została utworzona",Toast.LENGTH_SHORT).show();
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
                addGameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String favoritesName = adapter.getGroup(0).toString();

                        if(dbFavoritesHelper.gameInFavoriteExists(favoritesName, game))
                            Toast.makeText(displayFavorite.this, "ta zabawa już znajduje sie na tej liście ulubionych",Toast.LENGTH_SHORT).show();
                        else
                        {
                            dbFavoritesHelper.addGametoFavorite(favoritesName, game);
                            Toast.makeText(displayFavorite.this, "zabawa została dodana",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });

                return false;
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
                        tv.setBackground(getResources().getDrawable( R.drawable.list_background ));
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
