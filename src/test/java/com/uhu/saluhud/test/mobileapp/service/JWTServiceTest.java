package com.uhu.saluhud.test.mobileapp.service;

import com.uhu.saluhud.mobileapp.enums.JWTRegisteredClaim;
import com.uhu.saluhud.mobileapp.enums.JWTSaluhudPrivateClaim;
import com.uhu.saluhud.mobileapp.service.JWTService;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@SpringBootTest
public class JWTServiceTest 
{
    @Autowired
    private JWTService jwtService;
    
    @Test
    public void test_getUsername_returns_expected_username()
    {
        String username = "test_username";
        
        Map<JWTRegisteredClaim, Object> registeredClaims = new HashMap<>();
        Map<String, Object> privateClaims = new HashMap<>();
        privateClaims.put(JWTSaluhudPrivateClaim.SALUHUD_USERNAME.getClaimName(), username);
        
        String token = jwtService.generateToken(registeredClaims, privateClaims);
        
        String extractedUsername = jwtService.extractUsername(token);
        
        Assertions.assertEquals(username, extractedUsername);
    }
    
    @Test
    public void test_isTokenExpired_returns_true_when_past_date()
    {
        Map<JWTRegisteredClaim, Object> registeredClaims = new HashMap<>();
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.MARCH, 11, 10, 30, 0);

        Date pastDate = calendar.getTime();
        
        registeredClaims.put(JWTRegisteredClaim.EXPIRATION_TIME, pastDate);
        
        String token = jwtService.generateToken(registeredClaims, null);
        
        Assertions.assertTrue(jwtService.isTokenExpired(token));
    }
    
    @Test
    public void test_isTokenExpired_returns_false_when_date_is_null()
    {
        Map<JWTRegisteredClaim, Object> registeredClaims = new HashMap<>();        
        registeredClaims.put(JWTRegisteredClaim.EXPIRATION_TIME, null);
        
        String token = jwtService.generateToken(registeredClaims, null);
        
        Assertions.assertFalse(jwtService.isTokenExpired(token));
    }
    
    @Test
    public void test_isTokenExpired_returns_false_when_future_date()
    {
        Map<JWTRegisteredClaim, Object> registeredClaims = new HashMap<>();
        
        Date currentDate = new Date();
        
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.SECOND, 86400); //add one day
        Date expirationDate = c.getTime();
        
        registeredClaims.put(JWTRegisteredClaim.EXPIRATION_TIME, expirationDate);
        
        String token = jwtService.generateToken(registeredClaims, null);
        
        Assertions.assertFalse(jwtService.isTokenExpired(token));
    }
}
