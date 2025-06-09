package com.uhu.saluhud.mobileapp.controller.user;

import com.uhu.saluhud.mobileapp.dto.user.RegisterSleepHistoricalEntryDTO;
import com.uhu.saluhud.mobileapp.dto.user.SleepHistoricalDTO;
import com.uhu.saluhud.mobileapp.dto.user.SleepHistoricalDateRangeDTO;
import com.uhu.saluhud.mobileapp.dto.user.SleepHistoricalEntryDTO;
import com.uhu.saluhud.mobileapp.localization.MobileAppLocaleProvider;
import com.uhu.saluhud.mobileapp.mapper.user.MobileAppSleepHistoricalMapper;
import com.uhu.saluhud.mobileapp.response.ApiErrorResponse;
import com.uhu.saluhud.mobileapp.response.ApiInformationResponse;
import com.uhu.saluhud.mobileapp.service.JWTService;
import com.uhu.saluhud.mobileapp.service.MobileAppHttpRequestService;
import com.uhu.saluhuddatabaseutils.models.user.SleepHistorical;
import com.uhu.saluhuddatabaseutils.models.user.SleepHistoricalEntry;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppSaluhudUserService;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppSleepHistoricalService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
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
@RequestMapping("/saluhud-mobile-app/sleep-historical")
public class SleepHistoricalController
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
    private MobileAppSleepHistoricalService mobileAppSleepHistoricalService;
    
    @Autowired
    private MobileAppSleepHistoricalMapper mobileAppSleepHistoricalMapper;
    
    private static final Logger LOGGER = Logger.getLogger(SleepHistoricalController.class.getName());
    
    @GetMapping("/user")
    public ResponseEntity<Object> getUserSleepHistorical(HttpServletRequest request)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        Optional<SleepHistorical> sleepHistoricalOptional = 
                this.mobileAppSleepHistoricalService.findSleepHistoricalByUsername(username);
        
        if(sleepHistoricalOptional.isEmpty())
        {
            return buildApiErrorResponse("sleepHistoricalRetrieval.notFoundHistoricalForUser", request, HttpStatus.NOT_FOUND);
        }
        
        SleepHistoricalDTO sleepHistoricalDTO = this.mobileAppSleepHistoricalMapper.entityToDTO(sleepHistoricalOptional.get());
        
        return new ResponseEntity<>(sleepHistoricalDTO, HttpStatus.OK);
    }
    
    @GetMapping("/entries")
    public ResponseEntity<Object> getSleepHistoricalEntries(HttpServletRequest request,
            @RequestParam String startDate, @RequestParam String endDate)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        Optional<SleepHistorical> sleepHistoricalOptional = 
                this.mobileAppSleepHistoricalService.findSleepHistoricalByUsername(username);
        
        if(sleepHistoricalOptional.isEmpty())
        {
            return buildApiErrorResponse("sleepHistoricalRetrieval.notFoundHistoricalForUser", request, HttpStatus.NOT_FOUND);
        }
        
        SleepHistorical sleepHistorical = sleepHistoricalOptional.get();
        
        List<SleepHistoricalEntry> entries = 
                this.mobileAppSleepHistoricalService.findEntriesByDateRange(sleepHistorical.getId(), 
                        LocalDate.parse(startDate), LocalDate.parse(endDate));
        
        if(entries == null || entries.isEmpty())
        {
            return buildApiErrorResponse("sleepHistoricalRetrieval.noEntriesFoundForDateRange", request, HttpStatus.NOT_FOUND);
        }
        
        List<SleepHistoricalEntryDTO> entriesDTO = this.mobileAppSleepHistoricalMapper.entryEntitiesToDTOList(entries);
        
        return new ResponseEntity<>(entriesDTO, HttpStatus.OK);
    }
    
    @GetMapping("/current-week-entries")
    public ResponseEntity<Object> getCurrentWeekSleepHistoricalEntries(HttpServletRequest request)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        Optional<SleepHistorical> sleepHistoricalOptional = 
                this.mobileAppSleepHistoricalService.findSleepHistoricalByUsername(username);
        
        if(sleepHistoricalOptional.isEmpty())
        {
            return buildApiErrorResponse("sleepHistoricalRetrieval.notFoundHistoricalForUser", request, HttpStatus.NOT_FOUND);
        }
        
        SleepHistorical sleepHistorical = sleepHistoricalOptional.get();
        
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
        
        List<SleepHistoricalEntry> entries = 
                this.mobileAppSleepHistoricalService.findEntriesByDateRange(sleepHistorical.getId(), 
                        startDate, endDate);
        
        if(entries == null || entries.isEmpty())
        {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            SleepHistoricalDateRangeDTO entriesByDateRangeDTO = new SleepHistoricalDateRangeDTO();
            entriesByDateRangeDTO.setStartDate(startDate != null ? startDate.format(dateFormat) : "");
            entriesByDateRangeDTO.setEndDate(endDate != null ? endDate.format(dateFormat) : "");
            entriesByDateRangeDTO.setEntries(List.of());

            return new ResponseEntity<>(entriesByDateRangeDTO, HttpStatus.OK);
        }
        
        List<SleepHistoricalEntryDTO> entriesDTO = this.mobileAppSleepHistoricalMapper.entryEntitiesToDTOList(entries);
        
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        SleepHistoricalDateRangeDTO entriesByDateRangeDTO = new SleepHistoricalDateRangeDTO();
        entriesByDateRangeDTO.setStartDate(startDate != null ? startDate.format(dateFormat) : "");
        entriesByDateRangeDTO.setEndDate(endDate != null ? endDate.format(dateFormat) : "");
        entriesByDateRangeDTO.setEntries(entriesDTO);
        
        return new ResponseEntity<>(entriesByDateRangeDTO, HttpStatus.OK);
    }
    
    @GetMapping("/week-entries")
    public ResponseEntity<Object> getWeekSleepHistoricalEntries(HttpServletRequest request, @RequestParam Integer moveWeek, 
            @RequestParam String date)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        Optional<SleepHistorical> sleepHistoricalOptional = 
                this.mobileAppSleepHistoricalService.findSleepHistoricalByUsername(username);
        
        if(sleepHistoricalOptional.isEmpty())
        {
            return buildApiErrorResponse("sleepHistoricalRetrieval.notFoundHistoricalForUser", request, HttpStatus.NOT_FOUND);
        }
        
        SleepHistorical sleepHistorical = sleepHistoricalOptional.get();
        
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
        
        List<SleepHistoricalEntry> entries = 
                this.mobileAppSleepHistoricalService.findEntriesByDateRange(sleepHistorical.getId(), 
                        startDate, endDate);
        
        if(entries == null || entries.isEmpty())
        {
            return buildApiErrorResponse("sleepHistoricalRetrieval.noEntriesFoundForDateRange", request, 
                    HttpStatus.NOT_FOUND);
        }
        
        List<SleepHistoricalEntryDTO> entriesDTO = this.mobileAppSleepHistoricalMapper.entryEntitiesToDTOList(entries);
        
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        SleepHistoricalDateRangeDTO entriesByDateRangeDTO = new SleepHistoricalDateRangeDTO();
        entriesByDateRangeDTO.setStartDate(startDate != null ? startDate.format(dateFormat) : "");
        entriesByDateRangeDTO.setEndDate(endDate != null ? endDate.format(dateFormat) : "");
        entriesByDateRangeDTO.setEntries(entriesDTO);
        
        return new ResponseEntity<>(entriesByDateRangeDTO, HttpStatus.OK);
    }
    
    @PostMapping("/register-entry")
    public ResponseEntity<Object> registerSleepHistoricalEntry(HttpServletRequest request,
            @RequestBody RegisterSleepHistoricalEntryDTO registerEntryDTO)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        try
        {
            Optional<SleepHistorical> sleepHistoricalOptional
                    = this.mobileAppSleepHistoricalService.findSleepHistoricalByUsername(username);

            if (sleepHistoricalOptional.isEmpty())
            {
                return buildApiErrorResponse("sleepHistoricalRetrieval.notFoundHistoricalForUser", request, HttpStatus.NOT_FOUND);
            }

            SleepHistorical sleepHistorical = sleepHistoricalOptional.get();

            if (this.mobileAppSleepHistoricalService.existsEntryByDate(sleepHistorical.getId(), LocalDate.parse(registerEntryDTO.getEntryDate())))
            {
                return buildApiErrorResponse("dailyStepsHistoricalCRUD.alreadyExistentEntryInDate", request, HttpStatus.BAD_REQUEST);
            }

            this.mobileAppSleepHistoricalService.registerNewEntry(sleepHistorical.getId(), 
                    LocalDate.parse(registerEntryDTO.getEntryDate()), registerEntryDTO.getHoursSlept(), 
                    registerEntryDTO.getMinutesSlept());

            return buildApiInformationResponse("dailyStepsHistoricalCRUD.registeredEntrySuccesfully", request, HttpStatus.CREATED);
        } 
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            
            return buildApiErrorResponse("general.unknownError", request, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/week-entries-by-date")
    public ResponseEntity<Object> getWeekSleepHistoricalEntriesByDate(HttpServletRequest request, @RequestParam String date)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        Optional<SleepHistorical> sleepHistoricalOptional = 
                this.mobileAppSleepHistoricalService.findSleepHistoricalByUsername(username);
        
        if(sleepHistoricalOptional.isEmpty())
        {
            return buildApiErrorResponse("sleepHistoricalRetrieval.notFoundHistoricalForUser", request, HttpStatus.NOT_FOUND);
        }
        
        SleepHistorical sleepHistorical = sleepHistoricalOptional.get();
        
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
        
        List<SleepHistoricalEntry> entries = 
                this.mobileAppSleepHistoricalService.findEntriesByDateRange(sleepHistorical.getId(), 
                        startDate, endDate);
        
        if(entries == null || entries.isEmpty())
        {
            return buildApiErrorResponse("sleepHistoricalRetrieval.noEntriesFoundForDateRange", request, 
                    HttpStatus.NOT_FOUND);
        }
        
        List<SleepHistoricalEntryDTO> entriesDTO = this.mobileAppSleepHistoricalMapper.entryEntitiesToDTOList(entries);
        
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        SleepHistoricalDateRangeDTO entriesByDateRangeDTO = new SleepHistoricalDateRangeDTO();
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
