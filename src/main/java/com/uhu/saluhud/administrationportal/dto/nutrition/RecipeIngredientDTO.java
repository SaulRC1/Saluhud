package com.uhu.saluhud.administrationportal.dto.nutrition;

import com.uhu.saluhuddatabaseutils.models.nutrition.RecipeIngredientId;
import java.math.BigDecimal;

/**
 *
 * @author juald
 */
public class RecipeIngredientDTO
{

    private RecipeIngredientId id;

    private RecipeDTO recipeDTO;

    private IngredientDTO ingredientDTO;

    private BigDecimal quantity;

    private String unit;

    public RecipeIngredientDTO(RecipeIngredientId id, RecipeDTO recipeDTO,
            IngredientDTO ingredientDTO, 
            BigDecimal quantity, String unit)
    {
        this.id = id;
        this.recipeDTO = recipeDTO;
        this.ingredientDTO = ingredientDTO;
        this.quantity = quantity;
        this.unit = unit;
    }

    public RecipeIngredientId getId()
    {
        return id;
    }

    public void setId(RecipeIngredientId id)
    {
        this.id = id;
    }

    public RecipeDTO getRecipeDTO()
    {
        return recipeDTO;
    }

    public void setRecipeDTO(RecipeDTO recipeDTO)
    {
        this.recipeDTO = recipeDTO;
    }

    public IngredientDTO getIngredientDTO()
    {
        return ingredientDTO;
    }

    public void setIngredientDTO(IngredientDTO ingredientDTO)
    {
        this.ingredientDTO = ingredientDTO;
    }

    public BigDecimal getQuantity()
    {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity)
    {
        this.quantity = quantity;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }
    
    
}
