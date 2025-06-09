package com.uhu.saluhud.mobileapp.dto.user;

import java.util.List;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class DailyStepsHistoricalDTO 
{
    private Long id;
    private Long userId;

    public DailyStepsHistoricalDTO(Long id, Long userId)
    {
        this.id = id;
        this.userId = userId;
    }

    public DailyStepsHistoricalDTO()
    {
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
