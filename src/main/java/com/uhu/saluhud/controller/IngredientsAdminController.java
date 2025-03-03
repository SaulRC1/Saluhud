package com.uhu.saluhud.controller;

import com.uhu.saluhuddatabaseutils.models.nutrition.Ingredient;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalAllergenicService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/ingredients")
public class IngredientsAdminController
{

    @Autowired
    private AdministrationPortalIngredientService ingredientService;

    @Autowired
    private AdministrationPortalAllergenicService allergenicService;

    @GetMapping("/home")
    public ModelAndView getIngredients(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size)
    {
        ModelAndView modelAndView = new ModelAndView("ingredients/ingredientsHome");
        Page<Ingredient> ingredientPage = ingredientService.getIngredients(page, size);
        modelAndView.addObject("ingredients", ingredientPage.getContent());
        modelAndView.addObject("currentPage", ingredientPage.getNumber());
        modelAndView.addObject("totalPages", ingredientPage.getTotalPages());
        return modelAndView;
    }

    // Mostrar formulario para crear un nuevo ingrediente
    @GetMapping("/create")
    public ModelAndView showCreateForm()
    {
        ModelAndView modelAndView = new ModelAndView("ingredients/createIngredient");
        modelAndView.addObject("ingredient", new Ingredient());
        modelAndView.addObject("allergenics", allergenicService.findAllAllergenics());
        return modelAndView;
    }

    // Guardar nuevo ingrediente
    @PostMapping("/create")
    public ModelAndView createIngredient(@ModelAttribute Ingredient ingredient)
    {
        ModelAndView modelAndView = new ModelAndView("ingredients/createIngredient");
        try {
            ingredientService.saveIngredient(ingredient);
            modelAndView.addObject("successMessage", "Ingrediente creado correctamente.");
        } catch (Exception e) {
            modelAndView.addObject("errorMessage", "Error al crear el ingrediente: " + e.getMessage());
        }

        return modelAndView;
    }

    // Mostrar formulario para editar ingrediente
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable long id)
    {
        ModelAndView modelAndView = new ModelAndView("ingredients/editIngredient");
        Ingredient ingredient = ingredientService.getIngredientById(id);
        modelAndView.addObject("ingredient", ingredient);
        modelAndView.addObject("allergenics", allergenicService.findAllAllergenics());
        return modelAndView;
    }

    // Guardar edición de ingrediente
    @PostMapping("/edit")
    public ModelAndView updateIngredient(@ModelAttribute Ingredient ingredient)
    {
        ModelAndView modelAndView = new ModelAndView("ingredients/editIngredient");
        try {
            ingredientService.updateIngredient(ingredient);
            modelAndView.addObject("successMessage", "Ingrediente actualizado correctamente.");
        } catch (Exception e) {
            modelAndView.addObject("errorMessage", "Error al actualizar el ingrediente: " + e.getMessage());
        }

        return modelAndView;
    }

    // Eliminar ingrediente
    @GetMapping("/delete/{id}")
    public ModelAndView deleteIngredient(@PathVariable long id, RedirectAttributes redirectAttributes)
    {
        ModelAndView modelAndView = new ModelAndView("redirect:/ingredients/home");
        try {
            Ingredient ingredient = ingredientService.getIngredientById(id);
            ingredientService.deleteIngredient(ingredient);
            redirectAttributes.addFlashAttribute("successMessage", "Ingrediente eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el ingrediente: " + e.getMessage());
        }

        return modelAndView;
    }

    // Ver detalles de un ingrediente
    @GetMapping("/details/{id}")
    public ModelAndView showIngredientDetails(@PathVariable long id)
    {
        ModelAndView modelAndView = new ModelAndView("ingredients/details");
        Ingredient ingredient = ingredientService.getIngredientById(id);
        modelAndView.addObject("ingredient", ingredient);
        return modelAndView;
    }
}
