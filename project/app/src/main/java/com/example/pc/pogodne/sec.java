package com.example.pc.pogodne;

import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Random;

public class sec {
    private static String[] third = {
            "najlepszym",
            "wspaniałym",
            "dobrym",
            "wielkodusznym",
            "szlachetnym",
            "wyśmienitym",
            "serdecznym",
            "przemiłym",
            "doskonałym",
            "kapitalnym",
            "cudownym",
            "przystojnym",
            "pierwszorzędnym",
            "fantastycznym",
            "fenomenalnym",
            "wybitnym",
            "genialnym",
            "znakomitym",
            "super",
            "fajowym",
            "serdecznym",
            "zacnym",
            "niezwykłym",
            "wartościowym",
            "przezabawnym",
            "idealnym",
            "niezłomnym",

            "najlepszy",
            "wspaniały",
            "dobry",
            "wielkoduszny",
            "szlachetny",
            "wyśmienity",
            "serdeczny",
            "przemiły",
            "doskonały",
            "kapitalny",
            "cudowny",
            "przystojny",
            "pierwszorzędny",
            "fantastyczny",
            "fenomenalny",
            "wybitny",
            "genialny",
            "znakomity",
            "super",
            "fajowy",
            "serdeczny",
            "zacny",
            "niezwykły",
            "wartościowy",
            "przezabawny",
            "idealny",
            "niezłomny"
    };
    private static String[] fourth = {
            "ziomkiem",
            "przyjacielem",
            "kumplem",
            "znajomym",
            "mentorem",
            "bohaterem",
            "druhem",
            "brachem",
            "towarzyszem",
            "ziomalem",

            "ziomek",
            "przyjaciel",
            "kumpel",
            "znajomy",
            "mentor",
            "bohater",
            "druh",
            "brach",
            "towarzysz",
            "ziomal\n"
    };
    //public static String adresMac = "f4:e3:fb:e5:08:cc";
    public static byte[] mac = null;
    public static void getMac(File file)
    {
        byte[]tmpmac;
        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String plik = new String(buffer);
            String macAdress = "";
            for(int i = 0; i < plik.length() && plik.charAt(i) != ';'; ++ i)
            {
                macAdress = macAdress + plik.charAt(i);
            }
            tmpmac = macAdress.getBytes();
        }
        catch (IOException e)
        {
            tmpmac = "aa:bb:cc:op".getBytes();
        }
        mac = tmpmac;
    }

    private static int checkSecond(String secondElement) {
        int ei213osdae123913 = 13;
        System.out.println("Sec: "+ secondElement + mac.length);
        if (mac.length >= 3) {
            ei213osdae123913 = 10000007 * (mac[mac.length / 2] + mac[(mac.length / 2) - 1] + mac[mac.length/2 +1] + mac[0]);
        }
        else
            ei213osdae123913 = 553532;
        if(ei213osdae123913 % 2 == 0)
        {
            String iru32r98y = "to mój";
            if(iru32r98y.equals(secondElement))
            {
                return 17;
            }
            else
            {
                return 11;
            }
        }
        else
        {
            String rihwor3252i = "jest moim";
            if(!rihwor3252i.equals(secondElement))
            {
                return 4;
            }
            else
            {
                return 34;
            }
        }
    }

    private static int checkFourth(String fourthElement)
    {
        int lololololo = 5634;
        if(mac.length >= 2)
        {
            lololololo ^= mac[mac.length - 1];
            lololololo += mac[mac.length - 2];
        }
        else
            lololololo = 753893203;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] newmac = md.digest(mac);
            ByteBuffer wrapped = ByteBuffer.wrap(newmac); // big-endian by default
            lololololo = wrapped.getInt();

        }
        catch (Exception e)
        {

        }
        int gogoogogog = ((lololololo % 10) + 10)%10;
        boolean tf = true;
        if(fourth[gogoogogog].equals(fourthElement))
            tf = false;
        else
            tf = true;
        if(!tf)
        {
            return 17;
        }
        else
        if(fourth[gogoogogog + 10].equals(fourthElement))
        {
            return 17;
        }
        return 13;
    }

    private static boolean checkThird(String thirdElement)
    {
        int seed = 127;
        for(int i = 0; i < mac.length; ++i)
        {
            seed = (seed << 8) | mac[i];
        }
        Random rand = new Random(seed);
        for(int i = 0; i < 5; ++i)
        {
            rand.nextInt();
        }
        int op = rand.nextInt() % 27;
        if(!thirdElement.equals(third[op]))
        {
            if(thirdElement.equals(third[op + 10]))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if(thirdElement.equals(third[op + 10]))
            {
                return false;
            }
            else
                return true;
        }
    }
    public static int[] check(String key)
    {
        int[] value = new int[19];
        String[] split = key.split(" ");
        int size = split.length;
        //System.out.println(checkSecond(split[1] + " " + split[2]) + " size: " + size);
        if(size > 2 && 5 == size)
        {
            value[0] = 2 * checkSecond(split[1] + " " + split[2]);
        }
        else
        {
            if(size > 2)
            {
                value[0] = checkSecond(split[1] + " " + split[2]) / 3;
            }
            else
                value[0] = 1;
        }
        for(int i = 1; i < 17; ++i)
        {
            value[i] = 290;
        }
        if(size > 2)
        {
            if(checkSecond(split[1] + " " + split[2])/17 == 1) {
                if (size >= 5)
                {
                    value[17] = checkFourth(split[4]);
                    if (checkThird(split[3]))
                        value[18] = 1;
                    else
                        value[18] = 0;
                }
                else
                {
                    value[17] = 33;
                    value[18] = 1;
                }
            }
            else
            {
                if(size >= 5)
                {
                    value[17] = checkFourth(split[4]);
                    if (checkThird(split[3]))
                        value[18] = 1;
                    else
                        value[18] = 0;
                }
                else {

                    value[17] = 12;
                    value[18] = 1;
                }
            }
        }
        else {
            value[17] = 33;
            value[18] = 1;
        }

        if(size != 0 && !split[0].equals("Tomek"))
        {
            value[11] += 1;
            value[7] += 1;
        }

        return value;
    }

    public static String generate()
    {
        String napis = "Tomek ";
        int ei213osdae123913 = 10000007 * (mac[mac.length / 2] + mac[(mac.length / 2) - 1] + mac[mac.length/2 +1] + mac[0]);
        if(ei213osdae123913 % 2 == 0)
        {
            napis = napis + "to mój ";
            int seed = 127;
            for(int i = 0; i < mac.length; ++i)
            {
                seed = (seed << 8) | mac[i];
            }
            Random rand = new Random(seed);
            for(int i = 0; i < 5; ++i)
            {
                rand.nextInt();
            }
            int op = rand.nextInt() % 27 % 27;
            napis = napis + third[op+10] + " ";

            try {
                int lololololo = 5634;
                lololololo ^= mac[mac.length - 1];
                lololololo += mac[mac.length - 2];
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] newmac = md.digest(mac);
                ByteBuffer wrapped = ByteBuffer.wrap(newmac); // big-endian by default
                lololololo = wrapped.getInt();
                int gogoogogog = ((lololololo % 10) + 10)%10;
                napis = napis + fourth[gogoogogog + 10];
            }
            catch(Exception e)
            {

            }
        }
        else
        {
            napis = napis + "jest moim ";
            int seed = 127;
            for(int i = 0; i < mac.length; ++i)
            {
                seed = (seed << 8) | mac[i];
            }
            Random rand = new Random(seed);
            for(int i = 0; i < 5; ++i)
            {
                rand.nextInt();
            }
            int op = rand.nextInt() % 27;

            try {
                napis = napis + third[op] + " ";
            }
            catch(Exception e)
            {

            }


            int lololololo = 5634;
            lololololo ^= mac[mac.length - 1];
            lololololo += mac[mac.length - 2];
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] newmac = md.digest(mac);
                ByteBuffer wrapped = ByteBuffer.wrap(newmac); // big-endian by default
                lololololo = wrapped.getInt();
                int gogoogogog = ((lololololo % 10) + 10)%10;
                napis = napis + fourth[gogoogogog];
            }
            catch (Exception e)
            {

            }
        }


        return napis;
    }
}
