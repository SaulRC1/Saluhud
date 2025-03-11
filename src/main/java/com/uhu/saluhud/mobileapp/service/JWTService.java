package com.uhu.saluhud.mobileapp.service;

import com.uhu.saluhud.mobileapp.enums.JWTRegisteredClaim;
import com.uhu.saluhud.mobileapp.enums.JWTSaluhudPrivateClaim;
import com.uhu.saluhud.mobileapp.security.SaluhudJWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
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
    public String generateToken(Map<JWTRegisteredClaim, ?> registeredClaims, Map<String, ?> privateClaims)
    {
        return Jwts.builder().issuer(registeredClaims.containsKey(JWTRegisteredClaim.ISSUER) ? (String) registeredClaims.get(JWTRegisteredClaim.ISSUER) : "")
                .subject(registeredClaims.containsKey(JWTRegisteredClaim.SUBJECT) ? (String) registeredClaims.get(JWTRegisteredClaim.SUBJECT) : "")
                .issuedAt(registeredClaims.containsKey(JWTRegisteredClaim.ISSUED_AT) ? (Date) registeredClaims.get(JWTRegisteredClaim.ISSUED_AT) : new Date())
                .signWith(getSigningKey())
                .expiration(registeredClaims.containsKey(JWTRegisteredClaim.EXPIRATION_TIME)
                        ? (Date) registeredClaims.get(JWTRegisteredClaim.EXPIRATION_TIME) : null)
                .claims(privateClaims != null ? privateClaims : Collections.emptyMap()).compact();
    }

    private Key getSigningKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(this.saluhudJWTProperties.getSecretKeyBase64());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token)
    {
        return Jwts
                .parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token)
    {
        try
        {
            Claims claims = extractAllClaims(token);

            Date expirationDate = claims.get(JWTRegisteredClaim.EXPIRATION_TIME.getClaimName(), Date.class);

            if (expirationDate == null)
            {
                return false;
            }

            return expirationDate.before(new Date());
        } 
        catch (ExpiredJwtException e)
        {
            return true;
        }
    }

    public String extractUsername(String token)
    {
        Claims claims = extractAllClaims(token);

        return claims.get(JWTSaluhudPrivateClaim.SALUHUD_USERNAME.getClaimName(), String.class);
    }
    
}
