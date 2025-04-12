package com.uhu.saluhud.mobileapp.controller;

import com.uhu.saluhud.mobileapp.dto.user.SaluhudUserAuthenticationDTO;
import com.uhu.saluhud.mobileapp.enums.JWTRegisteredClaim;
import com.uhu.saluhud.mobileapp.enums.JWTSaluhudPrivateClaim;
import com.uhu.saluhud.mobileapp.localization.MobileAppLocaleProvider;
import com.uhu.saluhud.mobileapp.response.ApiErrorResponse;
import com.uhu.saluhud.mobileapp.security.SaluhudJWTProperties;
import com.uhu.saluhud.mobileapp.service.JWTService;
import com.uhu.saluhuddatabaseutils.models.user.SaluhudUser;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppSaluhudUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User authentication controller. This end-point will authenticate mobile app users
 * and provide the corresponding JSON Web Token for further access to other endpoints.
 * @author Saúl Rodríguez Naranjo
 */
@RestController
@RequestMapping("/saluhud-mobile-app/login")
public class UserAuthenticationController 
{
    @Autowired
    private MobileAppSaluhudUserService mobileAppSaluhudUserService;
    
    @Autowired
    private SaluhudJWTProperties saluhudJWTProperties;
    
    @Autowired
    private JWTService jwtService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private MobileAppLocaleProvider mobileAppLocaleProvider;
    
    @PostMapping
    public ResponseEntity<Object> authenticateUser(@RequestBody @Valid SaluhudUserAuthenticationDTO saluhudUserAuthenticationDTO, HttpServletRequest request)
    {
        String mobileAppLanguage = request.getHeader("Accept-Language");
        Locale mobileAppLocale = mobileAppLanguage != null ? Locale.of(mobileAppLanguage) : Locale.ENGLISH;
        
        String username = saluhudUserAuthenticationDTO.getUsername();
        String password = saluhudUserAuthenticationDTO.getRawPassword();
        
        Optional<SaluhudUser> saluhudUser = mobileAppSaluhudUserService.findByUsername(username);
        
        if(saluhudUser.isEmpty())
        {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(request.getServletPath(), 
                    mobileAppLocaleProvider.getTranslation("authentication.usernameNotExistent", 
                            MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppLocale));
            return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
        }
        
        if(!passwordEncoder.matches(password, saluhudUser.get().getPassword()))
        {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(request.getServletPath(), 
                    mobileAppLocaleProvider.getTranslation("authentication.incorrectPassword", 
                            MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppLocale));
            return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
        }
        
        Map<JWTRegisteredClaim, Object> jwtRegisteredClaims = new HashMap<>();
        
        jwtRegisteredClaims.put(JWTRegisteredClaim.ISSUER, saluhudJWTProperties.getIssuer());
        jwtRegisteredClaims.put(JWTRegisteredClaim.SUBJECT, saluhudJWTProperties.getSubject());
        
        Date currentDate = new Date();
        jwtRegisteredClaims.put(JWTRegisteredClaim.ISSUED_AT, currentDate);
        
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.SECOND, (int) saluhudJWTProperties.getExpirationTime());
        Date expirationDate = c.getTime();
        
        jwtRegisteredClaims.put(JWTRegisteredClaim.EXPIRATION_TIME, expirationDate);
        
        Map<String, String> jwtPrivateClaims = new HashMap<>();
        jwtPrivateClaims.put(JWTSaluhudPrivateClaim.SALUHUD_USERNAME.getClaimName(), username);
        
        Map<String, String> jwt = new HashMap<>();
        jwt.put("jwt", jwtService.generateToken(jwtRegisteredClaims, jwtPrivateClaims));
        
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }
}
