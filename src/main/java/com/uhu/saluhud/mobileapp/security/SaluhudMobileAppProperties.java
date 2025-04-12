package com.uhu.saluhud.mobileapp.security;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Saluhud mobile app properties located in file. They are located and can be 
 * defined in src/main/resources/saluhud_mobile_app.properties
 * @author Saúl Rodríguez Naranjo
 */
public class SaluhudMobileAppProperties 
{
    private final String apiKey;
    private final String apiKeyHTTPRequestHeader;
    
    public SaluhudMobileAppProperties() throws IOException
    {
        Properties saluhudMobileAppProperties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("saluhud_mobile_app.properties");
        saluhudMobileAppProperties.load(stream);
        
        this.apiKey = (String) saluhudMobileAppProperties.get("com.uhu.saluhud.mobileapp.apiKey");
        this.apiKeyHTTPRequestHeader = (String) saluhudMobileAppProperties.getProperty("com.uhu.saluhud.mobileapp.apiKeyHTTPRequestHeader");
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public String getApiKeyHTTPRequestHeader()
    {
        return apiKeyHTTPRequestHeader;
    }
    
}
