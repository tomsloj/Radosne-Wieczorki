package com.example.pc.pogodne;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;



public class display extends AppCompatActivity {

    int counter = 0;
    int textSize;
    TextView gameName;
    TextView text;
    String game;

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

        final File tmpFile = new File(this.getFilesDir(), "tmpfile");

        try {
            FileOutputStream fos = new FileOutputStream(tmpFile);
            byte[] buffer = game.getBytes();
            fos.write(buffer);
            fos.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error13", Toast.LENGTH_LONG).show();
        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        myToolbar.setTitle(game);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setTitle(game);



        DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());

        String txt = dbHelper.getText(game);

        gameName.setText(game);
        text.setText(txt);
        /*
        try {
            InputStream stream = getAssets().open(fileName);

            String textFromFile;
            gameName.setText(game);

            textFromFile = FileHelper.textOfGame(stream, game);
            text.setText(textFromFile);
        } catch (IOException ex) {
            Toast.makeText(this, "Error10", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
        */

        final Button hiddenButton = (Button) findViewById(R.id.hiddenButton);

        //TODO zz from database
        hiddenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(game.equals("HOP HOP HOP HULA HOP"))
                {
                    counter ++;
                    if(counter > 3)
                    {
                        DataBaseFavorites dataBaseFavorites = new DataBaseFavorites(display.this);
                        if(!dataBaseFavorites.favoriteExist("zakazane zabawy"))
                        {
                            dataBaseFavorites.createFavorites("zakazane zabawy", "Icek");
                            dataBaseFavorites.addGametoFavorite("zakazane zabawy", "zwierzęta");
                            dataBaseFavorites.addGametoFavorite("zakazane zabawy", "zwierzę");
                            Toast.makeText(display.this, "chyba dodało", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(display.this, "niespodzianka", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        final SettingsService sService = new SettingsService(getApplicationContext());
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
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    //@SuppressLint("SetTextI18n")
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
        else
            if(id == R.id.favoritesButton)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(display.this);
                final LayoutInflater inflater = display.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_to_favorites, null);

                final Button stworz = (Button) dialogView.findViewById(R.id.createFavorite);
                final EditText nazwa =(EditText) dialogView.findViewById(R.id.nameOfFavorite);
                final ListView listaulu = (ListView) dialogView.findViewById(R.id.dialogLisOfFavorites);

                final DataBaseFavorites dbFavoritesHelper = new DataBaseFavorites(display.this);

                final ArrayList<String> list = dbFavoritesHelper.getFavoritesList();
                final ArrayAdapter arrayAdapter = new ArrayAdapter<>(display.this,android.R.layout.simple_list_item_1, list);

                builder.setView(dialogView);
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                listaulu.setAdapter(arrayAdapter);

                stworz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nameOfFavorite = nazwa.getText().toString();
                        if(nameOfFavorite.isEmpty() || nameOfFavorite.replace(" ", "").isEmpty())
                        {
                            Toast.makeText(display.this, "nazwa nie może być pusta",Toast.LENGTH_LONG).show();
                        }
                        else
                            if(nameOfFavorite.contains("%") || nameOfFavorite.contains(">") ||
                                    nameOfFavorite.contains("@") || nameOfFavorite.contains("<") ||
                                    nameOfFavorite.contains("#") || nameOfFavorite.contains("|") ||
                                    nameOfFavorite.contains("$"))
                            {
                                Toast.makeText(display.this, "nazwa nie może zawierać:\n%<>@#$|",Toast.LENGTH_LONG).show();
                            }
                            else
                                {
                                    if(dbFavoritesHelper.favoriteExist(nameOfFavorite))
                                        Toast.makeText(display.this, "taka nazwa listy ulubionych już istnieje",Toast.LENGTH_SHORT).show();
                                    else
                                    {
                                        dbFavoritesHelper.createFavorites(nameOfFavorite, game);
                                        arrayAdapter.add(nameOfFavorite);
                                        arrayAdapter.notifyDataSetChanged();
                                        Toast.makeText(display.this, "nowa lista ulubionych została utworzona",Toast.LENGTH_SHORT).show();
                                    }
                            }
                    }
                });

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

                final TextView text = (TextView) dialog.findViewById(R.id.textChooseExistingFavorite);
                if(list.size() == 0)
                {
                    text.setText("Nie posiadasz jeszcze żadnej listy ulubionych");
                }
            }

        return super.onOptionsItemSelected(item);
    }

}
