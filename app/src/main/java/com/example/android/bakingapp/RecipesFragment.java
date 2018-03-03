package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.models.Recipe;

import java.util.ArrayList;

import static com.example.android.bakingapp.R.id.rv_recipes;

/**
 * Created by eslam on 27-Oct-17.
 */

public class RecipesFragment extends Fragment {
    private static int NUM_OF_COLUMNS;
    private RecyclerView recyclerView;
    private RecipesAdapter recipesAdapter;
    private SimpleIdlingResource mIdlingResource;
    private final String BUNDLE_RECYCLER_POSITION = "recycler_position";

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        return mIdlingResource;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int lastFirstVisiblePosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        outState.putInt(BUNDLE_RECYCLER_POSITION, lastFirstVisiblePosition);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);
        recyclerView = rootView.findViewById(rv_recipes);
        NUM_OF_COLUMNS = getResources().getInteger(R.integer.num_of_columns);
        mIdlingResource = new SimpleIdlingResource();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), NUM_OF_COLUMNS));

        recipesAdapter = new RecipesAdapter(getContext(), new RecipesAdapter.HandleRecipeOnClick() {
            @Override
            public void recipeOnClick(Recipe recipe) {
                Intent intent = new Intent(getContext(), RecipeDetailsActivity.class);
                intent.putExtra("recipe", recipe);
                startActivity(intent);


            }
        });

        recyclerView.setAdapter(recipesAdapter);
        getIdlingResource();
        FetchRecipesData fetchRecipesData = new FetchRecipesData(getContext(),new FetchRecipesData.CallBack() {
            @Override
            public void handleInResult(ArrayList<Recipe> recipes) {
                recipesAdapter.swapData(recipes);
                if (savedInstanceState != null) {
                    int lastFirstVisiblePosition = savedInstanceState.getInt(BUNDLE_RECYCLER_POSITION);
                    recyclerView.scrollToPosition(lastFirstVisiblePosition);
                }
            }
        },mIdlingResource);
        fetchRecipesData.execute();

        return rootView;
    }

//    @VisibleForTesting
//    @NonNull
//    public IdlingResource getIdlingResource() {
//        if (mIdlingResource == null) {
//            mIdlingResource = new SimpleIdlingResource();
//        }
//        return mIdlingResource;
//    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}
