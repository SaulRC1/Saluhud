package com.uhu.saluhud.mobileapp.dto.nutrition;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class IngredientDTO 
{

    private String name;
    private int kilocalories;
    private int proteinAmount;
    private int carbohydratesAmount;
    private int fatAmount;

    public IngredientDTO()
    {
    }

    public IngredientDTO(String name, int kilocalories, int proteinAmount, int carbohydratesAmount, int fatAmount)
    {
        this.name = name;
        this.kilocalories = kilocalories;
        this.proteinAmount = proteinAmount;
        this.carbohydratesAmount = carbohydratesAmount;
        this.fatAmount = fatAmount;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getKilocalories()
    {
        return kilocalories;
    }

    public void setKilocalories(int kilocalories)
    {
        this.kilocalories = kilocalories;
    }

    public int getProteinAmount()
    {
        return proteinAmount;
    }

    public void setProteinAmount(int proteinAmount)
    {
        this.proteinAmount = proteinAmount;
    }

    public int getCarbohydratesAmount()
    {
        return carbohydratesAmount;
    }

    public void setCarbohydratesAmount(int carbohydratesAmount)
    {
        this.carbohydratesAmount = carbohydratesAmount;
    }

    public int getFatAmount()
    {
        return fatAmount;
    }

    public void setFatAmount(int fatAmount)
    {
        this.fatAmount = fatAmount;
    }
    
}
