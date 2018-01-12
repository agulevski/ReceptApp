package com.example.admin.receptapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the "landing" fragment that is displayed on application start. It shows two scrollable
 * lists of (currently all) recipes for the user to browse. The lists use the small version of a
 * recipes image.
 */


public class HomeFragment extends Fragment {
    RecipesDataSource datasource;
    ListView leftView, rightView;

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
        datasource.open();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Get number of rows in recipes
        long count = datasource.getRecipesCount();
        int indexCount = (int) count;

        //Get all small images for all recipes in a list
        List<Bitmap> images = datasource.getRecipeImgSmall();
        //Split list into two new lists that each contain half of the images
        List<Bitmap> firstHalfImage = images.subList(0, indexCount/2);
        List<Bitmap> secondHalfImage = images.subList(indexCount/2+1, indexCount);

        //Get all titles for all recipes in a list
        List<String> titles = datasource.getRecipeTitles();
        //Split list into two new lists that each contain half of the titles
        List<String> firstHalfTitle = titles.subList(0, indexCount/2);
        List<String> secondHalfTitle = titles.subList(indexCount/2+1, indexCount);

        //Create two custom adapters, one for each listview
        CustomListAdapter leftAdapter = new CustomListAdapter(getActivity(), firstHalfTitle, firstHalfImage);
        CustomListAdapter rightAdapter = new CustomListAdapter(getActivity(), secondHalfTitle, secondHalfImage);

        //Initiate XML
        leftView = (getView().findViewById(R.id.lv_leftlist));
        rightView = (getView().findViewById(R.id.lv_rightlist));
        //Attach adapters
        leftView.setAdapter(leftAdapter);
        rightView.setAdapter(rightAdapter);

        //Set listeners for both lists to start RecipeInfoActivity with clicked item
        leftView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), RecipeInfoActivity.class);
                String recipe = String.valueOf(adapterView.getItemAtPosition(position));
                intent.putExtra("recipe", recipe);
                startActivity(intent);
            }
        });
        rightView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), RecipeInfoActivity.class);
                String recipe = String.valueOf(adapterView.getItemAtPosition(position));
                intent.putExtra("recipe", recipe);
                startActivity(intent);
            }
        });

        datasource.close();


    }

}
