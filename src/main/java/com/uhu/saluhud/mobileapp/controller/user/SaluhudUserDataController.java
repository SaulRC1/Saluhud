package com.uhu.saluhud.mobileapp.controller.user;

import com.uhu.saluhud.mobileapp.dto.user.SaluhudUserDTO;
import com.uhu.saluhud.mobileapp.dto.user.SaluhudUserFitnessDataDTO;
import com.uhu.saluhud.mobileapp.dto.user.SaveSaluhudUserFitnessDataDTO;
import com.uhu.saluhud.mobileapp.localization.MobileAppLocaleProvider;
import com.uhu.saluhud.mobileapp.response.ApiErrorResponse;
import com.uhu.saluhud.mobileapp.response.ApiInformationResponse;
import com.uhu.saluhud.mobileapp.service.JWTService;
import com.uhu.saluhud.mobileapp.service.MobileAppHttpRequestService;
import com.uhu.saluhuddatabaseutils.localization.NutritionLocaleProvider;
import com.uhu.saluhuddatabaseutils.models.user.BiologicalSex;
import com.uhu.saluhuddatabaseutils.models.user.BodyComposition;
import com.uhu.saluhuddatabaseutils.models.user.FitnessTargetEnum;
import com.uhu.saluhuddatabaseutils.models.user.HarrisBenedictBMRActivityFactor;
import com.uhu.saluhuddatabaseutils.models.user.SaluhudUser;
import com.uhu.saluhuddatabaseutils.models.user.SaluhudUserFitnessData;
import com.uhu.saluhuddatabaseutils.service.fitness.SaluhudUserFitnessDataService;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppSaluhudUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@RestController
@RequestMapping("/saluhud-mobile-app/user-data")
public class SaluhudUserDataController
{
    @Autowired
    private MobileAppHttpRequestService mobileAppHttpRequestService;
    
    @Autowired
    private MobileAppSaluhudUserService mobileAppSaluhudUserService;
    
    @Autowired
    private SaluhudUserFitnessDataService saluhudUserFitnessDataService;
    
    @Autowired
    private MobileAppLocaleProvider mobileAppLocaleProvider;
    
    @Autowired
    private JWTService jwtService;

    @GetMapping("/user")
    public ResponseEntity<Object> getSaluhudUserData(HttpServletRequest request)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);

        String username = jwtService.extractUsername(jwt);

        Optional<SaluhudUser> saluhudUserOptional = mobileAppSaluhudUserService.findByUsername(username);

        if (saluhudUserOptional.isEmpty())
        {
            ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), "User not found.");
            
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        SaluhudUser saluhudUser = saluhudUserOptional.get();

        SaluhudUserDTO saluhudUserDTO = new SaluhudUserDTO(saluhudUser.getUsername(), 
                saluhudUser.getEmail(), saluhudUser.getName(), saluhudUser.getSurname(), 
                saluhudUser.getPhoneNumber());

        return new ResponseEntity<>(saluhudUserDTO, HttpStatus.OK);
    }
    
    @GetMapping("/user-fitness-data")
    public ResponseEntity<SaluhudUserFitnessDataDTO> getSaluhudUserFitnessData(HttpServletRequest request)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        Optional<SaluhudUserFitnessData> fitnessDataOptional = 
                mobileAppSaluhudUserService.getSaluhudUserFitnessData(username);
        
        SaluhudUserFitnessDataDTO fitnessDataDTO = new SaluhudUserFitnessDataDTO();
        
        if(fitnessDataOptional.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        
        SaluhudUserFitnessData fitnessData = fitnessDataOptional.get();
        fitnessDataDTO.setAge(fitnessData.getAge());
        fitnessDataDTO.setBiologicalSex(fitnessData.getBiologicalSex().getSexName());
        fitnessDataDTO.setFitnessTarget(fitnessData.getFitnessTarget().getFitnessTargetName());
        fitnessDataDTO.setBodyComposition(fitnessData.getBodyComposition());
        fitnessDataDTO.setBodyMassIndex(fitnessData.getBodyMassIndex());
        fitnessDataDTO.setDailyKilocaloriesObjective(fitnessData.getMaintenanceDailyKilocalories());
        fitnessDataDTO.setFitnessTargetRecommendedKilocalories(fitnessData.getFitnessTargetRecommendedKilocalories());
        fitnessDataDTO.setHeight(fitnessData.getHeight());
        fitnessDataDTO.setRecommendedDailySteps(fitnessData.getRecommendedDailySteps());
        fitnessDataDTO.setRecommendedDailyWaterLiters(fitnessData.getRecommendedDailyWaterLiters());
        fitnessDataDTO.setRecommendedSleepHours(fitnessData.getRecommendedSleepHours());
        fitnessDataDTO.setWeight(fitnessData.getWeight());
        fitnessDataDTO.setActivityFactor(fitnessData.getActivityFactor().getActivityFactorValue());
                
        return new ResponseEntity<>(fitnessDataDTO, HttpStatus.OK);
    }
    
    @PostMapping("/user-fitness-data")
    public ResponseEntity<Object> saveSaluhudUserFitnessData(HttpServletRequest request, 
            @RequestBody @Valid SaveSaluhudUserFitnessDataDTO saveFitnessDataDTO)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        Optional<SaluhudUser> saluhudUserOptional = this.mobileAppSaluhudUserService.findByUsername(username);
        
        if(saluhudUserOptional.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        
        BodyComposition bodyComposition = null;
        
        if(saveFitnessDataDTO.getLeanBodyMassPercentage() != null && saveFitnessDataDTO.getBodyFatPercentage() != null 
                && saveFitnessDataDTO.getLeanBodyMassWeight() != null && saveFitnessDataDTO.getBodyFatWeight() != null)
        {
            bodyComposition = new BodyComposition(saveFitnessDataDTO.getLeanBodyMassPercentage(), 
                saveFitnessDataDTO.getBodyFatPercentage(), saveFitnessDataDTO.getBodyFatWeight(), 
                    saveFitnessDataDTO.getLeanBodyMassWeight());
        }
        
        SaluhudUserFitnessData fitnessData = this.saluhudUserFitnessDataService.buildSaluhudUserFitnessData(
                saveFitnessDataDTO.getWeight(), saveFitnessDataDTO.getHeight(), 
                saveFitnessDataDTO.getAge(), BiologicalSex.fromSexName(saveFitnessDataDTO.getBiologicalSex()), 
                HarrisBenedictBMRActivityFactor.fromActivityFactorValue(saveFitnessDataDTO.getActivityFactor()), bodyComposition,
                FitnessTargetEnum.fromFitnessTargetName(saveFitnessDataDTO.getFitnessTarget()));
        
        SaluhudUser saluhudUser = saluhudUserOptional.get();
        
        saluhudUser.setFitnessData(fitnessData);
        
        this.mobileAppSaluhudUserService.updateSaluhudUser(saluhudUser);
                
        return new ResponseEntity<>(new ApiInformationResponse(request.getServletPath(), 
                mobileAppLocaleProvider.getTranslation("fitnessDataProfile.fitnessDataSavedSuccessfully", 
                        MobileAppLocaleProvider.MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request))), 
                HttpStatus.OK);
    }
}
