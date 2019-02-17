package com.example.pc.pogodne;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



public class wyswietlenie extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wyswietlenie);

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
        } catch (IOException e) {
            Toast.makeText(wyswietlenie.this, "Error9".toString(), Toast.LENGTH_LONG).show();
        }


        final TextView nazwaZabawy = (TextView) findViewById(R.id.tytuł);
        final TextView text = (TextView) findViewById(R.id.text);

        nazwaZabawy.setTextSize(tekstSize + 6);
        text.setTextSize(tekstSize);

        final String zabawa = getIntent().getStringExtra("zabawa");

        final File tmpfile = new File(this.getFilesDir(), "tmpfile");

        try {
            FileOutputStream fos = new FileOutputStream(tmpfile);
            byte[] buffer = zabawa.getBytes();
            fos.write(buffer);
            fos.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error13".toString(), Toast.LENGTH_LONG).show();
        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        myToolbar.setTitle(zabawa);


        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowHomeEnabled(true);

        String fileName = "nazwy.txt";
        try {
            InputStream stream = getAssets().open(fileName);

            String tekst = "";
            nazwaZabawy.setText(zabawa);

            ObslugaPliku op = new ObslugaPliku();
            tekst = op.tekst(stream, zabawa);
            text.setText(tekst);
        } catch (IOException ex) {
            Toast.makeText(this, "Error10".toString(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ulu, menu);
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

        if (id == R.id.action_search) {
            Intent otworz_wyszukiwanie = new Intent(getApplicationContext(), search.class);
            startActivity(otworz_wyszukiwanie);
        } else if (id == R.id.action_settings) {
            Intent otworz_ustawienia = new Intent(getApplicationContext(), settings.class);
            startActivity(otworz_ustawienia);
        }
        else
            if(id == R.id.ulubione)
            {

                final AlertDialog.Builder builder = new AlertDialog.Builder(wyswietlenie.this);
                final LayoutInflater inflater = wyswietlenie.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dodaj_do_ulubionych, null);
                builder.setView(dialogView);
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                final Button stworz = (Button) dialogView.findViewById(R.id.stworzUlu);
                final EditText nazwa =(EditText) dialogView.findViewById(R.id.nazwaUlu);
                final ListView listaulu = (ListView) dialogView.findViewById(R.id.listaUlu);

                final File ulufile = new File(wyswietlenie.this.getFilesDir(), "ulu");
                final File tmpfile = new File(wyswietlenie.this.getFilesDir(), "tmpfile");

                final ObslugaPliku op = new ObslugaPliku();
                final ArrayList<String> lista = op.listaulu(ulufile);
                final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(wyswietlenie.this,android.R.layout.simple_list_item_1, lista);

                AlertDialog dialog = builder.create();
                dialog.show();

                listaulu.setAdapter(arrayAdapter);


                stworz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nazwaulu = nazwa.getText().toString();
                        if(nazwaulu.isEmpty() || nazwaulu.replace(" ", "").isEmpty())
                        {
                            Toast.makeText(wyswietlenie.this, "nazwa nie może być pusta".toString(),Toast.LENGTH_LONG).show();
                        }
                        else
                            if(nazwaulu.contains("%")||nazwaulu.contains(">")||nazwaulu.contains("@")||nazwaulu.contains("<")||nazwaulu.contains("#")||nazwaulu.contains("|")||nazwaulu.contains("$"))
                            {
                                Toast.makeText(wyswietlenie.this, "nazwa nie może zawierać:\n%<>@#$|".toString(),Toast.LENGTH_LONG).show();
                            }
                            else
                                {
                                    ObslugaPliku op = new ObslugaPliku();

                                    final File ulufile = new File(wyswietlenie.this.getFilesDir(), "ulu");

                                    final File tmpfile = new File(wyswietlenie.this.getFilesDir(), "tmpfile");

                                    String zabawa = null;
                                    try {
                                        FileInputStream stream = new FileInputStream(tmpfile);
                                        int size = stream.available();
                                        byte[] buffer = new byte[size];
                                        stream.read(buffer);
                                        stream.close();
                                        zabawa = new String(buffer);
                                    }
                                    catch (IOException e)
                                    {
                                        Toast.makeText(wyswietlenie.this, "Error22".toString(),Toast.LENGTH_LONG).show();

                                    }

                                    if(op.czyistnieje(ulufile, nazwaulu))
                                    {
                                        Toast.makeText(wyswietlenie.this, "taka nazwa listy ulubionych już istnieje".toString(),Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        if (op.dodajnowaulu(ulufile,nazwaulu, zabawa) == 1) {
                                            Toast.makeText(wyswietlenie.this, "zabawa została zapisana do nowopowstałej listy".toString(), Toast.LENGTH_SHORT).show();
                                            arrayAdapter.add(nazwaulu);
                                        }
                                            else
                                        {
                                            Toast.makeText(wyswietlenie.this, Integer.toString(op.dodajnowaulu(ulufile,nazwaulu, zabawa)),Toast.LENGTH_LONG).show();
                                        }
                                    }
                            }
                    }
                });






                listaulu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String nazwaulu = lista.get(i);

                        String zabawa = null;
                        try {
                            FileInputStream stream = new FileInputStream(tmpfile);
                            int size = stream.available();
                            byte[] buffer = new byte[size];
                            stream.read(buffer);
                            stream.close();
                            zabawa = new String(buffer);
                        }
                        catch (IOException e)
                        {
                            Toast.makeText(wyswietlenie.this, "Error23".toString(),Toast.LENGTH_LONG).show();
                        }
                        int tmp = op.dodajdoulu(ulufile, nazwaulu, zabawa);
                        if(tmp == 0)
                        {
                            Toast.makeText(wyswietlenie.this, "Error24".toString(),Toast.LENGTH_LONG).show();
                        }
                        else
                        if(tmp == 1)
                        {
                            Toast.makeText(wyswietlenie.this, "ta zabawa już znajduje sie na tej liście ulubionych".toString(),Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(wyswietlenie.this, "zabawa została dodana".toString(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                final TextView text = (TextView) dialog.findViewById(R.id.textUlu);
                if(lista.size() == 0)
                {
                    text.setText("Nie posiadasz jeszcze rzadnej listy ulubionych");
                }
                //Toast.makeText(wyswietlenie.this, "ooo".toString(),Toast.LENGTH_LONG).show();
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_wyswietlenie);

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
        } catch (IOException e) {
            Toast.makeText(wyswietlenie.this, "Error9".toString(), Toast.LENGTH_LONG).show();
        }


        final TextView nazwaZabawy = (TextView) findViewById(R.id.tytuł);
        final TextView text = (TextView) findViewById(R.id.text);

        nazwaZabawy.setTextSize(tekstSize + 6);
        text.setTextSize(tekstSize);

        final String zabawa = getIntent().getStringExtra("zabawa");

        final File tmpfile = new File(this.getFilesDir(), "tmpfile");

        try {
            FileOutputStream fos = new FileOutputStream(tmpfile);
            byte[] buffer = zabawa.getBytes();
            fos.write(buffer);
            fos.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error13".toString(), Toast.LENGTH_LONG).show();
        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        myToolbar.setTitle(zabawa);

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowHomeEnabled(true);

        String fileName = "nazwy.txt";
        try {
            InputStream stream = getAssets().open(fileName);

            String tekst = "";
            nazwaZabawy.setText(zabawa);

            ObslugaPliku op = new ObslugaPliku();
            tekst = op.tekst(stream, zabawa);
            text.setText(tekst);
        } catch (IOException ex) {
            Toast.makeText(this, "Error10".toString(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }


    }
}
