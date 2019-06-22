package com.example.pc.pogodne;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class settings extends AppCompatActivity
{
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Button plus = (Button) findViewById(R.id.plusButton);
        final Button minus = (Button) findViewById(R.id.minusButton);
        final Button send = (Button) findViewById(R.id.reportButton);
        final Button addGameButton = (Button) findViewById(R.id.addGameButton);

        final TextView textSizeText = (TextView) findViewById(R.id.textSizeText);
        final TextView report = (TextView) findViewById(R.id.reportText);
        final TextView addGame = (TextView) findViewById(R.id.addGameText);

        //set size of text
        final SettingsService sService = new SettingsService(getApplicationContext());
        textSize = sService.getTextSize();
        textSizeText.setTextSize(textSize);
        report.setTextSize(textSize);
        addGame.setTextSize(textSize);

        //set toolbar with title and back button
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ustawienia");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);

        //increase size of text
        plus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if( textSize < 24 )
                {
                    sService.setTextSize( textSize + 2 );
                    textSize += 2;
                }
                textSizeText.setTextSize(textSize);
                report.setTextSize(textSize);
                addGame.setTextSize(textSize);
            }
        });

        //decrease size of text
        minus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if( textSize > 10 )
                {
                    sService.setTextSize( textSize - 2 );
                    textSize -= 2;
                }
                textSizeText.setTextSize(textSize);
                report.setTextSize(textSize);
                addGame.setTextSize(textSize);
            }
        });

        //send report
        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //create window where user can write message
                final LayoutInflater inflater = LayoutInflater.from(settings.this);
                final View dialogView = inflater.inflate(R.layout.send_report, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(settings.this);
                final EditText message = (EditText) dialogView.findViewById(R.id.message);
                builder.setView(dialogView);
                //"send" button
                builder.setPositiveButton
                (
                    R.string.send,
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            String text =  message.getText().toString(); //text to send
                            //if text is empty
                            if(text.replace(" ", "").equals(""))
                            {
                                Toast.makeText(settings.this, "Wiadomość nie może być pusta", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String[] recipient = {getString(R.string.mail)};
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setData(Uri.parse("mailto:"));
                                intent.putExtra(Intent.EXTRA_EMAIL, recipient);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Zgłoszenie - aplikacja");
                                intent.putExtra(Intent.EXTRA_TEXT, text);
                                intent.setType("message/rfc822");
                                //choosing application which user wants to use to send report
                                Intent chooser = Intent.createChooser(intent, "Wybierz aplikację z której wyślesz maila");
                                startActivity(chooser);
                            }
                        }
                    }
                );
                builder.setNegativeButton
                (
                    R.string.cancel,
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.cancel();
                        }
                    }
                );
                builder.create().show();
            }
        });


        //add new game
        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = LayoutInflater.from(settings.this);
                final View dialogView = inflater.inflate(R.layout.add_game, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(settings.this);

                final TextView gameName = dialogView.findViewById(R.id.nameOfAddedGame);
                final TextView gameText = dialogView.findViewById(R.id.textOfAddedGame);
                final RadioGroup radioGroup = dialogView.findViewById(R.id.categoryGroup);
                final CheckBox shareBox = dialogView.findViewById(R.id.shareGameBox);

                builder.setView(dialogView);

                builder.setPositiveButton
                        (
                                R.string.addGame,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        int radioId = radioGroup.getCheckedRadioButtonId();
                                        RadioButton radioButton = (RadioButton) dialogView.findViewById(radioId);

                                        String game = gameName.getText().toString();
                                        String text = gameText.getText().toString();
                                        CharSequence charSequence = radioButton.getText();
                                        String category = charSequence.toString().toLowerCase();

                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                                        AddedGamesService addedGamesService = new AddedGamesService(getApplicationContext());

                                        if(!dataBaseHelper.getText(game).equals("Error 73") && !addedGamesService.gameExist(game))
                                        {
                                            Toast.makeText(settings.this, "Ta zabawa jest już dodana", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            //Toast.makeText(settings.this, Long.toString(addedGamesService.addGame(category, game, text)),Toast.LENGTH_SHORT).show();
                                            dataBaseHelper.addGame(category, game, text);

                                            if( shareBox.isChecked() )
                                            {
                                                String[] recipient = {getString(R.string.mail)};
                                                Intent intent = new Intent(Intent.ACTION_SEND);
                                                intent.setData(Uri.parse("mailto:"));
                                                intent.putExtra(Intent.EXTRA_EMAIL, recipient);
                                                intent.putExtra(Intent.EXTRA_SUBJECT, "Nowa zabawa");
                                                intent.putExtra(Intent.EXTRA_TEXT, "kategoria: " + category +
                                                        "\nNazwa: " + game + "\n" + text);
                                                intent.setType("message/rfc822");
                                                //choosing application which user wants to use to send report
                                                Intent chooser = Intent.createChooser(intent, "Wybierz aplikację z której wyślesz maila");
                                                startActivity(chooser);
                                            }
                                        }




                                    }
                                }
                        );
                builder.setNegativeButton
                        (
                                R.string.cancel,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        dialog.cancel();
                                    }
                                }
                        );
                builder.create().show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

}
