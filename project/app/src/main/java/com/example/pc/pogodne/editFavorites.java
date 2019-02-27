package com.example.pc.pogodne;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.pc.pogodne.R.layout.activity_edit_favorites;

public class editFavorites extends AppCompatActivity {

    int mSelectedItem = -1;
    int olderSelection = -1;
    String nameOfFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_edit_favorites);

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        myToolbar.setTitle("Ulubione");
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        //actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        //actionBar.setDisplayShowHomeEnabled(true);

        final Button deleteAll = (Button) findViewById(R.id.deleteAll);
        final Button editNameOfFavorite = (Button) findViewById(R.id.editNameButton);
        final Button deleteOne = (Button) findViewById(R.id.buttonDeleteOne);
        final Button upButton = (Button) findViewById(R.id.upButton);
        final Button downButton = (Button) findViewById(R.id.downButton);
        final ListView listView = (ListView) findViewById(R.id.editableList);


        String filename = "settingsFile";
        final File file = new File(this.getFilesDir(), filename);
        int textSize = 15;

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textFromFile = new String(buffer);
            textSize = Integer.parseInt(textFromFile);
        }
        catch (IOException e)
        {
            Toast.makeText(editFavorites.this, "Error5",Toast.LENGTH_LONG).show();
        }
        final int tSize = textSize;


        final File favoriteFile = new File(editFavorites.this.getFilesDir(), "ulu");

        nameOfFavorite = getIntent().getStringExtra("ulu");

        final DataBaseFavorites dbHelperFavorites = new DataBaseFavorites(editFavorites.this);

        final ArrayList<String> list = dbHelperFavorites.getGamesInFavorite(nameOfFavorite);
        myToolbar.setTitle(nameOfFavorite);

        final ArrayList<String> arrayList= list;

        //Toast.makeText(this, arrayList.get(0).toString(),Toast.LENGTH_SHORT).show();
        final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(editFavorites.this,android.R.layout.simple_list_item_1, arrayList)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(tSize);

                if(position == mSelectedItem)
                    view.setBackgroundColor(Color.GREEN);
                else
                    view.setBackgroundColor(Color.TRANSPARENT);

                return view;
            }
        };
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                olderSelection = mSelectedItem;
                mSelectedItem = i;
                arrayAdapter.notifyDataSetChanged();
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());

                AlertDialog.Builder builder = new AlertDialog.Builder(editFavorites.this);
                builder.setTitle("Czy na pewno chcesz usunąć tę listę?");
                builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelperFavorites.deleteName(nameOfFavorite);
                        finish();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        editNameOfFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(editFavorites.this);

                final EditText newNameSpace = new EditText(editFavorites.this);
                newNameSpace.setSingleLine();
                newNameSpace.setHint("Nowa nazwa");
                newNameSpace.setHintTextColor(getResources().getColor(R.color.hintColor));
                LinearLayout linearLayout = new LinearLayout(editFavorites.this);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(36, 8, 36, 8);

                newNameSpace.setLayoutParams(layoutParams);


                linearLayout.addView(newNameSpace, layoutParams);

                builder.setMessage("Podaj nową nazwę listy");
                builder.setView(linearLayout);
                builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Zmień", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = newNameSpace.getText().toString();
                        if (newName.replace(" ", "").equals(""))
                        {
                            Toast.makeText(editFavorites.this, "Uzupełnij nową nazwę", Toast.LENGTH_SHORT).show();
                        }
                        else
                        if(newName.contains("%") || newName.contains(">") ||
                                newName.contains("@") || newName.contains("<") ||
                                newName.contains("#") || newName.contains("|") ||
                                newName.contains("$"))
                        {
                            Toast.makeText(editFavorites.this, "nazwa nie może zawierać:\n%<>@#$|",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            dbHelperFavorites.editNameOfFavorite(nameOfFavorite, newName);
                            nameOfFavorite = newName;
                            myToolbar.setTitle(newName);
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        deleteOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedItem == -1)
                {
                    Toast.makeText(editFavorites.this, "wybierz zabawę", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    dbHelperFavorites.deleteGame(nameOfFavorite, list.get(mSelectedItem));
                    list.remove(mSelectedItem);
                    mSelectedItem = -1;
                    olderSelection = -1;
                    arrayAdapter.notifyDataSetChanged();
                }

            }
        });

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedItem > 0)
                {
                    String aboveItem = list.get(mSelectedItem-1);
                    String game = list.get(mSelectedItem);
                    list.set(mSelectedItem - 1, list.get(mSelectedItem));
                    list.set(mSelectedItem, aboveItem);
                    dbHelperFavorites.upGame(nameOfFavorite, game);
                    mSelectedItem--;
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedItem < list.size()-1)
                {
                    String aboveItem = list.get(mSelectedItem + 1);
                    String game = list.get(mSelectedItem);
                    list.set(mSelectedItem + 1, list.get(mSelectedItem));
                    list.set(mSelectedItem, aboveItem);
                    dbHelperFavorites.downGame(nameOfFavorite, game);
                    mSelectedItem++;
                    arrayAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(activity_edit_favorites);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        myToolbar.setTitle("Ulubione");
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        //actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        //actionBar.setDisplayShowHomeEnabled(true);

        final Button deleteAll = (Button) findViewById(R.id.deleteAll);
        final CheckBox deleteOne = (CheckBox) findViewById(R.id.deleteBox);
        final ListView listView = (ListView) findViewById(R.id.editableList);


        String filename = "settingsFile";
        final File file = new File(this.getFilesDir(), filename);
        int textSize = 15;

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textFromFile = new String(buffer);
            textSize = Integer.parseInt(textFromFile);
        }
        catch (IOException e)
        {
            Toast.makeText(editFavorites.this, "Error5",Toast.LENGTH_LONG).show();
        }
        final int tSize = textSize;

        ArrayList<String> list= new ArrayList<>();

        final File favoriteFile = new File(editFavorites.this.getFilesDir(), "ulu");

        final String nameOfFavorite = getIntent().getStringExtra("ulu");
        list = FileHelper.titlesInFavorite(favoriteFile, nameOfFavorite);
        myToolbar.setTitle(nameOfFavorite);

        final ArrayList<String> arrayList= list;
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
                if(deleteOne.isChecked())
                {
                    if(FileHelper.removeFromFavorites(favoriteFile,nameOfFavorite, arrayList.get(i)) == 0)
                    {
                        Toast.makeText(editFavorites.this, "Error27", Toast.LENGTH_LONG).show();
                    }
                    finish();
                    startActivity(getIntent());
                }
                else {
                    Intent openGame = new Intent(getApplicationContext(), display.class);
                    openGame.putExtra("zabawa", arrayList.get(i));
                    startActivity(openGame);
                }
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(editFavorites.this, nameOfFavorite,Toast.LENGTH_LONG).show();
                if(FileHelper.removeFavorites(favoriteFile, nameOfFavorite) == 0)
                {
                    Toast.makeText(editFavorites.this, "Error28",Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }
    */


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
