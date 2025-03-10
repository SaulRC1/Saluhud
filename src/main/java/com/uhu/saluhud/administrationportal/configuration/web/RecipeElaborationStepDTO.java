package com.uhu.saluhud.administrationportal.configuration.web;

import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;

/**
 *
 * @author juald
 */
public class RecipeElaborationStepDTO
{

    private int stepNumber;
    private String stepDescription;
    private Recipe recipeId;

    public int getStepNumber()
    {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber)
    {
        this.stepNumber = stepNumber;
    }

    public String getStepDescription()
    {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription)
    {
        this.stepDescription = stepDescription;
    }

    public Recipe getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(Recipe recipeId)
    {
        this.recipeId = recipeId;
    }
    
}
