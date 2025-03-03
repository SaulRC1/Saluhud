package com.uhu.saluhud.mobileapp.service;

import com.uhu.saluhud.mobileapp.enums.JWTRegisteredClaim;
import com.uhu.saluhud.mobileapp.security.SaluhudJWTProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service used for operating with JSON Web Tokens
 * 
 * @author Saúl Rodríguez Naranjo
 */
@Service
public class JWTService 
{
    @Autowired
    private SaluhudJWTProperties saluhudJWTProperties;
    
    /**
     * Generates a JSON Web Token with the given registered claims
     * 
     * @param registeredClaims The registered claims like issue, subject, etc. 
     * to be added to the JSON Web Token.
     * @param privateClaims Private claims to share information between parties.
     * They must not be registered nor public claims.
     * 
     * @return The generated JSON Web Token with the given registered claims 
     */
    public String generateToken(Map<JWTRegisteredClaim, Object> registeredClaims, Map<String, String> privateClaims)
    {        
        return Jwts.builder().issuer(registeredClaims.containsKey(JWTRegisteredClaim.ISSUER) ? (String) registeredClaims.get(JWTRegisteredClaim.ISSUER) : "")
                .subject(registeredClaims.containsKey(JWTRegisteredClaim.SUBJECT) ? (String) registeredClaims.get(JWTRegisteredClaim.SUBJECT) : "")
                .issuedAt(new Date()).signWith(getSigningKey())
                .expiration(registeredClaims.containsKey(JWTRegisteredClaim.EXPIRATION_TIME) ? 
                        (Date) registeredClaims.get(JWTRegisteredClaim.EXPIRATION_TIME) : null)
                .claims(privateClaims != null ? privateClaims : Collections.emptyMap()).compact();
    }
    
    private Key getSigningKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(this.saluhudJWTProperties.getSecretKeyBase64());        
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
