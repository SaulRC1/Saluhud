package com.uhu.saluhud.administrationportal.controller;

import com.uhu.saluhud.administrationportal.configuration.web.IngredientSearchDTO;
import com.uhu.saluhud.administrationportal.configuration.web.RecipeSearchDTO;
import com.uhu.saluhud.administrationportal.configuration.web.UserSearchDTO;
import com.uhu.saluhud.localization.NutritionLocaleProvider;
import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import com.uhu.saluhuddatabaseutils.models.nutrition.Ingredient;
import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;
import com.uhu.saluhuddatabaseutils.models.user.SaluhudUser;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalAllergenicService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalIngredientService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalRecipeService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.user.AdministrationPortalSaluhudUserService;
import java.util.List;
import java.util.Locale;
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

    @Autowired
    private AdministrationPortalSaluhudUserService userService;

    @Autowired
    private NutritionLocaleProvider nutritionLocaleProvider;

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
            @RequestParam(defaultValue = "20") int size, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("search/searchIngredients");
        Pageable pageable = PageRequest.of(page, size);
        Page<Ingredient> ingredientsPage;

        if (name != null && !name.isEmpty())
        {
            ingredientsPage = ingredientService.searchByStartName(name, page, size);
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

        // Traducir nombres de ingredientes antes de mostrarlos
        List<Ingredient> translatedIngredients = ingredientsPage.getContent().stream()
                .map(ingredient ->
                {
                    String translatedName = nutritionLocaleProvider.getTranslation(
                            ingredient.getName(),
                            NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX,
                            locale
                    );
                    ingredient.setName(translatedName);
                    return ingredient;
                })
                .toList();

        modelAndView.addObject("ingredientsPage", translatedIngredients);
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
            @RequestParam(defaultValue = "10") int size, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("search/searchRecipes");

        // Ingredientes
        Page<Ingredient> ingredientsPage = ingredientService.getIngredients(ingredientPage, ingredientSize);
        List<Ingredient> translatedIngredients = ingredientsPage.getContent().stream()
                .map(ingredient ->
                {
                    String translatedName = nutritionLocaleProvider.getTranslation(
                            ingredient.getName(),
                            NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX,
                            locale
                    );
                    ingredient.setName(translatedName);
                    return ingredient;
                })
                .toList();

        // Alérgenos
        List<Allergenic> allergenics = allergenicService.findAllAllergenics();
        List<Allergenic> translatedAllergenics = allergenics.stream()
                .map(allergenic ->
                {
                    String translatedName = nutritionLocaleProvider.getTranslation(
                            allergenic.getName(),
                            NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX,
                            locale
                    );
                    allergenic.setName(translatedName);
                    return allergenic;
                })
                .toList();

        Page<Recipe> recipes;

        // Cargar todos los alérgenos sin paginar
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

        List<Recipe> translatedRecipes = recipes.getContent().stream().peek(recipe ->
        {
            String translatedName = nutritionLocaleProvider.getTranslation(
                    recipe.getName(),
                    NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX,
                    locale
            );
            String translatedDescription = nutritionLocaleProvider.getTranslation(
                    recipe.getDescription(),
                    NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX,
                    locale
            );
            String translatedIngredientsDescription = nutritionLocaleProvider.getTranslation(
                    recipe.getIngredientsDescription(),
                    NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX,
                    locale
            );

            recipe.setName(translatedName);
            recipe.setDescription(translatedDescription);
            recipe.setIngredientsDescription(translatedIngredientsDescription);
        }).toList();

        modelAndView.addObject("recipesPage", translatedRecipes);
        modelAndView.addObject("currentPage", recipes.getNumber());
        modelAndView.addObject("totalPages", recipes.getTotalPages());

        modelAndView.addObject("ingredientsPage", translatedIngredients);
        modelAndView.addObject("ingredientCurrentPage", ingredientsPage.getNumber());
        modelAndView.addObject("ingredientTotalPages", ingredientsPage.getTotalPages());

        modelAndView.addObject("allergenics", translatedAllergenics);
        modelAndView.addObject("searchDTO", new RecipeSearchDTO(name, maxKilocalories,
                ingredientId, allergenicId, allergenicExclusionId));

        return modelAndView;
    }

    @GetMapping("/users")
    public ModelAndView searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {

        ModelAndView modelAndView = new ModelAndView("search/searchUsers");
        Page<SaluhudUser> users;

        if (username != null && !username.isEmpty())
        {
            users = userService.searchByUsername(username, page, size);
        }
        else if (name != null && !name.isEmpty())
        {
            users = userService.searchByName(name, page, size);
        }
        else if (surname != null && !surname.isEmpty())
        {
            users = userService.searchBySurname(surname, page, size);
        }
        else if (email != null && !email.isEmpty())
        {
            users = userService.searchByEmail(email, page, size);
        }
        else if (phoneNumber != null && !phoneNumber.isEmpty())
        {
            users = userService.searchByPhoneNumber(phoneNumber, page, size);
        }
        else
        {
            users = userService.getUsers(page, size);
        }

        modelAndView.addObject("usersPage", users);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", users.getTotalPages());
        modelAndView.addObject("searchDTO", new UserSearchDTO(username, name, surname, email, phoneNumber));

        return modelAndView;
    }
}
