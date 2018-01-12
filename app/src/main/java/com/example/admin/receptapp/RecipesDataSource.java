package com.example.admin.receptapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * This class handles database operations. For a quick overview of all methods see RecipeStore.
 *
 */

public class RecipesDataSource implements RecipeStore {

    //Database fields
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


    public Recipe createRecipe(String title, String description, String ingredients, String instructions, byte[] photo, byte[] photoSmall) {
        ContentValues values = new ContentValues();
        values.put(DBRecipeHelper.COLUMN_TITLE, title);
        values.put(DBRecipeHelper.COLUMN_DESCRIPTION, description);
        values.put(DBRecipeHelper.COLUMN_INGREDIENTS, ingredients);
        values.put(DBRecipeHelper.COLUMN_INSTRUCTIONS, instructions);
        values.put(DBRecipeHelper.COLUMN_IMAGE_BLOB, photo);
        values.put(DBRecipeHelper.COLUMN_IMAGE_BLOB_SMALL, photoSmall);

        long insertId = database.insert(DBRecipeHelper.TABLE_RECIPES, null,
                values);
        //TODO maybe remove this and set method to void
        Cursor cursor = database.query(DBRecipeHelper.TABLE_RECIPES,
                allColumns, DBRecipeHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Recipe newRecipe = cursorToRecipe(cursor);
        cursor.close();
        return newRecipe;
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
        c.close();
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
        while (!c.isAfterLast()){
            byte[] img = c.getBlob(0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            imgSmall.add(bitmap);
            c.moveToNext();
        }
        c.close();
        return imgSmall;
    }
    public List<String> getRecipeTitles(){
        List<String> titles = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT "+ DBRecipeHelper.COLUMN_TITLE + " FROM " + DBRecipeHelper.TABLE_RECIPES, null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            String title = c.getString(0);
            titles.add(title);
            c.moveToNext();
        }
        c.close();
        return titles;
    }

    public void addToFavorites(int recipeId){
        ContentValues id = new ContentValues();
        id.put(DBRecipeHelper.COLUMN_RECIPEID, recipeId);
        database.insert(DBRecipeHelper.TABLE_FAVORITES, null, id);
    }

    public List<Bitmap> getFavoriteRecipeImgs(){
        List<Bitmap> imgSmall = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT imgBlobSmall FROM recipes WHERE id in (SELECT recipe_id from favorites)", null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            byte[] img = c.getBlob(0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            imgSmall.add(bitmap);
            c.moveToNext();
        }
        c.close();
        return imgSmall;
    }

    public List<String> getFavoriteRecipeTitle(){
        List<String> titles = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT title FROM recipes WHERE id in (SELECT recipe_id from favorites)", null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            String title = c.getString(0);
            titles.add(title);
            c.moveToNext();
        }
        c.close();
        return titles;
    }

    public void deleteFavorite(int id){
        database.delete(DBRecipeHelper.TABLE_FAVORITES, DBRecipeHelper.COLUMN_RECIPEID + "=" + id, null);
    }

    public boolean isFavorite(int id){
        Cursor c = database.rawQuery("SELECT count(*) FROM favorites WHERE recipe_id = "+id, null);
        c.moveToFirst();
        if(c.getInt(0) < 1) {
            c.close();
            return false;
        }else {
            c.close();
            return true;
        }

    }

    public long getRecipesCount() {
        long count  = DatabaseUtils.queryNumEntries(database, DBRecipeHelper.TABLE_RECIPES);
        return count;
    }

}