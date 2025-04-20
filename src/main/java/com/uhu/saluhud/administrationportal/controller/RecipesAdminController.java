package com.uhu.saluhud.administrationportal.controller;

import com.uhu.saluhud.administrationportal.dto.nutrition.AllergenicDTO;
import com.uhu.saluhud.administrationportal.dto.nutrition.IngredientDTO;
import com.uhu.saluhud.administrationportal.dto.nutrition.RecipeDTO;
import com.uhu.saluhud.administrationportal.dto.nutrition.RecipeElaborationStepDTO;
import com.uhu.saluhud.administrationportal.dto.nutrition.RecipeIngredientDTO;
import com.uhu.saluhud.administrationportal.service.localization.NutritionLocaleService;
import com.uhu.saluhud.localization.NutritionLocaleProvider;
import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import com.uhu.saluhuddatabaseutils.models.nutrition.Ingredient;
import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;
import com.uhu.saluhuddatabaseutils.models.nutrition.RecipeIngredient;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalAllergenicService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalIngredientService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalRecipeService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
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

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private NutritionLocaleProvider nutritionLocaleProvider;

    @Autowired
    private NutritionLocaleService nutritionLocaleService;

    @GetMapping("/ingredients/search")
    @ResponseBody
    public List<IngredientDTO> searchIngredients(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, Locale locale)
    {
        return ingredientService.getIngredients(page, size).stream()
                .map(ingredient -> toIngredientDTO(ingredient, locale))
                .collect(Collectors.toList());
    }

    @GetMapping("/home")
    public ModelAndView getRecipes(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("recipes/recipesHome");
        Page<Recipe> recipePage = recipeService.getRecipes(page, size);

        List<RecipeDTO> recipeDTOs = recipePage.stream()
                .map(recipe -> toRecipeDTO(recipe, locale))
                .collect(Collectors.toList());

        modelAndView.addObject("recipes", recipeDTOs);
        modelAndView.addObject("currentPage", recipePage.getNumber());
        modelAndView.addObject("totalPages", recipePage.getTotalPages());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm(Locale locale)
    {
        Recipe recipe = new Recipe();
        recipe.setRecipeIngredients(new ArrayList<>());

        ModelAndView modelAndView = new ModelAndView("recipes/createRecipe");
        modelAndView.addObject("recipe", recipe);

        // Obtener y traducir alérgenos
        List<Allergenic> allergenics = allergenicService.findAllAllergenics();
        Map<Long, String> translatedAllergenics = allergenics.stream()
                .collect(Collectors.toMap(
                        Allergenic::getId,
                        allergenic -> nutritionLocaleProvider.getTranslation(
                                "allergenic." + allergenic.getId(),
                                NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX,
                                locale)
                ));
        modelAndView.addObject("translatedAllergenics", translatedAllergenics);

        // Obtener y traducir ingredientes
        List<Ingredient> ingredients = ingredientService.findAllIngredients();
        Map<Long, String> translatedIngredients = ingredients.stream()
                .collect(Collectors.toMap(
                        Ingredient::getId,
                        ingredient -> nutritionLocaleProvider.getTranslation(
                                "ingredient." + ingredient.getId(),
                                NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX,
                                locale)
                ));
        modelAndView.addObject("translatedIngredients", translatedIngredients);

        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createRecipe(@ModelAttribute Recipe recipe, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("recipes/createRecipe");
        try
        {
            if (recipe.getRecipeIngredients() != null)
            {
                List<RecipeIngredient> validIngredients = recipe.getRecipeIngredients().stream()
                        .filter(ri -> ri.getIngredientId() != null)
                        .collect(Collectors.toList());
                if (validIngredients.isEmpty())
                {
                    throw new RuntimeException("Debe seleccionar al menos un ingrediente.");
                }

                for (RecipeIngredient ri : validIngredients)
                {
                    Ingredient fullIngredient = ingredientService.getIngredientById(ri.getIngredientId());
                    if (fullIngredient == null)
                    {
                        throw new RuntimeException("Ingrediente no encontrado para id: " + ri.getIngredientId());
                    }
                    ri.setIngredient(fullIngredient);
                    ri.setRecipe(recipe);

                    String safeUnitKey = "recipeIngredient.unit." + ri.getId().getIdIngredient() + "." + ri.getUnit();
                    for (Locale langLocale : List.of(Locale.forLanguageTag("es"), Locale.forLanguageTag("en")))
                    {
                        nutritionLocaleService.addKeyToTranslationBundle(safeUnitKey, ri.getUnit(), langLocale,
                                NutritionLocaleProvider.RECIPE_INGREDIENT_TRANSLATION_BUNDLE_PREFIX);
                    }

                    String path = nutritionLocaleProvider.getTranslationsRootFolder() + File.separator
                            + NutritionLocaleProvider.RECIPE_INGREDIENT_TRANSLATION_BUNDLE_PREFIX + "_" + locale.toLanguageTag() + ".properties";

                    try (InputStreamReader reader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))
                    {
                        Properties translations = new Properties();
                        translations.load(reader);
                        String unitKey = translations.entrySet().stream()
                                .filter(entry -> entry.getValue().equals(ri.getUnit()))
                                .map(entry -> (String) entry.getKey())
                                .filter(k -> k.startsWith("recipeIngredient.unit."))
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException("No se encontró la clave para la unidad: " + ri.getUnit()));
                        ri.setUnit(unitKey);
                    }
                }

                recipe.setRecipeIngredients(validIngredients);
            }

            String baseKey = "recipe";
            for (Locale langLocale : List.of(Locale.forLanguageTag("es"), Locale.forLanguageTag("en")))
            {
                nutritionLocaleService.addKeyToTranslationBundle(baseKey + ".name." + recipe.getName(),
                        recipe.getName(), langLocale, NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX);
                nutritionLocaleService.addKeyToTranslationBundle(baseKey + ".description." + recipe.getName(),
                        recipe.getDescription(), langLocale, NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX);
                nutritionLocaleService.addKeyToTranslationBundle(baseKey + ".ingredientsDescription." + recipe.getName(),
                        recipe.getIngredientsDescription(), langLocale, NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX);
            }

            String path = nutritionLocaleProvider.getTranslationsRootFolder() + File.separator
                    + NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX + "_" + locale.toLanguageTag() + ".properties";

            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path),
                    StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(inputStreamReader))
            {
                Properties translations = new Properties();
                translations.load(reader);

                String generatedKey = translations.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(recipe.getName()))
                        .map(entry -> (String) entry.getKey())
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No se encontró la clave para el valor del nombre de la receta"));

                String descKey = translations.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(recipe.getDescription()))
                        .map(entry -> (String) entry.getKey())
                        .findFirst()
                        .orElse(recipe.getDescription());

                String ingDescKey = translations.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(recipe.getIngredientsDescription()))
                        .map(entry -> (String) entry.getKey())
                        .findFirst()
                        .orElse(recipe.getIngredientsDescription());

                recipe.setName(generatedKey);
                recipe.setDescription(descKey);
                recipe.setIngredientsDescription(ingDescKey);
            }

            recipeService.saveRecipe(recipe);

            String successMessage = messageSource.getMessage("recipe.created.success", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (RuntimeException e)
        {
            String errorMessage = messageSource.getMessage("recipe.created.error", new Object[]
            {
                e.getMessage()
            }, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        } catch (IOException ex)
        {
            Logger.getLogger(RecipesAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return modelAndView;
    }

    @GetMapping("/details/{id}")
    public ModelAndView showRecipeDetails(@PathVariable long id, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("recipes/details");

        Recipe recipe = recipeService.getRecipeById(id);
        RecipeDTO recipeDTO = toRecipeDTO(recipe, locale);

        modelAndView.addObject("recipe", recipeDTO);
        modelAndView.addObject("recipeIngredients", recipeDTO.getRecipeIngredientsDTO());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable long id, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("recipes/editRecipe");

        Recipe recipe = recipeService.getRecipeById(id);
        translateRecipeFields(recipe, locale);

        Map<Long, String> translatedAllergenics = getTranslatedAllergenics(locale);

        modelAndView.addObject("translatedAllergenics", translatedAllergenics);
        modelAndView.addObject("recipe", recipe);
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView updateRecipe(@ModelAttribute("recipe") Recipe recipe, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("recipes/editRecipe");

        try
        {
            enrichRecipeIngredients(recipe, locale, false);
            enrichAllergenics(recipe);
            storeTranslatableFields(recipe);
            readTranslationKeys(recipe, "es");

            recipeService.updateRecipe(recipe);

            String successMessage = messageSource.getMessage("recipe.updated.success", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (IOException | RuntimeException e)
        {
            String errorMessage = messageSource.getMessage("recipe.updated.error", new Object[]
            {
                e.getMessage()
            }, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteRecipe(@PathVariable long id, RedirectAttributes redirectAttributes, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("redirect:/recipes/home");

        try
        {
            Recipe recipe = recipeService.getRecipeById(id);

            deleteRecipeTranslations(recipe);

            recipeService.deleteRecipe(recipe);

            String successMessage = messageSource.getMessage("recipe.deleted.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (IOException | IllegalArgumentException | NoSuchMessageException e)
        {
            String errorMessage = messageSource.getMessage("recipe.deleted.error", new Object[]
            {
                e.getMessage()
            }, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            Logger.getLogger(RecipesAdminController.class.getName()).log(Level.SEVERE, "Error eliminando receta", e);
        }

        return modelAndView;
    }

    private void translateRecipeFields(Recipe recipe, Locale locale)
    {
        recipe.setName(nutritionLocaleProvider.getTranslation(
                recipe.getName(), NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, locale));
        recipe.setDescription(nutritionLocaleProvider.getTranslation(
                recipe.getDescription(), NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, locale));
        recipe.setIngredientsDescription(nutritionLocaleProvider.getTranslation(
                recipe.getIngredientsDescription(), NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, locale));
    }

    private Map<Long, String> getTranslatedAllergenics(Locale locale)
    {
        List<Allergenic> allergenics = allergenicService.findAllAllergenics();
        return allergenics.stream()
                .collect(Collectors.toMap(
                        Allergenic::getId,
                        allergenic -> nutritionLocaleProvider.getTranslation(
                                "allergenic." + allergenic.getId(),
                                NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX,
                                locale)
                ));
    }

    private void enrichRecipeIngredients(Recipe recipe, Locale locale, 
            boolean translateUnitKeyFromFile) throws IOException
    {
        List<RecipeIngredient> validIngredients = recipe.getRecipeIngredients().stream()
                .filter(ri -> ri.getIngredientId() != null)
                .collect(Collectors.toList());

        for (RecipeIngredient ri : validIngredients)
        {
            Ingredient ingredient = ingredientService.getIngredientById(ri.getIngredientId());
            if (ingredient == null)
            {
                throw new RuntimeException("Ingrediente no encontrado para id: " + ri.getIngredientId());
            }

            ri.setIngredient(ingredient);
            ri.setRecipe(recipe);

            String safeUnitKey = "recipeIngredient.unit." + ri.getId().getIdIngredient() + "." + ri.getUnit();
            for (Locale lang : List.of(Locale.forLanguageTag("es"), Locale.forLanguageTag("en")))
            {
                nutritionLocaleService.addKeyToTranslationBundle(safeUnitKey, ri.getUnit(), lang,
                        NutritionLocaleProvider.RECIPE_INGREDIENT_TRANSLATION_BUNDLE_PREFIX);
            }

            if (translateUnitKeyFromFile)
            {
                String path = nutritionLocaleProvider.getTranslationsRootFolder() + File.separator
                        + NutritionLocaleProvider.RECIPE_INGREDIENT_TRANSLATION_BUNDLE_PREFIX + "_" + locale.toLanguageTag() + ".properties";

                try (InputStreamReader reader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))
                {
                    Properties translations = new Properties();
                    translations.load(reader);
                    String unitKey = translations.entrySet().stream()
                            .filter(entry -> entry.getValue().equals(ri.getUnit()))
                            .map(entry -> (String) entry.getKey())
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("No se encontró la clave para la unidad: " + ri.getUnit()));
                    ri.setUnit(unitKey);
                }
            }
            else
            {
                ri.setUnit(safeUnitKey);
            }
        }

        recipe.setRecipeIngredients(validIngredients);
    }

    private void enrichAllergenics(Recipe recipe)
    {
        if (recipe.getAllergenics() != null && !recipe.getAllergenics().isEmpty())
        {
            Set<Allergenic> fullAllergenics = recipe.getAllergenics().stream()
                    .map(allergen -> allergenicService.findById(allergen.getId()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            recipe.setAllergenics(fullAllergenics);
        }
    }

    private void storeTranslatableFields(Recipe recipe)
    {
        String baseKey = "recipe";
        String nameKey = baseKey + ".name." + recipe.getName();
        String descriptionKey = baseKey + ".description." + recipe.getName();
        String ingredientsDescKey = baseKey + ".ingredientsDescription." + recipe.getName();

        List.of(Locale.forLanguageTag("es"), Locale.forLanguageTag("en")).forEach(lang ->
        {
            try
            {
                nutritionLocaleService.addKeyToTranslationBundle(nameKey, recipe.getName(), lang,
                        NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX);
                nutritionLocaleService.addKeyToTranslationBundle(descriptionKey, recipe.getDescription(), lang,
                        NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX);
                nutritionLocaleService.addKeyToTranslationBundle(ingredientsDescKey, recipe.getIngredientsDescription(), lang,
                        NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX);
            } catch (IOException ex)
            {
                Logger.getLogger(RecipesAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void readTranslationKeys(Recipe recipe, String languageTag) throws IOException
    {
        String bundlePath = nutritionLocaleProvider.getTranslationsRootFolder() + File.separator
                + NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX + "_" + languageTag + ".properties";

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(bundlePath), 
                StandardCharsets.UTF_8); BufferedReader bufferedReader = new BufferedReader(reader))
        {

            Properties translations = new Properties();
            translations.load(bufferedReader);

            recipe.setName(findTranslationKey(recipe.getName(), translations, "nombre de la receta"));
            recipe.setDescription(findTranslationKey(recipe.getDescription(), translations, "descripción de la receta"));
            recipe.setIngredientsDescription(findTranslationKey(recipe.getIngredientsDescription(), translations, "descripción de ingredientes"));
        }
    }

    private void deleteRecipeTranslations(Recipe recipe) throws IOException
    {
        List<Locale> supportedLocales = nutritionLocaleProvider.getSupportedLocales();

        String nameKey = recipe.getName();
        String descriptionKey = recipe.getDescription();
        String ingredientsDescKey = recipe.getIngredientsDescription();

        for (Locale locale : supportedLocales)
        {
            if (nameKey != null && nameKey.startsWith("recipe."))
            {
                nutritionLocaleService.deleteKeyFromTranslationBundle(nameKey, locale,
                        NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX);
            }
            if (descriptionKey != null && descriptionKey.startsWith("recipe."))
            {
                nutritionLocaleService.deleteKeyFromTranslationBundle(descriptionKey, locale,
                        NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX);
            }
            if (ingredientsDescKey != null && ingredientsDescKey.startsWith("recipe."))
            {
                nutritionLocaleService.deleteKeyFromTranslationBundle(ingredientsDescKey, locale,
                        NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX);
            }

            for (RecipeIngredient ingredient : recipe.getRecipeIngredients())
            {
                String unitKey = ingredient.getUnit();
                if (unitKey != null && unitKey.startsWith("recipeIngredient.unit."))
                {
                    nutritionLocaleService.deleteKeyFromTranslationBundle(unitKey,
                            locale, NutritionLocaleProvider.RECIPE_INGREDIENT_TRANSLATION_BUNDLE_PREFIX);
                }
            }
        }
    }

    private String findTranslationKey(String value, Properties translations, String fieldName)
    {
        return translations.entrySet().stream()
                .filter(entry -> entry.getValue().equals(value))
                .map(entry -> (String) entry.getKey())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró la clave para la " + fieldName));
    }

    private RecipeDTO toRecipeDTO(Recipe recipe, Locale locale)
    {
        String name = translate(recipe.getName(), NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, locale);
        String description = translate(recipe.getDescription(), NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, locale);
        String ingredientsDescription = translate(recipe.getIngredientsDescription(), NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, locale);

        Set<AllergenicDTO> allergenics = recipe.getAllergenics().stream()
                .map(a -> new AllergenicDTO(a.getId(),
                translate(a.getName(), NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX, locale)))
                .collect(Collectors.toSet());

        List<RecipeIngredientDTO> ingredients = recipe.getRecipeIngredients().stream()
                .map(ri -> toRecipeIngredientDTO(ri, name, locale))
                .collect(Collectors.toList());

        List<RecipeElaborationStepDTO> steps = recipe.getElaborationSteps().stream()
                .map(step -> new RecipeElaborationStepDTO(
                step.getId(),
                translate(step.getStepDescription(), NutritionLocaleProvider.RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX, locale),
                step.getStepNumber(),
                new RecipeDTO(recipe.getId(), name)))
                .collect(Collectors.toList());

        return new RecipeDTO(
                recipe.getId(),
                name,
                description,
                ingredientsDescription,
                recipe.getKilocalories(),
                allergenics,
                ingredients,
                steps
        );
    }

    private RecipeIngredientDTO toRecipeIngredientDTO(RecipeIngredient ri, String recipeName, Locale locale)
    {
        Ingredient ingredient = ri.getIngredient();
        String ingredientName = translate(ingredient.getName(), NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX, locale);
        String unit = translate(ri.getUnit(), NutritionLocaleProvider.RECIPE_INGREDIENT_TRANSLATION_BUNDLE_PREFIX, locale);

        IngredientDTO ingredientDTO = new IngredientDTO(ingredient.getId(), ingredientName);
        RecipeDTO recipeDTO = new RecipeDTO(ri.getRecipe().getId(), recipeName);

        return new RecipeIngredientDTO(ri.getId(), recipeDTO, ingredientDTO, ri.getQuantity(), unit);
    }

    private IngredientDTO toIngredientDTO(Ingredient ingredient, Locale locale)
    {
        String translatedName = translate(ingredient.getName(), NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX, locale);
        return new IngredientDTO(ingredient.getId(), translatedName);
    }

    private String translate(String key, String bundlePrefix, Locale locale)
    {
        return nutritionLocaleProvider.getTranslation(key, bundlePrefix, locale);
    }
}