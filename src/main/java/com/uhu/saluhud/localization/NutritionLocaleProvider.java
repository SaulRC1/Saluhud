package com.uhu.saluhud.localization;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Locale provider for Saluhud nutrition related entities, components, etc.
 *
 * @author Saúl Rodríguez Naranjo
 */
public class NutritionLocaleProvider
{
    private static final Logger logger = Logger.getLogger(NutritionLocaleProvider.class.getName());
    
    private final List<Locale> supportedLocales;

    private String translationsRootFolder;
    
    public static final String RECIPES_TRANSLATION_BUNDLE_PREFIX = "recipes";
    public static final String INGREDIENTS_TRANSLATION_BUNDLE_PREFIX = "ingredients";
    public static final String RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX = "recipeElaborationSteps";
    public static final String RECIPE_INGREDIENT_TRANSLATION_BUNDLE_PREFIX = "recipeIngredient";

    private Map<String, Properties> translationBundles;

    public NutritionLocaleProvider(String translationsRootFolder) throws Exception
    {
        logger.log(Level.INFO, "Nutrition translations being loaded from: \"{0}\"", translationsRootFolder);
        this.translationsRootFolder = translationsRootFolder;
        
        supportedLocales = Arrays.asList(Locale.ENGLISH, Locale.of("es"));
        translationBundles = new HashMap<>();
        
        for (Locale supportedLocale : supportedLocales)
        {
            String recipesTranslationsBundlePath = this.translationsRootFolder + File.separator 
                    + RECIPES_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag() + ".properties";
            
            logger.log(Level.INFO, "Loading recipes translations bundle for locale \"{0}\" from: \"{1}\"", new String[] {supportedLocale.toLanguageTag(), 
                recipesTranslationsBundlePath});
            
            try(FileInputStream input = new FileInputStream(recipesTranslationsBundlePath))
            {
                Properties recipesTranslationsBundle = new Properties();
                recipesTranslationsBundle.load(input);
                
                translationBundles.put(RECIPES_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag(), recipesTranslationsBundle);
                
                logger.log(Level.INFO, "Loaded recipes translations bundle for locale \"{0}\" succesfully", supportedLocale.toLanguageTag());
            }
            catch(Exception e)
            {
                //logger.log(Level.SEVERE, e.getMessage(), e);
                throw e;
            }
            
            String ingredientsTranslationsBundlePath = this.translationsRootFolder + File.separator 
                    + INGREDIENTS_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag() + ".properties";
            
            logger.log(Level.INFO, "Loading ingredients translations bundle for locale \"{0}\" from: \"{1}\"", new String[] {supportedLocale.toLanguageTag(), 
                ingredientsTranslationsBundlePath});
            
            try(FileInputStream input = new FileInputStream(ingredientsTranslationsBundlePath))
            {
                Properties ingredientsTranslationsBundle = new Properties();
                ingredientsTranslationsBundle.load(input);
                
                translationBundles.put(INGREDIENTS_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag(), ingredientsTranslationsBundle);
                
                logger.log(Level.INFO, "Loaded ingredients translations bundle for locale \"{0}\" succesfully", supportedLocale.toLanguageTag());
            }
            catch(Exception e)
            {
                //logger.log(Level.SEVERE, e.getMessage(), e);
                throw e;
            }
            
            String recipeElaborationStepsTranslationsBundlePath = this.translationsRootFolder + File.separator 
                    + RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag() + ".properties";
            
            logger.log(Level.INFO, "Loading recipe elaboration steps translations bundle for locale \"{0}\" from: \"{1}\"", new String[] {supportedLocale.toLanguageTag(), 
                recipeElaborationStepsTranslationsBundlePath});
            
            try(FileInputStream input = new FileInputStream(recipeElaborationStepsTranslationsBundlePath))
            {
                Properties recipeElaborationStepsTranslationsBundle = new Properties();
                recipeElaborationStepsTranslationsBundle.load(input);
                
                translationBundles.put(RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag(), recipeElaborationStepsTranslationsBundle);
                
                logger.log(Level.INFO, "Loaded recipe elaboration steps translations bundle for locale \"{0}\" succesfully", supportedLocale.toLanguageTag());
            }
            catch(Exception e)
            {
                //logger.log(Level.SEVERE, e.getMessage(), e);
                throw e;
            }
            
            String recipeIngredientTranslationsBundlePath = this.translationsRootFolder + File.separator 
                    + RECIPE_INGREDIENT_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag() + ".properties";
            
            logger.log(Level.INFO, "Loading recipe ingredient translations bundle for locale \"{0}\" from: \"{1}\"", new String[] {supportedLocale.toLanguageTag(), 
                recipeIngredientTranslationsBundlePath});
            
            try(FileInputStream input = new FileInputStream(recipeIngredientTranslationsBundlePath))
            {
                Properties recipeIngredientTranslationsBundle = new Properties();
                recipeIngredientTranslationsBundle.load(input);
                
                translationBundles.put(INGREDIENTS_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag(), recipeIngredientTranslationsBundle);
                
                logger.log(Level.INFO, "Loaded recipe ingredient translations bundle for locale \"{0}\" succesfully", supportedLocale.toLanguageTag());
            }
            catch(Exception e)
            {
                //logger.log(Level.SEVERE, e.getMessage(), e);
                throw e;
            }
        }
    }
    
    public List<Locale> getSupportedLocales()
    {
        return this.supportedLocales;
    }
    
    public boolean isLocaleSupported(Locale locale)
    {
        return this.supportedLocales.contains(locale);
    }

    /**
     * Gets the translation for the key specified by parameter within the specified
     * bundle and locale.
     * 
     * @param key The key for the desired translation.
     * @param bundle The bundle name where the key resides. Must be only the 
     * bundle's base name.
     * @param locale The desired translation's locale.
     * 
     * @return The translation for the specified key, within the specified bundle,
     * in the desired locale.
     */
    public String getTranslation(String key, String bundle, Locale locale)
    {
        if(!isLocaleSupported(locale))
        {
            throw new IllegalArgumentException("Locale " + locale.toString() + " is not supported");
        }
        
        Properties translationBundle = translationBundles.get(bundle + "_" + locale.toLanguageTag());
        
        if(translationBundle == null)
        {
            throw new IllegalArgumentException("Bundle '" + bundle + "' not found");
        }
        
        return (String) translationBundle.get(key);
    }
}
