package com.example.admin.receptapp;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private RecipesDataSource datasource;
    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bottomNavigationView = (BottomNavigationView) (findViewById(R.id.navigation));

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                selectedFragment = HomeFragment.newInstance();
                                break;
                            case R.id.action_search:
                                selectedFragment = SearchFragment.newInstance();
                                break;
                            case R.id.action_addRecipe:
                                selectedFragment = AddRecipeFragment.newInstance();
                                break;
                            case R.id.action_favorites:
                                selectedFragment = FavoritesFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        transaction.commit();

        datasource = new RecipesDataSource(this);
        try {
            datasource.open();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

    }

    public void initDB(){
        try {
            datasource.insertFromFile(this, R.raw.init);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        try {
            datasource.open();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
    public void startInputRecipesActivity(View view){
        Intent intent = new Intent(this, InputRecipesActivity.class);
        startActivity(intent);
    }
    public void startBrowseActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, BrowseActivity.class);
        startActivity(intent);

    }

}
