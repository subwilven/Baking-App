package com.example.android.bakingapp.models;

import java.io.Serializable;

/**
 * Created by eslam on 27-Oct-17.
 */

public class Ingredient implements Serializable{
    private double quantity;
    private String measure;
    private String ingredient;

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
    public double getQuantity()
    {
        return quantity;
    }
    public String getMeasure()
    {
        return measure;
    }
    public String getIngredient()
    {
        return ingredient;
    }
}
