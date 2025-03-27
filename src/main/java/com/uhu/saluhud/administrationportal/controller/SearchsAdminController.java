package com.uhu.saluhud.administrationportal.controller;

import com.uhu.saluhud.administrationportal.configuration.web.IngredientSearchDTO;
import com.uhu.saluhud.administrationportal.configuration.web.RecipeSearchDTO;
import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import com.uhu.saluhuddatabaseutils.models.nutrition.Ingredient;
import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalAllergenicService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalIngredientService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalRecipeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author juald
 */
@Controller
@RequestMapping("/search")
public class SearchsAdminController
{

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AdministrationPortalAllergenicService allergenicService;

    @Autowired
    private AdministrationPortalIngredientService ingredientService;

    @Autowired
    private AdministrationPortalRecipeService recipeService;

    @GetMapping("/advanced")
    public ModelAndView showAdvancedSearchPage()
    {
        ModelAndView modelAndView = new ModelAndView("search/searchAdvanced");
        return modelAndView;
    }

    // Método para buscar ingredientes
    @GetMapping("/ingredients")
    public ModelAndView searchIngredients(@RequestParam(required = false) String name,
            @RequestParam(required = false) Integer maxKilocalories,
            @RequestParam(required = false) Integer minProteinAmount,
            @RequestParam(required = false) Integer minCarbohydratesAmount,
            @RequestParam(required = false) Integer minFatAmount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size)
    {
        ModelAndView modelAndView = new ModelAndView("search/searchIngredients");
        Pageable pageable = PageRequest.of(page, size);
        Page<Ingredient> ingredientsPage;

        if (name != null && !name.isEmpty())
        {
            ingredientsPage = ingredientService.searchByName(name, page, size);
        }
        else if (maxKilocalories != null)
        {
            ingredientsPage = ingredientService.findByMaxKilocalories(maxKilocalories, pageable);
        }
        else if (minProteinAmount != null)
        {
            ingredientsPage = ingredientService.findByMinProteinAmount(minProteinAmount, pageable);
        }
        else if (minCarbohydratesAmount != null)
        {
            ingredientsPage = ingredientService.findByMinCarbohydratesAmount(minCarbohydratesAmount, pageable);
        }
        else if (minFatAmount != null)
        {
            ingredientsPage = ingredientService.findByMinFatAmount(minFatAmount, pageable);
        }
        else
        {
            ingredientsPage = ingredientService.getIngredients(page, size);
        }

        modelAndView.addObject("ingredientsPage", ingredientsPage.getContent());
        modelAndView.addObject("currentPage", ingredientsPage.getNumber());
        modelAndView.addObject("totalPages", ingredientsPage.getTotalPages());
        modelAndView.addObject("searchDTO", new IngredientSearchDTO(name, maxKilocalories, minProteinAmount, minCarbohydratesAmount, minFatAmount));

        return modelAndView;
    }

    @GetMapping("/recipes")
    public ModelAndView searchRecipes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer maxKilocalories,
            @RequestParam(required = false) Long ingredientId,
            @RequestParam(required = false) Long allergenicId,
            @RequestParam(required = false) Long allergenicExclusionId,
            @RequestParam(defaultValue = "0") int ingredientPage,
            @RequestParam(defaultValue = "20") int ingredientSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        ModelAndView modelAndView = new ModelAndView("search/searchRecipes");
        Page<Ingredient> ingredientsPage = ingredientService.getIngredients(ingredientPage, ingredientSize);
        Page<Recipe> recipes;

        // Cargar todos los alérgenos sin paginar
        List<Allergenic> allergenics = allergenicService.findAllAllergenics();

        if (name != null && !name.isEmpty())
        {
            recipes = recipeService.searchByName(name, page, size);
        }
        else if (maxKilocalories != null)
        {
            recipes = recipeService.searchByMaxKilocalories(maxKilocalories, page, size);
        }
        else if (ingredientId != null)
        {
            recipes = recipeService.searchByIngredient(ingredientId, page, size);
        }
        else if (allergenicId != null)
        {
            recipes = recipeService.searchByAllergenic(allergenicId, page, size);
        }
        else if (allergenicExclusionId != null)
        {
            recipes = recipeService.searchByAllergenicExclusion(allergenicExclusionId, page, size);
        }
        else
        {
            recipes = recipeService.getRecipes(page, size); 
        }

        modelAndView.addObject("recipesPage", recipes);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", recipes.getTotalPages());
        
        modelAndView.addObject("ingredientsPage", ingredientsPage.getContent());
        modelAndView.addObject("ingredientCurrentPage", ingredientsPage.getNumber());
        modelAndView.addObject("ingredientTotalPages", ingredientsPage.getTotalPages());
        
        modelAndView.addObject("allergenics", allergenics);
        modelAndView.addObject("searchDTO", new RecipeSearchDTO(name, maxKilocalories,
                ingredientId, allergenicId, allergenicExclusionId));

        return modelAndView;
    }
}
