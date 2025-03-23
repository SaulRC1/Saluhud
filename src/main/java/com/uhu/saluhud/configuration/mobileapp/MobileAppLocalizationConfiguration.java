package com.uhu.saluhud.configuration.mobileapp;

import com.uhu.saluhud.mobileapp.localization.MobileAppLocaleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@Configuration
public class MobileAppLocalizationConfiguration 
{
    @Bean
    public MobileAppLocaleProvider mobileAppLocaleProvider()
    {
        return new MobileAppLocaleProvider();
    }
}
