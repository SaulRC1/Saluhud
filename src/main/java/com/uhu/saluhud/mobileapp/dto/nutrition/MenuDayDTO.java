package com.uhu.saluhud.mobileapp.dto.nutrition;

import java.util.List;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class MenuDayDTO 
{
    private long id;
    private String weekDay;

    public MenuDayDTO(long id, String weekDay)
    {
        this.id = id;
        this.weekDay = weekDay;
    }

    public MenuDayDTO()
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
}
