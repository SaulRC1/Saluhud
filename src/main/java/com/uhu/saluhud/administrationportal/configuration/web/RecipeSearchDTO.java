package com.uhu.saluhud.administrationportal.configuration.web;

/**
 *
 * @author juald
 */
public class RecipeSearchDTO
{

    private String name;
    private Integer maxKilocalories;
    private Long ingredientId;
    private Long allergenicId;
    private Long allergenicExclusionId;

    // Constructor vacío necesario para Spring
    public RecipeSearchDTO()
    {
    }

    public RecipeSearchDTO(String name, Integer maxKilocalories, Long ingredientId,
            Long allergenicId, Long allergenicExclusionId)
    {
        this.name = name;
        this.maxKilocalories = maxKilocalories;
        this.ingredientId = ingredientId;
        this.allergenicId = allergenicId;
        this.allergenicExclusionId = allergenicExclusionId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getMaxKilocalories()
    {
        return maxKilocalories;
    }

    public void setMaxKilocalories(Integer maxKilocalories)
    {
        this.maxKilocalories = maxKilocalories;
    }

    public Long getIngredientId()
    {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId)
    {
        this.ingredientId = ingredientId;
    }

    public Long getAllergenicId()
    {
        return allergenicId;
    }

    public void setAllergenicId(Long allergenicId)
    {
        this.allergenicId = allergenicId;
    }

    public Long getAllergenicExclusionId()
    {
        return allergenicExclusionId;
    }

    public void setAllergenicExclusionId(Long allergenicExclusionId)
    {
        this.allergenicExclusionId = allergenicExclusionId;
    }

}
