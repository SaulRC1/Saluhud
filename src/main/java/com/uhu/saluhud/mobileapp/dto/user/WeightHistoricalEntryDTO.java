package com.uhu.saluhud.mobileapp.dto.user;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class WeightHistoricalEntryDTO 
{
    private Long id;
    private String entryDate;
    private Double weight;
    private Double height;
    private Integer kilocaloriesObjective;

    public WeightHistoricalEntryDTO()
    {
    }

    public WeightHistoricalEntryDTO(Long id, String entryDate, Double weight, Double height, Integer kilocaloriesObjective)
    {
        this.id = id;
        this.entryDate = entryDate;
        this.weight = weight;
        this.height = height;
        this.kilocaloriesObjective = kilocaloriesObjective;
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

    public Double getWeight()
    {
        return weight;
    }

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }

    public Double getHeight()
    {
        return height;
    }

    public void setHeight(Double height)
    {
        this.height = height;
    }

    public Integer getKilocaloriesObjective()
    {
        return kilocaloriesObjective;
    }

    public void setKilocaloriesObjective(Integer kilocaloriesObjective)
    {
        this.kilocaloriesObjective = kilocaloriesObjective;
    }
}
