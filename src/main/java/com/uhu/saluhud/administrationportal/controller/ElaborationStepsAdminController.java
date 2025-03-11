package com.uhu.saluhud.administrationportal.controller;

import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;
import com.uhu.saluhuddatabaseutils.models.nutrition.RecipeElaborationStep;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalRecipeElaborationStepService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalRecipeService;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
@RequestMapping("/elaborationSteps")
public class ElaborationStepsAdminController
{

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AdministrationPortalRecipeElaborationStepService elaborationStepService;

    @Autowired
    private AdministrationPortalRecipeService recipeService;

    @GetMapping("/home")
    public ModelAndView getElaborationSteps(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size)
    {
        ModelAndView modelAndView = new ModelAndView("elaborationSteps/elaborationStepsHome");
        Page<RecipeElaborationStep> elaborationStepPage = elaborationStepService.getElaborationSteps(page, size);
        modelAndView.addObject("elaborationSteps", elaborationStepPage.getContent());
        modelAndView.addObject("currentPage", elaborationStepPage.getNumber());
        modelAndView.addObject("totalPages", elaborationStepPage.getTotalPages());
        return modelAndView;
    }

    // Mostrar formulario para crear un nuevo paso
    @GetMapping("/create")
    public ModelAndView showCreateForm()
    {
        List<Recipe> recipes = recipeService.findAllRecipes(); // Obtén todas las recetas
        ModelAndView modelAndView = new ModelAndView("elaborationSteps/createElaborationStep");
        modelAndView.addObject("elaborationStep", new RecipeElaborationStep());
        modelAndView.addObject("recipes", recipes);
        return modelAndView;
    }

    // Guardar nuevo paso
    @PostMapping("/create")
    public ModelAndView createElaborationStep(@ModelAttribute("elaborationStep") 
            RecipeElaborationStep elaborationStep,
            BindingResult result, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("elaborationSteps/createElaborationStep");

        if (result.hasErrors()) {
            // Si hay errores, devolver el formulario con los errores
            List<Recipe> recipes = recipeService.findAllRecipes();
            modelAndView.addObject("recipes", recipes);
            return modelAndView;
        }

        try {
            elaborationStepService.saveRecipeElaborationStep(elaborationStep);
            String successMessage = messageSource.getMessage("elaborationStep.success.create", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (NoSuchMessageException e) {
            String errorMessage = messageSource.getMessage("elaborationStep.error.create", new Object[]{e.getMessage()}, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    // Mostrar formulario para editar paso
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable long id)
    {
        ModelAndView modelAndView = new ModelAndView("elaborationSteps/editElaborationStep");
        RecipeElaborationStep elaborationStep = elaborationStepService.getStepById(id);
        modelAndView.addObject("elaborationStep", elaborationStep);
        return modelAndView;
    }

    // Guardar edición de ingrediente
    @PostMapping("/edit")
    public ModelAndView updateElaborationStep(@ModelAttribute("elaborationStep") 
            RecipeElaborationStep elaborationStep,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("elaborationSteps/editElaborationStep");
        try {
            elaborationStepService.updateRecipeElaborationStep(elaborationStep);
            String successMessage = messageSource.getMessage("elaborationStep.success.edit", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (NoSuchMessageException e) {
            String errorMessage = messageSource.getMessage("elaborationStep.error.edit", new Object[]{e.getMessage()}, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    // Eliminar paso
    @GetMapping("/delete/{id}")
    public ModelAndView deleteElaborationStep(@PathVariable long id, RedirectAttributes redirectAttributes,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("redirect:/elaborationSteps/home");
        try {
            RecipeElaborationStep elaborationStep = elaborationStepService.getStepById(id);
            elaborationStepService.deleteRecipeElaborationStep(elaborationStep);
            String successMessage = messageSource.getMessage("elaborationStep.success.delete", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (NoSuchMessageException e) {
            String errorMessage = messageSource.getMessage("elaborationStep.error.delete", new Object[]{e.getMessage()}, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    // Ver detalles de un paso
    @GetMapping("/details/{id}")
    public ModelAndView showElaborationStepDetails(@PathVariable long id)
    {
        ModelAndView modelAndView = new ModelAndView("elaborationSteps/details");
        RecipeElaborationStep elaborationStep = elaborationStepService.getStepById(id);
        modelAndView.addObject("elaborationStep", elaborationStep);
        return modelAndView;
    }
}
