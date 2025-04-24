package com.uhu.saluhud.mobileapp.dto.nutrition;

import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class RecipeCardDTO 
{
    private long recipeID;
    private String recipeName;
    private int recipeKcal;
    private String recipeImageSource;
    private List<RecipeCardAllergenicDTO> recipeAllergenics;

    public RecipeCardDTO(long recipeID, String recipeName, int recipeKcal, String recipeImageSource, List<RecipeCardAllergenicDTO> recipeAllergenics)
    {
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.recipeKcal = recipeKcal;
        this.recipeImageSource = recipeImageSource;
        this.recipeAllergenics = recipeAllergenics;
    }

    public long getRecipeID()
    {
        return recipeID;
    }

    public void setRecipeID(long recipeID)
    {
        this.recipeID = recipeID;
    }

    public String getRecipeName()
    {
        return recipeName;
    }

    public void setRecipeName(String recipeName)
    {
        this.recipeName = recipeName;
    }

    public int getRecipeKcal()
    {
        return recipeKcal;
    }

    public void setRecipeKcal(int recipeKcal)
    {
        this.recipeKcal = recipeKcal;
    }

    public List<RecipeCardAllergenicDTO> getRecipeAllergenics()
    {
        return recipeAllergenics;
    }

    public void setRecipeAllergenics(List<RecipeCardAllergenicDTO> recipeAllergenics)
    {
        this.recipeAllergenics = recipeAllergenics;
    }

    public String getRecipeImageSource()
    {
        return recipeImageSource;
    }

    public void setRecipeImageSource(String recipeImageSource)
    {
        this.recipeImageSource = recipeImageSource;
    }
}
