package com.example.android.bakingapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.example.android.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.models.Recipe;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by eslam on 07-Nov-17.
 */

public class FetchRecipesData extends AsyncTask<Void, Void, ArrayList<Recipe>> {
    String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    final SimpleIdlingResource mIdlingResource;

    public FetchRecipesData(CallBack callBack, @Nullable SimpleIdlingResource idlingResource) {
        mIdlingResource = idlingResource;
        mCallBack = callBack;
    }

    CallBack mCallBack;

    public interface CallBack {
        void handleInResult(ArrayList<Recipe> recipes);
    }

    @Override
    protected ArrayList<Recipe> doInBackground(Void... voids) {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        Uri.Builder buildUri = Uri.parse(url).buildUpon();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
            String json = Utility.getResponseFromHttpUrl(url);
            StringBuilder str = new StringBuilder(json);
            str.insert(0, "{\"recipes\":\n");
            str.insert(str.toString().length(), "}");
            String json2 = str.toString();
            ArrayList<Recipe> recipes = Utility.parseJson(json2);
            return recipes;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> recipes) {
        super.onPostExecute(recipes);
        if (mCallBack != null) {
            mCallBack.handleInResult(recipes);
        }
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }
}
