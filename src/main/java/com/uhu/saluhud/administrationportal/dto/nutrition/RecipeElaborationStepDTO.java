package com.uhu.saluhud.administrationportal.dto.nutrition;

import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;

/**
 *
 * @author juald
 */
public class RecipeElaborationStepDTO
{

    private long id;

    private String stepDescription;

    private int stepNumber;

    private RecipeDTO recipeDTO;

    public RecipeElaborationStepDTO(long id, String stepDescription, int stepNumber, 
            RecipeDTO recipeDTO)
    {
        this.id = id;
        this.stepDescription = stepDescription;
        this.stepNumber = stepNumber;
        this.recipeDTO = recipeDTO;
    }
    
    public RecipeElaborationStepDTO(long id, String stepDescription, int stepNumber)
    {
        this.id = id;
        this.stepDescription = stepDescription;
        this.stepNumber = stepNumber;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getStepDescription()
    {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription)
    {
        this.stepDescription = stepDescription;
    }

    public int getStepNumber()
    {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber)
    {
        this.stepNumber = stepNumber;
    }

    public RecipeDTO getRecipeDTO()
    {
        return recipeDTO;
    }

    public void setRecipeDTO(RecipeDTO recipeDTO)
    {
        this.recipeDTO = recipeDTO;
    } 

}
