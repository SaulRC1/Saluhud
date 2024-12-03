package com.uhu.saluhud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author juald
 */
@Controller
public class PrincipalAdminController
{
    @GetMapping("/home")
    public String getWelcome(Model model) {
        model.addAttribute("message", "Hello World!");
        
        return "mainPage";
    }
}
