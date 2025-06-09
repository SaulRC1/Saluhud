package com.uhu.saluhud.mobileapp.dto.user;

import java.util.List;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class WeightHistoricalDateRangeDTO 
{
    private String startDate;
    private String endDate;
    private List<WeightHistoricalEntryDTO> entries;

    public WeightHistoricalDateRangeDTO()
    {
    }

    public WeightHistoricalDateRangeDTO(String startDate, String endDate, List<WeightHistoricalEntryDTO> entries)
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

    public List<WeightHistoricalEntryDTO> getEntries()
    {
        return entries;
    }

    public void setEntries(List<WeightHistoricalEntryDTO> entries)
    {
        this.entries = entries;
    }
}
