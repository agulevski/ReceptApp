package com.example.admin.receptapp;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class HomeFragment extends Fragment {
    RecipesDataSource datasource;
    ListView leftView, rightView;
    TextView textView;
    ImageView imageView;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datasource = new RecipesDataSource(getActivity());
        SQLiteDatabase db = datasource.open();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        List<Bitmap> images = datasource.getRecipeImgSmall();
        List<String> titles = datasource.getRecipeTitles();
        CustomListAdapter adapter = new CustomListAdapter(getActivity(), titles, images);

        leftView = (getView().findViewById(R.id.leftlist));
        rightView = (getView().findViewById(R.id.rightlist));
        leftView.setAdapter(adapter);
        rightView.setAdapter(adapter);


    }

    public int displayRecipes(){
        int countRecipes = 0;

        //Get all recipes
        List<Recipe> values = datasource.getAllRecipes();
        StringBuilder builder = new StringBuilder();

        //Loop through arraylist
        for (Recipe recipes : values) {
            builder.append((recipes + "\n"));
            countRecipes++;
        }
        //Display all content in textview
        textView.setText(builder.toString());

        //Return number of recipes displayed
        return countRecipes;
    }
}
