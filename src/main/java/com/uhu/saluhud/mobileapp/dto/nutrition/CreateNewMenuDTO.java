package com.uhu.saluhud.mobileapp.dto.nutrition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class CreateNewMenuDTO 
{
    @NotBlank
    @Size(min = 2, max = 255)
    private String menuName;

    public CreateNewMenuDTO()
    {
    }

    public CreateNewMenuDTO(String menuName)
    {
        this.menuName = menuName;
    }

    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }
}
