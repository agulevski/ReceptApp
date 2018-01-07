package com.example.admin.receptapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;


public class AddRecipeFragment extends Fragment {
    private RecipesDataSource datasource;
    ImageButton photoButton, addRecipeButton;
    EditText ingredientsText, instructionsText, titleText, descriptionText;
    LinearLayout linearLayout;
    RecipesDataSource dataSource;
    public static final int PICK_IMAGE = 1;
    Uri imageUri;



    public static AddRecipeFragment newInstance() {
        AddRecipeFragment fragment = new AddRecipeFragment();

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
        return inflater.inflate(R.layout.fragment_add_recipe, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        linearLayout = getView().findViewById(R.id.linearLayout);
        ingredientsText = getView().findViewById(R.id.et_Ingredients);
        instructionsText = getView().findViewById(R.id.et_Instructions);
        titleText = getView().findViewById(R.id.et_Title);
        descriptionText = getView().findViewById(R.id.et_Description);
        photoButton = getView().findViewById(R.id.ib_SelectPhoto);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);
            }
        });
        addRecipeButton = getView().findViewById(R.id.ib_AddRecipe);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get field values and call createRecipe method
                if(titleText.getText().toString().equals("") || descriptionText.getText().toString().equals("") || ingredientsText.getText().toString().equals("") || instructionsText.getText().toString().equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Fyll i alla fält först", Toast.LENGTH_LONG).show();
                }else{
                    Recipe recipe = datasource.createRecipe(titleText.getText().toString(), descriptionText.getText().toString(), ingredientsText.getText().toString(), instructionsText.getText().toString());
                    Toast.makeText(getActivity().getApplicationContext(), recipe.getTitle(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void selectImage(View view){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Välj foto");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            photoButton.setImageURI(imageUri);
            photoButton.setScaleType(ImageView.ScaleType.FIT_XY);
        }

    }




}
