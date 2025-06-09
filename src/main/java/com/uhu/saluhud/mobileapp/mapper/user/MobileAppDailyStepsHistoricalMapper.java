package com.uhu.saluhud.mobileapp.mapper.user;

import com.uhu.saluhud.mobileapp.dto.user.DailyStepsHistoricalDTO;
import com.uhu.saluhud.mobileapp.dto.user.DailyStepsHistoricalEntryDTO;
import com.uhu.saluhuddatabaseutils.models.user.DailyStepsHistorical;
import com.uhu.saluhuddatabaseutils.models.user.DailyStepsHistoricalEntry;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@Component
public class MobileAppDailyStepsHistoricalMapper 
{
    public DailyStepsHistoricalDTO entityToDTO(DailyStepsHistorical source)
    {
        DailyStepsHistoricalDTO dto = new DailyStepsHistoricalDTO();
        
        dto.setId(source.getId());
        dto.setUserId(source.getUser().getId());
        
        return dto;
    }
    
    public DailyStepsHistoricalEntryDTO entryEntityToDTO(DailyStepsHistoricalEntry source)
    {
        DailyStepsHistoricalEntryDTO target = new DailyStepsHistoricalEntryDTO();
        
        target.setId(source.getId());
        target.setDoneSteps(source.getDoneSteps());
        target.setKilocaloriesBurned((int) Math.round(source.getKiloCaloriesBurned()));
        target.setEntryDate(source.getEntryDate().toString());
        target.setEvaluation(source.getStepEvaluation().getEvaluationName());
        
        return target;
    }
    
    public List<DailyStepsHistoricalEntryDTO> entryEntitiesToDTOList(List<DailyStepsHistoricalEntry> source)
    {
        List<DailyStepsHistoricalEntryDTO> target = new ArrayList<>();
        
        for (DailyStepsHistoricalEntry dailyStepsHistoricalEntry : source)
        {
            DailyStepsHistoricalEntryDTO dto = this.entryEntityToDTO(dailyStepsHistoricalEntry);
            
            target.add(dto);
        }
        
        return target;
    }
}
