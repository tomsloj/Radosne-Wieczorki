package com.tomsloj.pc.pogodne;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Collections;

public class displayFavorite extends AppCompatActivity {

    String nameOfFavorite = "";
    Toolbar myToolbar;
    SwipeMenuListView listView;
    int favoriteID = -1;
    int listSize;
    int textSize;
    ArrayList<String> list;
    ArrayList<String> favoriteList;
    ArrayList<Boolean> moovables;
    AppAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_favorite);

        nameOfFavorite = getIntent().getStringExtra("ulu");
        favoriteID = (int) getIntent().getIntExtra("ID", -2);


        myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);

        final SettingsService sService = new SettingsService(getApplicationContext());
        textSize = sService.getTextSize();


        listView = (SwipeMenuListView)findViewById(R.id.listOfGames);


        final DataBaseFavorites dbFavorites = new DataBaseFavorites(displayFavorite.this);

        favoriteList = getIntent().getStringArrayListExtra("list");

        listSize = favoriteList.size();

        list = dbFavorites.getGamesInFavorite(nameOfFavorite);

        moovables = new ArrayList<Boolean>(Collections.nCopies(list.size(), false));

        arrayAdapter = new AppAdapter();
        listView.setAdapter(arrayAdapter);

        if(list.size() == 0)
            listView.setEmptyView(findViewById(R.id.emptyListText));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String gameName = list.get(i);
                Intent openGame = new Intent(getApplicationContext(), display.class);
                openGame.putExtra("zabawa", gameName);
                openGame.putExtra("playlist", nameOfFavorite );
                openGame.putExtra("kategoria", "ulu");
                startActivity(openGame);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "edit" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(getResources().getColor(R.color.iconbackground)));
                // set item width
                editItem.setWidth(130);
                // set item title
                editItem.setIcon(R.drawable.edit);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
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
            }
        };


        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index)
                {
                    case 0:
                    {
                        moovables.set(position, true);
                        listView.setAdapter(arrayAdapter);
                        break;
                    }
                    case 1:
                    {
                        final String game = list.get(position);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(displayFavorite.this);
                        final LayoutInflater inflater = displayFavorite.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);


                        builder.setView(dialogView);
                        final AlertDialog dialog1 = builder.create();
                        dialog1.show();

                        Button acceptButton = (Button) dialogView.findViewById(R.id.accept);
                        Button cancelButton = (Button) dialogView.findViewById(R.id.cancel);

                        acceptButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(getApplicationContext(), nameOfFavorite + "  " + game, Toast.LENGTH_SHORT).show();
                                dbFavorites.deleteGame(nameOfFavorite, game);
                                Toast.makeText(getApplicationContext(), "Zabawa została usunięta z listy", Toast.LENGTH_SHORT).show();
                                list = dbFavorites.getGamesInFavorite(nameOfFavorite);
                                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(displayFavorite.this, android.R.layout.simple_list_item_1, list) {
                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);
                                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                        tv.setBackground(getResources().getDrawable(R.drawable.list_background));
                                        tv.setTextSize(textSize);
                                        tv.setTextColor(Color.BLACK);

                                        return view;
                                    }
                                };
                                listView.setAdapter(arrayAdapter);
                                dialog1.dismiss();
                            }
                        });
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                    }

                }
                return false;
            }
        });


        final ImageButton wholeListButton = (ImageButton) findViewById(R.id.wholeListButton);
        final ImageButton searchButton = (ImageButton) findViewById(R.id.findButton);
        final ImageButton favoritesButton = (ImageButton) findViewById(R.id.favoritesButton);


        /*
         * set what buttons do
         */

        //show the whole list
        wholeListButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "all");
                NavUtils.navigateUpFromSameTask(displayFavorite.this);
                startActivity(openList);
            }
        });

        //show search engine
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openSearch = new Intent(getApplicationContext(), search.class);
                startActivity(openSearch);
                NavUtils.navigateUpFromSameTask(displayFavorite.this);
            }
        });

        //show favorites list
        favoritesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openListOfFavorites = new Intent(getApplicationContext(), listOfFavorites.class);
                NavUtils.navigateUpFromSameTask(displayFavorite.this);
                startActivity(openListOfFavorites);

            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        final SettingsService sService = new SettingsService(getApplicationContext());
        final int currentTextSize = sService.getTextSize();

        DataBaseFavorites dbFavorite = new DataBaseFavorites(displayFavorite.this);

        ArrayList<String> favoriteList = dbFavorite.getFavoritesList();

        if(listSize != favoriteList.size())
        {
            finish();
        }
        else
        {
            boolean toChange = false;
            String newNameOfFavorite = sService.getNewNameOfFavorite();
            if( !nameOfFavorite.equals(newNameOfFavorite) && !newNameOfFavorite.equals("") )
            {
                nameOfFavorite = newNameOfFavorite;
                toChange = true;
            }

            ArrayList<String> gamesList = dbFavorite.getGamesInFavorite(nameOfFavorite);
            if(list.size() != gamesList.size()) {
                list = gamesList;
                toChange = true;
            }

            myToolbar.setTitle(nameOfFavorite);
            if(currentTextSize != textSize || toChange)
            {
                textSize = currentTextSize;
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(displayFavorite.this, android.R.layout.simple_list_item_1, list) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        tv.setBackground(getResources().getDrawable( R.drawable.list_background ));
                        tv.setTextSize(currentTextSize);
                        tv.setTextColor(Color.BLACK);

                        return view;
                    }
                };
                listView.setAdapter(arrayAdapter);
            }

        }

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
            NavUtils.navigateUpFromSameTask(displayFavorite.this);
            startActivity(openSearch);
        }
        else
        if(id == R.id.action_settings)
        {
            Intent openSettings = new Intent(getApplicationContext(), settings.class);
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
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            // current menu type
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(moovables.get(position)) {
                if (convertView == null) {
                    convertView = View.inflate(getApplicationContext(),
                            R.layout.item_list_moovable, null);
                    new ViewHolder(convertView, position);
                }
                ViewHolder holder = (ViewHolder) convertView.getTag();
                //ApplicationInfo item = getItem(position);
                //holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
                holder.tv_name.setText(list.get(position));
                holder.tv_name.setTextSize(textSize);
                return convertView;
            }
            else
            {
                if (convertView == null) {
                    convertView = View.inflate(getApplicationContext(),
                            R.layout.item_list_app, null);
                    new ViewHolder(convertView, position);
                }
                ViewHolder holder = (ViewHolder) convertView.getTag();
                //ApplicationInfo item = getItem(position);
                //holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
                holder.tv_name.setText(list.get(position));
                holder.tv_name.setTextSize(textSize);
                return convertView;
            }
        }
    }
    class ViewHolder {
        TextView tv_name;
        ImageButton upButton;
        ImageButton downButton;
        ImageButton doneButton;
        DataBaseFavorites dbHelperFavorites = new DataBaseFavorites(getApplicationContext());

        public ViewHolder(View view, final int position) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(this);
            if(moovables.get(position))
            {
                upButton = (ImageButton) view.findViewById(R.id.upButton);
                upButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position > 0)
                        {
                            String aboveItem = list.get(position-1);
                            String game = list.get(position);
                            list.set(position - 1, list.get(position));
                            list.set(position, aboveItem);
                            dbHelperFavorites.upGame(nameOfFavorite, game);
                            boolean tmp = moovables.get(position);
                            moovables.set(position, moovables.get(position - 1));
                            moovables.set(position - 1, tmp);
                            listView.setAdapter(arrayAdapter);
                        }
                    }
                });
                downButton = (ImageButton)view.findViewById(R.id.downButton);
                downButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position < list.size()-1)
                        {
                            String aboveItem = list.get(position + 1);
                            String game = list.get(position);
                            list.set(position + 1, list.get(position));
                            list.set(position, aboveItem);
                            boolean tmp = moovables.get(position);
                            moovables.set(position, moovables.get(position + 1));
                            moovables.set(position + 1, tmp);
                            dbHelperFavorites.downGame(nameOfFavorite, game);
                            //Toast.makeText(getApplicationContext(), nameOfFavorite + " " + game, Toast.LENGTH_SHORT).show();
                            listView.setAdapter(arrayAdapter);
                        }
                    }
                });
                doneButton = (ImageButton) view.findViewById(R.id.doneButton);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moovables.set(position, false);
                        listView.setAdapter(arrayAdapter);
                    }
                });
            }

        }
    }


}
