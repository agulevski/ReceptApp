package com.example.admin.receptapp;

import java.util.List;

/**
 * Created by admin on 2017-11-13.
 */

public interface RecipeStore {
    public List<Recipe> getAllRecipes();
    public List<Recipe> getRecipeByIngredients();
    void storeRecipe(Recipe recipe);
    void deleteRecipe(Recipe recipe);
    void open() throws RecipeStoreException;
    void close() throws RecipeStoreException;
}
