package com.uhu.saluhud.administrationportal.controller;

import com.uhu.saluhud.administrationportal.configuration.web.IngredientSearchDTO;
import com.uhu.saluhuddatabaseutils.models.nutrition.Ingredient;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalAllergenicService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalIngredientService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
            ingredientsPage = ingredientService.getIngredientByName(name, pageable);
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
            ingredientsPage = ingredientService.getIngredients(0, 20);
        }

        modelAndView.addObject("ingredientsPage", ingredientsPage.getContent());
        modelAndView.addObject("currentPage", ingredientsPage.getNumber());
        modelAndView.addObject("totalPages", ingredientsPage.getTotalPages());
        modelAndView.addObject("searchDTO", new IngredientSearchDTO(name, maxKilocalories, minProteinAmount, minCarbohydratesAmount, minFatAmount));

        return modelAndView;
    }
}
