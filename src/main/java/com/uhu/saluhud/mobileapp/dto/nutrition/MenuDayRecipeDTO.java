package com.uhu.saluhud.mobileapp.dto.nutrition;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class MenuDayRecipeDTO 
{
    private Long id;
    
    private String startTime;
    
    private String endTime;

    private RecipeCardDTO recipe;

    public MenuDayRecipeDTO()
    {
    }

    public MenuDayRecipeDTO(Long id, String startTime, String endTime, RecipeCardDTO recipe)
    {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recipe = recipe;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public RecipeCardDTO getRecipe()
    {
        return recipe;
    }

    public void setRecipe(RecipeCardDTO recipe)
    {
        this.recipe = recipe;
    }
    
}
