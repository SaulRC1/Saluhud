package com.uhu.saluhud.mobileapp.dto.nutrition;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class AddMenuDayToMenuDTO 
{
    private long menuID;
    
    private String weekDay;

    public AddMenuDayToMenuDTO()
    {
    }

    public AddMenuDayToMenuDTO(long menuID, String weekDay)
    {
        this.menuID = menuID;
        this.weekDay = weekDay;
    }

    public long getMenuID()
    {
        return menuID;
    }

    public void setMenuID(long menuID)
    {
        this.menuID = menuID;
    }

    public String getWeekDay()
    {
        return weekDay;
    }

    public void setWeekDay(String weekDay)
    {
        this.weekDay = weekDay;
    }
    
}
