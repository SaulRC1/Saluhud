package com.uhu.saluhud.mobileapp.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class SaluhudUserRegistrationDTO 
{
    @NotBlank
    @Pattern(regexp = "^\\w+$")
    @Size(min = 2, max = 32)
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*\\-_]).{8,32}$")
    @Size(min = 8, max = 32)
    private String rawPassword;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    @Size(min = 6, max = 200) //Minimum possible size is of 6 characters taking
    //into account the regular expression  
    private String email;

    @Size(min = 1, max = 200)
    @Pattern(regexp = "^[a-zA-Zà-üÀ-Ü\\s]+$")
    @NotBlank
    private String name;

    @Size(max = 200)
    @Pattern(regexp = "^[a-zA-Zà-üÀ-Ü\\s]*$")
    private String surname;

    //This pattern accepts phone number having the following format: +34 xxxx or
    //+1-939 xxxx (being x any possible number)
    @Pattern(regexp = "^(\\+(\\d{1,3}|\\d{1,3}-\\d{1,3})\\s\\d{4,32})?$")
    private String phoneNumber;

    public SaluhudUserRegistrationDTO()
    {
    }
    
    public SaluhudUserRegistrationDTO(String username, String rawPassword, String email, String name, String surname, String phoneNumber)
    {
        this.username = username;
        this.rawPassword = rawPassword;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername()
    {
        return username;
    }

    public String getRawPassword()
    {
        return rawPassword;
    }

    public String getEmail()
    {
        return email;
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setRawPassword(String rawPassword)
    {
        this.rawPassword = rawPassword;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
}
