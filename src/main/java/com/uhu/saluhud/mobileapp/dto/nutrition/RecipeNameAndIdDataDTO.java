package com.uhu.saluhud.mobileapp.dto.nutrition;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class RecipeNameAndIdDataDTO 
{
    private long recipeId;
    private String recipeName;

    public RecipeNameAndIdDataDTO()
    {
    }

    public RecipeNameAndIdDataDTO(long recipeId, String recipeName)
    {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
    }

    public long getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(long recipeId)
    {
        this.recipeId = recipeId;
    }

    public String getRecipeName()
    {
        return recipeName;
    }

    public void setRecipeName(String recipeName)
    {
        this.recipeName = recipeName;
    }
    
}
