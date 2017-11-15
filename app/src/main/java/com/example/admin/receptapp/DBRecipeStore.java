package com.example.admin.receptapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by admin on 2017-11-13.
 */

public class DBRecipeStore implements  RecipeStore {

    private SQLiteDatabase database;
    private DBRecipeHelper dbHelper;
    private String[] allColumns = { DBRecipeHelper.COLUMN_ID, DBRecipeHelper.COLUMN_MEMBER_NAME };

    public DBRecipeStore(Context context){
        dbHelper = new DBRecipeHelper(context);
    }

}
