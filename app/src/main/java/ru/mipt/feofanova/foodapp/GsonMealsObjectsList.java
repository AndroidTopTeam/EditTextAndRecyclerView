package ru.mipt.feofanova.foodapp;

/**
 * Created by Талгат on 04.11.2017.
 */
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GsonMealsObjectsList
{
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("version")
    @Expose
    private Double version;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("results")
    @Expose
    private List<GsonMealObject> results = null;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Double getVersion()
    {
        return version;
    }

    public void setVersion(Double version)
    {
        this.version = version;
    }

    public String getHref()
    {
        return href;
    }

    public void setHref(String href)
    {
        this.href = href;
    }

    public List<GsonMealObject> getResults()
    {
        return results;
    }

    public void setResults(List<GsonMealObject> results)
    {
        this.results = results;
    }


}
