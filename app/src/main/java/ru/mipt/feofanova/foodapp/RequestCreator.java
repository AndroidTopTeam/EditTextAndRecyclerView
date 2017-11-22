package ru.mipt.feofanova.foodapp;

/**
 * Created by Талгат on 25.10.2017.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Талгат on 25.10.2017.
 */


public class RequestCreator
{
    private ArrayList<String> ingredients;
    private ArrayList<String> normalSearchQuery;
    private ArrayList<String> page;
    private static final String BASESOURCE = "http://www.recipepuppy.com/api/";


    public RequestCreator(ArrayList<String> ingr, ArrayList<String> query, ArrayList<String> _page)
    {
        ingredients = new ArrayList<>();
        ingredients = ingr;
        normalSearchQuery = new ArrayList<>();

        page = new ArrayList<>();
        page = _page;
    }


    public String makeRequestString()
    {
        String req = BASESOURCE;
        req += "?";

        boolean hasIngr = false, hasQuery = false, hasPage = false;

        if (ingredients.size() > 0)
        {
            req += "i=";
            for (int i = 0; i < ingredients.size() - 1; ++i)
                req += ingredients.get(i) + ",";
            req += ingredients.get(ingredients.size() - 1);
            hasIngr = true;
        }
        /*if (page.size() > 0)
        {
            req += "&";
            req += "p=";
            req += page.get(0);
        }*/
        /*if(normalSearchQuery.size()>0)
        {
            if (hasIngr)
                req+="&";
            req+="q=";
            req+=normalSearchQuery.get(0);
        }
        if(page.size()>0)
        {
            if((hasIngr && !hasQuery) || (hasQuery))
                req+="&";
            req+="p=";
            req+=page.get(0);
        }*/
        return req;
        //return ((req.length()-2 > BASESOURCE.length())? req : "None");
    }


}
