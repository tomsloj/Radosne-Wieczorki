package com.example.pc.pogodne;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
        }
        catch (IOException e)
        {
            Toast.makeText(search.this, "Error19".toString(),Toast.LENGTH_LONG).show();
        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        myToolbar.setTitle("Szukaj");


        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowHomeEnabled(true);


        final Button przyciskSzukaj = (Button) findViewById(R.id.przycisk);
        final CheckBox szukajTytuły = (CheckBox) findViewById(R.id.tytułyCheckbox);
        final CheckBox szukajTekst = (CheckBox) findViewById(R.id.tekstCheckbox);
        final EditText editText = (EditText) findViewById(R.id.poleWyszukiwania);
        final ListView listView = (ListView)findViewById(R.id.listView);
        final TextView textView = (TextView) findViewById(R.id.textView);

        szukajTekst.setTextSize(tekstSize);
        szukajTytuły.setTextSize(tekstSize);
        editText.setTextSize(tekstSize);

        przyciskSzukaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean box1 = szukajTytuły.isChecked();
                boolean box2 = szukajTekst.isChecked();
                String fileName = "nazwy.txt";
                String toFind = editText.getText().toString();

                if(!box1 && !box2)
                {
                    Toast.makeText(search.this, "Po co w ogóle tu wchodzisz\njeśli nie chcesz nic znaleźć?".toString(),Toast.LENGTH_LONG).show();
                }
                else
                    if(toFind.equals(""))
                    {
                        Toast.makeText(search.this, "Uzupełnij co chcesz wyszukać".toString(),Toast.LENGTH_LONG).show();
                    }
                else
                {
                    try {
                        InputStream stream = getAssets().open(fileName);
                        ObslugaPliku op = new ObslugaPliku();

                        ArrayList<String> lista = op.szukaj(stream, toFind, box1, box2);

                        if(!lista.isEmpty())
                        {
                            ArrayAdapter arrayAdapter = new ArrayAdapter(search.this, android.R.layout.simple_list_item_1, lista);
                            listView.setAdapter(arrayAdapter);
                            textView.setText("");
                        }
                        else
                        {
                            ArrayAdapter arrayAdapter = new ArrayAdapter(search.this, android.R.layout.simple_list_item_1, lista);
                            listView.setAdapter(arrayAdapter);
                            textView.setText(R.string.noFound);
                        }
                    }
                    catch (IOException e)
                    {
                        Toast.makeText(search.this, "Error20".toString(),Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent otworz_zabawe = new Intent(getApplicationContext(), wyswietlenie.class);
                otworz_zabawe.putExtra("zabawa", listView.getItemAtPosition(i).toString());
                startActivity(otworz_zabawe);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_search);

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
        }
        catch (IOException e)
        {
            Toast.makeText(search.this, "Error19".toString(),Toast.LENGTH_LONG).show();
        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        myToolbar.setTitle("Szukaj");

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowHomeEnabled(true);


        final Button przyciskSzukaj = (Button) findViewById(R.id.przycisk);
        final CheckBox szukajTytuły = (CheckBox) findViewById(R.id.tytułyCheckbox);
        final CheckBox szukajTekst = (CheckBox) findViewById(R.id.tekstCheckbox);
        final EditText editText = (EditText) findViewById(R.id.poleWyszukiwania);
        final ListView listView = (ListView)findViewById(R.id.listView);
        final TextView textView = (TextView) findViewById(R.id.textView);

        szukajTekst.setTextSize(tekstSize);
        szukajTytuły.setTextSize(tekstSize);
        editText.setTextSize(tekstSize);

        przyciskSzukaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean box1 = szukajTytuły.isChecked();
                boolean box2 = szukajTekst.isChecked();
                String fileName = "nazwy.txt";
                String toFind = editText.getText().toString();

                if(!box1 && !box2)
                {
                    Toast.makeText(search.this, "Po co w ogóle tu wchodzisz\njeśli nie chcesz nic znaleźć?".toString(),Toast.LENGTH_LONG).show();
                }
                else
                if(toFind.equals(""))
                {
                    Toast.makeText(search.this, "Uzupełnij co chcesz wyszukać".toString(),Toast.LENGTH_LONG).show();
                }
                else
                {
                    try {
                        InputStream stream = getAssets().open(fileName);
                        ObslugaPliku op = new ObslugaPliku();

                        ArrayList<String> lista = op.szukaj(stream, toFind, box1, box2);

                        if(!lista.isEmpty())
                        {
                            ArrayAdapter arrayAdapter = new ArrayAdapter(search.this, android.R.layout.simple_list_item_1, lista);
                            listView.setAdapter(arrayAdapter);
                            textView.setText("");
                        }
                        else
                        {
                            ArrayAdapter arrayAdapter = new ArrayAdapter(search.this, android.R.layout.simple_list_item_1, lista);
                            listView.setAdapter(arrayAdapter);
                            textView.setText(R.string.noFound);
                        }
                    }
                    catch (IOException e)
                    {
                        Toast.makeText(search.this, "Error20".toString(),Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent otworz_zabawe = new Intent(getApplicationContext(), wyswietlenie.class);
                otworz_zabawe.putExtra("zabawa", listView.getItemAtPosition(i).toString());
                startActivity(otworz_zabawe);
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
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
            Intent otworz_liste = new Intent(getApplicationContext(), search.class);
            startActivity(otworz_liste);
        }
            else
                if(id == R.id.action_settings)
                {
                    Intent otworz_liste = new Intent(getApplicationContext(), settings.class);
                    startActivity(otworz_liste);
                }

        return super.onOptionsItemSelected(item);
    }



}
