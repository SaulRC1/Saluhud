package com.uhu.saluhud.mobileapp.controller;

import com.uhu.saluhuddatabaseutils.models.user.SaluhudUser;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppSaluhudUserService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
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
    
    @PostMapping
    public ResponseEntity<Object> registerSaluhudUser(@RequestBody @Valid SaluhudUser saluhudUser)
    {   
        if(mobileAppSaluhudUserService.existsByUsername(saluhudUser.getUsername()))
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "The username specified is already in use.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        
        if(mobileAppSaluhudUserService.existsByEmailIgnoreCase(saluhudUser.getEmail()))
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "The email specified is already in use.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        
        if(mobileAppSaluhudUserService.existsByPhoneNumber(saluhudUser.getPhoneNumber()))
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "The phone number specified is already in use.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        
        mobileAppSaluhudUserService.saveUser(saluhudUser);
        
        return ResponseEntity.ok("User registered successfully");
    }
    
}
