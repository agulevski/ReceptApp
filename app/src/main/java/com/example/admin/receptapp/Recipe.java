package com.example.admin.receptapp;

/**
 * Created by admin on 2017-11-13.
 */

public class Recipe {
    private String title;
    private String description;
    private String ingredients;
    private String instructions;
    //private String image;

    public Recipe(String title, String description, String ingredients, String instructions){
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
       // this.image = image;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getIngredients(){
        return ingredients;
    }

    public String getInstructions(){
        return instructions;
    }

    /*public String getImage(){
        return image;
    }*/
}
