package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.models.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.R.id.tv_ingredients_measure;
import static com.example.android.bakingapp.R.id.tv_ingredients_name;
import static com.example.android.bakingapp.R.id.tv_ingredients_quantity;

/**
 * Created by eslam on 28-Oct-17.
 */

public class IngredientsAdapter  extends RecyclerView.Adapter<IngredientsAdapter.IngredientHolder>{
    private ArrayList<Ingredient> mIngredients;

    public void swapAdapter( ArrayList<Ingredient> ingredients)
    {
        mIngredients =ingredients;
        notifyDataSetChanged();
    }
    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        int layoutId=R.layout.list_item_ingredient;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(layoutId,parent,false);
        IngredientHolder ingredientHolder = new IngredientHolder(view);
        return ingredientHolder;
    }

    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);
        holder.setName(ingredient.getIngredient());
        holder.setMeasure(ingredient.getMeasure());
        holder.setQuantity(String.valueOf(ingredient.getQuantity()));
    }

    @Override
    public int getItemCount() {
        if(mIngredients!=null)return mIngredients.size();
        return 0;
    }

    public class IngredientHolder extends RecyclerView.ViewHolder
    {
        @BindView(tv_ingredients_name)
        TextView nameTextView;
        @BindView(tv_ingredients_quantity)
        TextView quantityTextView;
        @BindView(tv_ingredients_measure)
        TextView measureTextView;
        public IngredientHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setName(String name){nameTextView.setText(name);}
        public void setQuantity(String quantity){quantityTextView.setText(quantity);}
        public void setMeasure(String measure){measureTextView.setText(measure);}
    }
}
