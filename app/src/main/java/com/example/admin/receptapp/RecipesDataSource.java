package com.example.admin.receptapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017-11-15.
 *
 *
 */

public class RecipesDataSource implements RecipeStore {

    // Database fields
    private SQLiteDatabase database;
    private DBRecipeHelper dbHelper;
    private String[] allColumns = {DBRecipeHelper.COLUMN_ID,
            DBRecipeHelper.COLUMN_TITLE,
            DBRecipeHelper.COLUMN_DESCRIPTION,
            DBRecipeHelper.COLUMN_INGREDIENTS,
            DBRecipeHelper.COLUMN_INSTRUCTIONS};

    public RecipesDataSource(Context context) {
        dbHelper = new DBRecipeHelper(context);
    }

    public SQLiteDatabase open(){
        this.database = dbHelper.getWritableDatabase();
        return database;
    }

    public void close(){
        dbHelper.close();
    }


    public int insertFromFile(Context context, int resourceId) throws IOException {
        int result = 0;
        //Open resource
        InputStream insertStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertStream));

        while (insertReader.ready()){
            String insertStatement = insertReader.readLine();
            try {
                database.execSQL(insertStatement);
            }catch (SQLiteException sql){
                sql.printStackTrace();
            }
            result++;

        }
        //Close reader
        insertReader.close();

        return result;
    }


    public Recipe createRecipe(String recipe) {
        ContentValues values = new ContentValues();
        values.put(DBRecipeHelper.COLUMN_TITLE, recipe);
        values.put(DBRecipeHelper.COLUMN_DESCRIPTION, recipe);
        values.put(DBRecipeHelper.COLUMN_INGREDIENTS, recipe);
        values.put(DBRecipeHelper.COLUMN_INSTRUCTIONS, recipe);

        long insertId = database.insert(DBRecipeHelper.TABLE_RECIPES, null,
                values);
        Cursor cursor = database.query(DBRecipeHelper.TABLE_RECIPES,
                allColumns, DBRecipeHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Recipe newRecipe = cursorToRecipe(cursor);
        cursor.close();
        return newRecipe;
    }

    public void storeRecipe(Recipe recipe){}

    public void deleteRecipe(Recipe recipe) {
        int id = recipe.getId();
        System.out.println("Recipe deleted with id: " + id);
        database.delete(DBRecipeHelper.TABLE_RECIPES, DBRecipeHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        //Query DB for all recipes in the table
        Cursor cursor = database.query(DBRecipeHelper.TABLE_RECIPES,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        //Loop through cursor, save each row as new recipe
        while (!cursor.isAfterLast()) {
            Recipe recipe = cursorToRecipe(cursor);
            recipes.add(recipe);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return recipes;
    }

    private Recipe cursorToRecipe(Cursor cursor) {
        //Create a new recipe from cursor values
        Recipe newRecipe = new Recipe();
        newRecipe.setId(cursor.getInt(0));
        newRecipe.setTitle(cursor.getString(1));
        newRecipe.setDescription(cursor.getString(2));
        newRecipe.setIngredients(cursor.getString(3));
        newRecipe.setInstructions(cursor.getString(4));
        return newRecipe;
    }

    private Recipe cursorToTitle(Cursor cursor) {
        //Create a new recipe from cursor values
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle(cursor.getString(0));
        return newRecipe;
    }
    //Get a single recipe with searchparam title
    public Recipe getRecipe(CharSequence query){
        Cursor c = database.rawQuery("SELECT * FROM recipes WHERE title = '" + query.toString()+"'", null);
        c.moveToFirst();
        Recipe recipe = cursorToRecipe(c);

        c.close();
        return recipe;
    }

    public List<String> getRecipeByIngredients(CharSequence query){
        List<String> recipesByIngredients = new ArrayList<>();
       //Select ingredients by user input using LIKE
       Cursor c = database.rawQuery("SELECT title FROM recipes WHERE ingredients LIKE '%" + query.toString() +"%'", null);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            Recipe recipe = cursorToTitle(c);
            recipesByIngredients.add(recipe.getTitle());
            c.moveToNext();
        }
        c.close();
        return recipesByIngredients;
    }
}