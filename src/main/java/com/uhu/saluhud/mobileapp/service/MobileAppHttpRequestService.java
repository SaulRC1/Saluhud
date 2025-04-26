package com.uhu.saluhud.mobileapp.service;

import com.uhu.saluhud.mobileapp.security.SaluhudJWTProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@Service
public class MobileAppHttpRequestService 
{
    @Autowired
    private SaluhudJWTProperties saluhudJWTProperties;
    
    public Locale getMobileAppLocale(HttpServletRequest request)
    {
        if(request == null)
        {
            throw new IllegalArgumentException("Null request not accepted");
        }
        
        String mobileAppLanguage = request.getHeader("Accept-Language");
        return (mobileAppLanguage == null || mobileAppLanguage.isBlank()) ? Locale.ENGLISH : Locale.of(mobileAppLanguage);
    }
    
    public String getJsonWebToken(HttpServletRequest request)
    {
        if(request == null)
        {
            throw new IllegalArgumentException("Null request not accepted");
        }
        
        String jwt = request.getHeader(saluhudJWTProperties.getHttpHeader());
        
        return (jwt == null || jwt.isBlank()) ? "" : jwt;
    }
}
