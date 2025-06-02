package com.uhu.saluhud.mobileapp.dto.nutrition;

import java.util.List;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class MenuDayDetailDTO 
{
    private long id;
    private String weekDay;
    private List<MenuDayRecipeDTO> menuDayRecipes;

    public MenuDayDetailDTO(long id, String weekDay, List<MenuDayRecipeDTO> menuDayRecipes)
    {
        this.id = id;
        this.weekDay = weekDay;
        this.menuDayRecipes = menuDayRecipes;
    }

    public MenuDayDetailDTO()
    {
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getWeekDay()
    {
        return weekDay;
    }

    public void setWeekDay(String weekDay)
    {
        this.weekDay = weekDay;
    }

    public List<MenuDayRecipeDTO> getMenuDayRecipes()
    {
        return menuDayRecipes;
    }

    public void setMenuDayRecipes(List<MenuDayRecipeDTO> menuDayRecipes)
    {
        this.menuDayRecipes = menuDayRecipes;
    }
    
}
