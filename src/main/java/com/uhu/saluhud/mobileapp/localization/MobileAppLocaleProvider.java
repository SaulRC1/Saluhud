package com.uhu.saluhud.mobileapp.localization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Locale provider for Saluhud Mobile App API endpoints.
 *
 * @author Saúl Rodríguez Naranjo
 */
public class MobileAppLocaleProvider
{
    private static final Logger logger = Logger.getLogger(MobileAppLocaleProvider.class.getName());
    
    private final List<Locale> supportedLocales;
    private static final String ROOT_RESOURCE_BUNDLES_FOLDER = "translations.mobileapp";
    private static final String ERROR_MESSAGES_RESOURCE_BUNDLE_PREFIX = "error_messages";
    private static final String MESSAGES_RESOURCE_BUNDLE_PREFIX = "messages";
    
    public static final String ERROR_MESSAGES_RESOURCE_BUNDLE_KEY 
            = ROOT_RESOURCE_BUNDLES_FOLDER + "." + ERROR_MESSAGES_RESOURCE_BUNDLE_PREFIX;
    
    public static final String MESSAGES_RESOURCE_BUNDLE_KEY 
            = ROOT_RESOURCE_BUNDLES_FOLDER + "." + MESSAGES_RESOURCE_BUNDLE_PREFIX;

    private Map<String, ResourceBundle> resourceBundles;

    public MobileAppLocaleProvider()
    {
        supportedLocales = Arrays.asList(Locale.ENGLISH, Locale.of("es"));
        resourceBundles = new HashMap<>();
        
        for (Locale supportedLocale : supportedLocales)
        {
            ResourceBundle errorMessagesResourceBundles = 
                    ResourceBundle.getBundle(ROOT_RESOURCE_BUNDLES_FOLDER + "." + ERROR_MESSAGES_RESOURCE_BUNDLE_PREFIX, supportedLocale);
            logger.log(Level.INFO, "Loaded {0} resource bundle for locale {1}", 
                    new String[]{errorMessagesResourceBundles.getBaseBundleName(), supportedLocale.toLanguageTag()});
            resourceBundles.put(errorMessagesResourceBundles.getBaseBundleName() + "_" + supportedLocale.toLanguageTag(), errorMessagesResourceBundles);
            
            ResourceBundle messagesResourceBundles = 
                    ResourceBundle.getBundle(ROOT_RESOURCE_BUNDLES_FOLDER + "." + MESSAGES_RESOURCE_BUNDLE_PREFIX, supportedLocale);
            logger.log(Level.INFO, "Loaded {0} resource bundle for locale {1}", 
                    new String[]{messagesResourceBundles.getBaseBundleName(), supportedLocale.toLanguageTag()});
            resourceBundles.put(messagesResourceBundles.getBaseBundleName() + "_" + supportedLocale.toLanguageTag(), messagesResourceBundles);
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
        
        ResourceBundle rb = resourceBundles.get(bundle + "_" + locale.toLanguageTag());
        
        if(rb == null)
        {
            throw new IllegalArgumentException("Bundle '" + bundle + "' not found");
        }
        
        return rb.getString(key);
    }
}
