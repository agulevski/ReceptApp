package com.example.admin.receptapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017-11-15.
 */

public class RecipesDataSource {

    // Database fields
    private SQLiteDatabase database;
    private DBRecipeHelper dbHelper;
    private String[] allColumns = { DBRecipeHelper.COLUMN_ID,
            DBRecipeHelper.COLUMN_TITLE,
            DBRecipeHelper.COLUMN_DESCRIPTION,
            DBRecipeHelper.COLUMN_INGREDIENTS,
            DBRecipeHelper.COLUMN_INSTRUCTIONS};

    public RecipesDataSource(Context context) {
        dbHelper = new DBRecipeHelper(context);
        //insertFromFile(context, R.raw.init);
    }


    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public int insertFromFile(Context context, int resourceId) throws IOException {
        int result = 0;
        //Open resource
        InputStream insertStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertStream));

        while (insertReader.ready()){
            String insertStatement = insertReader.readLine();
            database.execSQL(insertStatement);
            result++;

        }
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

    public void deleteRecipe(Recipe recipe) {
        int id = recipe.getId();
        System.out.println("Recipe deleted with id: " + id);
        database.delete(DBRecipeHelper.TABLE_RECIPES, DBRecipeHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        Cursor cursor = database.query(DBRecipeHelper.TABLE_RECIPES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
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
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getInt(0));
        recipe.setTitle(cursor.getString(1));
        recipe.setDescription(cursor.getString(2));
        recipe.setIngredients(cursor.getString(3));
        recipe.setInstructions(cursor.getString(4));
        return recipe;
    }
}