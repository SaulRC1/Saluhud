package com.uhu.saluhud.administrationPortal.controller;

import com.uhu.saluhuddatabaseutils.models.security.UserAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author juald
 */
@Controller
@RequestMapping("/")
public class PrincipalAdminController
{

    @GetMapping("/welcome")
    public String getWelcome(Model model, @AuthenticationPrincipal UserAccount userAccount)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            System.out.println("No hay usuario autenticado");
        } else {
            System.out.println("Usuario autenticado: " + auth.getPrincipal());
            System.out.println("Roles: " + auth.getAuthorities());
        }
        return "mainPage";
    }
}
