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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class list extends AppCompatActivity {

    ListView listView;

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //get category of list
        String category = getIntent().getStringExtra("kategoria");

        //create toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        if(category.equals("all"))
            myToolbar.setTitle("Lista zabaw");
        else
            myToolbar.setTitle(category);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        //actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        //actionBar.setDisplayShowHomeEnabled(true);

        String fileSettingsName = "settingsFile";
        final File file = new File(this.getFilesDir(), fileSettingsName);
        int textSize = 15;

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textOfFile = new String(buffer);
            textSize = Integer.parseInt(textOfFile);
        }
        catch (IOException e)
        {
            Toast.makeText(list.this, "Error5",Toast.LENGTH_LONG).show();
        }
        final int tSize = textSize;

        listView=(ListView)findViewById(R.id.listview);
        ArrayList<String> list= new ArrayList<>();


        String fileBase = "dane.txt";

        try {
            InputStream stream = getAssets().open(fileBase);
            list = FileHelper.titlesInCategory(stream, category);

        } catch (IOException ex) {
            Toast.makeText(this, "Error6", Toast.LENGTH_SHORT).show();
        }

        final ArrayList<String> arrayList= list;

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(list.this,android.R.layout.simple_list_item_1, arrayList)
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
                Intent openGame = new Intent(getApplicationContext(), display.class);
                openGame.putExtra("zabawa", arrayList.get(i).toString());
                startActivity(openGame);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        String filename = "settingsFile";
        final File file = new File(this.getFilesDir(), filename);
        int textSize = 15;

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textOfFile = new String(buffer);
            textSize = Integer.parseInt(textOfFile);
        }
        catch (IOException e)
        {
            Toast.makeText(list.this, "Error5",Toast.LENGTH_LONG).show();
        }
        final int tSize = textSize;

        listView=(ListView)findViewById(R.id.listview);
        ArrayList<String> list= new ArrayList<>();

        String category = getIntent().getStringExtra("kategoria");
        String fileName = "dane.txt";

        try {
            InputStream stream = getAssets().open(fileName);
            list = FileHelper.titlesInCategory(stream, category);
            //Toast.makeText(list.this, Integer.toString(list.size()),Toast.LENGTH_LONG).show();

        } catch (IOException ex) {
            Toast.makeText(this, "Error6", Toast.LENGTH_SHORT).show();
        }

        final ArrayList<String> arrayList= list;
        //Toast.makeText(this, arrayList.get(0).toString(),Toast.LENGTH_SHORT).show();
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(list.this,android.R.layout.simple_list_item_1, arrayList)
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
