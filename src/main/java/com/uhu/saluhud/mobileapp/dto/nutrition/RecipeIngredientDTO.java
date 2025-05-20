package com.uhu.saluhud.mobileapp.dto.nutrition;

import com.uhu.saluhuddatabaseutils.models.nutrition.Ingredient;
import java.math.BigDecimal;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class RecipeIngredientDTO 
{
    private IngredientDTO ingredient;
    private BigDecimal quantity;
    private String unit;

    public RecipeIngredientDTO(IngredientDTO ingredient, BigDecimal quantity, String unit)
    {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

    public RecipeIngredientDTO()
    {
    }

    public IngredientDTO getIngredient()
    {
        return ingredient;
    }

    public void setIngredient(IngredientDTO ingredient)
    {
        this.ingredient = ingredient;
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
