package com.uhu.saluhud.controller;

import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import com.uhu.saluhuddatabaseutils.models.nutrition.Ingredient;
import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;
import com.uhu.saluhuddatabaseutils.models.nutrition.RecipeIngredient;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalAllergenicService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalIngredientService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalRecipeService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author juald
 */
@Controller
@RequestMapping("/recipes")
public class RecipesAdminController
{

    @Autowired
    private AdministrationPortalRecipeService recipeService;

    @Autowired
    private AdministrationPortalIngredientService ingredientService;

    @Autowired
    private AdministrationPortalAllergenicService allergenicService;

    @GetMapping("/ingredients/search")
    @ResponseBody
    public List<IngredientDto> searchIngredients(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Page<Ingredient> ingredientsPage = ingredientService.getIngredients(page, size);

        return ingredientsPage.getContent().stream()
                .map(ingredient -> new IngredientDto(ingredient.getId(), ingredient.getName()))
                .collect(Collectors.toList());
    }

    public record IngredientDto(Long id, String name)
            {

    }

    @GetMapping("/home")
    public ModelAndView getRecipes(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        ModelAndView modelAndView = new ModelAndView("recipes/recipesHome");
        Page<Recipe> recipePage = recipeService.getRecipes(page, size);
        modelAndView.addObject("recipes", recipePage.getContent());
        modelAndView.addObject("currentPage", recipePage.getNumber());
        modelAndView.addObject("totalPages", recipePage.getTotalPages());
        return modelAndView;
    }

    // Mostrar formulario para crear una nueva receta
    @GetMapping("/create")
    public ModelAndView showCreateForm()
    {
        Recipe recipe = new Recipe();
        recipe.setRecipeIngredients(new ArrayList<>());

        ModelAndView modelAndView = new ModelAndView("recipes/createRecipe");
        modelAndView.addObject("recipe", recipe);
        modelAndView.addObject("allergenics", allergenicService.findAllAllergenics());
        return modelAndView;
    }

    // Guardar nueva receta
    @PostMapping("/create")
    public ModelAndView createRecipe(@ModelAttribute Recipe recipe)
    {
        ModelAndView modelAndView = new ModelAndView("recipes/createRecipe");
        try {

            if (recipe.getRecipeIngredients() != null) {
                // Filtra y conserva solo los RecipeIngredient que tengan ingredientId (es decir, que se haya seleccionado un ingrediente)
                List<RecipeIngredient> validIngredients = recipe.getRecipeIngredients().stream()
                        .filter(ri -> ri.getIngredientId() != null)
                        .collect(Collectors.toList());
                if (validIngredients.isEmpty()) {
                    throw new RuntimeException("Debe seleccionar al menos un ingrediente.");
                }
                // Asocia la receta y completa el objeto Ingredient en cada RecipeIngredient
                for (RecipeIngredient ri : validIngredients) {
                    Ingredient fullIngredient = ingredientService.getIngredientById(ri.getIngredientId());
                    if (fullIngredient == null) {
                        throw new RuntimeException("Ingrediente no encontrado para id: " + ri.getIngredientId());
                    }
                    ri.setIngredient(fullIngredient);
                    ri.setRecipe(recipe);
                }
                // Reemplaza la lista original por la lista filtrada
                recipe.setRecipeIngredients(validIngredients);
            }

            recipeService.saveRecipe(recipe);
            modelAndView.addObject("successMessage", "Receta creada correctamente.");
        } catch (RuntimeException e) {
            modelAndView.addObject("errorMessage", "Error al crear la receta: " + e.getMessage());
        }

        return modelAndView;
    }

    // Mostrar formulario para editar receta
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable long id)
    {
        ModelAndView modelAndView = new ModelAndView("recipes/editRecipe");
        Recipe recipe = recipeService.getRecipeById(id);
        List<Allergenic> allAllergens = allergenicService.findAllAllergenics();
        modelAndView.addObject("allergens", allAllergens);
        modelAndView.addObject("recipe", recipe);
        return modelAndView;
    }

    // Guardar edición de receta
    @PostMapping("/edit")
    public ModelAndView updateRecipe(@ModelAttribute Recipe recipe)
    {
        System.out.println("Recibida solicitud de actualización para ID: " + recipe.getId());
        ModelAndView modelAndView = new ModelAndView("recipes/editRecipe");
        try {
            recipeService.updateRecipe(recipe);
            modelAndView.addObject("successMessage", "Receta actualizada correctamente.");
        } catch (Exception e) {
            modelAndView.addObject("errorMessage", "Error al actualizar la receta: " + e.getMessage());
        }

        return modelAndView;
    }

    // Eliminar receta
    @GetMapping("/delete/{id}")
    public ModelAndView deleteRecipe(@PathVariable long id, RedirectAttributes redirectAttributes)
    {
        ModelAndView modelAndView = new ModelAndView("redirect:/recipes/home");
        try {
            Recipe recipe = recipeService.getRecipeById(id);
            recipeService.deleteRecipe(recipe);
            redirectAttributes.addFlashAttribute("successMessage", "Receta eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la receta: " + e.getMessage());
        }

        return modelAndView;
    }

    @GetMapping("/details/{id}")
    public ModelAndView showRecipeDetails(@PathVariable long id)
    {
        ModelAndView modelAndView = new ModelAndView("recipes/details");
        Recipe recipe = recipeService.getRecipeById(id);
        modelAndView.addObject("recipe", recipe);
        modelAndView.addObject("recipeIngredients", recipe.getRecipeIngredients());
        return modelAndView;
    }
}
