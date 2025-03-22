package com.uhu.saluhud.administrationportal.configuration.web;

/**
 *
 * @author juald
 */
public class IngredientSearchDTO
{

    private String name;
    private Integer minKilocalories;
    private Integer maxKilocalories;
    private Integer minProteinAmount;
    private Integer maxProteinAmount;
    private Integer minCarbohydratesAmount;
    private Integer maxCarbohydratesAmount;
    private Integer minFatAmount;
    private Integer maxFatAmount;

    public IngredientSearchDTO(String name, Integer maxKilocalories, Integer minProteinAmount, Integer minCarbohydratesAmount, Integer minFatAmount)
    {
        this.name = name;
        this.maxKilocalories = maxKilocalories;
        this.minProteinAmount = minProteinAmount;
        this.minCarbohydratesAmount = minCarbohydratesAmount;
        this.minFatAmount = minFatAmount;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getMinKilocalories()
    {
        return minKilocalories;
    }

    public void setMinKilocalories(Integer minKilocalories)
    {
        this.minKilocalories = minKilocalories;
    }

    public Integer getMaxKilocalories()
    {
        return maxKilocalories;
    }

    public void setMaxKilocalories(Integer maxKilocalories)
    {
        this.maxKilocalories = maxKilocalories;
    }

    public Integer getMinProteinAmount()
    {
        return minProteinAmount;
    }

    public void setMinProteinAmount(Integer minProteinAmount)
    {
        this.minProteinAmount = minProteinAmount;
    }

    public Integer getMaxProteinAmount()
    {
        return maxProteinAmount;
    }

    public void setMaxProteinAmount(Integer maxProteinAmount)
    {
        this.maxProteinAmount = maxProteinAmount;
    }

    public Integer getMinCarbohydratesAmount()
    {
        return minCarbohydratesAmount;
    }

    public void setMinCarbohydratesAmount(Integer minCarbohydratesAmount)
    {
        this.minCarbohydratesAmount = minCarbohydratesAmount;
    }

    public Integer getMaxCarbohydratesAmount()
    {
        return maxCarbohydratesAmount;
    }

    public void setMaxCarbohydratesAmount(Integer maxCarbohydratesAmount)
    {
        this.maxCarbohydratesAmount = maxCarbohydratesAmount;
    }

    public Integer getMinFatAmount()
    {
        return minFatAmount;
    }

    public void setMinFatAmount(Integer minFatAmount)
    {
        this.minFatAmount = minFatAmount;
    }

    public Integer getMaxFatAmount()
    {
        return maxFatAmount;
    }

    public void setMaxFatAmount(Integer maxFatAmount)
    {
        this.maxFatAmount = maxFatAmount;
    }

}
