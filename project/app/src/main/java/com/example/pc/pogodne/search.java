package com.example.pc.pogodne;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fenjuly.mylibrary.FloorListView;

import java.util.ArrayList;

public class search extends AppCompatActivity {

    int textSize;
    ListView listOfFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final SettingsService sService = new SettingsService(getApplicationContext());
        textSize = sService.getTextSize();


        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Szukaj");

        //actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);


        final Button searchButton = (Button) findViewById(R.id.searchButton);
        final CheckBox titleBox = (CheckBox) findViewById(R.id.titlesCheckbox);
        final CheckBox textBox = (CheckBox) findViewById(R.id.textsCheckbox);
        final EditText searchSpace = (EditText) findViewById(R.id.searchSpace);
        final TextView textNoFavorites = (TextView) findViewById(R.id.textEmptyFavoritesList);

        listOfFound = (FloorListView) findViewById(R.id.lisOfFound);
        ((FloorListView) listOfFound).setMode(FloorListView.ABOVE);


        /*
        ArrayList<String> tmplist = new ArrayList<>();
        tmplist.add("aaaa");
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(search.this, android.R.layout.simple_list_item_1, tmplist)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(textSize);
                tv.setTextColor(Color.BLACK);

                return view;
            }
        };
        listOfFound.setAdapter(arrayAdapter);
        */
        listOfFound.setEmptyView(findViewById(R.id.textEmptyFavoritesList));


        textBox.setTextSize(textSize);
        titleBox.setTextSize(textSize);
        searchSpace.setTextSize(textSize);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean box1 = titleBox.isChecked();
                boolean box2 = textBox.isChecked();
                String toFind = searchSpace.getText().toString();

                if(!box1 && !box2)
                {
                    Toast.makeText(search.this, "Wybierz gdzie chcesz szukać",Toast.LENGTH_LONG).show();
                }
                else
                if(toFind.equals(""))
                {
                    Toast.makeText(search.this, "Uzupełnij co chcesz wyszukać",Toast.LENGTH_LONG).show();
                }
                else
                if( toFind.contains("'") || toFind.contains("%") || toFind.contains("_") )
                {
                    final ArrayList<String> list = new ArrayList<>();
                    ArrayAdapter arrayAdapter = new ArrayAdapter<>(search.this, android.R.layout.simple_list_item_1, list);
                    listOfFound.setAdapter(arrayAdapter);
                    textNoFavorites.setText(R.string.noFound);
                }
                else
                {
                    DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());

                    final ArrayList<String> list = dbHelper.find(toFind, box1, box2);

                    if(!list.isEmpty())
                    {
                        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(search.this, android.R.layout.simple_list_item_1, list)
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
                        listOfFound.setAdapter(arrayAdapter);
                        textNoFavorites.setText("");
                    }
                    else
                    {
                        ArrayAdapter arrayAdapter = new ArrayAdapter<>(search.this, android.R.layout.simple_list_item_1, list);
                        listOfFound.setEmptyView(findViewById(R.id.textEmptyFavoritesList));
                        textNoFavorites.setText(R.string.noFound);
                    }
                }


            }
        });

        listOfFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent openGame = new Intent(getApplicationContext(), display.class);
                openGame.putExtra("zabawa", listOfFound.getItemAtPosition(i).toString());
                startActivity(openGame);
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
