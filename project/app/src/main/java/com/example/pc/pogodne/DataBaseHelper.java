package com.example.pc.pogodne;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper  extends SQLiteOpenHelper {
    private SQLiteDatabase myBase;
    private static final String dataBaseName = "dane";
    private final Context appContext;
    private String dataBasePath;
    public static final int dataBAseVersion = 1;

    public  DataBaseHelper(Context context)
    {
        super(context, dataBaseName, null, dataBAseVersion);
        this.appContext = context;
        dataBasePath = context.getDatabasePath(dataBaseName).getPath();
    }

    public void createDataBase() throws IOException {
        if(!checkDataBase())
        {
            this.getReadableDatabase();
            copyDataBase();
            this.close();
        }
    }
    private boolean checkDataBase()
    {
        File dataBaseFile = new File(dataBasePath + dataBaseName);
        return dataBaseFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream inputStream = appContext.getAssets().open(dataBaseName);
        OutputStream outputStream = new FileOutputStream(dataBasePath);
        byte[] buffer = new byte[1024];
        int memLength;
        while ((memLength = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer, 0 , memLength);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }





    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
