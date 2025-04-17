package com.uhu.saluhud.administrationportal.dto.nutrition;

import java.util.List;
import java.util.Set;

/**
 *
 * @author juald
 */
public class RecipeDTO
{

    private long id;

    private String name;

    private String description;

    private String ingredientsDescription;

    private int kilocalories;

    private Set<AllergenicDTO> allergenicsDTO;

    private List<RecipeIngredientDTO> recipeIngredientsDTO;

    private List<RecipeElaborationStepDTO> elaborationStepsDTO;

    public RecipeDTO(long id, String name, String description,
            String ingredientsDescription, int kilocalories, Set<AllergenicDTO> allergenicsDTO,
            List<RecipeIngredientDTO> recipeIngredientsDTO, List<RecipeElaborationStepDTO> elaborationStepsDTO)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredientsDescription = ingredientsDescription;
        this.kilocalories = kilocalories;
        this.allergenicsDTO = allergenicsDTO;
        this.recipeIngredientsDTO = recipeIngredientsDTO;
        this.elaborationStepsDTO = elaborationStepsDTO;
    }

    public RecipeDTO(long id, String name)
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getIngredientsDescription()
    {
        return ingredientsDescription;
    }

    public void setIngredientsDescription(String ingredientsDescription)
    {
        this.ingredientsDescription = ingredientsDescription;
    }

    public int getKilocalories()
    {
        return kilocalories;
    }

    public void setKilocalories(int kilocalories)
    {
        this.kilocalories = kilocalories;
    }

    public Set<AllergenicDTO> getAllergenicsDTO()
    {
        return allergenicsDTO;
    }

    public void setAllergenicsDTO(Set<AllergenicDTO> allergenicsDTO)
    {
        this.allergenicsDTO = allergenicsDTO;
    }

    public List<RecipeIngredientDTO> getRecipeIngredientsDTO()
    {
        return recipeIngredientsDTO;
    }

    public void setRecipeIngredientsDTO(List<RecipeIngredientDTO> recipeIngredientsDTO)
    {
        this.recipeIngredientsDTO = recipeIngredientsDTO;
    }

    public List<RecipeElaborationStepDTO> getElaborationStepsDTO()
    {
        return elaborationStepsDTO;
    }

    public void setElaborationStepsDTO(List<RecipeElaborationStepDTO> elaborationStepsDTO)
    {
        this.elaborationStepsDTO = elaborationStepsDTO;
    }

}
