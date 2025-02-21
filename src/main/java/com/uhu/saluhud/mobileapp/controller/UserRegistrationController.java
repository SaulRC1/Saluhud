package com.uhu.saluhud.mobileapp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Registration end-point for Saluhud's mobile app. This controller will be used
 * to register new Saluhud users into the system.
 * 
 * @author Saúl Rodríguez Naranjo
 */
@RestController
@RequestMapping("saluhud-mobile-app/registration")
public class UserRegistrationController 
{
    @PostMapping
    public void registerSaluhudUser()
    {
        
    }
}
