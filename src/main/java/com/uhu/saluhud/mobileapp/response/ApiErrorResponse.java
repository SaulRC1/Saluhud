package com.uhu.saluhud.mobileapp.response;

import java.util.Objects;

/**
 * Standard response for API errors
 * 
 * @author Saúl Rodríguez Naranjo
 */
public class ApiErrorResponse 
{
    private final String apiEndPoint;
    private final String errorMessage;

    /**
     * Builds an {@link ApiErrorResponse}.
     * 
     * @param apiEndPoint The origin api endpoint that caused the error
     * @param errorMessage The error message
     */
    public ApiErrorResponse(String apiEndPoint, String errorMessage)
    {
        this.apiEndPoint = apiEndPoint;
        this.errorMessage = errorMessage;
    }

    public String getApiEndPoint()
    {
        return apiEndPoint;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.apiEndPoint);
        hash = 13 * hash + Objects.hashCode(this.errorMessage);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ApiErrorResponse other = (ApiErrorResponse) obj;
        if (!Objects.equals(this.apiEndPoint, other.apiEndPoint))
        {
            return false;
        }
        return Objects.equals(this.errorMessage, other.errorMessage);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ApiErrorResponse{");
        sb.append("apiEndPoint=").append(apiEndPoint);
        sb.append(", errorMessage=").append(errorMessage);
        sb.append('}');
        return sb.toString();
    }
}
