package ru.mipt.feofanova.foodapp;

/**
 * Created by Талгат on 25.10.2017.
 */

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Internal;

/**
 * Created by Талгат on 25.10.2017.
 */


public class RequestUrlCreator
{
    private ArrayList<String> ingredients;
    private ArrayList<String> normalSearchQuery;
    int page;
    private static final String BASESOURCE = "http://www.recipepuppy.com/api/";


    public RequestUrlCreator()
    {
    }

    public RequestUrlCreator(ArrayList<String> ingr, ArrayList<String> query, int _page)
    {
        ingredients = new ArrayList<>();
        ingredients = ingr;
        normalSearchQuery = new ArrayList<>();
        normalSearchQuery = query;

        page = _page;
    }

    public RequestUrlCreator(ArrayList<String> ingr)
    {
        ingredients = new ArrayList<>();
        ingredients = ingr;
        normalSearchQuery = new ArrayList<>();

        page = 1;
    }

    public RequestUrlCreator(ArrayList<String> ingr, int _page)
    {
        ingredients = new ArrayList<>();
        ingredients = ingr;
        normalSearchQuery = new ArrayList<>();

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

        if (page != 0 || page != 1)
        {
            req += "&p=" + page;
        }
        else
        {
            req += "&p=1";
        }

        return req;
        //return ((req.length()-2 > BASESOURCE.length())? req : "None");
    }

    public String changePage(String basicUrl, int delta)
    {
        int pPosition = basicUrl.length() - 3;
        final char p = '=';
        int currentPageNumber;
        String currentPageNumberString = "";
        String basicUrlBeforeP = "";
        for (int i = basicUrl.length() - 1; i >= 0; --i)
        {
            if (basicUrl.charAt(i) == p)
            {
                pPosition = i;
                break;
            }

        }

        for (int i = pPosition + 1; i < basicUrl.length(); ++i)
            currentPageNumberString += basicUrl.charAt(i);

        currentPageNumber = Integer.valueOf(currentPageNumberString);


        if (currentPageNumber + delta > 0)
        {
            currentPageNumber += delta;

            for (int i = 0; i < pPosition - 2 ; ++i)
                basicUrlBeforeP += basicUrl.charAt(i);

            basicUrlBeforeP += "&p=" + currentPageNumber;
            return basicUrlBeforeP;
        }
        else
            return basicUrl;
    }

}
