package com.uhu.saluhud.localization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
    public static final String ALLERGENIC_TRANSLATION_BUNDLE_PREFIX = "allergenic";

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

            logger.log(Level.INFO, "Loading recipes translations bundle for locale \"{0}\" from: \"{1}\"", new String[]
            {
                supportedLocale.toLanguageTag(),
                recipesTranslationsBundlePath
            });

            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(recipesTranslationsBundlePath), 
                    StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(inputStreamReader))
            {

                Properties recipesTranslationsBundle = new Properties();
                recipesTranslationsBundle.load(reader);

                translationBundles.put(RECIPES_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag(), recipesTranslationsBundle);

                logger.log(Level.INFO, "Loaded recipes translations bundle for locale \"{0}\" succesfully", supportedLocale.toLanguageTag());
            } catch (Exception e)
            {
                throw e;
            }

            String ingredientsTranslationsBundlePath = this.translationsRootFolder + File.separator
                    + INGREDIENTS_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag() + ".properties";

            logger.log(Level.INFO, "Loading ingredients translations bundle for locale \"{0}\" from: \"{1}\"", new String[]
            {
                supportedLocale.toLanguageTag(),
                ingredientsTranslationsBundlePath
            });

            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(ingredientsTranslationsBundlePath), 
                    StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(inputStreamReader))
            {

                Properties ingredientsTranslationsBundle = new Properties();
                ingredientsTranslationsBundle.load(reader);

                translationBundles.put(INGREDIENTS_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag(), ingredientsTranslationsBundle);

                logger.log(Level.INFO, "Loaded ingredients translations bundle for locale \"{0}\" succesfully", supportedLocale.toLanguageTag());
            } catch (Exception e)
            {
                throw e;
            }

            String recipeElaborationStepsTranslationsBundlePath = this.translationsRootFolder + File.separator
                    + RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag() + ".properties";

            logger.log(Level.INFO, "Loading recipe elaboration steps translations bundle for locale \"{0}\" from: \"{1}\"", new String[]
            {
                supportedLocale.toLanguageTag(),
                recipeElaborationStepsTranslationsBundlePath
            });

            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(recipeElaborationStepsTranslationsBundlePath), 
                    StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(inputStreamReader))
            {

                Properties recipeElaborationStepsTranslationsBundle = new Properties();
                recipeElaborationStepsTranslationsBundle.load(reader);

                translationBundles.put(RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag(), recipeElaborationStepsTranslationsBundle);

                logger.log(Level.INFO, "Loaded recipe elaboration steps translations bundle for locale \"{0}\" succesfully", supportedLocale.toLanguageTag());
            } catch (Exception e)
            {
                throw e;
            }

            String recipeIngredientTranslationsBundlePath = this.translationsRootFolder + File.separator
                    + RECIPE_INGREDIENT_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag() + ".properties";

            logger.log(Level.INFO, "Loading recipe ingredient translations bundle for locale \"{0}\" from: \"{1}\"", new String[]
            {
                supportedLocale.toLanguageTag(),
                recipeIngredientTranslationsBundlePath
            });

            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(recipeIngredientTranslationsBundlePath), StandardCharsets.UTF_8); 
                    BufferedReader reader = new BufferedReader(inputStreamReader))
            {

                Properties recipeIngredientTranslationsBundle = new Properties();
                recipeIngredientTranslationsBundle.load(reader);

                translationBundles.put(RECIPE_INGREDIENT_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag(), recipeIngredientTranslationsBundle);

                logger.log(Level.INFO, "Loaded recipe ingredient translations bundle for locale \"{0}\" succesfully", supportedLocale.toLanguageTag());
            } catch (Exception e)
            {
                throw e;
            }
            
            String allergenicTranslationsBundlePath = this.translationsRootFolder + File.separator
                    + ALLERGENIC_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag() + ".properties";

            logger.log(Level.INFO, "Loading allergenic translations bundle for locale \"{0}\" from: \"{1}\"", new String[]
            {
                supportedLocale.toLanguageTag(),
                allergenicTranslationsBundlePath
            });
            
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(allergenicTranslationsBundlePath), StandardCharsets.UTF_8); 
                    BufferedReader reader = new BufferedReader(inputStreamReader))
            {

                Properties allergenicTranslationsBundle = new Properties();
                allergenicTranslationsBundle.load(reader);

                translationBundles.put(ALLERGENIC_TRANSLATION_BUNDLE_PREFIX + "_" + supportedLocale.toLanguageTag(), allergenicTranslationsBundle);

                logger.log(Level.INFO, "Loaded allergenic translations bundle for locale \"{0}\" succesfully", supportedLocale.toLanguageTag());
            } catch (Exception e)
            {
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
     * Gets the translation for the key specified by parameter within the
     * specified bundle and locale.
     *
     * @param key The key for the desired translation.
     * @param bundle The bundle name where the key resides. Must be only the
     * bundle's base name.
     * @param locale The desired translation's locale.
     *
     * @return The translation for the specified key, within the specified
     * bundle, in the desired locale.
     */
    public String getTranslation(String key, String bundle, Locale locale)
    {
        if (!isLocaleSupported(locale))
        {
            throw new IllegalArgumentException("Locale " + locale.toString() + " is not supported");
        }

        Properties translationBundle = translationBundles.get(bundle + "_" + locale.toLanguageTag());

        if (translationBundle == null)
        {
            throw new IllegalArgumentException("Bundle '" + bundle + "' not found");
        }

        return (String) translationBundle.get(key);
    }

    public Map<String, Properties> getTranslationBundles()
    {
        return translationBundles;
    }

    public String getTranslationsRootFolder()
    {
        return translationsRootFolder;
    }
}
