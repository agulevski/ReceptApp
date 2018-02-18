package com.example.admin.receptapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

/**
 * This fragment is used to create a new recipe. No edittext fields can be left empty. The image field
 * can be left empty and a default image will be used.
 */

public class AddRecipeFragment extends Fragment {
    private RecipesDataSource datasource;
    ImageButton ib_addPhoto, ib_addRecipe, ib_removePhoto;
    EditText et_title, et_description, et_ingredients, et_instructions;
    LinearLayout linearLayout;
    public static final int TAKE_PHOTO = 0;
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
        datasource.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_recipe, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Initiate XML
        linearLayout = getView().findViewById(R.id.linearLayout);
        et_title = getView().findViewById(R.id.et_Title);
        et_description = getView().findViewById(R.id.et_Description);
        et_ingredients = getView().findViewById(R.id.et_Ingredients);
        et_instructions = getView().findViewById(R.id.et_Instructions);
        ib_addPhoto = getView().findViewById(R.id.ib_SelectPhoto);
        ib_removePhoto = getView().findViewById(R.id.ib_removePhoto);

        ib_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceDialog();
                //selectImage(view);
            }
        });
        ib_removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set ib_addPhoto back to original photo
                ib_addPhoto.setScaleType(ImageView.ScaleType.CENTER);
                ib_addPhoto.setImageResource(R.drawable.ic_photo_camera_black_36dp);

            }
        });
        ib_addRecipe = getView().findViewById(R.id.ib_AddRecipe);
        ib_addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get edittext values and call createRecipe method if none are empty
                if(isEmpty(et_title) || isEmpty(et_description) || isEmpty(et_ingredients) || isEmpty(et_description)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Fyll i alla fält först", Toast.LENGTH_LONG).show();
                }else{
                    //Get chosen image (that is set on imagebutton)
                    Bitmap bitmap = ((BitmapDrawable)ib_addPhoto.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] photo = stream.toByteArray();

                    //Resize chosen image to 500x223px to use in homefragment & favoritesfragment
                    Bitmap bitmapSmall = ((BitmapDrawable)ib_addPhoto.getDrawable()).getBitmap();
                    ByteArrayOutputStream streamSmall = new ByteArrayOutputStream();
                    bitmapSmall = Bitmap.createScaledBitmap(bitmapSmall, 500, 223, false );
                    bitmapSmall.compress(Bitmap.CompressFormat.JPEG, 100, streamSmall);
                    byte[] photoSmall = streamSmall.toByteArray();

                    //Create new recipe, default image is inserted if none is chosen
                    Recipe recipe = datasource.createRecipe(et_title.getText().toString(), et_description.getText().toString(), et_ingredients.getText().toString(), et_instructions.getText().toString(), photo, photoSmall);
                    //Toast with title of created recipe
                    Toast.makeText(getActivity().getApplicationContext(), "Skapade nytt recept: " +recipe.getTitle(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    //Let user select image from storage
    public void selectImage(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Välj foto");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    //Open camera
    public void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,TAKE_PHOTO);

    }

    //Display chosen image on ib_addPhoto
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
       if(resultCode == RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ib_addPhoto.setImageBitmap(photo);
            ib_addPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    //Check if an edittext is empty
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    //Alertdialog with 2 choices
    public void choiceDialog(){
        CharSequence choices[] = new CharSequence[] {"Ta foto", "Välj från enheten"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Välj bild");
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                if(which==0){
                    takePhoto();
                }else if (which==1){
                    selectImage();
                }
            }
        });
        builder.show();
    }





}
