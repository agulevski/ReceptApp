package com.example.admin.receptapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.List;

/**
 * Created by admin on 2017-11-13.
 */

public interface RecipeStore {
    List<Recipe> getAllRecipes();
    List<String> getRecipeByIngredients(CharSequence query);
    Recipe getRecipe(CharSequence query);
    void storeRecipe(Recipe recipe);
    void deleteRecipe(Recipe recipe);
    SQLiteDatabase open() throws SQLException;
    void close();
}
