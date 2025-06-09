package com.uhu.saluhud.mobileapp.dto.user;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class DailyStepsHistoricalEntryDTO 
{
    private Long id;
    private String entryDate;
    private Integer doneSteps;
    private Integer kilocaloriesBurned;
    private String evaluation;

    public DailyStepsHistoricalEntryDTO(Long id, String entryDate, Integer doneSteps, Integer kilocaloriesBurned, String evaluation)
    {
        this.id = id;
        this.entryDate = entryDate;
        this.doneSteps = doneSteps;
        this.kilocaloriesBurned = kilocaloriesBurned;
        this.evaluation = evaluation;
    }

    public DailyStepsHistoricalEntryDTO()
    {
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

    public Integer getDoneSteps()
    {
        return doneSteps;
    }

    public void setDoneSteps(Integer doneSteps)
    {
        this.doneSteps = doneSteps;
    }

    public Integer getKilocaloriesBurned()
    {
        return kilocaloriesBurned;
    }

    public void setKilocaloriesBurned(Integer kilocaloriesBurned)
    {
        this.kilocaloriesBurned = kilocaloriesBurned;
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
