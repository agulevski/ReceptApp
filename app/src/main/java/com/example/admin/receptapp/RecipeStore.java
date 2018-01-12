package com.example.admin.receptapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.util.List;

/**
 *
 * Interface of all the public methods used in RecipesDataSource.
 */

public interface RecipeStore {
    //Returns a recipe list of all recipes
    List<Recipe> getAllRecipes();
    //Returns a string list of recipe titles found from ingredients query
    List<String> getRecipeByIngredients(CharSequence query);
    //Returns a single recipe found from query
    Recipe getRecipe(CharSequence query);
    //Returns a single recipe image from query
    byte[] getRecipeImage(CharSequence query);
    //Returns a bitmap list of all small images for all recipes
    List<Bitmap> getRecipeImgSmall();
    //Returns a string list of all recipe titles
    List<String> getRecipeTitles();
    //Takes a recipe id and adds it to the favorites table
    void addToFavorites(int recipeId);
    //Returns a bitmap list of all images for recipes in the favorites table
    List<Bitmap> getFavoriteRecipeImgs();
    //Returns a string list of all titles for recipes in the favorites table
    List<String> getFavoriteRecipeTitle();
    //Takes a recipe id and deletes it from the favorites table
    void deleteFavorite(int id);
    //Takes a recipe id and checks if it exists in the favorites table
    boolean isFavorite(int id);
    //Takes recipe parameters and inserts a new row in recipes table
    Recipe createRecipe(String title, String description, String ingredients, String instructions, byte[] photo, byte[] photoSmall);
    //Returns number of rows in recipes
    long getRecipesCount();
    //Opens the database
    SQLiteDatabase open() throws SQLException;
    //Closes the database
    void close();
}
