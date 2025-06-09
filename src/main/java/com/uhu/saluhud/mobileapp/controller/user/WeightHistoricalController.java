package com.uhu.saluhud.mobileapp.controller.user;

import com.uhu.saluhud.mobileapp.dto.user.RegisterWeightHistoricalEntryDTO;
import com.uhu.saluhud.mobileapp.dto.user.WeightHistoricalDTO;
import com.uhu.saluhud.mobileapp.dto.user.WeightHistoricalDateRangeDTO;
import com.uhu.saluhud.mobileapp.dto.user.WeightHistoricalEntryDTO;
import com.uhu.saluhud.mobileapp.localization.MobileAppLocaleProvider;
import com.uhu.saluhud.mobileapp.mapper.user.MobileAppWeightHistoricalMapper;
import com.uhu.saluhud.mobileapp.response.ApiErrorResponse;
import com.uhu.saluhud.mobileapp.response.ApiInformationResponse;
import com.uhu.saluhud.mobileapp.service.JWTService;
import com.uhu.saluhud.mobileapp.service.MobileAppHttpRequestService;
import com.uhu.saluhuddatabaseutils.models.user.SaluhudUserFitnessData;
import com.uhu.saluhuddatabaseutils.models.user.WeightHistorical;
import com.uhu.saluhuddatabaseutils.models.user.WeightHistoricalEntry;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppSaluhudUserService;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppWeightHistoricalService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@RestController
@RequestMapping("/saluhud-mobile-app/weight-historical")
public class WeightHistoricalController
{
    @Autowired
    private MobileAppHttpRequestService mobileAppHttpRequestService;
    
    @Autowired
    private MobileAppSaluhudUserService mobileAppSaluhudUserService;
    
    @Autowired
    private MobileAppLocaleProvider mobileAppLocaleProvider;
    
    @Autowired
    private JWTService jwtService;
    
    @Autowired
    private MobileAppWeightHistoricalService mobileAppWeightHistoricalService;
    
    @Autowired
    private MobileAppWeightHistoricalMapper mobileAppWeightHistoricalMapper;
    
    private static final Logger LOGGER = Logger.getLogger(WeightHistoricalController.class.getName());
    
    @GetMapping("/user")
    public ResponseEntity<Object> getUserWeightHistorical(HttpServletRequest request)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        Optional<WeightHistorical> weightHistoricalOptional = 
                this.mobileAppWeightHistoricalService.findWeightHistoricalByUsername(username);
        
        if(weightHistoricalOptional.isEmpty())
        {
            return buildApiErrorResponse("weightHistoricalRetrieval.notFoundHistoricalForUser", request, HttpStatus.NOT_FOUND);
        }
        
        WeightHistoricalDTO weightHistoricalDTO = this.mobileAppWeightHistoricalMapper.entityToDTO(weightHistoricalOptional.get());
        
        return new ResponseEntity<>(weightHistoricalDTO, HttpStatus.OK);
    }
    
    @GetMapping("/entries")
    public ResponseEntity<Object> getWeightHistoricalEntries(HttpServletRequest request,
            @RequestParam String startDate, @RequestParam String endDate)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        Optional<WeightHistorical> weightHistoricalOptional = 
                this.mobileAppWeightHistoricalService.findWeightHistoricalByUsername(username);
        
        if(weightHistoricalOptional.isEmpty())
        {
            return buildApiErrorResponse("weightHistoricalRetrieval.notFoundHistoricalForUser", request, HttpStatus.NOT_FOUND);
        }
        
        WeightHistorical weightHistorical = weightHistoricalOptional.get();
        
        List<WeightHistoricalEntry> entries = 
                this.mobileAppWeightHistoricalService.findEntriesByDateRange(weightHistorical.getId(), 
                        LocalDate.parse(startDate), LocalDate.parse(endDate));
        
        if(entries == null || entries.isEmpty())
        {
            return buildApiErrorResponse("sleepHistoricalRetrieval.noEntriesFoundForDateRange", request, HttpStatus.NOT_FOUND);
        }
        
        List<WeightHistoricalEntryDTO> entriesDTO = this.mobileAppWeightHistoricalMapper.entryEntitiesToDTOList(entries);
        
        return new ResponseEntity<>(entriesDTO, HttpStatus.OK);
    }
    
    @GetMapping("/current-week-entries")
    public ResponseEntity<Object> getCurrentWeekWeightHistoricalEntries(HttpServletRequest request)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        Optional<WeightHistorical> weightHistoricalOptional = 
                this.mobileAppWeightHistoricalService.findWeightHistoricalByUsername(username);
        
        if(weightHistoricalOptional.isEmpty())
        {
            return buildApiErrorResponse("weightHistoricalRetrieval.notFoundHistoricalForUser", request, HttpStatus.NOT_FOUND);
        }
        
        WeightHistorical weightHistorical = weightHistoricalOptional.get();
        
        LocalDate currentDate = LocalDate.now();
        
        DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
        
        LocalDate startDate = null;
        LocalDate endDate = null;
        
        switch(currentDayOfWeek)
        {
            case MONDAY:
                startDate = currentDate;
                endDate = currentDate.plusDays(6);
                break;
            case TUESDAY:
                startDate = currentDate.minusDays(1);
                endDate = currentDate.plusDays(5);
                break;
            case WEDNESDAY:
                startDate = currentDate.minusDays(2);
                endDate = currentDate.plusDays(4);
                break;
            case THURSDAY:
                startDate = currentDate.minusDays(3);
                endDate = currentDate.plusDays(3);
                break;
            case FRIDAY:
                startDate = currentDate.minusDays(4);
                endDate = currentDate.plusDays(2);
                break;
            case SATURDAY:
                startDate = currentDate.minusDays(5);
                endDate = currentDate.plusDays(1);
                break;
            case SUNDAY:
                startDate = currentDate.minusDays(6);
                endDate = currentDate;
                break;
        }
        
        List<WeightHistoricalEntry> entries = 
                this.mobileAppWeightHistoricalService.findEntriesByDateRange(weightHistorical.getId(), 
                        startDate, endDate);
        
        if(entries == null || entries.isEmpty())
        {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            WeightHistoricalDateRangeDTO entriesByDateRangeDTO = new WeightHistoricalDateRangeDTO();
            entriesByDateRangeDTO.setStartDate(startDate != null ? startDate.format(dateFormat) : "");
            entriesByDateRangeDTO.setEndDate(endDate != null ? endDate.format(dateFormat) : "");
            entriesByDateRangeDTO.setEntries(List.of());

            return new ResponseEntity<>(entriesByDateRangeDTO, HttpStatus.OK);
        }
        
        List<WeightHistoricalEntryDTO> entriesDTO = this.mobileAppWeightHistoricalMapper.entryEntitiesToDTOList(entries);
        
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        WeightHistoricalDateRangeDTO entriesByDateRangeDTO = new WeightHistoricalDateRangeDTO();
        entriesByDateRangeDTO.setStartDate(startDate != null ? startDate.format(dateFormat) : "");
        entriesByDateRangeDTO.setEndDate(endDate != null ? endDate.format(dateFormat) : "");
        entriesByDateRangeDTO.setEntries(entriesDTO);
        
        return new ResponseEntity<>(entriesByDateRangeDTO, HttpStatus.OK);
    }
    
    @GetMapping("/week-entries")
    public ResponseEntity<Object> getWeekWeightHistoricalEntries(HttpServletRequest request, @RequestParam Integer moveWeek, 
            @RequestParam String date)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        Optional<WeightHistorical> weightHistoricalOptional = 
                this.mobileAppWeightHistoricalService.findWeightHistoricalByUsername(username);
        
        if(weightHistoricalOptional.isEmpty())
        {
            return buildApiErrorResponse("weightHistoricalRetrieval.notFoundHistoricalForUser", request, HttpStatus.NOT_FOUND);
        }
        
        WeightHistorical weightHistorical = weightHistoricalOptional.get();
        
        LocalDate parsedDate = LocalDate.parse(date);
        LocalDate targetDate = parsedDate;
        
        if(moveWeek == 0)
        {
            //Move a week backwards
            targetDate = parsedDate.minusWeeks(1);
        }
        else
        {
            targetDate = parsedDate.plusWeeks(1);
        }
        
        DayOfWeek dayOfWeek = targetDate.getDayOfWeek();
        
        LocalDate startDate = null;
        LocalDate endDate = null;
        
        switch(dayOfWeek)
        {
            case MONDAY:
                startDate = targetDate;
                endDate = targetDate.plusDays(6);
                break;
            case TUESDAY:
                startDate = targetDate.minusDays(1);
                endDate = targetDate.plusDays(5);
                break;
            case WEDNESDAY:
                startDate = targetDate.minusDays(2);
                endDate = targetDate.plusDays(4);
                break;
            case THURSDAY:
                startDate = targetDate.minusDays(3);
                endDate = targetDate.plusDays(3);
                break;
            case FRIDAY:
                startDate = targetDate.minusDays(4);
                endDate = targetDate.plusDays(2);
                break;
            case SATURDAY:
                startDate = targetDate.minusDays(5);
                endDate = targetDate.plusDays(1);
                break;
            case SUNDAY:
                startDate = targetDate.minusDays(6);
                endDate = targetDate;
                break;
        }
        
        List<WeightHistoricalEntry> entries = 
                this.mobileAppWeightHistoricalService.findEntriesByDateRange(weightHistorical.getId(), 
                        startDate, endDate);
        
        if(entries == null || entries.isEmpty())
        {
            return buildApiErrorResponse("sleepHistoricalRetrieval.noEntriesFoundForDateRange", request, 
                    HttpStatus.NOT_FOUND);
        }
        
        List<WeightHistoricalEntryDTO> entriesDTO = this.mobileAppWeightHistoricalMapper.entryEntitiesToDTOList(entries);
        
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        WeightHistoricalDateRangeDTO entriesByDateRangeDTO = new WeightHistoricalDateRangeDTO();
        entriesByDateRangeDTO.setStartDate(startDate != null ? startDate.format(dateFormat) : "");
        entriesByDateRangeDTO.setEndDate(endDate != null ? endDate.format(dateFormat) : "");
        entriesByDateRangeDTO.setEntries(entriesDTO);
        
        return new ResponseEntity<>(entriesByDateRangeDTO, HttpStatus.OK);
    }
    
    @PostMapping("/register-entry")
    public ResponseEntity<Object> registerWeightHistoricalEntry(HttpServletRequest request,
            @RequestBody RegisterWeightHistoricalEntryDTO registerEntryDTO)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        try
        {
            Optional<WeightHistorical> weightHistoricalOptional
                    = this.mobileAppWeightHistoricalService.findWeightHistoricalByUsername(username);

            if (weightHistoricalOptional.isEmpty())
            {
                return buildApiErrorResponse("weightHistoricalRetrieval.notFoundHistoricalForUser", request, HttpStatus.NOT_FOUND);
            }

            WeightHistorical weightHistorical = weightHistoricalOptional.get();

            if (this.mobileAppWeightHistoricalService.existsEntryByDate(weightHistorical.getId(), LocalDate.parse(registerEntryDTO.getEntryDate())))
            {
                return buildApiErrorResponse("dailyStepsHistoricalCRUD.alreadyExistentEntryInDate", request, HttpStatus.BAD_REQUEST);
            }
            
            SaluhudUserFitnessData userFitnessData = weightHistorical.getUser().getFitnessData();
            
            if(userFitnessData == null)
            {
                return buildApiErrorResponse("weightHistoricalCRUD.fitnessProfileNotFound", request, HttpStatus.NOT_FOUND);
            }

            this.mobileAppWeightHistoricalService.registerNewEntry(weightHistorical.getId(), 
                    LocalDate.parse(registerEntryDTO.getEntryDate()), registerEntryDTO.getWeight(), 
                    userFitnessData.getHeight(), userFitnessData.getFitnessTargetRecommendedKilocalories());

            return buildApiInformationResponse("dailyStepsHistoricalCRUD.registeredEntrySuccesfully", request, HttpStatus.CREATED);
        } 
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            
            return buildApiErrorResponse("general.unknownError", request, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/week-entries-by-date")
    public ResponseEntity<Object> getWeekWeightHistoricalEntriesByDate(HttpServletRequest request, @RequestParam String date)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        Optional<WeightHistorical> weightHistoricalOptional = 
                this.mobileAppWeightHistoricalService.findWeightHistoricalByUsername(username);
        
        if(weightHistoricalOptional.isEmpty())
        {
            return buildApiErrorResponse("weightHistoricalRetrieval.notFoundHistoricalForUser", request, HttpStatus.NOT_FOUND);
        }
        
        WeightHistorical weightHistorical = weightHistoricalOptional.get();
        
        LocalDate parsedDate = LocalDate.parse(date);
        LocalDate targetDate = parsedDate;
        
        DayOfWeek dayOfWeek = targetDate.getDayOfWeek();
        
        LocalDate startDate = null;
        LocalDate endDate = null;
        
        switch(dayOfWeek)
        {
            case MONDAY:
                startDate = targetDate;
                endDate = targetDate.plusDays(6);
                break;
            case TUESDAY:
                startDate = targetDate.minusDays(1);
                endDate = targetDate.plusDays(5);
                break;
            case WEDNESDAY:
                startDate = targetDate.minusDays(2);
                endDate = targetDate.plusDays(4);
                break;
            case THURSDAY:
                startDate = targetDate.minusDays(3);
                endDate = targetDate.plusDays(3);
                break;
            case FRIDAY:
                startDate = targetDate.minusDays(4);
                endDate = targetDate.plusDays(2);
                break;
            case SATURDAY:
                startDate = targetDate.minusDays(5);
                endDate = targetDate.plusDays(1);
                break;
            case SUNDAY:
                startDate = targetDate.minusDays(6);
                endDate = targetDate;
                break;
        }
        
        List<WeightHistoricalEntry> entries = 
                this.mobileAppWeightHistoricalService.findEntriesByDateRange(weightHistorical.getId(), 
                        startDate, endDate);
        
        if(entries == null || entries.isEmpty())
        {
            return buildApiErrorResponse("sleepHistoricalRetrieval.noEntriesFoundForDateRange", request, 
                    HttpStatus.NOT_FOUND);
        }
        
        List<WeightHistoricalEntryDTO> entriesDTO = this.mobileAppWeightHistoricalMapper.entryEntitiesToDTOList(entries);
        
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        WeightHistoricalDateRangeDTO entriesByDateRangeDTO = new WeightHistoricalDateRangeDTO();
        entriesByDateRangeDTO.setStartDate(startDate != null ? startDate.format(dateFormat) : "");
        entriesByDateRangeDTO.setEndDate(endDate != null ? endDate.format(dateFormat) : "");
        entriesByDateRangeDTO.setEntries(entriesDTO);
        
        return new ResponseEntity<>(entriesByDateRangeDTO, HttpStatus.OK);
    }
    
    private ResponseEntity<Object> buildApiErrorResponse(String errorMessageTranslationKey, HttpServletRequest request, HttpStatus status)
    {
        String errorResponseMessage = mobileAppLocaleProvider.getTranslation(errorMessageTranslationKey, 
                        MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
        ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
            
        return new ResponseEntity<>(errorResponse, status);
    }
    
    private ResponseEntity<Object> buildApiInformationResponse(String messageTranslationKey, HttpServletRequest request, HttpStatus status)
    {
        String informationResponseMessage = mobileAppLocaleProvider.getTranslation(messageTranslationKey, 
                        MobileAppLocaleProvider.MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
        ApiInformationResponse informationResponse = new ApiInformationResponse(request.getServletPath(), informationResponseMessage);
            
        return new ResponseEntity<>(informationResponse, status);
    }
}
