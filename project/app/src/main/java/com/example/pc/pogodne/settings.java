package com.example.pc.pogodne;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Button plus = (Button) findViewById(R.id.plus);
        final Button minus = (Button) findViewById(R.id.minus);
        final TextView czcionka = (TextView) findViewById(R.id.czcionka);
        final TextView report= (TextView) findViewById(R.id.report);
        final Button send = (Button) findViewById(R.id.reportButton);

        String filename = "settingsFile";
        final File file = new File(this.getFilesDir(), filename);

        int tekstSize = 15;

        if(!file.exists())
        {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = "15".getBytes();
                fos.write(buffer);
                fos.close();
            }
            catch (IOException e)
            {
                Toast.makeText(this, "Error13".toString(),Toast.LENGTH_LONG).show();
            }
        }
        else
        {
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
                Toast.makeText(settings.this, "Error14".toString(),Toast.LENGTH_LONG).show();
            }
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        myToolbar.setTitle("Ustawienia");

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowHomeEnabled(true);

        czcionka.setTextSize(tekstSize);
        report.setTextSize(tekstSize);

        final int tS = tekstSize;

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmpSize = 15;

                try {
                    FileInputStream stream = new FileInputStream(file);
                    int size = stream.available();
                    byte[] buffer = new byte[size];
                    stream.read(buffer);
                    stream.close();
                    String plik = new String(buffer);
                    tmpSize = Integer.parseInt(plik);
                }
                catch (IOException e)
                {
                    Toast.makeText(settings.this, "Error15".toString(),Toast.LENGTH_LONG).show();
                }

                if(tmpSize < 24)
                {
                    czcionka.setTextSize(tmpSize + 2);
                    report.setTextSize(tmpSize + 2);

                    String string = Integer.toString(tmpSize + 2);

                    FileOutputStream outputStream;
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buffer = string.getBytes();
                        fos.write(buffer);
                        fos.close();
                    }
                    catch (IOException e)
                    {
                        Toast.makeText(settings.this, "Error16".toString(),Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmpSize = 15;

                try {
                    FileInputStream stream = new FileInputStream(file);
                    int size = stream.available();
                    byte[] buffer = new byte[size];
                    stream.read(buffer);
                    stream.close();
                    String plik = new String(buffer);
                    tmpSize = Integer.parseInt(plik);
                }
                catch (IOException e)
                {
                    Toast.makeText(settings.this, "Error17".toString(),Toast.LENGTH_LONG).show();
                }

                if(tmpSize > 10)
                {
                    czcionka.setTextSize(tmpSize - 2);
                    report.setTextSize(tmpSize - 2);

                    String string = Integer.toString(tmpSize - 2);

                    FileOutputStream outputStream;
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buffer = string.getBytes();
                        fos.write(buffer);
                        fos.close();
                    }
                    catch (IOException e)
                    {
                        Toast.makeText(settings.this, "Error18".toString(),Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        return super.onOptionsItemSelected(item);
    }

}
