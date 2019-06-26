package com.example.pc.pogodne;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import iammert.com.expandablelib.ExpandableLayout;


public class display extends AppCompatActivity {

    int counter = 0;
    int textSize;
    TextView gameName;
    TextView text;
    String game;
    String txt;
    String category;
    String playlist;
    String notes;
    boolean hideFirst = false;
    boolean hideSecond = false;
    boolean hideThird = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);



        final SettingsService sService = new SettingsService(getApplicationContext());
        textSize = sService.getTextSize();

        gameName = (TextView) findViewById(R.id.titleOfGame);
        text = (TextView) findViewById(R.id.textOfGame);

        gameName.setTextSize(textSize + 6);
        text.setTextSize(textSize);

        game = getIntent().getStringExtra("zabawa");
        category = getIntent().getStringExtra("kategoria");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        myToolbar.setTitle(game);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setTitle(game);

        /*
         * display title of game and description
         */
        final DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
        txt = dbHelper.getText(game);

        gameName.setText(game);
        text.setText(txt);


        /*

        //surprise
        final Button hiddenButton = (Button) findViewById(R.id.hiddenButton);
        hiddenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(game.equals("HOP HOP HOP HULA HOP"))
                {
                    counter ++;
                    if(counter > 3)
                    {
                        DataBaseFavorites dataBaseFavorites = new DataBaseFavorites( display.this );
                        if(  !dataBaseFavorites.favoriteExist( "zakazane zabawy" ) )
                        {
                            DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                            ArrayList<String> listOfGames = dataBaseHelper.getGamesInCategory("zz");
                            if( ! listOfGames.isEmpty() )
                            {
                                dataBaseFavorites.createFavorites("zakazane zabawy", listOfGames.get( 0 ) );
                                for( int i = 1 ; i < listOfGames.size() ; ++i )
                                {
                                    dataBaseFavorites.addGametoFavorite("zakazane zabawy", listOfGames.get( i ) );
                                }
                            }

                            Toast.makeText(display.this, "niespodzianka", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        */

        //NOTES BUTTON CAN BE FAVORITE BUTTON
        final ImageButton starButton = (ImageButton) findViewById(R.id.starButton);
        final DataBaseFavorites dbFavorites = new DataBaseFavorites( getApplicationContext() );
        final FloatingActionButton notesButton = (FloatingActionButton)findViewById(R.id.notesButton);

        playlist = getIntent().getStringExtra("playlist");

        if(playlist == null)
        {
            notesButton.hide();
        }

        //favorite

            starButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(display.this);
                    final LayoutInflater inflater = display.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.add_to_favorites, null);

                    //final Button stworz = (Button) dialogView.findViewById(R.id.createFavorite);
                    //final EditText nazwa =(EditText) dialogView.findViewById(R.id.nameOfFavorite);
                    final ExpandableListView listaulu = (ExpandableListView) dialogView.findViewById(R.id.dialogLisOfFavorites);

                    final DataBaseFavorites dbFavoritesHelper = new DataBaseFavorites(display.this);

                    final ArrayList<String> list = dbFavoritesHelper.getFavoritesList();
                    final ArrayAdapter arrayAdapter = new ArrayAdapter<>(display.this,android.R.layout.simple_list_item_1, list);


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
                            final AlertDialog.Builder builder = new AlertDialog.Builder(display.this);
                            final LayoutInflater inflater = display.this.getLayoutInflater();
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
                                        Toast.makeText(display.this, "nazwa nie może być pusta",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    if(nameOfFavorite.contains("%") || nameOfFavorite.contains(">") ||
                                            nameOfFavorite.contains("@") || nameOfFavorite.contains("<") ||
                                            nameOfFavorite.contains("#") || nameOfFavorite.contains("|") ||
                                            nameOfFavorite.contains("$") || nameOfFavorite.contains("'") )
                                    {
                                        Toast.makeText(display.this, "nazwa nie może zawierać:\n%<>@#$|'",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        if(dbFavoritesHelper.favoriteExist(nameOfFavorite))
                                            Toast.makeText(display.this, "taka nazwa listy ulubionych już istnieje",Toast.LENGTH_SHORT).show();
                                        else
                                        {
                                            //create new list of favorites
                                            dbFavoritesHelper.createFavorites(nameOfFavorite, game);

                                            final AlertDialog.Builder builder = new AlertDialog.Builder(display.this);
                                            final LayoutInflater inflater = display.this.getLayoutInflater();
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



                                            Toast.makeText(display.this, "nowa lista ulubionych została utworzona",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(display.this, "ta zabawa już znajduje sie na tej liście ulubionych",Toast.LENGTH_SHORT).show();
                            else
                            {
                                dbFavoritesHelper.addGametoFavorite(favoritesName, game);
                                Toast.makeText(display.this, "zabawa została dodana",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });

                    /*
                    stworz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String nameOfFavorite = nazwa.getText().toString();
                            //list can't be empty, and can't contain %<>@#$|
                            if(nameOfFavorite.isEmpty() || nameOfFavorite.replace(" ", "").isEmpty())
                            {
                                Toast.makeText(display.this, "nazwa nie może być pusta",Toast.LENGTH_LONG).show();
                            }
                            else
                            if(nameOfFavorite.contains("%") || nameOfFavorite.contains(">") ||
                                    nameOfFavorite.contains("@") || nameOfFavorite.contains("<") ||
                                    nameOfFavorite.contains("#") || nameOfFavorite.contains("|") ||
                                    nameOfFavorite.contains("$") || nameOfFavorite.contains("'") )
                            {
                                Toast.makeText(display.this, "nazwa nie może zawierać:\n%<>@#$|'",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                if(dbFavoritesHelper.favoriteExist(nameOfFavorite))
                                    Toast.makeText(display.this, "taka nazwa listy ulubionych już istnieje",Toast.LENGTH_SHORT).show();
                                else
                                {
                                    //create new list of favorites
                                    dbFavoritesHelper.createFavorites(nameOfFavorite, game);

                                    //display new list
                                    arrayAdapter.add(nameOfFavorite);
                                    arrayAdapter.notifyDataSetChanged();

                                    text.setText(R.string.selectFavorite);

                                    Toast.makeText(display.this, "nowa lista ulubionych została utworzona",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    */

                    /*
                    listaulu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String favoritesName = list.get(i);

                            //dbHelper.addToFavorites(favoritesName, zabawa);
                            if(dbFavoritesHelper.gameInFavoriteExists(favoritesName, game))
                                Toast.makeText(display.this, "ta zabawa już znajduje sie na tej liście ulubionych",Toast.LENGTH_SHORT).show();
                            else
                            {
                                dbFavoritesHelper.addGametoFavorite(favoritesName, game);
                                Toast.makeText(display.this, "zabawa została dodana",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    */
                }
            });

        //notes
            notesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notes = dbFavorites.getNotes(game, playlist);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(display.this);
                    final LayoutInflater inflater = display.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.notes, null);

                    final EditText notesEditText = (EditText) dialogView.findViewById(R.id.notesEditText);
                    if (notes != null && !notes.equals("")) {
                        notesEditText.setText(notes);
                    }

                    builder.setView(dialogView);
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            notes = notesEditText.getText().toString();
                            dbFavorites.updateNotes(notes, game, playlist);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    Button negativButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    negativButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    dialog.setCanceledOnTouchOutside(false);
                }
            });

        final ImageButton nextButton = (ImageButton) findViewById(R.id.next);
        final ImageButton prevButton = (ImageButton) findViewById(R.id.prev);

        if( category!=null && (category.equals("gameOfTheDay") || category.equals("search")) )
        {
            nextButton.setVisibility(View.GONE);
            prevButton.setVisibility(View.GONE);
        }


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category.equals("ulu"))
                {
                    int idgame = dbFavorites.numberOfGame(playlist, game);
                    int maxNR = dbFavorites.maxNumber(playlist);
                    if( idgame != maxNR )
                    {
                        game = dbFavorites.gameFromNumber( idgame+1, playlist );
                    }
                }
                else
                {
                    String newgame = dbHelper.nextGame(category, game);
                    if (!newgame.equals(""))
                        game = newgame;
                }
                txt = dbHelper.getText(game);
                text.setText(txt);
                gameName.setText(game);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category.equals("ulu"))
                {
                    int idgame = dbFavorites.numberOfGame(playlist, game);
                    //int maxNR = dbFavorites.maxNumber(playlist);
                    if( idgame != 1 )
                    {
                        game = dbFavorites.gameFromNumber( idgame-1, playlist );
                        txt = dbHelper.getText(game);
                    }
                }
                else
                {
                    String newgame = dbHelper.prevGame(category, game);
                    if (!newgame.equals(""))
                        game = newgame;
                }
                txt = dbHelper.getText(game);
                text.setText(txt);
                gameName.setText(game);
            }
        });



    }

    @Override
    protected void onResume()
    {
        super.onResume();
        final SettingsService sService = new SettingsService(getApplicationContext());

        /*
         * check if text size is changed
         */
        int currentTextSize = sService.getTextSize();
        if(currentTextSize != textSize)
        {
            gameName.setTextSize(currentTextSize + 6);
            text.setTextSize(currentTextSize);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
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

        if (id == R.id.action_search) {
            Intent openSearching = new Intent(getApplicationContext(), search.class);
            startActivity(openSearching);
        }
        else if (id == R.id.action_settings) {
            Intent otworz_ustawienia = new Intent(getApplicationContext(), settings.class);
            startActivity(otworz_ustawienia);
        }
        return super.onOptionsItemSelected(item);
    }
}
