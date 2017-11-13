package com.example.admin.receptapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017-11-13.
 */

public class FakeRecipeStore implements RecipeStore {

    public FakeRecipeStore(Context context){
        DBRecipeStore();
    }
    @Override
    public List<Recipe> getAllRecipes(){
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("Pasta",
                "pasta är gott",
                "fullkornspasta!!",
                "koka pastan"));
        return recipes;
    }
}
