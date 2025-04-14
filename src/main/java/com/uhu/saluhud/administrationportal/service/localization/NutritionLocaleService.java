package com.uhu.saluhud.administrationportal.service.localization;

import com.uhu.saluhud.localization.NutritionLocaleProvider;
import static com.uhu.saluhud.localization.NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@Service
public class NutritionLocaleService 
{
    @Autowired
    private NutritionLocaleProvider nutritionLocaleProvider;

    /**
     * Adds a key-translation pair into the specified locale's nutrition
     * translation bundle.
     * 
     * @param key The key to be added
     * @param translation The translation to be added for the key
     * @param locale The desired translation bundle's locale
     * @param bundle The bundle where the addition must be made
     * @throws java.io.IOException If the translation bundle is not found in the
     * disk.
     */
    public synchronized void addKeyToTranslationBundle(String key, String translation, Locale locale, String bundle) throws IOException
    {
        if(key == null || key.trim().isBlank())
        {
            throw new IllegalArgumentException("Translation key must not be null");
        }
        
        if(translation == null || translation.trim().isBlank())
        {
            throw new IllegalArgumentException("Translation must not be null");
        }
        
        if(bundle == null || bundle.trim().isBlank())
        {
            throw new IllegalArgumentException("Translation bundle must not be null");
        }
        
        if(locale == null)
        {
            throw new IllegalArgumentException("Locale must not be null");
        }
        
        if(!nutritionLocaleProvider.isLocaleSupported(locale))
        {
            throw new IllegalArgumentException("Locale not supported");
        }
        
        if(!nutritionLocaleProvider.getTranslationBundles().containsKey(bundle + "_" + locale.toLanguageTag()))
        {
            throw new IllegalArgumentException("Translation bundle not found");
        }
        
        Properties translationBundle = this.nutritionLocaleProvider.getTranslationBundles().get(bundle + "_" + locale.toLanguageTag());
        translationBundle.put(key, translation);
        
        String translationBundlePath = this.nutritionLocaleProvider.getTranslationsRootFolder() 
                + File.separator + bundle + "_" + locale.toLanguageTag() + ".properties";
        
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(translationBundlePath), StandardCharsets.UTF_8))
        {
            translationBundle.store(writer, "Translations for " + bundle);
        } 
        catch (IOException e)
        {
            throw e;
        }
    }
    
    public synchronized void editKeyInTranslationBundle(String key, String translation, Locale locale, String bundle) throws IOException
    {
        addKeyToTranslationBundle(key, translation, locale, bundle);
    }
    
    public synchronized void deleteKeyFromTranslationBundle(String key, Locale locale, String bundle) throws IOException
    {
        if(key == null || key.trim().isBlank())
        {
            throw new IllegalArgumentException("Translation key must not be null");
        }
        
        if(bundle == null || bundle.trim().isBlank())
        {
            throw new IllegalArgumentException("Translation bundle must not be null");
        }
        
        if(locale == null)
        {
            throw new IllegalArgumentException("Locale must not be null");
        }
        
        if(!nutritionLocaleProvider.isLocaleSupported(locale))
        {
            throw new IllegalArgumentException("Locale not supported");
        }
        
        if(!nutritionLocaleProvider.getTranslationBundles().containsKey(bundle + "_" + locale.toLanguageTag()))
        {
            throw new IllegalArgumentException("Translation bundle not found");
        }
        
        Properties translationBundle = this.nutritionLocaleProvider.getTranslationBundles().get(bundle + "_" + locale.toLanguageTag());
        translationBundle.remove(key);
        
        String translationBundlePath = this.nutritionLocaleProvider.getTranslationsRootFolder() 
                + File.separator + bundle + "_" + locale.toLanguageTag() + ".properties";
        
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(translationBundlePath), StandardCharsets.UTF_8))
        {
            translationBundle.store(writer, "Translations for " + bundle);
        } 
        catch (IOException e)
        {
            throw e;
        }
    }
}
