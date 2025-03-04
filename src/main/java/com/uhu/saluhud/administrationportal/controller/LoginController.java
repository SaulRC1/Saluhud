package com.uhu.saluhud.administrationportal.controller;

import com.uhu.saluhuddatabaseutils.models.security.Credentials;
import com.uhu.saluhuddatabaseutils.services.administrationportal.security.AdministrationPortalLoginService;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author juald
 *
 */
@Controller
@RequestMapping("/security")
public class LoginController
{

    @Autowired
    private AdministrationPortalLoginService loginService;

    @Autowired
    private MessageSource messageSource;

    /**
     *
     */
    public LoginController()
    {

    }

    @GetMapping("/login")
    public ModelAndView loginPage(@RequestParam(value = "error", required = false) String error, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("security/login"); // Especificamos la vista

        if (error != null) {
            String errorMessage = messageSource.getMessage("login.error", null, locale);
            modelAndView.addObject("errorMsg", errorMessage);
        }

        modelAndView.addObject("credentials", new Credentials()); // Agregar objeto vacío para el formulario

        return modelAndView;
    }

}
