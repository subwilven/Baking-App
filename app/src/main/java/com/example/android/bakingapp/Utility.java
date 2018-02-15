package com.example.android.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by eslam on 27-Oct-17.
 */

public class Utility {

    public static boolean haveNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("recipes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static ArrayList<Recipe> parseJson(String json) throws JSONException {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        final String RECIPE_ID = "id";
        final String RECIPE_NAME = "name";
        final String RECIPE_SERVINGS = "servings";
        final String RECIPE_IMAGE = "image";
        final String RECIPE_STEPS = "steps";
        final String RECIPE_INGREDIENTS = "ingredients";
        final String STEP_ID = "id";
        final String STEP_SHORT_DESCRIPTION = "shortDescription";
        final String STEP_DESCRIPTION = "description";
        final String STEP_VIDEO_URL = "videoURL";
        final String STEP_THUMBNAIL_URL = "thumbnailURL";
        final String INGREDIENT_QUANTITY = "quantity";
        final String INGREDIENT_MEASURE = "measure";
        final String INGREDIENT_INGREDIENT = "ingredient";

        JSONObject jsonObject = new JSONObject(json);
        JSONArray recipesJSonArray = jsonObject.getJSONArray("recipes");
        for (int i = 0; i < recipesJSonArray.length(); i++) {
            JSONObject singleRecipe = recipesJSonArray.getJSONObject(i);

            Recipe recipe = new Recipe();
            recipe.setId(singleRecipe.getInt(RECIPE_ID));
            recipe.setImage(singleRecipe.getString(RECIPE_IMAGE));
            recipe.setName(singleRecipe.getString(RECIPE_NAME));
            recipe.setServings(singleRecipe.getString(RECIPE_SERVINGS));

            //------------------add steps to the recipe
            JSONArray stepsJSonArray = singleRecipe.getJSONArray(RECIPE_STEPS);
            ArrayList<Step> steps = new ArrayList<Step>();
            for (int x = 0; x < stepsJSonArray.length(); x++) {
                Step step = new Step();
                JSONObject singleStep = stepsJSonArray.getJSONObject(x);
                step.setId(singleStep.getInt(STEP_ID));
                step.setDescription(singleStep.getString(STEP_DESCRIPTION));
                step.setShortDescription(singleStep.getString(STEP_SHORT_DESCRIPTION));
                step.setThumbnailURL(singleStep.getString(STEP_THUMBNAIL_URL));
                step.setVideoURL(singleStep.getString(STEP_VIDEO_URL));
                steps.add(step);
            }
            recipe.setSteps(steps);
            //------------------add ingredients to the recipe
            JSONArray ingredientsJSonArray = singleRecipe.getJSONArray(RECIPE_INGREDIENTS);
            ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
            for (int x = 0; x < ingredientsJSonArray.length(); x++) {
                Ingredient ingredient = new Ingredient();
                JSONObject singleIngredient = ingredientsJSonArray.getJSONObject(x);
                ingredient.setIngredient(singleIngredient.getString(INGREDIENT_INGREDIENT));
                ingredient.setMeasure(singleIngredient.getString(INGREDIENT_MEASURE));
                ingredient.setQuantity(singleIngredient.getInt(INGREDIENT_QUANTITY));
                ingredients.add(ingredient);
            }
            recipe.setIngredients(ingredients);
            recipes.add(recipe);
        }

        return recipes;
    }
    @Nullable
    public static String getResponseFromHttpUrl(URL url) throws IOException {


        HttpURLConnection urlConnection = (HttpURLConnection) (url != null ? url.openConnection() : null);
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(10000);
        try {
            InputStream in = urlConnection != null ? urlConnection.getInputStream() : null;

            Scanner scanner = new Scanner(in != null ? in : null);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
    }
    public static void   setRecentVisitedRecipe(Context context,int recipeID)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("recent_visited_recipe",recipeID).apply();
    }
    public static int getRecentVisitedRecipeID(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int recipeID=prefs.getInt("recent_visited_recipe",1);
        return recipeID;
    }
    public static void setRecentVisitedRecipeName(Context context,String name)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("recent_visited_recipe_name",name).apply();
    }
    public static String getRecentVisitedRecipeName(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName=prefs.getString("recent_visited_recipe_name","");
        return recipeName;
    }

}
