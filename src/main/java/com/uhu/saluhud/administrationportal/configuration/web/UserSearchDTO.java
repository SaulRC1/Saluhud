package com.uhu.saluhud.administrationportal.configuration.web;

/**
 *
 * @author juald
 */
public class UserSearchDTO
{

    private String username;
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;

    public UserSearchDTO()
    {
    }

    public UserSearchDTO(String username, String name, String surname, String email, String phoneNumber)
    {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
}
