package com.example.pc.pogodne;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class settings extends AppCompatActivity {

    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Button plus = (Button) findViewById(R.id.plusButton);
        final Button minus = (Button) findViewById(R.id.minusButton);
        final TextView textSizeText = (TextView) findViewById(R.id.textSizeText);
        final TextView report= (TextView) findViewById(R.id.reportText);
        final Button send = (Button) findViewById(R.id.reportButton);

        final SettingsService sService = new SettingsService(getApplicationContext());
        textSize = sService.getSize();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Ustawienia");

        //actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        //actionBar.setDisplayShowHomeEnabled(true);

        textSizeText.setTextSize(textSize);
        report.setTextSize(textSize);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( textSize < 24 )
                {
                    sService.setSize( textSize + 2 );
                    textSize += 2;
                }
                textSizeText.setTextSize(textSize);
                report.setTextSize(textSize);

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( textSize > 10 )
                {
                    sService.setSize( textSize - 2 );
                    textSize -= 2;
                }
                textSizeText.setTextSize(textSize);
                report.setTextSize(textSize);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(settings.this);
                builder.setCancelable(true);
                builder.setView(R.layout.send_report);

                builder.setPositiveButton(
                        R.string.send,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String text = report.getText().toString();
                                if(text.replace(" ", "").equals(""))
                                {
                                    Toast.makeText(settings.this, "Wiadomość nie może być pusta", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String[] to = {getString(R.string.mail)};
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setData(Uri.parse("mailto:"));
                                    intent.putExtra(Intent.EXTRA_EMAIL, to);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Zgłoszenie - aplikacja");
                                    intent.putExtra(Intent.EXTRA_TEXT, text);

                                    intent.setType("message/rfc822");
                                    Intent chooser = Intent.createChooser(intent, "Wybierz aplikację z której wyślesz maila");
                                    startActivity(chooser);
                                }
                            }
                        });

                builder.setNegativeButton(
                        R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
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
        return super.onOptionsItemSelected(item);
    }

}
