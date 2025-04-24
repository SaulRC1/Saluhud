package com.uhu.saluhud.mobileapp.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@Service
public class MobileAppHttpRequestService 
{
    public Locale getMobileAppLocale(HttpServletRequest request)
    {
        if(request == null)
        {
            throw new IllegalArgumentException("Null request not accepted");
        }
        
        String mobileAppLanguage = request.getHeader("Accept-Language");
        return (mobileAppLanguage == null || mobileAppLanguage.isBlank()) ? Locale.ENGLISH : Locale.of(mobileAppLanguage);
    }
}
