package com.uhu.saluhud.mobileapp.dto.nutrition;

import java.util.List;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class RecipeDetailDTO 
{
    private String name;
    private String description;
    private String ingredientsDescription;
    private int kilocalories;
    private String imageSource;
    
    private List<RecipeCardAllergenicDTO> recipeAllergenics;
    private List<RecipeIngredientDTO> recipeIngredients;
    private List<RecipeElaborationStepDTO> elaborationSteps;

    public RecipeDetailDTO(String name, String description, String ingredientsDescription, 
            int kilocalories, String imageSource, List<RecipeCardAllergenicDTO> recipeAllergenics, 
            List<RecipeIngredientDTO> recipeIngredients, List<RecipeElaborationStepDTO> elaborationSteps)
    {
        this.name = name;
        this.description = description;
        this.ingredientsDescription = ingredientsDescription;
        this.kilocalories = kilocalories;
        this.imageSource = imageSource;
        this.recipeAllergenics = recipeAllergenics;
        this.recipeIngredients = recipeIngredients;
        this.elaborationSteps = elaborationSteps;
    }

    public RecipeDetailDTO()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getIngredientsDescription()
    {
        return ingredientsDescription;
    }

    public void setIngredientsDescription(String ingredientsDescription)
    {
        this.ingredientsDescription = ingredientsDescription;
    }

    public int getKilocalories()
    {
        return kilocalories;
    }

    public void setKilocalories(int kilocalories)
    {
        this.kilocalories = kilocalories;
    }

    public String getImageSource()
    {
        return imageSource;
    }

    public void setImageSource(String imageSource)
    {
        this.imageSource = imageSource;
    }

    public List<RecipeCardAllergenicDTO> getRecipeAllergenics()
    {
        return recipeAllergenics;
    }

    public void setRecipeAllergenics(List<RecipeCardAllergenicDTO> recipeAllergenics)
    {
        this.recipeAllergenics = recipeAllergenics;
    }

    public List<RecipeIngredientDTO> getRecipeIngredients()
    {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredientDTO> recipeIngredients)
    {
        this.recipeIngredients = recipeIngredients;
    }

    public List<RecipeElaborationStepDTO> getElaborationSteps()
    {
        return elaborationSteps;
    }

    public void setElaborationSteps(List<RecipeElaborationStepDTO> elaborationSteps)
    {
        this.elaborationSteps = elaborationSteps;
    }
}
