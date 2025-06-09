package com.uhu.saluhud.mobileapp.mapper.user;

import com.uhu.saluhud.mobileapp.dto.user.SleepHistoricalDTO;
import com.uhu.saluhud.mobileapp.dto.user.SleepHistoricalEntryDTO;
import com.uhu.saluhuddatabaseutils.models.user.SleepHistorical;
import com.uhu.saluhuddatabaseutils.models.user.SleepHistoricalEntry;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@Component
public class MobileAppSleepHistoricalMapper 
{
    public SleepHistoricalDTO entityToDTO(SleepHistorical source)
    {
        SleepHistoricalDTO dto = new SleepHistoricalDTO();
        
        dto.setId(source.getId());
        dto.setUserId(source.getUser().getId());
        
        return dto;
    }
    
    public SleepHistoricalEntryDTO entryEntityToDTO(SleepHistoricalEntry source)
    {
        SleepHistoricalEntryDTO target = new SleepHistoricalEntryDTO();
        
        target.setId(source.getId());
        target.setHoursSlept(source.getHoursSlept());
        target.setMinutesSlept((int) source.getMinutesSlept());
        target.setEntryDate(source.getEntryDate().toString());
        target.setEvaluation(source.getSleepEvaluation().getEvaluationName());
        
        return target;
    }
    
    public List<SleepHistoricalEntryDTO> entryEntitiesToDTOList(List<SleepHistoricalEntry> source)
    {
        List<SleepHistoricalEntryDTO> target = new ArrayList<>();
        
        for (SleepHistoricalEntry dailyStepsHistoricalEntry : source)
        {
            SleepHistoricalEntryDTO dto = this.entryEntityToDTO(dailyStepsHistoricalEntry);
            
            target.add(dto);
        }
        
        return target;
    }
}
