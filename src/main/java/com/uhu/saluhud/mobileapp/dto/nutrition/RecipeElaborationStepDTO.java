package com.uhu.saluhud.mobileapp.dto.nutrition;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class RecipeElaborationStepDTO 
{
    private String stepDescription;
    private int stepNumber;

    public RecipeElaborationStepDTO(String stepDescription, int stepNumber)
    {
        this.stepDescription = stepDescription;
        this.stepNumber = stepNumber;
    }

    public RecipeElaborationStepDTO()
    {
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
}
