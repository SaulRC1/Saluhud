package com.uhu.saluhud.configuration.mobileapp;

import com.uhu.saluhud.mobileapp.security.SaluhudMobileAppProperties;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * General configuration for mobile app related beans/components/services, etc.
 * 
 * @author Saúl Rodríguez Naranjo
 */
@Configuration
public class MobileAppConfiguration 
{
    @Bean
    public SaluhudMobileAppProperties saluhudMobileAppProperties() throws IOException
    {
        return new SaluhudMobileAppProperties();
    }
}
