package com.example.admin.receptapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
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
import java.util.Scanner;

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
            DBRecipeHelper.COLUMN_IMAGE_BLOB,
            DBRecipeHelper.COLUMN_IMAGE_BLOB_SMALL};

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
        Scanner insertReader = new Scanner(new InputStreamReader(insertStream));
        //insertReader.useDelimiter("','");
        //Compile insert statement
        SQLiteStatement insertStatement = database.compileStatement("INSERT INTO recipes(title, description, ingredients, instructions, imgBlob, imgBlobSmall) VALUES(?, ?, ?, ?, ?, ?)");
        int i = 0; //int to keep track of index in []fileNames arrays, same number of images in both /img/ and /img_small/

        while (insertReader.hasNext()){
            System.out.println("i = "+i);

            AssetManager assetManager = context.getResources().getAssets();
            InputStream inputStream;
            Bitmap bitmap;
            String[] fileNames = assetManager.list("img");
            System.out.println(fileNames[i]);
            String[] fileNamesSmall = assetManager.list("img_small");
            System.out.println(fileNamesSmall[i]);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //Bind image in /img/ to insertStatement
                try{
                    inputStream = assetManager.open("img/"+fileNames[i]);
                    if (inputStream != null)
                        Log.d(TAG, "Inserted: " + fileNames[i]);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                    byte[] image = stream.toByteArray();
                    Log.d(TAG, "Image: "+ image);
                    insertStatement.bindBlob(5, image);


                }catch (IOException e){
                    e.printStackTrace();
                }
                //Bind image in /img_small/ to insertStatement
                try {
                    inputStream = assetManager.open("img_small/"+fileNamesSmall[i]);
                    if (inputStream != null)
                        Log.d(TAG, "Inserted small image: " + fileNamesSmall[i]);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                    byte[] image = stream.toByteArray();
                    Log.d(TAG, "Image small: "+ image);
                    insertStatement.bindBlob(6, image);
                } catch (IOException e){
                    e.printStackTrace();
                }
            //Bind nextLine to insertStatement
            String line = insertReader.nextLine();
            String details[] = line.split("','");
            String recipeTitle = details[0];
            insertStatement.bindString(1, recipeTitle);
            String recipeDescription = details[1];
            insertStatement.bindString(2, recipeDescription);
            String recipeIngredients = details[2];
            insertStatement.bindString(3, recipeIngredients);
            String recipeInstructions = details[3];
            insertStatement.bindString(4, recipeInstructions);
            try {
                insertStatement.execute();
            }catch (SQLiteException sql){
                sql.printStackTrace();
            }

            result++;
            i++;


        }
        //Close reader
        insertReader.close();
        //Close DB
        database.close();

        return result;
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
        Cursor c = database.rawQuery("SELECT "+DBRecipeHelper.COLUMN_IMAGE_BLOB+" FROM recipes WHERE "+DBRecipeHelper.COLUMN_TITLE+" LIKE '%" + query.toString()+"%'", null);
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

    public List<Bitmap> getRecipeImgSmall(){
        List<Bitmap> imgSmall = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT "+ DBRecipeHelper.COLUMN_IMAGE_BLOB_SMALL + " FROM " + DBRecipeHelper.TABLE_RECIPES, null);
        c.moveToFirst();
        System.out.println(c.getBlob(0));
        while (!c.isAfterLast()){
            byte[] img = c.getBlob(0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            imgSmall.add(bitmap);
            c.moveToNext();
        }
        return imgSmall;
    }
    public List<String> getRecipeTitles(){
        List<String> titles = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT "+ DBRecipeHelper.COLUMN_TITLE + " FROM " + DBRecipeHelper.TABLE_RECIPES, null);
        c.moveToFirst();
        System.out.println(c.getString(0));
        while (!c.isAfterLast()){
            String title = c.getString(0);
            titles.add(title);
            c.moveToNext();
        }
        return titles;
    }
}