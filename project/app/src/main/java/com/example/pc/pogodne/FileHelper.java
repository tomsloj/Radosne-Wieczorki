package com.example.pc.pogodne;

import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class FileHelper {
    public ArrayList<String> tytułyWkategorii(InputStream stream, String kategoria)
    {
        ArrayList<String> lista = new ArrayList<String>();

        try
        {
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String calyPlik = new String(buffer);
            calyPlik = calyPlik.replace("\t", "");
            calyPlik = calyPlik.replace("\r", "");
            calyPlik = calyPlik.replace(Character.toString((char)65279), "");

            String[] podzial = calyPlik.split("@");
            for(int i = 0; i < podzial.length; ++i)
            {
                String[] podzialna2 = podzial[i].split("%");

                if((podzialna2.length == 2) && (kategoria.equals("all") || kategoria.equals(podzialna2[0])))
                {

                    String[] nazwaitekst = podzialna2[1].split(">\n");
                    for(int j = 0; j < nazwaitekst.length; ++j)
                    {
                        String[] podzialnapiewrsze = nazwaitekst[j].split(">");
                        lista.add(podzialnapiewrsze[0]);
                    }
                }
            }
        }
        catch (IOException ex)
        {
            //list.add("1111");
        }


        return lista;
    }

    public ArrayList<String> tytułyWulu(File file, String nazwaplaylisty)
    {
        ArrayList<String> lista = new ArrayList<String>();

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String calyPlik = new String(buffer);
            calyPlik = calyPlik.replace("\t", "");
            calyPlik = calyPlik.replace("\r", "");
            calyPlik = calyPlik.replace(Integer.toString(65279), "");

            String[] podzial = calyPlik.split("@");
            for(int i = 0; i < podzial.length; ++i)
            {
                String[] podzialna2 = podzial[i].split("%");
                if(podzialna2.length == 2 && nazwaplaylisty.equals(podzialna2[0]))
                {
                    String[] podzialnapierwsze = podzialna2[1].split(">\n");
                    for(int j = 0; j < podzialnapierwsze.length; ++j)
                    {
                        lista.add(podzialnapierwsze[j]);
                    }
                }
            }
        }
        catch (IOException e)
        {
        }
        return lista;
    }

    public ArrayList<String> listaulu(File file)
    {
        ArrayList<String> lista = new ArrayList<String>();

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String calyPlik = new String(buffer);
            calyPlik = calyPlik.replace("\t", "");
            calyPlik = calyPlik.replace("\r", "");
            calyPlik = calyPlik.replace(Integer.toString(65279), "");

            if(calyPlik.equals(""))
            {
                return lista;
            }

            String[] podzial = calyPlik.split("@");
            for(int i = 0; i < podzial.length; ++i)
            {
                String[] podzialna2 = podzial[i].split("%");
                lista.add(podzialna2[0]);
            }
        }
        catch (IOException e)
        {
        }
        return lista;
    }

    public boolean czyistnieje(File file, String nazwaulubionych)
    {
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String calyPlik = new String(buffer);
            calyPlik = calyPlik.replace("\t", "");
            calyPlik = calyPlik.replace("\r", "");
            calyPlik = calyPlik.replace(Integer.toString(65279), "");

            String[] podzial = calyPlik.split("@");
            for(int i = 0; i < podzial.length; ++i)
            {
                String[] podzialna2 = podzial[i].split("%");
                if(podzialna2.length == 2 && nazwaulubionych.equals(podzialna2[0]))
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
    public int dodajdoulu(File file, String nazwaulubionych, String nazwazabawy)
    {
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String calyPlik = new String(buffer);
            calyPlik = calyPlik.replace("\t", "");
            calyPlik = calyPlik.replace("\r", "");
            calyPlik = calyPlik.replace(Integer.toString(65279), "");

            String dozapisu = "";

            boolean bylo = false;
            boolean coszapisane = false;
            String[] podzial = calyPlik.split("@");
            for(int i = 0; i < podzial.length; ++i)
            {
                String[] podzialna2 = podzial[i].split("%");
                if(podzialna2.length == 2 && nazwaulubionych.equals(podzialna2[0]))
                {
                    String[] podzialnapierwsze = podzialna2[1].split(">\n");
                    for(int j = 0; j < podzialnapierwsze.length; ++j)
                    {
                        if(podzialnapierwsze[j].equals(nazwazabawy))
                        {
                            bylo = true;
                        }
                    }
                    if(bylo)
                    {
                        return 1;
                    }
                    else
                    {
                        if(coszapisane)
                            dozapisu = dozapisu + "@" + podzial[i] + ">\n" + nazwazabawy;
                        else
                        {
                            coszapisane = true;
                            dozapisu = podzial[i] + ">\n" + nazwazabawy;
                        }
                    }

                }
                else
                {
                    if(coszapisane)
                        dozapisu = dozapisu + "@" + podzial[i];
                    else
                    {
                        coszapisane = true;
                        dozapisu = podzial[i];
                    }
                }
            }

            try {
                FileOutputStream fos = new FileOutputStream(file);
                buffer = dozapisu.getBytes();
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
    public int dodajnowaulu(File file, String nazwaulubionych, String nazwazabawy)
    {
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String calyPlik = new String(buffer);
            calyPlik = calyPlik.replace("\t", "");
            calyPlik = calyPlik.replace("\r", "");
            calyPlik = calyPlik.replace(Integer.toString(65279), "");
            if(!calyPlik.equals(""))
                calyPlik = calyPlik + "@" + nazwaulubionych + "%" + nazwazabawy;
            else
                calyPlik = nazwaulubionych + "%" + nazwazabawy;
            try {
                FileOutputStream fos = new FileOutputStream(file);
                buffer = calyPlik.getBytes();
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
                String calyPlik = nazwaulubionych + "%" + nazwazabawy;
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[10000];
                    buffer = calyPlik.getBytes();
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

    public String tekst(InputStream stream, String nazwa)
    {
        String tekst = "";
        try
        {
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String calyPlik = new String(buffer);
            calyPlik = calyPlik.replace("\t", "");
            calyPlik = calyPlik.replace("\r", "");
            calyPlik = calyPlik.replace(Integer.toString(65279), "");
            String[] podzial = calyPlik.split("@\n");
            for(int i = 0; i < podzial.length; ++i)
            {
                String[] nazkattxt = podzial[i].split(">");
                if(nazkattxt[0].equals(nazwa))
                {
                    if(nazkattxt.length > 2 )
                        return nazkattxt[2];
                    else
                        return "";
                }
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return tekst;
    }

    public String randzabawa(InputStream stream, int seed)
    {
        String nazwa = "";
        try
        {
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String calyPlik = new String(buffer);
            calyPlik = calyPlik.replace("\t", "");
            calyPlik = calyPlik.replace("\r", "");
            calyPlik = calyPlik.replace(Integer.toString(65279), "");
            String[] podzial = calyPlik.split("@\n");
            Random rand = new Random();
            rand.setSeed(seed);
            int nr = rand.nextInt();
            nr = ((nr % podzial.length) + podzial.length)%podzial.length;
            String[] nazkattxt = podzial[nr].split(">");
            nazwa = nazkattxt[0];
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return nazwa;
    }

    public ArrayList<String> szukaj(InputStream stream, String toFind, boolean title, boolean text)
    {
        ArrayList<String> lista = new ArrayList<String>();
        int size = 0;
        try {
            size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String calyPlik = new String(buffer);


            boolean found = false;
            String calyPlik2 = calyPlik;
            String toFind2 = toFind;

            calyPlik = calyPlik.replace("\r", "");
            calyPlik = calyPlik.replace("\t", "");
            String[] podzialorginal = calyPlik.split("@\n");
            calyPlik = calyPlik.toLowerCase();

            toFind = toFind.replace("\r", "");
            toFind = toFind.replace("\t", "");
            toFind = toFind.toLowerCase();
            String[] podzial = calyPlik.split("@\n");
            for(int i = 0; i < podzial.length; ++i)
            {
                String[] nazkattxt = podzial[i].split(">");
                String[] nazkattxtorginal = podzialorginal[i].split(">");
                if(title && nazkattxt.length > 0 && nazkattxt[0].contains(toFind))
                {
                    lista.add(nazkattxtorginal[0]);
                }
                else
                {
                    if(text && nazkattxt.length > 2 && nazkattxt[2].contains(toFind))
                    {
                        lista.add(nazkattxtorginal[0]);
                    }
                }
            }
            return lista;
        } catch (IOException e) {
            return lista;
        }
    }

    public int usunzulu(File file, String nazwaulubionych, String nazwazabawy)
    {
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String calyPlik = new String(buffer);
            calyPlik = calyPlik.replace("\t", "");
            calyPlik = calyPlik.replace("\r", "");
            calyPlik = calyPlik.replace(Integer.toString(65279), "");

            String dozapisu = "";

            boolean coszapisane = false;

            String[] podzial = calyPlik.split("@");
            for(int i = 0; i < podzial.length; ++i)
            {
                String[] podzialna2 = podzial[i].split("%");
                if(podzialna2.length == 2 && nazwaulubionych.equals(podzialna2[0]))
                {
                    String[] podzialnapierwsze = podzialna2[1].split(">\n");


                    if(coszapisane)
                        dozapisu = dozapisu + "@" + podzialna2[0] + "%";
                    else
                    {
                        coszapisane = true;
                        dozapisu = podzialna2[0] + "%";
                    }


                    boolean jedenwstawiony = false;

                    for(int j = 0; j < podzialnapierwsze.length; ++j)
                    {
                        if(!podzialnapierwsze[j].equals(nazwazabawy))
                        {
                            if(jedenwstawiony)
                            {
                                dozapisu = dozapisu + ">\n" + podzialnapierwsze[j];
                            }
                            else
                            {
                                jedenwstawiony = true;
                                dozapisu = dozapisu + podzialnapierwsze[j];
                            }

                        }
                    }

                }
                else
                {
                    if(coszapisane)
                        dozapisu = dozapisu + "@" + podzial[i];
                    else
                    {
                        coszapisane = true;
                        dozapisu = podzial[i];
                    }
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                buffer = dozapisu.getBytes();
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

    public int usunlisteulu(File file, String nazwaulubionych)
    {
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String calyPlik = new String(buffer);
            calyPlik = calyPlik.replace("\t", "");
            calyPlik = calyPlik.replace("\r", "");
            calyPlik = calyPlik.replace(Integer.toString(65279), "");

            String dozapisu = "";

            boolean coszapisane = false;

            String[] podzial = calyPlik.split("@");
            for(int i = 0; i < podzial.length; ++i)
            {
                String[] podzialna2 = podzial[i].split("%");
                //dozapisu = dozapisu + "@" + podzialna2[0] + "%";
                //dozapisu = dozapisu + "\n" + podzialna2[0] + "----\n";
                if(podzialna2.length != 0 && nazwaulubionych.equals(podzialna2[0]))
                {
                    //dozapisu = dozapisu + ")";
                }
                else
                {
                    if(coszapisane)
                        dozapisu = dozapisu + "@" + podzial[i];
                    else
                    {
                        coszapisane = true;
                        dozapisu = podzial[i];
                    }
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                buffer = dozapisu.getBytes();
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

    public String calyplik(File file)
    {
        String calyPlik = "";
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            calyPlik = new String(buffer);
        }
        catch (Exception e)
        {

        }

        return calyPlik;
    }
}


