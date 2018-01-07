package com.example.admin.receptapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

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
            DBRecipeHelper.COLUMN_INSTRUCTIONS,
            DBRecipeHelper.COLUMN_IMAGE_BLOB};

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

        //Read init.sql line by line. First iteration gives SQLiteException because of "PRAGMA ENCODING..." being the first line in init.sql
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
    public void insertImages(Context context) throws IOException{
      AssetManager assetManager = context.getResources().getAssets();
      InputStream inputStream;
      Bitmap bitmap = null;
      String[] fileNames = assetManager.list("img");
      ByteArrayOutputStream stream = new ByteArrayOutputStream();

      for(String name : fileNames){
          ContentValues imgValues = new ContentValues();
          try{
              inputStream = assetManager.open("img/"+name);
              if (inputStream != null)
                  Log.d(TAG, "Inserted: " + name);
              bitmap = BitmapFactory.decodeStream(inputStream);
              bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
              byte[] image = stream.toByteArray();
              Log.d(TAG, "Image: "+ image);
              imgValues.put(DBRecipeHelper.COLUMN_IMAGE_BLOB, image);
              database.insert(DBRecipeHelper.TABLE_RECIPES, null, imgValues);



          }catch (IOException e){
              e.printStackTrace();
          }


      }
      database.close();
    }



    public Recipe createRecipe(String title, String description, String ingredients, String instructions) {
        ContentValues values = new ContentValues();
        values.put(DBRecipeHelper.COLUMN_TITLE, title);
        values.put(DBRecipeHelper.COLUMN_DESCRIPTION, description);
        values.put(DBRecipeHelper.COLUMN_INGREDIENTS, ingredients);
        values.put(DBRecipeHelper.COLUMN_INSTRUCTIONS, instructions);

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
        newRecipe.setPhoto(cursor.getBlob(5));
        newRecipe.setPhotoSmall(cursor.getBlob(6));
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
    public byte[] getRecipeImage(CharSequence query){
        Cursor c = database.rawQuery("SELECT "+DBRecipeHelper.COLUMN_IMAGE_BLOB+" FROM recipes WHERE "+DBRecipeHelper.COLUMN_TITLE+" = '" + query.toString()+"'", null);
        c.moveToFirst();
        byte[] photo = c.getBlob(0);
        System.out.println("Blob 1 = " + photo);
        return photo;

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