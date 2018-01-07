package com.example.admin.receptapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.List;

public class RecipeInfoActivity extends AppCompatActivity {
    TextView tv_title, tv_description, tv_ingredients, tv_instructions;
    ImageView iv_recipe;
    private RecipesDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        tv_title = (findViewById(R.id.tv_title));
        tv_description = (findViewById(R.id.tv_description));
        iv_recipe = (findViewById(R.id.iv_recipe));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            String recipe = (String) bundle.get("recipe");
            datasource = new RecipesDataSource(this);
            try {
                datasource.open();
            }catch (SQLiteException e){
                e.printStackTrace();
            }
            Recipe currentRecipe = datasource.getRecipe(recipe);
            tv_title.setText(currentRecipe.getTitle().toString());
            tv_description.setText(currentRecipe.getDescription().toString());
            byte[] photo = datasource.getRecipeImage(recipe);
            System.out.println("Blob 2 = " + photo);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(photo);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            iv_recipe.setImageBitmap(bitmap);
        }else{
            tv_title.setText("Hittade inte receptet");
        }




    }
}
