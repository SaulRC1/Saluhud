package com.uhu.saluhud.configuration.security;

import com.uhu.saluhud.mobileapp.security.SaluhudJWTProperties;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * General configuration for operating with JSON Web Tokens
 * 
 * @author Saúl Rodríguez Naranjo
 */
@Configuration
public class JWTConfiguration 
{
    @Bean
    public SaluhudJWTProperties saluhudJWTProperties() throws IOException
    {
        return new SaluhudJWTProperties();
    }
}
