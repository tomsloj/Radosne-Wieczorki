package com.example.pc.pogodne;

import android.annotation.SuppressLint;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



public class display extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        String filename = "settingsFile";
        final File file = new File(this.getFilesDir(), filename);
        int tekstSize = 15;

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String plik = new String(buffer);
            tekstSize = Integer.parseInt(plik);
        } catch (IOException e) {
            Toast.makeText(display.this, "Error9", Toast.LENGTH_LONG).show();
        }


        final TextView nazwaZabawy = (TextView) findViewById(R.id.titleOfGame);
        final TextView text = (TextView) findViewById(R.id.textOfGame);

        nazwaZabawy.setTextSize(tekstSize + 6);
        text.setTextSize(tekstSize);

        final String zabawa = getIntent().getStringExtra("zabawa");

        final File tmpfile = new File(this.getFilesDir(), "tmpfile");

        try {
            FileOutputStream fos = new FileOutputStream(tmpfile);
            byte[] buffer = zabawa.getBytes();
            fos.write(buffer);
            fos.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error13", Toast.LENGTH_LONG).show();
        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        myToolbar.setTitle(zabawa);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);

        String fileName = "nazwy.txt";
        try {
            InputStream stream = getAssets().open(fileName);

            String textFromFile;
            nazwaZabawy.setText(zabawa);

            textFromFile = FileHelper.textOfGame(stream, zabawa);
            text.setText(textFromFile);
        } catch (IOException ex) {
            Toast.makeText(this, "Error10", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
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

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent otworz_wyszukiwanie = new Intent(getApplicationContext(), search.class);
            startActivity(otworz_wyszukiwanie);
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
                builder.setView(dialogView);
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                final Button stworz = (Button) dialogView.findViewById(R.id.createFavorite);
                final EditText nazwa =(EditText) dialogView.findViewById(R.id.nameOfFavorite);
                final ListView listaulu = (ListView) dialogView.findViewById(R.id.dialogLisOfFavorites);

                final File ulufile = new File(display.this.getFilesDir(), "ulu");
                final File tmpfile = new File(display.this.getFilesDir(), "tmpfile");

                final ArrayList<String> lista = FileHelper.listOfFavorites(ulufile);
                final ArrayAdapter arrayAdapter = new ArrayAdapter<>(display.this,android.R.layout.simple_list_item_1, lista);

                AlertDialog dialog = builder.create();
                dialog.show();

                listaulu.setAdapter(arrayAdapter);


                stworz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nazwaulu = nazwa.getText().toString();
                        if(nazwaulu.isEmpty() || nazwaulu.replace(" ", "").isEmpty())
                        {
                            Toast.makeText(display.this, "nazwa nie może być pusta",Toast.LENGTH_LONG).show();
                        }
                        else
                            if(nazwaulu.contains("%")||nazwaulu.contains(">")||nazwaulu.contains("@")||nazwaulu.contains("<")||nazwaulu.contains("#")||nazwaulu.contains("|")||nazwaulu.contains("$"))
                            {
                                Toast.makeText(display.this, "nazwa nie może zawierać:\n%<>@#$|",Toast.LENGTH_LONG).show();
                            }
                            else
                                {
                                    FileHelper op = new FileHelper();

                                    final File ulufile = new File(display.this.getFilesDir(), "ulu");

                                    final File tmpfile = new File(display.this.getFilesDir(), "tmpfile");

                                    String zabawa = null;
                                    try {
                                        FileInputStream stream = new FileInputStream(tmpfile);
                                        int size = stream.available();
                                        byte[] buffer = new byte[size];
                                        stream.read(buffer);
                                        stream.close();
                                        zabawa = new String(buffer);
                                    }
                                    catch (IOException e)
                                    {
                                        Toast.makeText(display.this, "Error22",Toast.LENGTH_LONG).show();

                                    }

                                    if(FileHelper.doExistInFavoriteList(ulufile, nazwaulu))
                                    {
                                        Toast.makeText(display.this, "taka nazwa listy ulubionych już istnieje",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        if (FileHelper.createNewFavorite(ulufile,nazwaulu, zabawa) == 1) {
                                            Toast.makeText(display.this, "zabawa została zapisana do nowopowstałej listy", Toast.LENGTH_SHORT).show();
                                            arrayAdapter.add(nazwaulu);
                                        }
                                            else
                                        {
                                            Toast.makeText(display.this, Integer.toString(FileHelper.createNewFavorite(ulufile,nazwaulu, zabawa)),Toast.LENGTH_LONG).show();
                                        }
                                    }
                            }
                    }
                });






                listaulu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String nazwaulu = lista.get(i);

                        String zabawa = null;
                        try {
                            FileInputStream stream = new FileInputStream(tmpfile);
                            int size = stream.available();
                            byte[] buffer = new byte[size];
                            stream.read(buffer);
                            stream.close();
                            zabawa = new String(buffer);
                        }
                        catch (IOException e)
                        {
                            Toast.makeText(display.this, "Error23",Toast.LENGTH_LONG).show();
                        }
                        int tmp = FileHelper.addToFavorites(ulufile, nazwaulu, zabawa);
                        if(tmp == 0)
                        {
                            Toast.makeText(display.this, "Error24",Toast.LENGTH_LONG).show();
                        }
                        else
                        if(tmp == 1)
                        {
                            Toast.makeText(display.this, "ta zabawa już znajduje sie na tej liście ulubionych",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(display.this, "zabawa została dodana",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                final TextView text = (TextView) dialog.findViewById(R.id.textChooseExistingFavorite);
                if(lista.size() == 0)
                {
                    text.setText("Nie posiadasz jeszcze rzadnej listy ulubionych");
                }
                //Toast.makeText(display.this, "ooo".toString(),Toast.LENGTH_LONG).show();
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String filename = "settingsFile";
        final File file = new File(this.getFilesDir(), filename);
        int tekstSize = 15;

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String plik = new String(buffer);
            tekstSize = Integer.parseInt(plik);
        } catch (IOException e) {
            Toast.makeText(display.this, "Error9", Toast.LENGTH_LONG).show();
        }


        final TextView nazwaZabawy = (TextView) findViewById(R.id.titleOfGame);
        final TextView text = (TextView) findViewById(R.id.textOfGame);

        nazwaZabawy.setTextSize(tekstSize + 6);
        text.setTextSize(tekstSize);

        final String zabawa = getIntent().getStringExtra("zabawa");

        final File tmpfile = new File(this.getFilesDir(), "tmpfile");

        try {
            FileOutputStream fos = new FileOutputStream(tmpfile);
            byte[] buffer = zabawa.getBytes();
            fos.write(buffer);
            fos.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error13", Toast.LENGTH_LONG).show();
        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        myToolbar.setTitle(zabawa);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);


        String fileName = "nazwy.txt";
        try {
            InputStream stream = getAssets().open(fileName);

            String textFromFile;
            nazwaZabawy.setText(zabawa);

            textFromFile = FileHelper.textOfGame(stream, zabawa);
            text.setText(textFromFile);
        } catch (IOException ex) {
            Toast.makeText(this, "Error10", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }


    }
}
