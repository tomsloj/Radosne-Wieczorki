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

import com.fenjuly.mylibrary.FloorListView;

import java.io.File;
import java.util.ArrayList;

import static com.example.pc.pogodne.R.layout.activity_edit_favorites;

public class editFavorites extends AppCompatActivity {

    int mSelectedItem = -1;
    int olderSelection = -1;
    String nameOfFavorite;
    int textSize;
    ArrayList<String> list;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_edit_favorites);

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        myToolbar.setTitle("Ulubione");
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);

        final Button deleteAll = (Button) findViewById(R.id.deleteAll);
        final Button editNameOfFavorite = (Button) findViewById(R.id.editNameButton);
        final Button deleteOne = (Button) findViewById(R.id.buttonDeleteOne);
        final Button upButton = (Button) findViewById(R.id.upButton);
        final Button downButton = (Button) findViewById(R.id.downButton);
        listView = (FloorListView) findViewById(R.id.editableList);
        ((FloorListView) listView).setMode(FloorListView.ABOVE);
        final SettingsService sService = new SettingsService(getApplicationContext());
        textSize = sService.getTextSize();


        final File favoriteFile = new File(editFavorites.this.getFilesDir(), "ulu");

        nameOfFavorite = getIntent().getStringExtra("ulu");

        final DataBaseFavorites dbHelperFavorites = new DataBaseFavorites(editFavorites.this);

        list = dbHelperFavorites.getGamesInFavorite(nameOfFavorite);
        myToolbar.setTitle(nameOfFavorite);

        final ArrayList<String> arrayList= list;

        //list of games
        final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(editFavorites.this,android.R.layout.simple_list_item_1, arrayList)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setBackgroundColor(getResources().getColor( R.color.background ));
                tv.setTextSize(textSize);
                tv.setTextColor(Color.BLACK);

                //mark selected game
                if(position == mSelectedItem)
                    view.setBackgroundColor( getResources().getColor( R.color.checkedColor) );
                else
                    view.setBackgroundColor(getResources().getColor( R.color.background ));

                return view;
            }
        };
        listView.setAdapter(arrayAdapter);
        if(list.size() == 0)
            listView.setEmptyView(findViewById(R.id.emptyListText));
        //select game
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
                        dbHelperFavorites.deleteFavorite(nameOfFavorite);
                        finish();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                Button negativButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                negativButton.setTextColor( getResources().getColor( R.color.colorPrimary) );
                positiveButton.setTextColor( getResources().getColor( R.color.colorPrimary) );
            }
        });

        editNameOfFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(editFavorites.this);

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
                builder.setPositiveButton("Zmień", null ).create();

                final AlertDialog aDialog = builder.create();
                aDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button button = aDialog.getButton( AlertDialog.BUTTON_POSITIVE );
                        button.setOnClickListener( new View.OnClickListener(){
                            @Override
                            public void onClick( View view )
                            {
                                String newName = newNameSpace.getText().toString();
                                DataBaseFavorites dbFavoritesHelper = new DataBaseFavorites( getApplicationContext() );
                                if (newName.replace(" ", "").equals(""))
                                {
                                    Toast.makeText(editFavorites.this, "Uzupełnij nową nazwę", Toast.LENGTH_SHORT).show();
                                }
                                else
                                if(newName.contains("%") || newName.contains(">") ||
                                        newName.contains("@") || newName.contains("<") ||
                                        newName.contains("#") || newName.contains("|") ||
                                        newName.contains("$") || newName.contains("'") )
                                {
                                    Toast.makeText(editFavorites.this, "nazwa nie może zawierać:\n%<>@#$|'",Toast.LENGTH_LONG).show();
                                }
                                if( dbFavoritesHelper.favoriteExist( newName ) )
                                    Toast.makeText( getApplicationContext(), "taka nazwa listy ulubionych już istnieje",Toast.LENGTH_SHORT).show();
                                else
                                {
                                    dbHelperFavorites.editNameOfFavorite(nameOfFavorite, newName);
                                    nameOfFavorite = newName;
                                    myToolbar.setTitle(newName);
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });

                aDialog.show();

                Button negativButton = aDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                Button positiveButton = aDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                negativButton.setTextColor( getResources().getColor( R.color.colorPrimary) );
                positiveButton.setTextColor( getResources().getColor( R.color.colorPrimary) );
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
                if(mSelectedItem < list.size()-1 && mSelectedItem >= 0)
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

    @Override
    protected void onResume()
    {
        super.onResume();
        final SettingsService sService = new SettingsService(getApplicationContext());
        final int currentTextSize = sService.getTextSize();
        if(currentTextSize != textSize)
        {
            final ArrayList<String> arrayList= list;
            final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(editFavorites.this,android.R.layout.simple_list_item_1, arrayList)
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent)
                {
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    tv.setTextSize(currentTextSize);
                    tv.setTextColor(Color.BLACK);

                    if(position == mSelectedItem) {
                        view.setBackgroundColor( getResources().getColor( R.color.checkedColor) );
                    }
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
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        final SettingsService sService = new SettingsService(getApplicationContext());
        sService.setNewNameOfFavorite(nameOfFavorite);
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
