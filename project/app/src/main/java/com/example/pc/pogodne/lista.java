package com.example.pc.pogodne;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class lista extends AppCompatActivity {

    ListView listView;

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        //pobieram jaka kategoria ma się znajdować na liście
        String kategoria = getIntent().getStringExtra("kategoria");

        //ustawiam toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        if(kategoria.equals("all"))
            myToolbar.setTitle("Lista zabaw");
        else
            myToolbar.setTitle(kategoria);
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
            Toast.makeText(lista.this, "Error5".toString(),Toast.LENGTH_LONG).show();
        }
        final int tSize = tekstSize;

        listView=(ListView)findViewById(R.id.listview);
        ArrayList<String> lista= new ArrayList<>();


        String fileName = "dane.txt";

        try {
            InputStream stream = getAssets().open(fileName);
            ObslugaPliku op = new ObslugaPliku();
            lista = op.tytułyWkategorii(stream, kategoria);
            //Toast.makeText(lista.this, Integer.toString(lista.size()),Toast.LENGTH_LONG).show();

        } catch (IOException ex) {
            Toast.makeText(this, "Error6".toString(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

        //}
        final ArrayList<String> arrayList= lista;
        //Toast.makeText(this, arrayList.get(0).toString(),Toast.LENGTH_SHORT).show();
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(lista.this,android.R.layout.simple_list_item_1, arrayList)
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
                Intent otworz_zabawe = new Intent(getApplicationContext(), wyswietlenie.class);
                otworz_zabawe.putExtra("zabawa", arrayList.get(i).toString());
                startActivity(otworz_zabawe);
            }
        });
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
        }
        catch (IOException e)
        {
            Toast.makeText(lista.this, "Error5".toString(),Toast.LENGTH_LONG).show();
        }
        final int tSize = tekstSize;

        listView=(ListView)findViewById(R.id.listview);
        ArrayList<String> lista= new ArrayList<>();

        String kategoria = getIntent().getStringExtra("kategoria");
        String fileName = "dane.txt";

        try {
            InputStream stream = getAssets().open(fileName);
            ObslugaPliku op = new ObslugaPliku();
            lista = op.tytułyWkategorii(stream, kategoria);
            //Toast.makeText(lista.this, Integer.toString(lista.size()),Toast.LENGTH_LONG).show();

        } catch (IOException ex) {
            Toast.makeText(this, "Error6".toString(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

        //}
        final ArrayList<String> arrayList= lista;
        //Toast.makeText(this, arrayList.get(0).toString(),Toast.LENGTH_SHORT).show();
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(lista.this,android.R.layout.simple_list_item_1, arrayList)
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



        /*
        setContentView(R.layout.activity_lista);

        //pobieram jaka kategoria ma się znajdować na liście
        String kategoria = getIntent().getStringExtra("kategoria");

        //ustawiam toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        if(kategoria.equals("all"))
            myToolbar.setTitle("Lista zabaw");
        else
            myToolbar.setTitle(kategoria);
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
            Toast.makeText(lista.this, "Error5".toString(),Toast.LENGTH_LONG).show();
        }
        final int tSize = tekstSize;

        listView=(ListView)findViewById(R.id.listview);
        ArrayList<String> lista= new ArrayList<>();

            String fileName = "dane.txt";

            try {
                InputStream stream = getAssets().open(fileName);
                ObslugaPliku op = new ObslugaPliku();
                lista = op.tytułyWkategorii(stream, kategoria);
                //Toast.makeText(lista.this, Integer.toString(lista.size()),Toast.LENGTH_LONG).show();

            } catch (IOException ex) {
                Toast.makeText(this, "Error6".toString(), Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }

        //}
        final ArrayList<String> arrayList= lista;
        //Toast.makeText(this, arrayList.get(0).toString(),Toast.LENGTH_SHORT).show();
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(lista.this,android.R.layout.simple_list_item_1, arrayList)
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
                Intent otworz_zabawe = new Intent(getApplicationContext(), wyswietlenie.class);
                otworz_zabawe.putExtra("zabawa", arrayList.get(i).toString());
                startActivity(otworz_zabawe);
            }
        });

        */

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
