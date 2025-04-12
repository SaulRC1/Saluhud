package com.uhu.saluhud.mobileapp.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class SaluhudUserAuthenticationDTO 
{
    @NotBlank
    @Pattern(regexp = "^\\w+$")
    @Size(min = 2, max = 32)
    private String username;
    
    @NotBlank
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*\\-_]).{8,32}$")
    @Size(min = 8, max = 32)
    private String rawPassword;

    public SaluhudUserAuthenticationDTO(String username, String rawPassword)
    {
        this.username = username;
        this.rawPassword = rawPassword;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getRawPassword()
    {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword)
    {
        this.rawPassword = rawPassword;
    }
}
