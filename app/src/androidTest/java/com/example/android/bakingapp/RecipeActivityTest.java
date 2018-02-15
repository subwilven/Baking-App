package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.models.Recipe;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by eslam on 05-Nov-17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    private final int RECIPE_ID = 0;//check the recipe with id equal to zero
    private final int STEP_ID = 2;//check the second step of the recipe
    private ArrayList<Recipe> recipes;
    @Rule
    public ActivityTestRule<RecipeDetailsActivity> mActivityTestRule = new ActivityTestRule<>(RecipeDetailsActivity.class, false, false);


    public void startActivity() {
        Intent intent = new Intent();
        Context appContext = InstrumentationRegistry.getTargetContext();
        String json = Utility.loadJSONFromAsset(appContext);
        try {
            recipes = Utility.parseJson(json);
            intent.putExtra("recipe", recipes.get(RECIPE_ID));
            mActivityTestRule.launchActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void clickStepListItem_openStepDetails() throws JSONException {

        startActivity();

        onView(withId(R.id.rv_recipes_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(STEP_ID, click()));
        onView(withId(R.id.tv_step_description))
                .check(matches(withText(recipes.get(RECIPE_ID).getSteps().get(STEP_ID).getDescription())));

    }

    @Test
    public void clickNextAndPreviousButton() {
        startActivity();
        //open a step
        onView(withId(R.id.rv_recipes_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(STEP_ID, click()));
        //click the next button
        onView(withId(R.id.btn_next_step)).perform(click());
        //check the step step description is equal to the next one
        onView(withId(R.id.tv_step_description))
                .check(matches(withText(recipes.get(RECIPE_ID).getSteps().get(STEP_ID + 1).getDescription())));

        //----------------------------------------------------------------------------------------------------------

        //click the prev button
        onView(withId(R.id.btn_prev_step)).perform(click());
        //check the step step description is equal to the previous one
        onView(withId(R.id.tv_step_description))
                .check(matches(withText(recipes.get(RECIPE_ID).getSteps().get(STEP_ID).getDescription())));
    }


}
