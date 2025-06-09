package com.uhu.saluhud.mobileapp.dto.user;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class RegisterWeightHistoricalEntryDTO 
{
    private String entryDate;
    private Double weight;

    public RegisterWeightHistoricalEntryDTO()
    {
    }

    public RegisterWeightHistoricalEntryDTO(String entryDate, Double weight)
    {
        this.entryDate = entryDate;
        this.weight = weight;
    }

    public String getEntryDate()
    {
        return entryDate;
    }

    public void setEntryDate(String entryDate)
    {
        this.entryDate = entryDate;
    }

    public Double getWeight()
    {
        return weight;
    }

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }
}
