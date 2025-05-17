package com.uhu.saluhud.mobileapp.dto.user;

import java.util.Objects;
import com.uhu.saluhuddatabaseutils.models.user.SaluhudUser;

/**
 * Data Transfer Object (DTO) for showing {@link SaluhudUser} information.
 * 
 * @author Saúl Rodríguez Naranjo
 */
public class SaluhudUserDTO 
{
    private String username;
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;

    public SaluhudUserDTO()
    {
    }

    public SaluhudUserDTO(String username, String email, String name, String surname, String phoneNumber)
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

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.username);
        hash = 59 * hash + Objects.hashCode(this.email);
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + Objects.hashCode(this.surname);
        hash = 59 * hash + Objects.hashCode(this.phoneNumber);
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
        final SaluhudUserDTO other = (SaluhudUserDTO) obj;
        if (!Objects.equals(this.username, other.username))
        {
            return false;
        }
        if (!Objects.equals(this.email, other.email))
        {
            return false;
        }
        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }
        if (!Objects.equals(this.surname, other.surname))
        {
            return false;
        }
        return Objects.equals(this.phoneNumber, other.phoneNumber);
    }
}
