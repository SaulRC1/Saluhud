package com.uhu.saluhud.mobileapp.dto.nutrition;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class EditMenuDTO 
{
    @NotNull
    @Min(1)
    private Long menuId;
    
    @NotBlank
    @Size(min = 2, max = 255)
    private String menuName;

    public EditMenuDTO()
    {
    }

    public EditMenuDTO(Long menuId, String menuName)
    {
        this.menuId = menuId;
        this.menuName = menuName;
    }

    public Long getMenuId()
    {
        return menuId;
    }

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
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
