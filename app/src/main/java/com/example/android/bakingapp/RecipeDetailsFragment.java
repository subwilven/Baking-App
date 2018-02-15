package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

/**
 * Created by eslam on 28-Oct-17.
 */

public class RecipeDetailsFragment extends Fragment {
    private final String BUNDLE_ACTIVE_ITEM_POSITION="active_item_position";

    final static String TAG="RecipeDetailsFragment";
    RecyclerView rvSteps;
    StepsAdapter adpSteps;
    IngredientsAdapter adpIngredients;
    RecyclerView rvIngredients;
    Recipe mRecipe;
    private int mActiveItemPosition;
    public interface RecipeDetailsCallBack {
        void onStepClicked(Recipe recipe, int position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_ACTIVE_ITEM_POSITION,mActiveItemPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        rvIngredients = rootView.findViewById(R.id.rv_recipes_ingredients);
        rvSteps = rootView.findViewById(R.id.rv_recipes_steps);
        adpIngredients = new IngredientsAdapter();
        rvIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        rvIngredients.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvIngredients.setAdapter(adpIngredients);

        Bundle bundle = getArguments();
        mRecipe = (Recipe) bundle.getSerializable("recipe");
        boolean isTwoPane =bundle.getBoolean("isTwoPane",false);
        updateWidget();
        //udpate the last visited recipe to udpate the widget


        adpIngredients.swapAdapter(mRecipe.getIngredients());
        adpSteps = new StepsAdapter(new StepsAdapter.HandleStepOnClick() {
            @Override
            public void OnClickStep(Step step, int position) {
                ((RecipeDetailsCallBack) getActivity()).onStepClicked(mRecipe, position);
                mActiveItemPosition=position;

            }
        });
        rvSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSteps.setNestedScrollingEnabled(false);
        rvSteps.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        rvSteps.setAdapter(adpSteps);
        adpSteps.swapAdapter(mRecipe.getSteps());
        adpSteps.isTwoPane(isTwoPane);// to disable or enable item state
        if(savedInstanceState!=null)
        {
            mActiveItemPosition=savedInstanceState.getInt(BUNDLE_ACTIVE_ITEM_POSITION);
            adpSteps.changeTheActiveItem(mActiveItemPosition);
            rvSteps.scrollToPosition(mActiveItemPosition);
        }
        return rootView;
    }
    public void changeRecyclerViewActivitedItem(int position)
    {
        if(adpSteps!=null)
        {
            mActiveItemPosition=position;
            adpSteps.changeTheActiveItem(mActiveItemPosition);
        }
    }
    public void updateWidget() {
        int lastVisitedRecipeID = Utility.getRecentVisitedRecipeID(getContext());
        if (lastVisitedRecipeID != mRecipe.getId()) {
            Utility.setRecentVisitedRecipe(getContext(),Double.valueOf(mRecipe.getId()).intValue());
            Utility.setRecentVisitedRecipeName(getContext(),mRecipe.getName());


           // BakingWidgetProvider
        }
    }
}
