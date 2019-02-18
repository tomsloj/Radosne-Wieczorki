package com.example.pc.pogodne;

import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class editFavorites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_favorites);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        myToolbar.setTitle("Ulubione");
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowHomeEnabled(true);

        final Button usuncalosc = (Button) findViewById(R.id.deleteAll);
        final CheckBox usunjeden = (CheckBox) findViewById(R.id.deleteBox);
        final ListView listView = (ListView) findViewById(R.id.editableList);


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
            Toast.makeText(editFavorites.this, "Error5".toString(),Toast.LENGTH_LONG).show();
        }
        final int tSize = tekstSize;

        ArrayList<String> lista= new ArrayList<>();

        final File ulufile = new File(editFavorites.this.getFilesDir(), "ulu");

        final String nazwaUlu = getIntent().getStringExtra("ulu");
        FileHelper op = new FileHelper();
        lista = op.tytułyWulu(ulufile, nazwaUlu);
        myToolbar.setTitle(nazwaUlu);

        final ArrayList<String> arrayList= lista;
        //Toast.makeText(this, arrayList.get(0).toString(),Toast.LENGTH_SHORT).show();
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(editFavorites.this,android.R.layout.simple_list_item_1, arrayList)
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
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(usunjeden.isChecked())
                {
                    FileHelper op = new FileHelper();
                    if(op.usunzulu(ulufile,nazwaUlu, arrayList.get(i)) == 0)
                    {
                        Toast.makeText(editFavorites.this, "Error27".toString(),Toast.LENGTH_LONG).show();
                    }
                    finish();
                    startActivity(getIntent());
                }
                else {
                    Intent otworz_zabawe = new Intent(getApplicationContext(), display.class);
                    otworz_zabawe.putExtra("zabawa", arrayList.get(i).toString());
                    startActivity(otworz_zabawe);
                }
            }
        });

        usuncalosc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileHelper op = new FileHelper();
                Toast.makeText(editFavorites.this, nazwaUlu.toString(),Toast.LENGTH_LONG).show();
                if(op.usunlisteulu(ulufile, nazwaUlu) == 0)
                {
                    Toast.makeText(editFavorites.this, "Error28".toString(),Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_edit_favorites);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        myToolbar.setTitle("Ulubione");
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowHomeEnabled(true);

        final Button usuncalosc = (Button) findViewById(R.id.deleteAll);
        final CheckBox usunjeden = (CheckBox) findViewById(R.id.deleteBox);
        final ListView listView = (ListView) findViewById(R.id.editableList);


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
            Toast.makeText(editFavorites.this, "Error5".toString(),Toast.LENGTH_LONG).show();
        }
        final int tSize = tekstSize;

        ArrayList<String> lista= new ArrayList<>();

        final File ulufile = new File(editFavorites.this.getFilesDir(), "ulu");

        final String nazwaUlu = getIntent().getStringExtra("ulu");
        FileHelper op = new FileHelper();
        lista = op.tytułyWulu(ulufile, nazwaUlu);
        myToolbar.setTitle(nazwaUlu);

        final ArrayList<String> arrayList= lista;
        //Toast.makeText(this, arrayList.get(0).toString(),Toast.LENGTH_SHORT).show();
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(editFavorites.this,android.R.layout.simple_list_item_1, arrayList)
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
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(usunjeden.isChecked())
                {
                    FileHelper op = new FileHelper();
                    if(op.usunzulu(ulufile,nazwaUlu, arrayList.get(i)) == 0)
                    {
                        Toast.makeText(editFavorites.this, "Error27".toString(),Toast.LENGTH_LONG).show();
                    }
                    finish();
                    startActivity(getIntent());
                }
                else {
                    Intent otworz_zabawe = new Intent(getApplicationContext(), display.class);
                    otworz_zabawe.putExtra("zabawa", arrayList.get(i).toString());
                    startActivity(otworz_zabawe);
                }
            }
        });

        usuncalosc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileHelper op = new FileHelper();
                Toast.makeText(editFavorites.this, nazwaUlu.toString(),Toast.LENGTH_LONG).show();
                if(op.usunlisteulu(ulufile, nazwaUlu) == 0)
                {
                    Toast.makeText(editFavorites.this, "Error28".toString(),Toast.LENGTH_LONG).show();
                }
                finish();
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
