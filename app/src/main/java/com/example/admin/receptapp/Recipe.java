package com.example.admin.receptapp;

/**
 * Created by admin on 2017-11-13.
 *
 * Contains parameters for a Recipe and overrides toString();
 */

public class Recipe {
    private int id;
    private String title;
    private String description;
    private String ingredients;
    private String instructions;

    public int getId() { return id;}
    public void setId(int id) {this.id = id;}

    public String getTitle(){
        return title;
    }
    public void setTitle(String title) { this.title = title;}

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){ this.description = description;}

    public String getIngredients(){
        return ingredients;
    }
    public void setIngredients(String ingredients) {this.ingredients = ingredients;}

    public String getInstructions(){
        return instructions;
    }
    public void setInstructions(String instructions) {this.instructions = instructions;}

    @Override
    public String toString(){
        return title + description + ingredients + instructions;
    }


}
