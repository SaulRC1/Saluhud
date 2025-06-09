package com.uhu.saluhud.mobileapp.dto.user;

import java.util.List;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class SleepHistoricalDateRangeDTO 
{
    private String startDate;
    private String endDate;
    private List<SleepHistoricalEntryDTO> entries;

    public SleepHistoricalDateRangeDTO(String startDate, String endDate, List<SleepHistoricalEntryDTO> entries)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.entries = entries;
    }

    public SleepHistoricalDateRangeDTO()
    {
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

    public List<SleepHistoricalEntryDTO> getEntries()
    {
        return entries;
    }

    public void setEntries(List<SleepHistoricalEntryDTO> entries)
    {
        this.entries = entries;
    }
}
