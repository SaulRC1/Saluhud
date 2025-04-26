package com.uhu.saluhud.mobileapp.response;

/**
 * Error response intended to be sent when the user is unauthorized to access an 
 * API endpoint via JWT or another authentication system.
 * 
 * <p>
 * This API error must be sent alongside HTTP status codes 401 (unauthorized) or
 * 403 (forbidden)
 * </p>
 * 
 * @author Saúl Rodríguez Naranjo
 */
public class ApiUnauthorizedErrorResponse 
{
    private final String apiEndPoint;
    private final String unauthorizedErrorMessage;

    public ApiUnauthorizedErrorResponse(String apiEndPoint, String unauthorizedErrorMessage)
    {
        this.apiEndPoint = apiEndPoint;
        this.unauthorizedErrorMessage = unauthorizedErrorMessage;
    }

    public String getApiEndPoint()
    {
        return apiEndPoint;
    }

    public String getUnauthorizedErrorMessage()
    {
        return unauthorizedErrorMessage;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ApiUnauthorizedErrorResponse{");
        sb.append("apiEndPoint=").append(apiEndPoint);
        sb.append(", unauthorizedErrorMessage=").append(unauthorizedErrorMessage);
        sb.append('}');
        return sb.toString();
    }
}
