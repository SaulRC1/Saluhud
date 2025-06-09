package com.uhu.saluhud.mobileapp.dto.user;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class SleepHistoricalDTO 
{
    private Long id;
    private Long userId;

    public SleepHistoricalDTO(Long id, Long userId)
    {
        this.id = id;
        this.userId = userId;
    }

    public SleepHistoricalDTO()
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
