package com.uhu.saluhud.mobileapp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uhu.saluhud.mobileapp.localization.MobileAppLocaleProvider;
import com.uhu.saluhud.mobileapp.response.ApiErrorResponse;
import com.uhu.saluhud.mobileapp.security.SaluhudMobileAppProperties;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class MobileAppAPIKeyFilter implements Filter
{
    private SaluhudMobileAppProperties saluhudMobileAppProperties;
    private ObjectMapper mapper;

    public MobileAppAPIKeyFilter() throws IOException
    {
        this.saluhudMobileAppProperties = new SaluhudMobileAppProperties();
        this.mapper = new ObjectMapper();
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
        
        String requestApiKey = httpRequest.getHeader(saluhudMobileAppProperties.getApiKeyHTTPRequestHeader());
        
        if(requestApiKey == null || requestApiKey.isBlank() || !requestApiKey.equals(saluhudMobileAppProperties.getApiKey()))
        {
            HttpServletResponse res = (HttpServletResponse) response;
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(httpRequest.getServletPath(), 
                    "Invalid API Key");
            
            res.getWriter().write(mapper.writeValueAsString(apiErrorResponse));
            
            return;
        }
        
        fc.doFilter(request, response);
    }

}
