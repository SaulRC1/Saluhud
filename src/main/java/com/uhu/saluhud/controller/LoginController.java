package com.uhu.saluhud.controller;

import com.uhu.saluhud.database.utils.models.security.Credentials;
import com.uhu.saluhud.database.utils.services.saluhud.admin.security.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    LoginService loginService;

    /**
     *
     */
    public LoginController()
    {

    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value="error", required=false) String error,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Usuario o contraseña incorrectos.");
        }
        model.addAttribute("credentials", new Credentials());
        return "security/login"; 
    }

}
