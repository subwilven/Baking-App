package com.example.android.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.bakingapp.RecyclerViewMatcher.withRecyclerView;

/**
 * This test demos a user clicking on a GridView item in MenuActivity which opens up the
 * corresponding OrderActivity.
 * <p>
 * This test does not utilize Idling Resources yet. If idling is set in the MenuActivity,
 * then this test will fail. See the IdlingResourcesTest for an identical test that
 * takes into account Idling Resources.
 */


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public static final String RECIPE_NAME = "Nutella Pie";
    private IdlingResource mIdlingResource;
    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @Before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Clicks on a GridView item and checks it opens up the OrderActivity with the correct details.
     */


    @Before
    public void registerIdlingResource()
    {
        RecipesFragment recipesFragment
                =(RecipesFragment)mActivityTestRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.frg_activity_main);
        mIdlingResource= recipesFragment.getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }
    @Test
    public void clickRecyclerViewItem_openRecipeDetailsActivity() {

        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withRecyclerView(R.id.rv_recipes_ingredients)
                .atPositionOnView(0, R.id.tv_ingredients_name))
                .check(matches(withText("Graham Cracker crumbs")));



    }

    @After
    public void unRegisterIdlingResource()
    {
        if(mIdlingResource!=null)
        Espresso.unregisterIdlingResources(mIdlingResource);
    }

}