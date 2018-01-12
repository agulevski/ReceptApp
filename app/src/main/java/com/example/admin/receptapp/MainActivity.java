package com.example.admin.receptapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * This activity displays a bottom navigation bar and inflates the fragment HomeFragment onCreate.
 * The bottom navigation bar uses navigation_items.xml found in res/menu/ as resource.
 * Fragments HomeFragment, SearchFragment, AddRecipeFragment, and FavoritesFragment are all accessed
 * through this bottom navigation bar.
 * MainActivity also creates an instance of DBRecipeHelper to create the database if it does not exist,
 * or if it exists and the database version has been updated.
 */
public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private SQLiteDatabase database;
    private DBRecipeHelper dbHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bottomNavigationView = (findViewById(R.id.navigation));

        //Initiate bottomnavigationview
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

        //Create DB if it does not exist or version is updated
        dbHelper = new DBRecipeHelper(this);

    }



}
