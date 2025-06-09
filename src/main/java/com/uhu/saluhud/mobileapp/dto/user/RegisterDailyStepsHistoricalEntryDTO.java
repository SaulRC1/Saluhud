package com.uhu.saluhud.mobileapp.dto.user;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class RegisterDailyStepsHistoricalEntryDTO 
{
    private String entryDate;
    private Integer doneSteps;

    public RegisterDailyStepsHistoricalEntryDTO(String entryDate, Integer doneSteps)
    {
        this.entryDate = entryDate;
        this.doneSteps = doneSteps;
    }

    public RegisterDailyStepsHistoricalEntryDTO()
    {
    }

    public String getEntryDate()
    {
        return entryDate;
    }

    public void setEntryDate(String entryDate)
    {
        this.entryDate = entryDate;
    }

    public Integer getDoneSteps()
    {
        return doneSteps;
    }

    public void setDoneSteps(Integer doneSteps)
    {
        this.doneSteps = doneSteps;
    }
    
}
