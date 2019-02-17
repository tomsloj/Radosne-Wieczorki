package com.example.pc.pogodne;

import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class listaUlu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ulu);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        myToolbar.setTitle("Ulubione");
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowHomeEnabled(true);

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
            Toast.makeText(listaUlu.this, "Error29".toString(),Toast.LENGTH_LONG).show();
        }

        final int tSize = tekstSize;

        final ListView listaulu = (ListView) findViewById(R.id.listaUlubionych);
        final File ulufile = new File(listaUlu.this.getFilesDir(), "ulu");
        final ObslugaPliku op = new ObslugaPliku();

        ArrayList<String> lista1 = op.listaulu(ulufile);
        //lista1.add(op.calyplik(ulufile));
        final ArrayList<String> lista = lista1;

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(listaUlu.this,android.R.layout.simple_list_item_1, lista)
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

        listaulu.setAdapter(arrayAdapter);

        listaulu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nazwaulu = lista.get(i);

                    Intent otworz_edit = new Intent(getApplicationContext(), edytujUlu.class);
                    otworz_edit.putExtra("ulu", nazwaulu);
                    startActivity(otworz_edit);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_lista_ulu);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        myToolbar.setTitle("Ulubione");
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowHomeEnabled(true);

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
            Toast.makeText(listaUlu.this, "Error29".toString(),Toast.LENGTH_LONG).show();
        }

        final int tSize = tekstSize;

        final ListView listaulu = (ListView) findViewById(R.id.listaUlubionych);
        final File ulufile = new File(listaUlu.this.getFilesDir(), "ulu");
        final ObslugaPliku op = new ObslugaPliku();

        ArrayList<String> lista1 = op.listaulu(ulufile);
        //lista1.add(op.calyplik(ulufile));
        final ArrayList<String> lista = lista1;

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(listaUlu.this,android.R.layout.simple_list_item_1, lista)
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

        listaulu.setAdapter(arrayAdapter);

        listaulu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nazwaulu = lista.get(i);

                Intent otworz_edit = new Intent(getApplicationContext(), edytujUlu.class);
                otworz_edit.putExtra("ulu", nazwaulu);
                startActivity(otworz_edit);

            }
        });


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
