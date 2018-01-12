package com.example.admin.receptapp;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Shows detailed information about a chosen recipe, such as photo, title, description,
 * ingredients and instructions.
 * This activity is also used to save a recipe as favorite. Favorites are saved with ib_favorite.
 */
public class RecipeInfoActivity extends AppCompatActivity {
    TextView tv_title, tv_description, tv_ingredients, tv_instructions;
    ImageView iv_recipe;
    ImageButton ib_favorite;
    private RecipesDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        tv_title = (findViewById(R.id.tv_title));
        tv_description = (findViewById(R.id.tv_description));
        tv_ingredients = (findViewById(R.id.tv_ingredients));
        tv_instructions = (findViewById(R.id.tv_instructions));
        iv_recipe = (findViewById(R.id.iv_recipe));
        ib_favorite = (findViewById(R.id.ib_favorite));


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

            String recipe = (String) bundle.get("recipe");
            datasource = new RecipesDataSource(this);
            try {
                datasource.open();
            }catch (SQLiteException e){
                e.printStackTrace();
            }
            final Recipe currentRecipe = datasource.getRecipe(recipe);
            //If recipe is in favorites change favorite icon to red
            if(datasource.isFavorite(currentRecipe.getId())){
                ib_favorite.setImageResource(R.drawable.ic_favorite_red_900_24dp);
            }

            tv_title.setText(currentRecipe.getTitle());
            tv_description.setText(currentRecipe.getDescription());
            tv_ingredients.setText(currentRecipe.getIngredients());
            tv_instructions.setText(currentRecipe.getInstructions());
            byte[] photo = datasource.getRecipeImage(currentRecipe.getTitle());
            Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            iv_recipe.setImageBitmap(bitmap);


        ib_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If not in favorites, add to favorite
                if (!datasource.isFavorite(currentRecipe.getId())) {
                    datasource.addToFavorites(currentRecipe.getId());
                    ib_favorite.setImageResource(R.drawable.ic_favorite_red_900_24dp);
                    //If in favorites, remove and change icon to gray
                }else{
                    datasource.deleteFavorite(currentRecipe.getId());
                    ib_favorite.setImageResource(R.drawable.ic_favorite_grey_600_24dp);
                }
            }
        });




    }
    //Kill activity on back press
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
