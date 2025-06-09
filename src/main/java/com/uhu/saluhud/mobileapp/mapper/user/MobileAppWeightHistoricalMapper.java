package com.uhu.saluhud.mobileapp.mapper.user;

import com.uhu.saluhud.mobileapp.dto.user.WeightHistoricalDTO;
import com.uhu.saluhud.mobileapp.dto.user.WeightHistoricalEntryDTO;
import com.uhu.saluhuddatabaseutils.models.user.SleepHistorical;
import com.uhu.saluhuddatabaseutils.models.user.WeightHistorical;
import com.uhu.saluhuddatabaseutils.models.user.WeightHistoricalEntry;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@Component
public class MobileAppWeightHistoricalMapper 
{
    public WeightHistoricalDTO entityToDTO(WeightHistorical source)
    {
        WeightHistoricalDTO dto = new WeightHistoricalDTO();
        
        dto.setId(source.getId());
        dto.setUserId(source.getUser().getId());
        
        return dto;
    }
    
    public WeightHistoricalEntryDTO entryEntityToDTO(WeightHistoricalEntry source)
    {
        WeightHistoricalEntryDTO target = new WeightHistoricalEntryDTO();
        
        target.setId(source.getId());
        target.setEntryDate(source.getEntryDate().toString());
        target.setWeight(source.getWeightEntry());
        target.setHeight(source.getHeightEntry());
        target.setKilocaloriesObjective((int) source.getKilocaloriesObjectiveEntry());
        
        return target;
    }
    
    public List<WeightHistoricalEntryDTO> entryEntitiesToDTOList(List<WeightHistoricalEntry> source)
    {
        List<WeightHistoricalEntryDTO> target = new ArrayList<>();
        
        for (WeightHistoricalEntry weightHistoricalEntry : source)
        {
            WeightHistoricalEntryDTO dto = this.entryEntityToDTO(weightHistoricalEntry);
            
            target.add(dto);
        }
        
        return target;
    }
}
