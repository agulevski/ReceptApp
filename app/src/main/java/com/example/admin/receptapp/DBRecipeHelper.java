package com.example.admin.receptapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import static android.content.ContentValues.TAG;


/**
 *
 * DBHelper class to create and initialize the database with content from /res/raw/init.sql and
 * /assets/. Inserts 20 full recipes with images if onCreate() is called.
 *
 */

public class DBRecipeHelper extends SQLiteOpenHelper {

    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_INGREDIENTS = "ingredients";
    public static final String COLUMN_INSTRUCTIONS = "instructions";
    public static final String COLUMN_IMAGE_BLOB = "imgBlob";
    public static final String COLUMN_IMAGE_BLOB_SMALL = "imgBlobSmall";

    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_RECIPEID = "recipe_id";

    private static final String DATABASE_NAME = "recipes.db";
    public static final int DATABASE_VERSION = 1;

    private Context context;

    // Database creation sql statement
    private static final String CREATE_RECIPES = "create table "
            + TABLE_RECIPES
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text, "
            + COLUMN_DESCRIPTION + " text, "
            + COLUMN_INGREDIENTS + " text, "
            + COLUMN_INSTRUCTIONS + " text, "
            + COLUMN_IMAGE_BLOB + " blob, "
            + COLUMN_IMAGE_BLOB_SMALL + " blob)";
    private static final String CREATE_FAVORITES = "create table "
            + TABLE_FAVORITES
            + " (" + COLUMN_RECIPEID + " integer)";


    public DBRecipeHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;


    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //Creates database tables
        database.execSQL(CREATE_RECIPES);
        database.execSQL(CREATE_FAVORITES);

        //Insert content from files in /raw/ and /assets/
        try {
            int insertedRows = insertFromFile(database, context, R.raw.init);
            Log.d(TAG, "Initial insert: "+insertedRows+" items.");
        }catch (IOException e){
            e.printStackTrace();
        }


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBRecipeHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);

        onCreate(db);
    }

    public int insertFromFile(SQLiteDatabase database, Context context, int resourceId) throws IOException {
        int result = 0;
        //Open resource
        InputStream insertStream = context.getResources().openRawResource(resourceId);
        Scanner insertReader = new Scanner(new InputStreamReader(insertStream));
        //Compile insert statement
        SQLiteStatement insertStatement = database.compileStatement("INSERT INTO recipes(title, description, ingredients, instructions, imgBlob, imgBlobSmall) VALUES(?, ?, ?, ?, ?, ?)");
        int i = 0; //int to keep track of index in []fileNames arrays, same number of images in both /img/ and /img_small/

        while (insertReader.hasNext()){
            AssetManager assetManager = context.getResources().getAssets();
            InputStream inputStream;
            Bitmap bitmap;
            //Create two lists with filenames, one for each folder in /assets/
            String[] fileNames = assetManager.list("img");
            String[] fileNamesSmall = assetManager.list("img_small");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //Bind image in assets/img/ to insertStatement
            try{
                inputStream = assetManager.open("img/"+fileNames[i]);
                if (inputStream != null)
                    Log.d(TAG, "Inserted image: " + fileNames[i]);
                bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                byte[] image = stream.toByteArray();
                insertStatement.bindBlob(5, image); //Bind image


            }catch (IOException e){
                e.printStackTrace();
            }
            //Bind image in assets/img_small/ to insertStatement
            try {
                inputStream = assetManager.open("img_small/"+fileNamesSmall[i]);
                if (inputStream != null)
                    Log.d(TAG, "Inserted small image: " + fileNamesSmall[i]);
                bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                byte[] image = stream.toByteArray();
                insertStatement.bindBlob(6, image); //Bind small image
            } catch (IOException e){
                e.printStackTrace();
            }
            String line = insertReader.nextLine();
            String details[] = line.split("','"); //Split strings
            //Bind raw/init.sql nextLine to insertStatement
            String recipeTitle = details[0];
            insertStatement.bindString(1, recipeTitle); //First string to title
            String recipeDescription = details[1];
            insertStatement.bindString(2, recipeDescription); //Second string to description
            String recipeIngredients = details[2];
            insertStatement.bindString(3, recipeIngredients); //Third string to ingredients
            String recipeInstructions = details[3];
            insertStatement.bindString(4, recipeInstructions); //Fourth string to instructions

            //Execute insertStatement
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

        //Return number of rows inserted
        return result;
    }
}