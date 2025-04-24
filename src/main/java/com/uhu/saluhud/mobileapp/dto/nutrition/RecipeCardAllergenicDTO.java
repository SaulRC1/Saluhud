package com.uhu.saluhud.mobileapp.dto.nutrition;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class RecipeCardAllergenicDTO 
{
    private long allergenicId;
    private String allergenicName;

    public RecipeCardAllergenicDTO(long allergenId, String allergenName)
    {
        this.allergenicId = allergenId;
        this.allergenicName = allergenName;
    }

    public long getAllergenicId()
    {
        return allergenicId;
    }

    public void setAllergenicId(long allergenicId)
    {
        this.allergenicId = allergenicId;
    }

    public String getAllergenicName()
    {
        return allergenicName;
    }

    public void setAllergenicName(String allergenicName)
    {
        this.allergenicName = allergenicName;
    }
}
