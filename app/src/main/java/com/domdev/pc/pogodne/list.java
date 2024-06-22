package com.domdev.pc.pogodne;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

public class list extends AppCompatActivity {

    SwipeMenuListView listView;
    int textSize;
    String category;
    ExpandableListViewAdapter adapter = null;
    ArrayList<String> arrayList;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //get category of list
        category = getIntent().getStringExtra("kategoria");

        //create toolbar
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        if(category== null||category.equals("all"))
        {
            myToolbar.setTitle("Lista zabaw");
            category = "all";
        }
        else
        {
           myToolbar.setTitle(category.substring(0, 1).toUpperCase() + category.substring(1));
        }
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        //actionBar.setHomeButtonEnabled(true);


        if ( actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        final SettingsService sService = new SettingsService(getApplicationContext());
        textSize = sService.getTextSize();

        listView = (SwipeMenuListView) findViewById(R.id.listview);


        final DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());

        arrayList = dbHelper.getGamesInCategory(category);

        final AppAdapter arrayAdapter = new AppAdapter();
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent openGame = new Intent(getApplicationContext(), display.class);
                openGame.putExtra("zabawa", arrayList.get(i));
                openGame.putExtra("kategoria", category);
                startActivity(openGame);
            }
        });


        SwipeMenuCreator creator = new SwipeMenuCreator() {

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
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0: {
                        final String game = arrayList.get(position);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(list.this);
                        final LayoutInflater inflater = list.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.add_to_favorites, null);

                        //final Button stworz = (Button) dialogView.findViewById(R.id.createFavorite);
                        //final EditText nazwa =(EditText) dialogView.findViewById(R.id.nameOfFavorite);
                        final ExpandableListView listaulu = (ExpandableListView) dialogView.findViewById(R.id.dialogLisOfFavorites);

                        final DataBaseFavorites dbFavoritesHelper = new DataBaseFavorites(list.this);

                        final ArrayList<String> list = dbFavoritesHelper.getFavoritesList();


                        builder.setView(dialogView);
                        final AlertDialog dialog1 = builder.create();
                        dialog1.show();

                        //display info that user hasn't any list of favorites
                        //final TextView text = (TextView) dialog.findViewById(R.id.textChooseExistingFavorite);
                            /*
                            if(list.size() == 0)
                            {
                                text.setText("Nie posiadasz jeszcze żadnej listy ulubionych");
                            }
                            */

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
                                final AlertDialog.Builder builder = new AlertDialog.Builder(list.this);
                                final LayoutInflater inflater = list.this.getLayoutInflater();
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
                                            Toast.makeText(list.this, "nazwa nie może być pusta", Toast.LENGTH_LONG).show();
                                        } else if (nameOfFavorite.contains("%") || nameOfFavorite.contains(">") ||
                                                nameOfFavorite.contains("@") || nameOfFavorite.contains("<") ||
                                                nameOfFavorite.contains("#") || nameOfFavorite.contains("|") ||
                                                nameOfFavorite.contains("$") || nameOfFavorite.contains("'")) {
                                            Toast.makeText(list.this, "nazwa nie może zawierać:\n%<>@#$|'", Toast.LENGTH_LONG).show();
                                        } else {
                                            if (dbFavoritesHelper.favoriteExist(nameOfFavorite))
                                                Toast.makeText(list.this, "taka nazwa listy ulubionych już istnieje", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(list.this, "ta zabawa już znajduje sie na tej liście ulubionych", Toast.LENGTH_SHORT).show();
                                else {
                                    dbFavoritesHelper.addGametoFavorite(favoritesName, game);
                                    Toast.makeText(list.this, "zabawa została dodana", Toast.LENGTH_SHORT).show();
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
                        final String game = arrayList.get(position);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(list.this);
                        final LayoutInflater inflater = list.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);

                        final Button acceptButton = (Button) dialogView.findViewById(R.id.accept);
                        final Button cancelButton = (Button) dialogView.findViewById(R.id.cancel);

                        builder.setView(dialogView);
                        final AlertDialog dialog = builder.create();

                        acceptButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dbHelper.remove(game);
                                arrayList.remove(position);

                                //update list of favorites
                                arrayAdapter.notifyDataSetChanged();

                                //delete game from favorites
                                DataBaseFavorites dbFacorites = new DataBaseFavorites(getApplicationContext());
                                ArrayList<String> list = dbFacorites.getFavoritesList();

                                for ( String favorite:list )
                                {
                                    if( dbFacorites.gameInFavoriteExists(favorite, game) )
                                    {
                                        dbFacorites.deleteGame(favorite, game);
                                    }
                                }



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
                /*
                NavUtils.navigateUpFromSameTask(list.this);
                Intent openList = new Intent(getApplicationContext(), list.class);
                openList.putExtra("kategoria", "all");
                startActivity(openList);
                */
                category = "all";

                myToolbar.setTitle("Lista zabaw");
                arrayList= dbHelper.getGamesInCategory(category);
                listView.setAdapter(arrayAdapter);
            }
        });

        //show search engine
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openSearch = new Intent(getApplicationContext(), search.class);
                NavUtils.navigateUpFromSameTask(list.this);
                //openSearch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //finish();
                startActivity(openSearch);
            }
        });

        //show favorites list
        favoritesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openListOfFavorites = new Intent(getApplicationContext(), listOfFavorites.class);
                NavUtils.navigateUpFromSameTask(list.this);
                startActivity(openListOfFavorites);
            }
        });

    }

    /*
    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DataBaseHelper.dataBaseName);
            DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
            String outFileName = dataBaseHelper.dataBasePath + DataBaseHelper.dataBaseName;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("listActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     */

    @Override
    protected void onResume()
    {
        super.onResume();

        final AppAdapter arrayAdapter = new AppAdapter();

        final SettingsService sService = new SettingsService(getApplicationContext());
        final int currentTextSize = sService.getTextSize();
        if(currentTextSize != textSize)
        {
            textSize = currentTextSize;
            DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());

            final ArrayList<String> arrayList = dbHelper.getGamesInCategory(category);

            /*
            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(list.this, android.R.layout.simple_list_item_1, arrayList) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent)
                {
                    View view = super.getView(position, convertView, parent);
                    view.setBackground(getResources().getDrawable( R.drawable.list_background ));
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    tv.setBackground(getResources().getDrawable( R.drawable.list_background ));
                    tv.setTextSize(textSize);
                    tv.setTextColor(Color.BLACK);

                    return view;
                }
            };
            listView.setDividerHeight(0);
            */
            listView.setAdapter(arrayAdapter);

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
            NavUtils.navigateUpFromSameTask(list.this);
            startActivity(openSearch);
        }
        else
        if(id == R.id.action_settings)
        {
            Intent openSettings = new Intent(getApplicationContext(), settings.class);
            //NavUtils.navigateUpFromSameTask(list.this);
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
            return arrayList.size();
        }

        @Override
        public String getItem(int position) {
            return arrayList.get(position);
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
            if(addedGamesService.gameExist(arrayList.get(position)))
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
            holder.tv_name.setText(arrayList.get(position));
            holder.tv_name.setTextSize(textSize);
            return convertView;
        }
    }
        class ViewHolder {

            TextView tv_name;

            ViewHolder(View view) {
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }


}

