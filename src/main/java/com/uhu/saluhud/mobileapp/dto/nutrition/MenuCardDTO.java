package com.uhu.saluhud.mobileapp.dto.nutrition;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class MenuCardDTO 
{
    private long menuID;
    private String menuName;
    private boolean isFavourite;

    public MenuCardDTO()
    {
    }

    public MenuCardDTO(long menuID, String menuName, boolean isFavourite)
    {
        this.menuID = menuID;
        this.menuName = menuName;
        this.isFavourite = isFavourite;
    } 

    public long getMenuID()
    {
        return menuID;
    }

    public void setMenuID(long menuID)
    {
        this.menuID = menuID;
    }

    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public boolean isIsFavourite()
    {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite)
    {
        this.isFavourite = isFavourite;
    }
}
