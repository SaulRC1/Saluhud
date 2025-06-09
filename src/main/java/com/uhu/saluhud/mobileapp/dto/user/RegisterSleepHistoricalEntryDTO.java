package com.uhu.saluhud.mobileapp.dto.user;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class RegisterSleepHistoricalEntryDTO 
{
    private String entryDate;
    private Integer hoursSlept;
    private Integer minutesSlept;

    public RegisterSleepHistoricalEntryDTO()
    {
    }

    public RegisterSleepHistoricalEntryDTO(String entryDate, Integer hoursSlept, Integer minutesSlept)
    {
        this.entryDate = entryDate;
        this.hoursSlept = hoursSlept;
        this.minutesSlept = minutesSlept;
    }

    public String getEntryDate()
    {
        return entryDate;
    }

    public void setEntryDate(String entryDate)
    {
        this.entryDate = entryDate;
    }

    public Integer getHoursSlept()
    {
        return hoursSlept;
    }

    public void setHoursSlept(Integer hoursSlept)
    {
        this.hoursSlept = hoursSlept;
    }

    public Integer getMinutesSlept()
    {
        return minutesSlept;
    }

    public void setMinutesSlept(Integer minutesSlept)
    {
        this.minutesSlept = minutesSlept;
    }
}
