package com.example.admin.receptapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by Alexanders on 2017-11-13.
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

    private static final String DATABASE_NAME = "recipes.db";
    public static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_RECIPES
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text, "
            + COLUMN_DESCRIPTION + " text, "
            + COLUMN_INGREDIENTS + " text, "
            + COLUMN_INSTRUCTIONS + " text, "
            + COLUMN_IMAGE_BLOB + " blob, "
            + COLUMN_IMAGE_BLOB_SMALL + " blob)";

    public DBRecipeHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBRecipeHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

}