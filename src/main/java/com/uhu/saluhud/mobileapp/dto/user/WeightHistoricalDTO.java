package com.uhu.saluhud.mobileapp.dto.user;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class WeightHistoricalDTO 
{
    private Long id;
    private Long userId;

    public WeightHistoricalDTO()
    {
    }

    public WeightHistoricalDTO(Long id, Long userId)
    {
        this.id = id;
        this.userId = userId;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }
}
