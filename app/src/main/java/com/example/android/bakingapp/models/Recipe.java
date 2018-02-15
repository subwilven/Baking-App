package com.example.android.bakingapp.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by eslam on 27-Oct-17.
 */

public class Recipe implements Serializable{
    private double id;
    private String name;
    private String servings;
    private String image;
    private ArrayList<Step> steps;
    private ArrayList<Ingredient> ingredients;

    public void setId(double id) {
        this.id = id;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }
    public void setImage(String image)
    {
        this.image = image;
    }
    public void setSteps(ArrayList<Step> steps)
    {
        this.steps=steps;
    }
    public void setIngredients(ArrayList<Ingredient> ingredients)
    {
        this.ingredients=ingredients;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public String getServings()
    {
        return servings;
    }
    public String getImage()
    {
        return image;
    }
    public ArrayList<Step> getSteps()
    {
        return steps;
    }
    public ArrayList<Ingredient> getIngredients()
    {
        return ingredients;
    }
}
