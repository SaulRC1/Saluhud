package com.uhu.saluhud.administrationportal.dto.nutrition;

import com.uhu.saluhuddatabaseutils.models.nutrition.Ingredient;
import java.util.List;

/**
 *
 * @author juald
 */
public class AllergenicDTO
{

    private long id;

    private String name;

    private List<IngredientDTO> ingredientsDTO;

    public AllergenicDTO(long id, String name, List<IngredientDTO> ingredientsDTO)
    {
        this.id = id;
        this.name = name;
        this.ingredientsDTO = ingredientsDTO;
    }
    
    public AllergenicDTO(long id, String name)
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

    public List<IngredientDTO> getIngredientsDTO()
    {
        return ingredientsDTO;
    }

    public void setIngredientsDTO(List<IngredientDTO> ingredientsDTO)
    {
        this.ingredientsDTO = ingredientsDTO;
    }

}
