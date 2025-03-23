package com.uhu.saluhud.mobileapp.response;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class ApiInformationResponse 
{
    private final String apiEndPoint;
    private final String message;

    public ApiInformationResponse(String apiEndPoint, String message)
    {
        this.apiEndPoint = apiEndPoint;
        this.message = message;
    }

    public String getApiEndPoint()
    {
        return apiEndPoint;
    }

    public String getMessage()
    {
        return message;
    }
}
