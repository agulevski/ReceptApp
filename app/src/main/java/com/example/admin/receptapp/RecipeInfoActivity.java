package com.example.admin.receptapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class RecipeInfoActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        textView = (TextView) (findViewById(R.id.textView));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String recipe;
        if(bundle != null){
            recipe = (String) bundle.get("recipe");
            RecipesDataSource datasource = new RecipesDataSource(this);
            Recipe currentRecipe = datasource.getRecipe(recipe);

            textView.setText(currentRecipe.toString());
        }else{
            textView.setText("Hittade inte receptet");
        }




    }
}
