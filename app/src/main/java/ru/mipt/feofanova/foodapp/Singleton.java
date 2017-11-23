package ru.mipt.feofanova.foodapp;

import java.util.List;

/**
 * Created by Талгат on 08.11.2017.
 */

public class Singleton
{
    private static Singleton instance;
    private static List<GsonMealObject> parsedJsonResp;

    private Singleton(){}


    public static void setParsedJsonResp(List<GsonMealObject> parsedJsonResp)
    {
        Singleton.parsedJsonResp = parsedJsonResp;
    }


    public static List<GsonMealObject> getParsedJsonResp()
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
