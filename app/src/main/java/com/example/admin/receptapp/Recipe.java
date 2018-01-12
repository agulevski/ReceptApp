package com.example.admin.receptapp;

/**
 *
 * Contains parameters for a Recipe and overrides toString();
 */

public class Recipe {
    private int id;
    private String title;
    private String description;
    private String ingredients;
    private String instructions;
    private byte[] photo;
    private byte[] photoSmall;

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

    public byte[] getPhoto(){return photo;}
    public void setPhoto(byte[] photo){this.photo = photo;}

    public byte[] getPhotoSmall(){return photoSmall;}
    public void setPhotoSmall(byte[] photoSmall){this.photoSmall = photoSmall;}

    @Override
    public String toString(){
        return title + description + ingredients + instructions;
    }


}
