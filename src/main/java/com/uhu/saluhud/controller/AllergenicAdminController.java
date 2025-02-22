package com.uhu.saluhud.controller;

import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalAllergenicService;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author juald
 */
@Controller
@RequestMapping("/allergenic")
public class AllergenicAdminController
{

    @Autowired
    private AdministrationPortalAllergenicService allergenicService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/home")
    public ModelAndView getAlergens(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size)
    {
        ModelAndView modelAndView = new ModelAndView("allergenic/allergenicHome");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Page<Allergenic> alergenPage = allergenicService.getAlergens(page, size);
        modelAndView.addObject("allergenics", alergenPage.getContent());
        modelAndView.addObject("currentPage", alergenPage.getNumber());
        modelAndView.addObject("totalPages", alergenPage.getTotalPages());
        return modelAndView;
    }

    // Mostrar formulario para editar alergeno
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable long id)
    {
        ModelAndView modelAndView = new ModelAndView("allergenic/editAlergen");
        Allergenic allergenic = allergenicService.findById(id);
        modelAndView.addObject("allergenic", allergenic);
        return modelAndView;
    }

    // Guardar edición de alergeno
    @PostMapping("/edit")
    public ModelAndView updateAllergenic(@ModelAttribute Allergenic allergenic, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("allergenic/editAlergen");
        try {
            allergenicService.updateAllergenic(allergenic);
            String successMessage = messageSource.getMessage("allergen.updated.success", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (NoSuchMessageException e) {
            String errorMessage = messageSource.getMessage("allergen.updated.error", new Object[]{e.getMessage()}, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    // Eliminar alergeno
    @GetMapping("/delete/{id}")
    public ModelAndView deleteAlergen(@PathVariable long id, RedirectAttributes redirectAttributes)
    {
        ModelAndView modelAndView = new ModelAndView("redirect:/allergenic/home");
        try {
            Allergenic alergen = allergenicService.findById(id);
            allergenicService.deleteAllergenic(alergen);
            redirectAttributes.addFlashAttribute("successMessage", "Alérgeno eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el alérgeno: " + e.getMessage());
        }

        return modelAndView;
    }

    @GetMapping("/details/{id}")
    public ModelAndView showAllergenicDetails(@PathVariable long id)
    {
        ModelAndView modelAndView = new ModelAndView("allergenic/details");
        Allergenic allergenic = allergenicService.findById(id);
        modelAndView.addObject("allergenic", allergenic);
        return modelAndView;
    }

}
