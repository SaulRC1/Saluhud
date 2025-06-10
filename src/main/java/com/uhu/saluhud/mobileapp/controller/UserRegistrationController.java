package com.uhu.saluhud.mobileapp.controller;

import com.uhu.saluhud.mobileapp.dto.user.SaluhudUserRegistrationDTO;
import com.uhu.saluhud.mobileapp.localization.MobileAppLocaleProvider;
import com.uhu.saluhud.mobileapp.response.ApiErrorResponse;
import com.uhu.saluhud.mobileapp.response.ApiInformationResponse;
import com.uhu.saluhuddatabaseutils.models.user.DailyStepsHistorical;
import com.uhu.saluhuddatabaseutils.models.user.SaluhudUser;
import com.uhu.saluhuddatabaseutils.models.user.SleepHistorical;
import com.uhu.saluhuddatabaseutils.models.user.WeightHistorical;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppDailyStepsHistoricalService;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppSaluhudUserService;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppSleepHistoricalService;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppWeightHistoricalService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Registration end-point for Saluhud's mobile app. This controller will be used
 * to register new Saluhud users into the system.
 * 
 * @author Saúl Rodríguez Naranjo
 */
@RestController
@RequestMapping("/saluhud-mobile-app/registration")
public class UserRegistrationController 
{
    @Autowired
    private MobileAppSaluhudUserService mobileAppSaluhudUserService;
    
    @Autowired
    private MobileAppLocaleProvider mobileAppLocaleProvider;
    
    @Autowired
    private MobileAppDailyStepsHistoricalService mobileAppDailyStepsHistoricalService;
    
    @Autowired
    private MobileAppSleepHistoricalService mobileAppSleepHistoricalService;
    
    @Autowired
    private MobileAppWeightHistoricalService mobileAppWeightHistoricalService;
    
    @PostMapping
    public ResponseEntity<Object> registerSaluhudUser(@RequestBody @Valid SaluhudUserRegistrationDTO saluhudUserRegistrationDTO, HttpServletRequest request)
    {
        String mobileAppLanguage = request.getHeader("Accept-Language");
        Locale mobileAppLocale = mobileAppLanguage != null ? Locale.of(mobileAppLanguage) : Locale.ENGLISH;
        
        sanitizeSaluhudUserRegistrationDTO(saluhudUserRegistrationDTO);
        
        if(mobileAppSaluhudUserService.existsByUsername(saluhudUserRegistrationDTO.getUsername()))
        {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(request.getServletPath(), 
                    mobileAppLocaleProvider.getTranslation("registration.usernameAlreadyInUse", 
                            MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppLocale));
            return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
        }
        
        if(mobileAppSaluhudUserService.existsByEmailIgnoreCase(saluhudUserRegistrationDTO.getEmail()))
        {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(request.getServletPath(), 
                    mobileAppLocaleProvider.getTranslation("registration.emailAlreadyInUse", 
                            MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppLocale));
            return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
        }
        
        if(mobileAppSaluhudUserService.existsByPhoneNumber(saluhudUserRegistrationDTO.getPhoneNumber()))
        {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(request.getServletPath(), 
                    mobileAppLocaleProvider.getTranslation("registration.phoneAlreadyInUse", 
                            MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppLocale));
            return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
        }
        
        SaluhudUser saluhudUser = new SaluhudUser(saluhudUserRegistrationDTO.getUsername(), 
                saluhudUserRegistrationDTO.getRawPassword(), saluhudUserRegistrationDTO.getEmail(), 
                saluhudUserRegistrationDTO.getName(), saluhudUserRegistrationDTO.getSurname(), saluhudUserRegistrationDTO.getPhoneNumber());
        
        mobileAppSaluhudUserService.registerSaluhudUser(saluhudUser);
        
        Optional<SaluhudUser> savedUserOptional = this.mobileAppSaluhudUserService.findByUsername(saluhudUser.getUsername());
        
        if(savedUserOptional.isPresent())
        {
            WeightHistorical weightHistorical = new WeightHistorical();
            weightHistorical.setUser(savedUserOptional.get());
            
            this.mobileAppWeightHistoricalService.save(weightHistorical);
            
            SleepHistorical sleepHistorical = new SleepHistorical();
            sleepHistorical.setUser(savedUserOptional.get());
            
            this.mobileAppSleepHistoricalService.save(sleepHistorical);
            
            DailyStepsHistorical dailyStepsHistorical = new DailyStepsHistorical();
            dailyStepsHistorical.setUser(savedUserOptional.get());
            
            this.mobileAppDailyStepsHistoricalService.save(dailyStepsHistorical);
        }
        
        return ResponseEntity.ok(new ApiInformationResponse(request.getServletPath(), 
                mobileAppLocaleProvider.getTranslation("registration.userRegisteredSuccessfully", 
                            MobileAppLocaleProvider.MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppLocale)));
    }
    
    private void sanitizeSaluhudUserRegistrationDTO(SaluhudUserRegistrationDTO saluhudUserRegistrationDTO)
    {
        if(saluhudUserRegistrationDTO.getPhoneNumber().isBlank())
        {
            saluhudUserRegistrationDTO.setPhoneNumber(null);
        }
    }
    
}
