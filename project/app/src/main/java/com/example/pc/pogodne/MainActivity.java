package com.example.pc.pogodne;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting toolbar
        //toolbar with title
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        //define buttons
        final Button przyciskLista = (Button) findViewById(R.id.wholeListButton);
        final Button przyciskTańce = (Button) findViewById(R.id.dancesButton);
        final Button przyciskPiosenki = (Button) findViewById(R.id.singsButton);
        final Button przyciskRywalizacja = (Button) findViewById(R.id.competitionsButton);
        final Button przyciskIntegracyjne = (Button) findViewById(R.id.integralsButton);
        final Button przyciskInne = (Button) findViewById(R.id.othersButton);

        final Button przyciskUstawienia = (Button) findViewById(R.id.settingsButton);
        final Button przyciskSzukaj = (Button) findViewById(R.id.findButton);
        final Button przyciskUlubione = (Button) findViewById(R.id.favoritesButton);
        final Button zabawanadzis = (Button) findViewById(R.id.gameOfTheDayButton);

        //setting what buttons do

        //show the whole list
        przyciskLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otworz_liste = new Intent(getApplicationContext(), list.class);
                otworz_liste.putExtra("kategoria", "all");
                startActivity(otworz_liste);

            }
        });

        //show games in category "tańce"
        przyciskTańce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otworz_liste = new Intent(getApplicationContext(), list.class);
                otworz_liste.putExtra("kategoria", "tańce");
                startActivity(otworz_liste);

            }
        });

        //show games in category "piosenki"
        przyciskPiosenki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otworz_liste = new Intent(getApplicationContext(), list.class);
                otworz_liste.putExtra("kategoria", "piosenki");
                startActivity(otworz_liste);

            }
        });

        //show games in category "rywalizacja"
        przyciskRywalizacja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otworz_liste = new Intent(getApplicationContext(), list.class);
                otworz_liste.putExtra("kategoria", "rywalizacja");
                startActivity(otworz_liste);

            }
        });

        //display games in category "integracyjne"
        przyciskIntegracyjne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otworz_liste = new Intent(getApplicationContext(), list.class);
                otworz_liste.putExtra("kategoria", "integracyjne");
                startActivity(otworz_liste);

            }
        });

        //show games in category "inne"
        przyciskInne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otworz_liste = new Intent(getApplicationContext(), list.class);
                otworz_liste.putExtra("kategoria", "inne");
                startActivity(otworz_liste);

            }
        });

        //show searching
        przyciskSzukaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otworz_liste = new Intent(getApplicationContext(), search.class);
                startActivity(otworz_liste);
            }
        });

        //show settings
        przyciskUstawienia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otworz_ustawienia = new Intent(getApplicationContext(), settings.class);
                startActivity(otworz_ustawienia);
            }
        });

        //show favorites list
        przyciskUlubione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otworz_liste = new Intent(getApplicationContext(), listOfFavorites.class);

                startActivity(otworz_liste);

            }
        });

        //show random play in current day
        zabawanadzis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otworz_liste = new Intent(getApplicationContext(), display.class);
                String zabawa = null;
                int seed = Calendar.getInstance().get(Calendar.YEAR)*366+Calendar.getInstance().get(Calendar.MONTH)*31+Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                FileHelper op = new FileHelper();
                    String fileName = "nazwy.txt";

                    try {
                        InputStream stream = getAssets().open(fileName);
                        zabawa = op.randzabawa(stream, seed);
                        //Toast.makeText(MainActivity.this, op.randzabawa(stream, seed+1).toString(), Toast.LENGTH_SHORT).show();
                    } catch (IOException ex) {
                        Toast.makeText(MainActivity.this, "Error36".toString(), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }

                otworz_liste.putExtra("zabawa", zabawa);
                startActivity(otworz_liste);

            }
        });


        //if settingsFile doesn't exist, set the default text size at 15sp
        //and write it to settingFile
        String filename = "settingsFile";
        File file = new File(this.getFilesDir(), filename);

        if (!file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = "15".getBytes();
                fos.write(buffer);
                fos.close();
            } catch (IOException e) {
                Toast.makeText(this, "Error4".toString(), Toast.LENGTH_LONG).show();
            }
        }

    }
}


