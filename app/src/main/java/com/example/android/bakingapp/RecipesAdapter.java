package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.R.id.iv_recipe_image;
import static com.example.android.bakingapp.R.id.tv_recipe_name;

/**
 * Created by eslam on 27-Oct-17.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    Context mContext;
    ArrayList<Recipe> mRecipes;

    HandleRecipeOnClick mHandleRecipeOnClick;
    public  interface  HandleRecipeOnClick
    {
        void recipeOnClick(Recipe recipe);
    }
    public RecipesAdapter(Context context , HandleRecipeOnClick handleRecipeOnClick) {
        mContext = context;
        mHandleRecipeOnClick=handleRecipeOnClick;
    }

    public void swapData(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutID = R.layout.item_recipe;
        View view = inflater.inflate(layoutID, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.setRecipeImage(mRecipes.get(position).getImage());
        holder.setRecipeName(mRecipes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mRecipes != null) return mRecipes.size();
        return 0;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(iv_recipe_image)
        ImageView recipeImage;
        @BindView(tv_recipe_name)
        TextView recipeName;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setRecipeImage(String url) {
            if (url.isEmpty() || url.equals("")) {
                recipeImage.setImageResource(R.drawable.eating_everything);
            } else {
                Picasso.with(mContext).load(url).resize(250,350).into(recipeImage);
            }

        }
        public void setRecipeName(String name)
        {
            recipeName.setText(name);
        }
        @Override
        public void onClick(View view) {
            mHandleRecipeOnClick.recipeOnClick(mRecipes.get(getAdapterPosition()));
        }
    }
}
