package com.domdev.pc.pogodne;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class FileHelper {
    public static ArrayList<String> titlesInCategory(InputStream stream, String category)
    {
        ArrayList<String> list = new ArrayList<>();

        try
        {
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String wholeFileText = new String(buffer);
            wholeFileText = wholeFileText.replace("\t", "");
            wholeFileText = wholeFileText.replace("\r", "");
            wholeFileText = wholeFileText.replace(Character.toString((char)65279), "");

            String[] division = wholeFileText.split("@");
            for(int i = 0; i < division.length; ++i)
            {
                String[] towPartDivision = division[i].split("%");

                if((towPartDivision.length == 2) && (category.equals("all") || category.equals(towPartDivision[0])))
                {

                    String[] titleAndText = towPartDivision[1].split(">\n");
                    for(int j = 0; j < titleAndText.length; ++j)
                    {
                        String[] podzialnapiewrsze = titleAndText[j].split(">");
                        list.add(podzialnapiewrsze[0]);
                    }
                }
            }
        }
        catch (IOException ex)
        {
        }


        return list;
    }

    public static ArrayList<String> titlesInFavorite(File file, String nazwaplaylisty)
    {
        ArrayList<String> list = new ArrayList<>();

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String wholeFileText = new String(buffer);
            wholeFileText = wholeFileText.replace("\t", "");
            wholeFileText = wholeFileText.replace("\r", "");
            wholeFileText = wholeFileText.replace(Integer.toString(65279), "");

            String[] division = wholeFileText.split("@");
            for(int i = 0; i < division.length; ++i)
            {
                String[] twoPartsDivision = division[i].split("%");
                if(twoPartsDivision.length == 2 && nazwaplaylisty.equals(twoPartsDivision[0]))
                {
                    String[] theSmalestDivision = twoPartsDivision[1].split(">\n");
                    for(int j = 0; j < theSmalestDivision.length; ++j)
                    {
                        list.add(theSmalestDivision[j]);
                    }
                }
            }
        }
        catch (IOException e)
        {
        }
        return list;
    }

    public static ArrayList<String> listOfFavorites(File file)
    {
        ArrayList<String> lista = new ArrayList<>();

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textOfWholeFile = new String(buffer);
            textOfWholeFile = textOfWholeFile.replace("\t", "");
            textOfWholeFile = textOfWholeFile.replace("\r", "");
            textOfWholeFile = textOfWholeFile.replace(Integer.toString(65279), "");

            if(textOfWholeFile.equals(""))
            {
                return lista;
            }

            String[] division = textOfWholeFile.split("@");
            for(int i = 0; i < division.length; ++i)
            {
                String[] twoPartDivision = division[i].split("%");
                lista.add(twoPartDivision[0]);
            }
        }
        catch (IOException e)
        {
        }
        return lista;
    }

    public static boolean doExistInFavoriteList(File file, String favoriteList)
    {
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textOfWholeFile = new String(buffer);
            textOfWholeFile = textOfWholeFile.replace("\t", "");
            textOfWholeFile = textOfWholeFile.replace("\r", "");
            textOfWholeFile = textOfWholeFile.replace(Integer.toString(65279), "");

            String[] division = textOfWholeFile.split("@");
            for(int i = 0; i < division.length; ++i)
            {
                String[] twoPartDivision = division[i].split("%");
                if(twoPartDivision.length == 2 && favoriteList.equals(twoPartDivision[0]))
                {
                    return true;
                }
            }
        }
        catch (IOException e)
        {
        }
        return false;
    }
    //0 jeśli nie udało się dodać
    //1 jeśli już tam było
    //2 jeśli się udało
    public static int addToFavorites(File file, String titleOfFavorite, String gameTitle)
    {
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textOfWholeFile = new String(buffer);
            textOfWholeFile = textOfWholeFile.replace("\t", "");
            textOfWholeFile = textOfWholeFile.replace("\r", "");
            textOfWholeFile = textOfWholeFile.replace(Integer.toString(65279), "");

            String toSave = "";

            boolean was = false;
            boolean anythingWritten = false;
            String[] division = textOfWholeFile.split("@");
            for(int i = 0; i < division.length; ++i)
            {
                String[] twoPartDivision = division[i].split("%");
                if(twoPartDivision.length == 2 && titleOfFavorite.equals(twoPartDivision[0]))
                {
                    String[] theSmalestDivision = twoPartDivision[1].split(">\n");
                    for(int j = 0; j < theSmalestDivision.length; ++j)
                    {
                        if(theSmalestDivision[j].equals(gameTitle))
                        {
                            was = true;
                        }
                    }
                    if(was)
                    {
                        return 1;
                    }
                    else
                    {
                        if(anythingWritten)
                            toSave = toSave + "@" + division[i] + ">\n" + gameTitle;
                        else
                        {
                            anythingWritten = true;
                            toSave = division[i] + ">\n" + gameTitle;
                        }
                    }

                }
                else
                {
                    if(anythingWritten)
                        toSave = toSave + "@" + division[i];
                    else
                    {
                        anythingWritten = true;
                        toSave = division[i];
                    }
                }
            }

            try {
                FileOutputStream fos = new FileOutputStream(file);
                buffer = toSave.getBytes();
                fos.write(buffer);
                fos.close();
                return 2;
            }
            catch (IOException e)
            {
                return 0;
            }
        }
        catch (IOException e)
        {
            return 0;
        }
    }

    //false jeśli nie udało się dodać
    //true jeśli się udało
    public static int createNewFavorite(File file, String titleOfFavorite, String gameTitle)
    {
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textOfWholeFile = new String(buffer);
            textOfWholeFile = textOfWholeFile.replace("\t", "");
            textOfWholeFile = textOfWholeFile.replace("\r", "");
            textOfWholeFile = textOfWholeFile.replace(Integer.toString(65279), "");
            if(!textOfWholeFile.equals(""))
                textOfWholeFile = textOfWholeFile + "@" + titleOfFavorite + "%" + gameTitle;
            else
                textOfWholeFile = titleOfFavorite + "%" + gameTitle;
            try {
                FileOutputStream fos = new FileOutputStream(file);
                buffer = textOfWholeFile.getBytes();
                fos.write(buffer);
                fos.close();
                return 1;
            }
            catch (IOException e)
            {
                return 2;
            }
        }
        catch (IOException e)
        {
                String toSave = titleOfFavorite + "%" + gameTitle;
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[10000];
                    buffer = toSave.getBytes();
                    fos.write(buffer);
                    fos.close();
                    return 1;
                }
                catch (IOException ex)
                {
                    return 2;
                }
        }
    }

    public static String textOfGame(InputStream stream, String game)
    {
        String text = "";
        try
        {
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textOfWholeFile = new String(buffer);
            textOfWholeFile = textOfWholeFile.replace("\t", "");
            textOfWholeFile = textOfWholeFile.replace("\r", "");
            textOfWholeFile = textOfWholeFile.replace(Integer.toString(65279), "");
            String[] division = textOfWholeFile.split("@\n");
            for(int i = 0; i < division.length; ++i)
            {
                String[] nameAndCategoryAndText = division[i].split(">");
                if(nameAndCategoryAndText[0].equals(game))
                {
                    if(nameAndCategoryAndText.length > 2 )
                        return nameAndCategoryAndText[2];
                    else
                        return "";
                }
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return text;
    }

    public static String randGame(InputStream stream, int seed)
    {
        String nameOfGame = "";
        try
        {
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textOfWholeFile = new String(buffer);
            textOfWholeFile = textOfWholeFile.replace("\t", "");
            textOfWholeFile = textOfWholeFile.replace("\r", "");
            textOfWholeFile = textOfWholeFile.replace(Integer.toString(65279), "");
            String[] division = textOfWholeFile.split("@\n");
            Random rand = new Random();
            rand.setSeed(seed);
            int randNumber = rand.nextInt();
            randNumber = ((randNumber % division.length) + division.length)%division.length;
            String[] nameAndCategoryAndText = division[randNumber].split(">");
            nameOfGame = nameAndCategoryAndText[0];
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return nameOfGame;
    }

    public static ArrayList<String> find(InputStream stream, String toFind, boolean title, boolean text)
    {
        ArrayList<String> list = new ArrayList<>();
        int size = 0;
        try {
            size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textOfWholeFile = new String(buffer);

            textOfWholeFile = textOfWholeFile.replace("\r", "");
            textOfWholeFile = textOfWholeFile.replace("\t", "");
            String[] originalDivision = textOfWholeFile.split("@\n");
            textOfWholeFile = textOfWholeFile.toLowerCase();

            toFind = toFind.replace("\r", "");
            toFind = toFind.replace("\t", "");
            toFind = toFind.toLowerCase();
            String[] division = textOfWholeFile.split("@\n");
            for(int i = 0; i < division.length; ++i)
            {
                String[] nameAndCategoryAndText = division[i].split(">");
                String[] nameAndCategoryAndTextOriginal = originalDivision[i].split(">");
                if(title && nameAndCategoryAndText.length > 0 && nameAndCategoryAndText[0].contains(toFind))
                {
                    list.add(nameAndCategoryAndTextOriginal[0]);
                }
                else
                {
                    if(text && nameAndCategoryAndText.length > 2 && nameAndCategoryAndText[2].contains(toFind))
                    {
                        list.add(nameAndCategoryAndTextOriginal[0]);
                    }
                }
            }
            return list;
        } catch (IOException e) {
            return list;
        }
    }

    public static int removeFromFavorites(File file, String nameOfFavorite, String nameOfGame)
    {
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String TextOfWholeFile = new String(buffer);
            TextOfWholeFile = TextOfWholeFile.replace("\t", "");
            TextOfWholeFile = TextOfWholeFile.replace("\r", "");
            TextOfWholeFile = TextOfWholeFile.replace(Integer.toString(65279), "");

            String toSave = "";

            boolean sthWritten = false;

            String[] division = TextOfWholeFile.split("@");
            for(int i = 0; i < division.length; ++i)
            {
                String[] twoPartDivision = division[i].split("%");
                if(twoPartDivision.length == 2 && nameOfFavorite.equals(twoPartDivision[0]))
                {
                    String[] theSmalestDivision = twoPartDivision[1].split(">\n");


                    if(sthWritten)
                        toSave = toSave + "@" + twoPartDivision[0] + "%";
                    else
                    {
                        sthWritten = true;
                        toSave = twoPartDivision[0] + "%";
                    }


                    boolean firstWritten = false;

                    for(int j = 0; j < theSmalestDivision.length; ++j)
                    {
                        if(!theSmalestDivision[j].equals(nameOfGame))
                        {
                            if(firstWritten)
                            {
                                toSave = toSave + ">\n" + theSmalestDivision[j];
                            }
                            else
                            {
                                firstWritten = true;
                                toSave = toSave + theSmalestDivision[j];
                            }

                        }
                    }

                }
                else
                {
                    if(sthWritten)
                        toSave = toSave + "@" + division[i];
                    else
                    {
                        sthWritten = true;
                        toSave = division[i];
                    }
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                buffer = toSave.getBytes();
                fos.write(buffer);
                fos.close();
                return 1;
            }
            catch (IOException e)
            {
                return 0;
            }
        }
        catch (IOException e)
        {
            return 0;
        }
    }

    public static int removeFavorites(File file, String nameOfFavorites)
    {
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String textOfWholeFile = new String(buffer);
            textOfWholeFile = textOfWholeFile.replace("\t", "");
            textOfWholeFile = textOfWholeFile.replace("\r", "");
            textOfWholeFile = textOfWholeFile.replace(Integer.toString(65279), "");

            String toSave = "";

            boolean sthWritten = false;

            String[] division = textOfWholeFile.split("@");
            for(int i = 0; i < division.length; ++i)
            {
                String[] twoPartDivision = division[i].split("%");
                if(twoPartDivision.length != 0 && nameOfFavorites.equals(twoPartDivision[0]))
                {

                }
                else
                {
                    if(sthWritten)
                        toSave = toSave + "@" + division[i];
                    else
                    {
                        sthWritten = true;
                        toSave = division[i];
                    }
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                buffer = toSave.getBytes();
                fos.write(buffer);
                fos.close();
                return 1;
            }
            catch (IOException e)
            {
                return 0;
            }
        }
        catch (IOException e)
        {
            return 0;
        }
    }

    public static String wholeFile(File file)
    {
        String wholeFile = "";
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            wholeFile = new String(buffer);
        }
        catch (Exception e)
        {

        }

        return wholeFile;
    }


}


