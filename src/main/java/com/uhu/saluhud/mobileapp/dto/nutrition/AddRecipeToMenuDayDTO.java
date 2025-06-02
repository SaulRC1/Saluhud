package com.uhu.saluhud.mobileapp.dto.nutrition;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class AddRecipeToMenuDayDTO 
{
    @NotNull
    @Min(1)
    private Long menuDayId;
    
    @NotNull
    @Min(1)
    private Long recipeId;
    
    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "Time must be in HH:mm format")
    private String startTime;
    
    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "Time must be in HH:mm format")
    private String endTime;

    public AddRecipeToMenuDayDTO(Long menuDayId, Long recipeId, String startTime, String endTime)
    {
        this.menuDayId = menuDayId;
        this.recipeId = recipeId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public AddRecipeToMenuDayDTO()
    {
    }

    public Long getMenuDayId()
    {
        return menuDayId;
    }

    public void setMenuDayId(Long menuDayId)
    {
        this.menuDayId = menuDayId;
    }

    public Long getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(Long recipeId)
    {
        this.recipeId = recipeId;
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
}
