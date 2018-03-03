package com.example.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.FetchRecipesData;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Utility;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;

import java.util.ArrayList;

public class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private ArrayList<Ingredient> mIngredientsArray;
    private int mRecipeID;
    private Recipe mRecipe;


    public ListRemoteViewFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

        mRecipeID=Utility.getRecentVisitedRecipeID(mContext);
    }

    @Override
    public void onDataSetChanged() {
        int newRecipeID=Utility.getRecentVisitedRecipeID(mContext);//to check if load the data or not
        if(mIngredientsArray==null||mIngredientsArray.size()==0||mRecipeID!=newRecipeID) {//execute only when there no data or there is a new recipe id
            mRecipeID=newRecipeID;
            FetchRecipesData fetchRecipesData = new FetchRecipesData(mContext,new FetchRecipesData.CallBack() {
                @Override
                public void handleInResult(ArrayList<Recipe> recipes) {
                    mRecipe = recipes.get(mRecipeID - 1);
                    mIngredientsArray = mRecipe.getIngredients();
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, BakingWidgetProvider.class));
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget);
                }
            }, null);
            fetchRecipesData.execute();
        }

    }
    @Override
    public void onDestroy() {
        mIngredientsArray=null;
    }
    @Override
    public int getCount() {
        if(mIngredientsArray==null)return 0;
        return mIngredientsArray.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if(mIngredientsArray==null||mIngredientsArray.size()==0)
            return null;
        Ingredient ingredient = mIngredientsArray.get(i);
        RemoteViews remoteViews =new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget);
        remoteViews.setTextViewText(R.id.tv_widget_ingredients_name,ingredient.getIngredient());
        remoteViews.setTextViewText(R.id.tv_widget_ingredients_quantity,String.valueOf(ingredient.getQuantity()));
        remoteViews.setTextViewText(R.id.tv_widget_ingredients_measure,ingredient.getMeasure());
//        Intent fillIntent = new Intent();
//        fillIntent.putExtra("stepNumber",(int) step.getId());
//        fillIntent.putExtra("recipe",mRecipe);
//        remoteViews.setOnClickFillInIntent(R.id.ll_widget,fillIntent);


        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
