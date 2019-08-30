package com.tomsloj.pc.pogodne;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

public class search extends AppCompatActivity {

    int textSize;
    SwipeMenuListView listOfFound;
    ArrayList<String> list = new ArrayList<>();
    ExpandableListViewAdapter adapter = null;
    AppAdapter appAdapter;
    SwipeMenuCreator creator;

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

        listOfFound = (SwipeMenuListView) findViewById(R.id.listOfFound);
        appAdapter = new AppAdapter();
        listOfFound.setAdapter(appAdapter);

        creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                switch (menu.getViewType()) {
                    case 0:
                    {
                        // create "add" item
                        SwipeMenuItem addItem = new SwipeMenuItem(
                                getApplicationContext());
                        // set item background
                        addItem.setBackground(new ColorDrawable(getResources().getColor(R.color.iconbackground)));
                        // set item width
                        addItem.setWidth(130);

                        // set item title
                        addItem.setIcon(R.drawable.add);
                        // add to menu
                        menu.addMenuItem(addItem);
                        break;
                    }
                    case 1:
                    {
                        SwipeMenuItem addItem = new SwipeMenuItem(
                                getApplicationContext());
                        // set item background
                        addItem.setBackground(new ColorDrawable(getResources().getColor(R.color.iconbackground)));
                        // set item width
                        addItem.setWidth(130);

                        // set item title
                        addItem.setIcon(R.drawable.add);
                        // add to menu
                        menu.addMenuItem(addItem);
                        SwipeMenuItem deleteItem = new SwipeMenuItem(
                                getApplicationContext());
                        // set item background
                        deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.iconbackground)));
                        // set item width
                        deleteItem.setWidth(130);

                        // set item title
                        deleteItem.setIcon(R.drawable.delete);
                        // add to menu
                        menu.addMenuItem(deleteItem);
                        break;
                    }
                }
            }
        };

        // set creator
        listOfFound.setMenuCreator(creator);

        listOfFound.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0: {
                        final String game = list.get(position);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(search.this);
                        final LayoutInflater inflater = search.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.add_to_favorites, null);

                        //final Button stworz = (Button) dialogView.findViewById(R.id.createFavorite);
                        //final EditText nazwa =(EditText) dialogView.findViewById(R.id.nameOfFavorite);
                        final ExpandableListView listaulu = (ExpandableListView) dialogView.findViewById(R.id.dialogLisOfFavorites);

                        final DataBaseFavorites dbFavoritesHelper = new DataBaseFavorites(search.this);

                        final ArrayList<String> list = dbFavoritesHelper.getFavoritesList();
                        final ArrayAdapter arrayAdapter = new ArrayAdapter<>(search.this, android.R.layout.simple_list_item_1, list);


                        builder.setView(dialogView);
                        final AlertDialog dialog1 = builder.create();
                        dialog1.show();

                        Button createButton = (Button) dialogView.findViewById(R.id.create);
                        Button addGameButton = (Button) dialogView.findViewById(R.id.addGameButton);
                        Button cancelButton = (Button) dialogView.findViewById(R.id.cancel);

                        TextView favoritesDialogTitle = (TextView) dialogView.findViewById(R.id.favoritesDialogTitle);
                        if (list.isEmpty()) {
                            favoritesDialogTitle.setVisibility(View.GONE);
                            listaulu.setVisibility(View.GONE);
                            addGameButton.setVisibility(View.GONE);
                        } else {
                            adapter = new ExpandableListViewAdapter(getApplicationContext(), list.get(0), list, listaulu);
                            listaulu.setAdapter(adapter);
                        }


                        createButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(search.this);
                                final LayoutInflater inflater = search.this.getLayoutInflater();
                                final View dialogView = inflater.inflate(R.layout.create_new_favorite, null);
                                builder.setView(dialogView);
                                final AlertDialog dialog2 = builder.create();
                                dialog2.show();
                                Button createButton = (Button) dialogView.findViewById(R.id.newGameButton);
                                Button cancelButton = (Button) dialogView.findViewById(R.id.cancel);
                                final TextView name = (TextView) dialogView.findViewById(R.id.newListName);

                                createButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String nameOfFavorite = name.getText().toString();
                                        //list can't be empty, and can't contain %<>@#$|
                                        if (nameOfFavorite.isEmpty() || nameOfFavorite.replace(" ", "").isEmpty()) {
                                            Toast.makeText(search.this, "nazwa nie może być pusta", Toast.LENGTH_LONG).show();
                                        } else if (nameOfFavorite.contains("%") || nameOfFavorite.contains(">") ||
                                                nameOfFavorite.contains("@") || nameOfFavorite.contains("<") ||
                                                nameOfFavorite.contains("#") || nameOfFavorite.contains("|") ||
                                                nameOfFavorite.contains("$") || nameOfFavorite.contains("'")) {
                                            Toast.makeText(search.this, "nazwa nie może zawierać:\n%<>@#$|'", Toast.LENGTH_LONG).show();
                                        } else {
                                            if (dbFavoritesHelper.favoriteExist(nameOfFavorite))
                                                Toast.makeText(search.this, "taka nazwa listy ulubionych już istnieje", Toast.LENGTH_SHORT).show();
                                            else {
                                                //create new list of favorites
                                                dbFavoritesHelper.createFavorites(nameOfFavorite, game);

                                                Toast.makeText(getApplicationContext(), "Lista została stworzona", Toast.LENGTH_SHORT).show();
                                                dialog2.dismiss();
                                                dialog1.dismiss();


                                                //Toast.makeText(list.this, "nowa lista ulubionych została utworzona",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });


                                cancelButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                    }
                                });
                            }
                        });
                        addGameButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String favoritesName = "";
                                if (adapter != null)
                                    favoritesName = adapter.getGroup(0).toString();

                                if (dbFavoritesHelper.gameInFavoriteExists(favoritesName, game))
                                    Toast.makeText(search.this, "ta zabawa już znajduje sie na tej liście ulubionych", Toast.LENGTH_SHORT).show();
                                else {
                                    dbFavoritesHelper.addGametoFavorite(favoritesName, game);
                                    Toast.makeText(search.this, "zabawa została dodana", Toast.LENGTH_SHORT).show();
                                    dialog1.dismiss();
                                }
                            }
                        });

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                        break;
                    }
                    case 1:
                    {
                        final String game = list.get(position);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(search.this);
                        final LayoutInflater inflater = search.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);

                        final Button acceptButton = (Button) dialogView.findViewById(R.id.accept);
                        final Button cancelButton = (Button) dialogView.findViewById(R.id.cancel);

                        builder.setView(dialogView);
                        final AlertDialog dialog = builder.create();
                        final DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
                        acceptButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dbHelper.remove(game);
                                list.remove(position);
                                //Toast.makeText(getApplicationContext(),listFavorites.get(position),Toast.LENGTH_SHORT).show();


                                //update list of favorites
                                appAdapter.notifyDataSetChanged();

                                dialog.dismiss();
                            }
                        });

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });



                        dialog.show();
                        break;
                    }
                }
                return false;
            }
        });
        
        

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
                    list = new ArrayList<>();
                    appAdapter.notifyDataSetChanged();
                    textNoFavorites.setText(R.string.noFound);
                }
                else
                {
                    DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());

                    list = dbHelper.find(toFind, box1, box2);

                    if(!list.isEmpty())
                    {
                        appAdapter.notifyDataSetChanged();
                        textNoFavorites.setText("");
                    }
                    else
                    {
                        //Toast.makeText(search.this, "ooooooooooo",Toast.LENGTH_LONG).show();
                        appAdapter.notifyDataSetChanged();
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
                openGame.putExtra("kategoria", "search");
                startActivity(openGame);
            }
        });


        final ImageButton wholeListButton = (ImageButton) findViewById(R.id.wholeListButton);
        //final ImageButton searchButton = (ImageButton) findViewById(R.id.findButton);
        final ImageButton favoritesButton = (ImageButton) findViewById(R.id.favoritesButton);

        //show the whole list
        wholeListButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "all");
                //openList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                NavUtils.navigateUpFromSameTask(search.this);
                //finish();
                startActivity(openList);

            }
        });

        //show favorites list
        favoritesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openListOfFavorites = new Intent(getApplicationContext(), listOfFavorites.class);
                NavUtils.navigateUpFromSameTask(search.this);
                //finish();
                startActivity(openListOfFavorites);
            }
        });

        /*
        //show search engine
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openSearch = new Intent(getApplicationContext(), search.class);
                startActivity(openSearch);
            }
        });
        */

    }

    @Override
    protected void onResume() {
        super.onResume();


        final SettingsService sService = new SettingsService(getApplicationContext());
        final int currentTextSize = sService.getTextSize();
        //Toast.makeText(getApplicationContext(),currentTextSize,Toast.LENGTH_SHORT).show();

        final CheckBox titleBox = (CheckBox) findViewById(R.id.titlesCheckbox);
        final CheckBox textBox = (CheckBox) findViewById(R.id.textsCheckbox);
        final EditText searchSpace = (EditText) findViewById(R.id.searchSpace);

        if(currentTextSize != textSize)
        {
            textSize = currentTextSize;
            textBox.setTextSize(textSize);
            titleBox.setTextSize(textSize);
            searchSpace.setTextSize(textSize);
            listOfFound.setAdapter(appAdapter);
        }
    }

    /*
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        //Toast.makeText(search.this,"onConfig",Toast.LENGTH_SHORT).show();
        //Toast.makeText(search.this,list.get(0),Toast.LENGTH_SHORT).show();
        listOfFound.setAdapter(appAdapter);
        listOfFound.setMenuCreator(creator);
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
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
        if(id == R.id.action_settings)
        {
            Intent openSettings = new Intent(getApplicationContext(), settings.class);
            //openSettings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //NavUtils.navigateUpFromSameTask(search.this);
            //finish();
            startActivity(openSettings);
        }
        else
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            // menu type count
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            // current menu type
            AddedGamesService addedGamesService = new AddedGamesService(getApplicationContext());
            if(addedGamesService.gameExist(list.get(position)))
                return 1;
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            //ApplicationInfo item = getItem(position);
            //holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
            holder.tv_name.setText(list.get(position));
            holder.tv_name.setTextSize(textSize);
            return convertView;
        }
    }
    class ViewHolder {
        TextView tv_name;

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(this);
        }
    }


}
