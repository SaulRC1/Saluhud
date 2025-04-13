package com.uhu.saluhud.configuration;

import com.uhu.saluhud.localization.NutritionLocaleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Localization configuration for general usage inside Saluhud codebase.
 * 
 * @author Saúl Rodríguez Naranjo
 */
@Configuration
public class SaluhudLocalizationConfiguration 
{
    @Value("${saluhudNutritionTranslations}")
    private String translationsRootFolder;
    
    @Bean
    public NutritionLocaleProvider nutritionLocaleProvider() throws Exception
    {
        return new NutritionLocaleProvider(translationsRootFolder);
    }
}
