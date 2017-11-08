package ru.mipt.feofanova.foodapp;

import java.util.List;

/**
 * Created by Талгат on 08.11.2017.
 */

public class Singleton
{
    private static Singleton instance;
    private static List<GsonRecArray> parsedJsonResp;

    private Singleton(){}


    public static void setParsedJsonResp(List<GsonRecArray> parsedJsonResp)
    {
        Singleton.parsedJsonResp = parsedJsonResp;
    }


    public static List<GsonRecArray> getParsedJsonResp()
    {
        return parsedJsonResp;
    }

    public static synchronized Singleton getInstance()
    {
        if(instance==null)
            instance = new Singleton();
        return instance;
    }


}
