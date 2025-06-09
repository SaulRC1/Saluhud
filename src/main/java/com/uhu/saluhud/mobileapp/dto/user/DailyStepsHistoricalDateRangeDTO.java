package com.uhu.saluhud.mobileapp.dto.user;

import java.util.List;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class DailyStepsHistoricalDateRangeDTO 
{
    
    private String startDate;
    private String endDate;
    private List<DailyStepsHistoricalEntryDTO> entries;

    public DailyStepsHistoricalDateRangeDTO()
    {
    }

    public DailyStepsHistoricalDateRangeDTO(String startDate, String endDate, List<DailyStepsHistoricalEntryDTO> entries)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.entries = entries;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public List<DailyStepsHistoricalEntryDTO> getEntries()
    {
        return entries;
    }

    public void setEntries(List<DailyStepsHistoricalEntryDTO> entries)
    {
        this.entries = entries;
    }
    
}
