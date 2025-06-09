package com.uhu.saluhud.mobileapp.dto.user;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class SleepHistoricalEntryDTO 
{
    private Long id;
    private String entryDate;
    private Integer hoursSlept;
    private Integer minutesSlept;
    private String evaluation;

    public SleepHistoricalEntryDTO()
    {
    }

    public SleepHistoricalEntryDTO(Long id, String entryDate, Integer hoursSlept, Integer minutesSlept, String evaluation)
    {
        this.id = id;
        this.entryDate = entryDate;
        this.hoursSlept = hoursSlept;
        this.minutesSlept = minutesSlept;
        this.evaluation = evaluation;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    public String getEvaluation()
    {
        return evaluation;
    }

    public void setEvaluation(String evaluation)
    {
        this.evaluation = evaluation;
    }
}
