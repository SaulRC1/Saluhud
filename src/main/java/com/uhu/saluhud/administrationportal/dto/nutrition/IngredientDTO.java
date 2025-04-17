package com.uhu.saluhud.administrationportal.dto.nutrition;

import java.util.List;
import java.util.Set;

/**
 *
 * @author juald
 */
public class IngredientDTO
{

    private long id;

    private String name;

    private int kilocalories;

    private int proteinAmount;

    private int carbohydratesAmount;

    private int fatAmount;

    private Set<AllergenicDTO> allergenicsDTO;
    
    private List<RecipeIngredientDTO> recipeIngredients;

    public IngredientDTO(long id, String name, int kilocalories, int proteinAmount,
            int carbohydratesAmount, int fatAmount, Set<AllergenicDTO> allergenicsDTO,
            List<RecipeIngredientDTO> recipeIngredients)
    {
        this.id = id;
        this.name = name;
        this.kilocalories = kilocalories;
        this.proteinAmount = proteinAmount;
        this.carbohydratesAmount = carbohydratesAmount;
        this.fatAmount = fatAmount;
        this.allergenicsDTO = allergenicsDTO;
        this.recipeIngredients = recipeIngredients;
    }

    public IngredientDTO(long id, String name)
    {
        this.id = id;
        this.name = name;
    }
    

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
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

    public Set<AllergenicDTO> getAllergenicsDTO()
    {
        return allergenicsDTO;
    }

    public void setAllergenicsDTO(Set<AllergenicDTO> allergenicsDTO)
    {
        this.allergenicsDTO = allergenicsDTO;
    }

    public List<RecipeIngredientDTO> getRecipeIngredients()
    {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredientDTO> recipeIngredients)
    {
        this.recipeIngredients = recipeIngredients;
    }

}
