package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.models.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity
        implements RecipeDetailsFragment.RecipeDetailsCallBack,
        StepDetailsFragment.ButtonCallBack {
    private boolean mTwoPane;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        Intent intent = getIntent();
        mRecipe = (Recipe) intent.getSerializableExtra("recipe");
        getSupportActionBar().setTitle(mRecipe.getName());
        if (findViewById(R.id.steps_container) != null) {
            mTwoPane = true;
            //set the first step as clicked
            getSupportActionBar().setElevation(0f);
            if(savedInstanceState==null) onStepClicked(mRecipe,0);// if this thr first time set the first step as clicked
        } else {
            mTwoPane = false;
        }
        // when it get value that means the intent coming from the widget to open a specific step
        if(savedInstanceState==null) {
            if (intent.getIntExtra("stepNumber", -1) != -1) {
                if (mTwoPane) {//if it two pane we should display two fragments
                    displayRecipeDetailsFragment();
                    int stepNum = intent.getIntExtra("stepNumber", 0);
                    //set the displayed step details
                    onStepClicked(mRecipe, stepNum);
                } else//if one pase only display the step details  fragment
                {
                    onStepClicked(mRecipe, intent.getIntExtra("stepNumber", 0));
                }
            } else {// act normally
                displayRecipeDetailsFragment();
            }
        }
        // Log.i("recccipe",recipe.getName());

    }

    public void displayRecipeDetailsFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipe", mRecipe);
        bundle.putSerializable("isTwoPane", mTwoPane);
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        recipeDetailsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frg_recipe_details, recipeDetailsFragment,RecipeDetailsFragment.TAG).commit();
    }

    @Override
    public void onStepClicked(Recipe recipe, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipe", recipe);
        bundle.putInt("stepNumber", position);
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setArguments(bundle);

        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.steps_container, stepDetailsFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frg_recipe_details, stepDetailsFragment).addToBackStack(null).commit();
        }
    }

    //to handle changing the active item  in the steps recycler view
    @Override
    public void buttonOnClick(int position) {
        if (mTwoPane)
        {
            Fragment fragment=getSupportFragmentManager().findFragmentByTag(RecipeDetailsFragment.TAG);
            if(fragment instanceof RecipeDetailsFragment)
            {
                ((RecipeDetailsFragment) fragment).changeRecyclerViewActivitedItem(position);
            }
        }
    }
}
