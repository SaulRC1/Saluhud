package com.uhu.saluhud.mobileapp.security;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Saluhud JSON Web Token properties. They are located and can be defined in 
 * src/main/resources/saluhud_json_web_token.properties
 * 
 * @author Saúl Rodríguez Naranjo
 */
public class SaluhudJWTProperties 
{
    private final String issuer;
    private final String subject;
    private final String secretKeyPlain;
    private final String secretKeyBase64;
    private final int expirationTime;
    private final String httpHeader;
    
    public SaluhudJWTProperties() throws IOException
    {
        Properties saluhudJWTProperties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("saluhud_json_web_token.properties");
        saluhudJWTProperties.load(stream);
        
        this.issuer = (String) saluhudJWTProperties.get("com.uhu.saluhud.jwt.issuer");
        this.subject = (String) saluhudJWTProperties.get("com.uhu.saluhud.jwt.subject");
        this.secretKeyPlain = (String) saluhudJWTProperties.get("com.uhu.saluhud.jwt.secretKeyPlain");
        this.secretKeyBase64 = (String) saluhudJWTProperties.get("com.uhu.saluhud.jwt.secretKeyBase64");
        this.expirationTime = Integer.parseInt((String) saluhudJWTProperties.get("com.uhu.saluhud.jwt.expirationTime"));
        this.httpHeader = (String) saluhudJWTProperties.get("com.uhu.saluhud.jwt.httpHeader");
    }

    public String getIssuer()
    {
        return issuer;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getSecretKeyPlain()
    {
        return secretKeyPlain;
    }

    public String getSecretKeyBase64()
    {
        return secretKeyBase64;
    }

    /**
     * Gets the JSON Web Token expiration time in seconds. This expiration must 
     * be aggregated to the "issued at" time.
     * 
     * @return The JSON Web Token expiration time in seconds.
     */
    public int getExpirationTime()
    {
        return expirationTime;
    }

    /**
     * Gets the HTTP Header where the JSON Web Token must be stored inside an
     * incoming request.
     * 
     * @return The HTTP Header where the JWT is located inside a request.
     */
    public String getHttpHeader()
    {
        return httpHeader;
    }
}
