package com.uhu.saluhud.test.administrationportal.service.localization;

import com.uhu.saluhud.administrationportal.service.localization.NutritionLocaleService;
import com.uhu.saluhud.localization.NutritionLocaleProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NutritionLocaleServiceTest 
{
    @Autowired
    private NutritionLocaleService nutritionLocaleService;
    
    @Autowired
    private NutritionLocaleProvider nutritionLocaleProvider;
    
    @Test
    @Order(1)
    public void test_add_key_to_bundle()
    {
        try
        {
            nutritionLocaleService.addKeyToTranslationBundle("test", "test", 
                    Locale.ENGLISH, NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX);
            
            String translationBundlePath = nutritionLocaleProvider.getTranslationsRootFolder() + File.separator 
                    + NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX + "_" + Locale.ENGLISH.toLanguageTag() + ".properties";
            
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(translationBundlePath), 
                    StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(inputStreamReader))
            {

                Properties translationsBundle = new Properties();
                translationsBundle.load(reader);

                Assertions.assertTrue(translationsBundle.containsKey("test") && (translationsBundle.get("test").equals("test")));
            } catch (IOException e)
            {
                throw e;
            }
        } catch (IOException ex)
        {
            Logger.getLogger(NutritionLocaleServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
    
    @Test
    @Order(2)
    public void test_edit_key_in_bundle()
    {
        try
        {
            nutritionLocaleService.editKeyInTranslationBundle("test", "test_edit", 
                    Locale.ENGLISH, NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX);
            
            String translationBundlePath = nutritionLocaleProvider.getTranslationsRootFolder() + File.separator 
                    + NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX + "_" + Locale.ENGLISH.toLanguageTag() + ".properties";
            
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(translationBundlePath), 
                    StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(inputStreamReader))
            {

                Properties translationsBundle = new Properties();
                translationsBundle.load(reader);

                Assertions.assertTrue(translationsBundle.containsKey("test") && (translationsBundle.get("test").equals("test_edit")));
            } catch (IOException e)
            {
                throw e;
            }
            
        } catch (IOException ex)
        {
            Logger.getLogger(NutritionLocaleServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
    
    @Test
    @Order(3)
    public void test_delete_key_from_bundle()
    {
        try
        {
            nutritionLocaleService.deleteKeyFromTranslationBundle("test", 
                    Locale.ENGLISH, NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX);
            
            String translationBundlePath = nutritionLocaleProvider.getTranslationsRootFolder() + File.separator 
                    + NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX + "_" + Locale.ENGLISH.toLanguageTag() + ".properties";
            
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(translationBundlePath), 
                    StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(inputStreamReader))
            {

                Properties translationsBundle = new Properties();
                translationsBundle.load(reader);

                Assertions.assertFalse(translationsBundle.containsKey("test"));
            } catch (IOException e)
            {
                throw e;
            }
            
        } catch (IOException ex)
        {
            Logger.getLogger(NutritionLocaleServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
