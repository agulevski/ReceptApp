package com.example.admin.receptapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.IOException;
import java.util.List;

/**
 * Created by admin on 2017-11-13.
 */

public interface RecipeStore {
    List<Recipe> getAllRecipes();
    List<String> getRecipeByIngredients(CharSequence query);
    Recipe getRecipe(CharSequence query);
    Recipe createRecipe(String title, String description, String ingredients, String instructions);
    void storeRecipe(Recipe recipe);
    void deleteRecipe(Recipe recipe);
    SQLiteDatabase open() throws SQLException;
    int insertFromFile(Context context,int i) throws IOException;
    //void insertImages(Context context) throws IOException;
    void close();
}
