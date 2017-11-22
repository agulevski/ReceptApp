package com.example.admin.receptapp;

import android.database.sqlite.SQLiteException;

import java.util.List;

/**
 * Created by admin on 2017-11-13.
 */

public interface RecipeStore {
    List<Recipe> getAllRecipes();
    List<Recipe> getRecipeByIngredients();
    void storeRecipe(Recipe recipe);
    void deleteRecipe(Recipe recipe);
    void open() throws SQLiteException;
    void close() throws SQLiteException;
}
