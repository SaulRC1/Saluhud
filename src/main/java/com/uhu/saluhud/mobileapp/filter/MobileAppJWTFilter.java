package com.uhu.saluhud.mobileapp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uhu.saluhud.mobileapp.response.ApiErrorResponse;
import com.uhu.saluhud.mobileapp.response.ApiUnauthorizedErrorResponse;
import com.uhu.saluhud.mobileapp.service.JWTService;
import com.uhu.saluhud.mobileapp.service.MobileAppHttpRequestService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for checking if the JSON Web Token that comes from a HTTP request is valid
 * for accessing mobile app endpoints.
 * 
 * @author Saúl Rodríguez Naranjo
 */
public class MobileAppJWTFilter implements Filter
{
    private final JWTService jwtService;
    private final MobileAppHttpRequestService mobileAppHttpRequestService;
    private final ObjectMapper objectMapper;

    public MobileAppJWTFilter(JWTService jwtService, MobileAppHttpRequestService mobileAppHttpRequestService)
    {
        this.jwtService = jwtService;
        this.mobileAppHttpRequestService = mobileAppHttpRequestService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        if(!httpRequest.getServletPath().startsWith("/saluhud-mobile-app"))
        {
            fc.doFilter(request, response);
            return;
        }
        
        if(httpRequest.getServletPath().equals("/saluhud-mobile-app/registration")
                || httpRequest.getServletPath().equals("/saluhud-mobile-app/login"))
        {
            fc.doFilter(request, response);
            return;
        }
        
        String jwt = mobileAppHttpRequestService.getJsonWebToken(httpRequest);
        
        if(jwt.isBlank()) 
        {
            sendUnauthorizedResponse(response, httpRequest, "Unauthorized: No JSON Web Token found");
            return;
        }
        
        if(jwtService.isTokenExpired(jwt))
        {
            sendUnauthorizedResponse(response, httpRequest, "Unauthorized: JSON Web Token has already expired");
            return;
        }
        
        fc.doFilter(request, response);
    }
    
    private void sendUnauthorizedResponse(ServletResponse response, HttpServletRequest httpRequest, String message) throws IOException
    {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
        
        ApiUnauthorizedErrorResponse unauthorizedErrorResponse = 
                    new ApiUnauthorizedErrorResponse(httpRequest.getServletPath(), message);
        
        httpResponse.getWriter().write(objectMapper.writeValueAsString(unauthorizedErrorResponse));
    }
}
